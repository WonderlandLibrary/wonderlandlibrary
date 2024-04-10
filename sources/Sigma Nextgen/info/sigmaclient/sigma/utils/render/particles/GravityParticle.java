package info.sigmaclient.sigma.utils.render.particles;

import info.sigmaclient.sigma.utils.render.RenderUtils;

import java.awt.*;

/**
 * Created by Arithmo on 5/10/2017 at 9:03 PM.
 */
public class GravityParticle extends Particle {

    public GravityParticle(float posX, float posY, float size, float speed, float alpha) {
        super(posX, posY, size, speed, alpha);
    }

    public void render(ParticleManager p, float alpha) {
        super.render(p, alpha);
        setPosY(getPosY() + getSpeed());
        RenderUtils.drawFilledCircleNoGL(getPosX(), getPosY(), getSize(),
                new Color(255, 255, 255, (int) getAlpha()).getRGB(), 10);
    }

}
