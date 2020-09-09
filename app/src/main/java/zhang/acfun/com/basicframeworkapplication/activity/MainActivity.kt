package zhang.acfun.com.basicframeworkapplication.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.main.*
import zhang.acfun.com.basicframeworkapplication.R
import zhang.acfun.com.basicframeworklib.model.PhotoModel
import zhang.acfun.com.basicframeworklib.util.DoPicUtils
import zhang.acfun.com.basicframeworklib.util.StringUtils
import zhang.acfun.com.basicframeworklib.util.ToastUtil
import zhang.acfun.com.basicframeworklib.view.dialog.PhotoSelectorDialog
import zhang.acfun.com.basicframeworklib.view.dialog.photoSelect.AlbumController
import zhang.acfun.com.basicframeworklib.widget.dataTimeSelect.CardDatePickerDialog
import zhang.acfun.com.basicframeworklib.widget.dataTimeSelect.DateTimePicker
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

        date_select?.setOnClickListener {
            val displayList = mutableListOf<Int>()

            displayList.add(DateTimePicker.YEAR)
            displayList.add(DateTimePicker.MONTH)
            displayList.add(DateTimePicker.DAY)
            // displayList.add(DateTimePicker.HOUR)
            //displayList.add(DateTimePicker.MIN)
            //model = CardDatePickerDialog.STACK
            /*  if (radioModelCustom.isChecked)
                  model = R.drawable.shape_bg_dialog_custom*/

            CardDatePickerDialog.builder(this)
                .setTitle("选择时间")
                .setDisplayType(displayList)
                .setBackGroundModel(CardDatePickerDialog.STACK)
                //.showBackNow(checkBackNow.isChecked)
                /* .setDefaultTime(defaultDate)
                 .setMaxTime(maxDate)
                 .setMinTime(minDate)*/
                // .setThemeColor(if (model == R.drawable.shape_bg_dialog_custom) Color.parseColor("#FF8000") else 0)
                .showDateLabel(true)
                .showFocusDateInfo(true)
                .setOnChoose("选择") {
                    ToastUtil.showToast("" + StringUtils.conversionTime(it, "yyyy-MM-dd HH:mm"))
                }
                .setOnCancel("关闭") {
                }.build().show()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (photoSelectorDialog != null)
            photoSelectorDialog!!.onActivityResult(requestCode, resultCode, data)

    }
}