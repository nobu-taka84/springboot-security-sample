package com.springboot.security.form;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * JoinForm
 */
public class JoinForm implements Serializable {
    private static final long serialVersionUID = 1L;

    /** username */
    @NotEmpty(message = "username can't be blank.")
    private String username;

    /** password */
    @NotEmpty(message = "password can't be blank.")
    private String password;

    /** confirm password */
    private String confirmPassword;

    /**
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username セットする username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password セットする password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return confirmPassword
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * @param confirmPassword セットする confirmPassword
     */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

}
