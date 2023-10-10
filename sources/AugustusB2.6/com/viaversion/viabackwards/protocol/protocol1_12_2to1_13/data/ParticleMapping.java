// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.data;

import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.type.types.Particle;
import java.util.List;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.Protocol1_12_2To1_13;

public class ParticleMapping
{
    private static final ParticleData[] particles;
    
    public static ParticleData getMapping(final int id) {
        return ParticleMapping.particles[id];
    }
    
    private static ParticleData rewrite(final int replacementId) {
        return new ParticleData(replacementId);
    }
    
    private static ParticleData rewrite(final int replacementId, final ParticleHandler handler) {
        return new ParticleData(replacementId, handler);
    }
    
    static {
        final ParticleHandler blockHandler = new ParticleHandler() {
            @Override
            public int[] rewrite(final Protocol1_12_2To1_13 protocol, final PacketWrapper wrapper) throws Exception {
                return this.rewrite(wrapper.read((Type<Integer>)Type.VAR_INT));
            }
            
            @Override
            public int[] rewrite(final Protocol1_12_2To1_13 protocol, final List<Particle.ParticleData> data) {
                return this.rewrite((int)data.get(0).getValue());
            }
            
            private int[] rewrite(final int newType) {
                final int blockType = Protocol1_12_2To1_13.MAPPINGS.getNewBlockStateId(newType);
                final int type = blockType >> 4;
                final int meta = blockType & 0xF;
                return new int[] { type + (meta << 12) };
            }
            
            @Override
            public boolean isBlockHandler() {
                return true;
            }
        };
        particles = new ParticleData[] { rewrite(16), rewrite(20), rewrite(35), rewrite(37, blockHandler), rewrite(4), rewrite(29), rewrite(9), rewrite(44), rewrite(42), rewrite(19), rewrite(18), rewrite(30, new ParticleHandler() {
                @Override
                public int[] rewrite(final Protocol1_12_2To1_13 protocol, final PacketWrapper wrapper) throws Exception {
                    final float r = wrapper.read((Type<Float>)Type.FLOAT);
                    final float g = wrapper.read((Type<Float>)Type.FLOAT);
                    final float b = wrapper.read((Type<Float>)Type.FLOAT);
                    final float scale = wrapper.read((Type<Float>)Type.FLOAT);
                    wrapper.set(Type.FLOAT, 3, r);
                    wrapper.set(Type.FLOAT, 4, g);
                    wrapper.set(Type.FLOAT, 5, b);
                    wrapper.set(Type.FLOAT, 6, scale);
                    wrapper.set(Type.INT, 1, 0);
                    return null;
                }
                
                @Override
                public int[] rewrite(final Protocol1_12_2To1_13 protocol, final List<Particle.ParticleData> data) {
                    return null;
                }
            }), rewrite(13), rewrite(41), rewrite(10), rewrite(25), rewrite(43), rewrite(15), rewrite(2), rewrite(1), rewrite(46, blockHandler), rewrite(3), rewrite(6), rewrite(26), rewrite(21), rewrite(34), rewrite(14), rewrite(36, new ParticleHandler() {
                @Override
                public int[] rewrite(final Protocol1_12_2To1_13 protocol, final PacketWrapper wrapper) throws Exception {
                    return this.rewrite(protocol, wrapper.read(Type.FLAT_ITEM));
                }
                
                @Override
                public int[] rewrite(final Protocol1_12_2To1_13 protocol, final List<Particle.ParticleData> data) {
                    return this.rewrite(protocol, (Item)data.get(0).getValue());
                }
                
                private int[] rewrite(final Protocol1_12_2To1_13 protocol, final Item newItem) {
                    final Item item = protocol.getItemRewriter().handleItemToClient(newItem);
                    return new int[] { item.identifier(), item.data() };
                }
            }), rewrite(33), rewrite(31), rewrite(12), rewrite(27), rewrite(22), rewrite(23), rewrite(0), rewrite(24), rewrite(39), rewrite(11), rewrite(48), rewrite(12), rewrite(45), rewrite(47), rewrite(7), rewrite(5), rewrite(17), rewrite(4), rewrite(4), rewrite(4), rewrite(18), rewrite(18) };
    }
    
    public interface ParticleHandler
    {
        int[] rewrite(final Protocol1_12_2To1_13 p0, final PacketWrapper p1) throws Exception;
        
        int[] rewrite(final Protocol1_12_2To1_13 p0, final List<Particle.ParticleData> p1);
        
        default boolean isBlockHandler() {
            return false;
        }
    }
    
    public static final class ParticleData
    {
        private final int historyId;
        private final ParticleHandler handler;
        
        private ParticleData(final int historyId, final ParticleHandler handler) {
            this.historyId = historyId;
            this.handler = handler;
        }
        
        private ParticleData(final int historyId) {
            this(historyId, (ParticleHandler)null);
        }
        
        public int[] rewriteData(final Protocol1_12_2To1_13 protocol, final PacketWrapper wrapper) throws Exception {
            if (this.handler == null) {
                return null;
            }
            return this.handler.rewrite(protocol, wrapper);
        }
        
        public int[] rewriteMeta(final Protocol1_12_2To1_13 protocol, final List<Particle.ParticleData> data) {
            if (this.handler == null) {
                return null;
            }
            return this.handler.rewrite(protocol, data);
        }
        
        public int getHistoryId() {
            return this.historyId;
        }
        
        public ParticleHandler getHandler() {
            return this.handler;
        }
    }
}
