package com.hatak.benefit.repository;

import android.text.TextUtils;

import com.hatak.benefit.R;
import com.hatak.benefit.saldo.Saldo;
import com.hatak.benefit.saldo.Transaction;

import org.apache.commons.io.IOUtils;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import rx.Observable;

/**
 * Created by hatak on 05.09.16.
 */

@RunWith(RobolectricTestRunner.class)
public class RepositoryTest {

    @Test
    public void testGetSaldo() throws IOException {

        MockWebServer server = new MockWebServer();
        // Schedule some responses.
        final String response = IOUtils.toString(ClassLoader.getSystemResourceAsStream("response_ok.html"));
        server.enqueue(new MockResponse().setBody(response));
        // Start the server.
        server.start();
        // Ask the server for its URL. You'll need this to make HTTP requests.
        HttpUrl baseUrl = server.url("/");
        // Exercise your application code, which should make those HTTP requests.
        // Responses are returned in the same order that they are enqueued.

        Repository repository = new Repository(baseUrl.toString());
        final Saldo saldo = repository.getSaldo("1234", "abcd")
                .toBlocking()
                .first();

        assertTrue(saldo.getSaldo().contains("100,04"));
        assertTrue(saldo.getTransactions().size() == 3);

        final Transaction transaction = saldo.getTransactions().get(0);
        assertTrue(!TextUtils.isEmpty(transaction.getDate()));
        assertTrue(!TextUtils.isEmpty(transaction.getPlace()));
        assertTrue(!TextUtils.isEmpty(transaction.getPrice()));
        assertTrue(!TextUtils.isEmpty(transaction.getType()));

        server.shutdown();
    }

}
