package com.samsung.knox.samples.kioskmode.util;

import android.widget.EditText;

public class SAValidation {

	static SAValidation saValidationObj;

	private SAValidation() {

	}

	public static SAValidation getInstance() {

		if (saValidationObj == null) {
			saValidationObj = new SAValidation();
		}
		return saValidationObj;
	}

	public boolean isEditTextFilled(EditText editTextObj) {

		if (!editTextObj.getText().toString().equalsIgnoreCase("")) {
			return true;
		}
		return false;
	}
}