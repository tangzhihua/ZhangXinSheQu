package core_lib.http_engine.async_http_client;

import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;

import core_lib.engine_helper.EngineHelperSingleton;
import core_lib.global_data_cache.ApplicationSingleton;
import core_lib.global_data_cache.GlobalDataCacheForMemorySingleton;
import core_lib.simple_network_engine.engine_helper.interfaces.IPostDataPackage;
import core_lib.simple_network_engine.net_layer.INetLayerInterface;
import core_lib.simple_network_engine.net_layer.INetRequestHandle;
import core_lib.simple_network_engine.net_layer.domainbean.IDomainBeanRequestAsyncHttpResponseListener;
import core_lib.simple_network_engine.net_layer.file.IFileRequestAsyncHttpResponseListener;
import core_lib.toolutils.DebugLog;
import core_lib.toolutils.ToolsFunctionForThisProgect;

public class HttpEngineOfAsyncHttpClient implements INetLayerInterface {
    private final String TAG = this.getClass().getSimpleName();
    public final AsyncHttpClient asyncHttpClient;

    public HttpEngineOfAsyncHttpClient() {
        asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        asyncHttpClient.setUserAgent(ToolsFunctionForThisProgect.getUserAgent());

        // PersistentCookieStore类用于实现Apache HttpClient的CookieStore接口，
        // 可以自动的将cookie保存到Android设备的SharedPreferences中，如果你打算使用cookie来管理验证会话，
        // 这个非常有用，因为用户可以保持登录状态，不管关闭还是重新打开你的app
        PersistentCookieStore cookieStore = new PersistentCookieStore(ApplicationSingleton.getInstance.getApplication());
        asyncHttpClient.setCookieStore(cookieStore);
        //GlobalDataCacheForMemorySingleton.getInstance.setCookieStore(cookieStore);

        // The following exceptions will be whitelisted, i.e.: When an exception
        // of this type is raised, the request will be retried.
        AsyncHttpClient.allowRetryExceptionClass(IOException.class);
        AsyncHttpClient.allowRetryExceptionClass(SocketTimeoutException.class);
        AsyncHttpClient.allowRetryExceptionClass(ConnectTimeoutException.class);

        // The following exceptions will be blacklisted, i.e.: When an exception
        // of this type is raised, the request will not be retried and it will
        // fail immediately.
        AsyncHttpClient.blockRetryExceptionClass(UnknownHostException.class);
        AsyncHttpClient.blockRetryExceptionClass(ConnectionPoolTimeoutException.class);
    }

