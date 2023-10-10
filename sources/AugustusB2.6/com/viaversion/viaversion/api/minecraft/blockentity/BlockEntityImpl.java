// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.minecraft.blockentity;

import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;

public final class BlockEntityImpl implements BlockEntity
{
    private final byte packedXZ;
    private final short y;
    private final int typeId;
    private final CompoundTag tag;
    
    public BlockEntityImpl(final byte packedXZ, final short y, final int typeId, final CompoundTag tag) {
        this.packedXZ = packedXZ;
        this.y = y;
        this.typeId = typeId;
        this.tag = tag;
    }
    
    @Override
    public byte packedXZ() {
        return this.packedXZ;
    }
    
    @Override
    public short y() {
        return this.y;
    }
    
    @Override
    public int typeId() {
        return this.typeId;
    }
    
    @Override
    public CompoundTag tag() {
        return this.tag;
    }
}
