package com.emrood.e_contact.UI.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.emrood.e_contact.R;
import com.emrood.e_contact.UI.Activities.ContactActivity;
import com.emrood.e_contact.Utils.LanguageHelper;
import com.emrood.e_contact.Utils.PreferenceManager;

/**
 * Created by Noel Emmanuel Roodly on 11/23/2018.
 */
public class Settings extends DialogFragment {
    View root;

    Spinner lang;
    Button btnSetLang;
    private PreferenceManager prefManager;
    String language;
    ProgressDialog mprogressDialog;
    ContactActivity contactActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        
        root = inflater.inflate(R.layout.fragment_settings, null);
        
        lang = root.findViewById(R.id.spinLang);
        btnSetLang = root.findViewById(R.id.btnSetLang);

        prefManager = new PreferenceManager(getActivity());
        mprogressDialog = new ProgressDialog(getContext());
        mprogressDialog.setTitle(getString(R.string.set));
        mprogressDialog.setMessage(getString(R.string.wait_message));
        contactActivity = (ContactActivity) getActivity();
        
        btnSetLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefManager.setLangSelected(lang.getSelectedItem().toString());
                Toast.makeText(contactActivity, lang.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                mprogressDialog.show();
                Toast.makeText(contactActivity, R.string.restart_text, Toast.LENGTH_SHORT).show();
                try {
                    LanguageHelper.changeLocal(getResources(), language);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mprogressDialog.dismiss();
                        dismiss();
                        contactActivity.restartApp();

                    }
                }, 3000);
                
            }
        });
        
        return root; 
    }
}
