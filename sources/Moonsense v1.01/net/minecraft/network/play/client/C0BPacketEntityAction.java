// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.INetHandlerPlayServer;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;

public class C0BPacketEntityAction implements Packet
{
    private int field_149517_a;
    private Action field_149515_b;
    private int field_149516_c;
    private static final String __OBFID = "CL_00001366";
    
    public C0BPacketEntityAction() {
    }
    
    public C0BPacketEntityAction(final Entity p_i45937_1_, final Action p_i45937_2_) {
        this(p_i45937_1_, p_i45937_2_, 0);
    }
    
    public C0BPacketEntityAction(final Entity p_i45938_1_, final Action p_i45938_2_, final int p_i45938_3_) {
        this.field_149517_a = p_i45938_1_.getEntityId();
        this.field_149515_b = p_i45938_2_;
        this.field_149516_c = p_i45938_3_;
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.field_149517_a = data.readVarIntFromBuffer();
        this.field_149515_b = (Action)data.readEnumValue(Action.class);
        this.field_149516_c = data.readVarIntFromBuffer();
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeVarIntToBuffer(this.field_149517_a);
        data.writeEnumValue(this.field_149515_b);
        data.writeVarIntToBuffer(this.field_149516_c);
    }
    
    public void func_180765_a(final INetHandlerPlayServer p_180765_1_) {
        p_180765_1_.processEntityAction(this);
    }
    
    public Action func_180764_b() {
        return this.field_149515_b;
    }
    
    public int func_149512_e() {
        return this.field_149516_c;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.func_180765_a((INetHandlerPlayServer)handler);
    }
    
    public enum Action
    {
        START_SNEAKING("START_SNEAKING", 0, "START_SNEAKING", 0), 
        STOP_SNEAKING("STOP_SNEAKING", 1, "STOP_SNEAKING", 1), 
        STOP_SLEEPING("STOP_SLEEPING", 2, "STOP_SLEEPING", 2), 
        START_SPRINTING("START_SPRINTING", 3, "START_SPRINTING", 3), 
        STOP_SPRINTING("STOP_SPRINTING", 4, "STOP_SPRINTING", 4), 
        RIDING_JUMP("RIDING_JUMP", 5, "RIDING_JUMP", 5), 
        OPEN_INVENTORY("OPEN_INVENTORY", 6, "OPEN_INVENTORY", 6);
        
        private static final Action[] $VALUES;
        private static final String __OBFID = "CL_00002283";
        
        static {
            $VALUES = new Action[] { Action.START_SNEAKING, Action.STOP_SNEAKING, Action.STOP_SLEEPING, Action.START_SPRINTING, Action.STOP_SPRINTING, Action.RIDING_JUMP, Action.OPEN_INVENTORY };
        }
        
        private Action(final String name, final int ordinal, final String p_i45936_1_, final int p_i45936_2_) {
        }
    }
}
