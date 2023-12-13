// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.server;

import java.util.Iterator;
import java.util.EnumSet;
import net.minecraft.network.INetHandler;
import net.minecraft.network.play.INetHandlerPlayClient;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import java.util.Set;
import net.minecraft.network.Packet;

public class S08PacketPlayerPosLook implements Packet
{
    private double field_148940_a;
    private double field_148938_b;
    private double field_148939_c;
    public float yaw;
    public float pitch;
    private Set field_179835_f;
    private static final String __OBFID = "CL_00001273";
    
    public S08PacketPlayerPosLook() {
    }
    
    public S08PacketPlayerPosLook(final double p_i45993_1_, final double p_i45993_3_, final double p_i45993_5_, final float p_i45993_7_, final float p_i45993_8_, final Set p_i45993_9_) {
        this.field_148940_a = p_i45993_1_;
        this.field_148938_b = p_i45993_3_;
        this.field_148939_c = p_i45993_5_;
        this.yaw = p_i45993_7_;
        this.pitch = p_i45993_8_;
        this.field_179835_f = p_i45993_9_;
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.field_148940_a = data.readDouble();
        this.field_148938_b = data.readDouble();
        this.field_148939_c = data.readDouble();
        this.yaw = data.readFloat();
        this.pitch = data.readFloat();
        this.field_179835_f = EnumFlags.func_180053_a(data.readUnsignedByte());
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeDouble(this.field_148940_a);
        data.writeDouble(this.field_148938_b);
        data.writeDouble(this.field_148939_c);
        data.writeFloat(this.yaw);
        data.writeFloat(this.pitch);
        data.writeByte(EnumFlags.func_180056_a(this.field_179835_f));
    }
    
    public void func_180718_a(final INetHandlerPlayClient p_180718_1_) {
        p_180718_1_.handlePlayerPosLook(this);
    }
    
    public double func_148932_c() {
        return this.field_148940_a;
    }
    
    public double func_148928_d() {
        return this.field_148938_b;
    }
    
    public double func_148933_e() {
        return this.field_148939_c;
    }
    
    public float func_148931_f() {
        return this.yaw;
    }
    
    public float func_148930_g() {
        return this.pitch;
    }
    
    public Set func_179834_f() {
        return this.field_179835_f;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.func_180718_a((INetHandlerPlayClient)handler);
    }
    
    public enum EnumFlags
    {
        X("X", 0, 0), 
        Y("Y", 1, 1), 
        Z("Z", 2, 2), 
        Y_ROT("Y_ROT", 3, 3), 
        X_ROT("X_ROT", 4, 4);
        
        private int field_180058_f;
        private static final EnumFlags[] $VALUES;
        private static final String __OBFID = "CL_00002304";
        
        private EnumFlags(final String p_i45992_1_, final int p_i45992_2_, final int p_i45992_3_) {
            this.field_180058_f = p_i45992_3_;
        }
        
        private int func_180055_a() {
            return 1 << this.field_180058_f;
        }
        
        private boolean func_180054_b(final int p_180054_1_) {
            return (p_180054_1_ & this.func_180055_a()) == this.func_180055_a();
        }
        
        public static Set func_180053_a(final int p_180053_0_) {
            final EnumSet var1 = EnumSet.noneOf(EnumFlags.class);
            for (final EnumFlags var5 : values()) {
                if (var5.func_180054_b(p_180053_0_)) {
                    var1.add(var5);
                }
            }
            return var1;
        }
        
        public static int func_180056_a(final Set p_180056_0_) {
            int var1 = 0;
            for (final EnumFlags var3 : p_180056_0_) {
                var1 |= var3.func_180055_a();
            }
            return var1;
        }
        
        static {
            $VALUES = new EnumFlags[] { EnumFlags.X, EnumFlags.Y, EnumFlags.Z, EnumFlags.Y_ROT, EnumFlags.X_ROT };
        }
    }
}
