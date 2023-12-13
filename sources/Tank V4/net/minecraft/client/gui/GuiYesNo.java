package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.resources.I18n;

public class GuiYesNo extends GuiScreen {
   protected String cancelButtonText;
   protected String confirmButtonText;
   protected String messageLine1;
   protected int parentButtonClickedId;
   protected GuiYesNoCallback parentScreen;
   private String messageLine2;
   private final List field_175298_s = Lists.newArrayList();
   private int ticksUntilEnable;

   public GuiYesNo(GuiYesNoCallback var1, String var2, String var3, String var4, String var5, int var6) {
      this.parentScreen = var1;
      this.messageLine1 = var2;
      this.messageLine2 = var3;
      this.confirmButtonText = var4;
      this.cancelButtonText = var5;
      this.parentButtonClickedId = var6;
   }

   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRendererObj, this.messageLine1, width / 2, 70, 16777215);
      int var4 = 90;

      for(Iterator var6 = this.field_175298_s.iterator(); var6.hasNext(); var4 += this.fontRendererObj.FONT_HEIGHT) {
         String var5 = (String)var6.next();
         this.drawCenteredString(this.fontRendererObj, var5, width / 2, var4, 16777215);
      }

      super.drawScreen(var1, var2, var3);
   }

   public GuiYesNo(GuiYesNoCallback var1, String var2, String var3, int var4) {
      this.parentScreen = var1;
      this.messageLine1 = var2;
      this.messageLine2 = var3;
      this.parentButtonClickedId = var4;
      this.confirmButtonText = I18n.format("gui.yes");
      this.cancelButtonText = I18n.format("gui.no");
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

   protected void actionPerformed(GuiButton var1) throws IOException {
      this.parentScreen.confirmClicked(var1.id == 0, this.parentButtonClickedId);
   }

   public void initGui() {
      this.buttonList.add(new GuiOptionButton(0, width / 2 - 155, height / 6 + 96, this.confirmButtonText));
      this.buttonList.add(new GuiOptionButton(1, width / 2 - 155 + 160, height / 6 + 96, this.cancelButtonText));
      this.field_175298_s.clear();
      this.field_175298_s.addAll(this.fontRendererObj.listFormattedStringToWidth(this.messageLine2, width - 50));
   }

   public void setButtonDelay(int var1) {
      this.ticksUntilEnable = var1;

      GuiButton var2;
      for(Iterator var3 = this.buttonList.iterator(); var3.hasNext(); var2.enabled = false) {
         var2 = (GuiButton)var3.next();
      }

   }
}
