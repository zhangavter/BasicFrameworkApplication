package zhang.acfun.com.basicframeworklib.util

import android.annotation.SuppressLint
import android.os.Looper
import android.view.View
import android.view.ViewConfiguration
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by zxb on 2016/10/24.
 */
object RxJavaUtil {

    /**
     * @param delayTime 单位毫秒
     * @param consumer
     */
    @SuppressLint("CheckResult")
    fun delayAction(delayTime: Int, consumer: () -> Unit) {
        Observable.timer(delayTime.toLong(), TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { consumer.invoke() }
    }

    /**
     * @param view
     * @param listener
     */
    @SuppressLint("CheckResult")
    fun click(view: View, listener: ((View) -> Unit)?) {
      /*  RxView.clicks(view)
            .throttleFirst(ViewConfiguration.getDoubleTapTimeout().toLong(), TimeUnit.MILLISECONDS)
            .subscribe { listener?.invoke(view) }*/
    }

    /**
     * 运行于UI线程
     *
     * @param action
     */
    fun runOnUiThread(action: (() -> Unit)?) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            try {
                action?.invoke()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else {
            runThread(AndroidSchedulers.mainThread(), action)
        }
    }

    /**
     * 运行于那个线程
     *
     * @param scheduler
     * @param action
     */
    private fun runThread(scheduler: Scheduler, action: (() -> Unit)?) {
        Observable.empty<Any>()
            .observeOn(scheduler)
            .doOnComplete { action?.invoke() }
            .subscribe()
    }

    /**
     * 运行于非ui线程中
     *
     * @param action
     */
    fun runOnThread(action: () -> Unit) {
        runThread(Schedulers.io(), action)
    }
}
