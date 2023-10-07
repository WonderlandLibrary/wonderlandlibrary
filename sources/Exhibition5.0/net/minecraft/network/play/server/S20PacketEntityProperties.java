// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.INetHandlerPlayClient;
import java.io.IOException;
import java.util.UUID;
import java.util.ArrayList;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.network.PacketBuffer;
import java.util.Iterator;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import java.util.Collection;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.network.Packet;

public class S20PacketEntityProperties implements Packet
{
    private int field_149445_a;
    private final List field_149444_b;
    private static final String __OBFID = "CL_00001341";
    
    public S20PacketEntityProperties() {
        this.field_149444_b = Lists.newArrayList();
    }
    
    public S20PacketEntityProperties(final int p_i45236_1_, final Collection p_i45236_2_) {
        this.field_149444_b = Lists.newArrayList();
        this.field_149445_a = p_i45236_1_;
        for (final IAttributeInstance var4 : p_i45236_2_) {
            this.field_149444_b.add(new Snapshot(var4.getAttribute().getAttributeUnlocalizedName(), var4.getBaseValue(), var4.func_111122_c()));
        }
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.field_149445_a = data.readVarIntFromBuffer();
        for (int var2 = data.readInt(), var3 = 0; var3 < var2; ++var3) {
            final String var4 = data.readStringFromBuffer(64);
            final double var5 = data.readDouble();
            final ArrayList var6 = Lists.newArrayList();
            for (int var7 = data.readVarIntFromBuffer(), var8 = 0; var8 < var7; ++var8) {
                final UUID var9 = data.readUuid();
                var6.add(new AttributeModifier(var9, "Unknown synced attribute modifier", data.readDouble(), data.readByte()));
            }
            this.field_149444_b.add(new Snapshot(var4, var5, var6));
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeVarIntToBuffer(this.field_149445_a);
        data.writeInt(this.field_149444_b.size());
        for (final Snapshot var3 : this.field_149444_b) {
            data.writeString(var3.func_151409_a());
            data.writeDouble(var3.func_151410_b());
            data.writeVarIntToBuffer(var3.func_151408_c().size());
            for (final AttributeModifier var5 : var3.func_151408_c()) {
                data.writeUuid(var5.getID());
                data.writeDouble(var5.getAmount());
                data.writeByte(var5.getOperation());
            }
        }
    }
    
    public void func_180754_a(final INetHandlerPlayClient p_180754_1_) {
        p_180754_1_.handleEntityProperties(this);
    }
    
    public int func_149442_c() {
        return this.field_149445_a;
    }
    
    public List func_149441_d() {
        return this.field_149444_b;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.func_180754_a((INetHandlerPlayClient)handler);
    }
    
    public class Snapshot
    {
        private final String field_151412_b;
        private final double field_151413_c;
        private final Collection field_151411_d;
        private static final String __OBFID = "CL_00001342";
        
        public Snapshot(final String p_i45235_2_, final double p_i45235_3_, final Collection p_i45235_5_) {
            this.field_151412_b = p_i45235_2_;
            this.field_151413_c = p_i45235_3_;
            this.field_151411_d = p_i45235_5_;
        }
        
        public String func_151409_a() {
            return this.field_151412_b;
        }
        
        public double func_151410_b() {
            return this.field_151413_c;
        }
        
        public Collection func_151408_c() {
            return this.field_151411_d;
        }
    }
}
