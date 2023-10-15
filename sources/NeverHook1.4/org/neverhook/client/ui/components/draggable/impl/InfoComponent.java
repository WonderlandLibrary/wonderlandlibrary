/*    */ package org.neverhook.client.ui.components.draggable.impl;
/*    */ 
/*    */ import com.mojang.realmsclient.gui.ChatFormatting;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import org.neverhook.client.feature.impl.hud.HUD;
/*    */ import org.neverhook.client.helpers.misc.ClientHelper;
/*    */ import org.neverhook.client.helpers.player.MovementHelper;
/*    */ import org.neverhook.client.ui.components.draggable.DraggableModule;
/*    */ 
/*    */ public class InfoComponent
/*    */   extends DraggableModule {
/*    */   public InfoComponent() {
/* 13 */     super("InfoComponent", 100, 200);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getWidth() {
/* 18 */     return 75;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getHeight() {
/* 23 */     return 27;
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(int mouseX, int mouseY) {
/* 28 */     if (HUD.worldInfo.getBoolValue()) {
/* 29 */       String speed = String.format("%.2f " + ChatFormatting.WHITE + "blocks/sec", new Object[] { Float.valueOf(MovementHelper.getSpeed() * 16.0F * mc.timer.timerSpeed) });
/* 30 */       String fps = "" + Minecraft.getDebugFPS();
/* 31 */       String xCoord = "" + Math.round(mc.player.posX);
/* 32 */       String yCoord = "" + Math.round(mc.player.posY);
/* 33 */       String zCoord = "" + Math.round(mc.player.posZ);
/* 34 */       mc.robotoRegularFontRender.drawStringWithShadow("X: ", getX(), getY(), ClientHelper.getClientColor().getRGB());
/* 35 */       mc.robotoRegularFontRender.drawStringWithShadow(xCoord, (getX() + 10), getY(), -1);
/* 36 */       mc.robotoRegularFontRender.drawStringWithShadow("Y: ", (getX() + 30 + mc.robotoRegularFontRender.getStringWidth(xCoord) - 17), getY(), ClientHelper.getClientColor().getRGB());
/* 37 */       mc.robotoRegularFontRender.drawStringWithShadow(yCoord, (getX() + 40 + mc.robotoRegularFontRender.getStringWidth(xCoord) - 17), getY(), -1);
/* 38 */       mc.robotoRegularFontRender.drawStringWithShadow("Z: ", (getX() + 66 + mc.robotoRegularFontRender.getStringWidth(xCoord) - 23 + mc.robotoRegularFontRender.getStringWidth(yCoord) - 17), getY(), ClientHelper.getClientColor().getRGB());
/* 39 */       mc.robotoRegularFontRender.drawStringWithShadow(zCoord, (getX() + 76 + mc.robotoRegularFontRender.getStringWidth(xCoord) - 23 + mc.robotoRegularFontRender.getStringWidth(yCoord) - 17), getY(), -1);
/*    */       
/* 41 */       mc.robotoRegularFontRender.drawStringWithShadow("FPS: ", getX(), (getY() + 11), ClientHelper.getClientColor().getRGB());
/* 42 */       mc.robotoRegularFontRender.drawStringWithShadow(fps, (getX() + 22), (getY() + 11), -1);
/*    */       
/* 44 */       mc.robotoRegularFontRender.drawStringWithShadow(speed, (getX() + mc.robotoRegularFontRender.getStringWidth(fps) + 25), (getY() + 11), ClientHelper.getClientColor().getRGB());
/*    */     } 
/* 46 */     super.render(mouseX, mouseY);
/*    */   }
/*    */ 
/*    */   
/*    */   public void draw() {
/* 51 */     if (HUD.worldInfo.getBoolValue()) {
/* 52 */       String speed = String.format("%.2f " + ChatFormatting.WHITE + "blocks/sec", new Object[] { Float.valueOf(MovementHelper.getSpeed() * 16.0F * mc.timer.timerSpeed) });
/* 53 */       String fps = "" + Minecraft.getDebugFPS();
/* 54 */       String xCoord = "" + Math.round(mc.player.posX);
/* 55 */       String yCoord = "" + Math.round(mc.player.posY);
/* 56 */       String zCoord = "" + Math.round(mc.player.posZ);
/* 57 */       mc.robotoRegularFontRender.drawStringWithShadow("X: ", getX(), getY(), ClientHelper.getClientColor().getRGB());
/* 58 */       mc.robotoRegularFontRender.drawStringWithShadow(xCoord, (getX() + 10), getY(), -1);
/* 59 */       mc.robotoRegularFontRender.drawStringWithShadow("Y: ", (getX() + 30 + mc.robotoRegularFontRender.getStringWidth(xCoord) - 17), getY(), ClientHelper.getClientColor().getRGB());
/* 60 */       mc.robotoRegularFontRender.drawStringWithShadow(yCoord, (getX() + 40 + mc.robotoRegularFontRender.getStringWidth(xCoord) - 17), getY(), -1);
/* 61 */       mc.robotoRegularFontRender.drawStringWithShadow("Z: ", (getX() + 66 + mc.robotoRegularFontRender.getStringWidth(xCoord) - 23 + mc.robotoRegularFontRender.getStringWidth(yCoord) - 17), getY(), ClientHelper.getClientColor().getRGB());
/* 62 */       mc.robotoRegularFontRender.drawStringWithShadow(zCoord, (getX() + 76 + mc.robotoRegularFontRender.getStringWidth(xCoord) - 23 + mc.robotoRegularFontRender.getStringWidth(yCoord) - 17), getY(), -1);
/*    */       
/* 64 */       mc.robotoRegularFontRender.drawStringWithShadow("FPS: ", getX(), (getY() + 11), ClientHelper.getClientColor().getRGB());
/* 65 */       mc.robotoRegularFontRender.drawStringWithShadow(fps, (getX() + 22), (getY() + 11), -1);
/*    */       
/* 67 */       mc.robotoRegularFontRender.drawStringWithShadow(speed, (getX() + mc.robotoRegularFontRender.getStringWidth(fps) + 25), (getY() + 11), ClientHelper.getClientColor().getRGB());
/*    */     } 
/* 69 */     super.draw();
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\components\draggable\impl\InfoComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */