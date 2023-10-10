// 
// Decompiled by Procyon v0.5.36
// 

package javazoom.jl.decoder;

public abstract class Obuffer
{
    public static final int OBUFFERSIZE = 2304;
    public static final int MAXCHANNELS = 2;
    
    public abstract void append(final int p0, final short p1);
    
    public void appendSamples(final int channel, final float[] f) {
        int i = 0;
        while (i < 32) {
            final short s = this.clip(f[i++]);
            this.append(channel, s);
        }
    }
    
    private final short clip(final float sample) {
        return (short)((sample > 32767.0f) ? 32767 : ((sample < -32768.0f) ? -32768 : ((short)sample)));
    }
    
    public abstract void write_buffer(final int p0);
    
    public abstract void close();
    
    public abstract void clear_buffer();
    
    public abstract void set_stop_flag();
}
