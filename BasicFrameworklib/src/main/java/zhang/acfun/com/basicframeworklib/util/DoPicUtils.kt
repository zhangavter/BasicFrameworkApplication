package zhang.acfun.com.basicframeworklib.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import zhang.acfun.com.basicframeworklib.model.PhotoModel
import zhang.acfun.com.basicframeworklib.view.dialog.PhotoSelectorDialog
import zhang.acfun.com.basicframeworklib.view.dialog.photoSelect.AlbumController
import zhang.acfun.com.basicframeworklib.view.dialog.photoSelect.OnMediaListener
import java.io.File
import java.util.*

/**
 * 通用工具类
 *
 * @author CDY
 */
object DoPicUtils {

    private var cameraImagePath: File? = null
    private var cameraUri: Uri? = null




    /**
     * 开启相册选择的dialog
     *
     * @param type               设置需要显示什么类型的资源
     * @param maxNum             最多选择多少张图片， Integer.MAX_VALUE表示不限制
     * @param onEndClickListener 返回true 就是关闭窗口 false不关闭
     * @param isShowCheckbox     是否显示选中的框框
     */
    fun showMediaSelectDialog(
        activity: AppCompatActivity?,
        type: AlbumController.Type?,
        maxNum: Int,
        isShowGif: Boolean,
        isShowCheckbox: Boolean,
        onEndClickListener: (MutableList<PhotoModel>?) -> Boolean
    ): PhotoSelectorDialog = PhotoSelectorDialog().apply {
        setOnEndClickListener(onEndClickListener)
        setMaxNum(maxNum)
        setShowGif(isShowGif)
        setShowCamera(false)
        setShowCheckbox(isShowCheckbox)
        setType(type)
        show(activity, "PhotoSelectorDialog")
    }



    /**
     * 开启相机
     *
     * @param imagePath 文件放在什么位置
     */
    fun openCamera(
        ctx: Context,
        requestCode: Int
    ) {
        cameraImagePath = File(FileUtil.preparePicturePath(ctx, "capture_"))
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //拍照结果输出到这个uri对应的file中
        intent.putExtra(
            MediaStore.EXTRA_OUTPUT, when {
                FileUtil.isAndroidQorAbove -> {
                    cameraUri =
                        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED)
                            ctx.contentResolver.insert(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                ContentValues().apply {
                                    put(
                                        "relative_path",
                                        FileUtil.UserMediaPath
                                    )
                                }
                            )
                        else
                            ctx.contentResolver.insert(
                                MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                                ContentValues().apply {
                                    put(
                                        "relative_path",
                                        FileUtil.UserMediaPath
                                    )
                                }
                            )

                    cameraUri
                }
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                    //对这个uri进行授权
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    FileProvider.getUriForFile(
                        ctx,
                        ctx.applicationContext.packageName + ".fileProvider",
                        cameraImagePath!!
                    )
                }
                else -> Uri.fromFile(cameraImagePath)
            }
        )
        // 打开Camera
        (ctx as AppCompatActivity).startActivityForResult(intent, requestCode)
    }
}