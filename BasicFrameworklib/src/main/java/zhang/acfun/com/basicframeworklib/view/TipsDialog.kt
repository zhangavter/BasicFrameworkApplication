package zhang.acfun.com.basicframeworklib.view

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.TextView
import zhang.acfun.com.basicframeworklib.R
import zhang.acfun.com.basicframeworklib.view.dialog.DialogSelectListener


/**
 * 默认提示样式
 * zxb
 ***/
class TipsDialog : Dialog, View.OnClickListener, DialogInterface.OnDismissListener {
    private var btnOk: TextView? = null
    private var btnCancle: TextView? = null
    private var description: TextView? = null
    private var dailogTitle: TextView? = null
    private var root: RelativeLayout? = null
    private var mListeners: DialogSelectListener? = null


    constructor(context: Context) : super(context, R.style.CustomProgressDialog) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(
        context,
        R.style.DialogTheme
    ) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        R.style.CustomProgressDialog
    ) {
        init(context)
    }

    private fun init(context: Context) {
        val view = View.inflate(context, R.layout.dialog_default, null)
        btnOk = view.findViewById(R.id.btn_ok)
        btnCancle = view.findViewById(R.id.btn_cancle)
        description = view.findViewById(R.id.content)
        dailogTitle = view.findViewById(R.id.dailogTitle)
        root = view.findViewById(R.id.root)

        btnOk!!.setOnClickListener(this)
        btnCancle!!.setOnClickListener(this)

        val localLayoutParams = window?.attributes
        localLayoutParams?.height = WindowManager.LayoutParams.WRAP_CONTENT
        localLayoutParams?.width = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = localLayoutParams

        setContentView(view)

        setCancelOnTouchOutside(false)
        setOnDismissListener(this)
    }


    override fun onClick(v: View?) {
        if (mListeners != null && v != null) mListeners!!.onChlidViewClick(v)
        dismiss()
    }

    override fun onDismiss(p0: DialogInterface?) {

    }

    fun setBtnOk(btnOkStr: String?): TipsDialog {
        if (!TextUtils.isEmpty(btnOkStr)) {
            btnOk?.text = btnOkStr
        }
        return this
    }


    fun setBtnCancle(btnCancleStr: String?): TipsDialog {
        if (!TextUtils.isEmpty(btnCancleStr)) {
            btnCancle?.text = btnCancleStr
        }
        return this
    }


    fun setDialogTitle(dialogTitleStr: String?): TipsDialog {
        if (!TextUtils.isEmpty(dialogTitleStr)) {
            dailogTitle?.visibility = View.VISIBLE
            dailogTitle?.text = dialogTitleStr
        }
        return this
    }

    fun setDescriptionText(dialogTitleStr: String?): TipsDialog {
        if (!TextUtils.isEmpty(dialogTitleStr)) {
            description?.visibility = View.VISIBLE
            description?.text = dialogTitleStr
        }
        return this
    }

    fun setBtnOkTextColor(btnOkTextColor: Int): TipsDialog {
        if (btnOkTextColor != -1) {
            btnOk?.setTextColor(btnOkTextColor)
        }
        return this
    }

    fun setDialogListener(paramListener: DialogSelectListener): TipsDialog {
        this.mListeners = paramListener
        return this
    }

    fun HideCancleBtn(): TipsDialog {
        btnCancle?.visibility = View.GONE
        return this
    }

    fun HideTitleBtn(): TipsDialog {
        dailogTitle?.visibility = View.GONE
        return this
    }

    fun setCancelOnTouchOutside(cancel: Boolean) {
        setCanceledOnTouchOutside(cancel)
        setCancelable(cancel)
    }

}