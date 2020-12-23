package com.jimu.dev;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class TestActivity extends AppCompatActivity {

    //@Override
    //protected void onCreate(Bundle savedInstanceState) {
    //    super.onCreate(savedInstanceState);
    //    setContentView(R.layout.activity_test);
    //}


    private Camera camera;  //  定义 Camera对象,调用系统的摄像头
    private Preview preview;  // 声明Preview ,自定义类
    private ImageView ivFocus;  // 声明相机焦点
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss"); // 时间转换
    private String randomImage = "car.jpg";  // 文件名
    byte[] image;
    int k = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
        preview = new Preview(this);   // 创建Preview对象,传入当前Activity.
        setContentView(preview); // 设置preview为显示界面
        ivFocus = new ImageView(this); // 创建ImageView组件
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == KeyEvent.ACTION_DOWN) { // 按下事件
            openOptionsMenu(); // 打开菜单栏
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        menu.add(0, Menu.FIRST, 0, "拍照")
                .setIcon(R.drawable.ic_launcher_background);
        menu.add(0, Menu.FIRST + 1, 0, "取消")
                .setIcon(R.drawable.ic_launcher_background);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case Menu.FIRST:
                if (k == 0) {
                    camera.autoFocus(new Camera.AutoFocusCallback() { // 自动对焦
                        public void onAutoFocus(boolean success, Camera camera) {
                            if (success) {
                                // success为true表示对焦成功，改变对焦状态图像（一个绿色的png图像）
                                preview.takePicture(); // 进行拍照
                                k = 1;
                                ivFocus.setImageResource(R.drawable.ic_launcher_foreground); // white为图片
                            }
                        }
                    });
                }
                break;
            case Menu.FIRST + 1:
                camera.startPreview(); // 开始亮灯,重新获取拍摄界面
                ivFocus.setImageResource(R.drawable.ic_launcher_foreground); // focus1为图片
                k = 0;
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*

     * 使用接口的原因：因为使用SurfaceView 有一个原则，所有的绘图工作必须得在Surface 被创建之后才能开始(Surface—表面，这个概念在 图形编程中常常被提到。基本上我们可以把它当作显存的一个映射，写入到Surface 的内容
                      可以被直接复制到显存从而显示出来，这使得显示速度会非常快)，而在Surface 被销毁之前必须结束。所以Callback 中的surfaceCreated 和surfaceDestroyed 就成了绘图处理代码的边界
     */
    class Preview extends SurfaceView implements SurfaceHolder.Callback {

        // 定义 SurfaceHolder对象
        private SurfaceHolder holder;
        // 创建一个PictureCallback对象，并实现其中的onPictureTaken方法
        private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
            // 该方法用于处理拍摄后的照片数据
            public void onPictureTaken(byte[] data, Camera camera) {
                // data参数值就是照片数据，将这些数据以key-value形式保存，以便其他调用该Activity的程序可
                // 以获得照片数据
                Bitmap cameraBitmap;
//    // 文件名
                cameraBitmap = BitmapFactory.decodeByteArray(data, 0,
                        data.length);
                if (getWindowManager().getDefaultDisplay().getOrientation() == Surface.ROTATION_0) {//0
                    Matrix matrix = new Matrix();
                    matrix.setRotate(90);
                    cameraBitmap = Bitmap.createBitmap(cameraBitmap, 0, 0,
                            cameraBitmap.getWidth(), cameraBitmap.getHeight(),
                            matrix, true); // 生成图片数据
                }
                File myCaptureFile = new File("/sdcard/DCIM/Camera/" + randomImage);
                try {
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
                    cameraBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos); //压缩图片像素,将图片数据写入
                    bos.flush();
                    bos.close();
                    cameraBitmap.recycle(); // 回收Bitmap的空间
                    camera.reconnect(); // 重新连接摄像机
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        // Preview类的构造方法
        public Preview(Context context) {
            super(context);
            holder = getHolder(); // 显示一个surface的抽象接口，使你可以控制surface的大小和格式， 以及在surface上编辑像素，和监视surface的改变。
            holder.addCallback(this); // 指定用于捕捉拍照事件的SurfaceHolder.Callback对象
            // 设置SurfaceHolder对象的类型
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); // 表明该Surface不包含原生数据，Surface用到的数据由其他对象提供，在Camera图像预览中就使用该类型的Surface，有Camera
            // 负责提供给预览Surface数据，这样图像预览会比较流畅。
        }

        // 开始拍照时调用该方法
        public void surfaceCreated(SurfaceHolder holder) {
            // 获得Camera对象
            camera = Camera.open();
            try {
                // 设置用于控制surface对象
                camera.setPreviewDisplay(holder);

            } catch (IOException exception) {
                // 释放手机摄像头
                camera.release();
            }
        }

        // 停止拍照时调用该方法
        public void surfaceDestroyed(SurfaceHolder holder) {
            // 释放手机摄像头
            camera.release();
        }

        // 拍照状态变化时调用该方法
        public void surfaceChanged(final SurfaceHolder holder, int format,
                                   int w, int h) {

            try {
                Camera.Parameters parameters = camera.getParameters();
                // 设置照片格式
                parameters.setPictureFormat(PixelFormat.JPEG);
                camera.setDisplayOrientation(90);

                // 设置拍摄照片的实际分辨率，在本例中的分辨率是1024*768
                parameters.setPictureSize(1024, 768);
                // 设置保存的图像大小
                camera.setParameters(parameters);
                // 开始拍照
                camera.startPreview();
                // 准备用于表示对焦状态的图像
                ivFocus.setImageResource(R.drawable.ic_launcher_background);
                // 设置摄像图片布局
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
                ivFocus.setScaleType(ImageView.ScaleType.CENTER);
                addContentView(ivFocus, layoutParams);
                ivFocus.setVisibility(VISIBLE);
            } catch (Exception e) {
                // 释放手机摄像头
                camera.release();
            }
        }

        // 停止拍照，并将拍摄的照片传入PictureCallback接口的onPictureTaken方法
        public void takePicture() {
            if (camera != null) {
                camera.takePicture(null, null, pictureCallback);
            }
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
    }
}