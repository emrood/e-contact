package com.emrood.e_contact.UI.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.astuetz.PagerSlidingTabStrip;
import com.emrood.e_contact.R;
import com.emrood.e_contact.UI.Fragments.ContactList;
import com.emrood.e_contact.UI.Fragments.ContactScan;

import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{



    FragmentManager fm;
    FloatingActionButton BtnAddContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("e-contact");
        fm = getSupportFragmentManager();

        //Get the VoiewPager
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        vpPager.setAdapter(new EmroodPagerAdapter(getSupportFragmentManager()));
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        BtnAddContact = (FloatingActionButton) findViewById(R.id.BtnAddContact);
        BtnAddContact.setOnClickListener(this);
        tabStrip.setViewPager(vpPager);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.BtnAddContact:
                Toast.makeText(this, "Floating", Toast.LENGTH_SHORT).show();
                break;
        }

    }


    public  class EmroodPagerAdapter extends FragmentPagerAdapter {

        final int PAGE_COUNT = 2;
        private String tabTitle[] = {"Contact", "Scan"};

        public EmroodPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                return new ContactList();
            }else if (position == 1){
                return  new ContactScan();
            }else{
                return  null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitle[position];
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }
    }

    //affiche le fragment permettant de tweeter
    public void onNewView(View view) {
//        tweety.show(fm, "New Tweet");
    }
}
