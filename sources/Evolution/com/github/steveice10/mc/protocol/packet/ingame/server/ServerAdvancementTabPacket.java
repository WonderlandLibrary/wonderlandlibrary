/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import java.io.IOException;

public class ServerAdvancementTabPacket
extends MinecraftPacket {
    private String tabId;

    private ServerAdvancementTabPacket() {
    }

    public ServerAdvancementTabPacket(String tabId) {
        this.tabId = tabId;
    }

    public String getTabId() {
        return this.tabId;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.tabId = in.readBoolean() ? in.readString() : null;
    }

    @Override
    public void write(NetOutput out) throws IOException {
        if (this.tabId != null) {
            out.writeBoolean(true);
            out.writeString(this.tabId);
        } else {
            out.writeBoolean(false);
        }
    }
}

