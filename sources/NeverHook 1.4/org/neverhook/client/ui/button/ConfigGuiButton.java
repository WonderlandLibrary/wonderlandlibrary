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
/*    */ public class ConfigGuiButton
/*    */   extends GuiButton {
/* 16 */   private int fade = 20;
/*    */   
/*    */   public ConfigGuiButton(int buttonId, int x, int y, String buttonText) {
/* 19 */     this(buttonId, x, y, 200, 35, buttonText);
/*    */   }
/*    */   
/*    */   public ConfigGuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
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
/* 36 */       int height = this.height - 31;
/* 37 */       this.hovered = (mouseX >= this.xPosition + 93 && mouseY >= this.yPosition - 41 && mouseX < this.xPosition + this.width - 43 && mouseY < height + this.yPosition - 30);
/* 38 */       Color text = new Color(155, 155, 155, 255);
/* 39 */       if (this.enabled)
/* 40 */         if (this.hovered) {
/* 41 */           if (this.fade < 100)
/* 42 */             this.fade += 8; 
/* 43 */           text = Color.white;
/*    */         }
/* 45 */         else if (this.fade > 20) {
/* 46 */           this.fade -= 8;
/*    */         }  
/* 48 */       GlStateManager.enableBlend();
/* 49 */       GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 50 */       GlStateManager.blendFunc(770, 771);
/*    */       
/* 52 */       RectHelper.drawSkeetButton((this.xPosition + 125), (this.yPosition + 2), (this.width + this.xPosition - 75), (height + this.yPosition));
/* 53 */       mc.fontRendererObj.drawCenteredString(this.displayString, this.xPosition + this.width / 2.0F + 25.0F, (this.yPosition + this.height - 73), text.getRGB());
/*    */       
/* 55 */       mouseDragged(mc, mouseX, mouseY);
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
/* 67 */     int height = this.height - 31;
/* 68 */     return (this.enabled && this.visible && mouseX >= this.xPosition + 93 && mouseY >= this.yPosition - 45 && mouseX < this.xPosition + this.width - 43 && mouseY < height + this.yPosition - 30);
/*    */   }
/*    */   
/*    */   public boolean isMouseOver() {
/* 72 */     return this.hovered;
/*    */   }
/*    */ 
/*    */   
/*    */   public void drawButtonForegroundLayer(int mouseX, int mouseY) {}
/*    */   
/*    */   public void playPressSound(SoundHandler soundHandlerIn) {
/* 79 */     soundHandlerIn.playSound((ISound)PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
/*    */   }
/*    */   
/*    */   public int getButtonWidth() {
/* 83 */     return this.width;
/*    */   }
/*    */   
/*    */   public void setWidth(int width) {
/* 87 */     this.width = width;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\button\ConfigGuiButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */