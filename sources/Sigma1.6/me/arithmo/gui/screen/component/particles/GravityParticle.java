/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.gui.screen.component.particles;

import me.arithmo.gui.screen.component.particles.Particle;
import me.arithmo.gui.screen.component.particles.ParticleManager;
import me.arithmo.util.RenderingUtil;
import me.arithmo.util.render.Colors;

public class GravityParticle
extends Particle {
    public GravityParticle(float posX, float posY, float size, float speed, float alpha) {
        super(posX, posY, size, speed, alpha);
    }

    @Override
    public void render(ParticleManager p) {
        super.render(p);
        this.setPosY(this.getPosY() + this.getSpeed());
        RenderingUtil.drawFullCircle(this.getPosX(), this.getPosY(), this.getSize(), Colors.getColor(255, 255, 255, (int)this.getAlpha()));
    }
}

