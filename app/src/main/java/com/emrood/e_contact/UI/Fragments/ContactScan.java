package com.emrood.e_contact.UI.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emrood.e_contact.R;
import com.google.zxing.integration.android.IntentIntegrator;

/**
 * Created by Noel Emmanuel Roodly on 11/18/2018.
 */
public class ContactScan extends Fragment {


    View v;
    boolean mUserVisibleHint = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUserVisibleHint(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.contact_scan_fragment, container, false);

//        mUserVisibleHint = getUserVisibleHint();

//        if(mUserVisibleHint){
//            IntentIntegrator integrator = new IntentIntegrator(getActivity());
//            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
//            integrator.setPrompt("Scan en cours");
//            integrator.setCameraId(0);
//            integrator.setBeepEnabled(false);
//            integrator.setBarcodeImageEnabled(false);
//            integrator.initiateScan();
//        }

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getUserVisibleHint()){
            IntentIntegrator integrator = new IntentIntegrator(getActivity());
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
            integrator.setPrompt(getString(R.string.scanning));
            integrator.setCameraId(0);
            integrator.setBeepEnabled(false);
            integrator.setBarcodeImageEnabled(false);
            integrator.initiateScan();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            IntentIntegrator integrator = new IntentIntegrator(getActivity());
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
            integrator.setPrompt(getString(R.string.scanning));
            integrator.setCameraId(0);
            integrator.setBeepEnabled(false);
            integrator.setBarcodeImageEnabled(false);
            integrator.initiateScan();
        }
    }
}
