package com.thealtening.api.response;

import com.thealtening.api.response.AccountDetails;

public class Account {
    private String username;
    private String password;
    private String token;
    private boolean limit;
    private AccountDetails info;

    public String getToken() {
        return this.token;
    }

    public String getPassword() {
        return this.password;
    }

    public String getUsername() {
        return this.username;
    }

    public boolean isLimit() {
        return this.limit;
    }

    public AccountDetails getInfo() {
        return this.info;
    }

    public String toString() {
        return String.format((String)"Account[%s:%s:%s:%s]", (Object[])new Object[]{this.token, this.username, this.password, this.limit});
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Account)) {
            return false;
        }
        Account other = (Account)obj;
        return other.getUsername().equals((Object)this.username) && other.getToken().equals((Object)this.token);
    }
}
