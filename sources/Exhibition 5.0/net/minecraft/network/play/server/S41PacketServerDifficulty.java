// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.network.Packet;

public class S41PacketServerDifficulty implements Packet
{
    private EnumDifficulty field_179833_a;
    private boolean field_179832_b;
    private static final String __OBFID = "CL_00002303";
    
    public S41PacketServerDifficulty() {
    }
    
    public S41PacketServerDifficulty(final EnumDifficulty p_i45987_1_, final boolean p_i45987_2_) {
        this.field_179833_a = p_i45987_1_;
        this.field_179832_b = p_i45987_2_;
    }
    
    public void func_179829_a(final INetHandlerPlayClient p_179829_1_) {
        p_179829_1_.func_175101_a(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.field_179833_a = EnumDifficulty.getDifficultyEnum(data.readUnsignedByte());
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeByte(this.field_179833_a.getDifficultyId());
    }
    
    public boolean func_179830_a() {
        return this.field_179832_b;
    }
    
    public EnumDifficulty func_179831_b() {
        return this.field_179833_a;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.func_179829_a((INetHandlerPlayClient)handler);
    }
}
