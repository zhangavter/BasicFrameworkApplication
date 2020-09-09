package zhang.acfun.com.basicframeworklib.util;

import android.util.Log;

import zhang.acfun.com.basicframeworklib.Constants;

/**
 * 全局打印日志工具类
 */
public class MLog {

    /**
     * 是否开启全局打印日志
     */


    private static final String DEFAULT_TAG = "TaiheDebug";

    public static void i(String TAG, String msg) {
        if (Constants.EnDebug) {
            StackTraceElement[] sElements = new Throwable().getStackTrace();
            String className = sElements[1].getFileName();
            String methodName = sElements[1].getMethodName();
            int lineNumber = sElements[1].getLineNumber();

            StringBuffer buffer = new StringBuffer();
            buffer.append(className + "---");
            buffer.append(methodName + "---");

            buffer.append(lineNumber + "---");

            buffer.append(msg);
            msg = buffer.toString();
            Log.i(TAG, msg);
        } /*else
            BuglyLog.i(TAG, msg);*/
    }

    public static void e(String TAG, String msg) {
        if (Constants.EnDebug) {
            StackTraceElement[] sElements = new Throwable().getStackTrace();
            String className = sElements[1].getFileName();
            String methodName = sElements[1].getMethodName();
            int lineNumber = sElements[1].getLineNumber();

            StringBuffer buffer = new StringBuffer();
            buffer.append(className + "---");
            buffer.append(methodName + "---");

            buffer.append(lineNumber + "---");

            buffer.append(msg);
            msg = buffer.toString();
            Log.e(TAG, msg);
        } /*else
            BuglyLog.e(TAG, msg);*/
    }

    public static void d(String TAG, String msg) {
        if (Constants.EnDebug) {
            StackTraceElement[] sElements = new Throwable().getStackTrace();
            String className = sElements[1].getFileName();
            String methodName = sElements[1].getMethodName();
            int lineNumber = sElements[1].getLineNumber();

            StringBuffer buffer = new StringBuffer();
            buffer.append(className + "---");
            buffer.append(methodName + "---");

            buffer.append(lineNumber + "---");

            buffer.append(msg);
            msg = buffer.toString();
            Log.d(TAG, msg);
        } /*else
            BuglyLog.d(TAG, msg);*/
    }

    public static void v(String TAG, String msg) {
        if (Constants.EnDebug) {
            StackTraceElement[] sElements = new Throwable().getStackTrace();
            String className = sElements[1].getFileName();
            String methodName = sElements[1].getMethodName();
            int lineNumber = sElements[1].getLineNumber();

            StringBuffer buffer = new StringBuffer();
            buffer.append(className + "---");
            buffer.append(methodName + "---");

            buffer.append(lineNumber + "---");

            buffer.append(msg);
            msg = buffer.toString();
            Log.v(TAG, msg);
        } /*else
            BuglyLog.v(TAG, msg);*/
    }

    public static void w(String TAG, String msg) {
        if (Constants.EnDebug) {
            StackTraceElement[] sElements = new Throwable().getStackTrace();
            String className = sElements[1].getFileName();
            String methodName = sElements[1].getMethodName();
            int lineNumber = sElements[1].getLineNumber();

            StringBuffer buffer = new StringBuffer();
            buffer.append(className + "---");
            buffer.append(methodName + "---");

            buffer.append(lineNumber + "---");

            buffer.append(msg);
            msg = buffer.toString();
            Log.w(TAG, msg);
        } /*else
            BuglyLog.w(TAG, msg);*/
    }

    public static void i(String msg) {

        if (Constants.EnDebug) {
            StackTraceElement[] sElements = new Throwable().getStackTrace();
            String className = sElements[1].getFileName();
            String methodName = sElements[1].getMethodName();
            int lineNumber = sElements[1].getLineNumber();

            StringBuffer buffer = new StringBuffer();
            buffer.append(className + "---");
            buffer.append(methodName + "---");

            buffer.append(lineNumber + "---");

            buffer.append(msg);
            msg = buffer.toString();
            Log.i(DEFAULT_TAG, msg);
        } /*else
            BuglyLog.i(DEFAULT_TAG, msg);*/
    }

    public static void e(String msg) {

        if (Constants.EnDebug) {
            StackTraceElement[] sElements = new Throwable().getStackTrace();
            String className = sElements[1].getFileName();
            String methodName = sElements[1].getMethodName();
            int lineNumber = sElements[1].getLineNumber();

            StringBuffer buffer = new StringBuffer();
            buffer.append(className + "---");
            buffer.append(methodName + "---");

            buffer.append(lineNumber + "---");

            buffer.append(msg);
            msg = buffer.toString();
            Log.e(DEFAULT_TAG, msg);
        } /*else
            BuglyLog.e(DEFAULT_TAG, msg);*/
    }

    public static void d(String msg) {

        if (Constants.EnDebug) {
            StackTraceElement[] sElements = new Throwable().getStackTrace();
            String className = sElements[1].getFileName();
            String methodName = sElements[1].getMethodName();
            int lineNumber = sElements[1].getLineNumber();

            StringBuffer buffer = new StringBuffer();
            buffer.append(className + "---");
            buffer.append(methodName + "---");

            buffer.append(lineNumber + "---");

            buffer.append(msg);
            msg = buffer.toString();
            Log.d(DEFAULT_TAG, msg);
        } /*else
            BuglyLog.d(DEFAULT_TAG, msg);*/
    }

    public static void v(String msg) {
        if (Constants.EnDebug) {
            StackTraceElement[] sElements = new Throwable().getStackTrace();
            String className = sElements[1].getFileName();
            String methodName = sElements[1].getMethodName();
            int lineNumber = sElements[1].getLineNumber();

            StringBuffer buffer = new StringBuffer();
            buffer.append(className + "---");
            buffer.append(methodName + "---");

            buffer.append(lineNumber + "---");

            buffer.append(msg);
            msg = buffer.toString();
            Log.v(DEFAULT_TAG, msg);
        } /*else
            BuglyLog.v(DEFAULT_TAG, msg);*/
    }

    public static void w(String msg) {
        if (Constants.EnDebug) {
            StackTraceElement[] sElements = new Throwable().getStackTrace();
            String className = sElements[1].getFileName();
            String methodName = sElements[1].getMethodName();
            int lineNumber = sElements[1].getLineNumber();

            StringBuffer buffer = new StringBuffer();
            buffer.append(className + "---");
            buffer.append(methodName + "---");

            buffer.append(lineNumber + "---");

            buffer.append(msg);
            msg = buffer.toString();
            Log.w(DEFAULT_TAG, msg);
        } /*else
            BuglyLog.w(DEFAULT_TAG, msg);*/
    }

}
