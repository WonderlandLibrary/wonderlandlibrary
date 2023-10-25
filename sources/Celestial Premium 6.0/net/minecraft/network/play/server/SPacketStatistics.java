/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.play.server;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Map;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;

public class SPacketStatistics
implements Packet<INetHandlerPlayClient> {
    private Map<StatBase, Integer> statisticMap;

    public SPacketStatistics() {
    }

    public SPacketStatistics(Map<StatBase, Integer> statisticMapIn) {
        this.statisticMap = statisticMapIn;
    }

    @Override
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleStatistics(this);
    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        int i = buf.readVarIntFromBuffer();
        this.statisticMap = Maps.newHashMap();
        for (int j = 0; j < i; ++j) {
            StatBase statbase = StatList.getOneShotStat(buf.readStringFromBuffer(32767));
            int k = buf.readVarIntFromBuffer();
            if (statbase == null) continue;
            this.statisticMap.put(statbase, k);
        }
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.statisticMap.size());
        for (Map.Entry<StatBase, Integer> entry : this.statisticMap.entrySet()) {
            buf.writeString(entry.getKey().statId);
            buf.writeVarIntToBuffer(entry.getValue());
        }
    }

    public Map<StatBase, Integer> getStatisticMap() {
        return this.statisticMap;
    }
}

