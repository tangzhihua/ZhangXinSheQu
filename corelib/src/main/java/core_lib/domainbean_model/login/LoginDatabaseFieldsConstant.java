package core_lib.domainbean_model.login;

/**
 * 登录接口
 *
 * @author skyduck
 */
final class LoginDatabaseFieldsConstant {
    private LoginDatabaseFieldsConstant() {

    }

    public static enum RequestBean {
        //
        username,
        //
        password

    }

    public static enum RespondBean {
        //
        token,
        //
        userId
    }
}
