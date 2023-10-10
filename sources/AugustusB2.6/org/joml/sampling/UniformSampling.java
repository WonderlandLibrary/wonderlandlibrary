// 
// Decompiled by Procyon v0.5.36
// 

package org.joml.sampling;

import org.joml.Math;
import org.joml.Random;

public class UniformSampling
{
    public static class Disk
    {
        private final Random rnd;
        
        public Disk(final long seed, final int numSamples, final Callback2d callback) {
            this.rnd = new Random(seed);
            this.generate(numSamples, callback);
        }
        
        private void generate(final int numSamples, final Callback2d callback) {
            for (int i = 0; i < numSamples; ++i) {
                final float r = this.rnd.nextFloat();
                final float a = this.rnd.nextFloat() * 2.0f * 3.1415927f;
                final float sqrtR = Math.sqrt(r);
                final float x = sqrtR * (float)org.joml.sampling.Math.sin_roquen_9(a + 1.5707963267948966);
                final float y = sqrtR * (float)org.joml.sampling.Math.sin_roquen_9(a);
                callback.onNewSample(x, y);
            }
        }
    }
    
    public static class Sphere
    {
        private final Random rnd;
        
        public Sphere(final long seed, final int numSamples, final Callback3d callback) {
            this.rnd = new Random(seed);
            this.generate(numSamples, callback);
        }
        
        public void generate(final int numSamples, final Callback3d callback) {
            int i = 0;
            while (i < numSamples) {
                final float x1 = this.rnd.nextFloat() * 2.0f - 1.0f;
                final float x2 = this.rnd.nextFloat() * 2.0f - 1.0f;
                if (x1 * x1 + x2 * x2 >= 1.0f) {
                    continue;
                }
                final float sqrt = (float)Math.sqrt(1.0 - x1 * x1 - x2 * x2);
                final float x3 = 2.0f * x1 * sqrt;
                final float y = 2.0f * x2 * sqrt;
                final float z = 1.0f - 2.0f * (x1 * x1 + x2 * x2);
                callback.onNewSample(x3, y, z);
                ++i;
            }
        }
    }
}
