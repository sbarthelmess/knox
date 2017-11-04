package com.samsung.knox.samples.kioskmode.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.enterprise.EnterpriseDeviceManager;
import android.app.enterprise.kioskmode.KioskMode;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.samsung.knox.samples.kioskmode.R;
import com.samsung.knox.samples.kioskmode.model.API;
import com.samsung.knox.samples.kioskmode.model.APIType;
import com.samsung.knox.samples.kioskmode.util.SAUIHelper;

public class KIOSKRestrictionsPolicyListAdapter extends ArrayAdapter<API> {
	private final Context context;
	public static final String TAG = KIOSKRestrictionsPolicyListAdapter.class.getSimpleName();
	EnterpriseDeviceManager enterpriseDeviceManager;

	class ViewHolderCheckBox {
		CheckBox chkBox;
		TextView txtVwIndex;

		public ViewHolderCheckBox(View mainView) {
			chkBox = (CheckBox) mainView.findViewById(R.id.checkBox1);
			txtVwIndex = (TextView) mainView.findViewById(R.id.txtVwIndex);
			chkBox.setTextColor(Color.BLACK);
			txtVwIndex.setTextColor(Color.BLACK);
		}
	}

	class ViewHolderButton {
		Button button;
		TextView txtVwIndex;

		public ViewHolderButton(View mainView) {
			button = (Button) mainView.findViewById(R.id.btn1);
			txtVwIndex = (TextView) mainView.findViewById(R.id.txtVwIndex);
			txtVwIndex.setTextColor(Color.BLACK);
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

	class ViewHolderHdr {
		TextView txtView;
		TextView txtVwIndex;

		public ViewHolderHdr(View mainView) {
			txtView = (TextView) mainView.findViewById(R.id.textView1);
			txtVwIndex = (TextView) mainView.findViewById(R.id.txtVwIndex);
			txtView.setTextColor(Color.WHITE);
			txtVwIndex.setTextColor(Color.WHITE);
		}
	}

	public KIOSKRestrictionsPolicyListAdapter(Context context, int resource, List<API> values) {

		super(context, R.layout.list_cell, values);
		this.context = context;
		this.enterpriseDeviceManager = (EnterpriseDeviceManager) context
				.getSystemService(EnterpriseDeviceManager.ENTERPRISE_POLICY_SERVICE);
	}

	@Override
	public int getViewTypeCount() {
		return 4;
	}

	@Override
	public int getItemViewType(int position) {
		API api = (API) getItem(position);
		switch (api.apiType) {
		case SETTER:
			return 0;
		case UI:
			return 1;
		case OPERATION_BUTTON:
			return 2;
		default:
			return 3;
		}
	};

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = null;
		ViewHolderPlain viewHolder = null;
		ViewHolderCheckBox viewHolderCb = null;
		ViewHolderHdr viewHolderHdr = null;
		ViewHolderButton viewHolderBtn = null;

		API api = (API) getItem(position);

		if (convertView == null) {
			switch (api.apiType) {
			case SETTER:
				rowView = inflater.inflate(R.layout.list_cell, parent, false);
				rowView.setTag(new ViewHolderCheckBox(rowView));
				break;
			case UI:
				rowView = inflater.inflate(R.layout.list_cell_header, parent, false);
				rowView.setTag(new ViewHolderHdr(rowView));
				break;
			case OPERATION_BUTTON:
				System.out.println("setting tag button " + rowView);
				rowView = inflater.inflate(R.layout.list_cell_button, parent, false);
				rowView.setBackgroundResource(R.drawable.list_selector);
				rowView.setTag(new ViewHolderButton(rowView));
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
		case SETTER:
			viewHolderCb = (ViewHolderCheckBox) rowView.getTag();
			viewHolderCb.chkBox.setText(api.name);
			viewHolderCb.chkBox.setEnabled(true);
			// viewHolderCb.txtVwIndex.setText("" + position);
			final CheckBox cBox = viewHolderCb.chkBox;
			setStates(position, viewHolderCb);
			viewHolderCb.chkBox.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.i(TAG, "checkbox on click");
					invokeAPI(position, cBox.isChecked());
				}
			});
			break;

		case UI:
			viewHolderHdr = (ViewHolderHdr) rowView.getTag();
			viewHolderHdr.txtView.setText(api.name);
			// viewHolderHdr.txtVwIndex.setText("" + position);
			break;

		case OPERATION_BUTTON:
			System.out.println("getting tag button " + rowView);
			viewHolderBtn = (ViewHolderButton) rowView.getTag();
			viewHolderBtn.button.setText(api.name);
			// viewHolderBtn.txtVwIndex.setText("" + position);

