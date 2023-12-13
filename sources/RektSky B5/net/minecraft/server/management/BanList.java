/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.server.management;

import com.google.gson.JsonObject;
import java.io.File;
import java.net.SocketAddress;
import net.minecraft.server.management.IPBanEntry;
import net.minecraft.server.management.UserList;
import net.minecraft.server.management.UserListEntry;

public class BanList
extends UserList<String, IPBanEntry> {
    public BanList(File bansFile) {
        super(bansFile);
    }

    @Override
    protected UserListEntry<String> createEntry(JsonObject entryData) {
        return new IPBanEntry(entryData);
    }

    public boolean isBanned(SocketAddress address) {
        String s2 = this.addressToString(address);
        return this.hasEntry(s2);
    }

    public IPBanEntry getBanEntry(SocketAddress address) {
        String s2 = this.addressToString(address);
        return (IPBanEntry)this.getEntry(s2);
    }

    private String addressToString(SocketAddress address) {
        String s2 = address.toString();
        if (s2.contains("/")) {
            s2 = s2.substring(s2.indexOf(47) + 1);
        }
        if (s2.contains(":")) {
            s2 = s2.substring(0, s2.indexOf(58));
        }
        return s2;
    }
}

