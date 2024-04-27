/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.ccbluex.liquidbounce.utils.render;

import net.ccbluex.liquidbounce.utils.render.particle.ParticleGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public final class ParticleUtils {
    private static final ParticleGenerator particleGenerator = new ParticleGenerator(100);

    public static void drawParticles(int n, int n2) {
        particleGenerator.draw(n, n2);
    }
}

