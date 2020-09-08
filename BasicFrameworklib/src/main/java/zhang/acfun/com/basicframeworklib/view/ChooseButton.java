package zhang.acfun.com.basicframeworklib.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import zhang.acfun.com.basicframeworklib.R;


/**
 * 时间：2018/11/5
 * 作者：zxb
 */
public class ChooseButton extends LinearLayout {

    private int cb_normal_img, cb_check_img, cb_check_text_color;
    private int cb_check_text_size;
    private AppCompatImageView imageView;
    private TextView checkedText;
    private boolean isCheck = false, showCheckText = false;
    private OnCheckListener onCheckListener;
    private int imageWidth = ViewGroup.LayoutParams.WRAP_CONTENT, imageHeight = ViewGroup.LayoutParams.WRAP_CONTENT;

    public ChooseButton(Context context) {
        super(context);
        init(context, null);
    }

    public ChooseButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ChooseButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ChooseButton);
            cb_normal_img = a.getResourceId(R.styleable.ChooseButton_cb_normal_img, 0);
            cb_check_img = a.getResourceId(R.styleable.ChooseButton_cb_check_img, 0);
            cb_check_text_color = a.getColor(R.styleable.ChooseButton_cb_check_text_color, ContextCompat.getColor(context, R.color.C1));
            cb_check_text_size = a.getInt(R.styleable.ChooseButton_cb_check_text_size, 20);
            imageWidth = a.getDimensionPixelOffset(R.styleable.ChooseButton_cb_img_width, ViewGroup.LayoutParams.WRAP_CONTENT);
            imageHeight = a.getDimensionPixelOffset(R.styleable.ChooseButton_cb_img_height, ViewGroup.LayoutParams.WRAP_CONTENT);
            a.recycle();
        }

//        cb_check_text_size = ViewUtil.INSTANCE.sp2px(context, cb_check_text_size);

        setGravity(Gravity.CENTER);
        initView(context);

        setCheck(false);

        setOnClickListener((View view) -> {
            if (onCheckListener != null) {
                //实现接口方，是否拦截了事件
                boolean interceptEvent = onCheckListener.OnCheck(!isCheck());
                //如果实现接口方，没有拦截事件，这里就处理
                if (!interceptEvent) {
                    setCheck(!isCheck());
                }
            }
        });
    }

    private void initView(Context context) {

        RelativeLayout relativeLayout = new RelativeLayout(context);
        addView(relativeLayout);

        imageView = new AppCompatImageView(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.width = imageWidth;
        params.height = imageHeight;
        imageView.setLayoutParams(params);
        relativeLayout.addView(imageView);


        /**添加文字**/
        checkedText = new TextView(context);
        RelativeLayout.LayoutParams params_checkedText = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params_checkedText.addRule(RelativeLayout.CENTER_VERTICAL);
        params_checkedText.addRule(RelativeLayout.CENTER_HORIZONTAL);
        checkedText.setLayoutParams(params_checkedText);
        checkedText.setTextColor(cb_check_text_color);
        checkedText.setTextSize(cb_check_text_size);
        relativeLayout.addView(checkedText);
        checkedText.setVisibility(GONE);
    }

    public void setCheck(boolean check) {
        setCheck(check, false);
    }

    /**
     * @param doListener 是否触发事件
     */
    public void setCheck(boolean check, boolean doListener) {
        isCheck = check;
        if (check) {
            imageView.setImageResource(cb_check_img);
            if (showCheckText)
                checkedText.setVisibility(VISIBLE);

        } else {
            imageView.setImageResource(cb_normal_img);
            checkedText.setVisibility(GONE);
        }
        if (doListener && onCheckListener != null) {
            onCheckListener.OnCheck(check);
        }
    }

    public boolean isCheck() {
        return isCheck;
    }


    public void setSrc(int cb_normal_img, int cb_check_img) {
        this.cb_normal_img = cb_normal_img;
        this.cb_check_img = cb_check_img;
        setCheck(false);
    }


    /**
     * 使用此方法，必须先调用showCheckText（true）打开开关
     */
    public void setCheckedText(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            text = "";
        }
        checkedText.setText(text);
    }

    /**
     * 是否需要选中的文字
     */
    public void showCheckText(boolean showCheckText) {
        this.showCheckText = showCheckText;
    }

    public void setImageSize(int imageWidth, int imageHeight) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;

        if (imageView != null) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
            params.width = imageWidth;
            params.height = imageHeight;
            imageView.setLayoutParams(params);
        }
    }

    public void setOnCheckListener(OnCheckListener onCheckListener) {
        this.onCheckListener = onCheckListener;
    }

    public interface OnCheckListener {

        boolean OnCheck(boolean check);
    }
}
