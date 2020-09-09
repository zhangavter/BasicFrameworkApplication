package zhang.acfun.com.basicframeworklib.network.download;

public   interface ProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}
