/*    */ package org.neverhook.client.ui.particle;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.ScaledResolution;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import org.lwjgl.input.Mouse;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ import org.neverhook.client.helpers.render.RenderHelper;
/*    */ 
/*    */ 
/*    */ public class ParticleSystem
/*    */ {
/* 17 */   public List<Particle> particles = new ArrayList<>();
/*    */   public float lastMouseX;
/*    */   public float lastMouseY;
/*    */   
/*    */   public void render() {
/* 22 */     GlStateManager.enableBlend();
/* 23 */     GlStateManager.disableAlpha();
/* 24 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 25 */     ScaledResolution sr = new ScaledResolution(Minecraft.getInstance());
/* 26 */     float xOffset = 0.0F;
/* 27 */     float yOffset = 0.0F;
/* 28 */     if (this.particles.size() < sr.getScaledWidth() / 8.0F) {
/* 29 */       this.particles.add(new Particle(sr, (new Random()).nextFloat() + 0.5F, (new Random()).nextFloat() * 5.0F + 5.0F));
/*    */     }
/* 31 */     List<Particle> toRemove = new ArrayList<>();
/*    */     
/* 33 */     int maxOpacity = 52;
/* 34 */     int color = -570425345;
/* 35 */     for (Particle particle : this.particles) {
/* 36 */       double particleX = particle.x + Math.sin((particle.ticks / 2.0F)) * 50.0D + (-xOffset / 5.0F * Mouse.getX());
/* 37 */       double particleY = (particle.ticks * particle.speed * particle.ticks) / 10.0D + (-yOffset / 2.0F);
/* 38 */       if (particleY < sr.getScaledHeight()) {
/*    */         
/* 40 */         if (particle.opacity < maxOpacity) {
/* 41 */           particle.opacity += 0.5F;
/*    */         }
/* 43 */         if (particle.opacity > maxOpacity) {
/* 44 */           particle.opacity = maxOpacity;
/*    */         }
/* 46 */         GlStateManager.enableBlend();
/* 47 */         drawBorderedCircle((float)particleX, (float)particleY, particle.radius * particle.opacity / maxOpacity, color);
/*    */       } 
/* 49 */       particle.ticks = (float)(particle.ticks + 0.025D);
/* 50 */       if (particleY > (sr.getScaledHeight() / 4.0F) || particleY < 0.0D || particleX > sr.getScaledWidth() || particleX < 0.0D) {
/* 51 */         toRemove.add(particle);
/*    */       }
/*    */     } 
/*    */     
/* 55 */     this.particles.removeAll(toRemove);
/* 56 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 57 */     GlStateManager.enableBlend();
/* 58 */     GlStateManager.disableAlpha();
/* 59 */     this.lastMouseX = Mouse.getX();
/* 60 */     this.lastMouseY = Mouse.getY();
/*    */   }
/*    */   
/*    */   public void drawBorderedCircle(float x, float y, float radius, int insideC) {
/* 64 */     GL11.glDisable(3553);
/* 65 */     GL11.glBlendFunc(770, 771);
/* 66 */     GL11.glEnable(2848);
/* 67 */     GL11.glPushMatrix();
/* 68 */     GL11.glScalef(0.1F, 0.1F, 0.1F);
/* 69 */     RenderHelper.drawCircle(x * 10.0F, y * 10.0F, radius * 10.0F, true, new Color(insideC));
/* 70 */     GL11.glScalef(10.0F, 10.0F, 10.0F);
/* 71 */     GL11.glPopMatrix();
/* 72 */     GL11.glEnable(3553);
/* 73 */     GL11.glDisable(2848);
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\particle\ParticleSystem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */