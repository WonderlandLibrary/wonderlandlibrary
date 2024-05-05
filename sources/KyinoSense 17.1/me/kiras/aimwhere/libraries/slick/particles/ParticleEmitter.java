/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.particles;

import me.kiras.aimwhere.libraries.slick.Image;
import me.kiras.aimwhere.libraries.slick.particles.Particle;
import me.kiras.aimwhere.libraries.slick.particles.ParticleSystem;

public interface ParticleEmitter {
    public void update(ParticleSystem var1, int var2);

    public boolean completed();

    public void wrapUp();

    public void updateParticle(Particle var1, int var2);

    public boolean isEnabled();

    public void setEnabled(boolean var1);

    public boolean useAdditive();

    public Image getImage();

    public boolean isOriented();

    public boolean usePoints(ParticleSystem var1);

    public void resetState();
}

