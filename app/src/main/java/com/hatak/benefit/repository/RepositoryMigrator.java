package com.hatak.benefit.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Created by hatak on 01.09.16.
 */
public class RepositoryMigrator {

    public static final int VALID_MIGRATION_VERSION = 18;
    public static String TAG = RepositoryMigrator.class.getName();

    private static final String PREFS_NAME = "BenefitSaldoPrefs";
    private static final String NIK_KEY = "nik_number";
    private static final String CARD_NUMBER_KEY = "card_number";

    public void migrateUserData(final Context context){
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if(pInfo.versionCode == VALID_MIGRATION_VERSION){
                SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
                if (settings.contains(NIK_KEY) && settings.contains(CARD_NUMBER_KEY)) {
                    String cardNumber = settings.getString(CARD_NUMBER_KEY, null);
                    String nikNumber = settings.getString(NIK_KEY, null);
                    Repository.getInstance()
                            .saveUser(new User(cardNumber, nikNumber))
                            .toBlocking()
                            .subscribe(result -> {
                                settings.edit().clear().commit();
                                Log.i(TAG, "migration finished");
                            });
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "migration failed");
        }
    }
}
