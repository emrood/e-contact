package com.emrood.e_contact.UI.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.emrood.e_contact.App;
import com.emrood.e_contact.BuildConfig;
import com.emrood.e_contact.Model.Contact;
import com.emrood.e_contact.R;
import com.emrood.e_contact.Utils.Utils;
import com.example.circulardialog.CDialog;
import com.example.circulardialog.extras.CDConstants;
import com.jackandphantom.circularimageview.CircleImage;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddContact extends AppCompatActivity {


    EditText edtFirstName, edtLastName, edtEntreprise,
            edtPhoneCellular, edtPhoneWork, edtPhoneOther, edtEmailPersonnal,
            edtEmailProfessional, edtEmailOther, edtBirthday, edtNotes;

    CircleImageView ivContactPhoto;
    ScrollView add_contact_scroll;

    Boolean saveContact = true;
    Contact contact;
    public  Calendar myCalendar;
    public DatePickerDialog.OnDateSetListener date;
    Date d;
    String photoPath = null;
    static final int REQUEST_IMAGE_CAPTURE = 50;
    static final int IMAGE_FROM_GALERY = 82;
    Uri imageUri;
    public File avatarFile = null;
    public Bitmap bitmap = null;
    public String photoFileName = "photo.jpg";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    File photoFile;
    public final String APP_TAG = "emrood";
    File dcimDir;
    Uri uri;
    String photo_name;
    Contact c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.new_contact_text);
        init();

        try{
            contact = (Contact) getIntent().getSerializableExtra("contactEdit");
            Log.d("INFO", contact.getFirst_name());
        }catch (Exception e){
            Log.d("ERROR", e.getMessage());
        }

        c = new Contact();

        if(contact != null){
            actionBar.setTitle(R.string.edit_action_title);
            inflateUIWithContactInfo(contact);
        }


    }

    private void inflateUIWithContactInfo(Contact contact) {
        edtFirstName.setText(contact.getFirst_name());
        edtLastName.setText(contact.getLast_name());
        edtEntreprise.setText(contact.getEntreprise());
        edtPhoneCellular.setText(contact.getCellular_phone());
        edtPhoneWork.setText(contact.getWork_phone());
        edtPhoneOther.setText(contact.getOther_phone());
        edtEmailPersonnal.setText(contact.getPersonal_email());
        edtEmailProfessional.setText(contact.getWork_email());
        edtEmailOther.setText(contact.getOther_email());
        edtNotes.setText(contact.getNote());

        if(!TextUtils.isEmpty(contact.getPhoto())){
            Bitmap takenImage = BitmapFactory.decodeFile(contact.getPhoto());
            updateAvatar(takenImage);
//            ivContactPhoto.setVisibility(View.VISIBLE);
        }

        try{
            edtBirthday.setText(String.valueOf(new SimpleDateFormat("dd MMM yyyy", Locale.FRANCE).format(contact.getBirthday())));
        }catch (Exception e){

        }

    }

    private void selectImage() {

        if(contact != null){
            final CharSequence[] options = {getString(R.string.choose_in_galery), getString(R.string.camera), getString(R.string.delete_picture),getString(R.string.annule) };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.photo_contact);
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {

                    if (options[item].equals(getString(R.string.choose_in_galery)))
                    {
                        changePicture();
                    }else if(options[item].equals(getString(R.string.camera))){
                        startCamera();
                    }else if(options[item].equals( getString(R.string.delete_picture))){
                        contact.setPhoto(null);
                        ((App) App.getInstance()).getDaoSession().getContactDao().update(contact);
                        ivContactPhoto.setImageDrawable(getDrawable(R.drawable.add_contact));
                    }
                    else if (options[item].equals(getString(R.string.annule))) {
                        dialog.dismiss();
                    }
                }

            });
            builder.show();

        }else{
            final CharSequence[] options = {getString(R.string.choose_in_galery), getString(R.string.camera), getString(R.string.annule) };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.photo_contact);
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {

                    if (options[item].equals(getString(R.string.choose_in_galery)))
                    {
                        changePicture();
                    }else if(options[item].equals(getString(R.string.camera))){
                        startCamera();
                    }
                    else if (options[item].equals(getString(R.string.annule))) {
                        dialog.dismiss();
                    }
                }

            });
            builder.show();

        }

    }

    public void startCamera() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        photoFile = getPhotoFileUri(photoFileName);
