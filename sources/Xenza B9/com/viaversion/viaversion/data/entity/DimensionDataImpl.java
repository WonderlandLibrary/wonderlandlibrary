// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.data.entity;

import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.NumberTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.data.entity.DimensionData;

public final class DimensionDataImpl implements DimensionData
{
    private final int minY;
    private final int height;
    
    public DimensionDataImpl(final int minY, final int height) {
        this.minY = minY;
        this.height = height;
    }
    
    public DimensionDataImpl(final CompoundTag dimensionData) {
        final Tag height = dimensionData.get("height");
        if (!(height instanceof IntTag)) {
            throw new IllegalArgumentException("height missing in dimension data: " + dimensionData);
        }
        this.height = ((NumberTag)height).asInt();
        final Tag minY = dimensionData.get("min_y");
        if (minY instanceof IntTag) {
            this.minY = ((NumberTag)minY).asInt();
            return;
        }
        throw new IllegalArgumentException("min_y missing in dimension data: " + dimensionData);
    }
    
    @Override
    public int minY() {
        return this.minY;
    }
    
    @Override
    public int height() {
        return this.height;
    }
    
    @Override
    public String toString() {
        return "DimensionData{minY=" + this.minY + ", height=" + this.height + '}';
    }
}
