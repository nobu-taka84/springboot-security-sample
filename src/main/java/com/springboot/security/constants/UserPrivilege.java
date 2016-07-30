package com.springboot.security.constants;

/**
 * ユーザ権限
 *
 */
public enum UserPrivilege {

    /** 管理者 */
    ADMIN("01", "ROLE_ADMIN"),

    /** サブ管理者 */
    ADMIN_SUB("02", "ROLE_ADMIN_SUB"),

    /** 一般ユーザ */
    USER("03", "ROLE_USER");

    private final String userPrivilege;
    private final String role;

    private UserPrivilege(String userPrivilege, String role) {
        this.userPrivilege = userPrivilege;
        this.role = role;
    }

    /**
     * 権限idを取得する
     *
     * @return 権限id
     */
    public String getUserPrivilege() {
        return userPrivilege;
    }

    /**
     * ロール名を取得する
     *
     * @return ロール名
     */
    public String getRole() {
        return role;
    }

    /**
     * 指定した権限idのロール名を取得する
     *
     * @param userPrivilege
     * @return ロール名
     */
    public static String getUserRole(String userPrivilege) {
        UserPrivilege[] enumArray = UserPrivilege.values();
        for (UserPrivilege enumElement : enumArray) {
            if (enumElement.userPrivilege.equals(userPrivilege)) {
                return enumElement.getRole();
            }
        }
        return null;
    }

    /**
     * userRoleに該当するUserPrivilegeを取得する
     *
     * @param userRole ユーザロール名
     * @return UserPrivilege
     */
    public static UserPrivilege getUserPrivilegeByRole(String userRole) {
        UserPrivilege[] enumArray = UserPrivilege.values();
        for (UserPrivilege enumElement : enumArray) {
            if (enumElement.role.equals(userRole)) {
                return enumElement;
            }
        }
        return null;
    }

}
