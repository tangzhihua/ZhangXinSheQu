package core_lib.domainbean_model.login;

import java.io.Serializable;

/**
 * 登录
 */
public final class LoginNetRespondBean implements Serializable, Cloneable {

    /**
     *
     */
    private static final long serialVersionUID = -8276082554036156504L;
    // token
    private String token;
    //
    private String userId;

    @Override
    public LoginNetRespondBean clone() {
        LoginNetRespondBean cloneObject = null;
        try {
            cloneObject = (LoginNetRespondBean) super.clone();
            cloneObject.token = this.token;
            cloneObject.userId = this.userId;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e); // won't happen
        }
        return cloneObject;
    }

    public String getToken() {
        return token;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "LoginNetRespondBean{" +
                "token='" + token + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
