package com.springboot.security.service;

import java.util.List;

import com.springboot.security.dao.dto.UserInfoDto;
import com.springboot.security.dao.dto.UserPrivilegeInfoDto;
import com.springboot.security.dao.entity.LoginHistory;

public interface UserInfoService {

    /**
     * ユーザー存在チェック
     * 
     * @param userName ユーザー名
     * @return boolean true いる : false いない
     */
    boolean isUsernameExist(String userName);

    /**
     * ユーザー取得
     * 
     * @param userName ユーザー名
     * @return {@link UserInfoDto}
     */
    UserInfoDto selectUserInfoByUsername(String userName);

    /**
     * ユーザー取得
     * 
     * @param userName ユーザー名
     * @param password パスワード
     * @return {@link UserInfoDto}
     */
    UserInfoDto selectUserInfoByUsernameAndPassword(String userName, String password);

    /**
     * ユーザー権限取得
     * 
     * @param userInfoId ユーザーID
     * @return List<{@link UserPrivilegeInfoDto}>
     */
    List<UserPrivilegeInfoDto> selectUserPrivilegeInfoByUserInfoId(Long userInfoId);

    /**
     * ユーザー作成
     * 
     * @param userName ユーザー名
     * @param password パスワード
     * @return {@link UserInfoDtoDto}
     */
    UserInfoDto createUserInfo(String userName, String password);

    /**
     * パスワード更新
     * 
     * @param userName ユーザー名
     * @param password パスワード
     * @return {@link UserInfoDtoDto}
     */
    void updatePassword(String userName, String password);

    /**
     * 前回ログイン日時更新
     * 
     * @param userId ユーザーID
     */
    void updateLastLoginedAt(Long userId);

    /**
     * ログイン履歴更新
     * 
     * @param loginHistory ログイン履歴
     */
    void updateLoginHistory(LoginHistory loginHistory);

    /**
     * 認証失敗回数更新
     * 
     * @param userId ユーザーID
     */
    void updateMissCount(Long userId);

}
