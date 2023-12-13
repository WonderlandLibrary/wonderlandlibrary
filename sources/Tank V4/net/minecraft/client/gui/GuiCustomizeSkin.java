package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EnumPlayerModelParts;

public class GuiCustomizeSkin extends GuiScreen {
   private String title;
   private final GuiScreen parentScreen;

   public void initGui() {
      int var1 = 0;
      this.title = I18n.format("options.skinCustomisation.title");
      EnumPlayerModelParts[] var5;
      int var4 = (var5 = EnumPlayerModelParts.values()).length;

      for(int var3 = 0; var3 < var4; ++var3) {
         EnumPlayerModelParts var2 = var5[var3];
         this.buttonList.add(new GuiCustomizeSkin.ButtonPart(this, var2.getPartId(), width / 2 - 155 + var1 % 2 * 160, height / 6 + 24 * (var1 >> 1), 150, 20, var2, (GuiCustomizeSkin.ButtonPart)null));
         ++var1;
      }

      if (var1 % 2 == 1) {
         ++var1;
      }

      this.buttonList.add(new GuiButton(200, width / 2 - 100, height / 6 + 24 * (var1 >> 1), I18n.format("gui.done")));
   }

   static String access$0(GuiCustomizeSkin var0, EnumPlayerModelParts var1) {
      return var0.func_175358_a(var1);
   }

   protected void actionPerformed(GuiButton var1) throws IOException {
      if (var1.enabled) {
         if (var1.id == 200) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(this.parentScreen);
         } else if (var1 instanceof GuiCustomizeSkin.ButtonPart) {
            EnumPlayerModelParts var2 = GuiCustomizeSkin.ButtonPart.access$1((GuiCustomizeSkin.ButtonPart)var1);
            this.mc.gameSettings.switchModelPartEnabled(var2);
            var1.displayString = this.func_175358_a(var2);
         }
      }

   }

   public GuiCustomizeSkin(GuiScreen var1) {
      this.parentScreen = var1;
   }

   private String func_175358_a(EnumPlayerModelParts var1) {
      String var2;
      if (this.mc.gameSettings.getModelParts().contains(var1)) {
         var2 = I18n.format("options.on");
      } else {
         var2 = I18n.format("options.off");
      }

      return var1.func_179326_d().getFormattedText() + ": " + var2;
   }

   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRendererObj, this.title, width / 2, 20, 16777215);
      super.drawScreen(var1, var2, var3);
   }

   class ButtonPart extends GuiButton {
      final GuiCustomizeSkin this$0;
      private final EnumPlayerModelParts playerModelParts;

      ButtonPart(GuiCustomizeSkin var1, int var2, int var3, int var4, int var5, int var6, EnumPlayerModelParts var7, GuiCustomizeSkin.ButtonPart var8) {
         this(var1, var2, var3, var4, var5, var6, var7);
      }

      private ButtonPart(GuiCustomizeSkin var1, int var2, int var3, int var4, int var5, int var6, EnumPlayerModelParts var7) {
         super(var2, var3, var4, var5, var6, GuiCustomizeSkin.access$0(var1, var7));
         this.this$0 = var1;
         this.playerModelParts = var7;
      }

      static EnumPlayerModelParts access$1(GuiCustomizeSkin.ButtonPart var0) {
         return var0.playerModelParts;
      }
   }
}
