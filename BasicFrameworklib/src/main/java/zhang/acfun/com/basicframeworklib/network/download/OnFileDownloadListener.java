package zhang.acfun.com.basicframeworklib.network.download;

public interface OnFileDownloadListener {


    void onResponse(String result);

    void onFailure(String errMsg);

    void onProgress(int progress);
}
