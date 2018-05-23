package matic.mladen.chatapplication;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;

/**
 * Created by Mladen on 5/16/2018.
 */

public class HttpHelper {
    private static final int SUCCESS = 200;

    public JSONArray getJSONArrayFromURL(String urlString, String sessionId) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        java.net.URL url = new URL(urlString);
        urlConnection  = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setRequestProperty("sessionid", sessionId);
        urlConnection.setReadTimeout(10000);
        urlConnection.setConnectTimeout(15000);
        try {
            urlConnection.connect();
        } catch (IOException e) {
            return null;
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        Log.i("MSG", "Dosao do while-a posle string builder-a i line-a.");
        while((line = bufferedReader.readLine()) != null) {
            Log.i("MSG", line);
            stringBuilder.append(line + "\n");
        }
        bufferedReader.close();
        Log.i("MSG", "Zatvorio reader.");
        String jsonString = stringBuilder.toString();

        int responseCode = urlConnection.getResponseCode();
        urlConnection.disconnect();
        Log.i("MSG", "Dosao do return-a.");
        return responseCode == SUCCESS ? new JSONArray(jsonString) : null;
    }

    public JSONObject getJSONObjectFromURL(String urlString) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        java.net.URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setReadTimeout(10000);
        urlConnection.setConnectTimeout(15000);
        try {
            urlConnection.connect();
        } catch (IOException e) {
            return null;
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line + "\n");
        }
        bufferedReader.close();

        String jsonString = stringBuilder.toString();
        int responseCode = urlConnection.getResponseCode();
        urlConnection.disconnect();
        return responseCode == SUCCESS ? new JSONObject(jsonString) : null;
    }

    public RetrunClass postJSONObjectFromURL(String urlString, JSONObject jsonObject) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        java.net.URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        Log.i("MSG", "Dosao do POST-a.");
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        Log.i("MSG", "Set-ovao konekciju.");
        try {
            urlConnection.connect();
            Log.i("MSG", "Konektovao se.");
        } catch (IOException e) {
            Log.i("MSG", "Bacio IO exception.");
            return new RetrunClass(404, urlConnection.getHeaderField("sessionid"));
        }
        DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
        dataOutputStream.writeBytes(jsonObject.toString());
        dataOutputStream.flush();
        dataOutputStream.close();
        int responseCode = urlConnection.getResponseCode();
        urlConnection.disconnect();
        Log.i("MSG", "Dosao do return-a.");
        return responseCode == SUCCESS ? new RetrunClass(200, urlConnection.getHeaderField("sessionid")) : new RetrunClass(409, urlConnection.getHeaderField("sessionid"));
    }

    public RetrunClass postJSONObjectFromURL(String urlString, JSONObject jsonObject, String sessionId) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        java.net.URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");
        urlConnection.setRequestProperty("sessionid", sessionId);
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        try {
            urlConnection.connect();
        } catch (IOException e) {
            return new RetrunClass(404, urlConnection.getHeaderField("sessionid"));
        }
        DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
        dataOutputStream.writeBytes(jsonObject.toString());
        dataOutputStream.flush();
        dataOutputStream.close();
        int responseCode = urlConnection.getResponseCode();
        urlConnection.disconnect();
        return responseCode == SUCCESS ? new RetrunClass(200, urlConnection.getHeaderField("sessionid")) : new RetrunClass(409, urlConnection.getHeaderField("sessionid"));
    }

    public RetrunClass httpDelete(String urlString, JSONObject jsonObject, String sessionId) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        java.net.URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("DELETE");
        urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");
        urlConnection.setRequestProperty("sessionid", sessionId);
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        try {
            urlConnection.connect();
        } catch (IOException e) {
            Log.i("MSG", "DELETE bacio IO exception.");
            return new RetrunClass(404, urlConnection.getHeaderField("sessionid"));
        }
        DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
        dataOutputStream.writeBytes(jsonObject.toString());
        dataOutputStream.flush();
        dataOutputStream.close();
        int responseCode = urlConnection.getResponseCode();
        urlConnection.disconnect();
        Log.i("MSG", "Dosao DELETE do return-a.");
        Log.i("STATUS", String.valueOf(responseCode));
        return new RetrunClass(responseCode, urlConnection.getHeaderField("sessionid"));
    }

    public class RetrunClass {
        int mResponseCode;
        String mSessionId;

        public RetrunClass(int rc, String sid) {
            this.mResponseCode = rc;
            this.mSessionId = sid;
        }
    }
}
