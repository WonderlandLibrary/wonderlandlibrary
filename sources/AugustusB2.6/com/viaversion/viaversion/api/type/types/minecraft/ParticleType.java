// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types.minecraft;

import com.viaversion.viaversion.api.data.ParticleMappings;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.minecraft.item.Item;
import java.util.Iterator;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectArrayMap;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;
import com.viaversion.viaversion.api.type.types.Particle;
import com.viaversion.viaversion.api.type.Type;

public class ParticleType extends Type<Particle>
{
    private final Int2ObjectMap<ParticleReader> readers;
    
    public ParticleType(final Int2ObjectMap<ParticleReader> readers) {
        super("Particle", Particle.class);
        this.readers = readers;
    }
    
    public ParticleType() {
        this(new Int2ObjectArrayMap<ParticleReader>());
    }
    
    public ParticleTypeFiller filler(final Protocol<?, ?, ?, ?> protocol) {
        return this.filler(protocol, true);
    }
    
    public ParticleTypeFiller filler(final Protocol<?, ?, ?, ?> protocol, final boolean useMappedNames) {
        return new ParticleTypeFiller((Protocol)protocol, useMappedNames);
    }
    
    @Override
    public void write(final ByteBuf buffer, final Particle object) throws Exception {
        Type.VAR_INT.writePrimitive(buffer, object.getId());
        for (final Particle.ParticleData data : object.getArguments()) {
            data.getType().write(buffer, data.getValue());
        }
    }
    
    @Override
    public Particle read(final ByteBuf buffer) throws Exception {
        final int type = Type.VAR_INT.readPrimitive(buffer);
        final Particle particle = new Particle(type);
        final ParticleReader reader = this.readers.get(type);
        if (reader != null) {
            reader.read(buffer, particle);
        }
        return particle;
    }
    
    public static ParticleReader itemHandler(final Type<Item> itemType) {
        return (buf, particle) -> particle.add(itemType, (Item)itemType.read(buf));
    }
    
    public static final class Readers
    {
        public static final ParticleReader BLOCK;
        public static final ParticleReader ITEM;
        public static final ParticleReader VAR_INT_ITEM;
        public static final ParticleReader DUST;
        public static final ParticleReader DUST_TRANSITION;
        public static final ParticleReader VIBRATION;
        
        static {
            BLOCK = ((buf, particle) -> particle.add(Type.VAR_INT, Type.VAR_INT.readPrimitive(buf)));
            ITEM = ParticleType.itemHandler(Type.FLAT_ITEM);
            VAR_INT_ITEM = ParticleType.itemHandler(Type.FLAT_VAR_INT_ITEM);
            DUST = ((buf, particle) -> {
                particle.add(Type.FLOAT, Type.FLOAT.readPrimitive(buf));
                particle.add(Type.FLOAT, Type.FLOAT.readPrimitive(buf));
                particle.add(Type.FLOAT, Type.FLOAT.readPrimitive(buf));
                particle.add(Type.FLOAT, Type.FLOAT.readPrimitive(buf));
                return;
            });
            DUST_TRANSITION = ((buf, particle) -> {
                particle.add(Type.FLOAT, Type.FLOAT.readPrimitive(buf));
                particle.add(Type.FLOAT, Type.FLOAT.readPrimitive(buf));
                particle.add(Type.FLOAT, Type.FLOAT.readPrimitive(buf));
                particle.add(Type.FLOAT, Type.FLOAT.readPrimitive(buf));
                particle.add(Type.FLOAT, Type.FLOAT.readPrimitive(buf));
                particle.add(Type.FLOAT, Type.FLOAT.readPrimitive(buf));
                particle.add(Type.FLOAT, Type.FLOAT.readPrimitive(buf));
                return;
            });
            String resourceLocation;
            VIBRATION = ((buf, particle) -> {
                particle.add(Type.POSITION1_14, (Position)Type.POSITION1_14.read(buf));
                resourceLocation = Type.STRING.read(buf);
                particle.add(Type.STRING, resourceLocation);
                if (resourceLocation.startsWith("minecraft:")) {
                    resourceLocation = resourceLocation.substring(10);
                }
                if (resourceLocation.equals("block")) {
                    particle.add(Type.POSITION1_14, (Position)Type.POSITION1_14.read(buf));
                }
                else if (resourceLocation.equals("entity")) {
                    particle.add(Type.VAR_INT, Type.VAR_INT.readPrimitive(buf));
                }
                else {
                    Via.getPlatform().getLogger().warning("Unknown vibration path position source type: " + resourceLocation);
                }
                particle.add(Type.VAR_INT, Type.VAR_INT.readPrimitive(buf));
            });
        }
    }
    
    public final class ParticleTypeFiller
    {
        private final ParticleMappings mappings;
        private final boolean useMappedNames;
        
        private ParticleTypeFiller(final Protocol<?, ?, ?, ?> protocol, final boolean useMappedNames) {
            this.mappings = protocol.getMappingData().getParticleMappings();
            this.useMappedNames = useMappedNames;
        }
        
        public ParticleTypeFiller reader(final String identifier, final ParticleReader reader) {
            ParticleType.this.readers.put(this.useMappedNames ? this.mappings.mappedId(identifier) : this.mappings.id(identifier), reader);
            return this;
        }
        
        public ParticleTypeFiller reader(final int id, final ParticleReader reader) {
            ParticleType.this.readers.put(id, reader);
            return this;
        }
    }
    
    @FunctionalInterface
    public interface ParticleReader
    {
        void read(final ByteBuf p0, final Particle p1) throws Exception;
    }
}
