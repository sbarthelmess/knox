package com.samsung.knox.samples.kioskmode.util;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.samsung.knox.samples.kioskmode.R;
import com.samsung.knox.samples.kioskmode.constants.SAConstants;

public class SAUtils {

	public static final String TAG = SAUtils.class.getSimpleName();

	public static boolean isVRSAvailable = false;
	public static boolean isLargeLayout = true;

	public static int brandingDrawable = 0;
	public static int brandingImageDrawable = 0;
	public static int brandingRoundedCornersDrawable = 0;

	private SAUtils() {
		throw new AssertionError();
	}

	@SuppressWarnings("finally")
	public static String getAppVersion(Context ctxt) {
		String appVersion = null;
		try {
			PackageInfo pinfo = ctxt.getPackageManager().getPackageInfo(ctxt.getPackageName(), 0);
			appVersion = ctxt.getResources().getString(R.string.app_version_text) + " " + pinfo.versionName;
		} catch (Exception e) {
			// SALog.e(TAG, e.toString());
		} finally {
			return appVersion;
		}
	}

	public static String getDeviceId(Context ctxt) {
		return Secure.getString(ctxt.getContentResolver(), Secure.ANDROID_ID);
	}

	public static String getDeviceModel(Context ctxt) {
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		if (model.startsWith(manufacturer)) {
			return model;
		} else {
			return manufacturer + " " + model;
		}
	}

	public static String getCarrierName(Context ctxt) {
		TelephonyManager manager = (TelephonyManager) ctxt.getSystemService(Context.TELEPHONY_SERVICE);
		return manager.getNetworkOperatorName();
	}

	public static String getTruncatedString(String str) {
		if (str.length() > 8)
			return str.substring(0, 5) + "...";
		return str;
	}

	public static void goToWebPage(Context context, String url) {
		Uri uriUrl = Uri.parse(url);
		Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
		context.startActivity(launchBrowser);
	}

	public static boolean isConnectingToInternet(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}

	public static boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	public static boolean hasFroyo() {
		// Can use static final constants like FROYO, declared in later versions
		// of the OS since they are inlined at compile time. This is guaranteed
		// behavior.
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	public static boolean hasGingerbread() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}

	public static boolean hasHoneycomb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

	public static boolean hasHoneycombMR1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
	}

	public static boolean hasJellyBean() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
	}

	public static boolean hasKitKat() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
	}

	/**
	 * This method converts dp unit to equivalent pixels, depending on device
	 * density.
	 * 
	 * @param dp
	 *            A value in dp (density independent pixels) unit. Which we need
	 *            to convert into pixels
	 * @param context
	 *            Context to get resources and device specific display metrics
	 * @return A float value to represent px equivalent to dp depending on
	 *         device density
	 */
	public static float convertDpToPixel(float dp, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}

	/**
	 * This method converts device specific pixels to density independent
	 * pixels.
	 * 
	 * @param px
	 *            A value in px (pixels) unit. Which we need to convert into db
	 * @param context
	 *            Context to get resources and device specific display metrics
	 * @return A float value to represent dp equivalent to px value
	 */
	public static float convertPixelsToDp(float px, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = px / (metrics.densityDpi / 160f);
		return dp;
	}

	public static float pixelsToSp(float px, Context context) {
		Resources resources = context.getResources();
		float scaledDensity = resources.getDisplayMetrics().scaledDensity;
		return px / scaledDensity;
	}

	public final static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}

	public static int getVersionCode(Context context) {
		PackageInfo packageInfo;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			int versionCode = packageInfo.versionCode;
			// SALog.i(TAG, "versionCode: " + versionCode);
			return versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return SAConstants.NONE;
	}
}