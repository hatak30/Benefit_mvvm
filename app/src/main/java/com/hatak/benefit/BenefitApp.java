package com.hatak.benefit;

import android.support.multidex.MultiDexApplication;

import com.hatak.benefit.repository.Repository;

/**
 * Created by hatak on 22.08.16.
 */
public class BenefitApp extends MultiDexApplication{

    @Override
    public void onCreate() {
        super.onCreate();
        Repository.initialize(this);
        Repository.getInstance().migrateIfNeeded();
    }
}
