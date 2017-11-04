package com.samsung.knox.samples.kioskmode.ui;

import java.util.ArrayList;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.enterprise.EnterpriseDeviceManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.samsung.knox.samples.kioskmode.R;
import com.samsung.knox.samples.kioskmode.constants.SAConstants;
import com.samsung.knox.samples.kioskmode.model.API;
import com.samsung.knox.samples.kioskmode.model.APIType;
import com.samsung.knox.samples.kioskmode.model.Model;
import com.samsung.knox.samples.kioskmode.model.SAFESDKVersion;
import com.sec.enterprise.knox.EnterpriseKnoxManager;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	static Model mModelObj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab().setText(mSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
		}

		mModelObj = new Model(MainActivity.this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	void startAdminActivity() {
		Intent intent = new Intent(MainActivity.this, AdminLicenseActivationActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_disable_admin:

			Model mModelObj = new Model(MainActivity.this);

			if (mModelObj.deactivateAdmin()) {
				SharedPreferences sharedpreferences = getSharedPreferences(SAConstants.MY_PREFS_NAME,
						Context.MODE_PRIVATE);
				Editor editor = sharedpreferences.edit();
				editor.putBoolean(SAConstants.ADMIN, false);
				editor.putBoolean(SAConstants.ELM, false);
				editor.commit();
				Intent intent = new Intent(MainActivity.this, AdminLicenseActivationActivity.class);
				startActivity(intent);
				finish();
			}

			return true;

		case R.id.action_about:

			Intent aboutAppIntent = new Intent(this, AboutActivity.class);
			startActivity(aboutAppIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a KIOSKModesFragment (defined as a static inner
			// class
			// below).
			switch (position) {
			case 0:
				return KIOSKModesFragment.newInstance(position);
			case 1:
				return KIOSKRestrictionsFragment.newInstance(position);
			}
			return null;
		}

		@Override
		public int getCount() {
			// Show 2 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class KIOSKModesFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		protected static final String TAG = "KIOSK_MODE_FRAGMENT";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		EnterpriseKnoxManager knoxManager;
		EnterpriseDeviceManager enterpriseDeviceManager;
		ListView listVwKioskModesOptions;
		KIOSKModesPolicyListAdapter adapterKIOSKModes;
		ArrayList<API> arrAPIs = new ArrayList<API>();

		public static KIOSKModesFragment newInstance(int sectionNumber) {
			KIOSKModesFragment fragment = new KIOSKModesFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public KIOSKModesFragment() {
		}

		@Override
		public void onResume() {
			super.onResume();
			adapterKIOSKModes.notifyDataSetChanged();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			final View rootView = inflater.inflate(R.layout.fragment_main_2, container, false);

			enterpriseDeviceManager = (EnterpriseDeviceManager) getActivity().getSystemService(
					EnterpriseDeviceManager.ENTERPRISE_POLICY_SERVICE);

			populateAPIList();
			adapterKIOSKModes = new KIOSKModesPolicyListAdapter((MainActivity) getActivity(), 0, arrAPIs);

			listVwKioskModesOptions = (ListView) rootView.findViewById(R.id.list_vw_kiosk_modes);
			listVwKioskModesOptions.setTextFilterEnabled(true);
			listVwKioskModesOptions.setAdapter(adapterKIOSKModes);
			listVwKioskModesOptions.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

					// API api = (API)
					// listVwKioskModesOptions.getItemAtPosition(position);
					adapterKIOSKModes.invokeAPI(position, null);
				}
			});
			return rootView;
		}

		void populateAPIList() {
			arrAPIs.add(new API(getString(R.string.kiosk_mode_1), APIType.OPERATION, false, SAFESDKVersion.VER_3_0,
					null));
			arrAPIs.add(new API(getString(R.string.kiosk_mode_2), APIType.OPERATION_EXTRA_INFO, false,
					SAFESDKVersion.VER_3_0, getString(R.string.kiosk_enable_pre_configs)));
			arrAPIs.add(new API(getString(R.string.kiosk_mode_3), APIType.OPERATION, false, SAFESDKVersion.VER_3_0,
					null));
			arrAPIs.add(new API(getString(R.string.kiosk_mode_4), APIType.OPERATION, false, SAFESDKVersion.VER_3_0,
					null));
			arrAPIs.add(new API(getString(R.string.kiosk_mode_5), APIType.OPERATION_EXTRA_INFO, false,
					SAFESDKVersion.VER_3_0, getString(R.string.kiosk_disable_pre_configs)));
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class KIOSKRestrictionsFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		protected static final String TAG = "KIOSK_MODE_FRAGMENT";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		Button btnKiosk;
		ListView containerManagementPolicyList;
		KIOSKRestrictionsPolicyListAdapter kioskRestrictionsPolicyAdapter;
		ArrayList<API> arrAPIs = new ArrayList<API>();
		EnterpriseKnoxManager knoxManager;
		EnterpriseDeviceManager enterpriseDeviceManager;

		public static KIOSKRestrictionsFragment newInstance(int sectionNumber) {
			KIOSKRestrictionsFragment fragment = new KIOSKRestrictionsFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public void onResume() {
			super.onResume();
			kioskRestrictionsPolicyAdapter.notifyDataSetChanged();
		}

		public KIOSKRestrictionsFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_kiosk_restrictions, container, false);

			populateAPIList();
			kioskRestrictionsPolicyAdapter = new KIOSKRestrictionsPolicyListAdapter(getActivity(), 0, arrAPIs);

			knoxManager = EnterpriseKnoxManager.getInstance();
			enterpriseDeviceManager = (EnterpriseDeviceManager) getActivity().getSystemService(
					EnterpriseDeviceManager.ENTERPRISE_POLICY_SERVICE);

			containerManagementPolicyList = (ListView) rootView.findViewById(R.id.list_vw_kiosk_restrictions);
			containerManagementPolicyList.setTextFilterEnabled(true);
			containerManagementPolicyList.setAdapter(kioskRestrictionsPolicyAdapter);
			containerManagementPolicyList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					kioskRestrictionsPolicyAdapter.invokeAPI(position, false);
				}
			});
			return rootView;
		}

		void populateAPIList() {
			arrAPIs.add(new API(getString(R.string.api_hide_system_bar), APIType.SETTER, false, SAFESDKVersion.VER_3_0,
					null));
			arrAPIs.add(new API(getString(R.string.api_hide_nav_bar), APIType.SETTER, false, SAFESDKVersion.VER_3_0,
					null));
			arrAPIs.add(new API(getString(R.string.api_hide_status_bar), APIType.SETTER, false, SAFESDKVersion.VER_3_0,
					null));
			arrAPIs.add(new API(getString(R.string.api_allow_task_manager), APIType.SETTER, false,
					SAFESDKVersion.VER_3_0, null));
			arrAPIs.add(new API(getString(R.string.api_allow_air_command_mode), APIType.SETTER, false,
					SAFESDKVersion.VER_3_0, null));
			arrAPIs.add(new API(getString(R.string.api_allow_air_view_mode), APIType.SETTER, false,
					SAFESDKVersion.VER_3_0, null));
			arrAPIs.add(new API(getString(R.string.api_allow_hardware_keys), APIType.SETTER, false,
					SAFESDKVersion.VER_3_0, null));
			arrAPIs.add(new API(getString(R.string.api_allow_multi_mode), APIType.SETTER, false,
					SAFESDKVersion.VER_3_0, null));
			arrAPIs.add(new API(getString(R.string.api_clear_all_notifications), APIType.OPERATION, false,
					SAFESDKVersion.VER_3_0, null));
			arrAPIs.add(new API(getString(R.string.api_wipe_recents), APIType.OPERATION, false, SAFESDKVersion.VER_3_0,
					null));
			arrAPIs.add(new API(getString(R.string.api_is_system_bar_hidden), APIType.GETTER, false,
					SAFESDKVersion.VER_3_0, null));
			arrAPIs.add(new API(getString(R.string.api_is_nav_bar_hidden), APIType.GETTER, false,
					SAFESDKVersion.VER_3_0, null));
			arrAPIs.add(new API(getString(R.string.api_is_status_bar_hidden), APIType.GETTER, false,
					SAFESDKVersion.VER_3_0, null));
			arrAPIs.add(new API(getString(R.string.api_is_task_manager_allowed), APIType.GETTER, false,
					SAFESDKVersion.VER_3_0, null));
			arrAPIs.add(new API(getString(R.string.api_is_air_cmd_mode_allowed), APIType.GETTER, false,
					SAFESDKVersion.VER_3_0, null));
			arrAPIs.add(new API(getString(R.string.api_is_air_view_mode_allowed), APIType.GETTER, false,
					SAFESDKVersion.VER_3_0, null));
			arrAPIs.add(new API(getString(R.string.api_is_hardware_key_allowed), APIType.GETTER, false,
					SAFESDKVersion.VER_3_0, null));
			arrAPIs.add(new API(getString(R.string.api_is_multi_mode_window_allowed), APIType.GETTER, false,
					SAFESDKVersion.VER_3_0, null));
			arrAPIs.add(new API(getString(R.string.api_get_all_blocked_hardware_keys), APIType.GETTER, false,
					SAFESDKVersion.VER_3_0, null));
			arrAPIs.add(new API(getString(R.string.api_get_hardware_key_list), APIType.GETTER, false,
					SAFESDKVersion.VER_3_0, null));
			arrAPIs.add(new API(getString(R.string.api_get_kiosk_home_pkg), APIType.GETTER, false,
					SAFESDKVersion.VER_3_0, null));
		}
	}
}
