// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.WorldType;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.network.Packet;

public class S07PacketRespawn implements Packet
{
    private int field_149088_a;
    private EnumDifficulty field_149086_b;
    private WorldSettings.GameType field_149087_c;
    private WorldType field_149085_d;
    private static final String __OBFID = "CL_00001322";
    
    public S07PacketRespawn() {
    }
    
    public S07PacketRespawn(final int p_i45213_1_, final EnumDifficulty p_i45213_2_, final WorldType p_i45213_3_, final WorldSettings.GameType p_i45213_4_) {
        this.field_149088_a = p_i45213_1_;
        this.field_149086_b = p_i45213_2_;
        this.field_149087_c = p_i45213_4_;
        this.field_149085_d = p_i45213_3_;
    }
    
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleRespawn(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.field_149088_a = data.readInt();
        this.field_149086_b = EnumDifficulty.getDifficultyEnum(data.readUnsignedByte());
        this.field_149087_c = WorldSettings.GameType.getByID(data.readUnsignedByte());
        this.field_149085_d = WorldType.parseWorldType(data.readStringFromBuffer(16));
        if (this.field_149085_d == null) {
            this.field_149085_d = WorldType.DEFAULT;
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeInt(this.field_149088_a);
        data.writeByte(this.field_149086_b.getDifficultyId());
        data.writeByte(this.field_149087_c.getID());
        data.writeString(this.field_149085_d.getWorldTypeName());
    }
    
    public int func_149082_c() {
        return this.field_149088_a;
    }
    
    public EnumDifficulty func_149081_d() {
        return this.field_149086_b;
    }
    
    public WorldSettings.GameType func_149083_e() {
        return this.field_149087_c;
    }
    
    public WorldType func_149080_f() {
        return this.field_149085_d;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
