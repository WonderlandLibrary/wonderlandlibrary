// 
// Decompiled by Procyon v0.5.36
// 

package org.joml.sampling;

import org.joml.Random;

public class SpiralSampling
{
    private final Random rnd;
    
    public SpiralSampling(final long seed) {
        this.rnd = new Random(seed);
    }
    
    public void createEquiAngle(final float radius, final int numRotations, final int numSamples, final Callback2d callback) {
        for (int sample = 0; sample < numSamples; ++sample) {
            final float angle = 6.2831855f * (sample * numRotations) / numSamples;
            final float r = radius * sample / (numSamples - 1);
            final float x = (float)Math.sin_roquen_9(angle + 1.5707964f) * r;
            final float y = (float)Math.sin_roquen_9(angle) * r;
            callback.onNewSample(x, y);
        }
    }
    
    public void createEquiAngle(final float radius, final int numRotations, final int numSamples, final float jitter, final Callback2d callback) {
        final float spacing = radius / numRotations;
        for (int sample = 0; sample < numSamples; ++sample) {
            final float angle = 6.2831855f * (sample * numRotations) / numSamples;
            final float r = radius * sample / (numSamples - 1) + (this.rnd.nextFloat() * 2.0f - 1.0f) * spacing * jitter;
            final float x = (float)Math.sin_roquen_9(angle + 1.5707964f) * r;
            final float y = (float)Math.sin_roquen_9(angle) * r;
            callback.onNewSample(x, y);
        }
    }
}
