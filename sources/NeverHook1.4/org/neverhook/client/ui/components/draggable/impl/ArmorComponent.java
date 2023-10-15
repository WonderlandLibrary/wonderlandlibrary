/*    */ package org.neverhook.client.ui.components.draggable.impl;
/*    */ 
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import org.neverhook.client.feature.impl.hud.HUD;
/*    */ import org.neverhook.client.ui.components.draggable.DraggableModule;
/*    */ 
/*    */ public class ArmorComponent
/*    */   extends DraggableModule {
/*    */   public ArmorComponent() {
/* 11 */     super("ArmorComponent", 100, 350);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getWidth() {
/* 16 */     return 105;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getHeight() {
/* 21 */     return 30;
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(int mouseX, int mouseY) {
/* 26 */     GlStateManager.pushMatrix();
/* 27 */     GlStateManager.enableTexture2D();
/* 28 */     int i = getX();
/* 29 */     int count = 0;
/* 30 */     int y = getY();
/* 31 */     for (ItemStack is : mc.player.inventory.armorInventory) {
/* 32 */       count++;
/* 33 */       if (is.isEmpty()) {
/* 34 */         this.drag.setCanRender(false); continue;
/*    */       } 
/* 36 */       this.drag.setCanRender(true);
/* 37 */       int x = i - 90 + (9 - count) * 20 + 2;
/* 38 */       GlStateManager.enableDepth();
/* 39 */       (mc.getRenderItem()).zLevel = 200.0F;
/* 40 */       mc.getRenderItem().renderItemAndEffectIntoGUI(is, x, y);
/* 41 */       mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, is, x, y, "");
/* 42 */       (mc.getRenderItem()).zLevel = 0.0F;
/* 43 */       GlStateManager.enableTexture2D();
/* 44 */       GlStateManager.disableLighting();
/* 45 */       GlStateManager.disableDepth();
/*    */     } 
/*    */     
/* 48 */     GlStateManager.enableDepth();
/* 49 */     GlStateManager.disableLighting();
/* 50 */     GlStateManager.popMatrix();
/* 51 */     super.render(mouseX, mouseY);
/*    */   }
/*    */ 
/*    */   
/*    */   public void draw() {
/* 56 */     if (HUD.armor.getBoolValue()) {
/* 57 */       GlStateManager.pushMatrix();
/* 58 */       GlStateManager.enableTexture2D();
/* 59 */       int i = getX();
/* 60 */       int count = 0;
/* 61 */       int y = getY();
/* 62 */       for (ItemStack is : mc.player.inventory.armorInventory) {
/* 63 */         count++;
/* 64 */         if (is.isEmpty()) {
/* 65 */           this.drag.setCanRender(false); continue;
/*    */         } 
/* 67 */         this.drag.setCanRender(true);
/* 68 */         int x = i - 90 + (9 - count) * 20 + 2;
/* 69 */         GlStateManager.enableDepth();
/* 70 */         (mc.getRenderItem()).zLevel = 200.0F;
/* 71 */         mc.getRenderItem().renderItemAndEffectIntoGUI(is, x, y);
/* 72 */         mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, is, x, y, "");
/* 73 */         mc.getRenderItem().renderItemAndEffectIntoGUI(is, x, y);
/* 74 */         (mc.getRenderItem()).zLevel = 0.0F;
/* 75 */         GlStateManager.enableTexture2D();
/* 76 */         GlStateManager.disableLighting();
/* 77 */         GlStateManager.disableDepth();
/*    */       } 
/*    */       
/* 80 */       GlStateManager.enableDepth();
/* 81 */       GlStateManager.disableLighting();
/* 82 */       GlStateManager.popMatrix();
/*    */     } 
/* 84 */     super.draw();
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\components\draggable\impl\ArmorComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */