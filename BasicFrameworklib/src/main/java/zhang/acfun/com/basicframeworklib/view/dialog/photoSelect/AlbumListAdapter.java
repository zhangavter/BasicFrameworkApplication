package zhang.acfun.com.basicframeworklib.view.dialog.photoSelect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import zhang.acfun.com.basicframeworklib.R;
import zhang.acfun.com.basicframeworklib.base.BaseRecycleAdapter;
import zhang.acfun.com.basicframeworklib.model.AlbumModel;
import zhang.acfun.com.basicframeworklib.util.FileUtil;
import zhang.acfun.com.basicframeworklib.util.ImageLoader;
import zhang.acfun.com.basicframeworklib.util.ScreenUtils;
import zhang.acfun.com.basicframeworklib.view.PercentImageView;


/**
 * 时间：2018/11/14
 * 作者：zxb
 */
public class AlbumListAdapter extends BaseRecycleAdapter<AlbumModel> {


    private int imageSize;
    private OnItemClickListener onItemClickListener;

    public AlbumListAdapter(Context context) {
        super(context);
        imageSize = ScreenUtils.getScreenWidth(context) / 4;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder_impl(ViewGroup viewGroup, int viewType) {
        return new ContentViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_album_list, viewGroup, false));
    }

    @Override
    protected void onBindViewHolder_impl(RecyclerView.ViewHolder viewHolder, int itemType, int original_position, int real_position) {

        ContentViewHolder holder = (ContentViewHolder) viewHolder;

        AlbumModel model = mList.get(real_position);

        if (FileUtil.INSTANCE.isAndroidQorAbove())
            ImageLoader.loadImageByUri(holder.ivAlbum, model.getFileUri(), imageSize, imageSize, false);
        else
            ImageLoader.loadImageByPath(holder.ivAlbum, model.getRecent(), imageSize, imageSize);

        setText(holder.tvName, model.getName());
        setText(holder.tvCount, " · " + model.getCount());

        holder.itemView.setOnClickListener(view -> {

            if (onItemClickListener != null)
                onItemClickListener.OnItemClick(real_position, model);

        });
    }

    class ContentViewHolder extends RecyclerView.ViewHolder {

        private PercentImageView ivAlbum;
        private TextView tvName, tvCount;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAlbum = itemView.findViewById(R.id.iv_album_la);
            tvName = itemView.findViewById(R.id.tv_name_la);
            tvCount = itemView.findViewById(R.id.tv_count_album);
        }
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {

        void OnItemClick(int position, AlbumModel model);

    }
}
