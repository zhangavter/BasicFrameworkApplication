
package zhang.acfun.com.basicframeworklib.network;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import zhang.acfun.com.basicframeworklib.Constants;
import zhang.acfun.com.basicframeworklib.util.Utils;


/**
 * 接口参数
 */
public class ApiParams extends HashMap<String, Object> {


    public ApiParams() {

    }

    /**
     * 公共参数
     */
    private void setPublicParams() {
        put("terminal", "android");
        put("sysVersion", android.os.Build.VERSION.RELEASE);
        put("appVersion", Utils.INSTANCE.getVersionStr(Constants.getContext()));
        put("uuid", Utils.INSTANCE.uuid());
        put("deviceName", android.os.Build.MODEL);
    }


    /**
     * @return
     */
    private Iterator<Entry<String, Object>> getEntry() {
        return this.entrySet().iterator();
    }

    /**
     * 判断字符串中是否有中文
     *
     * @param str
     * @return
     */
    private boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 获取get参数
     *
     * @return
     */
    public String getParams(boolean why) {
        setPublicParams();
        StringBuffer sp = new StringBuffer(why ? "?" : "");
        Iterator<Entry<String, Object>> iter = getEntry();
        while (iter.hasNext()) {
            Entry<String, Object> entry = iter.next();
            String key = entry.getKey();
            Object val = entry.getValue();

            if (val != null && val instanceof String && isContainChinese((String) val)) {
                val = URLEncoder.encode((String) val);
            }
            sp.append(key).append("=").append(val).append("&");
        }
        // 删除最后一个&
        if (sp.length() > (why ? 2 : 1))
            sp.deleteCharAt(sp.length() - 1);

        return sp.toString();
    }


    /**
     * @param isEncrypt 是否需要加密
     * @return
     */
    public FormBody.Builder getPostParams(boolean isEncrypt) {
        FormBody.Builder formBuilder = new FormBody.Builder();

        if (isEncrypt) {
            encryptParams();
        } else {
            setPublicParams();
        }

        Iterator<Entry<String, Object>> iter = getEntry();

        while (iter.hasNext()) {
            Entry<String, Object> entry = iter.next();
            String key = entry.getKey();
            Object val = entry.getValue();
            if (val instanceof File) {
                throw new RuntimeException("有文件，使用 postFile 方法上传文件");
            }
            formBuilder.add(key, String.valueOf(val == null ? "" : val));
        }

        return formBuilder;
    }

    /**
     * 对参数进行加密
     */
    public void encryptParams() {
        put("q_time", System.currentTimeMillis() + "");
        String jsonString = JSON.toJSONString(this);
        clear();
      /*  String p_json_dig = Security.restoreString(jsonString).replace("\n", "");*/
        put("p_json_dig", jsonString);
        //MLog.e("http:params", "加密前：" + jsonString + "  加密后：" + p_json_dig);
        setPublicParams();
    }


    /***
     * 一般是上传文件时调用
     *
     */
    public MultipartBody.Builder getFileUploadParams() {

        MultipartBody.Builder filebuilder = new MultipartBody.Builder();

        setPublicParams();

        Iterator<Entry<String, Object>> iter = getEntry();
        while (iter.hasNext()) {
            Entry<String, Object> entry = iter.next();
            String key = entry.getKey();
            Object val = entry.getValue();

            if (val != null && val instanceof File) {
                //如果是文件，就按上传文件的方式处理
                File file = (File) val;
                MediaType type = MediaType.parse("image/" + getSuffix(file));
                filebuilder.addFormDataPart(key, URLEncoder.encode(file.getName()), RequestBody.create(type, file));
            } else {
                filebuilder.addFormDataPart(key, String.valueOf(val == null ? "" : val));
            }
        }

        return filebuilder;
    }

    /**
     * 获取文件后缀名
     */
    private String getSuffix(File file) {
        String fileName = file.getName();
        int index = fileName.lastIndexOf(".");
        if (index != -1)
            return fileName.substring(index + 1);
        return "";
    }
}
