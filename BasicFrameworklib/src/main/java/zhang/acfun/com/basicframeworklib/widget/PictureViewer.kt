package zhang.acfun.com.basicframeworklib.widget

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import net.mikaelzero.mojito.Mojito
import net.mikaelzero.mojito.impl.DefaultPercentProgress
import net.mikaelzero.mojito.impl.SimpleMojitoViewCallback
import net.mikaelzero.mojito.interfaces.IProgress
import net.mikaelzero.mojito.loader.InstanceLoader

/**图片查看器
 * zxb
 * **/
class PictureViewer constructor(
    val context: Context,
    val imgUrls: List<String>,
    val reycleView: RecyclerView,
    val viewId: Int,
    val position: Int,
) {

    fun startPictureLoad() {
        Mojito.with(context)
            .urls(imgUrls)
            .position(position)
            .views(reycleView, viewId)
            .autoLoadTarget(false)
            .setActivityCoverLoader(PictureActivityCoverLoader())
            .setProgressLoader(object : InstanceLoader<IProgress> {
                override fun providerInstance(): IProgress {
                    return DefaultPercentProgress()
                }
            })
            .setOnMojitoListener(object : SimpleMojitoViewCallback() {
                override fun onLongClick(
                    fragmentActivity: FragmentActivity?,
                    view: View,
                    x: Float,
                    y: Float,
                    position: Int
                ) {
                    Toast.makeText(context, "long click", Toast.LENGTH_SHORT).show()
                }

                override fun onClick(view: View, x: Float, y: Float, position: Int) {
                    Toast.makeText(context, "tap click", Toast.LENGTH_SHORT).show()
                }
            })
            .start()
    }
}