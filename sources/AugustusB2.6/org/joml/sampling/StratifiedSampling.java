// 
// Decompiled by Procyon v0.5.36
// 

package org.joml.sampling;

import org.joml.Random;

public class StratifiedSampling
{
    private final Random rnd;
    
    public StratifiedSampling(final long seed) {
        this.rnd = new Random(seed);
    }
    
    public void generateRandom(final int n, final Callback2d callback) {
        for (int y = 0; y < n; ++y) {
            for (int x = 0; x < n; ++x) {
                final float sampleX = (this.rnd.nextFloat() / n + x / (float)n) * 2.0f - 1.0f;
                final float sampleY = (this.rnd.nextFloat() / n + y / (float)n) * 2.0f - 1.0f;
                callback.onNewSample(sampleX, sampleY);
            }
        }
    }
    
    public void generateCentered(final int n, final float centering, final Callback2d callback) {
        final float start = centering * 0.5f;
        final float end = 1.0f - centering;
        for (int y = 0; y < n; ++y) {
            for (int x = 0; x < n; ++x) {
                final float sampleX = ((start + this.rnd.nextFloat() * end) / n + x / (float)n) * 2.0f - 1.0f;
                final float sampleY = ((start + this.rnd.nextFloat() * end) / n + y / (float)n) * 2.0f - 1.0f;
                callback.onNewSample(sampleX, sampleY);
            }
        }
    }
}
