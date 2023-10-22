package cn.hanabi.altmanager;

import cn.hanabi.Hanabi;
import cn.hanabi.utils.RenderUtil;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

public final class GuiAltLogin extends GuiScreen {
   private final GuiScreen previousScreen;
   private PasswordField password;
   private AltLoginThread thread;
   private GuiTextField username;

   public GuiAltLogin(GuiScreen previousScreen) {
      this.previousScreen = previousScreen;
   }

   protected void actionPerformed(GuiButton button) {
      try {
         switch (button.id) {
            case 0:
               (this.thread = new AltLoginThread(new Alt(this.username.getText(), this.password.getText()))).start();
               break;
            case 1:
               this.mc.displayGuiScreen(this.previousScreen);
               break;
            case 2:
               String data = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
               if (data.contains(":")) {
                  String[] credentials = data.split(":");
                  this.username.setText(credentials[0]);
                  this.password.setText(credentials[1]);
               }
               break;
            case 3:
               Hanabi.INSTANCE.mslogin = !Hanabi.INSTANCE.mslogin;
               this.mc.displayGuiScreen(new GuiAltLogin(new GuiAltManager()));
         }
      } catch (Throwable var4) {
         var4.printStackTrace();
      }

   }

   public void drawScreen(int x, int y, float z) {
      this.drawDefaultBackground();
      ScaledResolution res = new ScaledResolution(this.mc);
      RenderUtil.drawRect(0.0F, 0.0F, (float)res.getScaledWidth(), (float)res.getScaledHeight(), 0);
      this.username.drawTextBox();
      this.password.drawTextBox();
      this.drawCenteredString(this.mc.fontRendererObj, "Alt Login", this.width / 2, 20, -1);
      this.drawCenteredString(this.mc.fontRendererObj, this.thread == null ? EnumChatFormatting.GRAY + "Idle..." : this.thread.getStatus(), this.width / 2, 29, -1);
      if (this.username.getText().isEmpty()) {
         this.drawString(this.mc.fontRendererObj, "Username / E-Mail", this.width / 2 - 96, 66, -7829368);
      }

      if (this.password.getText().isEmpty()) {
         this.drawString(this.mc.fontRendererObj, "Password", this.width / 2 - 96, 106, -7829368);
      }

      super.drawScreen(x, y, z);
   }

   public void initGui() {
      int var3 = this.height / 4 + 24;
      this.buttonList.add(new GuiButton(0, this.width / 2 - 100, var3 + 72 + 12, "Login"));
      this.buttonList.add(new GuiButton(1, this.width / 2 - 100, var3 + 72 + 12 + 24, "Back"));
      this.buttonList.add(new GuiButton(2, this.width / 2 - 100, var3 + 72 + 12 + 48, "Import user:pass"));
      this.username = new GuiTextField(var3, this.mc.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
      this.password = new PasswordField(this.mc.fontRendererObj, this.width / 2 - 100, 100, 200, 20);
      this.username.setFocused(true);
      Keyboard.enableRepeatEvents(true);
   }

   protected void keyTyped(char character, int key) {
      try {
         super.keyTyped(character, key);
      } catch (IOException var4) {
         var4.printStackTrace();
      }

      if (character == '\t') {
         if (!this.username.isFocused() && !this.password.isFocused()) {
            this.username.setFocused(true);
         } else {
            this.username.setFocused(this.password.isFocused());
            this.password.setFocused(!this.username.isFocused());
         }
      }

      if (character == '\r') {
         this.actionPerformed((GuiButton)this.buttonList.get(0));
      }

      this.username.textboxKeyTyped(character, key);
      this.password.textboxKeyTyped(character, key);
   }

   protected void mouseClicked(int x, int y, int button) {
      try {
         super.mouseClicked(x, y, button);
      } catch (IOException var5) {
         var5.printStackTrace();
      }

      this.username.mouseClicked(x, y, button);
      this.password.mouseClicked(x, y, button);
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   public void updateScreen() {
      this.username.updateCursorCounter();
      this.password.updateCursorCounter();
   }
}
