// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.common;

import oshi.annotation.concurrent.Immutable;
import oshi.hardware.SoundCard;

@Immutable
public abstract class AbstractSoundCard implements SoundCard
{
    private String kernelVersion;
    private String name;
    private String codec;
    
    protected AbstractSoundCard(final String kernelVersion, final String name, final String codec) {
        this.kernelVersion = kernelVersion;
        this.name = name;
        this.codec = codec;
    }
    
    @Override
    public String getDriverVersion() {
        return this.kernelVersion;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public String getCodec() {
        return this.codec;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SoundCard@");
        builder.append(Integer.toHexString(this.hashCode()));
        builder.append(" [name=");
        builder.append(this.name);
        builder.append(", kernelVersion=");
        builder.append(this.kernelVersion);
        builder.append(", codec=");
        builder.append(this.codec);
        builder.append(']');
        return builder.toString();
    }
}
