package com.emrood.e_contact.UI.Activities;

import android.Manifest;
import android.arch.persistence.room.Transaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.emrood.e_contact.Model.Contact;
import com.emrood.e_contact.R;
import com.emrood.e_contact.Utils.PreferenceManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.emrood.e_contact.UI.Activities.ContactActivity.hasPermissions;

public class FirstLaunch extends AppCompatActivity {


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    CircleImageView ivContactPhoto;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext;
    private PreferenceManager prefManager;
    public static String langPref = "HT";


    private static final int PERMISSION_ALL = 1;

    private static final String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.USE_BIOMETRIC,
            Manifest.permission.BODY_SENSORS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    Gson gson;
    String list;
    ArrayList<Transaction> listTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        prefManager = new PreferenceManager(this);

        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_first_launch);
        //Store transaction
        sharedPreferences = getSharedPreferences("PreferencesTAG", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //Create persistence
        gson = new Gson();
        listTransaction = new ArrayList<>();
        list = gson.toJson(listTransaction);
        editor.putString("listTransaction", list);
        editor.commit();


        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);

        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.welcome,
                R.layout.language,
                R.layout.my_info};

        // adding bottom dots
        addBottomDots(0);

        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                Toast.makeText(FirstLaunch.this, String.valueOf(current), Toast.LENGTH_SHORT).show();
                switch (current -1) {
                    case 1:
                        prefManager.setLangSelected(myViewPagerAdapter.language.getSelectedItem().toString());
                        Toast.makeText(FirstLaunch.this, myViewPagerAdapter.language.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        if (TextUtils.isEmpty(myViewPagerAdapter.edtFirstName.getText())) {
                            myViewPagerAdapter.edtFirstName.setError(getString(R.string.obligation_field));
                            myViewPagerAdapter.edtFirstName.requestFocus();
                            return;
                        } else if (TextUtils.isEmpty(myViewPagerAdapter.edtLastName.getText())) {
                            myViewPagerAdapter.edtLastName.setError(getString(R.string.obligation_field));
                            myViewPagerAdapter.edtLastName.requestFocus();
                            return;
                        } else if (TextUtils.isEmpty(myViewPagerAdapter.edtEmailPersonnal.getText()) || !android.util.Patterns.EMAIL_ADDRESS.matcher(myViewPagerAdapter.edtEmailPersonnal.getText().toString()).matches()) {
                            myViewPagerAdapter.edtEmailPersonnal.setError(getString(R.string.obligation_field));
                            myViewPagerAdapter.edtEmailPersonnal.requestFocus();
                            return;
                        } else if (TextUtils.isEmpty(myViewPagerAdapter.edtPin.getText())) {
                            myViewPagerAdapter.edtPin.setError(getString(R.string.obligation_field));
                            myViewPagerAdapter.edtPin.requestFocus();
                            return;
                        } else if (!TextUtils.equals(myViewPagerAdapter.edtPin.getText(), myViewPagerAdapter.edtPin2.getText())) {
                            myViewPagerAdapter.edtPin2.setError(getString(R.string.invalide_pin));
                            myViewPagerAdapter.edtPin2.requestFocus();
                            return;
                        }

                        Contact c = new Contact();
                        c.setFirst_name(myViewPagerAdapter.edtFirstName.getText().toString().trim());
                        c.setLast_name(myViewPagerAdapter.edtLastName.getText().toString().trim());
                        if(!TextUtils.isEmpty(myViewPagerAdapter.edtPhoneCellular.getText().toString())){
                            c.setCellular_phone(myViewPagerAdapter.edtPhoneCellular.getText().toString().trim());
                        }
                        if(!TextUtils.isEmpty(myViewPagerAdapter.edtEntreprise.getText().toString())){
                            c.setEntreprise(myViewPagerAdapter.edtEntreprise.getText().toString().trim());
                        }
                        if(!TextUtils.isEmpty(myViewPagerAdapter.edtEmailPersonnal.getText().toString())){
                            c.setPersonal_email(myViewPagerAdapter.edtEmailPersonnal.getText().toString().trim());
                        }
                        if(!TextUtils.isEmpty(myViewPagerAdapter.edtPhoneWork.getText().toString())){
                            c.setWork_phone(myViewPagerAdapter.edtPhoneWork.getText().toString().trim());
                        }
                        if(!TextUtils.isEmpty(myViewPagerAdapter.edtEmailProfessional.getText().toString()) && android.util.Patterns.EMAIL_ADDRESS.matcher(myViewPagerAdapter.edtEmailProfessional.getText().toString()).matches()){
                            c.setWork_email(myViewPagerAdapter.edtEmailProfessional.getText().toString().trim());
                        }

                        if(!TextUtils.isEmpty(myViewPagerAdapter.edtPhoneOther.getText().toString())){
                            c.setOther_phone(myViewPagerAdapter.edtPhoneOther.getText().toString().trim());
                        }
                        if(!TextUtils.isEmpty(myViewPagerAdapter.edtEmailOther.getText().toString()) && android.util.Patterns.EMAIL_ADDRESS.matcher(myViewPagerAdapter.edtEmailOther.getText().toString()).matches()){
                            c.setOther_email(myViewPagerAdapter.edtEmailOther.getText().toString().trim());
                        }

                        c.setIsFav(false);
                        c.setIsSecret(false);

                        prefManager.saveUserContact(c);
                        prefManager.setUserInfoSaved(true);
                        prefManager.saveUsetPin(myViewPagerAdapter.edtPin.getText().toString());
                        Toast.makeText(FirstLaunch.this, "User saved", Toast.LENGTH_SHORT).show();

                        break;
                    default:
                        break;

                }
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });


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

    private void launchHomeScreen() {
        Intent i = new Intent(FirstLaunch.this, Launcher.class);
        startActivity(i);
        overridePendingTransition(R.anim.right, R.anim.left);
    }


    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }


    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText("Ok");
                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText(R.string.next_welcome);
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        public EditText edtFirstName, edtLastName, edtEntreprise,
                edtPhoneCellular, edtPhoneWork, edtPhoneOther, edtEmailPersonnal,
                edtEmailProfessional, edtEmailOther, edtPin, edtPin2;

        public Spinner language;

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            switch (position) {
                case 0:

                    break;
                case 1:
                    language = view.findViewById(R.id.spinLang);
                    break;
                case 2:
                    edtPin = view.findViewById(R.id.edtPin);
                    edtPin2 = view.findViewById(R.id.edtPin2);
                    edtFirstName = view.findViewById(R.id.edtfirst_name);
                    edtLastName = view.findViewById(R.id.edtLastname);
                    edtEmailPersonnal = view.findViewById(R.id.edtEmail);
                    edtEmailProfessional = view.findViewById(R.id.edtWorlMail);
                    edtEmailOther = view.findViewById(R.id.edtOtherMail);
                    edtPhoneCellular = view.findViewById(R.id.edtPhoneCellular);
                    edtPhoneWork = view.findViewById(R.id.edtphoneWork);
                    edtPhoneOther = view.findViewById(R.id.edtPhoneOther);
                    edtEntreprise = view.findViewById(R.id.edtEntreprise);
                    ivContactPhoto = view.findViewById(R.id.ivContactPhoto);
                    break;
            }

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}


