package cn.hanabi.gui.particles;

import cn.hanabi.utils.ParticleRenderUtils;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;

public class ParticleGenerator {
   private final List particles = new ArrayList();
   private final int amount;
   private int prevWidth;
   private int prevHeight;

   public ParticleGenerator(int amount) {
      this.amount = amount;
   }

   public void draw(int mouseX, int mouseY) {
      if (this.particles.isEmpty() || this.prevWidth != Minecraft.getMinecraft().displayWidth || this.prevHeight != Minecraft.getMinecraft().displayHeight) {
         this.particles.clear();
         this.create();
      }

      this.prevWidth = Minecraft.getMinecraft().displayWidth;
      this.prevHeight = Minecraft.getMinecraft().displayHeight;
      Iterator var3 = this.particles.iterator();

      while(var3.hasNext()) {
         Particle particle = (Particle)var3.next();
         particle.fall();
         particle.interpolation();
         int range = 0;
         boolean mouseOver = (float)mouseX >= particle.x - (float)range && (float)mouseY >= particle.y - (float)range && (float)mouseX <= particle.x + (float)range && (float)mouseY <= particle.y + (float)range;
         if (mouseOver) {
            this.particles.stream().filter((part) -> {
               return part.getX() > particle.getX() && part.getX() - particle.getX() < (float)range && particle.getX() - part.getX() < (float)range && (part.getY() > particle.getY() && part.getY() - particle.getY() < (float)range || particle.getY() > part.getY() && particle.getY() - part.getY() < (float)range);
            }).forEach((connectable) -> {
               particle.connect(connectable.getX(), connectable.getY());
            });
         }

         for(int i = 0; i <= 5; ++i) {
            ParticleRenderUtils.drawCircle(particle.getX(), particle.getY(), particle.size + (float)i / 2.0F, (new Color(255, 255, 255, 15)).getRGB());
         }
      }

   }

   private void create() {
      Random random = new Random();

      for(int i = 0; i < this.amount; ++i) {
         this.particles.add(new Particle(random.nextInt(Minecraft.getMinecraft().displayWidth), random.nextInt(Minecraft.getMinecraft().displayHeight)));
      }

   }
}
