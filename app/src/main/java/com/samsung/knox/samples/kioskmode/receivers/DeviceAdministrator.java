package com.samsung.knox.samples.kioskmode.receivers;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.samsung.knox.samples.kioskmode.constants.SAConstants;
import com.samsung.knox.samples.kioskmode.ui.AdminLicenseActivationActivity;
import com.samsung.knox.samples.kioskmode.util.SAUIHelper;

public class DeviceAdministrator extends DeviceAdminReceiver {

	SharedPreferences.Editor adminLicensePrefsEditor;
	SharedPreferences adminLicensePrefs;
	static AdminLicenseActivationActivity activityObj;

	public DeviceAdministrator() {
	}

	public DeviceAdministrator(AdminLicenseActivationActivity activityObj) {
		DeviceAdministrator.activityObj = activityObj;
	}

	@Override
	public void onEnabled(Context context, Intent intent) {
		adminLicensePrefsEditor = context.getSharedPreferences(SAConstants.MY_PREFS_NAME, Context.MODE_PRIVATE).edit();

		adminLicensePrefs = context.getSharedPreferences(SAConstants.MY_PREFS_NAME, Context.MODE_PRIVATE);

		SAUIHelper.showToast(context, SAConstants.DEVICE_ADMIN_ENABLED);
		if (DeviceAdministrator.activityObj != null) {
			DeviceAdministrator.activityObj.setUIStates(SAConstants.RESULT_ENABLE_ADMIN);
		} else {
			saveState(SAConstants.RESULT_ENABLE_ADMIN);
		}
	}

	@Override
	public CharSequence onDisableRequested(Context context, Intent intent) {
		return SAConstants.DISABLE_ADMIN_WARNING;
	}

	@Override
	public void onDisabled(Context context, Intent intent) {
		adminLicensePrefsEditor = context.getSharedPreferences(SAConstants.MY_PREFS_NAME, Context.MODE_PRIVATE).edit();

		adminLicensePrefs = context.getSharedPreferences(SAConstants.MY_PREFS_NAME, Context.MODE_PRIVATE);
		SAUIHelper.showToast(context, SAConstants.DEVICE_ADMIN_DISABLED);
		if (DeviceAdministrator.activityObj != null) {
			DeviceAdministrator.activityObj.setUIStates(SAConstants.RESULT_DISABLE_ADMIN);
		} else {
			saveState(SAConstants.RESULT_DISABLE_ADMIN);
		}
	}

	public void saveState(int condition) {

		switch (condition) {

		case SAConstants.RESULT_ENABLE_ADMIN:

			adminLicensePrefsEditor.putBoolean(SAConstants.ADMIN, true);
			adminLicensePrefsEditor.commit();

			break;

		case SAConstants.RESULT_DISABLE_ADMIN:

			adminLicensePrefsEditor.putBoolean(SAConstants.ADMIN, false);
			adminLicensePrefsEditor.putBoolean(SAConstants.ELM, false);
			adminLicensePrefsEditor.commit();

			break;

		default:
			break;

		}

	}

}
