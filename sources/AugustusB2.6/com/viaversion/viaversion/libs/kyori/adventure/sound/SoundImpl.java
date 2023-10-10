// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.sound;

import com.viaversion.viaversion.libs.kyori.examination.Examiner;
import com.viaversion.viaversion.libs.kyori.examination.string.StringExaminer;
import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import com.viaversion.viaversion.libs.kyori.adventure.util.ShadyPines;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

abstract class SoundImpl implements Sound
{
    static final Emitter EMITTER_SELF;
    private final Source source;
    private final float volume;
    private final float pitch;
    private SoundStop stop;
    
    SoundImpl(@NotNull final Source source, final float volume, final float pitch) {
        this.source = source;
        this.volume = volume;
        this.pitch = pitch;
    }
    
    @NotNull
    @Override
    public Source source() {
        return this.source;
    }
    
    @Override
    public float volume() {
        return this.volume;
    }
    
    @Override
    public float pitch() {
        return this.pitch;
    }
    
    @NotNull
    @Override
    public SoundStop asStop() {
        if (this.stop == null) {
            this.stop = SoundStop.namedOnSource(this.name(), this.source());
        }
        return this.stop;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SoundImpl)) {
            return false;
        }
        final SoundImpl that = (SoundImpl)other;
        return this.name().equals(that.name()) && this.source == that.source && ShadyPines.equals(this.volume, that.volume) && ShadyPines.equals(this.pitch, that.pitch);
    }
    
    @Override
    public int hashCode() {
        int result = this.name().hashCode();
        result = 31 * result + this.source.hashCode();
        result = 31 * result + Float.hashCode(this.volume);
        result = 31 * result + Float.hashCode(this.pitch);
        return result;
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of((ExaminableProperty[])new ExaminableProperty[] { ExaminableProperty.of("name", this.name()), ExaminableProperty.of("source", this.source), ExaminableProperty.of("volume", this.volume), ExaminableProperty.of("pitch", this.pitch) });
    }
    
    @Override
    public String toString() {
        return this.examine((Examiner<String>)StringExaminer.simpleEscaping());
    }
    
    static {
        EMITTER_SELF = new Emitter() {
            @Override
            public String toString() {
                return "SelfSoundEmitter";
            }
        };
    }
}
