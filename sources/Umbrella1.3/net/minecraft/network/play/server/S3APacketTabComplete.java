/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S3APacketTabComplete
implements Packet {
    private String[] field_149632_a;
    private static final String __OBFID = "CL_00001288";

    public S3APacketTabComplete() {
    }

    public S3APacketTabComplete(String[] p_i45178_1_) {
        this.field_149632_a = p_i45178_1_;
    }

    @Override
    public void readPacketData(PacketBuffer data) throws IOException {
        this.field_149632_a = new String[data.readVarIntFromBuffer()];
        for (int var2 = 0; var2 < this.field_149632_a.length; ++var2) {
            this.field_149632_a[var2] = data.readStringFromBuffer(32767);
        }
    }

    @Override
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeVarIntToBuffer(this.field_149632_a.length);
        for (String var5 : this.field_149632_a) {
            data.writeString(var5);
        }
    }

    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleTabComplete(this);
    }

    public String[] func_149630_c() {
        return this.field_149632_a;
    }

    public void processPacket(INetHandler handler) {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}

