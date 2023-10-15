/*    */ package org.neverhook.client.ui.components.draggable.impl;
/*    */ 
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import org.neverhook.client.NeverHook;
/*    */ import org.neverhook.client.feature.impl.hud.HUD;
/*    */ import org.neverhook.client.feature.impl.misc.Disabler;
/*    */ import org.neverhook.client.helpers.misc.ClientHelper;
/*    */ import org.neverhook.client.helpers.world.EntityHelper;
/*    */ import org.neverhook.client.ui.components.draggable.DraggableModule;
/*    */ 
/*    */ public class PingComponent extends DraggableModule {
/*    */   public PingComponent() {
/* 13 */     super("PingComponent", 100, 300);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getWidth() {
/* 18 */     return 55;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getHeight() {
/* 23 */     return 12;
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(int mouseX, int mouseY) {
/* 28 */     if (mc.player != null && mc.world != null) {
/* 29 */       int ping = NeverHook.instance.featureManager.getFeatureByClass(Disabler.class).getState() ? 0 : (mc.isSingleplayer() ? 0 : EntityHelper.getPing((EntityPlayer)mc.player));
/* 30 */       if (HUD.font.currentMode.equals("Minecraft")) {
/* 31 */         ClientHelper.getFontRender().drawStringWithShadow("Ping: §7" + ping + "ms", getX(), getY(), -1);
/*    */       } else {
/* 33 */         mc.fontRendererObj.drawStringWithShadow("Ping: §7" + ping + "ms", getX(), getY(), -1);
/*    */       } 
/*    */     } 
/* 36 */     super.render(mouseX, mouseY);
/*    */   }
/*    */ 
/*    */   
/*    */   public void draw() {
/* 41 */     if (mc.player != null && mc.world != null) {
/* 42 */       int ping = NeverHook.instance.featureManager.getFeatureByClass(Disabler.class).getState() ? 0 : (mc.isSingleplayer() ? 0 : EntityHelper.getPing((EntityPlayer)mc.player));
/* 43 */       if (HUD.font.currentMode.equals("Minecraft")) {
/* 44 */         ClientHelper.getFontRender().drawStringWithShadow("Ping: §7" + ping + "ms", getX(), getY(), -1);
/*    */       } else {
/* 46 */         mc.fontRendererObj.drawStringWithShadow("Ping: §7" + ping + "ms", getX(), getY(), -1);
/*    */       } 
/*    */     } 
/* 49 */     super.draw();
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\components\draggable\impl\PingComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */