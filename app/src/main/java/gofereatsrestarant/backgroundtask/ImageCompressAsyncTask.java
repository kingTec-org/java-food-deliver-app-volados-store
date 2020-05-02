package gofereatsrestarant.backgroundtask;
/**
 * @package com.trioangle.gofereatsrestarant
 * @subpackage backgroundtask
 * @category ImageCompressAsyncTask
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.graphics.Bitmap;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import gofereatsrestarant.configs.AppController;
import gofereatsrestarant.configs.SessionManager;
import gofereatsrestarant.interfaces.ImageListener;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/*****************************************************************
 Compress image while upload in background
 ****************************************************************/
public class ImageCompressAsyncTask extends AsyncTask<Void, Void, Void> {
    public @Inject
    SessionManager sessionManager;
    private String filePath = "", compressPath = "";
    private WeakReference<AppCompatActivity> compressImgWeakRef;
    private RequestBody requestBody;
    private ImageListener imageListener;

    public ImageCompressAsyncTask(AppCompatActivity activity, String filePath, ImageListener imageListener) {
        AppController.getAppComponent().inject(this);
        this.compressImgWeakRef = new WeakReference<>(activity);
        //this.params = params;
        this.filePath = filePath;
        this.imageListener = imageListener;
    }

    /**
     * Call when before call the WS.
     */
    @Override
    protected void onPreExecute() {
        if (this.compressImgWeakRef == null) {
            this.cancel(true);
        }
    }

    /**
     * action to be performed in background
     */
    @Override
    protected Void doInBackground(Void... params) {
        try {
            if (filePath != null) {
                File file = new File(filePath);
                if (file.exists()) {
                    publishProgress();
                    file = new Compressor.Builder(this.compressImgWeakRef.get())
                            .setMaxWidth(1080)
                            .setMaxHeight(1920)
                            .setQuality(75)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .build()
                            .compressToFile(file);
                    compressPath = file.getPath();
                    requestBody = uploadImgParam(compressPath);
                }
            }
        } catch (OutOfMemoryError | Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * called after the WS return the response.
     */
    @Override
    protected void onPostExecute(Void value) {
        if (compressImgWeakRef != null && compressImgWeakRef.get() != null && requestBody != null) {
            imageListener.onImageCompress(compressPath, requestBody);
        } else {
            imageListener.onImageCompress(compressPath, null);
        }
    }

    /**
     * To Return Request Body of the  Compressed image  by Passing compressed path
     *
     * @param imagePath compressed image path to be passed
     * @return returns RequestBody to be uploaded
     */

    public RequestBody uploadImgParam(String imagePath) throws IOException {
        MultipartBody.Builder multipartBody = new MultipartBody.Builder();
        multipartBody.setType(MultipartBody.FORM);
        File file = null;
        try {
            file = new File(imagePath);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            multipartBody.addFormDataPart("image", "IMG_" + timeStamp + ".jpg",
                    RequestBody.create(MediaType.parse("image/png"), file));
            multipartBody.addFormDataPart("token", sessionManager.getToken());
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody formBody = multipartBody.build();
        return formBody;
    }
}
