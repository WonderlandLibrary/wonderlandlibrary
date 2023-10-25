/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 */
package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.BlockPos;
import org.apache.commons.lang3.StringUtils;

public class C14PacketTabComplete
implements Packet<INetHandlerPlayServer> {
    private BlockPos targetBlock;
    private String message;

    @Override
    public void processPacket(INetHandlerPlayServer iNetHandlerPlayServer) {
        iNetHandlerPlayServer.processTabComplete(this);
    }

    public C14PacketTabComplete() {
    }

    public C14PacketTabComplete(String string, BlockPos blockPos) {
        this.message = string;
        this.targetBlock = blockPos;
    }

    public C14PacketTabComplete(String string) {
        this(string, null);
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.message = packetBuffer.readStringFromBuffer(Short.MAX_VALUE);
        boolean bl = packetBuffer.readBoolean();
        if (bl) {
            this.targetBlock = packetBuffer.readBlockPos();
        }
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeString(StringUtils.substring((String)this.message, (int)0, (int)Short.MAX_VALUE));
        boolean bl = this.targetBlock != null;
        packetBuffer.writeBoolean(bl);
        if (bl) {
            packetBuffer.writeBlockPos(this.targetBlock);
        }
    }

    public BlockPos getTargetBlock() {
        return this.targetBlock;
    }

    public String getMessage() {
        return this.message;
    }
}

