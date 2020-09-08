package zhang.acfun.com.basicframeworkapplication.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.main.*
import zhang.acfun.com.basicframeworkapplication.R
import zhang.acfun.com.basicframeworklib.model.PhotoModel
import zhang.acfun.com.basicframeworklib.util.DoPicUtils
import zhang.acfun.com.basicframeworklib.view.dialog.PhotoSelectorDialog
import zhang.acfun.com.basicframeworklib.view.dialog.photoSelect.AlbumController
import java.util.*

class MainActivity : AppCompatActivity() {
    private var photoSelectorDialog: PhotoSelectorDialog? = null
    private var callBack: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        initListener()
    }

    fun initListener() {
        test_loadimg?.setOnClickListener {
            startActivity(Intent(this@MainActivity, PreviewActivity::class.java))
        }

        ablum_select?.setOnClickListener {
            photoSelectorDialog = DoPicUtils.showMediaSelectDialog(
                this,
                AlbumController.Type.PHOTO,
                2,
                true,
                true
            ) { photos: MutableList<PhotoModel>? ->
                if (photos == null || photos.size == 0)
                    true
                else {

                    val list = ArrayList<String>()
                    for (i in 0 until photos.size) {
                        val outPath = photos[i].realPath
                        list.add(outPath)
                    }

                    callBack?.addAll(list)
                    true
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (photoSelectorDialog != null)
            photoSelectorDialog!!.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK) {
            return
        }

    }
}