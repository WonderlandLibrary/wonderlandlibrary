/*    */ package org.neverhook.client.ui.components.draggable.impl;
/*    */ 
/*    */ import com.mojang.realmsclient.gui.ChatFormatting;
/*    */ import org.neverhook.client.NeverHook;
/*    */ import org.neverhook.client.feature.impl.hud.HUD;
/*    */ import org.neverhook.client.helpers.misc.ClientHelper;
/*    */ import org.neverhook.client.ui.components.draggable.DraggableModule;
/*    */ import org.neverhook.security.utils.LicenseUtil;
/*    */ 
/*    */ public class ClientInfoComponent
/*    */   extends DraggableModule {
/*    */   public ClientInfoComponent() {
/* 13 */     super("ClientInfoComponent", sr.getScaledWidth() - mc.robotoRegularFontRender.getStringWidth(NeverHook.instance.type + " - " + ChatFormatting.WHITE + NeverHook.instance.version + ChatFormatting.RESET + " - " + LicenseUtil.userName), sr.getScaledHeight() - 20);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getWidth() {
/* 18 */     return 120;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getHeight() {
/* 23 */     return 15;
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(int mouseX, int mouseY) {
/* 28 */     if (HUD.clientInfo.getBoolValue()) {
/*    */       
/* 30 */       String buildStr = NeverHook.instance.type + " - " + ChatFormatting.WHITE + NeverHook.instance.version + ChatFormatting.RESET + " - " + LicenseUtil.userName;
/* 31 */       if (mc.player != null && mc.world != null) {
/* 32 */         mc.robotoRegularFontRender.drawStringWithShadow(buildStr, getX(), getY(), ClientHelper.getClientColor().getRGB());
/*    */       }
/*    */     } 
/*    */     
/* 36 */     super.render(mouseX, mouseY);
/*    */   }
/*    */ 
/*    */   
/*    */   public void draw() {
/* 41 */     if (HUD.clientInfo.getBoolValue()) {
/*    */       
/* 43 */       String buildStr = NeverHook.instance.type + " - " + ChatFormatting.WHITE + NeverHook.instance.version + ChatFormatting.RESET + " - " + LicenseUtil.userName;
/* 44 */       if (mc.player != null && mc.world != null) {
/* 45 */         mc.robotoRegularFontRender.drawStringWithShadow(buildStr, getX(), getY(), ClientHelper.getClientColor().getRGB());
/*    */       }
/*    */     } 
/* 48 */     super.draw();
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\components\draggable\impl\ClientInfoComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */