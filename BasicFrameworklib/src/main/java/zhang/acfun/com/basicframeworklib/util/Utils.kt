package zhang.acfun.com.basicframeworklib.util

import android.content.Context
import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest
import java.util.*

/**
 * @author Soli
 * @Time 18-5-17 下午4:11
 */
object Utils {

    // 去掉"-"符号
     fun uuid(): String {
        val uuid = UUID.randomUUID()
        val str = uuid.toString()
        return (str.substring(0, 8) + str.substring(9, 13)
                + str.substring(14, 18) + str.substring(19, 23)
                + str.substring(24))
    }



    /**
     * @param sourceStr
     * @return
     */
    fun MD5(sourceStr: String): String {
        try {
            val md = MessageDigest.getInstance("MD5")
            md.update(sourceStr.toByteArray())
            return bytesToHexString(md.digest())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }



    /**
     * 检测是否有虚拟按键
     *
     * @param context
     * @return
     */
    fun checkDeviceHasNavigationBar(context: Context): Boolean {
        var hasNavigationBar = false
        val rs = context.resources
        val id = rs.getIdentifier("config_showNavigationBar", "bool", "android")
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id)
        }

        return hasNavigationBar

    }

    /**
     * 获取虚拟按键的高度
     *
     * @param context
     * @return
     */
    fun getNavigationBarHeight(context: Context): Int {
        var navigationBarHeight = 0
        val rs = context.resources
        val id = rs.getIdentifier("navigation_bar_height", "dimen", "android")
        if (id > 0 && checkDeviceHasNavigationBar(context)) {
            navigationBarHeight = rs.getDimensionPixelSize(id)
        }
        return navigationBarHeight
    }

    /**
     * 获取屏幕高度
     */
    fun getScreenHeightPixels(context: Context): Int {
        // 获取当前屏幕
        val dm = context.applicationContext.resources.displayMetrics

//        return dm.heightPixels
        val hasNav = checkDeviceHasNavigationBar(context)
        return if (hasNav) {
            dm.heightPixels - getNavigationBarHeight(context)
        } else dm.heightPixels

    }

    private fun bytesToHexString(src: ByteArray?): String {
        if (src == null || src.isEmpty()) {
            return ""
        }

        val stringBuilder = StringBuilder()
        for (i in src.indices) {
            val v = src[i].toInt() and 0xff
            val hv = Integer.toHexString(v)
            if (hv.length < 2) {
                stringBuilder.append(0)
            }
            stringBuilder.append(hv)
        }
        return stringBuilder.toString()
    }

    fun getFileMD5(file: File): String {
        if (!file.isFile) {
            return ""
        }

        try {
            val buffer = ByteArray(1024)
            val digest = MessageDigest.getInstance("MD5")
            val input = FileInputStream(file)
            var len = 0
            while (len != -1) {
                len = input.read(buffer, 0, 1024)
                if (len != -1)
                    digest.update(buffer, 0, len)
            }
            input.close()
            return bytesToHexString(digest.digest())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }


    /**
     * 获取版本名字versionName
     *
     * @return
     */
    fun getVersionStr(context: Context): String{
        return try {
            val manager = context.packageManager
            val info = manager.getPackageInfo(context.packageName, 0)
            info.versionName
        } catch (e: java.lang.Exception) {
            "0.0"
        }
    }
}
