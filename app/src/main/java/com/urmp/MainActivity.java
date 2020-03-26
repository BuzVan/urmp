package com.urmp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.urmp.service.Permission;

public class MainActivity extends AppCompatActivity {
    private static  int  STORAGE_PER_CODE = 0;
    private boolean allPerIsOk = true;
    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AlertDialog.Builder  builder = new AlertDialog.Builder(this);
        builder.setMessage("Для скачивания клиента необходимо разрешить приложению работать с памятью устройства");
        dialog = builder.create();
    }

    public void setupButtonOnClick(View view) {
        allPerIsOk = false;
        //проверка разрешений
        if (!Permission.checkPermission(this,Permission.STORAGE)){
            Permission.askPermission(this,Permission.STORAGE,STORAGE_PER_CODE);
        }
        else {
            Intent intent = new Intent(getApplicationContext(), LoadingActivity.class );
            startActivity(intent);
        }

    }
    // Эта функция вызывается, когда пользователь принимает или отклоняет разрешение.
    // Запрос кода используется, чтобы проверить, какое разрешение вызвало эту функцию.
    // Этот код запроса предоставляется, когда пользователю предлагается разрешение.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PER_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(getApplicationContext(), LoadingActivity.class );
                startActivity(intent);
            }
            else {
                dialog.create();
                dialog.show();
            }
        }
    }

}
