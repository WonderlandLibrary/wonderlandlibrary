// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.data;

import java.util.Arrays;
import com.viaversion.viaversion.libs.gson.JsonArray;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.libs.gson.JsonElement;

public interface Mappings
{
    int getNewId(final int p0);
    
    void setNewId(final int p0, final int p1);
    
    int size();
    
    int mappedSize();
    
    default <T extends Mappings> Builder<T> builder(final MappingsSupplier<T> supplier) {
        return new Builder<T>(supplier);
    }
    
    public static class Builder<T extends Mappings>
    {
        protected final MappingsSupplier<T> supplier;
        protected JsonElement unmapped;
        protected JsonElement mapped;
        protected JsonObject diffMappings;
        protected int mappedSize;
        protected int size;
        protected boolean warnOnMissing;
        
        protected Builder(final MappingsSupplier<T> supplier) {
            this.mappedSize = -1;
            this.size = -1;
            this.warnOnMissing = true;
            this.supplier = supplier;
        }
        
        public Builder<T> customEntrySize(final int size) {
            this.size = size;
            return this;
        }
        
        public Builder<T> customMappedSize(final int size) {
            this.mappedSize = size;
            return this;
        }
        
        public Builder<T> warnOnMissing(final boolean warnOnMissing) {
            this.warnOnMissing = warnOnMissing;
            return this;
        }
        
        public Builder<T> unmapped(final JsonArray unmappedArray) {
            this.unmapped = unmappedArray;
            return this;
        }
        
        public Builder<T> unmapped(final JsonObject unmappedObject) {
            this.unmapped = unmappedObject;
            return this;
        }
        
        public Builder<T> mapped(final JsonArray mappedArray) {
            this.mapped = mappedArray;
            return this;
        }
        
        public Builder<T> mapped(final JsonObject mappedObject) {
            this.mapped = mappedObject;
            return this;
        }
        
        public Builder<T> diffMappings(final JsonObject diffMappings) {
            this.diffMappings = diffMappings;
            return this;
        }
        
        public T build() {
            final int size = (this.size != -1) ? this.size : this.size(this.unmapped);
            final int mappedSize = (this.mappedSize != -1) ? this.mappedSize : this.size(this.mapped);
            final int[] mappings = new int[size];
            Arrays.fill(mappings, -1);
            if (this.unmapped.isJsonArray()) {
                if (this.mapped.isJsonObject()) {
                    MappingDataLoader.mapIdentifiers(mappings, this.toJsonObject(this.unmapped.getAsJsonArray()), this.mapped.getAsJsonObject(), this.diffMappings, this.warnOnMissing);
                }
                else {
                    MappingDataLoader.mapIdentifiers(mappings, this.unmapped.getAsJsonArray(), this.mapped.getAsJsonArray(), this.diffMappings, this.warnOnMissing);
                }
            }
            else if (this.mapped.isJsonArray()) {
                MappingDataLoader.mapIdentifiers(mappings, this.unmapped.getAsJsonObject(), this.toJsonObject(this.mapped.getAsJsonArray()), this.diffMappings, this.warnOnMissing);
            }
            else {
                MappingDataLoader.mapIdentifiers(mappings, this.unmapped.getAsJsonObject(), this.mapped.getAsJsonObject(), this.diffMappings, this.warnOnMissing);
            }
            return this.supplier.supply(mappings, mappedSize);
        }
        
        protected int size(final JsonElement element) {
            return element.isJsonObject() ? element.getAsJsonObject().size() : element.getAsJsonArray().size();
        }
        
        protected JsonObject toJsonObject(final JsonArray array) {
            final JsonObject object = new JsonObject();
            for (int i = 0; i < array.size(); ++i) {
                final JsonElement element = array.get(i);
                object.add(Integer.toString(i), element);
            }
            return object;
        }
    }
    
    @FunctionalInterface
    public interface MappingsSupplier<T extends Mappings>
    {
        T supply(final int[] p0, final int p1);
    }
}
