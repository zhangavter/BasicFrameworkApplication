package zhang.acfun.com.basicframeworklib.view.dialog.photoSelect

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.media.MediaPlayer
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.Images.ImageColumns
import zhang.acfun.com.basicframeworklib.Constants
import zhang.acfun.com.basicframeworklib.R
import zhang.acfun.com.basicframeworklib.model.AlbumModel
import zhang.acfun.com.basicframeworklib.model.PhotoModel
import zhang.acfun.com.basicframeworklib.util.FileUtil
import java.io.File
import java.io.IOException
import java.util.*

class AlbumController(private val ctx: Context) {

    companion object {
        /* 选择本地视频时，最小视频时长(单位：ms) */
        private const val MIN_VIDEO_DURATION = 5000

        /* 选择本地视频时，最大视频时长(单位：ms) */
        private const val MAX_VIDEO_DURATION = 1800000
    }

    private val resolver = ctx.contentResolver

    //图片3k
    private val minSize = 1024 * 2

    enum class Type {
        ALL, PHOTO, VIDEO
    }

    /**
     * 获取资源
     *
     * @param queryCount 需要查询多少条，-1为全部
     * @return
     */
    fun getCurrent(
        type: Type,
        queryCount: Int
    ): List<PhotoModel> {
        return when (type) {
            Type.ALL -> getVideoImage(type, queryCount)
            Type.PHOTO -> nearPhoto
            Type.VIDEO -> video
        }
    }// "最近照片"相册

    /**
     * 获取所有相册列表
     *
     * @return
     */
    val albums: List<AlbumModel>
        get() {
            val albums: MutableList<AlbumModel> = ArrayList()
            val map: MutableMap<String, AlbumModel> = HashMap()

            val cursor = resolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                arrayOf(
                    ImageColumns.DATA,
                    ImageColumns.BUCKET_DISPLAY_NAME,
                    ImageColumns.SIZE,
                    MediaStore.Images.Media._ID
                ),
                null,
                null,
                null
            )

            if (cursor == null || !cursor.moveToNext()) return ArrayList()

            cursor.moveToLast()

            // "最近照片"相册
            val current = addAlbumModel(
                cursor,
                ctx.resources.getString(R.string.str_allPicture),
                0,
                true
            )

            albums.add(current)

