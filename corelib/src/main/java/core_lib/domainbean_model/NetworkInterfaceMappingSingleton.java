package core_lib.domainbean_model;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

import core_lib.simple_network_engine.domain_layer.IDomainBeanHelper;

import static com.google.common.collect.Maps.newHashMap;

/**
 * 所有业务接口, 要在这里完成NetRequestBean和DomainBeanToolsFactory的映射
 *
 * @author zhihua.tang
 */

public enum NetworkInterfaceMappingSingleton {
    getInstance;

    private Map<Class<?>, IDomainBeanHelper<?, ?>> networkInterfaceMapping = newHashMap();

    public ImmutableMap<Class<?>, IDomainBeanHelper<?, ?>> getNetworkInterfaceMapping() {
        return ImmutableMap.copyOf(networkInterfaceMapping);
    }

    // 注意：
    // Key : 网络请求业务Bean
    // Value : 是该网络接口对应的抽象工厂类
    {


    }
}
