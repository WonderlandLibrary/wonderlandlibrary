// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.util;

import com.viaversion.viaversion.libs.kyori.examination.Examiner;
import com.viaversion.viaversion.libs.kyori.examination.string.StringExaminer;
import java.util.Objects;
import org.jetbrains.annotations.Nullable;

final class HSVLikeImpl implements HSVLike
{
    private final float h;
    private final float s;
    private final float v;
    
    HSVLikeImpl(final float h, final float s, final float v) {
        this.h = h;
        this.s = s;
        this.v = v;
    }
    
    @Override
    public float h() {
        return this.h;
    }
    
    @Override
    public float s() {
        return this.s;
    }
    
    @Override
    public float v() {
        return this.v;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof HSVLikeImpl)) {
            return false;
        }
        final HSVLikeImpl that = (HSVLikeImpl)other;
        return ShadyPines.equals(that.h, this.h) && ShadyPines.equals(that.s, this.s) && ShadyPines.equals(that.v, this.v);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.h, this.s, this.v);
    }
    
    @Override
    public String toString() {
        return this.examine((Examiner<String>)StringExaminer.simpleEscaping());
    }
}
