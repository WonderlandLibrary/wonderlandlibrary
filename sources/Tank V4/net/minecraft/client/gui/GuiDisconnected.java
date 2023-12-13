package net.minecraft.client.gui;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IChatComponent;

public class GuiDisconnected extends GuiScreen {
   private List multilineMessage;
   private String reason;
   private IChatComponent message;
   private int field_175353_i;
   private final GuiScreen parentScreen;

   public void initGui() {
      this.buttonList.clear();
      this.multilineMessage = this.fontRendererObj.listFormattedStringToWidth(this.message.getFormattedText(), width - 50);
      this.field_175353_i = this.multilineMessage.size() * this.fontRendererObj.FONT_HEIGHT;
      this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT, I18n.format("gui.toMenu")));
   }

   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRendererObj, this.reason, width / 2, height / 2 - this.field_175353_i / 2 - this.fontRendererObj.FONT_HEIGHT * 2, 11184810);
      int var4 = height / 2 - this.field_175353_i / 2;
      if (this.multilineMessage != null) {
         for(Iterator var6 = this.multilineMessage.iterator(); var6.hasNext(); var4 += this.fontRendererObj.FONT_HEIGHT) {
            String var5 = (String)var6.next();
            this.drawCenteredString(this.fontRendererObj, var5, width / 2, var4, 16777215);
         }
      }

      super.drawScreen(var1, var2, var3);
   }

   public GuiDisconnected(GuiScreen var1, String var2, IChatComponent var3) {
      this.parentScreen = var1;
      this.reason = I18n.format(var2);
      this.message = var3;
   }

   protected void keyTyped(char var1, int var2) throws IOException {
   }

   protected void actionPerformed(GuiButton var1) throws IOException {
      if (var1.id == 0) {
         this.mc.displayGuiScreen(this.parentScreen);
      }

   }
}
