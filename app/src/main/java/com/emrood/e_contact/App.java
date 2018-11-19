package com.emrood.e_contact;

import android.app.Application;
import android.content.Context;

import com.emrood.e_contact.Model.DaoMaster;
import com.emrood.e_contact.Model.DaoSession;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;

/**
 * Created by Noel Emmanuel Roodly on 11/18/2018.
 */
public class App extends Application {

    public static App mInstance;

    private DaoSession mDaoSession;
    public static final boolean ENCRYPTED = true;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "econtact.db");
        Database db = helper.getWritableDb();
        mDaoSession = new DaoMaster(db).newSession();


    }

    public static App getInstance() {
        return mInstance;

    }


    public static Context getAppContext() {
        return mInstance;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }


}
