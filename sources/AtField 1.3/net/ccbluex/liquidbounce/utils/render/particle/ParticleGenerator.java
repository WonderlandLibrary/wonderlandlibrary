/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.ccbluex.liquidbounce.utils.render.particle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.ccbluex.liquidbounce.utils.render.particle.Particle;
import net.ccbluex.liquidbounce.utils.render.particle.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class ParticleGenerator {
    private int prevWidth;
    private final List particles = new ArrayList();
    private int prevHeight;
    private final int amount;

    private static boolean lambda$draw$0(Particle particle, int n, Particle particle2) {
        return particle2.getX() > particle.getX() && particle2.getX() - particle.getX() < (float)n && particle.getX() - particle2.getX() < (float)n && (particle2.getY() > particle.getY() && particle2.getY() - particle.getY() < (float)n || particle.getY() > particle2.getY() && particle.getY() - particle2.getY() < (float)n);
    }

    private void create() {
        Random random = new Random();
        for (int i = 0; i < this.amount; ++i) {
            this.particles.add(new Particle(random.nextInt(Minecraft.func_71410_x().field_71443_c), random.nextInt(Minecraft.func_71410_x().field_71440_d)));
        }
    }

    public ParticleGenerator(int n) {
        this.amount = n;
    }

    public void draw(int n, int n2) {
        if (this.particles.isEmpty() || this.prevWidth != Minecraft.func_71410_x().field_71443_c || this.prevHeight != Minecraft.func_71410_x().field_71440_d) {
            this.particles.clear();
            this.create();
        }
        this.prevWidth = Minecraft.func_71410_x().field_71443_c;
        this.prevHeight = Minecraft.func_71410_x().field_71440_d;
        for (Particle particle : this.particles) {
            boolean bl;
            particle.fall();
            particle.interpolation();
            int n3 = 50;
            boolean bl2 = bl = (float)n >= particle.x - (float)n3 && (float)n2 >= particle.y - (float)n3 && (float)n <= particle.x + (float)n3 && (float)n2 <= particle.y + (float)n3;
            if (bl) {
                this.particles.stream().filter(arg_0 -> ParticleGenerator.lambda$draw$0(particle, n3, arg_0)).forEach(arg_0 -> ParticleGenerator.lambda$draw$1(particle, arg_0));
            }
            RenderUtils.drawCircle(particle.getX(), particle.getY(), particle.size, -1);
        }
    }

    private static void lambda$draw$1(Particle particle, Particle particle2) {
        particle.connect(particle2.getX(), particle2.getY());
    }
}

