/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import java.io.IOException;

public class ServerTabCompletePacket
extends MinecraftPacket {
    private String[] matches;

    private ServerTabCompletePacket() {
    }

    public ServerTabCompletePacket(String[] matches) {
        this.matches = matches;
    }

    public String[] getMatches() {
        return this.matches;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.matches = new String[in.readVarInt()];
        int index = 0;
        while (index < this.matches.length) {
            this.matches[index] = in.readString();
            ++index;
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.matches.length);
        String[] stringArray = this.matches;
        int n = this.matches.length;
        int n2 = 0;
        while (n2 < n) {
            String match = stringArray[n2];
            out.writeString(match);
            ++n2;
        }
    }
}

