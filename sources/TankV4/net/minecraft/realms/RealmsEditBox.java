package net.minecraft.realms;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;

public class RealmsEditBox {
   private final GuiTextField editBox;

   public void tick() {
      this.editBox.updateCursorCounter();
   }

   public void setMaxLength(int var1) {
      this.editBox.setMaxStringLength(var1);
   }

   public void render() {
      this.editBox.drawTextBox();
   }

   public void keyPressed(char var1, int var2) {
      this.editBox.textboxKeyTyped(var1, var2);
   }

   public RealmsEditBox(int var1, int var2, int var3, int var4, int var5) {
      Minecraft.getMinecraft();
      this.editBox = new GuiTextField(var1, Minecraft.fontRendererObj, var2, var3, var4, var5);
   }

   public boolean isFocused() {
      return this.editBox.isFocused();
   }

   public String getValue() {
      return this.editBox.getText();
   }

   public void mouseClicked(int var1, int var2, int var3) {
      this.editBox.mouseClicked(var1, var2, var3);
   }

   public void setFocus(boolean var1) {
      this.editBox.setFocused(var1);
   }

   public void setIsEditable(boolean var1) {
      this.editBox.setEnabled(var1);
   }

   public void setValue(String var1) {
      this.editBox.setText(var1);
   }
}
