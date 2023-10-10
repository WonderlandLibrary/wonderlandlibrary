// 
// Decompiled by Procyon v0.5.36
// 

package javazoom.jl.decoder;

public final class Equalizer
{
    public static final float BAND_NOT_PRESENT = Float.NEGATIVE_INFINITY;
    public static final Equalizer PASS_THRU_EQ;
    private static final int BANDS = 32;
    private final float[] settings;
    
    public Equalizer() {
        this.settings = new float[32];
    }
    
    public Equalizer(final float[] settings) {
        this.settings = new float[32];
        this.setFrom(settings);
    }
    
    public Equalizer(final EQFunction eq) {
        this.settings = new float[32];
        this.setFrom(eq);
    }
    
    public void setFrom(final float[] eq) {
        this.reset();
        for (int max = (eq.length > 32) ? 32 : eq.length, i = 0; i < max; ++i) {
            this.settings[i] = this.limit(eq[i]);
        }
    }
    
    public void setFrom(final EQFunction eq) {
        this.reset();
        for (int max = 32, i = 0; i < max; ++i) {
            this.settings[i] = this.limit(eq.getBand(i));
        }
    }
    
    public void setFrom(final Equalizer eq) {
        if (eq != this) {
            this.setFrom(eq.settings);
        }
    }
    
    public void reset() {
        for (int i = 0; i < 32; ++i) {
            this.settings[i] = 0.0f;
        }
    }
    
    public int getBandCount() {
        return this.settings.length;
    }
    
    public float setBand(final int band, final float neweq) {
        float eq = 0.0f;
        if (band >= 0 && band < 32) {
            eq = this.settings[band];
            this.settings[band] = this.limit(neweq);
        }
        return eq;
    }
    
    public float getBand(final int band) {
        float eq = 0.0f;
        if (band >= 0 && band < 32) {
            eq = this.settings[band];
        }
        return eq;
    }
    
    private float limit(final float eq) {
        if (eq == Float.NEGATIVE_INFINITY) {
            return eq;
        }
        if (eq > 1.0f) {
            return 1.0f;
        }
        if (eq < -1.0f) {
            return -1.0f;
        }
        return eq;
    }
    
    float[] getBandFactors() {
        final float[] factors = new float[32];
        for (int i = 0, maxCount = 32; i < maxCount; ++i) {
            factors[i] = this.getBandFactor(this.settings[i]);
        }
        return factors;
    }
    
    float getBandFactor(final float eq) {
        if (eq == Float.NEGATIVE_INFINITY) {
            return 0.0f;
        }
        final float f = (float)Math.pow(2.0, eq);
        return f;
    }
    
    static {
        PASS_THRU_EQ = new Equalizer();
    }
    
    public abstract static class EQFunction
    {
        public float getBand(final int band) {
            return 0.0f;
        }
    }
}
