/*    */ package org.neverhook.client.ui.newclickgui.settings;
/*    */ 
/*    */ import com.mojang.realmsclient.gui.ChatFormatting;
/*    */ import java.awt.Color;
/*    */ import java.io.IOException;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.ScaledResolution;
/*    */ import org.lwjgl.input.Keyboard;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.helpers.Helper;
/*    */ 
/*    */ public class KeybindButton
/*    */   implements Helper
/*    */ {
/*    */   private final Feature feature;
/*    */   public boolean isBinding;
/*    */   private int x;
/*    */   
/*    */   public KeybindButton(Feature feature) {
/* 20 */     this.feature = feature;
/*    */   }
/*    */   private int y; private int width; private int height;
/*    */   public void drawScreen() {
/* 24 */     String keybind = this.isBinding ? "..." : Keyboard.getKeyName(this.feature.getBind());
/* 25 */     String text = ">> " + ChatFormatting.GRAY + "Keybind: " + keybind;
/* 26 */     ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getInstance());
/* 27 */     mc.circleregular.drawStringWithShadow(text, (scaledResolution.getScaledWidth() / 2.0F - 140.0F), (this.y + this.height / 2.0F - mc.circleregular.getFontHeight() / 2.0F - 3.0F), (new Color(123, 153, 183)).getRGB());
/*    */   }
/*    */   
/*    */   public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/* 31 */     if (isHovering(mouseX, mouseY) && mouseButton == 0) {
/* 32 */       this.isBinding = true;
/*    */     }
/*    */   }
/*    */   
/*    */   public void keyTyped(int key) {
/* 37 */     if (!this.isBinding) {
/*    */       return;
/*    */     }
/* 40 */     if (key == 1) {
/* 41 */       this.isBinding = false;
/*    */     }
/*    */     
/* 44 */     int newValue = (Keyboard.getEventKey() == 0) ? (Keyboard.getEventCharacter() + 256) : Keyboard.getEventKey();
/* 45 */     if (key == 211) {
/* 46 */       this.feature.setBind(0);
/* 47 */     } else if (key == 54) {
/* 48 */       this.feature.setBind(54);
/*    */     } else {
/* 50 */       this.feature.setBind(newValue);
/*    */     } 
/*    */ 
/*    */     
/* 54 */     this.isBinding = false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setPosition(int x, int y, int width, int height) {
/* 59 */     this.x = x;
/* 60 */     this.y = y;
/* 61 */     this.width = width;
/* 62 */     this.height = height;
/*    */   }
/*    */   
/*    */   public boolean isHovering(int mouseX, int mouseY) {
/* 66 */     return (mouseX > this.x && mouseX < this.x + this.width + 4 && mouseY > this.y && mouseY < this.y + this.height);
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\newclickgui\settings\KeybindButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */