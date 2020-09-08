package zhang.acfun.com.basicframeworklib.view.dialog.photoSelect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import zhang.acfun.com.basicframeworklib.R;
import zhang.acfun.com.basicframeworklib.base.BaseRecycleAdapter;
import zhang.acfun.com.basicframeworklib.model.PhotoModel;
import zhang.acfun.com.basicframeworklib.util.ScreenUtils;
import zhang.acfun.com.basicframeworklib.view.dialog.PhotoSelectorDialog;

/**
 * 时间：2018/11/13
 * 作者：zxb
 */
public class PhotoAdapter extends BaseRecycleAdapter<PhotoModel> {

    public static final int REQUEST_CAMERA = 121;
    public static String photo_camera;

    public boolean isShowCheckbox = true;//是否显示选中的框框
    private boolean isGifAutoPlay = true;
    private int itemWidth;
    private PhotoItem.onPhotoItemCheckedListener listener;
    private  PhotoItem.onPhotoItemClickListener  clicklistener;
    private Map<Integer, PhotoItem> viewMap;
//    private boolean isShowCamera = false;//是否显示相机

    public PhotoAdapter(Context context) {
        super(context);

        itemWidth = ScreenUtils.getScreenWidth(context) / 5;
        viewMap = new HashMap<>();
    }


    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder_impl(ViewGroup parent, int viewType) {

        if (viewType == ITEM_TYPE_HEADER) {
            return new HeaderViewHolder(HeaderView);
        } else if (viewType == ITEM_TYPE_NORMAL) {
            return new ContentViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_photo_selector, parent, false));
        }
        return null;
    }

    @Override
    protected void onBindViewHolder_impl(RecyclerView.ViewHolder viewHolder, int itemType, int original_position, int real_position) {

        if (itemType == ITEM_TYPE_HEADER) {
            //头部
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) viewHolder;

            headerViewHolder.photoItem.setCameraStyle();
            headerViewHolder.photoItem.setOnClickListener(view -> {
               // DoPicUtils.INSTANCE.openCamera(ctx, REQUEST_CAMERA);
            });


        } else if (itemType == ITEM_TYPE_NORMAL) {
            ContentViewHolder contentViewHolder = (ContentViewHolder) viewHolder;
            final PhotoItem item = contentViewHolder.photoItem;
            PhotoModel model = getNewPhotoModel(mList.get(real_position));

            item.setShowCheckBox(isShowCheckbox);
            item.setData(model, itemWidth, isGifAutoPlay);
            item.setCheck(model.isCheck());
            item.setOnPhotoItemCheckedListener(listener);

            viewMap.put(real_position, item);

            item.setOnClickListener(view -> {
                if(clicklistener!=null){
                    clicklistener.onClick(model,real_position);
                }
            });

        }
    }

    /**
     * 对比PhotoModel，判断当前model是否在选中的map里面，是的话就用map里面的替换
     */
    private PhotoModel getNewPhotoModel(PhotoModel model) {
        LinkedHashMap<String, PhotoModel> mCheckPhoto = PhotoSelectorDialog.mCheckPhoto;
        if (mCheckPhoto != null) {
            PhotoModel model_value = mCheckPhoto.get(model.getOriginalPath());
            if (model_value != null) {
                model_value.setCheck(true);
                return model_value;
            } else {
                model.setCheck(false);
            }
        }

        return model;
    }

    public void setIsGifAutoPlay(boolean autoPlay) {
        isGifAutoPlay = autoPlay;
    }


//    public void setShowCamera(boolean showCamera) {
//        isShowCamera = showCamera;
//    }

    @Override
    public void setList(List<PhotoModel> list) {
        if (viewMap != null)
            viewMap.clear();
        super.setList(list);
    }

    /**
     * 刷新时，右上角的选框状态，文案等调整
     */
    public void notifyCheckBoxChanged() {

        for (Map.Entry<Integer, PhotoItem> entry : viewMap.entrySet()) {
            PhotoItem item = entry.getValue();
            PhotoModel photoModel = getNewPhotoModel(item.getPhotoModel());
            item.setCheck(photoModel.isCheck());
            item.notifyCheckNumChanged(photoModel);
        }

    }


    public void setOnPhotoItemCheckedListener(PhotoItem.onPhotoItemCheckedListener listener) {
        this.listener = listener;
    }

    public void setOnPhotoItemClickListener(PhotoItem.onPhotoItemClickListener listener) {
        this.clicklistener = listener;
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        private PhotoItem photoItem;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            photoItem = itemView.findViewById(R.id.photo_item);
        }
    }

    class ContentViewHolder extends RecyclerView.ViewHolder {

        private PhotoItem photoItem;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            photoItem = itemView.findViewById(R.id.photo_item);
        }
    }


}
