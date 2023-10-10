// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.minecraft.entities;

public interface EntityType
{
    int getId();
    
    EntityType getParent();
    
    String name();
    
    default boolean is(final EntityType... types) {
        for (final EntityType type : types) {
            if (this == type) {
                return true;
            }
        }
        return false;
    }
    
    default boolean is(final EntityType type) {
        return this == type;
    }
    
    default boolean isOrHasParent(final EntityType type) {
        EntityType parent = this;
        while (parent != type) {
            parent = parent.getParent();
            if (parent == null) {
                return false;
            }
        }
        return true;
    }
}
