// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.INetHandlerPlayClient;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.entity.DataWatcher;
import java.util.List;
import net.minecraft.network.Packet;

public class S1CPacketEntityMetadata implements Packet
{
    private int field_149379_a;
    private List field_149378_b;
    private static final String __OBFID = "CL_00001326";
    
    public S1CPacketEntityMetadata() {
    }
    
    public S1CPacketEntityMetadata(final int p_i45217_1_, final DataWatcher p_i45217_2_, final boolean p_i45217_3_) {
        this.field_149379_a = p_i45217_1_;
        if (p_i45217_3_) {
            this.field_149378_b = p_i45217_2_.getAllWatched();
        }
        else {
            this.field_149378_b = p_i45217_2_.getChanged();
        }
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.field_149379_a = data.readVarIntFromBuffer();
        this.field_149378_b = DataWatcher.readWatchedListFromPacketBuffer(data);
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeVarIntToBuffer(this.field_149379_a);
        DataWatcher.writeWatchedListToPacketBuffer(this.field_149378_b, data);
    }
    
    public void func_180748_a(final INetHandlerPlayClient p_180748_1_) {
        p_180748_1_.handleEntityMetadata(this);
    }
    
    public List func_149376_c() {
        return this.field_149378_b;
    }
    
    public int func_149375_d() {
        return this.field_149379_a;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.func_180748_a((INetHandlerPlayClient)handler);
    }
}