			viewHolderBtn.button.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					invokeAPI(position, false);
				}
			});

			break;

		default:
			viewHolder = (ViewHolderPlain) rowView.getTag();
			viewHolder.txtView.setText(api.name);
			// viewHolder.txtVwIndex.setText("" + position);
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

	void setStates(int position, ViewHolderCheckBox viewHolderCb) {
		try {
			KioskMode kioskModeService = KioskMode.getInstance(context);
			switch (position) {
			case 0:
				viewHolderCb.chkBox.setChecked(kioskModeService.isSystemBarHidden());
				break;
			case 1:
				viewHolderCb.chkBox.setChecked(kioskModeService.isNavigationBarHidden());
				break;
			case 2:
				viewHolderCb.chkBox.setChecked(kioskModeService.isStatusBarHidden());
				break;
			case 3:
				viewHolderCb.chkBox.setChecked(kioskModeService.isTaskManagerAllowed());
				break;
			case 4:
				viewHolderCb.chkBox.setChecked(kioskModeService.isAirCommandModeAllowed());
				break;
			case 5:
				viewHolderCb.chkBox.setChecked(kioskModeService.isAirViewModeAllowed());
				break;
			case 6:
				viewHolderCb.chkBox.setChecked(kioskModeService.isHardwareKeyAllowed(KeyEvent.KEYCODE_VOLUME_UP));
				break;
			case 7:
				viewHolderCb.chkBox.setChecked(kioskModeService.isMultiWindowModeAllowed());
				break;
			}
		} catch (SecurityException se) {
			se.printStackTrace();
		}
	}

	void invokeAPI(int position, boolean extra) {

		API api = (API) getItem(position);
		KioskMode kioskModeService = KioskMode.getInstance(context);
		try {

			boolean success = false;
			boolean result = false;
			List<Integer> listHwKeys = null;

			switch (position) {

			case 0: // API to hide the device status bar, system bar, or
					// navigation bar.
				success = kioskModeService.hideSystemBar(extra);
				SAUIHelper.showToast(context, api.name + " : " + (success ? extra : !extra));
				break;

			case 1: // API to hide the device navigation bar.
				success = kioskModeService.hideNavigationBar(extra);
				SAUIHelper.showToast(context, api.name + " : " + (success ? extra : !extra));
				break;

			case 2: // API to hide the device status bar.
				success = kioskModeService.hideStatusBar(extra);
				SAUIHelper.showToast(context, api.name + " : " + (success ? extra : !extra));
				break;

			case 3: // API to allow or deny task manager application and recent
					// applications window.
				success = kioskModeService.allowTaskManager(extra);
				SAUIHelper.showToast(context, api.name + " : " + (success ? extra : !extra));
				break;

			case 4: // API to allow or deny Air Command on device.
				success = kioskModeService.allowAirCommandMode(extra);
				SAUIHelper.showToast(context, api.name + " : " + (success ? extra : !extra));
				break;

			case 5: // API to allow or deny Air View on device.
				success = kioskModeService.allowAirViewMode(extra);
				SAUIHelper.showToast(context, api.name + " : " + (success ? extra : !extra));
				break;

			case 6: // API to allow or block hardware keys on the device.
				listHwKeys = new ArrayList<Integer>();
				listHwKeys.add(KeyEvent.KEYCODE_VOLUME_UP);
				kioskModeService.allowHardwareKeys(listHwKeys, extra);
				SAUIHelper.showToast(context, api.name + " : Volume UP key - " + (extra ? "Allowed" : "Not Allowed"));
				break;

			case 7: // API to allow or deny multi window mode on device.
				success = kioskModeService.allowMultiWindowMode(extra);
				SAUIHelper.showToast(context, api.name + " : " + (success ? extra : !extra));
				break;

			case 8: // API to clear all notifications from status bar, except
					// those flagged with Notification.FLAG_ONGOING_EVENT or
					// Notification.FLAG_NO_CLEAR.
				success = kioskModeService.clearAllNotifications();
				SAUIHelper.showToast(context, api.name + " : " + (success));
				break;

			case 9: // API to clear recent tasks list.
				success = kioskModeService.wipeRecentTasks();
				SAUIHelper.showToast(context, api.name + " : " + (success));
				break;

			case 10: // API to check whether the status bar, the system bar, or
						// the navigation bar is hidden.
				result = kioskModeService.isSystemBarHidden();
				SAUIHelper.showToast(context, api.name + " : " + result);
				break;

			case 11: // API to check whether the navigation bar is hidden
				result = kioskModeService.isNavigationBarHidden();
				SAUIHelper.showToast(context, api.name + " : " + result);
				break;

			case 12: // API to check whether the status bar is hidden.
				result = kioskModeService.isStatusBarHidden();
				SAUIHelper.showToast(context, api.name + " : " + result);
				break;

			case 13: // API to check whether task manager and recent
						// applications are allowed.
				result = kioskModeService.isTaskManagerAllowed();
				SAUIHelper.showToast(context, api.name + " : " + result);
				break;

			case 14: // API to check whether Air Command is allowed.
				result = kioskModeService.isAirCommandModeAllowed();
				SAUIHelper.showToast(context, api.name + " : " + result);
				break;

			case 15: // API to check whether Air View is allowed.
				result = kioskModeService.isAirViewModeAllowed();
				SAUIHelper.showToast(context, api.name + " : " + result);
				break;

			case 16: // API to check whether Air View is allowed.
				result = kioskModeService.isHardwareKeyAllowed(KeyEvent.KEYCODE_VOLUME_UP);
				SAUIHelper.showToast(context, api.name + " : " + result);
				break;

			case 17: // API to check if multi window mode is allowed or not
						// allowed.
				result = kioskModeService.isMultiWindowModeAllowed();
				SAUIHelper.showToast(context, api.name + " : " + result);
				break;

			case 18: // API to get a list of all blocked hardware keys.
				listHwKeys = kioskModeService.getAllBlockedHardwareKeys();
				String csv1 = listHwKeys.toString().replace("[", "").replace("]", "").replace(", ", ",");
				SAUIHelper.showToast(context, api.name + " : " + csv1);
				break;

			case 19: // API to get the list of hardware keys available on the
						// device.
				listHwKeys = kioskModeService.getHardwareKeyList();
				String csv2 = listHwKeys.toString().replace("[", "").replace("]", "").replace(", ", ",");
				SAUIHelper.showToast(context, api.name + " : " + csv2);
				break;

			case 20: // API to return package name of kiosk home screen.
				String kioskHomePkg = kioskModeService.getKioskHomePackage();
				SAUIHelper.showToast(context, api.name + " : " + kioskHomePkg);
				break;
			}
		} catch (SecurityException se) {

		}
	}
}