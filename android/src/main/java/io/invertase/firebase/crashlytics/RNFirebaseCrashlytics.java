package io.invertase.firebase.crashlytics;

import android.util.Log;
import android.os.Handler;

// import com.crashlytics.android.Crashlytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;

import java.util.ArrayList;

// import io.fabric.sdk.android.Fabric;

public class RNFirebaseCrashlytics extends ReactContextBaseJavaModule {

  private static final String TAG = "RNFirebaseCrashlytics";

  public RNFirebaseCrashlytics(ReactApplicationContext reactContext) {
    super(reactContext);
    Log.d(TAG, "New instance");
  }

  @Override
  public String getName() {
    return TAG;
  }

  @ReactMethod
  public void crash() {
    // throw new RuntimeException("Test Crash");
    new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
          throw new RuntimeException("Crash Test");
        }
      }, 50);
  }

  @ReactMethod
  public void log(final String message) {
    FirebaseCrashlytics.getInstance().log(message);
  }

  @ReactMethod
  public void recordError(final int code, final String domain) {
    FirebaseCrashlytics.getInstance().recordException(new Exception(code + ": " + domain));
  }

  @ReactMethod
  public void recordCustomError(String name, String reason, ReadableArray frameArray) {
      ArrayList<StackTraceElement> stackList = new ArrayList<>(0);
      for (int i = 0; i < frameArray.size(); i++) {
        ReadableMap map = frameArray.getMap(i);
        ReadableMap additional = map.hasKey("additional") ? map.getMap("additional") : null;
        String functionName = map.hasKey("functionName") ? map.getString("functionName") : "Unknown Function";
        String className = map.hasKey("className") ? map.getString("className") : "Unknown Class";
        StackTraceElement stack = new StackTraceElement(
          className,
          functionName,
          map.getString("fileName"),
          map.hasKey("lineNumber") ? map.getInt("lineNumber") : -1
        );
        stackList.add(stack);

        if(additional != null){
          StackTraceElement s = new StackTraceElement(
            "Additional Parameters",
            additional.toString(),
            map.getString("fileName"),
            map.hasKey("lineNumber") ? map.getInt("lineNumber") : -1
          );
          stackList.add(s);
        }
      }
      StackTraceElement[] stackTrace =  new StackTraceElement[stackList.size()];
      Exception e = new Exception(name + "\n" + reason);
      stackTrace = stackList.toArray(stackTrace);
      e.setStackTrace(stackTrace);
      FirebaseCrashlytics.getInstance().recordException(e);
  }

  // @ReactMethod
  // public void setBoolValue(final String key, final boolean boolValue) {
  //   FirebaseCrashlytics.getInstance().setCustomKey(key, boolValue);
  // }

  @ReactMethod
  public void setFloatValue(final String key, final float floatValue) {
    FirebaseCrashlytics.getInstance().setCustomKey(key, floatValue);
  }

  @ReactMethod
  public void setIntValue(final String key, final int intValue) {
    FirebaseCrashlytics.getInstance().setCustomKey(key, intValue);
  }

  @ReactMethod
  public void setStringValue(final String key, final String stringValue) {
    FirebaseCrashlytics.getInstance().setCustomKey(key, stringValue);
  }

  @ReactMethod
  public void setUserIdentifier(String userId) {
    FirebaseCrashlytics.getInstance().setUserId(userId);
  }

  // @ReactMethod
  // public void setUserName(String userName) {
  //   FirebaseCrashlytics.getInstance().setUserName(userName);
  // }

  // @ReactMethod
  // public void setUserEmail(String userEmail) {
  //   FirebaseCrashlytics.getInstance().setUserEmail(userEmail);
  // }

  // @ReactMethod
  // public void enableCrashlyticsCollection() {
  //   Fabric.with(getReactApplicationContext(), new Crashlytics());
  // }

  @ReactMethod
  public void enableCrashlyticsCollection(final boolean boolValue) {
    FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(boolValue);
  }

}
