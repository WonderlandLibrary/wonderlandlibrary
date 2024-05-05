package com.thealtening.api.data;

import com.thealtening.api.data.info.AccountInfo;

public class AccountData {

    private String token;

    private String password;

    private String username;

    private boolean limit;

    private AccountInfo info;

    public String getToken() {
        return token;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public boolean isLimit() {
        return limit;
    }

    public AccountInfo getInfo() {
        return info;
    }

    @Override
    public String toString() {
        return String.format("AccountData[%s:%s:%s:%s]", token, username, password, limit);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AccountData)) {
            return false;
        }
        AccountData castedAccountInfo = (AccountData) obj;
        return castedAccountInfo.getUsername().equals(username) && castedAccountInfo.getToken().equals(token);
    }
}
