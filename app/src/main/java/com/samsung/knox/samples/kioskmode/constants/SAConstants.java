package com.samsung.knox.samples.kioskmode.constants;

public interface SAConstants {

	String ENABLE_ADMIN = "Enable Admin";
	String DISABLE_ADMIN = "Disable Admin";
	String MY_PREFS_NAME = "SampleApps";
	String ADMIN = "admin";
	String ELM = "elm";
	String ADMIN_ALREADY_ENABLED = "Admin already enabled";
	String ADMIN_ALREADY_DISABLED = "Admin already disabled";
	String DEVICE_ADMIN_ENABLED = "Device Admin enabled";
	String DEVICE_ADMIN_DISABLED = "Device Admin disabled";
	String DISABLE_ADMIN_WARNING = "Do you want to disable the administrator?";
	String CANCELLED_ENABLING_DEVICE_ADMIN = "Cancelled Enabling Device Administrator";
	String CANCELLED_DISABLING_DEVICE_ADMIN = "Cancelled Disabling Device Administrator";

	String PWD_RESET_TOKEN = "SamSung1!";
	String ELM_ACTIVATION_SUCCESS = "ELM activation successful";
	String ELM_ACTIVATION_FAILURE = "ELM activation failed with errorcode: ";
	String LICENSE_KEY = "License Key";
	String ENTER_ELM_KEY = "Please enter the ELM key";
	String OK = "OK";
	String CANCEL = "Cancel";
	String LICENSE_RESULT = "License result";
	String PACKAGE_NAME = "com.samsung.knox.samples.container.rcp";
	String KNOX_B2B = "knox-b2b";
	String CONTAINER_CREATION = "Container Creation";
	String CONTAINER_CREATION_FAILED = "Container creation failed with errorcode: ";
	String CONTAINER_CREATION_PROGRESS = "Container creation in progress with id: ";
	String FAKE_INTENT = "Intent belongs to another MDM or it is a fake intent";
	String CONTAINER_CREATED_SUCCESSFULLY = "Container created successfully";
	String NOT_SUPPORTED = "This API is not supported on this device";
	String MODEL_TAG = "Model";

	String CONTACTS = "CONTACTS";
	String CALENDAR = "CALENDAR";
	String BOOKMARKS = "BOOKMARKS";
	String CLIPBOARD = "CLIPBOARD";
	String CALL_LOG = "CALL_LOG";
	String SMS = "SMS";
	String NOTIFICATIONS = "NOTIFICATIONS";
	String SHORTCUTS = "SHORTCUTS";

	int NONE = -1;
	int INITIAL_STATE = 0;
	int RESULT_ENABLE_ADMIN = 1;
	int RESULT_DISABLE_ADMIN = 2;
	int RESULT_ELM_ACTIVATED = 4;
	int DEFAULT_ERROR = -888;

}