/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.EnumDifficulty;

public class S41PacketServerDifficulty
implements Packet<INetHandlerPlayClient> {
    private EnumDifficulty difficulty;
    private boolean difficultyLocked;

    public EnumDifficulty getDifficulty() {
        return this.difficulty;
    }

    public S41PacketServerDifficulty(EnumDifficulty enumDifficulty, boolean bl) {
        this.difficulty = enumDifficulty;
        this.difficultyLocked = bl;
    }

    public boolean isDifficultyLocked() {
        return this.difficultyLocked;
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeByte(this.difficulty.getDifficultyId());
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleServerDifficulty(this);
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.difficulty = EnumDifficulty.getDifficultyEnum(packetBuffer.readUnsignedByte());
    }

    public S41PacketServerDifficulty() {
    }
}

