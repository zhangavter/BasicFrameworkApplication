package zhang.acfun.com.basicframeworklib.storage;

import com.tencent.mmkv.MMKV;

/**
 * tencent 开源存储框架 替代 SharedPreferences
 * zxb
 **/
public class MMKVUtil {

    MMKV kv;

    private volatile static MMKVUtil mmkvUtil;

    public MMKVUtil() {
    }

    public static MMKVUtil getInstance() {
        if (mmkvUtil == null)
            synchronized (MMKVUtil.class) {
                if (mmkvUtil == null)
                    mmkvUtil = new MMKVUtil();
            }
        return mmkvUtil;
    }

    public MMKV getMMKV() {
        if (kv == null)
            kv = MMKV.defaultMMKV();
        return kv;
    }

    /**
     * 存储一个String
     **/
    public void putValue(String key, String value) {
        getMMKV().encode(key, value);
    }

    /**
     * 存储一个int
     **/
    public void putValue(String key, int value) {
        getMMKV().encode(key, value);
    }

    /**
     * 存储一个boolean
     **/
    public void putValue(String key, boolean value) {
        getMMKV().encode(key, value);
    }

    /**
     * 存储一个long
     **/
    public void putValue(String key, long value) {
        getMMKV().encode(key, value);
    }

    /**
     * 存储一个float
     **/
    public void putValue(String key, float value) {
        getMMKV().encode(key, value);
    }

    /**
     * 存储一个byte数组
     **/
    public void putValue(String key, byte[] value) {
        getMMKV().encode(key, value);
    }

    public String getStringValue(String key) {
        return getMMKV().decodeString(key);
    }

    public int getIntValue(String key) {
        return getMMKV().decodeInt(key, 0);
    }

    public boolean getBooleanValue(String key) {
        return getMMKV().decodeBool(key, false);
    }

    public long getLongValue(String key) {
        return getMMKV().decodeLong(key, 0);
    }

    public float getFloatValue(String key) {
        return getMMKV().decodeFloat(key, 0f);
    }

    public byte[] getValue(String key) {
        return getMMKV().decodeBytes(key);
    }

    /**
     * 更具key移除
     **/
    public void removeValueForKey(String key) {
        getMMKV().removeValueForKey(key);
    }

    /**
     * 根据key集合移除
     **/
    public void removeValueForKeys(String[] key) {
        getMMKV().removeValuesForKeys(key);
    }
}
