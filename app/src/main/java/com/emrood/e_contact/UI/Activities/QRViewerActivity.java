package com.emrood.e_contact.UI.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.emrood.e_contact.Model.Contact;
import com.emrood.e_contact.Model.QRObject;
import com.emrood.e_contact.R;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONObject;

public class QRViewerActivity extends AppCompatActivity {


    ImageView ivQR;
    TextView tvQr;
    String qrInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrviewer);

        ivQR = findViewById(R.id.ivQRCode);
        tvQr = findViewById(R.id.tvQR);


        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try{

            //GET CONTACT FROM PREFERENCE HELPER
            Contact contact = new Contact();
            contact.setFirst_name("LOL");
            contact.setLast_name("LOLO");
            contact.setCellular_phone("369858254");
            QRObject  qrObject = new QRObject(contact);

            Gson json = new Gson();
            qrInfo = json.toJson(qrObject);

            BitMatrix bitMatrix = multiFormatWriter.encode(qrInfo, BarcodeFormat.QR_CODE, 300,300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

            ivQR.setImageBitmap(bitmap);

        }
        catch (WriterException e){
            e.printStackTrace();
        }


    }






}
