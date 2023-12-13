/*
 * Decompiled with CFR 0.152.
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

public class S37PacketStatistics
implements Packet<INetHandlerPlayClient> {
    private Map<StatBase, Integer> field_148976_a;

    public S37PacketStatistics() {
    }

    public S37PacketStatistics(Map<StatBase, Integer> p_i45173_1_) {
        this.field_148976_a = p_i45173_1_;
    }

    @Override
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleStatistics(this);
    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        int i2 = buf.readVarIntFromBuffer();
        this.field_148976_a = Maps.newHashMap();
        for (int j2 = 0; j2 < i2; ++j2) {
            StatBase statbase = StatList.getOneShotStat(buf.readStringFromBuffer(Short.MAX_VALUE));
            int k2 = buf.readVarIntFromBuffer();
            if (statbase == null) continue;
            this.field_148976_a.put(statbase, k2);
        }
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.field_148976_a.size());
        for (Map.Entry<StatBase, Integer> entry : this.field_148976_a.entrySet()) {
            buf.writeString(entry.getKey().statId);
            buf.writeVarIntToBuffer(entry.getValue());
        }
    }

    public Map<StatBase, Integer> func_148974_c() {
        return this.field_148976_a;
    }
}

