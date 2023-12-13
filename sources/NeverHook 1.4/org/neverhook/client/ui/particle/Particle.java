/*    */ package org.neverhook.client.ui.particle;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.client.gui.ScaledResolution;
/*    */ 
/*    */ 
/*    */ public class Particle
/*    */ {
/*    */   public float x;
/*    */   public float y;
/*    */   public float radius;
/*    */   public float speed;
/*    */   public float ticks;
/*    */   public float opacity;
/*    */   
/*    */   public Particle(ScaledResolution sr, float radius, float speed) {
/* 17 */     this.x = (new Random()).nextFloat() * sr.getScaledWidth();
/* 18 */     this.y = (new Random()).nextFloat() * sr.getScaledHeight();
/* 19 */     this.ticks = (new Random()).nextFloat() * sr.getScaledHeight() / 2.0F;
/* 20 */     this.radius = radius;
/* 21 */     this.speed = speed;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\particle\Particle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */