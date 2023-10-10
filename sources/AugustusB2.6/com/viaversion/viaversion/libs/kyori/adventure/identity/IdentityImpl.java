// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.identity;

import org.jetbrains.annotations.Nullable;
import com.viaversion.viaversion.libs.kyori.examination.Examiner;
import com.viaversion.viaversion.libs.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;
import com.viaversion.viaversion.libs.kyori.examination.Examinable;

final class IdentityImpl implements Examinable, Identity
{
    private final UUID uuid;
    
    IdentityImpl(final UUID uuid) {
        this.uuid = uuid;
    }
    
    @NotNull
    @Override
    public UUID uuid() {
        return this.uuid;
    }
    
    @Override
    public String toString() {
        return this.examine((Examiner<String>)StringExaminer.simpleEscaping());
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Identity)) {
            return false;
        }
        final Identity that = (Identity)other;
        return this.uuid.equals(that.uuid());
    }
    
    @Override
    public int hashCode() {
        return this.uuid.hashCode();
    }
}
