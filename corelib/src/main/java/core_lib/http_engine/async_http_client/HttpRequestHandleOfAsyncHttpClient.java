package core_lib.http_engine.async_http_client;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import core_lib.simple_network_engine.net_layer.INetRequestHandle;
import core_lib.simple_network_engine.net_layer.INetRequestIsCancelled;

public class HttpRequestHandleOfAsyncHttpClient extends AsyncHttpResponseHandler implements
    INetRequestHandle, INetRequestIsCancelled {
  /**
   * 目前发现 AsyncHttpClient 的 isFinished() 方法有时是无效的, 也就是说有时候发现才将请求入队,
   * requestHandle.isFinished() 就立刻为true 所以目前先使用我们自己定义的标志位 isFinished
   */
  private boolean isFinished = false;

  public boolean isFinished() {
    return isFinished;
  }

  public void setFinished(boolean isFinished) {
    this.isFinished = isFinished;
  }

  public HttpRequestHandleOfAsyncHttpClient() {
  }

  public HttpRequestHandleOfAsyncHttpClient(RequestHandle requestHandle) {
    this.requestHandle = requestHandle;
  }

  private RequestHandle requestHandle;

  public void setRequestHandle(RequestHandle requestHandle) {
    this.requestHandle = requestHandle;
  }

  @Override
  public boolean isIdle() {
    // TODO : 发现 AsyncHttpClient 的 isFinished() 方法有时是无效的, 也就是说有时候发现才将请求入队,
    // requestHandle.isFinished() 就立刻为true
    // return requestHandle.isFinished() || requestHandle.isCancelled();
    return this.isFinished || requestHandle.isCancelled();
  }

  @Override
  public void cancel() {
    requestHandle.cancel(true);
  }

  @Override
  public boolean isCancelled() {
    return requestHandle.isCancelled();
  }

  @Override
  public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
    // TODO Auto-generated method stub

  }
}
