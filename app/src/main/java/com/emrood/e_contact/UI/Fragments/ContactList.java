package com.emrood.e_contact.UI.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.Lifecycle;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.emrood.e_contact.App;
import com.emrood.e_contact.Model.Contact;
import com.emrood.e_contact.Model.ContactDao;
import com.emrood.e_contact.R;
import com.emrood.e_contact.UI.Activities.AddContact;
import com.emrood.e_contact.UI.Activities.ContactActivity;
import com.emrood.e_contact.UI.Activities.QRViewerActivity;
import com.emrood.e_contact.UI.Adapters.ContactRecyclerAdapter;
import com.emrood.e_contact.UI.Listeners.ContactClickListener;
import com.emrood.e_contact.UI.Listeners.ContactLongClickListener;
import com.emrood.e_contact.UI.Listeners.SmsListener;
import com.emrood.e_contact.UI.Utils.RecyclerItemTouchHelper;
import com.emrood.e_contact.UI.Utils.SwipeHelper;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Noel Emmanuel Roodly on 11/18/2018.
 */
public class ContactList extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener,
        ContactClickListener,
        ContactLongClickListener{


    View v;
    RecyclerView contactList;
    SearchView searchView;
    ArrayList<Contact> contacts;
    ArrayList<Contact> filterContacts;
    ArrayList<Contact> phoneBookContact;
    ContactRecyclerAdapter mContactAdapter;
    public ProgressDialog progressDialog;
    ContactLongClickListener mLongClickListener;
    ContactClickListener mClickListener;
    String smsNumber = null;
    FragmentManager fm;
    public final String regexStr = "^[+]?[0-9]{8,18}$";

    private com.emrood.e_contact.Utils.PreferenceManager prefManager;

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//        ContactActivity contactActivity;
//
//        if(context instanceof ContactActivity){
//            contactActivity = (ContactActivity) context;
//            try {
//                mClickListener = (ContactClickListener) contactActivity;
//                mLongClickListener = (ContactLongClickListener) contactActivity;
//            }catch (ClassCastException e){
//                throw new ClassCastException(getActivity().toString()+ " must implement ONProductClickListner");
//            }
//
//        }
//
//
//    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mClickListener = (ContactClickListener) getActivity();
            mLongClickListener = (ContactLongClickListener) getActivity();
        }catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString()+ " must implement ONProductClickListner");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.contact_list_fragment, container, false);
        contactList = v.findViewById(R.id.contactList);
        searchView = v.findViewById(R.id.contactSearch);
        LinearLayoutManager mLinearLayout = new LinearLayoutManager(getActivity());



        prefManager = new com.emrood.e_contact.Utils.PreferenceManager(getActivity());

        contacts = new ArrayList<>();
        filterContacts = new ArrayList<>();
        mContactAdapter = new ContactRecyclerAdapter(contacts, getContext());
        contactList.setLayoutManager(mLinearLayout);
        contactList.setAdapter(mContactAdapter);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(getString(R.string.contacts_loading));
        progressDialog.setMessage(getString(R.string.contacts_loading_message));

//        contacts.addAll(((App) App.getInstance()).getDaoSession().getContactDao().loadAll());
//        mContactAdapter.notifyDataSetChanged();

        mContactAdapter.newAddeddata(((App) App.getInstance()).getDaoSession().getContactDao().queryBuilder().where(ContactDao.Properties.IsSecret.eq(false)).list());
        mContactAdapter.setContactClickListener(ContactList.this);
        mContactAdapter.setmContactLongClickListener(ContactList.this);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                contacts.clear();
                contactList.invalidate();
                contacts.addAll(((App) App.getInstance()).getDaoSession().getContactDao().queryBuilder().where(ContactDao.Properties.IsSecret.eq(false)).whereOr(ContactDao.Properties.First_name.like("%" + s + "%"), ContactDao.Properties.Last_name.like("%" + s + "%"), ContactDao.Properties.Cellular_phone.like("%" + s + "%")).list());
                Log.d("INFO", contacts.toString());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mContactAdapter.notifyDataSetChanged();
                    }
                }, 800);
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                contacts.clear();
                contactList.invalidate();
                contacts.addAll(((App) App.getInstance()).getDaoSession().getContactDao().queryBuilder().where(ContactDao.Properties.IsSecret.eq(false)).list());
                mContactAdapter.notifyDataSetChanged();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mContactAdapter.notifyDataSetChanged();
                        contactList.smoothScrollToPosition(0);
                    }
                }, 800);
                return false;
            }
        });



