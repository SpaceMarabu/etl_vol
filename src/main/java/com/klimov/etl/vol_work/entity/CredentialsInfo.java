package com.klimov.etl.vol_work.entity;

public class CredentialsInfo {
    private String login;
    private String password;
    private boolean authError;

    public CredentialsInfo() {
        this.authError = false;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAuthError() {
        return authError;
    }

    public void setAuthError(boolean authError) {
        this.authError = authError;
    }
}
