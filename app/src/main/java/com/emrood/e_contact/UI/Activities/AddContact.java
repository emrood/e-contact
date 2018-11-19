package com.emrood.e_contact.UI.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.emrood.e_contact.App;
import com.emrood.e_contact.Model.Contact;
import com.emrood.e_contact.R;
import com.jackandphantom.circularimageview.CircleImage;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddContact extends AppCompatActivity {


    EditText edtFirstName, edtLastName, edtEntreprise,
            edtPhoneCellular, edtPhoneWork, edtPhoneOther, edtEmailPersonnal,
            edtEmailProfessional, edtEmailOther, edtBirthday, edtNotes;

    CircleImageView ivContactPhoto;
    ScrollView add_contact_scroll;

    Boolean saveContact = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
        init();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.save_contact:
                trySaveContact();
                break;
            case R.id.undo:
                Toast.makeText(this, "quit", Toast.LENGTH_SHORT).show();
                break;
        }


        return super.onOptionsItemSelected(item);
    }


    public void trySaveContact(){
        if(TextUtils.isEmpty(edtFirstName.getText().toString())){
            edtFirstName.setError(getString(R.string.obligation_field));
            saveContact = false;

        }
        if(TextUtils.isEmpty(edtLastName.getText().toString())){
            edtLastName.setError(getString(R.string.obligation_field));
            saveContact = false;
        }

        if(saveContact){
            Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();
            Contact c = new Contact();
            c.setFirst_name(edtFirstName.getText().toString().trim());
            c.setLast_name(edtLastName.getText().toString().trim());
            if(!TextUtils.isEmpty(edtPhoneCellular.getText().toString())){
                c.setCellular_phone(edtPhoneCellular.getText().toString().trim());
            }
            if(!TextUtils.isEmpty(edtBirthday.getText().toString())){
                c.setBirthday(new Date());
            }
            if(!TextUtils.isEmpty(edtEntreprise.getText().toString())){
                c.setEntreprise(edtEntreprise.getText().toString().trim());
            }
            if(!TextUtils.isEmpty(edtEmailPersonnal.getText().toString())){
                c.setPersonal_email(edtEmailPersonnal.getText().toString().trim());
            }
            if(!TextUtils.isEmpty(edtPhoneWork.getText().toString())){
                c.setWork_phone(edtPhoneWork.getText().toString().trim());
            }
            if(!TextUtils.isEmpty(edtEmailProfessional.getText().toString())){
                c.setWork_email(edtEmailProfessional.getText().toString().trim());
            }
            if(!TextUtils.isEmpty(edtNotes.getText().toString())){
                c.setNote(edtNotes.getText().toString().trim());
            }
            if(!TextUtils.isEmpty(edtPhoneOther.getText().toString())){
                c.setOther_phone(edtPhoneOther.getText().toString().trim());
            }
            if(!TextUtils.isEmpty(edtEmailOther.getText().toString())){
                c.setOther_email(edtEmailOther.getText().toString().trim());
            }

            ((App) App.getInstance()).getDaoSession().getContactDao().save(c);
            Intent i = new Intent(AddContact.this, ContactActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.right, R.anim.left);

        }else{
            Toast.makeText(this, "not saved", Toast.LENGTH_SHORT).show();
        }
    }



    public void init(){
        edtBirthday = findViewById(R.id.edtBirthday);
        edtNotes = findViewById(R.id.edtNotes);
        edtFirstName = findViewById(R.id.edtfirst_name);
        edtLastName = findViewById(R.id.edtLastname);
        edtEmailPersonnal = findViewById(R.id.edtEmail);
        edtEmailProfessional = findViewById(R.id.edtWorlMail);
        edtEmailOther = findViewById(R.id.edtOtherMail);
        edtPhoneCellular = findViewById(R.id.edtPhoneCellular);
        edtPhoneWork = findViewById(R.id.edtphoneWork);
        edtPhoneOther = findViewById(R.id.edtPhoneOther);
        edtEntreprise = findViewById(R.id.edtEntreprise);
        ivContactPhoto = findViewById(R.id.ivContactPhoto);
        add_contact_scroll = findViewById(R.id.addContactScroll);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
