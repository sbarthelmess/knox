package com.samsung.knox.samples.kioskmode.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class SANetworkUtils {

	public static enum NET_INFO {
		TYPE_NOT_CONNECTED, TYPE_WIFI, TYPE_MOBILE
	};

	public static int TYPE_WIFI = 1;
	public static int TYPE_MOBILE = 2;
	public static int TYPE_NOT_CONNECTED = 0;

	private SANetworkUtils() {
		throw new AssertionError();
	}

	public static boolean isOnline(Context context) {
		if (SANetworkUtils.getConnectivityStatus(context) == NET_INFO.TYPE_NOT_CONNECTED)
			return false;
		return true;
	}

	public static NET_INFO getConnectivityStatus(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
			if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
				return NET_INFO.TYPE_WIFI;

			if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
				return NET_INFO.TYPE_MOBILE;
		}
		return NET_INFO.TYPE_NOT_CONNECTED;
	}

	public static String getConnectivityStatusString(Context context) {
		NET_INFO conn = SANetworkUtils.getConnectivityStatus(context);
		String status = null;

		switch (conn) {
		case TYPE_NOT_CONNECTED:
			status = "Not connected to Internet";
			break;

		case TYPE_WIFI:
			status = "Wifi enabled";
			break;

		case TYPE_MOBILE:
			status = "Mobile data enabled";
			break;

		default:
			status = "network status unknown";
			break;
		}
		return status;
	}
}