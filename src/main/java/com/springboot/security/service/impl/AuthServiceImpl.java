package com.springboot.security.service.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.common.base.Strings;
import com.springboot.security.constants.ErrorCode;
import com.springboot.security.constants.UserInfoErrorType;
import com.springboot.security.dao.dto.UserInfoDto;
import com.springboot.security.dao.dto.UserPrivilegeInfoDto;
import com.springboot.security.exception.SystemException;
import com.springboot.security.exception.UserInfoException;
import com.springboot.security.service.AuthService;
import com.springboot.security.service.UserInfoService;

@Service
public class AuthServiceImpl implements AuthService {

    @Value("${auth.userNameMinLength:0}")
    private int userNameMinLength;

    @Value("${auth.userNameMaxLength:0}")
    private int userNameMaxLength;

    @Value("${auth.userNameComplexity}")
    private String userNameComplexity;

    @Value("${auth.passwordMinLength:0}")
    private int passwordMinLength;

    @Value("${auth.passwordMaxLength:0}")
    private int passwordMaxLength;

    @Value("${auth.passwordEncryption.partOfSolt}")
    private String partOfSolt;

    @Value("${auth.passwordEncryption.stretchingCount:0}")
    private int stretchingCount;

    @Value("${auth.authMissMax:0}")
    private int authMissMax;

    @Value("${auth.passwordComplexity}")
    private String passwordComplexity;

    @Autowired
    private UserInfoService userInfoService;

    @Override
    public boolean checkUserName(String userName) throws UserInfoException {
        if (userNameMinLength > 0 && userName.length() < userNameMinLength) {
            throw new UserInfoException(UserInfoErrorType.USER_NAME_TOO_SHORT);
        }
        if (userNameMaxLength > 0 && userName.length() > userNameMaxLength) {
            throw new UserInfoException(UserInfoErrorType.USER_NAME_TOO_LONG);
        }
        if (!Strings.isNullOrEmpty(userNameComplexity)) {
            if (!chackPattern(userName, userNameComplexity)) {
                throw new UserInfoException(UserInfoErrorType.ID_OR_PASSWORD_INVALID);
            }
        }
        return true;
    }

    @Override
    public boolean checkPassword(String password) throws UserInfoException {
        if (passwordMinLength > 0 && password.length() < passwordMinLength) {
            throw new UserInfoException(UserInfoErrorType.PASSWORD_TOO_SHORT);
        }
        if (passwordMaxLength > 0 && password.length() > passwordMaxLength) {
            throw new UserInfoException(UserInfoErrorType.PASSWORD_TOO_LONG);
        }
        if (!Strings.isNullOrEmpty(passwordComplexity)) {
            if (!chackPattern(password, passwordComplexity)) {
                throw new UserInfoException(UserInfoErrorType.PASSWORD_UNCOMPLICATED);
            }
        }
        return true;
    }

    /**
     * パターンチェック
     * 
     * @param word 対象文字列
     * @param regex チェックパターン
     * @return パターンにマッチした:true, マッチしない:false
     */
    private boolean chackPattern(String word, String regex) {
        return Pattern.compile(regex).matcher(word).find();
    }

    @Override
    public String getSoltAndStretchingPassword(String password, Long userId) {
        if (StringUtils.isEmpty(password)) {
            return null;
        }

        String hash = "";
        String salt = getSha256(userId + partOfSolt);

        for (int i = 0; i < stretchingCount; i++) {
            hash = getSha256(hash + salt + password);
        }

        return hash;
    }

    @Override
    public String getSha256(String target) {
        MessageDigest md = null;
        StringBuilder sb = new StringBuilder();

        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new SystemException(ErrorCode.SYSTEMERROR_GENERAL.name(),
                    "SHA256のハッシュ値を取得に失敗しました [" + e.getMessage() + "]");
        }
        md.update(target.getBytes());
        byte[] digest = md.digest();

        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }

    @Override
    public UserInfoDto authenticate(String username, String password) throws UserInfoException {
        // ユーザー名チェック
        if (!Strings.isNullOrEmpty(userNameComplexity)) {
            if (!chackPattern(username, userNameComplexity)) {
                throw new UserInfoException(UserInfoErrorType.ID_OR_PASSWORD_INVALID);
            }
        }

        UserInfoDto tempUserInfoDto = userInfoService.selectUserInfoByUsername(username);
        // userが存在しない
        if (tempUserInfoDto == null || tempUserInfoDto.getId() == null) {
            throw new UserInfoException(UserInfoErrorType.USER_NOT_EXISTS);
        }
        // アカウントロック
        if (authMissMax > 0 && tempUserInfoDto.getMissCount() >= authMissMax) {
            throw new UserInfoException(UserInfoErrorType.ACCOUNT_LOCKED);
        }

        // passwordのストレッチ
        String stretchingPassword = getSoltAndStretchingPassword(password, tempUserInfoDto.getId());

        // user_info取得
        UserInfoDto userInfoDto =
                userInfoService.selectUserInfoByUsernameAndPassword(username, stretchingPassword);

        // passwordが違う
        if (userInfoDto == null) {
            userInfoService.updateMissCount(tempUserInfoDto.getId());
            throw new UserInfoException(UserInfoErrorType.ID_OR_PASSWORD_INVALID);
        }

        List<UserPrivilegeInfoDto> dtoList =
                userInfoService.selectUserPrivilegeInfoByUserInfoId(userInfoDto.getId());

        // 権限
        List<String> userPrivilegeList = new ArrayList<>();
        dtoList.forEach(userPrivilegeInfo -> {
            userPrivilegeList.add(userPrivilegeInfo.getUserPrivilege());
        });
        userInfoDto.setUserPrivilegeInfoList(userPrivilegeList);

        return userInfoDto;
    }

}
