// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.WorldType;
import net.minecraft.world.GameType;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketRespawn implements Packet<INetHandlerPlayClient>
{
    private int dimensionID;
    private EnumDifficulty difficulty;
    private GameType gameType;
    private WorldType worldType;
    
    public SPacketRespawn() {
    }
    
    public SPacketRespawn(final int dimensionIdIn, final EnumDifficulty difficultyIn, final WorldType worldTypeIn, final GameType gameModeIn) {
        this.dimensionID = dimensionIdIn;
        this.difficulty = difficultyIn;
        this.gameType = gameModeIn;
        this.worldType = worldTypeIn;
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleRespawn(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.dimensionID = buf.readInt();
        this.difficulty = EnumDifficulty.byId(buf.readUnsignedByte());
        this.gameType = GameType.getByID(buf.readUnsignedByte());
        this.worldType = WorldType.byName(buf.readString(16));
        if (this.worldType == null) {
            this.worldType = WorldType.DEFAULT;
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeInt(this.dimensionID);
        buf.writeByte(this.difficulty.getId());
        buf.writeByte(this.gameType.getID());
        buf.writeString(this.worldType.getName());
    }
    
    public int getDimensionID() {
        return this.dimensionID;
    }
    
    public EnumDifficulty getDifficulty() {
        return this.difficulty;
    }
    
    public GameType getGameType() {
        return this.gameType;
    }
    
    public WorldType getWorldType() {
        return this.worldType;
    }
}
