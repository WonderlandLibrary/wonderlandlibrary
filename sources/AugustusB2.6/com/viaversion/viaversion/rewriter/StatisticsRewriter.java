// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.rewriter;

import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.minecraft.RegistryType;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.protocol.Protocol;

public class StatisticsRewriter
{
    private final Protocol protocol;
    private final int customStatsCategory = 8;
    
    public StatisticsRewriter(final Protocol protocol) {
        this.protocol = protocol;
    }
    
    public void register(final ClientboundPacketType packetType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                int newSize;
                int size;
                int i;
                int categoryId;
                int statisticId;
                int value;
                RegistryType type;
                IdRewriteFunction statisticsRewriter;
                this.handler(wrapper -> {
                    for (size = (newSize = wrapper.passthrough((Type<Integer>)Type.VAR_INT)), i = 0; i < size; ++i) {
                        categoryId = wrapper.read((Type<Integer>)Type.VAR_INT);
                        statisticId = wrapper.read((Type<Integer>)Type.VAR_INT);
                        value = wrapper.read((Type<Integer>)Type.VAR_INT);
                        if (categoryId == 8 && StatisticsRewriter.this.protocol.getMappingData().getStatisticsMappings() != null) {
                            statisticId = StatisticsRewriter.this.protocol.getMappingData().getStatisticsMappings().getNewId(statisticId);
                            if (statisticId == -1) {
                                --newSize;
                                continue;
                            }
                        }
                        else {
                            type = StatisticsRewriter.this.getRegistryTypeForStatistic(categoryId);
                            if (type != null && (statisticsRewriter = StatisticsRewriter.this.getRewriter(type)) != null) {
                                statisticId = statisticsRewriter.rewrite(statisticId);
                            }
                        }
                        wrapper.write(Type.VAR_INT, categoryId);
                        wrapper.write(Type.VAR_INT, statisticId);
                        wrapper.write(Type.VAR_INT, value);
                    }
                    if (newSize != size) {
                        wrapper.set(Type.VAR_INT, 0, newSize);
                    }
                });
            }
        });
    }
    
    protected IdRewriteFunction getRewriter(final RegistryType type) {
        switch (type) {
            case BLOCK: {
                return (this.protocol.getMappingData().getBlockMappings() != null) ? (id -> this.protocol.getMappingData().getNewBlockId(id)) : null;
            }
            case ITEM: {
                return (this.protocol.getMappingData().getItemMappings() != null) ? (id -> this.protocol.getMappingData().getNewItemId(id)) : null;
            }
            case ENTITY: {
                return (this.protocol.getEntityRewriter() != null) ? (id -> this.protocol.getEntityRewriter().newEntityId(id)) : null;
            }
            default: {
                throw new IllegalArgumentException("Unknown registry type in statistics packet: " + type);
            }
        }
    }
    
    public RegistryType getRegistryTypeForStatistic(final int statisticsId) {
        switch (statisticsId) {
            case 0: {
                return RegistryType.BLOCK;
            }
            case 1:
            case 2:
            case 3:
            case 4:
            case 5: {
                return RegistryType.ITEM;
            }
            case 6:
            case 7: {
                return RegistryType.ENTITY;
            }
            default: {
                return null;
            }
        }
    }
}
