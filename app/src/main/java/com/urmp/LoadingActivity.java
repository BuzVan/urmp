package com.urmp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.urmp.download.DownloadTask;

import java.io.File;

public class LoadingActivity extends AppCompatActivity {
    ProgressBar progressBar;
    TextView loadTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        progressBar = findViewById(R.id.progressBar);
        loadTV = findViewById(R.id.loadingTextView);

        String url = getString(R.string.url_apk);
        String filename = "client.apk";
        DownloadTask downloadTask = new DownloadTask(new DownloadTask.DownloadListener() {
            @Override public void onDownloadComplete(File filename) {
               // setup(filename);
            }

            @Override public void onProgressUpdate(int progress) {
                progressBar.setProgress(progress);
                loadTV.setText( getString(R.string.loading) + " " +progress + "%");
            }

            @Override public void onDownloadFailure(String msg) {

            }
        });
        downloadTask.setContext(this);
        downloadTask.execute(url, filename);

    }

    private void setup(File filename) {
        int k=0;
        Uri uri;
        if (Build.VERSION.SDK_INT < 24) {
            uri = Uri.fromFile(filename);
        } else {
            uri = Uri.parse("content://"+filename.getAbsolutePath()); // My work-around for new SDKs, doesn't work in Android 10.
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);

    }

}

