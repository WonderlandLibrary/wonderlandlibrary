// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.util.Iterator;
import java.io.IOException;
import net.minecraft.stats.StatList;
import com.google.common.collect.Maps;
import net.minecraft.network.PacketBuffer;
import net.minecraft.stats.StatBase;
import java.util.Map;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketStatistics implements Packet<INetHandlerPlayClient>
{
    private Map<StatBase, Integer> statisticMap;
    
    public SPacketStatistics() {
    }
    
    public SPacketStatistics(final Map<StatBase, Integer> statisticMapIn) {
        this.statisticMap = statisticMapIn;
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleStatistics(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        final int i = buf.readVarInt();
        this.statisticMap = (Map<StatBase, Integer>)Maps.newHashMap();
        for (int j = 0; j < i; ++j) {
            final StatBase statbase = StatList.getOneShotStat(buf.readString(32767));
            final int k = buf.readVarInt();
            if (statbase != null) {
                this.statisticMap.put(statbase, k);
            }
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarInt(this.statisticMap.size());
        for (final Map.Entry<StatBase, Integer> entry : this.statisticMap.entrySet()) {
            buf.writeString(entry.getKey().statId);
            buf.writeVarInt(entry.getValue());
        }
    }
    
    public Map<StatBase, Integer> getStatisticMap() {
        return this.statisticMap;
    }
}
