package com.samsung.knox.samples.kioskmode.receivers;

import android.app.enterprise.license.EnterpriseLicenseManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.samsung.knox.samples.kioskmode.constants.SAConstants;
import com.samsung.knox.samples.kioskmode.ui.AdminLicenseActivationActivity;
import com.samsung.knox.samples.kioskmode.util.SAUIHelper;

public class LicenseReceiver extends BroadcastReceiver {

	private Context mContext;
	SharedPreferences.Editor adminLicensePrefsEditor;
	SharedPreferences adminLicensePrefs;
	static AdminLicenseActivationActivity activityObj;

	public LicenseReceiver() {

	}

	public LicenseReceiver(AdminLicenseActivationActivity activityObj) {
		LicenseReceiver.activityObj = activityObj;
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		mContext = context;
		adminLicensePrefsEditor = mContext.getSharedPreferences(SAConstants.MY_PREFS_NAME, Context.MODE_PRIVATE).edit();

		adminLicensePrefs = mContext.getSharedPreferences(SAConstants.MY_PREFS_NAME, Context.MODE_PRIVATE);

		if (intent != null) {
			String action = intent.getAction();
			if (action == null) {
				return;
			} else if (action.equals(EnterpriseLicenseManager.ACTION_LICENSE_STATUS)) {
				int errorCode = intent.getIntExtra(EnterpriseLicenseManager.EXTRA_LICENSE_ERROR_CODE,
						SAConstants.DEFAULT_ERROR);

				if (errorCode == EnterpriseLicenseManager.ERROR_NONE) {
					SAUIHelper.showToast(mContext, SAConstants.ELM_ACTIVATION_SUCCESS);
					if (LicenseReceiver.activityObj != null) {
						LicenseReceiver.activityObj.setUIStates(SAConstants.RESULT_ELM_ACTIVATED);
					} else {
						saveState(SAConstants.RESULT_ELM_ACTIVATED);
					}
				} else {
					SAUIHelper.showToast(mContext, SAConstants.ELM_ACTIVATION_FAILURE + errorCode);
				}
			}
		}
	}

	public void saveState(int condition) {

		switch (condition) {

		case SAConstants.RESULT_ELM_ACTIVATED:

			adminLicensePrefsEditor.putBoolean(SAConstants.ELM, true);
			adminLicensePrefsEditor.commit();

			break;

		default:
			break;

		}

	}

}
