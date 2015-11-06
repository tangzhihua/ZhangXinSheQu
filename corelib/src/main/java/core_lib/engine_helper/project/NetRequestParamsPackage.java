package core_lib.engine_helper.project;

import java.util.Map;

import com.loopj.android.http.RequestParams;

import core_lib.simple_network_engine.engine_helper.interfaces.IPostDataPackage;

public class NetRequestParamsPackage implements IPostDataPackage {

  @SuppressWarnings("unchecked")
  @Override
  public <T> T packageNetRequestParams(String httpRequestMethod,
      Map<String, String> requestParamsDictionary) {
    do {
      if (requestParamsDictionary == null || requestParamsDictionary.size() <= 0) {
        break;
      }

      RequestParams requestParams = new RequestParams(requestParamsDictionary);
      return (T) requestParams;

    } while (false);

    // 不要对外返回空指针
    return (T) new RequestParams();
  }

}
