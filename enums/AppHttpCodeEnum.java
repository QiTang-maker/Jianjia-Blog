package com.dyj.enums;

public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200, "操作成功"),
    // 登录
    NEED_LOGIN(401, "需要登录后操作"),
    NO_OPERATOR_AUTH(403, "无权限操作"),
    SYSTEM_ERROR(500, "出现错误"),
    USERNAME_EXIST(501, "用户名已存在"),
    PHONENUMBER_EXIST(502, "手机号已存在"),
    EMAIL_EXIST(503, "邮箱已存在"),
    NICKNAME_EXIST(512, "昵称已存在"),
    REQUIRE_USERNAME(504, "必需填写用户名"),
    LOGIN_ERROR(505, "用户名或密码错误"),
    CONTEXT_NULL(506, "评论内容为空"),
    FILE_TYPE_ERROR(507, "文件类型错误，请上传png或jpg文件"),
    USERNAME_NULL(508, "用户名为空"),
    NICKNAME_NULL(509, "昵称为空"),
    EMAIL_NULL(510, "邮箱为空"),
    PASSWORD_NULL(511, "密码为空"),
    TAG_NAME_NULL(513, "标签名为空"),
    SAME_PARENT_MENU(500, "修改菜单失败，上级菜单不能选择自己"),
    CHILDREN_MENU_EXIT(500, "存在子菜单，不允许删除");
    int code;
    String msg;

    AppHttpCodeEnum(int code, String errorMessage) {
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}