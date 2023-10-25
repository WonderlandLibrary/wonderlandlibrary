/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.packet.ingame.client.world;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.mc.protocol.util.NetUtil;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import java.io.IOException;

public class ClientUpdateSignPacket
extends MinecraftPacket {
    private Position position;
    private String[] lines;

    private ClientUpdateSignPacket() {
    }

    public ClientUpdateSignPacket(Position position, String[] lines) {
        if (lines.length != 4) {
            throw new IllegalArgumentException("Lines must contain exactly 4 strings!");
        }
        this.position = position;
        this.lines = lines;
    }

    public Position getPosition() {
        return this.position;
    }

    public String[] getLines() {
        return this.lines;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.position = NetUtil.readPosition(in);
        this.lines = new String[4];
        int count = 0;
        while (count < this.lines.length) {
            this.lines[count] = in.readString();
            ++count;
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        NetUtil.writePosition(out, this.position);
        String[] stringArray = this.lines;
        int n = this.lines.length;
        int n2 = 0;
        while (n2 < n) {
            String line = stringArray[n2];
            out.writeString(line);
            ++n2;
        }
    }
}

