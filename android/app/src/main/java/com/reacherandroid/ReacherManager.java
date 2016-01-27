package com.reacherandroid;
import com.reacherandroid.ReacherModule;

import android.text.TextUtils;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class ReacherManager {
  static String typeOfInput;

  public static boolean connect(String input){
    typeOfInput = getTypeOfInput(input);
    if (typeOfInput == "invalid") {
      return false;
    }

    ExecutorService executor = Executors.newSingleThreadExecutor();
    Future<Boolean> answer = executor.submit(new DoBackGroundTask(input));
    try {
      return answer.get();
    } catch (Exception e) {
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
                    System.out.println("The IP address is out of range.");
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
            System.out.println(e.getMessage());
        } else if (e.getMessage().contains("Protocol not found")) {
            System.out.println("Provide a protocol (http:// or https://)");
        }
        return "invalid";
    }
    return "URL";
  }

  public static class DoBackGroundTask implements Callable<Boolean> {
      static boolean reachable = false;
      private String input;

      public DoBackGroundTask(String input) {
        this.input = input;
      }

      @Override
      public Boolean call() throws Exception {
        return reach(input);
      }

      private boolean reach(String input) throws InterruptedException {
        // Checks when input is URL: https://www.google.com
        if (typeOfInput == "URL") {
            try {
                URL url = new URL(input);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestProperty("User-Agent", "Android Application");
                httpURLConnection.setRequestProperty("Connection", "close");
                httpURLConnection.setConnectTimeout(2000);
                httpURLConnection.connect();
                reachable = (httpURLConnection.getResponseCode() == 200);
            } catch (IOException e) {
                return false;
            }
        } else {
            // Checks when input is IP: 216.58.213.196
            try {
                Process process = Runtime.getRuntime().exec("/system/bin/ping -c 1 -w 1 " + input);
                int mExitValue = process.waitFor();
                reachable = mExitValue == 0;
            }
            catch (InterruptedException ignore) {
                ignore.printStackTrace();
                System.out.println(" Exception:" + ignore);
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