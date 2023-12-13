// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.network.play.INetHandlerPlayClient;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;

public class S49PacketUpdateEntityNBT implements Packet
{
    private int field_179766_a;
    private NBTTagCompound field_179765_b;
    private static final String __OBFID = "CL_00002301";
    
    public S49PacketUpdateEntityNBT() {
    }
    
    public S49PacketUpdateEntityNBT(final int p_i45979_1_, final NBTTagCompound p_i45979_2_) {
        this.field_179766_a = p_i45979_1_;
        this.field_179765_b = p_i45979_2_;
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.field_179766_a = data.readVarIntFromBuffer();
        this.field_179765_b = data.readNBTTagCompoundFromBuffer();
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeVarIntToBuffer(this.field_179766_a);
        data.writeNBTTagCompoundToBuffer(this.field_179765_b);
    }
    
    public void func_179762_a(final INetHandlerPlayClient p_179762_1_) {
        p_179762_1_.func_175097_a(this);
    }
    
    public NBTTagCompound func_179763_a() {
        return this.field_179765_b;
    }
    
    public Entity func_179764_a(final World worldIn) {
        return worldIn.getEntityByID(this.field_179766_a);
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.func_179762_a((INetHandlerPlayClient)handler);
    }
}
