package net.minecraft.client.gui;

import com.google.common.base.Predicate;
import java.io.IOException;
import java.net.IDN;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

public class GuiScreenAddServer extends GuiScreen {
   private final GuiScreen parentScreen;
   private GuiTextField serverIPField;
   private GuiTextField serverNameField;
   private GuiButton serverResourcePacks;
   private final ServerData serverData;
   private Predicate field_181032_r = new Predicate(this) {
      final GuiScreenAddServer this$0;

      public boolean apply(Object var1) {
         return this.apply((String)var1);
      }

      public boolean apply(String var1) {
         if (var1.length() == 0) {
            return true;
         } else {
            String[] var2 = var1.split(":");
            if (var2.length == 0) {
               return true;
            } else {
               try {
                  String var3 = IDN.toASCII(var2[0]);
                  return true;
               } catch (IllegalArgumentException var4) {
                  return false;
               }
            }
         }
      }

      {
         this.this$0 = var1;
      }
   };

   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRendererObj, I18n.format("addServer.title"), width / 2, 17, 16777215);
      this.drawString(this.fontRendererObj, I18n.format("addServer.enterName"), width / 2 - 100, 53, 10526880);
      this.drawString(this.fontRendererObj, I18n.format("addServer.enterIp"), width / 2 - 100, 94, 10526880);
      this.serverNameField.drawTextBox();
      this.serverIPField.drawTextBox();
      super.drawScreen(var1, var2, var3);
   }

   protected void actionPerformed(GuiButton var1) throws IOException {
      if (var1.enabled) {
         if (var1.id == 2) {
            this.serverData.setResourceMode(ServerData.ServerResourceMode.values()[(this.serverData.getResourceMode().ordinal() + 1) % ServerData.ServerResourceMode.values().length]);
            this.serverResourcePacks.displayString = I18n.format("addServer.resourcePack") + ": " + this.serverData.getResourceMode().getMotd().getFormattedText();
         } else if (var1.id == 1) {
            this.parentScreen.confirmClicked(false, 0);
         } else if (var1.id == 0) {
            this.serverData.serverName = this.serverNameField.getText();
            this.serverData.serverIP = this.serverIPField.getText();
            this.parentScreen.confirmClicked(true, 0);
         }
      }

   }

   protected void mouseClicked(int var1, int var2, int var3) throws IOException {
      super.mouseClicked(var1, var2, var3);
      this.serverIPField.mouseClicked(var1, var2, var3);
      this.serverNameField.mouseClicked(var1, var2, var3);
   }

   public void initGui() {
      Keyboard.enableRepeatEvents(true);
      this.buttonList.clear();
      this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + 18, I18n.format("addServer.add")));
      this.buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 120 + 18, I18n.format("gui.cancel")));
      this.buttonList.add(this.serverResourcePacks = new GuiButton(2, width / 2 - 100, height / 4 + 72, I18n.format("addServer.resourcePack") + ": " + this.serverData.getResourceMode().getMotd().getFormattedText()));
      this.serverNameField = new GuiTextField(0, this.fontRendererObj, width / 2 - 100, 66, 200, 20);
      this.serverNameField.setFocused(true);
      this.serverNameField.setText(this.serverData.serverName);
      this.serverIPField = new GuiTextField(1, this.fontRendererObj, width / 2 - 100, 106, 200, 20);
      this.serverIPField.setMaxStringLength(128);
      this.serverIPField.setText(this.serverData.serverIP);
      this.serverIPField.func_175205_a(this.field_181032_r);
      ((GuiButton)this.buttonList.get(0)).enabled = this.serverIPField.getText().length() > 0 && this.serverIPField.getText().split(":").length > 0 && this.serverNameField.getText().length() > 0;
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   public GuiScreenAddServer(GuiScreen var1, ServerData var2) {
      this.parentScreen = var1;
      this.serverData = var2;
   }

   public void updateScreen() {
      this.serverNameField.updateCursorCounter();
      this.serverIPField.updateCursorCounter();
   }

   protected void keyTyped(char var1, int var2) throws IOException {
      this.serverNameField.textboxKeyTyped(var1, var2);
      this.serverIPField.textboxKeyTyped(var1, var2);
      if (var2 == 15) {
         this.serverNameField.setFocused(!this.serverNameField.isFocused());
         this.serverIPField.setFocused(!this.serverIPField.isFocused());
      }

      if (var2 == 28 || var2 == 156) {
         this.actionPerformed((GuiButton)this.buttonList.get(0));
      }

      ((GuiButton)this.buttonList.get(0)).enabled = this.serverIPField.getText().length() > 0 && this.serverIPField.getText().split(":").length > 0 && this.serverNameField.getText().length() > 0;
   }
}
