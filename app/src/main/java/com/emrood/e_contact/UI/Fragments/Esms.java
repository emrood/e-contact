package com.emrood.e_contact.UI.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.emrood.e_contact.R;
import com.emrood.e_contact.UI.Listeners.SmsListener;

/**
 * Created by Noel Emmanuel Roodly on 11/21/2018.
 */
public class Esms extends DialogFragment {


    View root;
    EditText smsBody;
    ImageButton btnSend;
    String phone = null;

    public static Esms newInstance(String number){
        Esms e = new Esms();
        Bundle args = new Bundle();
        args.putString("phone", number);
        e.setArguments(args);
        return e;
    }

    public Esms() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_sms, null);

        smsBody = root.findViewById(R.id.edtSmsBody);
        btnSend = root.findViewById(R.id.btnSendSms);


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(smsBody.getText().toString())){
//                    SmsListener listener = (SmsListener) getTargetFragment();
                    SmsListener listener = (SmsListener) getActivity();
                    listener.onSmsContent(smsBody.getText().toString().trim(), phone);
                    dismiss();
                }else{
                    smsBody.setError(getString(R.string.empty_field));
                }

            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        phone = getArguments().getString("phone");
//        Toast.makeText(getContext(), phone, Toast.LENGTH_SHORT).show();
        super.onViewCreated(view, savedInstanceState);
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
