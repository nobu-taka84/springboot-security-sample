package com.springboot.security.constants;

/**
 * エラーコード
 */
public enum ErrorCode {

    /* BusinessException */
    /** 入力不正(通常) */
    INVALIDINPUT_GENERAL,

    /** 入力不正(想定外) */
    UNEXPECTEDINPUT_GENERAL,

    /* SystemException */
    /** システム障害 */
    SYSTEMERROR_GENERAL,

    /** データ不整合 */
    CONSISTENCYERROR_GENERAL;

}
