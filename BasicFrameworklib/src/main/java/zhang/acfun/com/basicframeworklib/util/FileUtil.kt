package zhang.acfun.com.basicframeworklib.util

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.ContentValues
import android.content.Context
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.media.MediaExtractor
import android.media.MediaMetadataRetriever
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.webkit.MimeTypeMap
import zhang.acfun.com.basicframeworklib.Constants
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * @author zxb
 * @Time 18-5-17 下午2:26
 */
object FileUtil {

    const val UserMediaPath = "DCIM/Camera"

    private final val isNeedTargetQ = false

    //是否是Android Q以上
    val isAndroidQorAbove: Boolean
        get() = isNeedTargetQ && Build.VERSION.SDK_INT >= 29

    private val isExternalMemoryAvailable: Boolean
        get() = Environment.getExternalStorageState().equals(
            Environment.MEDIA_MOUNTED,
            ignoreCase = true
        )

    /**
     * 获取需要下载到本地，并且用户需要看到的目录
     */
    fun getUserCanSeeDir(ctx: Context): File {

        val dir = if (isExternalMemoryAvailable) File(
            Environment.getExternalStorageDirectory().absolutePath,
            UserMediaPath
        ) else
            getRootDir(ctx, false)

        if (!dir.exists())
            dir.mkdirs()

        return dir
    }


    /**
     * 获取目录
     *
     * @param context
     * @return
     */
    private fun getRootDir(context: Context, isInAndroidDataFile: Boolean): File {
        var targetDir: File? = null

        try {
            targetDir = if (isInAndroidDataFile)
                context.cacheDir
            else {
                context.externalCacheDir
            }

            if (!targetDir!!.exists()) {
                targetDir.mkdirs()
            }
        } catch (e: Exception) {
        }

        if (targetDir == null || !targetDir.exists()) {
            targetDir = context.cacheDir
            if (!targetDir!!.exists()) {
                targetDir.mkdirs()
            }
        }

        return targetDir
    }


    /**
     * 获取目录
     *
     * @param context
     * @param name
     * @return
     */
    private fun getDir(context: Context, name: String): File {
        return getDir(getRootDir(context, true), name)
    }

    /**
     * @param context
     * @param name
     * @param isInAndroidDataFile
     * @return
     */
    fun getDir(context: Context, name: String, isInAndroidDataFile: Boolean): File {
        return getDir(getRootDir(context, isInAndroidDataFile), name)
    }

    /**
     *
     */
    fun getDir(parentFile: File, dirName: String): File {
        val file = File(parentFile, dirName)
        if (!file.exists())
            file.mkdirs()
        return file
    }

    /**
     * 获取文件
     *
     * @param context
     * @param dir
     * @param fileName
     * @return
     */
    fun getFile(context: Context, dir: String, fileName: String): File {
        return File(getDir(context, dir), fileName)
    }

    /**
     * 获取文件
     *
     * @param context
     * @param dir
     * @param fileName
     * @return
     */
    fun getFile(context: Context, dir: String, fileName: String, isInData: Boolean): File {
        return File(getDir(context, dir, isInData), fileName)
    }

