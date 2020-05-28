package com.teddy.bitmapproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;
import com.hjq.toast.style.ToastWhiteStyle;

import java.util.List;

import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity {

    private static final String imageUrl = "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2534506313,1688529724&fm=26&gp=0.jpg";


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int NOT_NOTICE = 2;

    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

     private  AlertDialog alertDialog = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化吐司工具类
        ToastUtils.init(getApplication(), new ToastWhiteStyle(getApplicationContext()));




        requestPermission();

        ToastUtils.show("获取权限成功");


         //这个方法里面有很多的弹框，也很好用的，
//        verifyStoragePermissions(this);
//        checkPermission();

//        BitmapUtil.GetImageInputStream(imageUrl, new BitmapListenner() {
//            @Override
//            public void saveBitmap(Bitmap bitmap) {
//                if (bitmap != null ){
//                    BitmapUtil.saveBitmap(bitmap);
//                }else {
//                    Log.i("tag","bitmap 为空");
//                }
//
//            }
//        });

    }


    //然后通过一个函数来申请
    public static void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_EXTERNAL_STORAGE){
            for (int i =0;i<permissions.length;i++){
                if (grantResults[i] == PERMISSION_GRANTED){ //选择了“始终允许”
                    Toast.makeText(this, "" + "权限" + permissions[i] + "申请成功", Toast.LENGTH_SHORT).show();
                }else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this,permissions[i])){//用户选择了禁止不再询问
                        AlertDialog.Builder builder   = new AlertDialog.Builder(this);
                        builder.setTitle("permission")
                                .setMessage("点击允许可以使用我们的APP哦")
                                .setPositiveButton("去允许", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (alertDialog != null && alertDialog.isShowing()){
                                            alertDialog.dismiss();
                                        }
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package",getPackageName(),null);//注意就是"package",不用改成自己的包名
                                       intent.setData(uri);
                                       startActivityForResult(intent,NOT_NOTICE);


                                    }
                                });
                        alertDialog = builder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();

                    }
                }
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NOT_NOTICE){
            verifyStoragePermissions(this);
        }
    }

    //动态权限，
    // 网址https://blog.csdn.net/hqyhqyhq/article/details/77163607?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.nonecase&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.nonecase
    private void checkPermission() {
        //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show();
            }
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,}, REQUEST_EXTERNAL_STORAGE);

        } else {
            Toast.makeText(this, "授权成功！", Toast.LENGTH_SHORT).show();
            Log.e("tag", "checkPermission: 已经授权！");


        }
    }


    public void requestPermission() {
        XXPermissions.with(this)
                .permission(Permission.Group.STORAGE)
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean all) {
                        if (all) { //所有的权限都给了
                            ToastUtils.show("获取权限成功");

                            //获取到了权限，就进行你自己的逻辑处理。
                            BitmapUtil.GetImageInputStream(imageUrl, new BitmapListenner() {
                                @Override
                                public void saveBitmap(Bitmap bitmap) {
                                    if (bitmap != null) {
                                        BitmapUtil.saveBitmap(bitmap);
                                    } else {
                                        Log.i("tag", "bitmap 为空");
                                    }
                                }
                            });
                        } else {
                            ToastUtils.show("获取权限成功，部分权限未正常授予");
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        //没有允许权限
                        if (quick) {
                            ToastUtils.show("被永久拒绝授权，请手动授予权限");
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(MainActivity.this);
                        } else {
                            ToastUtils.show("获取权限失败");
                            System.out.println("测试失败了");
                        }
                    }
                });
    }


}
