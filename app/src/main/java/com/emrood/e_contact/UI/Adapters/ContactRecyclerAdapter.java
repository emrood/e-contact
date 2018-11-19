package com.emrood.e_contact.UI.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.emrood.e_contact.Model.Contact;
import com.emrood.e_contact.R;
import com.emrood.e_contact.UI.Listeners.ContactClickListener;
import com.emrood.e_contact.UI.Listeners.ContactLongClickListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Noel Emmanuel Roodly on 11/18/2018.
 */
public class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactRecyclerAdapter.ViewHolder> {


    private LayoutInflater inflater;
    ArrayList<Contact> contacts;
    ArrayList<Contact> contactFiltered;
    Context mContext;
    ContactClickListener mContactClickListener;
    ContactLongClickListener mContactLongClickListener;
    private int lastPosition = -1;

    public ContactRecyclerAdapter(ArrayList<Contact> contacts, Context mContext) {
        this.contacts = contacts;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_contact, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String firstname = contacts.get(i).getFirst_name();
        String lastnma = contacts.get(i).getLast_name();
        String phone = " ";
        String email = " ";
        if(!TextUtils.isEmpty(contacts.get(i).getCellular_phone())){
            phone = contacts.get(i).getCellular_phone();
        }else if(!TextUtils.isEmpty(contacts.get(i).getWork_phone())){
            phone = contacts.get(i).getWork_phone();
        }else if(!TextUtils.isEmpty(contacts.get(i).getOther_phone())){
            phone = contacts.get(i).getOther_phone();
        }
        if(!TextUtils.isEmpty(contacts.get(i).getPersonal_email())){
            email = contacts.get(i).getPersonal_email();
        }else if(!TextUtils.isEmpty(contacts.get(i).getWork_email())){
            email = contacts.get(i).getWork_email();
        }else if(!TextUtils.isEmpty(contacts.get(i).getOther_email())){
            email = contacts.get(i).getOther_email();
        }

        viewHolder.tvName.setText(lastnma + " " + firstname);
        viewHolder.tvEmail.setText(email);
        viewHolder.tvPhone.setText(phone);

//        if(!TextUtils.isEmpty(contacts.get(i).getPhoto())){
//            Glide.with(mContext).load("").into(viewHolder.tvPhoto);
//        }

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView tvName, tvPhone, tvEmail;
        CircleImageView tvPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvContactName);
            tvPhone = itemView.findViewById(R.id.tvPhoneNumber);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvPhoto = itemView.findViewById(R.id.ivContactPhoto);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mContactClickListener != null) {
                mContactClickListener.onItemClick(v, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(mContactLongClickListener != null){
                mContactLongClickListener.onItemLongClick(v, getAdapterPosition());
            }
            return true;
        }
    }


    public Contact getContact(int id){

        try{
            return contacts.get(id);
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public void setFilter(ArrayList<Contact> arrayList) {
        //arrayList = new ArrayList<>(); // remove this line
        contacts.clear(); // add this so that it will clear old data
        contacts.addAll(arrayList);
        notifyDataSetChanged();
    }

    public void newAddeddata(ArrayList<Contact> contactList){

        if (contactList != null) {
            contacts.addAll(contactList);
        }

        notifyDataSetChanged();
    }

    public void newAddeddata(List<Contact> contactList){

        if (contactList != null) {
            contacts.addAll(contactList);
        }

        notifyDataSetChanged();
    }

    public void removeContact(int position) {
        contacts.remove(position);
        notifyItemRemoved(position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