//        if(Build.VERSION.SDK_INT >= 24){
//            Uri fileProvider = FileProvider.getUriForFile(AddContact.this, "com.emrood.fileprovider", photoFile);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
//            if (intent.resolveActivity(getPackageManager()) != null) {
//                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
//            }
//        }

        dcimDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        dcimDir.mkdir();
        photo_name = System.currentTimeMillis()+"_profil.jpg";
        uri = Uri.fromFile(new File(dcimDir, photo_name));
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }



    }


    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM), APP_TAG);

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }
        File file = new File(fileName);
        photoPath = fileName;
        photoFileName = fileName;
        return file;
    }


    public void changePicture(){
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, getString(R.string.select_picture)), IMAGE_FROM_GALERY );
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
                if(contact == null){
                    trySaveContact();
                }else{
                    tryUpdateContact();
                }
                break;
            case R.id.undo:
//                Toast.makeText(this, "quit", Toast.LENGTH_SHORT).show();
                if(contact == null){
                    finish();
                }else{
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.edition)
                            .setMessage(getString(R.string.validate_edit_annulation) + " " + contact.getFirst_name() + " ?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    finish();
                                }})
                            .setNegativeButton(R.string.non, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();

                }

                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == IMAGE_FROM_GALERY && resultCode == RESULT_OK) {
//            Toast.makeText(this, "GALERY", Toast.LENGTH_SHORT).show();
            try {
                if(data != null){
                    imageUri = data.getData();
//                    photoPath = data.getDataString();
                    photoPath = getUriRealPathAboveKitkat(this, imageUri);

                    if(contact != null){
                        contact.setPhoto(photoPath);
                    }else{
                        c.setPhoto(photoPath);
                    }
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    updateAvatar(bitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
//                Toast.makeText(this, "CAMERA", Toast.LENGTH_SHORT).show();
                photoPath = uri.getPath();
                if(contact != null){
                    contact.setPhoto(photoPath);
                }else{
                    c.setPhoto(photoPath);
                }
//                getPhotoFileUri(photoPath);
                Bitmap takenImage = BitmapFactory.decodeFile(photoPath);
                updateAvatar(takenImage);
            } else { // Result was a failure
                Toast.makeText(this, "Pas de photo", Toast.LENGTH_SHORT).show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private String getUriRealPathAboveKitkat(Context ctx, Uri uri)
    {
        String ret = "";

        if(ctx != null && uri != null) {

            if(isContentUri(uri))
            {
                if(isGooglePhotoDoc(uri.getAuthority()))
                {
                    ret = uri.getLastPathSegment();
                }else {
                    ret = getImageRealPath(getContentResolver(), uri, null);
                }
            }else if(isFileUri(uri)) {
                ret = uri.getPath();
            }else if(isDocumentUri(ctx, uri)){

                // Get uri related document id.
                String documentId = DocumentsContract.getDocumentId(uri);

                // Get uri authority.
                String uriAuthority = uri.getAuthority();

                if(isMediaDoc(uriAuthority))
                {
                    String idArr[] = documentId.split(":");
                    if(idArr.length == 2)
                    {
                        // First item is document type.
                        String docType = idArr[0];

                        // Second item is document real id.
                        String realDocId = idArr[1];

                        // Get content uri by document type.
                        Uri mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        if("image".equals(docType))
                        {
                            mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        }else if("video".equals(docType))
                        {
                            mediaContentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        }else if("audio".equals(docType))
                        {
                            mediaContentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        }

                        // Get where clause with real document id.
                        String whereClause = MediaStore.Images.Media._ID + " = " + realDocId;

                        ret = getImageRealPath(getContentResolver(), mediaContentUri, whereClause);
                    }

                }else if(isDownloadDoc(uriAuthority))
                {
                    // Build download uri.
                    Uri downloadUri = Uri.parse("content://downloads/public_downloads");

                    // Append download document id at uri end.
                    Uri downloadUriAppendId = ContentUris.withAppendedId(downloadUri, Long.valueOf(documentId));

                    ret = getImageRealPath(getContentResolver(), downloadUriAppendId, null);

                }else if(isExternalStoreDoc(uriAuthority))
                {
                    String idArr[] = documentId.split(":");
                    if(idArr.length == 2)
                    {
                        String type = idArr[0];
                        String realDocId = idArr[1];

                        if("primary".equalsIgnoreCase(type))
                        {
                            ret = Environment.getExternalStorageDirectory() + "/" + realDocId;
                        }
                    }
                }
            }
        }

        return ret;
    }


    private boolean isAboveKitKat()
    {
        boolean ret = false;
        ret = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        return ret;
    }

    /* Check whether this uri represent a document or not. */
    private boolean isDocumentUri(Context ctx, Uri uri)
    {
        boolean ret = false;
        if(ctx != null && uri != null) {
            ret = DocumentsContract.isDocumentUri(ctx, uri);
        }
        return ret;
    }

    /* Check whether this uri is a content uri or not.
     *  content uri like content://media/external/images/media/1302716
     *  */
    private boolean isContentUri(Uri uri)
    {
        boolean ret = false;
        if(uri != null) {
            String uriSchema = uri.getScheme();
            if("content".equalsIgnoreCase(uriSchema))
            {
                ret = true;
            }
        }
        return ret;
    }

    /* Check whether this uri is a file uri or not.
     *  file uri like file:///storage/41B7-12F1/DCIM/Camera/IMG_20180211_095139.jpg
     * */
    private boolean isFileUri(Uri uri)
    {
        boolean ret = false;
        if(uri != null) {
            String uriSchema = uri.getScheme();
            if("file".equalsIgnoreCase(uriSchema))
            {
                ret = true;
            }
        }
        return ret;
    }


    /* Check whether this document is provided by ExternalStorageProvider. */
    private boolean isExternalStoreDoc(String uriAuthority)
    {
        boolean ret = false;

        if("com.android.externalstorage.documents".equals(uriAuthority))
        {
            ret = true;
        }

        return ret;
    }

    /* Check whether this document is provided by DownloadsProvider. */
    private boolean isDownloadDoc(String uriAuthority)
    {
        boolean ret = false;

        if("com.android.providers.downloads.documents".equals(uriAuthority))
        {
            ret = true;
        }

        return ret;
    }

    /* Check whether this document is provided by MediaProvider. */
    private boolean isMediaDoc(String uriAuthority)
    {
        boolean ret = false;

        if("com.android.providers.media.documents".equals(uriAuthority))
        {
            ret = true;
        }

        return ret;
    }

    /* Check whether this document is provided by google photos. */
    private boolean isGooglePhotoDoc(String uriAuthority)
    {
        boolean ret = false;

        if("com.google.android.apps.photos.content".equals(uriAuthority))
        {
            ret = true;
        }

        return ret;
    }

    /* Return uri represented document file real local path.*/
    private String getImageRealPath(ContentResolver contentResolver, Uri uri, String whereClause)
    {
        String ret = "";

        // Query the uri with condition.
        Cursor cursor = contentResolver.query(uri, null, whereClause, null, null);

        if(cursor!=null)
        {
            boolean moveToFirst = cursor.moveToFirst();
            if(moveToFirst)
            {

                // Get columns name by uri type.
                String columnName = MediaStore.Images.Media.DATA;

                if( uri==MediaStore.Images.Media.EXTERNAL_CONTENT_URI )
                {
                    columnName = MediaStore.Images.Media.DATA;
                }else if( uri==MediaStore.Audio.Media.EXTERNAL_CONTENT_URI )
                {
                    columnName = MediaStore.Audio.Media.DATA;
                }else if( uri==MediaStore.Video.Media.EXTERNAL_CONTENT_URI )
                {
                    columnName = MediaStore.Video.Media.DATA;
                }

                // Get column index.
                int imageColumnIndex = cursor.getColumnIndex(columnName);

                // Get column value which is the uri related file local path.
                ret = cursor.getString(imageColumnIndex);
            }
        }

        return ret;
    }

    private void updateAvatar(Bitmap bitmap) {
        ivContactPhoto.setImageBitmap(bitmap);
    }

    public void tryUpdateContact(){
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
            contact.setFirst_name(edtFirstName.getText().toString().trim());
            contact.setLast_name(edtLastName.getText().toString().trim());
            if(!TextUtils.isEmpty(edtPhoneCellular.getText().toString())){
                contact.setCellular_phone(edtPhoneCellular.getText().toString().trim());
            }
            if(!TextUtils.isEmpty(edtBirthday.getText().toString())){
                contact.setBirthday(new Date());
            }
            if(!TextUtils.isEmpty(edtEntreprise.getText().toString())){
                contact.setEntreprise(edtEntreprise.getText().toString().trim());
            }
            if(!TextUtils.isEmpty(edtEmailPersonnal.getText().toString())){
                contact.setPersonal_email(edtEmailPersonnal.getText().toString().trim());
            }
            if(!TextUtils.isEmpty(edtPhoneWork.getText().toString())){
                contact.setWork_phone(edtPhoneWork.getText().toString().trim());
            }
            if(!TextUtils.isEmpty(edtEmailProfessional.getText().toString())){
                contact.setWork_email(edtEmailProfessional.getText().toString().trim());
            }
            if(!TextUtils.isEmpty(edtNotes.getText().toString())){
                contact.setNote(edtNotes.getText().toString().trim());
            }
            if(!TextUtils.isEmpty(edtPhoneOther.getText().toString())){
                contact.setOther_phone(edtPhoneOther.getText().toString().trim());
            }
            if(!TextUtils.isEmpty(edtEmailOther.getText().toString())){
                contact.setOther_email(edtEmailOther.getText().toString().trim());
            }

            if(d != null){
                contact.setBirthday(d);
            }


            ((App) App.getInstance()).getDaoSession().getContactDao().update(contact);

            new CDialog(this).createAlert(getString(R.string.contact_update),
                    CDConstants.SUCCESS,   // Type of dialog
                    CDConstants.LARGE)    //  size of dialog
                    .setAnimation(CDConstants.SCALE_FROM_BOTTOM_TO_TOP)     //  Animation for enter/exit
                    .setDuration(1500)   // in milliseconds
                    .setTextSize(CDConstants.NORMAL_TEXT_SIZE)  // CDConstants.LARGE_TEXT_SIZE, CDConstants.NORMAL_TEXT_SIZE
                    .show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(AddContact.this, ContactActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.right, R.anim.left);
                }
            }, 1500);



        }else{
            Toast.makeText(this, "not saved", Toast.LENGTH_SHORT).show();
        }
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
//            Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();

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

            if(d != null){
                c.setBirthday(d);
            }

            c.setIsFav(false);
            c.setIsSecret(false);

            ((App) App.getInstance()).getDaoSession().getContactDao().save(c);

            new CDialog(this).createAlert(getString(R.string.contact_saved),
                    CDConstants.SUCCESS,   // Type of dialog
                    CDConstants.LARGE)    //  size of dialog
                    .setAnimation(CDConstants.SCALE_FROM_BOTTOM_TO_TOP)     //  Animation for enter/exit
                    .setDuration(1500)   // in milliseconds
                    .setTextSize(CDConstants.NORMAL_TEXT_SIZE)  // CDConstants.LARGE_TEXT_SIZE, CDConstants.NORMAL_TEXT_SIZE
                    .show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(AddContact.this, ContactActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.right, R.anim.left);
                }
            }, 1500);



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

        edtBirthday.setInputType(InputType.TYPE_NULL);

        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                d = myCalendar.getTime();
                edtBirthday.setText(dayOfMonth+"/"+monthOfYear+"/"+year);
            }

        };


        edtBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddContact.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        ivContactPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
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
        if(contact != null){
            new AlertDialog.Builder(this)
                    .setTitle(R.string.edition)
                    .setMessage(getString(R.string.validate_edit_annulation) + " " + contact.getFirst_name() + " ?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent i = new Intent(AddContact.this, ContactActivity.class);
                            startActivity(i);
                        }})
                    .setNegativeButton(R.string.non, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }else{
            Intent i = new Intent(AddContact.this, ContactActivity.class);
            startActivity(i);
            super.onBackPressed();
        }
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
