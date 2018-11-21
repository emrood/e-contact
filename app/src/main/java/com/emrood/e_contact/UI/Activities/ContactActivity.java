package com.emrood.e_contact.UI.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.emrood.e_contact.App;
import com.emrood.e_contact.Model.Contact;
import com.emrood.e_contact.Model.QRObject;
import com.emrood.e_contact.R;
import com.emrood.e_contact.UI.Fragments.ContactList;
import com.emrood.e_contact.UI.Fragments.ContactScan;
import com.emrood.e_contact.Utils.Constant;
import com.emrood.e_contact.Utils.PreferenceManager;
import com.example.circulardialog.CDialog;
import com.example.circulardialog.extras.CDConstants;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.SEND_SMS;
import static android.Manifest.permission.BODY_SENSORS;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.USE_BIOMETRIC;
import static android.Manifest.permission.RECEIVE_SMS;



public class ContactActivity extends AppCompatActivity {



    private SectionsPagerAdapter mSectionsPagerAdapter;

    private static final int PERMISSION_ALL = 1;

    private static final String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.USE_BIOMETRIC,
            Manifest.permission.BODY_SENSORS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_CONTACTS
    };


    private PreferenceManager prefManager;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    Boolean exitApp = false;
    TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        prefManager = new PreferenceManager(this);
        prefManager.setFirstTimeLaunch(false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent i = new Intent(ContactActivity.this, AddContact.class);
                startActivity(i);
                finish();
            }
        });



        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

    }





    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.action_settings:
                Toast.makeText(this, "Settings screen", Toast.LENGTH_SHORT).show();
                break;
            case R.id.e_QR:
                Toast.makeText(this, "generate QR", Toast.LENGTH_SHORT).show();
                Intent e = new Intent(ContactActivity.this, QRViewerActivity.class);
                startActivity(e);
                break;
            case R.id.my_info:
                Toast.makeText(this, "Info dialog", Toast.LENGTH_SHORT).show();
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public int currentPage = 0;

        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                currentPage = position;
                return new ContactList();
            }else if (position == 1){
                currentPage = position;
                return  new ContactScan();
            }else{
                return  null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        public final int getCurrentPage() {
            return currentPage;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result!=null){
            if(result.getContents()==null){
                mViewPager.setCurrentItem(0);
            }
            else {

                Gson json = new Gson();
                try{
                    String r = result.getContents();

                    QRObject object = json.fromJson(r, QRObject.class);

                    if(TextUtils.equals(object.getToken(), Constant.token)){
                        //REGISTER CONTACT
                        try{
                            ((App) App.getInstance()).getDaoSession().getContactDao().save(object.getContact());
                            new CDialog(this).createAlert(getString(R.string.saved_contact),
                                    CDConstants.SUCCESS,   // Type of dialog
                                    CDConstants.LARGE)    //  size of dialog
                                    .setAnimation(CDConstants.SCALE_FROM_BOTTOM_TO_TOP)     //  Animation for enter/exit
                                    .setDuration(2500)   // in milliseconds
                                    .setTextSize(CDConstants.NORMAL_TEXT_SIZE)  // CDConstants.LARGE_TEXT_SIZE, CDConstants.NORMAL_TEXT_SIZE
                                    .show();
                        }catch (Exception e){
                            new CDialog(this).createAlert(getString(R.string.unsaved_contact),
                                    CDConstants.WARNING,   // Type of dialog
                                    CDConstants.LARGE)    //  size of dialog
                                    .setAnimation(CDConstants.SCALE_FROM_BOTTOM_TO_TOP)     //  Animation for enter/exit
                                    .setDuration(2500)   // in milliseconds
                                    .setTextSize(CDConstants.NORMAL_TEXT_SIZE)  // CDConstants.LARGE_TEXT_SIZE, CDConstants.NORMAL_TEXT_SIZE
                                    .show();
                        }
                    }else{
//                        Toast.makeText(getApplicationContext(), R.string.bad_qr, Toast.LENGTH_LONG).show();
                        new CDialog(this).createAlert(getString(R.string.bad_qr),
                                CDConstants.ERROR,   // Type of dialog
                                CDConstants.LARGE)    //  size of dialog
                                .setAnimation(CDConstants.SCALE_FROM_BOTTOM_TO_TOP)     //  Animation for enter/exit
                                .setDuration(2500)   // in milliseconds
                                .setTextSize(CDConstants.NORMAL_TEXT_SIZE)  // CDConstants.LARGE_TEXT_SIZE, CDConstants.NORMAL_TEXT_SIZE
                                .show();
                    }

                }catch (Exception e){
                    new CDialog(this).createAlert(getString(R.string.bad_qr),
                            CDConstants.ERROR,   // Type of dialog
                            CDConstants.LARGE)    //  size of dialog
                            .setAnimation(CDConstants.SCALE_FROM_BOTTOM_TO_TOP)     //  Animation for enter/exit
                            .setDuration(2500)   // in milliseconds
                            .setTextSize(CDConstants.NORMAL_TEXT_SIZE)  // CDConstants.LARGE_TEXT_SIZE, CDConstants.NORMAL_TEXT_SIZE
                            .show();
                }

                mViewPager.setCurrentItem(0);

            }

        }
        else{
//            Toast.makeText(getApplicationContext(), "No e-Code", Toast.LENGTH_SHORT).show();
            mViewPager.setCurrentItem(0);
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {

        final int index = mViewPager.getCurrentItem();
        if(exitApp){
            //System.gc();
            //System.exit(0);
            moveTaskToBack(true);
        }else{
            if(mSectionsPagerAdapter.getCurrentPage() == 1){
                mViewPager.setCurrentItem(0);
            }
            Toast.makeText(this, R.string.quit_message, Toast.LENGTH_SHORT).show();
            exitApp = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exitApp = false;
                }
            }, 3 * 1000);
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

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
