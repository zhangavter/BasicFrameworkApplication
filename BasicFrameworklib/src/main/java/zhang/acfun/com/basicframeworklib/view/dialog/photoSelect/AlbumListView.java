package zhang.acfun.com.basicframeworklib.view.dialog.photoSelect;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import zhang.acfun.com.basicframeworklib.R;
import zhang.acfun.com.basicframeworklib.model.AlbumModel;


/**
 * 时间：2018/11/13
 * 作者：CDY
 */
public class AlbumListView extends LinearLayout {


    private RecyclerView rv_album_list;
    private AlbumListAdapter adapter;

    public AlbumListView(Context context) {
        super(context);
        init(context);
    }

    public AlbumListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AlbumListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_album_list, this);

        rv_album_list = findViewById(R.id.rv_album_list);

        LinearLayoutManager manager = new LinearLayoutManager(context);
        rv_album_list.setLayoutManager(manager);

        adapter = new AlbumListAdapter(context);
        rv_album_list.setAdapter(adapter);
    }

    public void setData(List<AlbumModel> beans) {
        adapter.setList(beans);
    }


    public void smoothScrollToPosition(int position) {
        rv_album_list.smoothScrollToPosition(position);
    }

    public void setOnItemClickListener(AlbumListAdapter.OnItemClickListener onItemClickListener) {
        adapter.setOnItemClickListener(onItemClickListener);
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == VISIBLE && rv_album_list != null) {
            rv_album_list.smoothScrollToPosition(0);
        }
        super.setVisibility(visibility);
    }
}
