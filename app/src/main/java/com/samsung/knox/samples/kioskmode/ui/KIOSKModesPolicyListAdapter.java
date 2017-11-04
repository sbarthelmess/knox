package com.samsung.knox.samples.kioskmode.ui;

import java.util.List;

import android.app.enterprise.EnterpriseDeviceManager;
import android.app.enterprise.kioskmode.KioskMode;
import android.app.enterprise.kioskmode.KioskSetting;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.samsung.knox.samples.kioskmode.R;
import com.samsung.knox.samples.kioskmode.model.API;
import com.samsung.knox.samples.kioskmode.model.APIType;
import com.samsung.knox.samples.kioskmode.util.SAUIHelper;

public class KIOSKModesPolicyListAdapter extends ArrayAdapter<API> implements DialogInputTextListener {
	private final MainActivity context;
	public static final String TAG = KIOSKModesPolicyListAdapter.class.getSimpleName();
	EnterpriseDeviceManager enterpriseDeviceManager;

	class ViewHolderButton {
		Button button;
		EditText edTextInput;
		TextView txtView;

		public ViewHolderButton(View mainView) {
			button = (Button) mainView.findViewById(R.id.btn1);
			edTextInput = (EditText) mainView.findViewById(R.id.ed_txt_pkg_name);
			txtView = (TextView) mainView.findViewById(R.id.textView1);
			txtView.setTextColor(Color.BLACK);
		}
	}

	class ViewHolderPlain {
		TextView txtView;
		TextView txtVwIndex;

		public ViewHolderPlain(View mainView) {
			txtView = (TextView) mainView.findViewById(R.id.textView1);
			txtVwIndex = (TextView) mainView.findViewById(R.id.txtVwIndex);
			txtView.setTextColor(Color.BLACK);
			txtVwIndex.setTextColor(Color.BLACK);
		}
	}

	class ViewHolderExtraInfo {
		TextView txtView;
		TextView txtVwExtraInfo;

		public ViewHolderExtraInfo(View mainView) {
			txtView = (TextView) mainView.findViewById(R.id.textView1);
			txtVwExtraInfo = (TextView) mainView.findViewById(R.id.textView2);
			txtView.setTextColor(Color.BLACK);
			txtVwExtraInfo.setTextColor(Color.GRAY);
		}
	}

	public KIOSKModesPolicyListAdapter(MainActivity context, int resource, List<API> values) {

		super(context, R.layout.list_cell, values);
		this.context = context;
		this.enterpriseDeviceManager = (EnterpriseDeviceManager) context
				.getSystemService(EnterpriseDeviceManager.ENTERPRISE_POLICY_SERVICE);
	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public int getItemViewType(int position) {
		API api = (API) getItem(position);
		switch (api.apiType) {
		case OPERATION:
			return 0;
		case OPERATION_BUTTON:
			return 1;
		case OPERATION_EXTRA_INFO:
			return 2;
		default:
			return 0;
		}
	};

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = null;
		ViewHolderPlain viewHolder = null;
		ViewHolderExtraInfo viewHolderHdr = null;

		API api = (API) getItem(position);

		if (convertView == null) {
			switch (api.apiType) {
			case OPERATION_EXTRA_INFO:
				rowView = inflater.inflate(R.layout.list_cell_plain_2, parent, false);
				rowView.setBackgroundResource(R.drawable.list_selector);
				rowView.setTag(new ViewHolderExtraInfo(rowView));
				break;
			default:
				rowView = inflater.inflate(R.layout.list_cell_plain, parent, false);
				rowView.setTag(new ViewHolderPlain(rowView));
				rowView.setBackgroundResource(R.drawable.list_selector);
			}
		} else {
			rowView = convertView;
			rowView.setEnabled(true);
			if (api.apiType != APIType.UI)
				rowView.setBackgroundResource(R.drawable.list_selector);
		}

		checkKNOXAPIVersions(rowView, api, position);

		switch (api.apiType) {

		case OPERATION_EXTRA_INFO:
			viewHolderHdr = (ViewHolderExtraInfo) rowView.getTag();
			viewHolderHdr.txtView.setText(api.name);
			viewHolderHdr.txtVwExtraInfo.setText(api.extraInfo);
			break;

		default:
			viewHolder = (ViewHolderPlain) rowView.getTag();
			viewHolder.txtView.setText(api.name);
			break;
		}

		return rowView;
	}

