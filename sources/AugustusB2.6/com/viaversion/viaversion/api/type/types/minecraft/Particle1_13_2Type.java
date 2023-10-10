// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types.minecraft;

import java.util.Iterator;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.type.types.Particle;
import com.viaversion.viaversion.api.type.Type;

@Deprecated
public class Particle1_13_2Type extends Type<Particle>
{
    public Particle1_13_2Type() {
        super("Particle", Particle.class);
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
        switch (type) {
            case 3:
            case 20: {
                particle.getArguments().add(new Particle.ParticleData(Type.VAR_INT, Type.VAR_INT.readPrimitive(buffer)));
                break;
            }
            case 11: {
                particle.getArguments().add(new Particle.ParticleData(Type.FLOAT, Type.FLOAT.readPrimitive(buffer)));
                particle.getArguments().add(new Particle.ParticleData(Type.FLOAT, Type.FLOAT.readPrimitive(buffer)));
                particle.getArguments().add(new Particle.ParticleData(Type.FLOAT, Type.FLOAT.readPrimitive(buffer)));
                particle.getArguments().add(new Particle.ParticleData(Type.FLOAT, Type.FLOAT.readPrimitive(buffer)));
                break;
            }
            case 27: {
                particle.getArguments().add(new Particle.ParticleData(Type.FLAT_VAR_INT_ITEM, Type.FLAT_VAR_INT_ITEM.read(buffer)));
                break;
            }
        }
        return particle;
    }
}
