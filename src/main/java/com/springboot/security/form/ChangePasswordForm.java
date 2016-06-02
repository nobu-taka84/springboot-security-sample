package com.springboot.security.form;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * ChangePasswordForm
 */
public class ChangePasswordForm implements Serializable {
    private static final long serialVersionUID = 1L;

    /** password */
    @NotEmpty(message = "password can't be blank.")
    private String password;

    /** confirm password */
    private String confirmPassword;

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
