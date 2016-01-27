package com.reacherandroid;
import com.reacherandroid.ReacherModule;

import android.content.Context;
import android.text.TextUtils;
import android.os.AsyncTask;
import android.widget.Toast;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;


class ReacherManager {
  static String typeOfInput;
  static boolean reachable = false;

  public static boolean connect(String input) {
    typeOfInput = getTypeOfInput(input);
    if (typeOfInput == "invalid") {
      return false;
    }

    DoBackGroundTask doBackGroundTask = new DoBackGroundTask();
    try {
      return doBackGroundTask.execute(input).get();
    } catch(Exception e) {
      e.printStackTrace();
    }

    return false;
  }

  private static String getTypeOfInput(String input) {
    boolean isIP = false;
    boolean isValidIP = false;

    String[] array = input.split("\\.");
    if (array.length == 4 && !input.contains("www") && !input.contains("http")) {
        for (String ipNum: array) {
            isIP = TextUtils.isDigitsOnly(ipNum);
            if (!isIP) {
                break;
            } else {
                if (Integer.parseInt(ipNum) >= 0 && Integer.parseInt(ipNum) <= 255) {
                    isValidIP = true;
                } else {
                    // Toast.makeText(getReactApplicationContext(), "The IP address is out of range.", Toast.LENGTH_LONG).show();
                    isValidIP = false;
                    break;
                }
            }
        }
    }
    if (isIP) {
        return "IP";
    } else if (isIP && !isValidIP) {
        return "invalid";
    }

    try {
        new URL(input);
    } catch (MalformedURLException e) {
        e.printStackTrace();
        if (e.getMessage().contains("Unknown protocol")) {
            // Toast.makeText(getReactApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        } else if (e.getMessage().contains("Protocol not found")) {
            // Toast.makeText(getReactApplicationContext(), "Provide a protocol (http:// or https://)", Toast.LENGTH_LONG).show();
        }
        return "invalid";
    }
    return "URL";
  }

  public static class DoBackGroundTask extends AsyncTask<String, Void, Boolean> {

      @Override
      protected Boolean doInBackground(String...inputs) {
          // Checks when input is URL: https://www.google.com
          if (typeOfInput == "URL") {
              System.out.println("Working");
              try {
                  URL input = new URL(inputs[0]);
                  HttpURLConnection httpURLConnection = (HttpURLConnection) input.openConnection();
                  httpURLConnection.setRequestProperty("User-Agent", "Android Application");
                  httpURLConnection.setRequestProperty("Connection", "close");
                  httpURLConnection.setConnectTimeout(2000);
                  httpURLConnection.connect();
                  reachable = (httpURLConnection.getResponseCode() == 200);
              } catch (IOException e) {
                  reachable = false;
              }
          } else {
              // Checks when input is IP: 216.58.213.196
              try {
                  Process process = Runtime.getRuntime().exec("/system/bin/ping -c 1 -w 1 " + inputs[0]);
                  int mExitValue = process.waitFor();
                  reachable = mExitValue == 0;
              }
              catch (InterruptedException ignore) {
                  ignore.printStackTrace();
                  System.out.println(" Exception:"+ignore);
              }
              catch (IOException e) {
                  e.printStackTrace();
                  System.out.println(" Exception:" + e);
              }
          }
          return reachable;
      }
  }

}