//        ItemTouchHelper.SimpleCallback itemTouchCallBack = new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
//        new ItemTouchHelper(itemTouchCallBack).attachToRecyclerView(contactList);


        ItemTouchHelper.SimpleCallback itemTouchCallBack = new SwipeHelper(getActivity(), contactList) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Delete",
                        R.drawable.ic_delete_white_24dp,
                        Color.parseColor("#FF3C30"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                Toast.makeText(getActivity(), getString(R.string.delete_contact) + " " + contacts.get(pos).getFirst_name(), Toast.LENGTH_SHORT).show();
                                delete(pos);
                            }
                        }
                ));

                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "e-sms",
                        0,
                        Color.parseColor("#FF9502"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                sendSms(pos);
                            }
                        }
                ));
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Call",
                        0,
                        Color.parseColor("#C7C7CB"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                callNumber(pos);
                            }
                        }
                ));
            }
        };

        new ItemTouchHelper(itemTouchCallBack).attachToRecyclerView(contactList);
//        new ItemTouchHelper(swipeHelper).attachToRecyclerView(contactList);


        if(!prefManager.isPhoneBookSynced()){
            getContactList();
        }

        return v;
    }


    private void getContactList() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.show();
                        }
                    });

                    phoneBookContact = new ArrayList<>();
                    ContentResolver cr = getActivity().getContentResolver();
                    Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                            null, null, null, null);

                    if ((cur != null ? cur.getCount() : 0) > 0) {
                        while (cur != null && cur.moveToNext()) {

                            Contact cc = new Contact();
                            String id = cur.getString(
                                    cur.getColumnIndex(ContactsContract.Contacts._ID));
                            String name = cur.getString(cur.getColumnIndex(
                                    ContactsContract.Contacts.DISPLAY_NAME));
                            String last_name = cur.getString(cur.getColumnIndex(
                                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
                            String other_name = cur.getString(cur.getColumnIndex(
                                    ContactsContract.Contacts.DISPLAY_NAME_ALTERNATIVE));

                            if (cur.getInt(cur.getColumnIndex(
                                    ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                                Cursor pCur = cr.query(
                                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                        null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                        new String[]{id}, null);
                                while (pCur.moveToNext()) {
                                    String phoneNo = pCur.getString(pCur.getColumnIndex(
                                            ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    int type = pCur.getInt(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                                    switch (type) {
                                        case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                            cc.setOther_phone(phoneNo);
                                            break;
                                        case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                            cc.setCellular_phone(phoneNo);
                                            break;
                                        case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                            cc.setWork_phone(phoneNo);
                                            break;
                                        default:
                                            cc.setOther_phone(phoneNo);
                                            break;
                                    }

                                    Log.i("INFO", "Name: " + name);
                                    Log.i("INFO", "Phone Number: " + phoneNo);
                                }
                                pCur.close();
                            }

                            cc.setFirst_name(name);
                            cc.setLast_name(last_name);
                            cc.setIsFav(false);
                            cc.setIsSecret(false);

                            phoneBookContact.add(cc);
                        }
                    }
                    if (cur != null) {
                        cur.close();
                    }

                    if (phoneBookContact != null) {
                        if (!phoneBookContact.isEmpty()) {
                            for (Contact contact : phoneBookContact) {
                                try {
                                    ((App) App.getInstance()).getDaoSession().getContactDao().save(contact);
                                } catch (Exception e) {

                                }
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mContactAdapter.newAddeddata(((App) App.getInstance()).getDaoSession().getContactDao().loadAll());
                                    mContactAdapter.notifyDataSetChanged();
                                }
                            });
                            prefManager.setPhoneBookSynced(true);
                        }
                    }

                }
            });

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                }
            }, 8000);

        }

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contacts = new ArrayList<>();
        fm = getFragmentManager();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof ContactRecyclerAdapter.ViewHolder) {
//            final int index = viewHolder.getAdapterPosition();
//            final Contact contact = contacts.get(index);
//            String regexStr = "^[+]?[0-9]{8,18}$";
            String number = mContactAdapter.returnNumber(viewHolder.getAdapterPosition());
            if(number != null){
                if(number.matches(regexStr)){
                    Toast.makeText(getActivity(), number, Toast.LENGTH_SHORT).show();
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+number));
                    if (callIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(callIntent);
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            contacts.clear();
                            contactList.invalidate();
                            contacts.addAll(((App) App.getInstance()).getDaoSession().getContactDao().loadAll());
                            mContactAdapter.notifyDataSetChanged();
                        }
                    }, 1000);
                }else{
                    Toast.makeText(getActivity(), "not good", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            contacts.clear();
                            contactList.invalidate();
                            contacts.addAll(((App) App.getInstance()).getDaoSession().getContactDao().loadAll());
                            mContactAdapter.notifyDataSetChanged();
                        }
                    }, 1000);
                }
            }else{
                Toast.makeText(getActivity(), "Nummero invalide", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        contacts.clear();
                        contactList.invalidate();
                        contacts.addAll(((App) App.getInstance()).getDaoSession().getContactDao().loadAll());
                        mContactAdapter.notifyDataSetChanged();
                    }
                }, 1000);
            }

        }
    }


    public void callNumber(int pos){
        String number = mContactAdapter.returnNumber(pos);
        if(number != null){
            if(number.matches(regexStr)){
                Toast.makeText(getActivity(), number, Toast.LENGTH_SHORT).show();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+number));
                if (callIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(callIntent);
                }

            }else{
                Toast.makeText(getActivity(), "not good", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getActivity(), "Nummero invalide", Toast.LENGTH_SHORT).show();

        }
    }

    public void sendSms(int pos){

        smsNumber = mContactAdapter.returnNumber(pos);
        if(smsNumber != null){
            if(smsNumber.matches(regexStr)){
                Esms e = Esms.newInstance(smsNumber);
                e.show(fm, "e-Sms");
            }else{
                Toast.makeText(getActivity(), R.string.invalide_number, Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getActivity(), R.string.invalide_number, Toast.LENGTH_SHORT).show();

        }
    }


    public void reallySendSms(String content){
        Uri smsUri = Uri.parse("tel:" + smsNumber);
        Intent intent = new Intent(Intent.ACTION_VIEW, smsUri);
        intent.putExtra("address", smsNumber);
        intent.putExtra("sms_body", content);
        intent.setType("vnd.android-dir/mms-sms");//here setType will set the previous data null.
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }



    public interface OnContactClickListener{
        public void onContactClick(Contact contact);
    }


    public interface OnContactLongClickListener{
        public void onContactLongClick(Contact contact);
    }


    public void delete(int pos){
        ((App) App.getInstance()).getDaoSession().getContactDao().delete(contacts.get(pos));
        mContactAdapter.removeContact(pos);
    }

    public void editContact(int pos){
        Contact c = contacts.get(pos);
        Intent i = new Intent(getActivity(), AddContact.class);
        i.putExtra("contactEdit", c);
        startActivity(i);
        getActivity().overridePendingTransition(R.anim.right, R.anim.left);
    }

    @Override
    public void onItemClick(View view, int positon) {
        Toast.makeText(getActivity(), "click " + positon, Toast.LENGTH_SHORT).show();
        mClickListener.onItemClick(view, positon);
        if(v != null){

        }

    }

    public void onContactClick(Contact contact){
//        Toast.makeText(getActivity(), "longclickz ", Toast.LENGTH_SHORT).show();

    }

    public void onContactLongClick(Contact contact){
//        Toast.makeText(getActivity(), "longclickz", Toast.LENGTH_SHORT).show();

    }

    public void makeFavorite(int pos){

    }

    public void makeSecret(int pos){

    }

    @Override
    public void onItemLongClick(View v, int position) {
//        Toast.makeText(getActivity(), "longclick " + position, Toast.LENGTH_SHORT).show();
        if( v != null){
            final String[] fonts = {getString(R.string.edit), getString(R.string.add_fav), getString(R.string.secret_contact_add), getString(R.string.generate_contact_qr)};
            final int p = position;
            final Contact c = contacts.get(position);
//            Toast.makeText(getActivity(), c.getFirst_name(), Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Select a text size");
            builder.setItems(fonts, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (getString(R.string.edit).equals(fonts[which])){
                        Toast.makeText(getActivity(),"Edit", Toast.LENGTH_SHORT).show();
//                        editContact(p);
                        Intent i = new Intent(getActivity(), AddContact.class);
                        i.putExtra("contactEdit", c);
                        startActivity(i);
                        getActivity().overridePendingTransition(R.anim.right, R.anim.left);
                    }
                    else if (getString(R.string.add_fav).equals(fonts[which])){
                        Toast.makeText(getActivity(),"favorite", Toast.LENGTH_SHORT).show();
                        c.setIsFav(true);
                        ((App) App.getInstance()).getDaoSession().getContactDao().update(c);
                        mContactAdapter.makeContactFav(p);
                    }
                    else if (getString(R.string.secret_contact_add).equals(fonts[which])){
                        Toast.makeText(getActivity(),"secret", Toast.LENGTH_SHORT).show();
                        c.setIsSecret(true);
                        ((App) App.getInstance()).getDaoSession().getContactDao().update(c);
                        mContactAdapter.makeContactSecret(p);
                    }
                    else if (getString(R.string.generate_contact_qr).equals(fonts[which])){
                        Toast.makeText(getActivity(),"e-QR", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getActivity(), QRViewerActivity.class);
                        i.putExtra("e-contact", c);
                        startActivity(i);
                    }

                }
            });
            builder.show();
        }
    }
}
