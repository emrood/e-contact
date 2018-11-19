package com.emrood.e_contact.UI.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emrood.e_contact.Model.Contact;
import com.emrood.e_contact.UI.Listener.ContactClickListener;

import java.util.ArrayList;

/**
 * Created by Noel Emmanuel Roodly on 11/18/2018.
 */
public class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactRecyclerAdapter.ViewHolder> {


    private LayoutInflater inflater;
    ArrayList<Contact> contacts;
    ArrayList<Contact> contactFiltered;
    Context mContext;
    ContactClickListener mContactClickListener;


    public ContactRecyclerAdapter(ArrayList<Contact> contacts, Context mContext) {
        this.contacts = contacts;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mContactClickListener != null) {
                mContactClickListener.onItemClick(v, getAdapterPosition());
            }
        }
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
}
