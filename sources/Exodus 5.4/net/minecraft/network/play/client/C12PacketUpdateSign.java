/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;

public class C12PacketUpdateSign
implements Packet<INetHandlerPlayServer> {
    private IChatComponent[] lines;
    private BlockPos pos;

    public C12PacketUpdateSign(BlockPos blockPos, IChatComponent[] iChatComponentArray) {
        this.pos = blockPos;
        this.lines = new IChatComponent[]{iChatComponentArray[0], iChatComponentArray[1], iChatComponentArray[2], iChatComponentArray[3]};
    }

    public C12PacketUpdateSign() {
    }

    @Override
    public void processPacket(INetHandlerPlayServer iNetHandlerPlayServer) {
        iNetHandlerPlayServer.processUpdateSign(this);
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.pos = packetBuffer.readBlockPos();
        this.lines = new IChatComponent[4];
        int n = 0;
        while (n < 4) {
            IChatComponent iChatComponent;
            String string = packetBuffer.readStringFromBuffer(384);
            this.lines[n] = iChatComponent = IChatComponent.Serializer.jsonToComponent(string);
            ++n;
        }
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeBlockPos(this.pos);
        int n = 0;
        while (n < 4) {
            IChatComponent iChatComponent = this.lines[n];
            String string = IChatComponent.Serializer.componentToJson(iChatComponent);
            packetBuffer.writeString(string);
            ++n;
        }
    }

    public BlockPos getPosition() {
        return this.pos;
    }

    public IChatComponent[] getLines() {
        return this.lines;
    }
}

