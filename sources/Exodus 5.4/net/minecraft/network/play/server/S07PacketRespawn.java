/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;

public class S07PacketRespawn
implements Packet<INetHandlerPlayClient> {
    private WorldType worldType;
    private int dimensionID;
    private EnumDifficulty difficulty;
    private WorldSettings.GameType gameType;

    public WorldSettings.GameType getGameType() {
        return this.gameType;
    }

    public WorldType getWorldType() {
        return this.worldType;
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeInt(this.dimensionID);
        packetBuffer.writeByte(this.difficulty.getDifficultyId());
        packetBuffer.writeByte(this.gameType.getID());
        packetBuffer.writeString(this.worldType.getWorldTypeName());
    }

    public S07PacketRespawn() {
    }

    public S07PacketRespawn(int n, EnumDifficulty enumDifficulty, WorldType worldType, WorldSettings.GameType gameType) {
        this.dimensionID = n;
        this.difficulty = enumDifficulty;
        this.gameType = gameType;
        this.worldType = worldType;
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleRespawn(this);
    }

    public EnumDifficulty getDifficulty() {
        return this.difficulty;
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.dimensionID = packetBuffer.readInt();
        this.difficulty = EnumDifficulty.getDifficultyEnum(packetBuffer.readUnsignedByte());
        this.gameType = WorldSettings.GameType.getByID(packetBuffer.readUnsignedByte());
        this.worldType = WorldType.parseWorldType(packetBuffer.readStringFromBuffer(16));
        if (this.worldType == null) {
            this.worldType = WorldType.DEFAULT;
        }
    }

    public int getDimensionID() {
        return this.dimensionID;
    }
}

