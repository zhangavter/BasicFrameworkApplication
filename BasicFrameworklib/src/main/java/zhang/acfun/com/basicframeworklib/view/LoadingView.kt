package zhang.acfun.com.basicframeworklib.view

import android.app.Dialog
import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.airbnb.lottie.LottieAnimationView
import zhang.acfun.com.basicframeworklib.R

/**
 * 默认Loading 加载样式
 * zxb
 * **/
class LoadingView : Dialog {

    constructor(context: Context) : super(context, R.style.CustomProgressDialog) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(
        context,
        R.style.CustomProgressDialog
    ) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        R.style.CustomProgressDialog
    ) {
        init()
    }

    private fun init() {
        val contentView = View.inflate(context, R.layout.dialog_progress, null)
        val loadingAnimation = contentView.findViewById<LottieAnimationView>(R.id.loading_animation)
        setContentView(contentView)

        setOnDismissListener {
            loadingAnimation?.clearAnimation()
        }
    }

    override fun show() {
        super.show()
    }

}