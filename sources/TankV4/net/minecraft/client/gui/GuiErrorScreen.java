package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.resources.I18n;

public class GuiErrorScreen extends GuiScreen {
   private String field_146313_a;
   private String field_146312_f;

   public GuiErrorScreen(String var1, String var2) {
      this.field_146313_a = var1;
      this.field_146312_f = var2;
   }

   public void initGui() {
      super.initGui();
      this.buttonList.add(new GuiButton(0, width / 2 - 100, 140, I18n.format("gui.cancel")));
   }

   protected void actionPerformed(GuiButton var1) throws IOException {
      this.mc.displayGuiScreen((GuiScreen)null);
   }

   protected void keyTyped(char var1, int var2) throws IOException {
   }

   public void drawScreen(int var1, int var2, float var3) {
      drawGradientRect(0.0D, 0.0D, (float)width, (float)height, -12574688, -11530224);
      this.drawCenteredString(this.fontRendererObj, this.field_146313_a, width / 2, 90, 16777215);
      this.drawCenteredString(this.fontRendererObj, this.field_146312_f, width / 2, 110, 16777215);
      super.drawScreen(var1, var2, var3);
   }
}
