package com.trusttags.spykar.services;

import android.content.Context;


import com.trusttags.spykar.R;
import com.trusttags.spykar.utils.PreferenceConnector;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.trusttags.spykar.BuildConfig.FLAVOR;

public class RetrofitClient {


    public static String BASE_URL = "https://spykar-dev-api.trusttags.in/";


    private static Retrofit retrofitRx = null;
    public static Retrofit getClientbyRx(Context context) {

        if(FLAVOR=="spykar")
            BASE_URL = "https://spykar-dev-api.trusttags.in/";
        else if(FLAVOR=="pepe")
            BASE_URL = /*"https://pepe-api.trusttags.in/";*/"https://pepe-dev-api.trusttags.in/";
        else if(FLAVOR=="pantaloons")
            BASE_URL = "https://pantaloons-dev-api.trusttags.in/";
        else if(FLAVOR=="biba")
            BASE_URL = "https://biba-dev-api.trusttags.in/";
        else if(FLAVOR=="clovia")
            BASE_URL = "https://clovia-dev-api.trusttags.in/";

        if (retrofitRx==null) {
            try {
                retrofitRx = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(getHTTPClient(context))
                        .build();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return retrofitRx;

    }


    public static OkHttpClient getHTTPClient(final Context context) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
//            var client = OkHttpClient()

        int certRawResId = 0;

        if(FLAVOR=="spykar")
            certRawResId = R.raw.spykar_dev;
        else if(FLAVOR=="pepe")
            certRawResId = R.raw.pepe_dev;
        else if(FLAVOR=="pantaloons")
            certRawResId = R.raw.pantloons_dev;
        else if(FLAVOR=="biba")
            certRawResId = R.raw.biba_dev;
        else if(FLAVOR=="clovia")
            certRawResId = R.raw.clovia_dev;

//            val keyStore = buildKeyStore(context, R.raw.cer_root_prod_old)
        KeyStore keyStore = buildKeyStore(context, certRawResId);
        SSLContext sslContext = SSLContext.getInstance("TLS");
//        val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(tmfAlgorithm);

        trustManagerFactory.init(keyStore);
        try {
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

//            val loggingInterceptor = HttpLoggingInterceptor()
//            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        String sessionToken = PreferenceConnector.readString(context,PreferenceConnector.ACCESS_TOKEN,"");
                        if(sessionToken == null)
                            sessionToken= "";

                        String xKey = PreferenceConnector.readString(context,PreferenceConnector.X_KEY,"");
                        if(xKey == null)
                            xKey= "";

                        Request request = original.newBuilder()
                                .header("Content-Type", "application/json")
                                .header("Accept", "application/json")
                                .header("x-access-token", sessionToken)
                                .header("x-key", xKey)
                                .method(original.method(), original.body())
                                .build();

                        Response reponse  = chain.proceed(request);

                        return reponse;
                    }
                }).connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String s, SSLSession sslSession) {
                        return true;
                    }
                }).sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustManagerFactory.getTrustManagers()[0])
                .build();

        return client;

    }
    public static KeyStore buildKeyStore(Context context, int certRawResId) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        // init a default key store
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);

        // read and add certificate authority
        Certificate cert = readCert(context, certRawResId);
        keyStore.setCertificateEntry("ca", cert);

        return keyStore;
    }

    private static Certificate readCert(Context context, int certResourceId) throws CertificateException, IOException {

        // read certificate resource
        InputStream caInput = context.getResources().openRawResource(certResourceId);

        Certificate ca;
        try {
            // generate a certificate
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            ca = cf.generateCertificate(caInput);
        } finally {
            caInput.close();
        }

        return ca;
    }
}