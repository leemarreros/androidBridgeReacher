package com.reacherandroid;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

import java.util.Map;
import java.util.HashMap;

public class ReacherModule extends ReactContextBaseJavaModule {
   public ReacherModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return "ReacherModule";
  }

  @ReactMethod
  public void reach(String input, Callback errorCallback, Callback successCallback) {

    boolean result = ReacherManager.connect(input);
    successCallback.invoke(result);

  }

}
