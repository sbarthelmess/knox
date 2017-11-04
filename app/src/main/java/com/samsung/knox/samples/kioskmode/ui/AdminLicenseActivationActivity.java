package com.samsung.knox.samples.kioskmode.ui;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.samsung.knox.samples.kioskmode.R;
import com.samsung.knox.samples.kioskmode.constants.SAConstants;
import com.samsung.knox.samples.kioskmode.model.Model;
import com.samsung.knox.samples.kioskmode.receivers.DeviceAdministrator;
import com.samsung.knox.samples.kioskmode.receivers.LicenseReceiver;
import com.samsung.knox.samples.kioskmode.util.SAUIHelper;
import com.samsung.knox.samples.kioskmode.util.SAValidation;

public class AdminLicenseActivationActivity extends Activity implements OnClickListener {

	static SAValidation saValidationObj;
	Button btnActivateDeactivateAdmin, btnActivateELM;
	EditText edtELM;

	SharedPreferences.Editor adminLicensePrefsEditor;
	SharedPreferences adminLicensePrefs;
	DevicePolicyManager mDPM;
	ComponentName mCN;
	LicenseReceiver mLicenseReceiver;
	DeviceAdministrator mDeviceAdmin;
	Model mModelObj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_license_activation);

		mModelObj = new Model(AdminLicenseActivationActivity.this);
		saValidationObj = SAValidation.getInstance();
		if (mLicenseReceiver == null) {
			mLicenseReceiver = new LicenseReceiver(AdminLicenseActivationActivity.this);
		}
		if (mDeviceAdmin == null) {
			mDeviceAdmin = new DeviceAdministrator(AdminLicenseActivationActivity.this);
		}

		btnActivateDeactivateAdmin = (Button) findViewById(R.id.admin_license_activation_btn_admin);
		btnActivateELM = (Button) findViewById(R.id.admin_license_activation_btn_activate_elm);

		edtELM = (EditText) findViewById(R.id.admin_license_activation_edt_elm_key);

		adminLicensePrefsEditor = getSharedPreferences(SAConstants.MY_PREFS_NAME, MODE_PRIVATE).edit();
		adminLicensePrefs = getSharedPreferences(SAConstants.MY_PREFS_NAME, MODE_PRIVATE);
	}

	void startHomeActivity() {
		Intent intent = new Intent(AdminLicenseActivationActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setUIStates(SAConstants.INITIAL_STATE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			if (requestCode == SAConstants.RESULT_ENABLE_ADMIN) {
				if (resultCode == Activity.RESULT_CANCELED) {
					SAUIHelper.showToast(AdminLicenseActivationActivity.this,
							SAConstants.CANCELLED_ENABLING_DEVICE_ADMIN);
				} else if (resultCode == Activity.RESULT_OK) {
					setUIStates(requestCode);
				}
			}
		} catch (Exception e) {
			Log.e(AdminLicenseActivationActivity.this.getLocalClassName(), e.getMessage());
		}
	}

	public void setUIStates(int condition) {

		switch (condition) {

		case SAConstants.INITIAL_STATE:

			if (adminLicensePrefs.getBoolean(SAConstants.ADMIN, false)) {

				btnActivateDeactivateAdmin.setText(SAConstants.DISABLE_ADMIN);
				edtELM.setEnabled(true);
				btnActivateELM.setEnabled(true);

			} else {
				btnActivateDeactivateAdmin.setText(SAConstants.ENABLE_ADMIN);
				edtELM.setText("");
				edtELM.setEnabled(false);
				btnActivateELM.setEnabled(false);
			}

			if (adminLicensePrefs.getBoolean(SAConstants.ELM, false)) {

				btnActivateELM.setEnabled(false);
				edtELM.setEnabled(false);
				startHomeActivity();
			}
			break;

		case SAConstants.RESULT_ENABLE_ADMIN:

			btnActivateDeactivateAdmin.setText(SAConstants.DISABLE_ADMIN);
			adminLicensePrefsEditor.putBoolean(SAConstants.ADMIN, true);
			adminLicensePrefsEditor.commit();
			edtELM.setEnabled(true);
			btnActivateELM.setEnabled(true);

			break;

		case SAConstants.RESULT_DISABLE_ADMIN:

			btnActivateDeactivateAdmin.setText(SAConstants.ENABLE_ADMIN);
			adminLicensePrefsEditor.putBoolean(SAConstants.ADMIN, false);
			adminLicensePrefsEditor.putBoolean(SAConstants.ELM, false);
			adminLicensePrefsEditor.commit();
			edtELM.setText("");
			edtELM.setEnabled(false);
			btnActivateELM.setEnabled(false);
			break;

		case SAConstants.RESULT_ELM_ACTIVATED:

			btnActivateELM.setEnabled(false);
			edtELM.setEnabled(false);
			adminLicensePrefsEditor.putBoolean(SAConstants.ELM, true);
			adminLicensePrefsEditor.commit();
			startHomeActivity();
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {

		Log.i(AdminLicenseActivationActivity.this.getLocalClassName(), "in onclick");

		switch (v.getId()) {

		case R.id.admin_license_activation_btn_admin:

			if (!adminLicensePrefs.getBoolean(SAConstants.ADMIN, false)) {
				if (!mModelObj.activateAdmin()) {
					SAUIHelper.showToast(AdminLicenseActivationActivity.this, SAConstants.ADMIN_ALREADY_ENABLED);
				}
			} else {
				if (mModelObj.deactivateAdmin()) {
					setUIStates(SAConstants.RESULT_DISABLE_ADMIN);
				} else {
					SAUIHelper.showToast(AdminLicenseActivationActivity.this, SAConstants.ADMIN_ALREADY_DISABLED);
				}
			}

			break;

		case R.id.admin_license_activation_btn_activate_elm:

			if (saValidationObj.isEditTextFilled(edtELM)) {
				mModelObj.activateELM(edtELM.getText().toString());
			} else {
				SAUIHelper.showAlert(AdminLicenseActivationActivity.this, SAConstants.LICENSE_KEY,
						SAConstants.ENTER_ELM_KEY, SAConstants.OK);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_about) {
			Intent aboutAppIntent = new Intent(this, AboutActivity.class);
			startActivity(aboutAppIntent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
