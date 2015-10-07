package com.lucien.hkmdemo.model;

/**
 * Created by lucien.li on 2015/10/5.
 */
public class AccountModel {

    private String username;
    private String password;

    private boolean status;

    public AccountModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLogin() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public class LoginStatus {
        private int success;
        private int error;

        public int getSuccess() {
            return success;
        }

        public void setSuccess(int success) {
            this.success = success;
            setStatus(true);
        }

        public int getError() {
            return error;
        }

        public void setError(int error) {
            this.error = error;
            setStatus(false);
        }
    }
}
