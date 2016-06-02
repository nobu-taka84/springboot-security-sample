package com.springboot.security.constants;

public enum UserInfoErrorType {

    /** ユーザIDまたはパスワードが不正 */
    ID_OR_PASSWORD_INVALID,

    /** ユーザがすでに存在する */
    USER_ALREADY_EXISTS,

    /** ユーザが存在しない */
    USER_NOT_EXISTS,

    /** ユーザー名が短すぎる */
    USER_NAME_TOO_SHORT,

    /** ユーザー名が長すぎる */
    USER_NAME_TOO_LONG,

    /** パスワードが短すぎる */
    PASSWORD_TOO_SHORT,

    /** パスワードが長すぎる */
    PASSWORD_TOO_LONG,

    /** パスワードが複雑でない */
    PASSWORD_UNCOMPLICATED,

    /** アカウントロック */
    ACCOUNT_LOCKED,

    /** パスワード有効期限切れ */
    PASSWORD_EXPIRED;
}
