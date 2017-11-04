package com.samsung.knox.samples.kioskmode.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.widget.EditText;

import com.samsung.knox.samples.kioskmode.R;

public class FragmentDialogInput extends DialogFragment implements OnClickListener {
	private MainActivity context;
	private DialogInputTextListener listener;

	public interface FragmentDialogInputListener {
		void onFinishEditDialog(String inputText);
	}

	private EditText mEditText;

	public FragmentDialogInput() {
		// Empty constructor required for DialogFragment
	}

	public void setContext(MainActivity context) {
		this.context = context;
	}

	public void setDialogInputTextListenerObj(DialogInputTextListener listener) {
		this.listener = listener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		mEditText = new EditText(context);
		mEditText.setInputType(InputType.TYPE_CLASS_TEXT);

		return new AlertDialog.Builder(getActivity()).setTitle(R.string.kiosk_mode_3_package)
				.setMessage(R.string.enter_custom_launcher_pkg).setPositiveButton(R.string.ok, this)
				.setNegativeButton(R.string.cancel, null).setView(mEditText).create();
	}

	@Override
	public void onClick(DialogInterface dialog, int position) {

		String value = mEditText.getText().toString();
		listener.setDialogInputTextValue(value);
		dialog.dismiss();
	}
}