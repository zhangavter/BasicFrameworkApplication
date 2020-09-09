package zhang.acfun.com.basicframeworklib.network;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;
import zhang.acfun.com.basicframeworklib.Constants;
import zhang.acfun.com.basicframeworklib.R;
import zhang.acfun.com.basicframeworklib.enummodel.EErrorCode;
import zhang.acfun.com.basicframeworklib.model.ResultModel;
import zhang.acfun.com.basicframeworklib.util.NetworkUtils;
import zhang.acfun.com.basicframeworklib.util.SecurityUtil;

public class ApiHelper {

    /**
     * get请求访问接口
     * OO
     *
     * @param url      接口相对路径
     * @param params   参数
     * @param callBack 回调接口
     */
    public static void get(Context ctx, String url, ApiParams params, ApiCallBack callBack) {
        if (dealNetWorkInfo(ctx, callBack)) return;

        if (params == null)
            params = new ApiParams();
        HttpUtil.get(ctx, Constants.SERVER + url + params.getParams(!url.endsWith("&") && !url.endsWith("?")), new MyHttpCallBack(ctx, callBack));
    }

    /**
     * 访问接口
     *
     * @param url      接口相对路径
     * @param params   参数
     * @param callBack 回调接口
     */
    public static void post(Context ctx, String url, ApiParams params, ApiCallBack callBack) {
        post(ctx, url, params, true, callBack);
    }

    /**
     * 没有网络 ，手机是否开启了vpn,如果是网络请求失败
     *
     * @param callBack
     */
    private static boolean dealNetWorkInfo(Context ctx, final ApiCallBack callBack) {

        if (!NetworkUtils.isNetworkAvailable()) {
            Result result = new Result(ResultCode.NETWORK_TROBLE, ctx.getString(R.string.error_network_unavailable));
            if (callBack != null) {
                callBack.receive(result);
            }
            return true;
        }

        if (SecurityUtil.dealNetSecurityCheck()) {
            Result result = new Result(ResultCode.NETWORK_TROBLE, "网络异常");
            if (callBack != null) {
                callBack.receive(result);
            }
            return true;
        }

        return false;
    }


    /**
     * post 方法请求 访问接口
     *
     * @param ctx
     * @param url       接口相对路径
     * @param params    参数
     * @param isEncrypt 是否需要加密
     * @param callBack  回调接口
     */
    public static void post(Context ctx, String url, ApiParams params, boolean isEncrypt, ApiCallBack callBack) {

        if (dealNetWorkInfo(ctx, callBack)) return;

        if (params == null)
            params = new ApiParams();

        HttpUtil.post(ctx, Constants.SERVER + url, params.getPostParams(isEncrypt), new MyHttpCallBack(ctx, callBack));
    }


    /**
     * 访问接口
     *
     * @param url      接口相对路径
     * @param params   参数
     * @param callBack 回调接口
     */
    public static void postFile(Context ctx, String url, ApiParams params, ApiCallBack callBack) {

        if (dealNetWorkInfo(ctx, callBack)) return;

        if (params == null)
            params = new ApiParams();
        HttpUtil.postFile(ctx, Constants.SERVER + url, params.getFileUploadParams(), new MyHttpCallBack(ctx, callBack));
    }

    /**
     * 统一处理回调
     */
    private static class MyHttpCallBack extends HttpCallBack {

        protected Result result;// 返回结果
        private Context ctx;
        private ApiCallBack callBack;

        public MyHttpCallBack(final Context ctx, final ApiCallBack callBack) {
            this.ctx = ctx;
            this.callBack = callBack;
        }


        /**
         * 处理返回结果的方法
         *
         * @param content
         * @param failStr
         * @return
         */
        private Result getResultByContent(String content, String failStr) {
            Result result;
            ResultModel json = JSON.parseObject(content, ResultModel.class);

            if ("1".equals(json.getState())) {
                result = new Result(ResultCode.RESULT_OK, content);
            } else {
                String msg = failStr;
                if (!TextUtils.isEmpty(json.getMsg())) {
                    msg = json.getMsg();
                }

                result = new Result(ResultCode.RESULT_FAILED, msg);

                if (EErrorCode.third_register_alert.getValue().equals(json.getState())) {
                    result.setCode(ResultCode.THIRD_REGISTER_ALERT);
                } else if (EErrorCode.access_to_many.getValue().equals(json.getState())) {
                    result.setCode(ResultCode.ACCESS_TO_MANY);
                } else if (EErrorCode.user_not_login.getValue().equals(json.getState())) {
                    result.setCode(ResultCode.USER_NOT_LOGIN_SIGN_ERROR);
                    result.setObj("登录已过期");
                } else if (EErrorCode.user_other_login.getValue().equals(json.getState())) {
                    result.setCode(ResultCode.OTHER_LOGIN);
                    result.setObj((TextUtils.isEmpty(json.getMsg()) ? "登录失效,账号已在其他设备登录" : json.getMsg()));
                } else if (EErrorCode.ServiceErr.getValue().equals(json.getState())) {
                    //服务器维护
                    Constants.ServiceErrMsg = msg;
                    result.setCode(ResultCode.ServiceErr);
                    result.setObj(msg);
                }
            }

            return result;
        }


        @Override
        public void OnSuccess(String resultStr, Call call, Response response) {
            try {
                result = getResultByContent(resultStr, ctx.getString(R.string.failed_reason));
            } catch (Exception e) {
                e.printStackTrace();
                if (TextUtils.isEmpty(resultStr)) {
                    resultStr = ctx.getString(R.string.failed_reason);
                }
                result = new Result(ResultCode.RESULT_FAILED, resultStr);
            }
        }

        @Override
        public void OnFailure(Call call, IOException e) {
            result = new Result(ResultCode.RESULT_FAILED, ctx.getString(R.string.error_network_unavailable));
        }

        @Override
        public void OnFinish() {
            if (callBack != null && result != null) {
               /* if (ctx instanceof Activity) {
                    Activity activity = (Activity) ctx;
                    if (activity.isFinishing()) {
                        return;
                    }
                }*/
                callBack.receive(result);
            }
        }
    }


}
