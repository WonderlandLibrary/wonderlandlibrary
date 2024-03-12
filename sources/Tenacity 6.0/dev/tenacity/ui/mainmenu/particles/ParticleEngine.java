// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.mainmenu.particles;

import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Iterator;
import java.util.Collection;
import java.util.Comparator;
import net.minecraft.client.gui.ScaledResolution;
import dev.tenacity.utils.tuples.Pair;
import java.util.ArrayList;
import dev.tenacity.utils.tuples.mutable.MutablePair;
import java.util.List;
import dev.tenacity.utils.Utils;

public class ParticleEngine implements Utils
{
    private final List<ParticleImage> particleImages;
    private MutablePair<Integer, Integer> particleTypes;
    private final List<Particle> particles;
    private final List<Particle> toRemove;
    private static final int BIG_LIMIT = 2;
    private static final int SMALL_LIMIT = 4;
    
    public ParticleEngine() {
        this.particleImages = new ArrayList<ParticleImage>();
        this.particleTypes = MutablePair.of(0, 0);
        this.particles = new ArrayList<Particle>();
        this.toRemove = new ArrayList<Particle>();
        this.particleImages.add(new ParticleImage(1, Pair.of(297, 301)));
        this.particleImages.add(new ParticleImage(2, Pair.of(303, 310)));
        this.particleImages.add(new ParticleImage(3, Pair.of(748, 781)));
        this.particleImages.add(new ParticleImage(4, Pair.of(227, 283)));
        this.particleImages.add(new ParticleImage(5, Pair.of(251, 302)));
        this.particleImages.add(new ParticleImage(6, Pair.of(253, 228)));
        this.particleImages.add(new ParticleImage(7, Pair.of(419, 476)));
        this.particleImages.add(new ParticleImage(8, Pair.of(564, 626)));
    }
    
    public void render() {
        final ScaledResolution sr = new ScaledResolution(ParticleEngine.mc);
        if (this.particles.size() < 6) {
            this.particles.add(new Particle(sr, this.getParticleImage()));
        }
        final ParticleImage pImg;
        this.particles.sort(Comparator.comparingDouble(p -> {
            pImg = p.getParticleImage();
            return (double)(pImg.getDimensions().getFirst() + pImg.getDimensions().getSecond());
        }).reversed());
        for (final Particle particle : this.particles) {
            particle.setX(particle.getInitialX() - particle.getTicks() * 20.0f);
            particle.setY(particle.getInitialY() + particle.getTicks() * (particle.getTicks() * particle.getSpeed() / 7.0f));
            particle.draw();
            final float particleHeight = particle.getParticleImage().getDimensions().getSecond() / 2.0f;
            final float particleWidth = particle.getParticleImage().getDimensions().getFirst() / 2.0f;
            if (particle.getX() + particleWidth < 0.0f || particle.getY() > sr.getScaledHeight() || particle.getX() > sr.getScaledWidth()) {
                this.toRemove.add(particle);
                if (particle.getParticleImage().getParticleType().equals(ParticleType.BIG)) {
                    this.particleTypes.computeSecond(s -> Math.max(0, s - 1));
                }
                else {
                    this.particleTypes.computeFirst(f -> Math.max(0, f - 1));
                }
            }
            particle.setTicks(particle.getTicks() + 0.03f);
        }
        if (this.toRemove.size() > 0) {
            this.particles.removeAll(this.toRemove);
            this.toRemove.clear();
        }
    }
    
    private ParticleImage getParticleImage() {
        final ParticleType particleType = this.getParticleType();
        final List<ParticleImage> particleList = this.particleImages.stream().filter(particleImg -> particleImg.getParticleType().equals(particleType)).collect((Collector<? super Object, ?, List<ParticleImage>>)Collectors.toList());
        return particleList.get((int)(new Random().nextFloat() * (particleList.size() - 1)));
    }
    
    private ParticleType getParticleType() {
        if (this.particleTypes.getFirst() == 0 && this.particleTypes.getSecond() == 0) {
            this.particleTypes.computeSecond(s -> s + 1);
            return ParticleType.BIG;
        }
        if (this.particleTypes.getFirst() < 4) {
            this.particleTypes.computeFirst(f -> f + 1);
            return ParticleType.SMALL;
        }
        if (this.particleTypes.getSecond() < 2) {
            this.particleTypes.computeSecond(s -> s + 1);
            return ParticleType.BIG;
        }
        System.out.println(this.particleTypes);
        throw new RuntimeException("pranked gg.");
    }
}
