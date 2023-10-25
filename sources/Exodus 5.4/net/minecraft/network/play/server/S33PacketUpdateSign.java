/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public class S33PacketUpdateSign
implements Packet<INetHandlerPlayClient> {
    private World world;
    private BlockPos blockPos;
    private IChatComponent[] lines;

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.blockPos = packetBuffer.readBlockPos();
        this.lines = new IChatComponent[4];
        int n = 0;
        while (n < 4) {
            this.lines[n] = packetBuffer.readChatComponent();
            ++n;
        }
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleUpdateSign(this);
    }

    public IChatComponent[] getLines() {
        return this.lines;
    }

    public S33PacketUpdateSign(World world, BlockPos blockPos, IChatComponent[] iChatComponentArray) {
        this.world = world;
        this.blockPos = blockPos;
        this.lines = new IChatComponent[]{iChatComponentArray[0], iChatComponentArray[1], iChatComponentArray[2], iChatComponentArray[3]};
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeBlockPos(this.blockPos);
        int n = 0;
        while (n < 4) {
            packetBuffer.writeChatComponent(this.lines[n]);
            ++n;
        }
    }

    public S33PacketUpdateSign() {
    }

    public BlockPos getPos() {
        return this.blockPos;
    }
}

