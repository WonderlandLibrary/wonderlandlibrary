/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.vitox;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.vitox.Particle;
import net.vitox.particle.util.RenderUtils;

@SideOnly(value=Side.CLIENT)
public class ParticleGenerator {
    private final List<Particle> particles = new ArrayList<Particle>();
    private final int amount;
    private int prevWidth;
    private int prevHeight;

    public ParticleGenerator(int amount) {
        this.amount = amount;
    }

    public void draw(int mouseX, int mouseY) {
        if (this.particles.isEmpty() || this.prevWidth != Minecraft.func_71410_x().field_71443_c || this.prevHeight != Minecraft.func_71410_x().field_71440_d) {
            this.particles.clear();
            this.create();
        }
        this.prevWidth = Minecraft.func_71410_x().field_71443_c;
        this.prevHeight = Minecraft.func_71410_x().field_71440_d;
        for (Particle particle : this.particles) {
            boolean mouseOver;
            particle.fall();
            particle.interpolation();
            int range = 50;
            boolean bl = mouseOver = (float)mouseX >= particle.x - (float)range && (float)mouseY >= particle.y - (float)range && (float)mouseX <= particle.x + (float)range && (float)mouseY <= particle.y + (float)range;
            if (mouseOver) {
                this.particles.stream().filter(part -> part.getX() > particle.getX() && part.getX() - particle.getX() < (float)range && particle.getX() - part.getX() < (float)range && (part.getY() > particle.getY() && part.getY() - particle.getY() < (float)range || particle.getY() > part.getY() && particle.getY() - part.getY() < (float)range)).forEach(connectable -> particle.connect(connectable.getX(), connectable.getY()));
            }
            RenderUtils.drawCircle(particle.getX(), particle.getY(), particle.size, -1);
        }
    }

    private void create() {
        Random random = new Random();
        for (int i = 0; i < this.amount; ++i) {
            this.particles.add(new Particle(random.nextInt(Minecraft.func_71410_x().field_71443_c), random.nextInt(Minecraft.func_71410_x().field_71440_d)));
        }
    }
}

