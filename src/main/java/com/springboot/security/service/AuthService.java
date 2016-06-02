package com.springboot.security.service;

import com.springboot.security.dao.dto.UserInfoDto;
import com.springboot.security.exception.UserInfoException;

public interface AuthService {

    /**
     * ユーザー名チェック
     * 
     * @param userName ユーザー名
     * @return true : OK
     * @throws UserInfoException
     */
    boolean checkUserName(String userName) throws UserInfoException;

    /**
     * パスワードチェック
     * 
     * @param password パスワード
     * @return true : OK
     * @throws UserInfoException
     */
    boolean checkPassword(String password) throws UserInfoException;

    /**
     * ソルト + ストレッチングしたパスワードを取得する
     * 
     * @param password パスワード
     * @param userId ユーザーID
     * @return ソルト＋ストレッチングしたパスワード
     */
    String getSoltAndStretchingPassword(String password, Long userId);

    /**
     * 文字列から SHA256 のハッシュ値を取得する
     * 
     * @param target 対象文字列
     * @return ハッシュ後の文字列
     */
    String getSha256(String target);

    /**
     * ユーザー認証
     * 
     * @param username ユーザー名
     * @param password パスワード
     * @return {@link UserInfoDto}
     */
    UserInfoDto authenticate(String username, String password) throws UserInfoException;

}
