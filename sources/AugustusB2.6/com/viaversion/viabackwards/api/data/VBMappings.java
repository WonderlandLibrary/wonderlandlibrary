// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.api.data;

import com.viaversion.viaversion.api.data.MappingDataLoader;
import java.util.Arrays;
import com.viaversion.viaversion.api.data.Mappings;
import com.viaversion.viaversion.api.data.IntArrayMappings;

public final class VBMappings extends IntArrayMappings
{
    private VBMappings(final int[] oldToNew, final int mappedIds) {
        super(oldToNew, mappedIds);
    }
    
    public static Mappings.Builder<VBMappings> vbBuilder() {
        return new Builder((Mappings.MappingsSupplier)VBMappings::new);
    }
    
    public static final class Builder extends Mappings.Builder<VBMappings>
    {
        private Builder(final Mappings.MappingsSupplier<VBMappings> supplier) {
            super(supplier);
        }
        
        @Override
        public VBMappings build() {
            final int size = (this.size != -1) ? this.size : this.size(this.unmapped);
            final int mappedSize = (this.mappedSize != -1) ? this.mappedSize : this.size(this.mapped);
            final int[] mappings = new int[size];
            Arrays.fill(mappings, -1);
            if (this.unmapped.isJsonArray()) {
                if (this.mapped.isJsonObject()) {
                    VBMappingDataLoader.mapIdentifiers(mappings, this.toJsonObject(this.unmapped.getAsJsonArray()), this.mapped.getAsJsonObject(), this.diffMappings, this.warnOnMissing);
                }
                else {
                    MappingDataLoader.mapIdentifiers(mappings, this.unmapped.getAsJsonArray(), this.mapped.getAsJsonArray(), this.diffMappings, this.warnOnMissing);
                }
            }
            else if (this.mapped.isJsonArray()) {
                VBMappingDataLoader.mapIdentifiers(mappings, this.unmapped.getAsJsonObject(), this.toJsonObject(this.mapped.getAsJsonArray()), this.diffMappings, this.warnOnMissing);
            }
            else {
                VBMappingDataLoader.mapIdentifiers(mappings, this.unmapped.getAsJsonObject(), this.mapped.getAsJsonObject(), this.diffMappings, this.warnOnMissing);
            }
            return (VBMappings)this.supplier.supply(mappings, mappedSize);
        }
    }
}
