package com.urmp.download;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;

import androidx.core.content.FileProvider;

import com.urmp.BuildConfig;
import com.urmp.MainActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import static androidx.core.app.ActivityCompat.requestPermissions;

public class DownloadTask extends AsyncTask<String, Integer, Boolean> {
    public interface DownloadListener {
        void onDownloadComplete(File filename);

        void onProgressUpdate(int progress);

        void onDownloadFailure(final String msg);
    }

    private final DownloadListener listener;
    private String msg;
    private File saveTo;

    public DownloadTask(DownloadListener listener) {
        this.listener = listener;
    }
    Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    @Override protected Boolean doInBackground(String... params) {
        if (params == null || params.length < 2) {
            msg = "Incomplete parameters";
            return false;
        }

        String sUrl = params[0];
        String filename = params[1];
        File rootDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        saveTo = new File(rootDir, filename);
        System.out.println(": " + saveTo);
        try {
            URL url = new URL(sUrl);
            URLConnection conn = url.openConnection();
            conn.connect();
            int fileLength = conn.getContentLength();
            InputStream is = new BufferedInputStream(url.openStream());
            OutputStream os = new FileOutputStream(saveTo);
            byte buffer[] = new byte[512];
            long totalDownloaded = 0;
            int count;

            while ((count = is.read(buffer)) != -1) {
                totalDownloaded += count;

                publishProgress((int) (totalDownloaded * 100 / fileLength));
                os.write(buffer, 0, count);
            }

            os.flush();
            os.close();
            is.close();

            Uri uri;
            if (Build.VERSION.SDK_INT < 24) {
                uri = Uri.fromFile(saveTo);
            } else {
                uri = FileProvider.getUriForFile(context,
                        BuildConfig.APPLICATION_ID + ".provider", saveTo);
              //  uri = Uri.parse("content://"+saveTo.getCanonicalPath()); // My work-around for new SDKs, doesn't work in Android 10.
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            msg = String.valueOf(e.getCause());
        }

        return false;
    }

    @Override protected void onProgressUpdate(Integer... values) {
        if (listener != null) listener.onProgressUpdate(values[0]);
    }

    @Override protected void onPostExecute(Boolean result) {
        if (!result) {
            if (listener != null) listener.onDownloadFailure(msg);
            return;
        }

        if (listener != null) listener.onDownloadComplete(saveTo);
    }
}