            do {
                if (cursor.getInt(cursor.getColumnIndex(ImageColumns.SIZE)) < minSize) continue

                current.increaseCount()

                val name =
                    cursor.getString(cursor.getColumnIndex(ImageColumns.BUCKET_DISPLAY_NAME)) ?: "无"

                if (map.keys.contains(name))
                    map[name]!!.increaseCount()
                else {
                    val album = addAlbumModel(cursor, name, 1)
                    map[name] = album
                    albums.add(album)
                }
            } while (cursor.moveToPrevious())
            cursor.close()
            return albums
        }

    /**
     *
     */
    private fun addAlbumModel(
        cursor: Cursor,
        name: String,
        count: Int,
        ischeck: Boolean = false
    ): AlbumModel {
        val filepath = cursor.getString(cursor.getColumnIndex(ImageColumns.DATA))
        val fileUri = forImageUri(
            cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID))
        )
        val isExist = isFileExist(filepath, fileUri)

        return AlbumModel(
            name,
            count,
            if (isExist) filepath else "",
            if (isExist) fileUri else null,
            ischeck
        )
    }

    /**
     *
     */
    private fun forImageUri(_id: Long) =
        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, _id)

    /**
     *
     */
    private fun forVideoUri(_id: Long) =
        ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, _id)

    /**
     *
     */
    private fun isFileExist(file: String, path: Uri): Boolean {
        return if (FileUtil.isAndroidQorAbove)
            FileUtil.isFileExistsByUri(Constants.getContext(), path)
        else
            FileUtil.isFileExists(file)
    }

    /**
     * 获取对应相册下的照片
     */
    fun getAlbum(name: String): List<PhotoModel> {
        val cursor = resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(
                ImageColumns.BUCKET_DISPLAY_NAME,
                ImageColumns.DATA,
                ImageColumns.DATE_ADDED,
                ImageColumns.SIZE
            ),
            "bucket_display_name = ?",
            arrayOf(name),
            ImageColumns.DATE_ADDED
        )
        if (cursor == null || !cursor.moveToNext()) return ArrayList()
        val photos: MutableList<PhotoModel> = ArrayList()
        cursor.moveToLast()
        do {
            if (cursor.getLong(cursor.getColumnIndex(ImageColumns.SIZE)) > minSize) {
                val path = cursor.getString(cursor.getColumnIndex(ImageColumns.DATA))
                if (File(path).exists()) {
                    //只有存在的才可以用，因为这个文件可能已经删除了
                    val photoModel = PhotoModel()
                    photoModel.originalPath = path
                    photos.add(photoModel)
                }
            }
        } while (cursor.moveToPrevious())
        cursor.close()
        return photos
    }//查询方式更改

    /**
     * 获取最近使用的照片
     */
    private val nearPhoto: List<PhotoModel>
        private get() {
            //查询方式更改
            val cursor = resolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                arrayOf(
                    ImageColumns.DATA, ImageColumns.DATE_ADDED, ImageColumns.SIZE,
                    MediaStore.Images.Media._ID
                ),
                null,
                null,
                ImageColumns.DATE_ADDED
            )
            if (cursor == null || !cursor.moveToNext()) return ArrayList()

            val photos: MutableList<PhotoModel> = ArrayList()
            cursor.moveToLast()

            do {
                if (cursor.getLong(cursor.getColumnIndex(ImageColumns.SIZE)) > minSize) {

                    val path = cursor.getString(cursor.getColumnIndex(ImageColumns.DATA))
                    val uri = forImageUri(
                        cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID))
                    )
                    if (isFileExist(path, uri)) {
                        photos.add(PhotoModel(path, uri, 0))
                    }
                }
            } while (cursor.moveToPrevious())
            cursor.close()
            return photos
        }

    /**
     * 获取视频和图片，按时间排序
     *
     * @param queryCount 需要查询多少条，-1为全部
     */
    private fun getVideoImage(
        type: Type,
        queryCount: Int
    ): List<PhotoModel> {
        val QUERY_URI = MediaStore.Files.getContentUri("external")
        val PROJECTION = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.MIME_TYPE,
            MediaStore.MediaColumns.SIZE,
            MediaStore.MediaColumns.DATA,
            MediaStore.Video.Media.DURATION
        )
        val SELECTION = ("(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)"
                + " AND " + MediaStore.MediaColumns.SIZE + ">0")
        val SELECTION_ALL_ARGS = when (type) {
            Type.ALL -> {
                arrayOf(
                    MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
                    MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
                )
            }
            Type.VIDEO -> {
                arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString())
            }
            else -> {
                arrayOf(
                    MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
                    MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
                )
            }
        }
        val cursor = resolver.query(
            QUERY_URI,
            PROJECTION,
            SELECTION,
            SELECTION_ALL_ARGS,
            ImageColumns.DATE_ADDED
        )
        if (cursor == null || !cursor.moveToNext()) return ArrayList()
        val photos: MutableList<PhotoModel> = ArrayList()
        cursor.moveToLast()
        do {
            if (queryCount != -1) {
                //限制查询条数,因为会过滤掉一些时长不满足的视频，所以我们直接用photos.size()来判断数量
                if (photos.size >= queryCount) {
                    break
                }
            }
            try {
                val data =
                    cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA))
                val mimeType =
                    cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE))
                val id =
                    cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID))

                var contentUri: Uri?
                var fileType = 0
                var duration = 0

                if (mimeType.startsWith("video")) {
                    duration =
                        cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION))
                    if (duration <= 0) {
                        try {
                            //不知为何部分视频使用cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));获取的时长为0，所以使用MediaPlayer获取时长
                            val mediaPlayer = MediaPlayer()
                            mediaPlayer.setDataSource(data)
                            mediaPlayer.prepare()
                            duration = mediaPlayer.duration
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                    if (duration <= MIN_VIDEO_DURATION || duration > MAX_VIDEO_DURATION) {
                        //过滤掉时长不足的视频
                        continue
                    }
                }

                when {
                    mimeType.startsWith("image") -> {
                        fileType = 0
                        contentUri = forImageUri(id)
                    }
                    mimeType.startsWith("video") -> {
                        fileType = 1
                        contentUri = forVideoUri(id)
                    }
                    else -> {
                        contentUri = MediaStore.Files.getContentUri("external")
                    }
                }

                if (isFileExist(data, contentUri)) {
                    val photoModel = PhotoModel(data, contentUri, fileType)
                    photoModel.videoTime = duration
                    photos.add(photoModel)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } while (cursor.moveToPrevious())
        cursor.close()
        return photos
    }

    /**
     * 只获取视频
     */
    private val video: List<PhotoModel>
        private get() {
            val cursor = resolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null,
                null, "date_added desc"
            )
            val photos: MutableList<PhotoModel> = ArrayList()
            while (cursor!!.moveToNext()) {
                val duration =
                    cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION))

                if (duration <= MIN_VIDEO_DURATION || duration > MAX_VIDEO_DURATION) {
                    //过滤掉时长不足的视频
                    continue
                }

                val path =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
                val uri = forVideoUri(
                    cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID))
                )
                if (isFileExist(path, uri)) {
                    val photoModel = PhotoModel(path, uri, 1)
                    photoModel.videoTime = duration
                    photos.add(photoModel)
                }
            }
            cursor.close()
            return photos
        }

}