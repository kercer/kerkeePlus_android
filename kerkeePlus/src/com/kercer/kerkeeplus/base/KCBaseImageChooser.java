package com.kercer.kerkeeplus.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;

import com.kercer.kercore.debug.KCLog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 图片选择工具类
 * 上传，保存本地功能
 * Created by liweisu on 15/9/10.
 */
public class KCBaseImageChooser {

    public static final String TAG = "KCBaseImageChooser";
    public static final int Image_Pick_RequestCode = 1000;
    public static final int Camera_Capture_RequestCode = 1001;

    /**
     * @param filename
     * @param uploadUrl
     * @return 上传结果
     */
    public String uploadFile(String filename, String uploadUrl) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            URL url = new URL(uploadUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestMethod("POST");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
            DataOutputStream ds =
                    new DataOutputStream(con.getOutputStream());
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; " +
                    "name=\"file1\";filename=\"" +
                    "img.jpg" + "\"" + end);
            ds.writeBytes(end);
            FileInputStream fStream = null;
            try {
                fStream = new FileInputStream(filename);
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];
                int length;
                while ((length = fStream.read(buffer)) != -1) {
                    ds.write(buffer, 0, length);
                }
                ds.writeBytes(end);
                ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            } catch (Exception e) {
                KCLog.e(TAG, e);
            } finally {
                if (fStream != null) {
                    fStream.close();
                }
            }
            ds.flush();
            InputStream is = con.getInputStream();
            StringBuffer b = new StringBuffer();
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            char[] buf = new char[1024];
            while (inputStreamReader.read(buf) != -1) {
                b.append(buf);
            }
            ds.close();
            is.close();
            return b.toString();
        } catch (Exception e) {
            KCLog.e(TAG, e);
        }
        return null;
    }


    public String uploadFile(byte[] bitmap, String uploadUrl) {
        if (bitmap == null) {
            return null;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(bitmap, 0, bitmap.length);
        InputStream isBm = new ByteArrayInputStream(baos.toByteArray());

        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            URL url = new URL(uploadUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestMethod("POST");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
            DataOutputStream ds =
                    new DataOutputStream(con.getOutputStream());
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; " +
                    "name=\"file1\";filename=\"" +
                    "img.jpg" + "\"" + end);
            ds.writeBytes(end);


            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = -1;
            while ((length = isBm.read(buffer)) != -1) {
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            isBm.close();
            ds.flush();
            InputStream is = con.getInputStream();
            StringBuffer b = new StringBuffer();
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            char[] buf = new char[1024];
            while (inputStreamReader.read(buf) != -1) {
                b.append(buf);
            }
            ds.close();
            is.close();
            return b.toString();
        } catch (Exception e) {
            KCLog.e(TAG, e);
        }
        return null;
    }

    public String uploadFile(Bitmap bitmap, String uploadUrl) {
        if (bitmap == null) {
            return null;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        InputStream isBm = new ByteArrayInputStream(baos.toByteArray());

        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            URL url = new URL(uploadUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestMethod("POST");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
            DataOutputStream ds =
                    new DataOutputStream(con.getOutputStream());
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; " +
                    "name=\"file1\";filename=\"" +
                    "img.jpg" + "\"" + end);
            ds.writeBytes(end);


            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = -1;
            while ((length = isBm.read(buffer)) != -1) {
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            isBm.close();
            ds.flush();
            InputStream is = con.getInputStream();
            StringBuffer b = new StringBuffer();
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            char[] buf = new char[1024];
            while (inputStreamReader.read(buf) != -1) {
                b.append(buf);
            }
            ds.close();
            is.close();
            return b.toString();
        } catch (Exception e) {
            KCLog.e(TAG, e);
        }
        return null;
    }

    public void showImageAndCameraChooser(final Activity current, DialogInterface.OnCancelListener onCancelListener) {
        final CharSequence[] items =
                {"相册", "拍照"};
        AlertDialog dlg = new AlertDialog.Builder(current).setTitle("选择图片").setItems(items,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            chooseImageChooser(current);
                        } else {
                            openCameraChooser(current);
                        }
                    }
                }).create();
        dlg.setOnCancelListener(onCancelListener);
        dlg.show();
    }

    public void showImageAndCameraChooser(final Activity current) {
        final CharSequence[] items =
                {"相册", "拍照"};
        AlertDialog dlg = new AlertDialog.Builder(current).setTitle("选择图片").setItems(items,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            chooseImageChooser(current);
                        } else {
                            openCameraChooser(current);
                        }
                    }
                }).create();
        dlg.show();
    }

    public void chooseImageChooser(Activity current) {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            if (intent.resolveActivity(current.getPackageManager()) != null) {
                current.startActivityForResult(intent, Image_Pick_RequestCode);
            } else {
                Intent intent2 = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                if (intent2.resolveActivity(current.getPackageManager()) != null) {
                    current.startActivityForResult(intent, Image_Pick_RequestCode);
                } else {
                    KCLog.d(TAG, "无默认图片选择器");
                }
            }
        } catch (Exception e) {
            KCLog.e(TAG, e);
        }

    }

    public void openCameraChooser(Activity current) {
        try {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            current.startActivityForResult(intent, Camera_Capture_RequestCode);
        } catch (Exception e) {
            KCLog.e(TAG, e);
        }

    }

    public String getPickImagePath(Activity currentActivity, Intent data) {
        if (data != null) {
            String picturePath = null;
            try {
                Uri selectedImage = data.getData();
                picturePath = null;
                if (selectedImage.getScheme().toLowerCase().startsWith("content")) {
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = currentActivity.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        picturePath = cursor.getString(columnIndex);
                    }
                } else if (selectedImage.getScheme().toLowerCase().startsWith("file")) {
                    picturePath = selectedImage.getPath();
                }
            } catch (Exception e) {
                KCLog.e(TAG, e);
            }
            if (!TextUtils.isEmpty(picturePath)) {
                return picturePath;
            }
        }
        return null;
    }

    /**
     * 获得相机图片
     * @param data
     * @return
     */
    public Bitmap getCameraBitmap(Intent data) {
        Bitmap bitmap = null;
        try {
            bitmap = null;
            if (data != null) {
                Bundle extras = data.getExtras();
                bitmap = (Bitmap) extras.get("data");
            }
        } catch (Exception e) {
            KCLog.e(TAG, e);
        }
        return bitmap;
    }

    /**
     * 图片转base64
     * @param bitmap
     * @return
     */
    public String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.NO_WRAP);
            }
        } catch (IOException e) {
            KCLog.e(TAG, e);
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                KCLog.e(TAG, e);
            }
        }
        return result;
    }

    /**
     * 保存图片到缓存
     */
    public String saveBitMapToCache(Context context, Bitmap bitmap, String name) {
        if (TextUtils.isEmpty(name)) return "";
        FileOutputStream b = null;
        String result = "";
        File file = new File(context.getCacheDir(), String.format("%s.jpeg", name));
        try {
            if (file.exists())
                file.delete();
            b = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
            result = file.toString();
        } catch (FileNotFoundException e) {
            KCLog.e(TAG, e);
        } finally {
            try {
                if (b != null) {
                    b.flush();
                    b.close();
                }
            } catch (IOException e) {
                KCLog.e(TAG, e);
            }
        }
        return result;
    }

    /**
     * 解析相册选择地址
     *
     * @param data
     */
    public void getImagePickResult(KCH5BaseActivity current, Intent data) {
        String filePath = getPickImagePath(current, data);
        if (!TextUtils.isEmpty(filePath)) {
            if (current.getCurrentWebView().getUploadMessage() != null) {
                if (filePath.startsWith("/"))
                    filePath = "file://" + filePath;
                current.getCurrentWebView().getUploadMessage().onReceiveValue(Uri.parse(filePath));
            }
        }
    }

    /**
     * 解析相机照相图片
     *
     * @param data
     */
    public void getCameraPickResult(KCH5BaseActivity current, Intent data) {
        final Bitmap bitmap = getCameraBitmap(data);
        if (bitmap != null) {
            String filePath = saveBitMapToCache(current,bitmap, "camera" + System.currentTimeMillis());
            if (current.getCurrentWebView().getUploadMessage() != null) {
                if (filePath.startsWith("/"))
                    filePath = "file://" + filePath;
                current.getCurrentWebView().getUploadMessage().onReceiveValue(Uri.parse(filePath));
            }
        }
    }
}
