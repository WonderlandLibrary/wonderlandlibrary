/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 */
package com.wallhacks.losebypass.gui.components;

import com.mojang.authlib.GameProfile;
import com.wallhacks.losebypass.manager.AltManager;
import com.wallhacks.losebypass.utils.SessionUtils;
import com.wallhacks.losebypass.utils.auth.account.Account;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.network.NetworkPlayerInfo;

public class AltAccountComponent {
    Account account;
    EntityOtherPlayerMP player;

    public AltAccountComponent(Account account) {
        this.account = account;
        this.player = new EntityOtherPlayerMP(AltManager.fakeWorld, new GameProfile(SessionUtils.fromString(account.getUUID()), account.getName()));
        this.player.playerInfo = new NetworkPlayerInfo(this.player.getGameProfile());
        ThreadSkin threadSkin = new ThreadSkin(this.player.playerInfo);
        threadSkin.start();
    }

    public EntityOtherPlayerMP getPlayer() {
        return this.player;
    }

    public Account getAccount() {
        return this.account;
    }

    class ThreadSkin
    extends Thread {
        NetworkPlayerInfo info;

        public ThreadSkin(NetworkPlayerInfo info) {
            this.info = info;
        }

        @Override
        public void run() {
            SessionUtils.setSkin(this.info, this.info.getGameProfile().getId());
        }
    }
}

