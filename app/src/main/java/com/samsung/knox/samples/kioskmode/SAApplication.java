package com.samsung.knox.samples.kioskmode;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class SAApplication extends Application {

	public static final String TAG = SAApplication.class.getSimpleName();
	public static Context mAppContext;

	public static final boolean IS_DEBUG = true;
	public static final boolean IS_INFO = true;

	@Override
	public void onCreate() {
		super.onCreate();
		mAppContext = getApplicationContext();
	}

	public static Context getAppContext() {
		return mAppContext;
	}

	@Override
	public void onLowMemory() {
		Log.w(TAG, "SAApplication onLowMemory");
		System.gc();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}
}