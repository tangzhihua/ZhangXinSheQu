package core_lib.engine_helper.project;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.Map;

import core_lib.global_data_cache.LoginManageSingleton;
import core_lib.simple_network_engine.engine_helper.interfaces.INetRequestPublicParams;
import core_lib.toolutils.SimpleUniqueIdentifierSingleton;

public class NetRequestPublicParams implements INetRequestPublicParams {

    @Override
    public ImmutableMap<String, String> publicParams() {
        Map<String, String> params = Maps.newHashMap();
        params.put("sysVersion", android.os.Build.VERSION.RELEASE);
        params.put("deviceId", SimpleUniqueIdentifierSingleton.getInstance.getDeviceUniqueIdentifierString());
        params.put("deviceType", "Android");
        params.put("deviceModel", android.os.Build.MODEL);
        if (LoginManageSingleton.getInstance.getLatestLoginNetRespondBean() != null) {
            params.put("userId", LoginManageSingleton.getInstance.getLatestLoginNetRespondBean().getUserId());
            params.put("token", LoginManageSingleton.getInstance.getLatestLoginNetRespondBean().getToken());
        }
        return ImmutableMap.copyOf(params);
    }
}