    /**
     * @param b
     * @param ret
     * @return
     */
    fun getFileFromBytes(b: ByteArray?, ret: File): File {

        if (b == null) return ret

        var stream: BufferedOutputStream? = null
        try {
            val fstream = FileOutputStream(ret)
            stream = BufferedOutputStream(fstream)
            stream.write(b)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (stream != null) {
                try {
                    stream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        return ret
    }

    /**
     * 照片文件默认放到V下面
     *
     * @param context
     * @param head    文件名，后面加时间参数
     * @return
     */
    fun preparePicturePath(context: Context, head: String): String {
        return File(getUserCanSeeDir(context), getPictureName(head)).absolutePath
    }

    /**
     * android 10 从公共区域拷贝到沙盒区域的文件命名
     */
    fun prepareTempCopyFilePath(
        context: Context,
        head: String,
        fileType: String = "jpeg"
    ): File {
        return getFile(
            context,
            "tempCopy",
            head + Utils.MD5(System.currentTimeMillis().toString()) + "." + fileType
        )
    }

    /**
     * 上传图片临时存放文件的位置
     *
     * @param context
     * @return
     */
    fun getPicUploadTempPath(context: Context, path: String): String {
        return getFile(context, "upload", getFileName("upload_", path), true).absolutePath
    }

    /**
     *
     * @param prex 前缀
     * @param url 地址
     * @return
     */
    fun getFileName(prex: String, url: String?): String {
        try {
            return "$prex${Utils.MD5(url!!)}.${getFileExtension(url)}"
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return "$prex${Utils.MD5(url ?: "dksldksldklskdlskdie2w0392039")}"
    }


    /**
     * 根据时间来设置照片的名字
     *
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    fun getPictureName(head: String): String {
        val format = SimpleDateFormat("yyyyMMdd_hhmmss")
        val date = Date(System.currentTimeMillis())

        return head + format.format(date) + ".jpeg"
    }


    /**
     * 把assets里的文件copy到指定文件目录下，同名拷贝
     *
     * @param context
     * @param dir
     * @param assetFileName assets文件的名称
     * @return
     */
    fun CopyAssetsToXXX(
        context: Context, dir: String,
        assetFileName: String
    ): File? {
        var file: File?
        file = getFile(context, dir, assetFileName)
        try {
            val myOutput = FileOutputStream(file)
            val myInput = context.assets.open(assetFileName)
            val buffer = ByteArray(1024)
            var length = myInput.read(buffer)
            while (length > 0) {
                myOutput.write(buffer, 0, length)
                length = myInput.read(buffer)
            }

            myOutput.flush()
            myInput.close()
            myOutput.close()
        } catch (e: Exception) {
            file = null
        }

        return file
    }

    /**
     * 读取文本数据
     *
     * @param context  程序上下文
     * @param fileName 文件名
     * @return String, 读取到的文本内容，失败返回null
     */
    fun readAssets(context: Context, fileName: String): String? {
        var input: InputStream? = null
        var content: String? = null
        try {
            input = context.assets.open(fileName)
            if (input != null) {

                val buffer = ByteArray(1024)
                val arrayOutputStream = ByteArrayOutputStream()
                while (true) {
                    val readLength = input.read(buffer)
                    if (readLength == -1)
                        break
                    arrayOutputStream.write(buffer, 0, readLength)
                }
                input.close()
                arrayOutputStream.close()
                content = String(arrayOutputStream.toByteArray())

            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
            content = null
        } finally {
            try {
                input?.close()
            } catch (ioe: IOException) {
                ioe.printStackTrace()
            }

        }
        return content
    }

    /**
     * @param bitmap
     * @param file
     * @return
     * @throws IOException
     * @throws FileNotFoundException
     */
    @Throws(IOException::class, FileNotFoundException::class)
    fun writeBitmapToFile(bitmap: Bitmap, file: File): Boolean {

        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(file)
            return bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        } finally {
            out?.close()
        }
    }

    /**
     * copy
     *
     * @param oldfile
     * @param newPath
     * @param showUpdateMei
     */
    fun copyFile(ctx: Context, oldfile: File, newPath: File) {
        try {
            var bytesum = 0
            require(oldfile.exists()) { "要包括的文件不存在" }
            if (oldfile.exists()) {
                FileInputStream(oldfile).use { inStream ->
                    FileOutputStream(newPath).use { fs ->
                        val buffer = ByteArray(1444)
                        var byteread = inStream.read(buffer)
                        while (byteread != -1) {
                            bytesum += byteread
                            fs.write(buffer, 0, byteread)
                            byteread = inStream.read(buffer)
                        }
                    }
                }
            }
        } catch (e: Exception) {
        }
    }

    /**
     * 判断文件是否存在
     */
    fun isFileExists(strFile: String): Boolean {
        if (TextUtils.isEmpty(strFile))
            return false

        return File(strFile).exists()

    }

    /**
     * 获取视图的bitmap
     *
     * @return
     */
    fun getViewBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
//        BitmapFillet.fillet(bitmap,100,BitmapFillet.CORNER_ALL)
        return bitmap
    }



    /**
     * 把视图的bitmap储存为图片文件
     *
     * @param ctx
     * @param view
     * @param key  文件名，不需要加后缀，比如保存一张图片
     * @return
     */
    fun saveViewBitmapToFile(ctx: Context, view: View, key: String): File {
        val bitmap: Bitmap? = getViewBitmap(view)
        val file = File(getUserCanSeeDir(ctx), "$key.png")
        return saveBitmapToFile(ctx, bitmap, Bitmap.CompressFormat.PNG, file)
    }

    /**
     * 把BitMap保存到文件
     * */
    fun saveBitmapToData(ctx: Context, bitmap: Bitmap?, key: String): File {
        val file = getFile(ctx, "picture", "$key.jpeg", true)
        return saveBitmapToFile(ctx, bitmap, Bitmap.CompressFormat.JPEG, file)
    }

    /**
     * 把BitMap保存到文件
     * @param saveFile 保存的位置
     *
     * */
    fun saveBitmapToFile(
        ctx: Context,
        bitmap: Bitmap?,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
        saveFile: File
    ): File {

        val stream = ByteArrayOutputStream()
        bitmap!!.compress(format, 100, stream)
        val byteArray = stream.toByteArray()

        try {
            val fos = FileOutputStream(saveFile)
            fos.write(byteArray, 0, byteArray.size)
            fos.flush()

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (!bitmap.isRecycled)
                bitmap.recycle()
//            bitmap = null
        }

        return saveFile
    }


    /**
     * 删除文件
     */
    fun delete(fielPath: String?): Boolean {
        if (TextUtils.isEmpty(fielPath)) {
            return false
        }
        val file = File(fielPath)
        return if (file.isFile && file.exists()) {
            file.delete()
        } else false
    }


    /**
     * 返回小写的后缀名
     * @param url
     * @return
     */
    private fun getSuffer(murl: String?): String {
        try {
            if (TextUtils.isEmpty(murl)) return ""

            var url = murl!!
            //网页过来的图片
            var index = url.indexOf("@!")
            if (index != -1) {
                url = url.substring(0, index)
            }

            //DMH xcode 链接 保存的文件名去掉后面的xcode
            index = url.indexOf("?xcode")
            if (index != -1) {
                url = url.substring(0, index)
            }

            index = url.indexOf("?")
            if (index != -1) {
                url = url.substring(0, index)
            }

            index = url.indexOf("@")
            if (index != -1) {
                url = url.substring(0, index)
            }

            index = url.lastIndexOf(".")

            var value = (if (index != -1) url.substring(index + 1) else "").toLowerCase()

            if (TextUtils.isEmpty(value)) {
                index = murl.lastIndexOf(".")
                if (index >= 0)
                    value = murl.substring(index + 1)

                val atIndex = value.indexOf("@")
                if (atIndex >= 0) {
                    value = value.substring(0, atIndex)
                }
            }

            return value
        } catch (e: Exception) {
        }

        return ""
    }

    /**
     * 获取文件后缀
     * */
    fun getFileExtension(str: String?): String {
        var extension = ""
        val url: String = str ?: ""

        if (TextUtils.isEmpty(url)) return extension

        if (!url.startsWith("http")) {
            //如果是本地图片，在从数据数据流中获取类型，进一步确认
            try {
                if (File(url).exists()) {
                    val options = BitmapFactory.Options()
                    options.inJustDecodeBounds = true
                    BitmapFactory.decodeFile(url, options)
                    val mimeType = options.outMimeType
                    if (mimeType.contains("image/"))
                        extension = mimeType.replace("image/", "").toLowerCase()
                }
            } catch (e: Exception) {
            }
        }

        if (!TextUtils.isEmpty(extension)) return extension

        val temp = MimeTypeMap.getFileExtensionFromUrl(url)
        extension = if (!TextUtils.isEmpty(temp)) temp.toLowerCase() else ""

        if (TextUtils.isEmpty(extension))
            extension = getSuffer(url)//从url后缀来识别

        return extension
    }

    /**
     * 是否是gif图片
     * */
    fun isGIF(filePath: String?): Boolean {
        val type = getFileExtension(filePath)
        if ("gif" == type) {
            return true
        }
        return false
    }

    /**
     * 是否是音频文件
     *
     * @param filename
     * @return
     */
    fun isAudioFile(fileExt: String? = null, fullPath: String? = null): Boolean {

        var extension = fileExt

        if (!TextUtils.isEmpty(fullPath))
            extension = getFileExtension(fullPath)

        if (TextUtils.isEmpty(extension)) return false

        return arrayOf(
            "mp3",
            "aac",
            "flac",
            "amr",
            "wav",
            "m4a",
            "ogg"
        ).indexOf(extension?.toLowerCase() ?: "") != -1
    }

    fun isVideoFile(fileExt: String? = null, fullPath: String? = null): Boolean {

        var extension = fileExt

        if (!TextUtils.isEmpty(fullPath))
            extension = getFileExtension(fullPath)

        if (TextUtils.isEmpty(extension)) return false

        return arrayOf("mp4", "m3u8", "3gp", "avi", "rm", "rmvb", "mkv", "mov", "m4v").indexOf(
            extension?.toLowerCase() ?: ""
        ) != -1
    }

    /**
     *
     */
    fun isImageFile(fileExt: String? = null, fullPath: String? = null): Boolean {

        var extension = fileExt

        if (!TextUtils.isEmpty(fullPath))
            extension = getFileExtension(fullPath)

        if (TextUtils.isEmpty(extension)) return false

        return arrayOf(
            "jpg",
            "jpeg",
            "png",
            "webp",
            "apng",
            "gif",
            "bmp"
        ).indexOf(extension?.toLowerCase() ?: "") != -1
    }


    /**
     * 文件上传业务逻辑
     */
    fun getFileUploadType(fileExt: String): Int {
        return when {
            fileExt == "gif" -> 4
            isImageFile(fileExt) -> 3
            isVideoFile(fileExt) -> 2
            isAudioFile(fileExt) -> 1
            else -> 5
        }
    }

    /**
     * 文件上传业务逻辑
     */
    fun getJavaFileUploadType(fileExt: String): Int {
        return when {
            isImageFile(fileExt) -> 1
            isVideoFile(fileExt) -> 2
            isAudioFile(fileExt) -> 3
            else -> 4
        }
    }

    /**
     * 获取视频文件的，视频的宽和高
     */
    fun getVideoFileWidthAndHeight(videoFile: String): Array<String> {
        val retr = MediaMetadataRetriever()
        retr.setDataSource(videoFile)
        return arrayOf(
            retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH),
            retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
        )
    }

    /**
     * 获取视频文件的，视频的宽和高
     */
    fun getVideoFileWidthAndHeightByUri(ctx: Context, uri: Uri): Array<String> {
        val retr = MediaMetadataRetriever()
        retr.setDataSource(ctx, uri)
        return arrayOf(
            retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH),
            retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
        )
    }

    /**
     * 加入下载的文件到系统媒体数据库
     */
    fun scanMediaForFile(ctx: Context, filePath: String?) {
        if (TextUtils.isEmpty(filePath)) return

        MediaScannerConnection.scanFile(
            ctx,
            arrayOf(filePath),
            null
        ) { path, uri -> }
    }

    /**
     * Android 10上，存储文件到公共目录，picture 和video都放到DCIM/Camera  音频放到Musics目录下
     * 思路就是，app 先下载文件到自己的沙盒地方，然后从沙盒的地方通过ConternResolver拷贝到公共目录
     *
     * 类似的需求是，用户下载图片或者拍摄照片或者下载视频、音频
     */
    @TargetApi(29)
    fun storeFileInPublicAtTargetQ(ctx: Context, file: File?) {
        file ?: return
        if (!file.exists()) return

        var external: Uri? = null

        try {
            Thread {
                val values = when {
                    isVideoFile(fullPath = file.absolutePath) -> ContentValues().apply {
                        put(MediaStore.Video.Media.DESCRIPTION, "Create from" + ctx.packageName)
                        put(MediaStore.Video.Media.DISPLAY_NAME, file.name)
                        put("relative_path", UserMediaPath)
                        external = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    }
                    isImageFile(fullPath = file.absolutePath) -> ContentValues().apply {
                        put(MediaStore.Images.Media.DESCRIPTION, "Create from" + ctx.packageName)
                        put(MediaStore.Images.Media.DISPLAY_NAME, file.name)
                        put("relative_path", UserMediaPath)
                        external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    }
                    isAudioFile(fullPath = file.absolutePath) -> ContentValues().apply {
                        put(MediaStore.Audio.Media.DISPLAY_NAME, file.name)
                        put("relative_path", Environment.DIRECTORY_MUSIC)
                        external = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                    else -> null
                }

                values ?: return@Thread
                external ?: return@Thread

                val resolver = ctx.contentResolver
                //相同的文件如果已经insert的话，这里这个inserUri会返回null
                val inserUri = resolver.insert(external!!, values)

                var os: OutputStream? = null
                try {
                    if (inserUri != null)
                        os = resolver.openOutputStream(inserUri)

                    os ?: return@Thread

                    val inputStream = FileInputStream(file)
                    val buffer = ByteArray(1024)
                    var byteRead = inputStream.read(buffer)
                    while (byteRead != -1) {
                        os.write(buffer, 0, byteRead)
                        byteRead = inputStream.read(buffer)
                    }
                    os.close()
                    inputStream.close()

                    //最后删除源文件
//                    file.delete()
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    os?.close()
                }
            }.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 从公共区域拷贝文件到自己的沙盒，用于后续的文件压缩，处理
     * @param source 源文件的Uri
     * @param destfile 需要拷贝的目标文件地址
     */
    fun copyFileFromPublicToPrivateAtTargetQ(ctx: Context, source: Uri, destfile: File): File? {

        try {
            val fileIn = ctx.contentResolver.openInputStream(source)
            fileIn ?: return null

            val fileOut = FileOutputStream(destfile)
            val buffer = ByteArray(1024)
            var byteRead = fileIn.read(buffer)
            while (byteRead != -1) {
                fileOut.write(buffer, 0, byteRead)
                byteRead = fileIn.read(buffer)
            }
            fileIn.close()
            fileOut.close()

            return destfile
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 文件下载后，需要根据是否是存储到公共区域做区分处理
     * 1.Android 10 如果需要存储到公共区域 需要从沙盒位置通过contentprivoder拷贝文件到系统目录下
     * 2.如果是Android 10以下，那么需要刷新媒体库
     */
    fun dealPicAfterSave(saveInPublicPlace: Boolean, downloadFile: File) {
        if (saveInPublicPlace && downloadFile.exists()) {
            if (isAndroidQorAbove)
                storeFileInPublicAtTargetQ(Constants.getContext(), downloadFile)
            else
                scanMediaForFile(Constants.getContext(), downloadFile.absolutePath)
        }
    }

    /**
     * 通过Uri来判断文件是否存在
     */
    fun isFileExistsByUri(context: Context, uri: Uri): Boolean {
        var afd: AssetFileDescriptor? = null
        val cr = context.contentResolver
        try {
            afd = cr.openAssetFileDescriptor(uri, "r")
            afd?.close() ?: return false
        } catch (e: FileNotFoundException) {
            return false
        } finally {
            afd?.close()
        }
        return true
    }


    /**
     * 判断当前Video是否支持编辑
     */
    fun isSupportVideo(path: String?): Boolean {
        if (TextUtils.isEmpty(path)) return false

        val var1 = MediaExtractor()
        try {
            var1.setDataSource(path!!)
        } catch (var3: IOException) {
            var3.printStackTrace()
        }

        if (var1.trackCount <= 1) {
            //无音轨
            return false
        }

        return true
    }

    /**
     * 获取视频文件大小
     *
     * @return
     */
    fun getFileSize(path: String?): Long {
        if (TextUtils.isEmpty(path)) return 0
        return File(path).length()
    }
}