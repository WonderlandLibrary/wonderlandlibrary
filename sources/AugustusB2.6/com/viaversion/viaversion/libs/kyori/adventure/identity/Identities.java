// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.identity;

import org.jetbrains.annotations.NotNull;
import java.util.UUID;

final class Identities
{
    static final Identity NIL;
    
    private Identities() {
    }
    
    static {
        NIL = new Identity() {
            private final UUID uuid = new UUID(0L, 0L);
            
            @NotNull
            @Override
            public UUID uuid() {
                return this.uuid;
            }
            
            @Override
            public String toString() {
                return "Identity.nil()";
            }
            
            @Override
            public boolean equals(final Object that) {
                return this == that;
            }
            
            @Override
            public int hashCode() {
                return 0;
            }
        };
    }
}
