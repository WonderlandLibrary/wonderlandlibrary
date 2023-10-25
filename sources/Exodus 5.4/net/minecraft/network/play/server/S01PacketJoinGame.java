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

public class S01PacketJoinGame
implements Packet<INetHandlerPlayClient> {
    private int entityId;
    private int maxPlayers;
    private WorldType worldType;
    private boolean hardcoreMode;
    private WorldSettings.GameType gameType;
    private boolean reducedDebugInfo;
    private int dimension;
    private EnumDifficulty difficulty;

    public S01PacketJoinGame() {
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeInt(this.entityId);
        int n = this.gameType.getID();
        if (this.hardcoreMode) {
            n |= 8;
        }
        packetBuffer.writeByte(n);
        packetBuffer.writeByte(this.dimension);
        packetBuffer.writeByte(this.difficulty.getDifficultyId());
        packetBuffer.writeByte(this.maxPlayers);
        packetBuffer.writeString(this.worldType.getWorldTypeName());
        packetBuffer.writeBoolean(this.reducedDebugInfo);
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.entityId = packetBuffer.readInt();
        int n = packetBuffer.readUnsignedByte();
        this.hardcoreMode = (n & 8) == 8;
        this.gameType = WorldSettings.GameType.getByID(n &= 0xFFFFFFF7);
        this.dimension = packetBuffer.readByte();
        this.difficulty = EnumDifficulty.getDifficultyEnum(packetBuffer.readUnsignedByte());
        this.maxPlayers = packetBuffer.readUnsignedByte();
        this.worldType = WorldType.parseWorldType(packetBuffer.readStringFromBuffer(16));
        if (this.worldType == null) {
            this.worldType = WorldType.DEFAULT;
        }
        this.reducedDebugInfo = packetBuffer.readBoolean();
    }

    public S01PacketJoinGame(int n, WorldSettings.GameType gameType, boolean bl, int n2, EnumDifficulty enumDifficulty, int n3, WorldType worldType, boolean bl2) {
        this.entityId = n;
        this.dimension = n2;
        this.difficulty = enumDifficulty;
        this.gameType = gameType;
        this.maxPlayers = n3;
        this.hardcoreMode = bl;
        this.worldType = worldType;
        this.reducedDebugInfo = bl2;
    }

    public WorldSettings.GameType getGameType() {
        return this.gameType;
    }

    public boolean isHardcoreMode() {
        return this.hardcoreMode;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public WorldType getWorldType() {
        return this.worldType;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public EnumDifficulty getDifficulty() {
        return this.difficulty;
    }

    public int getDimension() {
        return this.dimension;
    }

    public boolean isReducedDebugInfo() {
        return this.reducedDebugInfo;
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleJoinGame(this);
    }
}

