/*     */ package org.neverhook.client.helpers.render;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import org.neverhook.client.helpers.math.MathematicHelper;
/*     */ 
/*     */ public class ScreenHelper
/*     */ {
/*     */   private float x;
/*     */   private float y;
/*     */   private long lastMS;
/*     */   
/*     */   public ScreenHelper(float x, float y) {
/*  13 */     this.x = x;
/*  14 */     this.y = y;
/*  15 */     this.lastMS = System.currentTimeMillis();
/*     */   }
/*     */   
/*     */   public static double animate(double target, double current, double speed) {
/*  19 */     boolean larger = (target > current);
/*  20 */     if (speed < 0.0D) {
/*  21 */       speed = 0.0D;
/*  22 */     } else if (speed > 1.0D) {
/*  23 */       speed = 1.0D;
/*     */     } 
/*     */     
/*  26 */     double dif = Math.max(target, current) - Math.min(target, current);
/*  27 */     double factor = dif * speed;
/*  28 */     if (factor < 0.1D) {
/*  29 */       factor = 0.1D;
/*     */     }
/*     */     
/*  32 */     if (larger) {
/*  33 */       current += factor;
/*     */     } else {
/*  35 */       current -= factor;
/*     */     } 
/*     */     
/*  38 */     return current;
/*     */   }
/*     */   
/*     */   public static double progressiveAnimation(double now, double desired, double speed) {
/*  42 */     double dif = Math.abs(now - desired);
/*  43 */     int fps = Minecraft.getDebugFPS();
/*  44 */     if (dif > 0.0D) {
/*  45 */       double animationSpeed = MathematicHelper.round(Math.min(10.0D, Math.max(0.05D, 144.0D / fps * dif / 10.0D * speed)), 0.05D);
/*  46 */       if (dif < animationSpeed) {
/*  47 */         animationSpeed = dif;
/*     */       }
/*  49 */       if (now < desired) {
/*  50 */         return now + animationSpeed;
/*     */       }
/*  52 */       if (now > desired) {
/*  53 */         return now - animationSpeed;
/*     */       }
/*     */     } 
/*  56 */     return now;
/*     */   }
/*     */   
/*     */   public static double linearAnimation(double now, double desired, double speed) {
/*  60 */     double dif = Math.abs(now - desired);
/*  61 */     int fps = Minecraft.getDebugFPS();
/*  62 */     if (dif > 0.0D) {
/*  63 */       double animationSpeed = MathematicHelper.round(Math.min(10.0D, Math.max(0.005D, 144.0D / fps * speed)), 0.005D);
/*  64 */       if (dif != 0.0D && dif < animationSpeed) {
/*  65 */         animationSpeed = dif;
/*     */       }
/*  67 */       if (now < desired) {
/*  68 */         return now + animationSpeed;
/*     */       }
/*  70 */       if (now > desired) {
/*  71 */         return now - animationSpeed;
/*     */       }
/*     */     } 
/*  74 */     return now;
/*     */   }
/*     */   
/*     */   public void interpolate(float targetX, float targetY, double speed) {
/*  78 */     long currentMS = System.currentTimeMillis();
/*  79 */     long delta = currentMS - this.lastMS;
/*  80 */     this.lastMS = currentMS;
/*  81 */     double deltaX = 0.0D;
/*  82 */     double deltaY = 0.0D;
/*  83 */     if (speed != 0.0D) {
/*  84 */       deltaX = (Math.abs(targetX - this.x) * 0.35F) / 10.0D / speed;
/*  85 */       deltaY = (Math.abs(targetY - this.y) * 0.35F) / 10.0D / speed;
/*     */     } 
/*  87 */     this.x = AnimationHelper.calculateCompensation(targetX, this.x, delta, deltaX);
/*  88 */     this.y = AnimationHelper.calculateCompensation(targetY, this.y, delta, deltaY);
/*     */   }
/*     */   
/*     */   public void calculateCompensation(float targetX, float targetY, float xSpeed, float ySpeed) {
/*  92 */     int deltaX = (int)(Math.abs(targetX - this.x) * xSpeed);
/*  93 */     int deltaY = (int)(Math.abs(targetY - this.y) * ySpeed);
/*  94 */     this.x = AnimationHelper.calculateCompensation(targetX, this.x, (long)Minecraft.frameTime, deltaX);
/*  95 */     this.y = AnimationHelper.calculateCompensation(targetY, this.y, (long)Minecraft.frameTime, deltaY);
/*     */   }
/*     */   
/*     */   public float getX() {
/*  99 */     return this.x;
/*     */   }
/*     */   
/*     */   public void setX(float x) {
/* 103 */     this.x = x;
/*     */   }
/*     */   
/*     */   public float getY() {
/* 107 */     return this.y;
/*     */   }
/*     */   
/*     */   public void setY(float y) {
/* 111 */     this.y = y;
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\helpers\render\ScreenHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */