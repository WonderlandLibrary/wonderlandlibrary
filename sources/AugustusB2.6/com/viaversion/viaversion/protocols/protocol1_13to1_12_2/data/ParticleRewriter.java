// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.data;

import java.util.ArrayList;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.packets.WorldPackets;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.Protocol1_13To1_12_2;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.minecraft.item.DataItem;
import java.util.concurrent.ThreadLocalRandom;
import com.viaversion.viaversion.api.type.Type;
import java.util.Arrays;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.type.types.Particle;
import java.util.List;

public class ParticleRewriter
{
    private static final List<NewParticle> particles;
    
    public static Particle rewriteParticle(final int particleId, final Integer[] data) {
        if (particleId >= ParticleRewriter.particles.size()) {
            Via.getPlatform().getLogger().severe("Failed to transform particles with id " + particleId + " and data " + Arrays.toString(data));
            return null;
        }
        final NewParticle rewrite = ParticleRewriter.particles.get(particleId);
        return rewrite.handle(new Particle(rewrite.getId()), data);
    }
    
    private static void add(final int newId) {
        ParticleRewriter.particles.add(new NewParticle(newId, null));
    }
    
    private static void add(final int newId, final ParticleDataHandler dataHandler) {
        ParticleRewriter.particles.add(new NewParticle(newId, dataHandler));
    }
    
    private static ParticleDataHandler reddustHandler() {
        return new ParticleDataHandler() {
            @Override
            public Particle handler(final Particle particle, final Integer[] data) {
                particle.getArguments().add(new Particle.ParticleData(Type.FLOAT, randomBool() ? 1.0f : 0.0f));
                particle.getArguments().add(new Particle.ParticleData(Type.FLOAT, 0.0f));
                particle.getArguments().add(new Particle.ParticleData(Type.FLOAT, randomBool() ? 1.0f : 0.0f));
                particle.getArguments().add(new Particle.ParticleData(Type.FLOAT, 1.0f));
                return particle;
            }
        };
    }
    
    private static boolean randomBool() {
        return ThreadLocalRandom.current().nextBoolean();
    }
    
    private static ParticleDataHandler iconcrackHandler() {
        return new ParticleDataHandler() {
            @Override
            public Particle handler(final Particle particle, final Integer[] data) {
                Item item;
                if (data.length == 1) {
                    item = new DataItem(data[0], (byte)1, (short)0, null);
                }
                else {
                    if (data.length != 2) {
                        return particle;
                    }
                    item = new DataItem(data[0], (byte)1, data[1].shortValue(), null);
                }
                Via.getManager().getProtocolManager().getProtocol(Protocol1_13To1_12_2.class).getItemRewriter().handleItemToClient(item);
                particle.getArguments().add(new Particle.ParticleData(Type.FLAT_ITEM, item));
                return particle;
            }
        };
    }
    
    private static ParticleDataHandler blockHandler() {
        return new ParticleDataHandler() {
            @Override
            public Particle handler(final Particle particle, final Integer[] data) {
                final int value = data[0];
                final int combined = (value & 0xFFF) << 4 | (value >> 12 & 0xF);
                final int newId = WorldPackets.toNewId(combined);
                particle.getArguments().add(new Particle.ParticleData(Type.VAR_INT, newId));
                return particle;
            }
        };
    }
    
    static {
        particles = new ArrayList<NewParticle>();
        add(34);
        add(19);
        add(18);
        add(21);
        add(4);
        add(43);
        add(22);
        add(42);
        add(42);
        add(6);
        add(14);
        add(37);
        add(30);
        add(12);
        add(26);
        add(17);
        add(0);
        add(44);
        add(10);
        add(9);
        add(1);
        add(24);
        add(32);
        add(33);
        add(35);
        add(15);
        add(23);
        add(31);
        add(-1);
        add(5);
        add(11, reddustHandler());
        add(29);
        add(34);
        add(28);
        add(25);
        add(2);
        add(27, iconcrackHandler());
        add(3, blockHandler());
        add(3, blockHandler());
        add(36);
        add(-1);
        add(13);
        add(8);
        add(16);
        add(7);
        add(40);
        add(20, blockHandler());
        add(41);
        add(38);
    }
    
    private static class NewParticle
    {
        private final int id;
        private final ParticleDataHandler handler;
        
        public NewParticle(final int id, final ParticleDataHandler handler) {
            this.id = id;
            this.handler = handler;
        }
        
        public Particle handle(final Particle particle, final Integer[] data) {
            if (this.handler != null) {
                return this.handler.handler(particle, data);
            }
            return particle;
        }
        
        public int getId() {
            return this.id;
        }
        
        public ParticleDataHandler getHandler() {
            return this.handler;
        }
    }
    
    interface ParticleDataHandler
    {
        Particle handler(final Particle p0, final Integer[] p1);
    }
}
