package optifine;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiMessage extends GuiScreen {
   private GuiScreen parentScreen;
   private String messageLine1;
   private String messageLine2;
   private final List listLines2 = Lists.newArrayList();
   protected String confirmButtonText;
   private int ticksUntilEnable;

   public GuiMessage(GuiScreen var1, String var2, String var3) {
      this.parentScreen = var1;
      this.messageLine1 = var2;
      this.messageLine2 = var3;
      this.confirmButtonText = I18n.format("gui.done");
   }

   public void initGui() {
      this.buttonList.add(new GuiOptionButton(0, width / 2 - 74, height / 6 + 96, this.confirmButtonText));
      this.listLines2.clear();
      this.listLines2.addAll(this.fontRendererObj.listFormattedStringToWidth(this.messageLine2, width - 50));
   }

   protected void actionPerformed(GuiButton var1) throws IOException {
      Config.getMinecraft().displayGuiScreen(this.parentScreen);
   }

   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRendererObj, this.messageLine1, width / 2, 70, 16777215);
      int var4 = 90;

      for(Iterator var6 = this.listLines2.iterator(); var6.hasNext(); var4 += this.fontRendererObj.FONT_HEIGHT) {
         Object var5 = var6.next();
         this.drawCenteredString(this.fontRendererObj, (String)var5, width / 2, var4, 16777215);
      }

      super.drawScreen(var1, var2, var3);
   }

   public void setButtonDelay(int var1) {
      this.ticksUntilEnable = var1;

      GuiButton var2;
      for(Iterator var3 = this.buttonList.iterator(); var3.hasNext(); var2.enabled = false) {
         var2 = (GuiButton)var3.next();
      }

   }

   public void updateScreen() {
      super.updateScreen();
      GuiButton var1;
      if (--this.ticksUntilEnable == 0) {
         for(Iterator var2 = this.buttonList.iterator(); var2.hasNext(); var1.enabled = true) {
            var1 = (GuiButton)var2.next();
         }
      }

   }
}
