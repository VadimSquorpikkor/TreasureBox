package com.squorpikkor.app.treasurebox.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.squorpikkor.app.treasurebox.MainActivity;
import com.squorpikkor.app.treasurebox.MainViewModel;


/**Базовый класс для диалогов. Есть варианты с ViewModel и без*/
class BaseDialog extends DialogFragment {

    Context mContext;
    MainViewModel mViewModel;
    View view;
    AlertDialog dialog;

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }

    /**Добавляет диалогу лэйаут и задает параметры*/
    @SuppressWarnings("unused")
    public void initialize(int layout) {
        dialog = new AlertDialog.Builder(mContext).create();
        Window window = dialog.getWindow();
//        if (window != null) window.setBackgroundDrawableResource(R.drawable.main_gradient);
        view = requireActivity().getLayoutInflater().inflate(layout, null);
        dialog.setView(view, 0, 0, 0, 0);
    }

    /**Дать диалогу лэйаут и задать параметры. Добавление ViewModel*/
    public void initializeWithVM(int layout) {
        dialog = new AlertDialog.Builder(mContext).create();
        Window window = dialog.getWindow();
//        if (window != null) window.setBackgroundDrawableResource(R.drawable.main_gradient);
        view = requireActivity().getLayoutInflater().inflate(layout, null);
        dialog.setView(view, 0, 0, 0, 0);
        mViewModel = new ViewModelProvider((MainActivity) mContext).get(MainViewModel.class);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return dialog;
    }
}
