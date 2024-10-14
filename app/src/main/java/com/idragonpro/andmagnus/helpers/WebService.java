package com.idragonpro.andmagnus.helpers;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class WebService {
  private static final String TAG = WebService.class.getSimpleName();

  private String readStream(InputStream in) {
    BufferedReader reader = null;
    StringBuffer response = new StringBuffer();
    try {
      reader = new BufferedReader(new InputStreamReader(in));
      String line = "";
      while ((line = reader.readLine()) != null) {
        response.append(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return response.toString();
  }

  public String getVast(String sUrl,Context context) {
    try {
      URL url = new URL(sUrl);
      Log.d("TAG", "readStream: " + url.toString() + sUrl);

      HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

      urlConnection.setRequestMethod("POST");
      urlConnection.setDoInput(true);
      urlConnection.setDoOutput(true);

      OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
      wr.flush();

      urlConnection.connect();

      int responseCode = urlConnection.getResponseCode();

      if (responseCode == HttpURLConnection.HTTP_OK) {
        return readStream(urlConnection.getInputStream());
      } else {
        return readStream(urlConnection.getInputStream());
      }

    } catch (MalformedURLException e) {
      return e.getMessage();
    } catch (ProtocolException e) {
      return e.getMessage();
    } catch (IOException e) {
      return e.getMessage();
    }
  }
}
