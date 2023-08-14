package com.squorpikkor.app.treasurebox;

import static android.provider.Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

/**
 * Запрос разрешений (permission) в отдельном классе. Теперь не нужно каждый раз писать лаунчеры
 * или askPermission вместе с onPermissionRequest. Упрощает и разгружает активити (фрагмент)
 *
 * Все запросы помещаются в очередь. Если запросов было несколько, то они будут выполняться не
 * одновременно, каждый последующий запустится только после завершения предыдущего.
 *
 *
 * Использование:
 * PermissionChecker permissionChecker = new PermissionChecker(this);
 *         if (savedInstanceState == null) {
 *             permissionChecker.askStorage(granted -> init());
 *             permissionChecker.askLocationBackground();
 *         }
 *
 * v-1.02
 * */
@SuppressWarnings("unused")
public class PermissionChecker {

    private final AppCompatActivity mActivity;
// Лаунчеры ----------------------------------------------------------------------------------------
    private final ActivityResultLauncher<String> requestPermissionLauncher;
    private final ActivityResultLauncher<Intent> requestLocationLauncher_11;
    private final ActivityResultLauncher<String> requestLocationLauncher;
    private final ActivityResultLauncher<String> requestFineLocationForBGLauncher;
    private final ActivityResultLauncher<String> requestBGLauncher;

    public PermissionChecker(AppCompatActivity activity) {
        mActivity = activity;
        //Пермишн лаунчер для доступа к файлам
        requestPermissionLauncher = activity.registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                PermissionChecker.this::_storageResult_
        );
        //Лаунчер запроса на доступ к файлам для Андройда 11
        requestLocationLauncher_11 = activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        PermissionChecker.this._storageResult_(Environment.isExternalStorageManager());
                    }
                });
        requestLocationLauncher = activity.registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                PermissionChecker.this::_locationResult_);
        requestBGLauncher = activity.registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                PermissionChecker.this::_locationBackgroundResult_);
        requestFineLocationForBGLauncher = activity.registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                granted -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        if (granted) requestBGLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
                        else _locationBackgroundResult_(false);
                    } else {
                        _locationBackgroundResult_(granted);
                    }
                });
    }


    private void checkLocation() {
        requestLocationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void checkBackgroundLocation() {
        requestFineLocationForBGLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void checkStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) _storageResult_(true);
            else requestLocationLauncher_11.launch(new Intent(ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            switch (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                case PackageManager.PERMISSION_DENIED:  requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE); break;
                case PackageManager.PERMISSION_GRANTED: _storageResult_(true); break;
            }
        } else {
            _storageResult_(true);
        }
    }
// Очередь -----------------------------------------------------------------------------------------
    private static final int LOCATION    = 1;
    private static final int LOCATION_BG = 2;
    private static final int STORAGE     = 3;

    private final ArrayList<Integer> queue = new ArrayList<>();
    private boolean started;

    private void add(int permission) {
        queue.add(permission);
        if (!started) {
            started = true;
            doNext();
        }
    }

    private void doNext() {
        if (queue.size()== 0) {
            started = false;
            return;
        }
        int permission = queue.get(0);
        queue.remove(0);
        switch (permission) {
            case LOCATION:      checkLocation(); break;
            case LOCATION_BG:   checkBackgroundLocation(); break;
            case STORAGE:       checkStorage(); break;
        }
    }
//  ------------------------------------------------------------------------------------------------
    private void _locationResult_(boolean granted) {
        if (locationListener!=null) locationListener.onResult(granted);
        doNext();
    }

    private void _locationBackgroundResult_(boolean granted) {
        if (locationBGListener!=null) locationBGListener.onResult(granted);
        doNext();
    }

    private void _storageResult_(boolean granted) {
        if (storageListener!=null) storageListener.onResult(granted);
        doNext();
    }

//  ------------------------------------------------------------------------------------------------
    private OnResultListener storageListener;
    private OnResultListener locationListener;
    private OnResultListener locationBGListener;

    /**Что будем делать при получении ответа на запрос разрешения*/
    public interface OnResultListener {
        /**Ответ на запрос*/
        void onResult(boolean granted);
    }

    /**Разрешение на доступ к местонахождению. Просто запрос, на ответ ничего не делаем*/
    public void askLocation() {
        add(LOCATION);
    }

    /**Разрешение на доступ к местонахождению. Можно задать, что выполнять при получении ответа на запрос*/
    public void askLocation(OnResultListener onResultListener) {
        locationListener = onResultListener;
        add(LOCATION);
    }

    /**Разрешение на доступ к местонахождению в фоне. Просто запрос, на ответ ничего не делаем*/
    public void askLocationBackground() {
        add(LOCATION_BG);
    }

    /**Разрешение на доступ к местонахождению в фоне. Можно задать, что выполнять при получении ответа на запрос*/
    public void askLocationBackground(OnResultListener onResultListener) {
        locationBGListener = onResultListener;
        add(LOCATION_BG);
    }

    /**Разрешение на доступ к файлам. Просто запрос, на ответ ничего не делаем*/
    public void askStorage() {
        add(STORAGE);
    }

    /**Разрешение на доступ к файлам. Можно задать, что выполнять при получении ответа на запрос*/
    public void askStorage(OnResultListener onResultListener) {
        storageListener = onResultListener;
        add(STORAGE);
    }

}
