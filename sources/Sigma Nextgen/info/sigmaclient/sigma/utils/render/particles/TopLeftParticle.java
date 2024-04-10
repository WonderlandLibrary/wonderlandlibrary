package info.sigmaclient.sigma.utils.render.particles;

import info.sigmaclient.sigma.utils.RandomUtil;
import info.sigmaclient.sigma.utils.render.RenderUtils;

import java.awt.*;

/**
 * Created by Arithmo on 5/10/2017 at 9:05 PM.
 */
public class TopLeftParticle extends Particle {
    public TopLeftParticle(float posX, float posY, float size, float speed, float alpha) {
        super(posX, posY, size, speed, alpha);
    }

    public void render(ParticleManager p, float alpha) {
        super.render(p, alpha);
//        setSpeed(getSpeed() * 1.01f);
//        setAlpha(Math.min(100, getAlpha() + mc.timer.renderPartialTicks * 5));
        setSpeed(getSpeed() + RandomUtil.nextFloat(-0.05, 0.05));

        setPosY(getPosY() + getSpeed() * (2 + RandomUtil.nextFloat(0, 1)));
        setPosX(getPosX() + getSpeed() * (2 + RandomUtil.nextFloat(0, 1)));
        RenderUtils.drawFilledCircleNoGL(getPosX(), getPosY(), getSize() * 1.2,
                new Color(255, 255, 255, (int)(Math.min(230, (int) (getAlpha() * 3)) * alpha)).getRGB(), 5);
    }
}