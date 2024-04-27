/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.particles;

import net.ccbluex.liquidbounce.utils.render.particle.ParticleGenerator;

public final class ParticleUtils {
    private static final ParticleGenerator particleGenerator = new ParticleGenerator(100);

    public static void drawParticles(int n, int n2) {
        particleGenerator.draw(n, n2);
    }
}

