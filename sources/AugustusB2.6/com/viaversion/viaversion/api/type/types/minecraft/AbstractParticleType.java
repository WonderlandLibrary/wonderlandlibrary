// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types.minecraft;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.minecraft.item.Item;
import java.util.Iterator;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;
import com.viaversion.viaversion.api.type.types.Particle;
import com.viaversion.viaversion.api.type.Type;

@Deprecated
public abstract class AbstractParticleType extends Type<Particle>
{
    protected final Int2ObjectMap<ParticleReader> readers;
    
    protected AbstractParticleType() {
        super("Particle", Particle.class);
        this.readers = new Int2ObjectOpenHashMap<ParticleReader>();
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
    
    protected ParticleReader blockHandler() {
        return (buf, particle) -> particle.getArguments().add(new Particle.ParticleData(Type.VAR_INT, Type.VAR_INT.readPrimitive(buf)));
    }
    
    protected ParticleReader itemHandler(final Type<Item> itemType) {
        return (buf, particle) -> particle.getArguments().add(new Particle.ParticleData(itemType, itemType.read(buf)));
    }
    
    protected ParticleReader dustHandler() {
        return (buf, particle) -> {
            particle.getArguments().add(new Particle.ParticleData(Type.FLOAT, Type.FLOAT.readPrimitive(buf)));
            particle.getArguments().add(new Particle.ParticleData(Type.FLOAT, Type.FLOAT.readPrimitive(buf)));
            particle.getArguments().add(new Particle.ParticleData(Type.FLOAT, Type.FLOAT.readPrimitive(buf)));
            particle.getArguments().add(new Particle.ParticleData(Type.FLOAT, Type.FLOAT.readPrimitive(buf)));
        };
    }
    
    protected ParticleReader dustTransitionHandler() {
        return (buf, particle) -> {
            particle.getArguments().add(new Particle.ParticleData(Type.FLOAT, Type.FLOAT.readPrimitive(buf)));
            particle.getArguments().add(new Particle.ParticleData(Type.FLOAT, Type.FLOAT.readPrimitive(buf)));
            particle.getArguments().add(new Particle.ParticleData(Type.FLOAT, Type.FLOAT.readPrimitive(buf)));
            particle.getArguments().add(new Particle.ParticleData(Type.FLOAT, Type.FLOAT.readPrimitive(buf)));
            particle.getArguments().add(new Particle.ParticleData(Type.FLOAT, Type.FLOAT.readPrimitive(buf)));
            particle.getArguments().add(new Particle.ParticleData(Type.FLOAT, Type.FLOAT.readPrimitive(buf)));
            particle.getArguments().add(new Particle.ParticleData(Type.FLOAT, Type.FLOAT.readPrimitive(buf)));
        };
    }
    
    protected ParticleReader vibrationHandler(final Type<Position> positionType) {
        final String resourceLocation;
        return (buf, particle) -> {
            particle.getArguments().add(new Particle.ParticleData(positionType, positionType.read(buf)));
            resourceLocation = (String)Type.STRING.read(buf);
            if (resourceLocation.equals("block")) {
                particle.getArguments().add(new Particle.ParticleData(positionType, positionType.read(buf)));
            }
            else if (resourceLocation.equals("entity")) {
                particle.getArguments().add(new Particle.ParticleData(Type.VAR_INT, Type.VAR_INT.readPrimitive(buf)));
            }
            else {
                Via.getPlatform().getLogger().warning("Unknown vibration path position source type: " + resourceLocation);
            }
            particle.getArguments().add(new Particle.ParticleData(Type.VAR_INT, Type.VAR_INT.readPrimitive(buf)));
        };
    }
    
    @FunctionalInterface
    public interface ParticleReader
    {
        void read(final ByteBuf p0, final Particle p1) throws Exception;
    }
}
