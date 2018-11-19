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

import com.emrood.e_contact.Model.Contact;
import com.emrood.e_contact.R;

import java.util.ArrayList;

/**
 * Created by Noel Emmanuel Roodly on 11/18/2018.
 */
public class ContactList extends Fragment {


    View v;
    RecyclerView contactList;
    SearchView searchView;
    ArrayList<Contact> contacts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.contact_list_fragment, container, false);
        contactList = v.findViewById(R.id.contactList);
        searchView = v.findViewById(R.id.contactSearch);



        return v;
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
