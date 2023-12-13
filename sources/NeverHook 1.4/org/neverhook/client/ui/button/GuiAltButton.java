/*    */ package org.neverhook.client.ui.button;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiButton;
/*    */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*    */ 
/*    */ public class GuiAltButton
/*    */   extends GuiButton
/*    */ {
/* 11 */   private int fade = 20;
/* 12 */   private int fadeOutline = 20;
/*    */   
/*    */   public GuiAltButton(int buttonId, int x, int y, String buttonText) {
/* 15 */     this(buttonId, x, y, 200, 20, buttonText);
/*    */   }
/*    */   
/*    */   public GuiAltButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
/* 19 */     super(buttonId, x, y, widthIn, heightIn, buttonText);
/*    */   }
/*    */   
/*    */   public void drawButton(Minecraft mc, int mouseX, int mouseY, float mouseButton) {
/* 23 */     if (this.visible) {
/* 24 */       this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
/* 25 */       Color text = new Color(215, 215, 215, 255);
/* 26 */       Color color = new Color(this.fade + 14, this.fade + 14, this.fade + 14, 120);
/* 27 */       if (this.hovered) {
/* 28 */         if (this.fade < 100) {
/* 29 */           this.fade += 7;
/*    */         }
/* 31 */         text = Color.white;
/*    */       }
/* 33 */       else if (this.fade > 20) {
/* 34 */         this.fade -= 7;
/*    */       } 
/*    */ 
/*    */       
/* 38 */       Color colorOutline = new Color(this.fade + 60, this.fade, this.fade, 255);
/* 39 */       if (this.hovered) {
/* 40 */         if (this.fadeOutline < 100) {
/* 41 */           this.fadeOutline += 7;
/*    */         }
/*    */       }
/* 44 */       else if (this.fadeOutline > 20) {
/* 45 */         this.fadeOutline -= 7;
/*    */       } 
/*    */ 
/*    */       
/* 49 */       RectHelper.drawOutlineRect(this.xPosition, this.yPosition, this.width, this.height, color, colorOutline);
/* 50 */       mc.circleregular.drawCenteredString(this.displayString, this.xPosition + this.width / 2.0F, this.yPosition + (this.height - 7.5F) / 2.0F, text.getRGB());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\button\GuiAltButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */