// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types.minecraft;

import com.viaversion.viaversion.api.type.Type;

@Deprecated
public class Particle1_13Type extends AbstractParticleType
{
    public Particle1_13Type() {
        this.readers.put(3, this.blockHandler());
        this.readers.put(20, this.blockHandler());
        this.readers.put(11, this.dustHandler());
        this.readers.put(27, this.itemHandler(Type.FLAT_ITEM));
    }
}
