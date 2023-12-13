package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

public class GuiScreenServerList extends GuiScreen {
   private final GuiScreen field_146303_a;
   private GuiTextField field_146302_g;
   private final ServerData field_146301_f;

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
      this.mc.gameSettings.lastServer = this.field_146302_g.getText();
      this.mc.gameSettings.saveOptions();
   }

   public void initGui() {
      Keyboard.enableRepeatEvents(true);
      this.buttonList.clear();
      this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + 12, I18n.format("selectServer.select")));
      this.buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 120 + 12, I18n.format("gui.cancel")));
      this.field_146302_g = new GuiTextField(2, this.fontRendererObj, width / 2 - 100, 116, 200, 20);
      this.field_146302_g.setMaxStringLength(128);
      this.field_146302_g.setFocused(true);
      this.field_146302_g.setText(this.mc.gameSettings.lastServer);
      ((GuiButton)this.buttonList.get(0)).enabled = this.field_146302_g.getText().length() > 0 && this.field_146302_g.getText().split(":").length > 0;
   }

   protected void mouseClicked(int var1, int var2, int var3) throws IOException {
      super.mouseClicked(var1, var2, var3);
      this.field_146302_g.mouseClicked(var1, var2, var3);
   }

   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRendererObj, I18n.format("selectServer.direct"), width / 2, 20, 16777215);
      this.drawString(this.fontRendererObj, I18n.format("addServer.enterIp"), width / 2 - 100, 100, 10526880);
      this.field_146302_g.drawTextBox();
      super.drawScreen(var1, var2, var3);
   }

   public void updateScreen() {
      this.field_146302_g.updateCursorCounter();
   }

   public GuiScreenServerList(GuiScreen var1, ServerData var2) {
      this.field_146303_a = var1;
      this.field_146301_f = var2;
   }

   protected void actionPerformed(GuiButton var1) throws IOException {
      if (var1.enabled) {
         if (var1.id == 1) {
            this.field_146303_a.confirmClicked(false, 0);
         } else if (var1.id == 0) {
            this.field_146301_f.serverIP = this.field_146302_g.getText();
            this.field_146303_a.confirmClicked(true, 0);
         }
      }

   }

   protected void keyTyped(char var1, int var2) throws IOException {
      if (this.field_146302_g.textboxKeyTyped(var1, var2)) {
         ((GuiButton)this.buttonList.get(0)).enabled = this.field_146302_g.getText().length() > 0 && this.field_146302_g.getText().split(":").length > 0;
      } else if (var2 == 28 || var2 == 156) {
         this.actionPerformed((GuiButton)this.buttonList.get(0));
      }

   }
}
