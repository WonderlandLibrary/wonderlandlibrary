package info.sigmaclient.sigma.utils.render.particles;

import info.sigmaclient.sigma.utils.render.rendermanagers.ScaledResolution;
import net.minecraft.client.Minecraft;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class ParticleManager {
    private List<Particle> particles = new CopyOnWriteArrayList<>();
    public Minecraft mc = Minecraft.getInstance();
    private boolean rightClicked;

    public ParticleManager() {
        getParticles().clear();
        for(int i =0;i<500;i++) {
            getParticles().add(new TopLeftParticle(
                    centerWidth() + random(-getRes().getScaledWidth(), getRes().getScaledWidth()) * 2,
                    centerHeight() + random(-getRes().getScaledHeight(), getRes().getScaledHeight()) * 2,
                    random(1, 2),
                    random(40, 100) / 100f,
                    random(40, 100)));
        }
    }

    public void render(int x, int y, float alpha) {
        if (getParticles().size() <= 500) {
            for(int i =0;i<500 - getParticles().size();i++) {
                getParticles().add(new TopLeftParticle(
                            random(-100, getRes().getScaledWidth() + 100),
                            -50,
                            random(1, 2),
                            random(3, 100) / 100f,
                            random(40, 100)));
            }
        }
        for (Particle p : getParticles()) {
            if (p.getAlpha() <= 0.0F) {
                getParticles().remove(p);
            }
            if(p.getPosX() < -100 || p.getPosY() < -100 || p.getPosX() > getRes().getScaledWidth() || p.getPosY() > getRes().getScaledWidth()){
                getParticles().remove(p);
            }
            p.render(this, alpha);
        }
    }

    Random r = new Random();
    public int random(int low, int high) {
        return r.nextInt(high - low) + low;
    }

    public ScaledResolution getRes() {
        return new ScaledResolution(this.mc);
    }

    public int centerWidth() {
        return getRes().getScaledWidth() / 2;
    }

    public int centerHeight() {
        return getRes().getScaledHeight() / 2;
    }

    public List<Particle> getParticles() {
        return particles;
    }

}