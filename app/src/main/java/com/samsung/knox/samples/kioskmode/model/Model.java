package com.samsung.knox.samples.kioskmode.model;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.app.enterprise.license.EnterpriseLicenseManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.samsung.knox.samples.kioskmode.constants.SAConstants;
import com.samsung.knox.samples.kioskmode.receivers.DeviceAdministrator;
import com.samsung.knox.samples.kioskmode.util.SAUIHelper;

public class Model {

	SAUIHelper saUiHelperObj;
	static Model modelObj;
	SharedPreferences.Editor adminLicensePrefsEditor;
	SharedPreferences adminLicensePrefs;
	Context mContext;
	DevicePolicyManager mDPM;
	ComponentName mCN;

	public Model(Context context) {
		mContext = context;
	}

	public boolean activateAdmin() {

		// saLoggerObj.i(SAConstants.MODEL_TAG,
		// "activateDeactivateAdmin "+btnActivateDeactivateAdmin.getText().toString());

		try {
			if (mDPM == null) {
				mDPM = (DevicePolicyManager) mContext.getSystemService(Context.DEVICE_POLICY_SERVICE);
			}
			if (mCN == null) {
				mCN = new ComponentName(mContext, DeviceAdministrator.class);
			}

			if (mDPM != null && !mDPM.isAdminActive(mCN)) {
				Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
				intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mCN);
				((Activity) mContext).startActivityForResult(intent, SAConstants.RESULT_ENABLE_ADMIN);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			Log.e(Model.this.getClass().getSimpleName(), e.getMessage());
		}
		return false;
	}

	public boolean deactivateAdmin() {

		// saLoggerObj.i(SAConstants.MODEL_TAG,
		// "activateDeactivateAdmin "+btnActivateDeactivateAdmin.getText().toString());

		if (mDPM == null) {
			mDPM = (DevicePolicyManager) mContext.getSystemService(Context.DEVICE_POLICY_SERVICE);
		}
		if (mCN == null) {
			mCN = new ComponentName(mContext, DeviceAdministrator.class);
		}

		try {
			if (mDPM != null && mDPM.isAdminActive(mCN)) {
				mDPM.removeActiveAdmin(mCN);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			Log.e(Model.this.getClass().getSimpleName(), e.getMessage());
		}
		return false;
	}

	public void activateELM(String elmKey) {

		EnterpriseLicenseManager elmMgr = null;

		try {
			if (elmMgr == null) {
				elmMgr = EnterpriseLicenseManager.getInstance(mContext);
			}

			elmMgr.activateLicense(elmKey);
		} catch (Exception e) {
			Log.e(Model.this.getClass().getSimpleName(), e.getMessage());
		}
	}

}
