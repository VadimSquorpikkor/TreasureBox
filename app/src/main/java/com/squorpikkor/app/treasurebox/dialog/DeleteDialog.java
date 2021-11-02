package com.squorpikkor.app.treasurebox.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squorpikkor.app.treasurebox.R;
import com.squorpikkor.app.treasurebox.crypto.Encrypter2;

public class DeleteDialog extends BaseDialog{

    private int position;

    public DeleteDialog() {
    }

    public DeleteDialog(int position) {
        this.position = position;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        initializeWithVM(R.layout.delete_dialog);

        String docName = mViewModel.getEntitiesList().getValue().get(position).getDocName();
        String entityName = mViewModel.getEntitiesList().getValue().get(position).getName();

        ((TextView)view.findViewById(R.id.delete_text)).setText(String.format("Элемент %s будет удален из БД навсегда", entityName));

        view.findViewById(R.id.cancel).setOnClickListener(v -> dismiss());
        view.findViewById(R.id.ok).setOnClickListener(v -> {
            mViewModel.deleteDocumentByName(docName);
            dismiss();
        });

        return dialog;
    }

}
