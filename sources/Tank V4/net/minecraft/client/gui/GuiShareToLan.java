package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.WorldSettings;

public class GuiShareToLan extends GuiScreen {
   private boolean field_146600_i;
   private GuiButton field_146597_g;
   private GuiButton field_146596_f;
   private String field_146599_h = "survival";
   private final GuiScreen field_146598_a;

   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRendererObj, I18n.format("lanServer.title"), width / 2, 50, 16777215);
      this.drawCenteredString(this.fontRendererObj, I18n.format("lanServer.otherPlayers"), width / 2, 82, 16777215);
      super.drawScreen(var1, var2, var3);
   }

   public void initGui() {
      this.buttonList.clear();
      this.buttonList.add(new GuiButton(101, width / 2 - 155, height - 28, 150, 20, I18n.format("lanServer.start")));
      this.buttonList.add(new GuiButton(102, width / 2 + 5, height - 28, 150, 20, I18n.format("gui.cancel")));
      this.buttonList.add(this.field_146597_g = new GuiButton(104, width / 2 - 155, 100, 150, 20, I18n.format("selectWorld.gameMode")));
      this.buttonList.add(this.field_146596_f = new GuiButton(103, width / 2 + 5, 100, 150, 20, I18n.format("selectWorld.allowCommands")));
      this.func_146595_g();
   }

   protected void actionPerformed(GuiButton var1) throws IOException {
      if (var1.id == 102) {
         this.mc.displayGuiScreen(this.field_146598_a);
      } else if (var1.id == 104) {
         if (this.field_146599_h.equals("spectator")) {
            this.field_146599_h = "creative";
         } else if (this.field_146599_h.equals("creative")) {
            this.field_146599_h = "adventure";
         } else if (this.field_146599_h.equals("adventure")) {
            this.field_146599_h = "survival";
         } else {
            this.field_146599_h = "spectator";
         }

         this.func_146595_g();
      } else if (var1.id == 103) {
         this.field_146600_i = !this.field_146600_i;
         this.func_146595_g();
      } else if (var1.id == 101) {
         this.mc.displayGuiScreen((GuiScreen)null);
         String var2 = this.mc.getIntegratedServer().shareToLAN(WorldSettings.GameType.getByName(this.field_146599_h), this.field_146600_i);
         Object var3;
         if (var2 != null) {
            var3 = new ChatComponentTranslation("commands.publish.started", new Object[]{var2});
         } else {
            var3 = new ChatComponentText("commands.publish.failed");
         }

         this.mc.ingameGUI.getChatGUI().printChatMessage((IChatComponent)var3);
      }

   }

   public GuiShareToLan(GuiScreen var1) {
      this.field_146598_a = var1;
   }

   private void func_146595_g() {
      this.field_146597_g.displayString = I18n.format("selectWorld.gameMode") + " " + I18n.format("selectWorld.gameMode." + this.field_146599_h);
      this.field_146596_f.displayString = I18n.format("selectWorld.allowCommands") + " ";
      if (this.field_146600_i) {
         this.field_146596_f.displayString = this.field_146596_f.displayString + I18n.format("options.on");
      } else {
         this.field_146596_f.displayString = this.field_146596_f.displayString + I18n.format("options.off");
      }

   }
}
