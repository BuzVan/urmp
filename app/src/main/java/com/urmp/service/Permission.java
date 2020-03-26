package com.urmp.service;

import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.urmp.MainActivity;

public class Permission {
    public static final String STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;


    // Функция для проверки и запроса разрешения

    public static boolean checkPermission(AppCompatActivity activity, String permission) {
        // Проверка, если разрешение не предоставлено
        if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED) {
            return false;
        } else {
            return true;
        }

    }
    public static void askPermission(AppCompatActivity activity, String permission, int requestCode){
        ActivityCompat.requestPermissions(
                activity,
                new String[]{permission},
                requestCode);
    }

}
