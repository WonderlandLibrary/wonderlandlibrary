// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.util.Iterator;
import java.io.IOException;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import com.google.common.collect.Maps;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import java.util.Map;
import net.minecraft.network.Packet;

public class S37PacketStatistics implements Packet
{
    private Map field_148976_a;
    private static final String __OBFID = "CL_00001283";
    
    public S37PacketStatistics() {
    }
    
    public S37PacketStatistics(final Map p_i45173_1_) {
        this.field_148976_a = p_i45173_1_;
    }
    
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleStatistics(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        final int var2 = data.readVarIntFromBuffer();
        this.field_148976_a = Maps.newHashMap();
        for (int var3 = 0; var3 < var2; ++var3) {
            final StatBase var4 = StatList.getOneShotStat(data.readStringFromBuffer(32767));
            final int var5 = data.readVarIntFromBuffer();
            if (var4 != null) {
                this.field_148976_a.put(var4, var5);
            }
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeVarIntToBuffer(this.field_148976_a.size());
        for (final Map.Entry var3 : this.field_148976_a.entrySet()) {
            data.writeString(var3.getKey().statId);
            data.writeVarIntToBuffer(var3.getValue());
        }
    }
    
    public Map func_148974_c() {
        return this.field_148976_a;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
