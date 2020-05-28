package com.teddy.bitmapproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * author : Teddy
 * date   : 2020/5/27
 * desc   :
 */
public class BitmapUtil{
    /**
     * 获取网络图片
     *
     * @param imageurl 图片网络地址
     * @return Bitmap 返回位图
     */
    public static void GetImageInputStream(final String imageurl,final BitmapListenner bitmapListenner) {

        new Thread(new Runnable() {
            @Override public void run() {
                URL url = null;
                HttpURLConnection connection = null;
                Bitmap bitmap = null;
                try {
                    url = new URL(imageurl);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(10000); //超时设置
//                    connection.setDoInput(true);
                    connection.setRequestMethod("GET");
                    connection.setUseCaches(false); //设置不使用缓存
                    connection.connect();
                    InputStream inputStream = connection.getInputStream();
                    if (connection.getResponseCode() == 200){
                        if (inputStream==null){
                            throw  new RuntimeException("stream is null");
                        }else {
                            try{
                                Bitmap bitmap1 = BitmapFactory.decodeStream(inputStream);
                                if (bitmap1 != null ){
                                    bitmapListenner.saveBitmap(bitmap1);
//                                byte[] data=readStream(inputStream);
//                                String d = new String(data);
//                                int length = d.length();
//                                if (d!=null){
//                                    bitmap= BitmapFactory.decodeByteArray(data,0,length);
//                                    bitmapListenner.saveBitmap(bitmap);
//                                }
                                }else {
                                    Log.i("tag","bitmap为空，BitmapFactory");
                                }

                            }catch (Exception e){
                                e.printStackTrace();

                            }
                            inputStream.close();
                        }
                    }else {
                        Log.i("tag","error");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }


    /*
     * 得到图片字节流 数组大小
     * */
    public static byte[] readStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len=inStream.read(buffer)) != -1){
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

    //保存图片，到你手机，指定的位置
    public static void saveBitmap(Bitmap bmp) {
        FileOutputStream fos =null;
        String bitmapName = "fenxiang.jpg";
        File file = null;
        String QQFilePath;

        try {
            //获取SDCard指定目录下
            String sdCardDir = Environment.getExternalStorageDirectory() + "/zupubao/";
            //目录转化成文件夹  如果不存在，那就建立这个文件夹
            File dirFile = new File(sdCardDir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            //文件夹有啦，就可以保存图片啦
            // 在SDcard的目录下创建图片文,以当前时间为其命名
            file = new File(dirFile, bitmapName);
//            file.createNewFile();

//            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));

            fos = new FileOutputStream(file);
            fos.flush();
//            fos.close();
            bmp.compress(Bitmap.CompressFormat.JPEG,100,fos);
            Log.e("tag", "saveBitmap: 图片保存到了"+Environment.getExternalStorageDirectory()+"/zupubao/"+bitmapName );

            QQFilePath = Environment.getExternalStorageDirectory() +"/zupubao/"+"fengxiang.jpg";

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
