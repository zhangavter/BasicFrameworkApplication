package zhang.acfun.com.basicframeworkapplication.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_display.*
import net.mikaelzero.mojito.Mojito
import net.mikaelzero.mojito.impl.DefaultPercentProgress
import net.mikaelzero.mojito.impl.NumIndicator
import net.mikaelzero.mojito.impl.SimpleMojitoViewCallback
import net.mikaelzero.mojito.interfaces.IProgress
import net.mikaelzero.mojito.loader.InstanceLoader
import zhang.acfun.com.basicframeworkapplication.R
import zhang.acfun.com.basicframeworkapplication.adapter.FrescoAdapter
import zhang.acfun.com.basicframeworklib.Constants
import zhang.acfun.com.basicframeworklib.widget.PictureActivityCoverLoader
import zhang.acfun.com.basicframeworklib.widget.PictureViewer
import java.util.*

class PreviewActivity : AppCompatActivity() {
    var context: Context? = null

    fun getNormalImages(): List<String> {
        val list: MutableList<String> = ArrayList()
        list.add("https://cdn.nlark.com/yuque/0/2020/jpeg/252337/1592042332605-assets/web-upload/1af8e4c0-bf8b-410a-bfff-a16fec01ccb5.jpeg")
        list.add("https://cdn.nlark.com/yuque/0/2020/jpeg/252337/1591710912974-assets/web-upload/1e6325b7-4e26-443f-98f8-aa3925222ea1.jpeg")
        list.add("https://cdn.nlark.com/yuque/0/2020/jpeg/252337/1588042170204-assets/web-upload/48a5152a-5024-43fd-bd50-796d6f284e77.jpeg")
        list.add("https://cdn.nlark.com/yuque/0/2020/jpeg/252337/1592042333257-assets/web-upload/dfe8a4eb-9872-444b-b2a5-83378f467915.jpeg")
        list.add("https://cdn.nlark.com/yuque/0/2020/jpeg/252337/1591753659216-assets/web-upload/2c772338-b6b6-4173-a830-202831511172.jpeg")
        list.add("https://cdn.nlark.com/yuque/0/2020/jpeg/252337/1592042333210-assets/web-upload/8d20ed3d-1472-47c9-a2e6-da96e6019299.jpeg")
        list.add("https://cdn.nlark.com/yuque/0/2020/jpeg/252337/1592042333165-assets/web-upload/cde12f44-07bb-46aa-ab7d-0ced4783b2ee.jpeg")
        list.add("https://cdn.nlark.com/yuque/0/2020/png/252337/1587091196083-assets/web-upload/62122ab5-986b-4662-be88-d3007a5e31c5.png")
        list.add("https://cdn.nlark.com/yuque/0/2020/jpeg/252337/1592288524369-assets/web-upload/84ee5f6c-8459-4497-8175-153836bef167.jpeg")
        return list
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        setContentView(R.layout.activity_display)

        recyclerView.layoutManager = GridLayoutManager(this, 3)
        val adapter = FrescoAdapter()
        adapter.setList(getNormalImages())
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener { _, _, position ->
            PictureViewer(
                context!!,
                getNormalImages(),
                recyclerView,
                R.id.srcImageView,
                position
            ).startPictureLoad()
        }
    }


    companion object {
        var imageLoader: Int = 0
    }
}