package com.samsung.knox.samples.kioskmode.util;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import com.samsung.knox.samples.kioskmode.R;

public class SAUIHelper {

	public static final String TAG = SAUIHelper.class.getSimpleName();

	private SAUIHelper() {
		throw new AssertionError("Cannot create object of SAUIHelper");
	}

	public static void showAlert(Context ctxt, String heading, String message, String okButtonText) {
		if (ctxt != null)
			new AlertDialog.Builder(ctxt).setTitle(heading).setMessage(message).setNegativeButton(okButtonText, null)
					.show();
	}

	public static void showNoInternetConnectionAlert(Context ctxt) {
		if (ctxt != null)
			showAlert(ctxt, null, ctxt.getString(R.string.no_internet_detail_message), ctxt.getString(R.string.ok));
	}

	public static void showToast(Context ctxt, String message) {
		if (ctxt != null)
			Toast.makeText(ctxt, message, Toast.LENGTH_LONG).show();
	}

}