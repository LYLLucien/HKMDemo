package com.lucien.hkmdemo.api;

import android.content.Context;
import android.provider.BaseColumns;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.lucien.hkmdemo.constant.Config;
import com.lucien.hkmdemo.constant.Enumeration.ApiStatus;
import com.lucien.hkmdemo.model.AccountModel;
import com.lucien.hkmdemo.utils.common.CommonLog;
import com.lucien.hkmdemo.utils.https.CustomSSLSocketFactory;
import com.lucien.hkmdemo.utils.https.CustomX509TrustManager;
import com.lucien.hkmdemo.utils.https.HttpsUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

/**
 * Created by lucien.li on 2015/10/5.
 */
public class Api {

    private final String CLASSTAG = Api.class.getSimpleName();
    private static Api api;

    private Context context;
    private String androidID;


    public static Api getApiInstance(Context context) {
        if (api == null) {
            api = new Api(context);
        }
        return api;
    }

    private Api(Context context) {
        this.context = context;
        androidID = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.v(CLASSTAG, "Api constructing");
    }

    public ApiStatus loginAccount(ApiResult result, AccountModel account) {
        Map<String, String> params = new HashMap<>();
        params.put(ApiConstants.USERNAME, account.getUsername());
        params.put(ApiConstants.PASSWORD, account.getPassword());

//        HttpClient client = new DefaultHttpClient();
        HttpClient client = HttpsUtils.getSSLClient();

        HttpContext httpContext = new BasicHttpContext();
        String msg = "";

        try {
            HttpPost post = new HttpPost(Config.API_URL + Config.URL_LOGIN);
            CommonLog.i(CLASSTAG, "login url: " + Config.API_URL + Config.URL_LOGIN);
            UrlEncodedFormEntity encodedFormEntity = new UrlEncodedFormEntity(generatePostContent(params));
            post.setEntity(encodedFormEntity);
            HttpResponse response = client.execute(new HttpHost(Config.API_HOST), post, httpContext);
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity != null) {
                msg = EntityUtils.toString(httpEntity);
            }

            if (!TextUtils.isEmpty(msg) && !msg.contains("Fatal error")) {
                result.setState(0);
                result.setMsg(msg);
            } else {
                System.out.println("msg: " + msg.toString());
                return ApiStatus.Connection_Error;
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return ApiStatus.General_Error;
        }
        return ApiStatus.Success;
    }

    public ApiStatus httpsLogin(ApiResult result, AccountModel account) {
        Map<String, String> params = new HashMap<>();
        params.put(ApiConstants.USERNAME, account.getUsername());
        params.put(ApiConstants.PASSWORD, account.getPassword());

        HttpClient sslClient = HttpsUtils.getNewHttpClient();
        HttpContext httpContext = new BasicHttpContext();
        String msg = "";

        try {
//            URL url = new URL(Config.API_URL + Config.URL_LOGIN);
//            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//           // connection.setRequestProperty("USER-AGENT", "runscope/0.1");
//            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//           // connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
//
//            connection.setDoOutput(true);
//            DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//            dStream.writeBytes("username=hkm&password=123");
//            dStream.flush();
//            dStream.close();
//
//            int responseCode = connection.getResponseCode();
//            System.out.println("responseCode: " + responseCode);
            HttpPost post = new HttpPost(Config.API_URL + Config.URL_LOGIN);
            post.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
           // post.addHeader("Content-Type", "application/json; charset=UTF-8");
            //post.addHeader("Host", Config.API_HOST);

            CommonLog.i(CLASSTAG, "login url: " + Config.API_URL + Config.URL_LOGIN);
            UrlEncodedFormEntity encodedFormEntity = new UrlEncodedFormEntity(generatePostContent(params), HTTP.UTF_8);
            post.setEntity(encodedFormEntity);
            //HttpResponse response = sslClient.execute(new HttpHost(Config.API_HOST), post, httpContext);
            HttpResponse response = sslClient.execute(post);
            BufferedReader r = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuilder total = new StringBuilder();

            String line = null;

            while ((line = r.readLine()) != null) {
                total.append(line);
            }
            CommonLog.i(CLASSTAG, "msg: " + total.toString());
////        HttpResponse response;
//
//        try {
//            response = request(Config.API_URL + Config.URL_LOGIN, account);
//            HttpEntity httpEntity = response.getEntity();
//            if (httpEntity != null) {
//                msg = EntityUtils.toString(httpEntity);
//            }
//            CommonLog.i(CLASSTAG, "msg: " + msg);

            if (!TextUtils.isEmpty(msg) && !msg.contains("Fatal error")) {
                result.setState(0);
                result.setMsg(msg);
            } else {
                return ApiStatus.Connection_Error;
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return ApiStatus.General_Error;
        }

        return ApiStatus.Success;
    }

    public HttpResponse request(String url, AccountModel account)
            throws IOException, IllegalStateException {
        Map<String, String> params = new HashMap<>();
        params.put(ApiConstants.USERNAME, account.getUsername());
        params.put(ApiConstants.PASSWORD, account.getPassword());
        DefaultHttpClient client = (DefaultHttpClient) HttpsUtils.getNewHttpClient();

        HttpPost post = new HttpPost(url);
        UrlEncodedFormEntity encodedFormEntity = new UrlEncodedFormEntity(generatePostContent(params), HTTP.UTF_8);
        post.setEntity(encodedFormEntity);
        HttpResponse response = client.execute(post);
        return response;
    }

    public ApiStatus getHttpsMovies(ApiResult result) {

        String msg = "";

        try {
            String responseContent = "";

            DefaultHttpClient sslClient = HttpsUtils.getSSLClient();

            HttpGet get = null;
            try {
                get = new HttpGet(new URI(Config.API_HTTPS + Config.API_HOST + Config.URL_MOVIES));
            } catch (URISyntaxException e1) {
                e1.printStackTrace();
            }
            HttpResponse response = null;
            try {
                response = sslClient.execute(get);
            } catch (ClientProtocolException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            try {
                responseContent = EntityUtils.toString(response.getEntity());
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (responseContent != null) {
                msg = responseContent;
            }

            if (!TextUtils.isEmpty(msg) && !msg.contains("Fatal error")) {
                result.setState(0);
                result.setMsg(msg);
            } else {
                return ApiStatus.Connection_Error;
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return ApiStatus.General_Error;
        }
        return ApiStatus.Success;
    }

    private List<NameValuePair> generatePostContent(Map<String, String> params) throws UnsupportedEncodingException {
        List<NameValuePair> content = new ArrayList<>();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            content.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        CommonLog.i(CLASSTAG, "post content:" + content.toString());
        return content;
    }

    public static final class ApiConstants implements BaseColumns {
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
    }
}
