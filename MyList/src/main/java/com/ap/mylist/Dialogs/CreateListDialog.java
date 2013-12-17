package com.ap.mylist.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.ap.mylist.R;

/**
 * Created by Arick Chow on 11/27/13.
 */
public class CreateListDialog extends DialogFragment {

    public interface CreateListDialogListener{
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    CreateListDialogListener mListener;

    public void onAttach(Activity activity){
        super.onAttach(activity);
        mListener = (CreateListDialogListener) activity;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.create_list_dialog,null))
                .setPositiveButton(R.string.dialog_create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.onDialogPositiveClick(CreateListDialog.this);
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.onDialogNegativeClick(CreateListDialog.this);
                    }
                });
        return builder.create();
    }
}