	void checkKNOXAPIVersions(View rowView, API api, int position) {
		if (api.safeSdkVersionReqd != null)
			System.out.println("pos " + position + "enterpriseDeviceManager.getEnterpriseSdkVer().ordinal() "
					+ enterpriseDeviceManager.getEnterpriseSdkVer().ordinal() + " api.safeSdkVersionReqd.ordinal() "
					+ api.safeSdkVersionReqd.ordinal());

		if (api.safeSdkVersionReqd != null
				&& enterpriseDeviceManager.getEnterpriseSdkVer().ordinal() < api.safeSdkVersionReqd.ordinal()) {
			rowView.setEnabled(false);
			rowView.setBackgroundColor(Color.LTGRAY);
			System.out.println("disabling");
		}
	}

	void invokeAPI(int position, String extra) {

		KioskMode kioskModeService = KioskMode.getInstance(context);

		try {
			switch (position) {
			case 0:
				/*
				 * API to enable kiosk mode with the default kiosk home screen.
				 * The use of this API requires the caller to have the
				 * "android.permission.sec.MDM_KIOSK_MODE" permission with a
				 * protection level of signature. An administrator can use this
				 * API to enable kiosk mode on the device. When kiosk mode is
				 * enabled with no arguments, the user is presented with a
				 * restricted version of the native Samsung home screen. No
				 * default application shortcuts appear when the home screen is
				 * launched for the first time, including the phone application
				 * used to make emergency calls. The most recent tasks are
				 * cleared. The uninstallation and reinstallation of the default
				 * kiosk application is also disabled. The administrator can
				 * enforce additional restrictions in kiosk mode using other
				 * kiosk mode and EDM policies. An administrator receives an
				 * error in an attempt to enable kiosk mode if it is already
				 * enabled.
				 */
				if (!kioskModeService.isKioskModeEnabled()) {
					kioskModeService.enableKioskMode();
				} else {
					SAUIHelper.showToast(context, context.getString(R.string.warn_kiosk_created));
				}
				break;

			case 1:
				// API to enable Kiosk Mode with default Kiosk home screen and
				// predefined Kiosk settings.
				// The use of this API requires the caller to add the
				// "android.permission.sec.MDM_KIOSK_MODE" permissions with a
				// protection level of signature

				if (!kioskModeService.isKioskModeEnabled()) {
					KioskSetting kiosk = new KioskSetting();
					kiosk.SettingsChanges = false;
					kiosk.StatusBarExpansion = false;
					kiosk.HomeKey = false;
					kiosk.AirCommand = false;
					kiosk.AirView = false;
					kiosk.MultiWindow = false;
					kiosk.SmartClip = false;
					kiosk.TaskManager = false;
					kiosk.NavigationBar = true;
					kiosk.StatusBar = true;
					kiosk.SystemBar = true;
					kiosk.WipeRecentTasks = true;
					kiosk.ClearAllNotifications = true;

					kioskModeService.enableKioskMode(kiosk);
				} else {
					SAUIHelper.showToast(context, context.getString(R.string.warn_kiosk_created));
				}
				break;

			case 2:
				// API to enable kiosk mode with an administrator-specified home
				// screen.

				showEditDialog();

				break;

			case 3:
				// API to disable Kiosk Mode
				if (kioskModeService.isKioskModeEnabled()) {
					kioskModeService.disableKioskMode();
				} else {
					SAUIHelper.showToast(context, context.getString(R.string.warn_create_kiosk));
				}
				break;

			case 4:
				// API to disable Kiosk Mode with predefined Kiosk settings.
				if (kioskModeService.isKioskModeEnabled()) {
					KioskSetting kiosk = new KioskSetting();
					kiosk.SettingsChanges = true;
					kiosk.StatusBarExpansion = true;
					kiosk.HomeKey = true;
					kiosk.AirCommand = true;
					kiosk.AirView = true;
					kiosk.MultiWindow = true;
					kiosk.SmartClip = true;
					kiosk.TaskManager = true;
					kiosk.NavigationBar = false;
					kiosk.StatusBar = false;
					kiosk.SystemBar = false;
					kiosk.WipeRecentTasks = false;
					kiosk.ClearAllNotifications = false;

					kioskModeService.disableKioskMode(kiosk);
				} else {
					SAUIHelper.showToast(context, context.getString(R.string.warn_create_kiosk));
				}
				break;
			}
		} catch (SecurityException se) {
			Log.e(TAG, "Security Exception : " + se);
		}
	}

	private void showEditDialog() {
		FragmentDialogInput dialog = new FragmentDialogInput();
		dialog.setContext(context);
		dialog.setDialogInputTextListenerObj(this);
		dialog.show(context.getSupportFragmentManager(), "Dialog");
	}

	public void setDialogInputTextValue(String extra) {
		KioskMode kioskModeService = KioskMode.getInstance(context);
		// API to enable kiosk mode with an administrator-specified home screen
		if (!kioskModeService.isKioskModeEnabled()) {
			kioskModeService.enableKioskMode(extra);
		} else {
			SAUIHelper.showToast(context, context.getString(R.string.warn_kiosk_created));
		}
	}
}