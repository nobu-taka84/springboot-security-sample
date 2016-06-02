package com.springboot.security.dao.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.springboot.security.dao.entity.UserInfo;
import com.springboot.security.util.EncryptUtils;

@Repository
public class UserInfoRepository extends NamedParameterJdbcDaoSupport {

    @Autowired
    private EncryptUtils encrypt;

    private static final String FIND_BY_USERNAME = //
            "SELECT "//
                    + " t.id, "//
                    + " decrypt_null_on_err(t.username, :enc_key::bytea, :enc_iv::bytea, 'aes', 'UTF8') username, "//
                    + " t.last_logined_at, "//
                    + " t.password_valid_term, "//
                    + " t.miss_count "//
                    + " FROM user_info t "//
                    + " WHERE t.username = encrypt_iv(:username::bytea, :enc_key::bytea, :enc_iv::bytea, 'aes') "//
                    + "   AND t.delete_flg = false ";

    private static final String SELECT_USER_INFO = //
            "SELECT "//
                    + " t.id, "//
                    + " decrypt_null_on_err(t.username, :enc_key::bytea, :enc_iv::bytea, 'aes', 'UTF8') username, "//
                    + " t.last_logined_at, "//
                    + " t.password_valid_term, "//
                    + " t.miss_count "//
                    + " FROM user_info t "//
                    + " WHERE t.username = encrypt_iv(:username::bytea, :enc_key::bytea, :enc_iv::bytea, 'aes') "//
                    + "   AND t.password = encrypt_iv(:password::bytea, :enc_key::bytea, :enc_iv::bytea, 'aes') "//
                    + "   AND t.delete_flg = false ";

    private static final String INSERT_USER_INFO = //
            "INSERT INTO user_info ("//
                    + "  id, "//
                    + "  username, "//
                    + "  password, "//
                    + "  password_valid_term, "//
                    + "  created_by, "//
                    + "  updated_by "//
                    + ") VALUES ( "//
                    + "  :id, "//
                    + "  encrypt_iv(:username::bytea, :enc_key::bytea, :enc_iv::bytea, 'aes'), "//
                    + "  encrypt_iv(:password::bytea, :enc_key::bytea, :enc_iv::bytea, 'aes'), "//
                    + "  :password_valid_term, "//
                    + "  :created_by, "//
                    + "  :updated_by "//
                    + ")";

    private static final String UPDATE_PASSWORD = //
            "UPDATE user_info SET"//
                    + "  password = encrypt_iv(:password::bytea, :enc_key::bytea, :enc_iv::bytea, 'aes'), " //
                    + "  password_valid_term = :password_valid_term, " //
                    + "  miss_count = 0 " //
                    + " WHERE id = :id";//

    private static final String UPDATE_MISS_COUNT = //
            "UPDATE user_info SET " //
                    + "  miss_count = miss_count + 1 " //
                    + " WHERE id = :id";//

    private static final String UPDATE_LOGIN = //
            "UPDATE user_info SET " //
                    + "  miss_count = 0, " //
                    + "  last_logined_at = :last_logined_at " //
                    + " WHERE id = :id";//

    /**
     * コンストラクタ
     * 
     * @param dataSource
     */
    @Autowired
    public UserInfoRepository(DataSource dataSource) {
        setDataSource(dataSource);
    }

    /**
     * ユーザー名存在チェック
     * 
     * @param username
     * @return boolean
     * @return {@link UserInfo}
     */
    public UserInfo findByUsername(String username) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        params.put("enc_key", encrypt.getKey());
        params.put("enc_iv", encrypt.getIv());
        try {
            return getNamedParameterJdbcTemplate().queryForObject(//
                    FIND_BY_USERNAME, params, new UserInfoRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * ユーザー情報を検索
     * 
     * @param username
     * @param password
     * @return {@link UserInfo}
     */
    public UserInfo findByUsernameAndPassword(String username, String password) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        params.put("password", password);
        params.put("enc_key", encrypt.getKey());
        params.put("enc_iv", encrypt.getIv());

        try {
            return getNamedParameterJdbcTemplate().queryForObject(//
                    SELECT_USER_INFO, params, new UserInfoRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * ユーザー情報を登録
     * 
     * @param userInfo ユーザー情報
     */
    public void createUserInfo(UserInfo userInfo) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", userInfo.getId());
        params.put("username", userInfo.getUsername());
        params.put("password", userInfo.getPassword());
        params.put("password_valid_term", userInfo.getPasswordValidTerm());
        params.put("created_by", userInfo.getCreatedBy());
        params.put("updated_by", userInfo.getUpdatedBy());
        params.put("enc_key", encrypt.getKey());
        params.put("enc_iv", encrypt.getIv());
        getNamedParameterJdbcTemplate().update(INSERT_USER_INFO, params);
    }

    /**
     * パスワード変更
     * 
     * @param userInfo ユーザー情報
     */
    public void updatePassword(UserInfo userInfo) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", userInfo.getId());
        params.put("password", userInfo.getPassword());
        params.put("password_valid_term", userInfo.getPasswordValidTerm());
        params.put("enc_key", encrypt.getKey());
        params.put("enc_iv", encrypt.getIv());
        getNamedParameterJdbcTemplate().update(UPDATE_PASSWORD, params);
    }

    /**
     * ミス回数更新
     * 
     * @param userId ユーザーID
     */
    public void updateMissCount(Long userId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", userId);
        params.put("enc_key", encrypt.getKey());
        params.put("enc_iv", encrypt.getIv());
        getNamedParameterJdbcTemplate().update(UPDATE_MISS_COUNT, params);
    }

    /**
     * 最終ログイン日時更新
     * 
     * @param userId ユーザーID
     */
    public void updateLastLoginedAt(Long userId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", userId);
        params.put("last_logined_at", new Timestamp(System.currentTimeMillis()));
        params.put("enc_key", encrypt.getKey());
        params.put("enc_iv", encrypt.getIv());
        getNamedParameterJdbcTemplate().update(UPDATE_LOGIN, params);
    }

    /**
     * ユーザ情報のRowMapper
     */
    public static class UserInfoRowMapper implements RowMapper<UserInfo> {

        @Override
        public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserInfo userInfo = new UserInfo();
            baseMapping(userInfo, rs);
            return userInfo;
        }

        /**
         * ResultSetとエンティティのマッピングを行う
         * 
         * @param userInfo ユーザ情報
         * @param rs ResultSet
         * @throws SQLException SQLエラー
         */
        protected void baseMapping(UserInfo userInfo, ResultSet rs) throws SQLException {
            if (rs.getString("id") != null) {
                userInfo.setId(rs.getLong("id"));
            }
            userInfo.setUsername(rs.getString("username"));
            userInfo.setLastLoginedAt(rs.getTimestamp("last_logined_at"));
            userInfo.setPasswordValidTerm(rs.getDate("password_valid_term"));
            if (rs.getString("miss_count") != null) {
                userInfo.setMissCount(rs.getShort("miss_count"));
            }
        }
    }

}
