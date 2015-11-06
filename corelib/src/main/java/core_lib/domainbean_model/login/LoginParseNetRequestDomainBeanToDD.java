package core_lib.domainbean_model.login;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import core_lib.simple_network_engine.domain_layer.interfaces.IParseNetRequestDomainBeanToDataDictionary;
import core_lib.toolutils.SimpleMD5Tools;

final class LoginParseNetRequestDomainBeanToDD implements
        IParseNetRequestDomainBeanToDataDictionary<LoginNetRequestBean> {

    @Override
    public Map<String, String> parseNetRequestBeanToDataDictionary(
            final LoginNetRequestBean netRequestDomainBean) throws Exception {
        final String username = netRequestDomainBean.getUsername();
        final String password = netRequestDomainBean.getPassword();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            throw new Exception("用户名/密码不能为空!");
        }
        final Map<String, String> params = new HashMap<String, String>();
        params.put(LoginDatabaseFieldsConstant.RequestBean.username.name(), username);
        /* 注意 : 传递到服务器的密码要经过MD5处理 */
        /**
         * MD5简介
         * Message Digest Algorithm MD5（中文名为消息摘要算法第五版）
         * 为计算机安全领域广泛使用的一种散列函数，用以提供消息的完整性保护。
         *
         * MD5即Message-Digest Algorithm 5（信息-摘要算法5），
         * 用于确保信息传输完整一致。是计算机广泛使用的杂凑算法之一（又译摘要算法、哈希算法）
         *
         *
         * MD5应用
         * 1.一致性验证 : 典型应用是对一段信息（Message）产生信息摘要（Message-Digest），以防止被篡改。
         * 2.数字签名 : MD5的典型应用是对一段Message(字节串)产生fingerprint(指纹），以防止被“篡改”。
         * 3.安全访问认证 : MD5还广泛用于操作系统的登陆认证上，
         *      如Unix、各类BSD系统登录密码、数字签名等诸多方面。
         *      如在Unix系统中用户的密码是以MD5（或其它类似的算法）经Hash运算后存储在文件系统中。
         *      当用户登录的时候，系统把用户输入的密码进行MD5 Hash运算，然后再去和保存在文件系统中的MD5值进行比较，
         *      进而确定输入的密码是否正确。通过这样的步骤，系统在并不知道用户密码的明码的情况下就可以确定用户登
         *      录系统的合法性。这可以避免用户的密码被具有系统管理员权限的用户知道。
         */
        params.put(LoginDatabaseFieldsConstant.RequestBean.password.name(), SimpleMD5Tools.getMd5(password));
        return params;
    }
}
