/*    */ package org.neverhook.client.feature.impl.hud;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import org.neverhook.client.NeverHook;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.render.EventRender2D;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.helpers.misc.ClientHelper;
/*    */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*    */ 
/*    */ public class Keystrokes
/*    */   extends Feature
/*    */ {
/* 15 */   public int lastA = 0;
/* 16 */   public int lastW = 0;
/* 17 */   public int lastS = 0;
/* 18 */   public int lastD = 0;
/*    */   public long deltaAnim;
/*    */   
/*    */   public Keystrokes() {
/* 22 */     super("Keystrokes", "Показывает нажатые клавиши", Type.Hud);
/* 23 */     this.deltaAnim = 0L;
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onRender2D(EventRender2D event) {
/* 28 */     boolean A = mc.gameSettings.keyBindLeft.pressed;
/* 29 */     boolean W = mc.gameSettings.keyBindForward.pressed;
/* 30 */     boolean S = mc.gameSettings.keyBindBack.pressed;
/* 31 */     boolean D = mc.gameSettings.keyBindRight.pressed;
/* 32 */     int alphaA = A ? 255 : 0;
/* 33 */     int alphaW = W ? 255 : 0;
/* 34 */     int alphaS = S ? 255 : 0;
/* 35 */     int alphaD = D ? 255 : 0;
/*    */ 
/*    */     
/* 38 */     if (this.lastA != alphaA) {
/* 39 */       float diff = (alphaA - this.lastA);
/* 40 */       this.lastA = (int)(this.lastA + diff / 40.0F);
/*    */     } 
/*    */     
/* 43 */     if (this.lastW != alphaW) {
/* 44 */       float diff = (alphaW - this.lastW);
/* 45 */       this.lastW = (int)(this.lastW + diff / 40.0F);
/*    */     } 
/*    */     
/* 48 */     if (this.lastS != alphaS) {
/* 49 */       float diff = (alphaS - this.lastS);
/* 50 */       this.lastS = (int)(this.lastS + diff / 40.0F);
/*    */     } 
/*    */     
/* 53 */     if (this.lastD != alphaD) {
/* 54 */       float diff = (alphaD - this.lastD);
/* 55 */       this.lastD = (int)(this.lastD + diff / 40.0F);
/*    */     } 
/*    */ 
/*    */     
/* 59 */     if (!HUD.blur.getBoolValue()) {
/* 60 */       RectHelper.drawRect(5.0D, 49.0D, 25.0D, 69.0D, (new Color(this.lastA, this.lastA, this.lastA, 150)).getRGB());
/*    */     } else {
/* 62 */       NeverHook.instance.blurUtil.blur(5.0F, 49.0F, 25.0F, 69.0F, 30);
/*    */     } 
/* 64 */     mc.fontRenderer.drawCenteredString("A", 15.0F, 56.0F, ClientHelper.getClientColor().getRGB());
/*    */     
/* 66 */     if (!HUD.blur.getBoolValue()) {
/* 67 */       RectHelper.drawRect(27.0D, 27.0D, 47.0D, 47.0D, (new Color(this.lastW, this.lastW, this.lastW, 150)).getRGB());
/*    */     } else {
/* 69 */       NeverHook.instance.blurUtil.blur(27.0F, 27.0F, 47.0F, 47.0F, 30);
/*    */     } 
/* 71 */     mc.fontRenderer.drawCenteredString("W", 37.0F, 34.0F, ClientHelper.getClientColor().getRGB());
/*    */     
/* 73 */     if (!HUD.blur.getBoolValue()) {
/* 74 */       RectHelper.drawRect(27.0D, 49.0D, 47.0D, 69.0D, (new Color(this.lastS, this.lastS, this.lastS, 150)).getRGB());
/*    */     } else {
/* 76 */       NeverHook.instance.blurUtil.blur(27.0F, 49.0F, 47.0F, 69.0F, 30);
/*    */     } 
/* 78 */     mc.fontRenderer.drawCenteredString("S", 37.0F, 56.0F, ClientHelper.getClientColor().getRGB());
/* 79 */     if (!HUD.blur.getBoolValue()) {
/* 80 */       RectHelper.drawRect(49.0D, 49.0D, 69.0D, 69.0D, (new Color(this.lastD, this.lastD, this.lastD, 150)).getRGB());
/*    */     } else {
/* 82 */       NeverHook.instance.blurUtil.blur(49.0F, 49.0F, 69.0F, 69.0F, 30);
/*    */     } 
/*    */     
/* 85 */     mc.fontRenderer.drawCenteredString("D", 59.0F, 56.0F, ClientHelper.getClientColor().getRGB());
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\hud\Keystrokes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */