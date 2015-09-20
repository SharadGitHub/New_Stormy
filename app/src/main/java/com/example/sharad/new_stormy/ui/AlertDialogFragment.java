package com.example.sharad.new_stormy.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import com.example.sharad.new_stormy.R;
/**
 * Created by sharad on 11-08-2015.
 */
public class AlertDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Context contextt= getActivity();
        AlertDialog.Builder builder= new AlertDialog.Builder(contextt);
        builder.setTitle(contextt.getString(R.string.error_title))
                .setMessage(contextt.getString(R.string.error_message))
                .setPositiveButton(contextt.getString(R.string.error_ok_button_text), null);


        AlertDialog dialog= builder.create();
        return dialog;
    }
}
