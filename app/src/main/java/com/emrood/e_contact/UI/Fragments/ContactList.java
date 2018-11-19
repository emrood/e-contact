package com.emrood.e_contact.UI.Fragments;

import android.arch.lifecycle.Lifecycle;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emrood.e_contact.App;
import com.emrood.e_contact.Model.Contact;
import com.emrood.e_contact.Model.ContactDao;
import com.emrood.e_contact.R;
import com.emrood.e_contact.UI.Adapters.ContactRecyclerAdapter;

import java.util.ArrayList;

/**
 * Created by Noel Emmanuel Roodly on 11/18/2018.
 */
public class ContactList extends Fragment {


    View v;
    RecyclerView contactList;
    SearchView searchView;
    ArrayList<Contact> contacts;
    ArrayList<Contact> filterContacts;
    ContactRecyclerAdapter mContactAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.contact_list_fragment, container, false);
        contactList = v.findViewById(R.id.contactList);
        searchView = v.findViewById(R.id.contactSearch);
        contacts = new ArrayList<>();
        filterContacts = new ArrayList<>();
        mContactAdapter = new ContactRecyclerAdapter(contacts, getContext());
        contactList.setAdapter(mContactAdapter);

        contacts.addAll(((App) App.getInstance()).getDaoSession().getContactDao().loadAll());
        mContactAdapter.notifyDataSetChanged();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mContactAdapter.newAddeddata(((App) App.getInstance()).getDaoSession().getContactDao().queryBuilder().where(
                        ContactDao.Properties.First_name.like(s),
                        ContactDao.Properties.Last_name.like(s),
                        ContactDao.Properties.Personal_email.like(s),
                        ContactDao.Properties.Cellular_phone.like(s)).list());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mContactAdapter.newAddeddata(((App) App.getInstance()).getDaoSession().getContactDao().queryBuilder().where(
                        ContactDao.Properties.First_name.like(s),
                        ContactDao.Properties.Last_name.like(s),
                        ContactDao.Properties.Personal_email.like(s),
                        ContactDao.Properties.Cellular_phone.like(s)).list());
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                contacts.addAll(((App) App.getInstance()).getDaoSession().getContactDao().loadAll());
                mContactAdapter.notifyDataSetChanged();
                return false;
            }
        });



        return v;
    }


    public void populateContactList(){

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contacts = new ArrayList<>();

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
}
