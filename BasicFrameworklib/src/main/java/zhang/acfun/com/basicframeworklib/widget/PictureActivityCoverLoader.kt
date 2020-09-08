package zhang.acfun.com.basicframeworklib.widget

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import net.mikaelzero.mojito.interfaces.ActivityCoverLoader
import net.mikaelzero.mojito.interfaces.IMojitoActivity
import net.mikaelzero.mojito.interfaces.IMojitoFragment
import zhang.acfun.com.basicframeworklib.R
import zhang.acfun.com.basicframeworklib.util.ToastUtil

/**
 * 图片查看器-插件
 * **/
class PictureActivityCoverLoader : ActivityCoverLoader {
    lateinit var view: View
    var numTv: TextView? = null

    override fun attach(context: IMojitoActivity) {
        view =
            LayoutInflater.from(context.getContext()).inflate(R.layout.bilibili_cover_layout, null)
        numTv = view.findViewById(R.id.numTv)

        val closeIv = view.findViewById<ImageView>(R.id.closeIv)
        closeIv.setOnClickListener {
            context.getCurrentFragment().backToMin()
        }

        val moreIcon = view.findViewById<ImageView>(R.id.moreIv)
        moreIcon.setOnClickListener {
            ToastUtil.showToast("more..")
        }
    }

    override fun providerView(): View {
        return view
    }

    override fun move(moveX: Float, moveY: Float) {

    }

    override fun fingerRelease(isToMax: Boolean, isToMin: Boolean) {

    }

    @SuppressLint("SetTextI18n")
    override fun pageChange(iMojitoFragment: IMojitoFragment, totalSize: Int, position: Int) {
        numTv?.text = (position + 1).toString() + "/" + totalSize
    }

}