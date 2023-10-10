// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.api.entities.storage;

import java.util.Iterator;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import java.util.List;

public final class WrappedMetadata
{
    private final List<Metadata> metadataList;
    
    public WrappedMetadata(final List<Metadata> metadataList) {
        this.metadataList = metadataList;
    }
    
    public boolean has(final Metadata data) {
        return this.metadataList.contains(data);
    }
    
    public void remove(final Metadata data) {
        this.metadataList.remove(data);
    }
    
    public void remove(final int index) {
        this.metadataList.removeIf(meta -> meta.id() == index);
    }
    
    public void add(final Metadata data) {
        this.metadataList.add(data);
    }
    
    public Metadata get(final int index) {
        for (final Metadata meta : this.metadataList) {
            if (index == meta.id()) {
                return meta;
            }
        }
        return null;
    }
    
    public List<Metadata> metadataList() {
        return this.metadataList;
    }
    
    @Override
    public String toString() {
        return "MetaStorage{metaDataList=" + this.metadataList + '}';
    }
}
