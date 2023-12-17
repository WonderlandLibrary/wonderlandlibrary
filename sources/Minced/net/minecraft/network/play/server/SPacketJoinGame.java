// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.WorldType;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameType;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketJoinGame implements Packet<INetHandlerPlayClient>
{
    private int playerId;
    private boolean hardcoreMode;
    private GameType gameType;
    private int dimension;
    private EnumDifficulty difficulty;
    private int maxPlayers;
    private WorldType worldType;
    private boolean reducedDebugInfo;
    
    public SPacketJoinGame() {
    }
    
    public SPacketJoinGame(final int playerIdIn, final GameType gameTypeIn, final boolean hardcoreModeIn, final int dimensionIn, final EnumDifficulty difficultyIn, final int maxPlayersIn, final WorldType worldTypeIn, final boolean reducedDebugInfoIn) {
        this.playerId = playerIdIn;
        this.dimension = dimensionIn;
        this.difficulty = difficultyIn;
        this.gameType = gameTypeIn;
        this.maxPlayers = maxPlayersIn;
        this.hardcoreMode = hardcoreModeIn;
        this.worldType = worldTypeIn;
        this.reducedDebugInfo = reducedDebugInfoIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.playerId = buf.readInt();
        int i = buf.readUnsignedByte();
        this.hardcoreMode = ((i & 0x8) == 0x8);
        i &= 0xFFFFFFF7;
        this.gameType = GameType.getByID(i);
        this.dimension = buf.readInt();
        this.difficulty = EnumDifficulty.byId(buf.readUnsignedByte());
        this.maxPlayers = buf.readUnsignedByte();
        this.worldType = WorldType.byName(buf.readString(16));
        if (this.worldType == null) {
            this.worldType = WorldType.DEFAULT;
        }
        this.reducedDebugInfo = buf.readBoolean();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeInt(this.playerId);
        int i = this.gameType.getID();
        if (this.hardcoreMode) {
            i |= 0x8;
        }
        buf.writeByte(i);
        buf.writeInt(this.dimension);
        buf.writeByte(this.difficulty.getId());
        buf.writeByte(this.maxPlayers);
        buf.writeString(this.worldType.getName());
        buf.writeBoolean(this.reducedDebugInfo);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleJoinGame(this);
    }
    
    public int getPlayerId() {
        return this.playerId;
    }
    
    public boolean isHardcoreMode() {
        return this.hardcoreMode;
    }
    
    public GameType getGameType() {
        return this.gameType;
    }
    
    public int getDimension() {
        return this.dimension;
    }
    
    public EnumDifficulty getDifficulty() {
        return this.difficulty;
    }
    
    public int getMaxPlayers() {
        return this.maxPlayers;
    }
    
    public WorldType getWorldType() {
        return this.worldType;
    }
    
    public boolean isReducedDebugInfo() {
        return this.reducedDebugInfo;
    }
}
