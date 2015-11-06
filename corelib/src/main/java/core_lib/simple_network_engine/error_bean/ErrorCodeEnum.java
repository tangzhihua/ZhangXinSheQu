package core_lib.simple_network_engine.error_bean;

/**
 * 网络请求时, 错误码枚举
 *
 * @author zhihua.tang
 */
public enum ErrorCodeEnum {

    // 无效的
    NONE(-2014, "无效错误码"),

    // ------------------------------------------------------------------------------------------------------------
    // TODO:网络访问成功(也就是说从服务器获取到了正常的有效数据)
    Success(200, "请求成功"),

    // ------------------------------------------------------------------------------------------------------------
    // TODO:http错误码 (300 ~ 999)
    HTTP_Error(300, "HTTP错误"),
    //
    HTTP_NotFound(404, "找不到 — 服务器找不到给定的资源；文档不存在"),

    // ------------------------------------------------------------------------------------------------------------
    // TODO:客户端错误 (1000 ~ 1999)
    Client_Error(1000, "客户端错误"),
    //
    Client_ProgrammingError(1001, "客户端编程错误"),
    //
    Client_TimeOut(1002, "客户端设备联网超时"),
    // 客户端没有激活的网络.
    Client_isNoAvailableNetwork(1003, "联网失败, 请检查网络设置"),
    // 参数问题(参数可能为空, 或者类型错误)
    Client_InputParamsError(1004, "入参错误."),
    // INetResponseRawEntityDataUnpack接口返回的数据类型和IGetServerResponseDataValidityData接口所需要的入参类型不统一
    // 比如说 INetResponseRawEntityDataUnpack 返回的是 JSONObject类型, 那么
    // IGetServerResponseDataValidityData
    // 接口的入参也必须是 JSONObject 类型, 否则就出错了
    Client_NetResponseDataTypeDifferent(1005, "客户端定义的网络响应数据类型不统一."),
    // 创建网络响应业务Bean失败.
    Client_CreateNetRespondBeanFailed(1006, "创建网络响应业务Bean失败."),

    // ------------------------------------------------------------------------------------------------------------
    // TODO:服务器错误 (2000 ~ 2999)
    Server_Error(2000, "服务器错误"),

    // 从服务器端获得的实体数据为空(EntityData), 这种情况有可能是正常的, 比如 退出登录 接口, 服务器就只是通知客户端访问成功,
    // 而不发送任何实体数据.
    Server_NoResponseData(2001, "从服务器端获得的实体数据为空(EntityData)"),
    // 解析服务器端返回的实体数据失败, 在netUnpackedDataOfUTF8String不为空的时候,
    // unpackNetRespondRawEntityDataToUTF8String是绝对不能为空的.
    Server_UnpackedResponseDataFailed(2002, "解析服务器端返回的实体数据失败"),
    // 客户端和服务器端, 数据交换协议不匹配
    Server_DataExchangeProtocolMismatch(2003, "客户端和服务器端, 数据交换协议不匹配(比如说XML换成了JSON"),
    // 将网络返回的数据字符串(JSON/XML)解析成业务Bean失败.
    Server_ParseNetRespondStringToDomainBeanFailed(2004,
            "将网络返回的数据字符串(JSON/XML)解析成业务Bean失败"),
    // 服务器传递给客户端的数据中, 关键字段丢失或者类型不正确.
    Server_LostCoreField(2005, "服务器传递给客户端的数据中, 关键字段丢失或者类型不正确"),
    // 服务器返回的数据中, 丢失有效数据字段
    Server_LostDataField(2006, "服务器返回的数据中, 丢失有效数据字段(data)."),
    // 服务器返回的数据中, 丢失错误码字段
    Server_LostErrorCodeField(2007, "服务器返回的数据中, 丢失错误码字段(errorCode)."),

    // ------------------------------------------------------------------------------------------------------------
    // TODO:和服务器约定好的错误码, 联网成功, 但是服务器那边发生了错误, 服务器要告知客户端错误的详细信息 (服务器返回0证明成功,
    // 错误码从1~10开始)
    Server_Custom_Error(-1, "和服务器约定好的错误码, 联网成功, 但是服务器那边发生了错误, 服务器要告知客户端错误的详细信息"),

    // 这是为了解决, 同一个账号在不同设备上登录所照成的数据不同步问题而设计的
    kErrorCodeEnum_Server_Custom_Error_ReportIsDeleted(10, "报告已经被删除.");

    private final int code;

    public int getCode() {
        return code;
    }

    private final String message;

    public String getMessage() {
        return message;
    }

    private ErrorCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ErrorCodeEnum valueOfCode(int code) {
        for (ErrorCodeEnum item : ErrorCodeEnum.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }

        if (code > HTTP_Error.getCode() && code < Client_Error.getCode()) {
            return HTTP_Error;
        } else {
            return NONE;
        }
    }

    /**
     * 检测是否是网络错误
     *
     * @param code 错误代码
     * @return
     */
    public static boolean isNetError(int code) {
        ErrorCodeEnum codeEnum = valueOfCode(code);
        return codeEnum == HTTP_Error
                || codeEnum == Client_isNoAvailableNetwork
                || codeEnum == Client_TimeOut;
    }
}
