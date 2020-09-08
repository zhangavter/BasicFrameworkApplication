package zhang.acfun.com.basicframeworklib.model;

import android.net.Uri;

public class AlbumModel {

    private String name;

    private int count;

    private String recent;

    private Uri fileUri;

    private boolean isCheck;

    public AlbumModel() {
        super();
    }

    public AlbumModel(String name) {
        this.name = name;
    }

    public AlbumModel(String name, int count, String recent, Uri mUri) {
        super();
        this.name = name;
        this.count = count;
        this.recent = recent;
        this.fileUri = mUri;
    }

    public AlbumModel(String name, int count, String recent, Uri mUri, boolean isCheck) {
        super();
        this.name = name;
        this.count = count;
        this.recent = recent;
        this.fileUri = mUri;
        this.isCheck = isCheck;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getRecent() {
        return recent;
    }

    public void setRecent(String recent) {
        this.recent = recent;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public void increaseCount() {
        count++;
    }

	public Uri getFileUri() {
		return fileUri;
	}

	public void setFileUri(Uri fileUri) {
		this.fileUri = fileUri;
	}
}
