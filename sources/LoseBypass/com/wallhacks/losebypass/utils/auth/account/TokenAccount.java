/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.utils.auth.account;

import com.wallhacks.losebypass.utils.auth.account.Account;
import com.wallhacks.losebypass.utils.auth.account.AccountType;
import net.minecraft.util.Session;

public class TokenAccount
extends Account {
    String token;

    public TokenAccount(String uuid, String token, String name) {
        super(new Session(name, uuid, token, "Mojang"), AccountType.TOKEN, uuid);
        this.token = token;
    }

    @Override
    public String getStatus() {
        return "Token Account";
    }

    public String getToken() {
        return this.token;
    }
}

