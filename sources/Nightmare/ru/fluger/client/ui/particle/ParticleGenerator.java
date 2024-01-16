// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.ui.particle;

import java.util.Random;
import java.util.Iterator;
import ru.fluger.client.helpers.render.RenderHelper;
import ru.fluger.client.helpers.misc.ClientHelper;
import java.util.ArrayList;
import java.util.List;

public class ParticleGenerator
{
    private final List<Particle> particles;
    private final int amount;
    private int prevWidth;
    private int prevHeight;
    
    public ParticleGenerator(final int amount) {
        this.particles = new ArrayList<Particle>();
        this.amount = amount;
    }
    
    public void draw(final int mouseX, final int mouseY) {
        if (this.particles.isEmpty() || this.prevWidth != bib.z().d || this.prevHeight != bib.z().e) {
            this.particles.clear();
            this.create();
        }
        this.prevWidth = bib.z().d;
        this.prevHeight = bib.z().e;
        for (final Particle particle : this.particles) {
            particle.fall();
            particle.interpolation();
            final int range = 110;
            RenderHelper.drawCircle(particle.getX(), particle.getY(), particle.size, ClientHelper.getClientColor().getRGB());
        }
    }
    
    private void create() {
        final Random random = new Random();
        for (int i = 0; i < this.amount; ++i) {
            this.particles.add(new Particle(random.nextInt(bib.z().d), random.nextInt(bib.z().e)));
        }
    }
}
