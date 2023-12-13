/*    */ package org.neverhook.client.ui.button;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.audio.ISound;
/*    */ import net.minecraft.client.audio.PositionedSoundRecord;
/*    */ import net.minecraft.client.audio.SoundHandler;
/*    */ import net.minecraft.client.gui.GuiButton;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.init.SoundEvents;
/*    */ import org.lwjgl.input.Mouse;
/*    */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*    */ 
/*    */ public class GuiNeverHookMenuButton
/*    */   extends GuiButton {
/* 16 */   private int fade = 20;
/*    */   
/*    */   public GuiNeverHookMenuButton(int buttonId, int x, int y, String buttonText) {
/* 19 */     this(buttonId, x, y, 200, 35, buttonText);
/*    */   }
/*    */   
/*    */   public GuiNeverHookMenuButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
/* 23 */     super(buttonId, x, y, widthIn, heightIn, buttonText);
/*    */   }
/*    */   
/*    */   public static int getMouseX() {
/* 27 */     return Mouse.getX() * sr.getScaledWidth() / (Minecraft.getInstance()).displayWidth;
/*    */   }
/*    */   
/*    */   public static int getMouseY() {
/* 31 */     return sr.getScaledHeight() - Mouse.getY() * sr.getScaledHeight() / (Minecraft.getInstance()).displayHeight - 1;
/*    */   }
/*    */   
/*    */   public void drawButton(Minecraft mc, int mouseX, int mouseY, float mouseButton) {
/* 35 */     if (this.visible) {
/* 36 */       this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height + 10);
/* 37 */       Color text = new Color(155, 155, 155, 255);
/* 38 */       Color color = new Color(this.fade + 14, this.fade + 14, this.fade + 14, 55);
/* 39 */       if (this.hovered) {
/* 40 */         if (this.fade < 100)
/* 41 */           this.fade += 8; 
/* 42 */         text = Color.white;
/*    */       }
/* 44 */       else if (this.fade > 20) {
/* 45 */         this.fade -= 8;
/*    */       } 
/* 47 */       GlStateManager.enableBlend();
/* 48 */       GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 49 */       GlStateManager.blendFunc(770, 771);
/* 50 */       int height = this.height + 11;
/* 51 */       RectHelper.drawSmoothRect(this.xPosition, this.yPosition, (this.width + this.xPosition), (height + this.yPosition), color.getRGB());
/* 52 */       mc.bigButtonFontRender.drawCenteredString(this.displayString, this.xPosition + this.width / 2.0F, (this.yPosition + this.height - 12), text.getRGB());
/* 53 */       mouseDragged(mc, mouseX, mouseY);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {}
/*    */ 
/*    */   
/*    */   public void mouseReleased(int mouseX, int mouseY) {}
/*    */ 
/*    */   
/*    */   public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
/* 65 */     return (this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height + 10);
/*    */   }
/*    */   
/*    */   public boolean isMouseOver() {
/* 69 */     return this.hovered;
/*    */   }
/*    */ 
/*    */   
/*    */   public void drawButtonForegroundLayer(int mouseX, int mouseY) {}
/*    */ 
/*    */   
/*    */   public void playPressSound(SoundHandler soundHandlerIn) {
/* 77 */     soundHandlerIn.playSound((ISound)PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
/*    */   }
/*    */   
/*    */   public int getButtonWidth() {
/* 81 */     return this.width;
/*    */   }
/*    */   
/*    */   public void setWidth(int width) {
/* 85 */     this.width = width;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\button\GuiNeverHookMenuButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */