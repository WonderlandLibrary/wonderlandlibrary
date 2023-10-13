/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C00PacketKeepAlive
implements Packet {
    public int key;
    private static final String __OBFID = "CL_00001359";

    public C00PacketKeepAlive() {
    }

    public C00PacketKeepAlive(int p_i45252_1_) {
        this.key = p_i45252_1_;
    }

    public void processPacket(INetHandlerPlayServer handler) {
        handler.processKeepAlive(this);
    }

    @Override
    public void readPacketData(PacketBuffer data) throws IOException {
        this.key = data.readVarIntFromBuffer();
    }

    @Override
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeVarIntToBuffer(this.key);
    }

    public int getKey() {
        return this.key;
    }

    public void processPacket(INetHandler handler) {
        this.processPacket((INetHandlerPlayServer)handler);
    }
}

