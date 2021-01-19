package com.jimu.dev

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.media.Image
import android.media.ImageReader
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.SparseIntArray
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.jimu.dev.Camera2Activity1
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

/**
 * Created by Ljh on 2020/12/23.
 * Description:
 * https://www.jianshu.com/p/7f766eb2f4e7
 */
class Camera2Activity1 : AppCompatActivity(), View.OnClickListener {
    companion object {
        private const val TAG = "Camera2Activity"
        private val ORIENTATIONS = SparseIntArray()

        ///为了使照片竖直显示
        init {
            ORIENTATIONS.append(Surface.ROTATION_0, 90)
            ORIENTATIONS.append(Surface.ROTATION_90, 0)
            ORIENTATIONS.append(Surface.ROTATION_180, 270)
            ORIENTATIONS.append(Surface.ROTATION_270, 180)
        }
    }

    private var mSurfaceView: SurfaceView? = null
    private var mSurfaceHolder: SurfaceHolder? = null
    private var iv_show: ImageView? = null
    private var mCameraManager //摄像头管理器
            : CameraManager? = null
    private var childHandler: Handler? = null
    private var mainHandler: Handler? = null
    private var mCameraID //摄像头Id 0 为后  1 为前
            : String? = null
    private var mImageReader: ImageReader? = null
    private var mCameraCaptureSession: CameraCaptureSession? = null
    private var mCameraDevice: CameraDevice? = null
    private var mFile: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera2_1)
        initVIew()
        mFile = File(getExternalFilesDir(null), "pic.jpg")
    }

    /**
     * 初始化
     */
    private fun initVIew() {
        iv_show = findViewById<View>(R.id.iv_show_camera2_activity) as ImageView
        //mSurfaceView
        mSurfaceView = findViewById<View>(R.id.surface_view_camera2_activity) as SurfaceView
        mSurfaceView!!.setOnClickListener(this)
        mSurfaceHolder = mSurfaceView!!.holder
        // mSurfaceView添加回调
        mSurfaceHolder?.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) { //SurfaceView创建
                // 初始化Camera
                initCamera2()
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
            override fun surfaceDestroyed(holder: SurfaceHolder) { //SurfaceView销毁
                // 释放Camera资源
                if (null != mCameraDevice) {
                    mCameraDevice!!.close()
                    mCameraDevice = null
                }
            }
        })
    }

    /**
     * 初始化Camera2
     */
    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun initCamera2() {
        val handlerThread = HandlerThread("Camera2")
        handlerThread.start()
        childHandler = Handler(handlerThread.looper)
        mainHandler = Handler(mainLooper)
        mCameraID = "" + CameraCharacteristics.LENS_FACING_FRONT //后摄像头
        mImageReader = ImageReader.newInstance(1080, 1920, ImageFormat.JPEG, 1)
        mImageReader!!.setOnImageAvailableListener({ reader ->

            //可以在这里处理拍照得到的临时照片 例如，写入本地
            childHandler!!.post(ImageSaver(reader.acquireNextImage(), mFile))
            //mCameraDevice.close();
            //mSurfaceView.setVisibility(View.GONE);
            //iv_show.setVisibility(View.VISIBLE);
            //// 拿到拍照照片数据
            //Image image = reader.acquireNextImage();
            //ByteBuffer buffer = image.getPlanes()[0].getBuffer();
            //byte[] bytes = new byte[buffer.remaining()];
            //buffer.get(bytes);//由缓冲区存入字节数组
            //final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            //if (bitmap != null) {
            //    iv_show.setImageBitmap(bitmap);
            //}
        }, mainHandler)
        //获取摄像头管理
        mCameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return
            }
            //打开摄像头
            mCameraManager!!.openCamera(mCameraID!!, stateCallback, mainHandler)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    /**
     * 摄像头创建监听
     */
    private val stateCallback: CameraDevice.StateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) { //打开摄像头
            mCameraDevice = camera
            //开启预览
            takePreview()
        }

        override fun onDisconnected(camera: CameraDevice) { //关闭摄像头
            if (null != mCameraDevice) {
                mCameraDevice!!.close()
                mCameraDevice = null
            }
        }

        override fun onError(camera: CameraDevice, error: Int) { //发生错误
            Toast.makeText(this@Camera2Activity1, "摄像头开启失败", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 开始预览
     */
    private fun takePreview() {
        try {
            // 创建预览需要的CaptureRequest.Builder
            val previewRequestBuilder = mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            // 将SurfaceView的surface作为CaptureRequest.Builder的目标
            previewRequestBuilder.addTarget(mSurfaceHolder!!.surface)
            // 创建CameraCaptureSession，该对象负责管理处理预览请求和拍照请求
            mCameraDevice!!.createCaptureSession(Arrays.asList(mSurfaceHolder!!.surface, mImageReader!!.surface), object : CameraCaptureSession.StateCallback( // ③
            ) {
                override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                    if (null == mCameraDevice) return
                    // 当摄像头已经准备好时，开始显示预览
                    mCameraCaptureSession = cameraCaptureSession
                    try {
                        // 自动对焦
                        previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
                        // 打开闪光灯
                        previewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)
                        // 显示预览
                        val previewRequest = previewRequestBuilder.build()
                        mCameraCaptureSession!!.setRepeatingRequest(previewRequest, null, childHandler)
                    } catch (e: CameraAccessException) {
                        e.printStackTrace()
                    }
                }

                override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {
                    Toast.makeText(this@Camera2Activity1, "配置失败", Toast.LENGTH_SHORT).show()
                }
            }, childHandler)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    /**
     * 点击事件
     */
    override fun onClick(v: View) {
        takePicture()
    }

    /**
     * 拍照
     */
    private fun takePicture() {
        if (mCameraDevice == null) return
        // 创建拍照需要的CaptureRequest.Builder
        val captureRequestBuilder: CaptureRequest.Builder
        try {
            captureRequestBuilder = mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            // 将imageReader的surface作为CaptureRequest.Builder的目标
            captureRequestBuilder.addTarget(mImageReader!!.surface)
            // 自动对焦
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
            // 自动曝光
            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)
            // 获取手机方向
            val rotation = windowManager.defaultDisplay.rotation
            // 根据设备方向计算设置照片的方向
            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS[rotation])
            //拍照
            val mCaptureRequest = captureRequestBuilder.build()
            mCameraCaptureSession!!.capture(mCaptureRequest, null, childHandler)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    /**
     * Saves a JPEG [Image] into the specified [File].
     */
    private class ImageSaver internal constructor(
            /**
             * The JPEG image
             */
            private val mImage: Image,
            /**
             * The file we save the image into.
             */
            private val mFile: File?) : Runnable {
        override fun run() {
            val buffer = mImage.planes[0].buffer
            val bytes = ByteArray(buffer.remaining())
            buffer[bytes]
            var output: FileOutputStream? = null
            try {
                output = FileOutputStream(mFile)
                output.write(bytes)
                Log.w(TAG, "ImageSaver output.write(bytes)")
            } catch (e: IOException) {
                Log.w(TAG, "e:" + e.message)
                e.printStackTrace()
            } finally {
                Log.w(TAG, "ImageSaver close " + mFile!!.absolutePath)
                mImage.close()
                if (null != output) {
                    try {
                        output.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}