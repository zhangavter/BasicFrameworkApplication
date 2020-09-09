package zhang.acfun.com.basicframeworklib.network;

import android.content.Context;
import android.os.Build;

import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import zhang.acfun.com.basicframeworklib.Constants;
import zhang.acfun.com.basicframeworklib.network.cookie.PersistentCookieJar;
import zhang.acfun.com.basicframeworklib.network.cookie.cache.SetCookieCache;
import zhang.acfun.com.basicframeworklib.network.cookie.https.HttpsUtils;
import zhang.acfun.com.basicframeworklib.network.cookie.https.Tls12SocketFactory;
import zhang.acfun.com.basicframeworklib.network.cookie.persistence.SharedPrefsCookiePersistor;
import zhang.acfun.com.basicframeworklib.network.download.OnFileDownloadListener;
import zhang.acfun.com.basicframeworklib.network.download.ProgressListener;
import zhang.acfun.com.basicframeworklib.network.download.ProgressResponseBody;
import zhang.acfun.com.basicframeworklib.util.FileUtil;
import zhang.acfun.com.basicframeworklib.util.MLog;
import zhang.acfun.com.basicframeworklib.util.SecurityUtil;


/**
 * 开源库官网 http://loopj.com/android-async-http/
 *
 * @author milanoouser
 */
public class HttpUtil {

    private static OkHttpClient client;
    private static OkHttpClient.Builder clientBuilder;

    /**
     * 获取单例
     *
     * @return
     */
    public static OkHttpClient getInstance(Context context) {
        if (client != null)
            return client;

        synchronized (HttpUtil.class) {
            clientBuilder = initClient(context);
            client = clientBuilder.build();
        }

        return client;
    }

    public static void refreshApiHelper() {
        client = null;
    }

    /**
     *
     */
    private static OkHttpClient.Builder initClient(Context context) {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        if (Constants.EnDebug) {
            okHttpClient.connectTimeout(60, TimeUnit.SECONDS);
        } else {
            okHttpClient.connectTimeout(10, TimeUnit.SECONDS);
        }

        if (!SecurityUtil.getNeedCapturePacket())
            okHttpClient.proxy(Proxy.NO_PROXY);

        okHttpClient.readTimeout(10, TimeUnit.SECONDS);
        okHttpClient.writeTimeout(10, TimeUnit.SECONDS);
        okHttpClient.retryOnConnectionFailure(true);
        okHttpClient.cookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context)));
        okHttpClient.hostnameVerifier((hostname, session) -> true);

        if (Constants.EnDebug) {
            okHttpClient.addInterceptor((new HttpLoggingInterceptor()).setLevel(HttpLoggingInterceptor.Level.BODY));
//            okHttpClient.addNetworkInterceptor(new StethoInterceptor());
        }

        //支持https访问  Android 5.0以下 TLSV1.1和TLSV1.2是关闭的，要自己打开，Android 5.0以上是打开的
        //这里就针对这两种情况，不同处理
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            //Android 5.0以上
            HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
            okHttpClient.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        } else {
            //Android 5.0 以下
            SSLContext sslContext = null;
            try {
                sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, null, null);

                SSLSocketFactory socketFactory = new Tls12SocketFactory(sslContext.getSocketFactory());
                okHttpClient.sslSocketFactory(socketFactory, new HttpsUtils.UnSafeTrustManager());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return okHttpClient;
    }

    /**
     * 使用get方法,用一个完整url获取一个string对象
     *
     * @param urlString url绝对路径
     * @param callBack  结果
     */
    public static void get(Context context, String urlString, HttpCallBack callBack) {
        Request request = new Request.Builder()
                .url(urlString)
                .get()
                .build();
        getInstance(context).newCall(request).enqueue(callBack);
    }

    /**
     * 使用post方法,url里面带参数,获取一个string对象
     *
     * @param urlString   url绝对路径
     * @param formBuilder 参数
     * @param callBack    结果
     */
    public static void post(Context context, String urlString, FormBody.Builder formBuilder, HttpCallBack callBack) {
        RequestBody body = formBuilder.build();
        Request request = new Request.Builder()
                .url(urlString)
                .post(body)
                .build();
        getInstance(context).newCall(request).enqueue(callBack);
    }


    /**
     * 使用post方法,上传文件
     *
     * @param urlString   url绝对路径
     * @param filebuilder 参数
     * @param callBack    结果
     */
    public static void postFile(Context context, String urlString, MultipartBody.Builder filebuilder, HttpCallBack callBack) {
        RequestBody body = filebuilder.build();
        Request request = new Request.Builder()
                .url(urlString)
                .post(body)
                .build();
        getInstance(context).newCall(request).enqueue(callBack);
    }


    /**
     * 下载
     *
     * @param fileUrl  下载
     * @param savefile 本地文件存储的文件夹
     * @param listener 监听
     */

    public static void download(Context context, String fileUrl, final File savefile, final OnFileDownloadListener listener) {

        getInstance(context);

        final Request request = new Request.Builder()
                .url(fileUrl)
                .build();

        final ProgressListener progressListener = new ProgressListener() {
            private int progress = -1;//onProgress到了100%的时候会执行2次，我们这里判断一下

            @Override
            public void update(final long bytesRead, final long contentLength, boolean done) {
                if (listener != null && contentLength > 0) {
                    int i = (int) ((100 * bytesRead) / contentLength);
                    if (progress == i) {
                        return;
                    }
                    progress = i;
                    MLog.d("图片下载进度", String.valueOf(progress));
                    listener.onProgress(progress);
                }
            }
        };

        clientBuilder.networkInterceptors().add(chain -> {
            Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
        });


        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    if (response != null && response.body() != null) {
                        byte[] bt = response.body().bytes();
                        if (!savefile.exists()) {
                            savefile.createNewFile();
                        }
                        FileUtil.INSTANCE.getFileFromBytes(bt, savefile);

                        if (listener != null)
                            listener.onResponse(savefile.getAbsolutePath());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                //下载失败
                if (listener != null)
                    listener.onFailure("Fail");
            }

        });
    }

}
