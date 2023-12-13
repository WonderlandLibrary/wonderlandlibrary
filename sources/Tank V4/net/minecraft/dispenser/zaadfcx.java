package net.minecraft.dispenser;

import java.awt.Color;
import java.io.IOException;
import my.NewSnake.utils.GuiTextBoxLogin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

public class zaadfcx extends GuiScreen {
   GuiTextBoxLogin loginBox;

   protected void keyTyped(char var1, int var2) throws IOException {
      if (var2 != 28 || !this.loginBox.getText().isEmpty()) {
         this.loginBox.textboxKeyTyped(var1, var2);
      }
   }

   public void drawScreen(int var1, int var2, float var3) {
      this.drawBackground2(var1);
      this.loginBox.drawTextBox(var1, var2);
      super.drawScreen(var1, var2, var3);
   }

   public static boolean isAllowedToRun() {
      return false;
   }

   public void updateScreen() {
      this.loginBox.updateCursorCounter();
      super.updateScreen();
   }

   public void initGui() {
      this.loginBox = new GuiTextBoxLogin(0, this.fontRendererObj, "Insira a Senha", width / 2 - 150, height / 2 - 16, 300, 30);
      this.buttonList.add((new GuiButton(0, width / 2 - 150, height / 2 + 15, 300, 20, "Login")).setBaseColor(new Color(100, 100, 100, 150)));
      Keyboard.enableRepeatEvents(true);
      super.initGui();
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
      super.onGuiClosed();
   }

   protected void actionPerformed(GuiButton var1) throws IOException {
      String var2 = this.loginBox.getText();
      if (var1.id == 0) {
         if (this.loginBox.getText().isEmpty()) {
            return;
         }

         if (var1.id == 0 && var2.contains("@@A2!!__")) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
         }
      } else {
         int var10000 = var1.id;
      }

      super.actionPerformed(var1);
   }

   protected void mouseClicked(int var1, int var2, int var3) throws IOException {
      this.loginBox.mouseClicked(var1, var2, var3);
      super.mouseClicked(var1, var2, var3);
   }
}
