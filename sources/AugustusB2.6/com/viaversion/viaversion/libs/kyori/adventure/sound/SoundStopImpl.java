// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.sound;

import com.viaversion.viaversion.libs.kyori.adventure.key.Key;
import com.viaversion.viaversion.libs.kyori.examination.Examiner;
import com.viaversion.viaversion.libs.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import java.util.Objects;
import org.jetbrains.annotations.Nullable;

abstract class SoundStopImpl implements SoundStop
{
    static final SoundStop ALL;
    private final Sound.Source source;
    
    SoundStopImpl(final Sound.Source source) {
        this.source = source;
    }
    
    @Override
    public Sound.Source source() {
        return this.source;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SoundStopImpl)) {
            return false;
        }
        final SoundStopImpl that = (SoundStopImpl)other;
        return Objects.equals(this.sound(), that.sound()) && Objects.equals(this.source, that.source);
    }
    
    @Override
    public int hashCode() {
        int result = Objects.hashCode(this.sound());
        result = 31 * result + Objects.hashCode(this.source);
        return result;
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of((ExaminableProperty[])new ExaminableProperty[] { ExaminableProperty.of("name", this.sound()), ExaminableProperty.of("source", this.source) });
    }
    
    @Override
    public String toString() {
        return this.examine((Examiner<String>)StringExaminer.simpleEscaping());
    }
    
    static {
        ALL = new SoundStopImpl() {
            @Nullable
            @Override
            public Key sound() {
                return null;
            }
        };
    }
}
