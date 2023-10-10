// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.minecraft.blockentity;

import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;

public interface BlockEntity
{
    default byte sectionX() {
        return (byte)(this.packedXZ() >> 4 & 0xF);
    }
    
    default byte sectionZ() {
        return (byte)(this.packedXZ() & 0xF);
    }
    
    byte packedXZ();
    
    short y();
    
    int typeId();
    
    CompoundTag tag();
}
