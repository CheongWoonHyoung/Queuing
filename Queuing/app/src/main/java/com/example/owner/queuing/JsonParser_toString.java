package com.example.owner.queuing;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by mark_mac on 2015. 7. 28..
 */

public class JsonParser_toString extends AsyncTask<String,Void,String>{

    private String result;
    private InputStream is;
    private final String TAG = "PARSER";

    JsonParser_toString(){
        result = null;
        is = null;
    }

    @Override
    protected String doInBackground(String... urls) {
        return convert_String(httpPost_json(urls[0]));
    }

    public InputStream httpPost_json(String url){

        try{
            HttpClient  httpClient = new DefaultHttpClient();
            Log.d("TAG","URL : " + url);
            HttpPost httpPost = new HttpPost(url);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.d(TAG, "CONNECTION SUCCESS");

        } catch (Exception e) {
            Log.e(TAG,"Error in Http connection : " + e.toString());
        }
        return is;
    }

    public String convert_String(InputStream is){

        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;

            while((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            is.close();
            result = sb.toString();

        } catch (Exception e) {
            Log.e(TAG, "Error converting String result : " + e.toString());
        }
        return result;
    }

}
