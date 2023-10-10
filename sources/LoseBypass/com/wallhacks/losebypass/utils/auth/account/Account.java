/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package com.wallhacks.losebypass.utils.auth.account;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.wallhacks.losebypass.utils.auth.account.AccountType;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class Account {
    public Session session;
    public AccountType accountType;
    public boolean loading = false;
    public String uuid;

    public Account(Session session, AccountType accountType, String uuid) {
        this.session = session;
        this.accountType = accountType;
        this.uuid = uuid;
    }

    public void login() {
        if (this.session == null) {
            this.setSession();
            return;
        }
        Minecraft.getMinecraft().session = this.session;
    }

    public void setSession() {
    }

    public String getName() {
        return this.session.getProfile().getName();
    }

    public String getUUID() {
        if (this.session == null) return this.uuid;
        return this.session.getProfile().getId().toString();
    }

    public String getStatus() {
        return ChatFormatting.RED + "Cracked";
    }
}

