package ir.roela.bametro.utils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class AsyncCall extends AsyncTask<String, Integer, String> {

    private final TaskWS task;
    public DataListener dataListener;

    @Override
    protected String doInBackground(String... params) {
        switch (task) {
            case downloadImage:
                downloadImage(params[0], params[1]);
                break;
            default:
                break;
        }
        return null;
    }

    public enum TaskWS {
        downloadImage
    }

    public AsyncCall(TaskWS newTask) {
        task = newTask;
    }

    @SafeVarargs
    static public <T> void executeAsyncTask(AsyncTask<T, ?, ?> task, T... params) {
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
    }

    private void downloadImage(String imgPath, String imgUrl) {
        String res;
        try {
            File file = new File(imgPath);
            if (!file.exists() || file.length() == 0) {
                URL url = new URL(imgUrl/*General.getUrlImage4Download(imgName)*/);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap bmp = BitmapFactory.decodeStream(input);

//                String ext = Constants.getFileExtension(imgName);
                FileOutputStream stream = new FileOutputStream(imgPath);

                ByteArrayOutputStream outstream = new ByteArrayOutputStream();
//                if (ext.equals("png")) {
//                    bmp.compress(Bitmap.CompressFormat.PNG, 100, outstream);
//                } else if (ext.equals("jpg") || ext.equals("jpeg")) {
//                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
//                }
                byte[] byteArray = outstream.toByteArray();
                stream.write(byteArray);
                stream.close();
                bmp.recycle();
                input.close();
            }
            res = "1";
        } catch (Exception e) {
            res = "-1";
            Log.e("", "downloadImage ex: " + e.getMessage());
        }
        if (dataListener != null) {
            dataListener.onDataReceive(res);
        }
    }

}