    @Override
    public INetRequestHandle requestDomainBean(final String urlString,
                                               final String httpMethod,
                                               final Map<String, String> httpHeaders,
                                               final Map<String, String> httpParams,
                                               final IPostDataPackage customPostDataPackageHandler,
                                               final IDomainBeanRequestAsyncHttpResponseListener domainBeanRequestAsyncHttpResponseListener) {
        // DebugLog.e(TAG, "短连接 : sendHttpRequest->url=" + url + ",requestMethod=" +
        // requestMethod
        // + ",requestParamsDictionary=" + requestParamsDictionary.toString());

        RequestHandle requestHandle = null;

        // 定义异步HTTP网络响应处理监听器
        final AsyncHttpResponseHandler responseHandler = new HttpRequestHandleOfAsyncHttpClient() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                domainBeanRequestAsyncHttpResponseListener.onSuccess(this, response);

                this.setFinished(true);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                domainBeanRequestAsyncHttpResponseListener.onFailure(this, statusCode, e);

                this.setFinished(true);
            }
        };

        // 将业务数据字典, 打包成网络请求报文的实体数据, (可以在这里完成数据的加密工作)
        final RequestParams requestParams = EngineHelperSingleton.getInstance.postDataPackageFunction().packageNetRequestParams(httpMethod, httpParams);
        if ("POST".equalsIgnoreCase(httpMethod)) {
            requestHandle = asyncHttpClient.post(urlString, requestParams, responseHandler);
            DebugLog.e(TAG, "POST请求 : " + urlString + "?" + requestParams.toString());
        } else {
            requestHandle = asyncHttpClient.get(urlString, requestParams, responseHandler);
            DebugLog.e(TAG, "GET请求 : " + urlString + requestParams.toString());
        }

        // 定义 AsyncHttpClient 定制的 HttpRequestHandle
        HttpRequestHandleOfAsyncHttpClient httpRequestHandleOfAsyncHttpClient = (HttpRequestHandleOfAsyncHttpClient) responseHandler;
        httpRequestHandleOfAsyncHttpClient.setRequestHandle(requestHandle);
        httpRequestHandleOfAsyncHttpClient.setFinished(false);
        return httpRequestHandleOfAsyncHttpClient;
    }

    @Override
    public INetRequestHandle requestDownloadFile(final String url,
                                                 final boolean isNeedContinuingly,
                                                 final String requestMethod,
                                                 final Map<String, String> params,
                                                 final File downLoadFile,
                                                 final IFileRequestAsyncHttpResponseListener fileRequestAsyncHttpResponseListener) {
        final RequestParams requestParams = new RequestParams(params);
        DebugLog.i(TAG, "文件请求 : sendHttpRequest->url=" + url + ",requestMethod=" + requestMethod + ",httpParams=" + requestParams.toString());

        // effective for java 38 检查参数有效性, 对于共有的方法,
        // 要使用异常机制来通知调用方发生了入参错误.
        if (TextUtils.isEmpty(url)) {
            throw new NullPointerException("入参为空.");
        }
        RequestHandle requestHandle = null;

        long seekPos = 0;
        asyncHttpClient.removeHeader("Range");
        if (isNeedContinuingly) {
            // 需要断点续传
            if (downLoadFile.exists()) {
                asyncHttpClient.addHeader("Range", "bytes=" + downLoadFile.length() + "-");
                seekPos = downLoadFile.length();
            }
        } else {
            // 不需要断点续传时, 要删除之前的临时文件, 好重头进行下载
            seekPos = 0;
        }

        try {
            final RandomAccessFileAsyncHttpResponseHandler fileAsyncHttpResponseHandler = new RandomAccessFileAsyncHttpResponseHandler(
                    downLoadFile, seekPos) {

                @Override
                public void onSuccess(int statusCode, Header[] headers, File file) {
                    DebugLog.i(TAG, "headers=" + headers);
                    fileRequestAsyncHttpResponseListener.onSuccess(null);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                    fileRequestAsyncHttpResponseListener.onFailure(statusCode, throwable);
                }

                @Override
                public void onProgress(long bytesWritten, long totalSize) {
                    fileRequestAsyncHttpResponseListener.onProgress(bytesWritten, totalSize);
                }


            };

            // 将业务数据字典, 打包成网络请求报文的实体数据, (可以在这里完成数据的加密工作)
            if ("POST".equalsIgnoreCase(requestMethod)) {
                requestHandle = asyncHttpClient.post(url, requestParams, fileAsyncHttpResponseHandler);
            } else {
                requestHandle = asyncHttpClient.get(url, requestParams, fileAsyncHttpResponseHandler);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new HttpRequestHandleOfAsyncHttpClient(requestHandle);
    }

    @Override
    public String getLocalizedFailureDescriptionForDomainBean(int statusCode, Throwable e) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getLocalizedFailureDescriptionForFile(int statusCode, Throwable e) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public INetRequestHandle requestUploadFile(final String urlString,
                                               final Map<String, String> params,
                                               final String uploadFileKey,
                                               final File uploadFile,
                                               final IFileRequestAsyncHttpResponseListener fileRequestAsyncHttpResponseListener) {

        // 封装文件上传的参数
        RequestParams requestParams = new RequestParams(params);

        RequestHandle requestHandle = null;
        try {

            // 添加文件数据
            requestParams.put(uploadFileKey, uploadFile);

            final AsyncHttpResponseHandler fileAsyncHttpResponseHandler = new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String responseBodyString = new String(responseBody).toString();
                    DebugLog.e(TAG, "statusCode=" + statusCode + ", header=" + headers.toString() + ", responseBody=" + responseBodyString);
                    fileRequestAsyncHttpResponseListener.onSuccess(responseBody);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    DebugLog.e(TAG, "statusCode=" + statusCode + ", header=" + headers.toString() + ", responseBody=" + new String(responseBody).toString() + ",error=" + error.toString());
                    fileRequestAsyncHttpResponseListener.onFailure(statusCode, error);
                }

                @Override
                public void onProgress(long bytesWritten, long totalSize) {
                    DebugLog.e(TAG, "bytesWritten=" + bytesWritten + ", totalSize=" + totalSize);
                    fileRequestAsyncHttpResponseListener.onProgress(bytesWritten, totalSize);
                }
            };

            requestHandle = asyncHttpClient.post(urlString, requestParams, fileAsyncHttpResponseHandler);
        } catch (Exception e) {

        }

        return new HttpRequestHandleOfAsyncHttpClient(requestHandle);
    }
}
