package com.hatak.benefit.repository;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.common.base.Optional;
import com.hatak.benefit.login.UIUser;
import com.hatak.benefit.saldo.Saldo;
import com.hatak.benefit.saldo.SaldoParser;
import com.hatak.benefit.saldo.Transaction;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;

/**
 * Created by hatak on 22.08.16.
 */
public class Repository {

    private static final String CARD_NUMBER = "CARD_NUMBER";
    private static final String NIK_NUMBER = "NIK_NUMBER";
    private static final String FORM_DATA = "AuthenticationMethod=UsernameAuthenticator&BackURL=/saldo&Username=CARD_NUMBER&Password=NIK_NUMBER&action_dologin.x=51&action_dologin.y=14";
    private static final String URL = "https://www.mypremium.pl/Security/LoginForm";
    private static final String BASE_URL = "https://www.mypremium.pl/";
    private static final String SALDO_PATH = "Security/LoginForm";
    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    public static final String KEY_ID = "id";
    public static final int TIMEOUT = 30 * 1000; // 30 sec

    private static Repository instance;
    private Context appContext;
    private final String baseUrl;

    public Repository(final String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public static void initialize(@NonNull final Context context){
        initialize(context, BASE_URL);
    }

    public static void initialize(@NonNull final Context context, @NonNull String baseUrl) {
        if (instance == null) {
            RealmConfiguration config = new RealmConfiguration.Builder()
                    .name("benefit")
                    .deleteRealmIfMigrationNeeded()
                    .build();
            Realm.setDefaultConfiguration(config);
            instance = new Repository(baseUrl);
            instance.appContext = context.getApplicationContext();
        }
    }

    public static Repository getInstance() {
        if (instance != null) {
            return instance;
        } else {
            throw new RuntimeException("Initialize repository first");
        }
    }

    public Observable<Boolean> saveUser(final User user) {
        return Observable.create(subscriber -> {
            final Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.insertOrUpdate(user);
            realm.commitTransaction();
            realm.close();
            subscriber.onNext(Boolean.TRUE);
            subscriber.onCompleted();
        });
    }

    public Observable<UIUser> loadUser(){
        return Observable.create(new Observable.OnSubscribe<Optional<User>>() {
            @Override
            public void call(final Subscriber<? super Optional<User>> subscriber) {
                final Realm realm = Realm.getDefaultInstance();
                final User user = realm
                        .where(User.class)
                        .equalTo(KEY_ID, User.DEFAULT_ID)
                        .findFirst();
                subscriber.onNext(Optional.fromNullable(user));
                subscriber.onCompleted();
                realm.close();
            }
        }).map(user -> {
            if(user.isPresent()){
                return new UIUser(user.get().getCardNumber(), user.get().getNikNumber());
            }else{
                return UIUser.createEmpty();
            }
        });
    }

    public Observable<Saldo> getSaldo(final String cardNumber, final String nikNumber){
        return Observable.create(subscriber -> {
            try {
                SyncHttpClient client = getUnsafeSyncHttpClient();
                StringEntity form = new StringEntity(FORM_DATA.replace(CARD_NUMBER, cardNumber).replace(NIK_NUMBER, nikNumber));
                client.post(appContext, baseUrl + SALDO_PATH, form, CONTENT_TYPE, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        subscriber.onError(throwable);
                    }
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String html) {
                        final String saldoValue = SaldoParser.getSaldoValue(html);
                        final List<Transaction> transactions = SaldoParser.getTransactions(html);
                        Saldo saldo = new Saldo(saldoValue, transactions);
                        subscriber.onNext(saldo);
                        subscriber.onCompleted();
                    }
                });
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    public void migrateIfNeeded(){
        RepositoryMigrator repositoryMigrator = new RepositoryMigrator();
        repositoryMigrator.migrateUserData(appContext);
    }

    @NonNull
    private SyncHttpClient getUnsafeSyncHttpClient() throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, KeyManagementException, UnrecoverableKeyException {
        // We initialize a default Keystore
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        // We load the KeyStore
        trustStore.load(null, null);
        // We initialize a new SSLSocketFactory
        MySSLSocketFactory socketFactory = new MySSLSocketFactory(trustStore);
        // We set that all host names are allowed in the socket factory
        socketFactory.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        // We initialize the Async Client
        SyncHttpClient client = new SyncHttpClient();
        // We set the timeout to 30 seconds
        client.setTimeout(TIMEOUT);
        // We set the SSL Factory
        client.setSSLSocketFactory(socketFactory);
        client.setEnableRedirects(true);
        client.setUserAgent("Mozilla/5.0 (Linux; Android 4.4; Nexus 5 Build/_BuildID_) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36");
        return client;
    }
}
