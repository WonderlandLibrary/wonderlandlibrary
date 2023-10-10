package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import optifine.Config;
import optifine.GuiAnimationSettingsOF;
import optifine.GuiDetailSettingsOF;
import optifine.GuiOptionButtonOF;
import optifine.GuiOptionSliderOF;
import optifine.GuiOtherSettingsOF;
import optifine.GuiPerformanceSettingsOF;
import optifine.GuiQualitySettingsOF;
import optifine.Lang;
import optifine.TooltipManager;
import shadersmod.client.GuiShaders;

public class GuiVideoSettings extends GuiScreen {
   private static final String __OBFID = "CL_00000718";
   private TooltipManager tooltipManager = new TooltipManager(this);
   private GuiScreen parentGuiScreen;
   private static GameSettings.Options[] videoOptions;
   protected String screenTitle = "Video Settings";
   private GameSettings guiGameSettings;

   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRendererObj, this.screenTitle, width / 2, 15, 16777215);
      String var4 = Config.getVersion();
      String var5 = "HD_U";
      if (var5.equals("HD")) {
         var4 = "OptiFine HD H8";
      }

      if (var5.equals("HD_U")) {
         var4 = "OptiFine HD H8 Ultra";
      }

      if (var5.equals("L")) {
         var4 = "OptiFine H8 Light";
      }

      this.drawString(this.fontRendererObj, var4, 2, height - 10, 8421504);
      String var6 = "Minecraft 1.8.8";
      int var7 = this.fontRendererObj.getStringWidth(var6);
      this.drawString(this.fontRendererObj, var6, width - var7 - 2, height - 10, 8421504);
      super.drawScreen(var1, var2, var3);
      this.tooltipManager.drawTooltips(var1, var2, this.buttonList);
   }

   public void initGui() {
      this.screenTitle = I18n.format("options.videoTitle");
      this.buttonList.clear();

      int var1;
      for(var1 = 0; var1 < videoOptions.length; ++var1) {
         GameSettings.Options var2 = videoOptions[var1];
         if (var2 != null) {
            int var3 = width / 2 - 155 + var1 % 2 * 160;
            int var4 = height / 6 + 21 * (var1 / 2) - 12;
            if (var2.getEnumFloat()) {
               this.buttonList.add(new GuiOptionSliderOF(var2.returnEnumOrdinal(), var3, var4, var2));
            } else {
               this.buttonList.add(new GuiOptionButtonOF(var2.returnEnumOrdinal(), var3, var4, var2, this.guiGameSettings.getKeyBinding(var2)));
            }
         }
      }

      var1 = height / 6 + 21 * (videoOptions.length / 2) - 12;
      boolean var5 = false;
      int var6 = width / 2 - 155 + 0;
      this.buttonList.add(new GuiOptionButton(231, var6, var1, Lang.get("of.options.shaders")));
      var6 = width / 2 - 155 + 160;
      this.buttonList.add(new GuiOptionButton(202, var6, var1, Lang.get("of.options.quality")));
      var1 += 21;
      var6 = width / 2 - 155 + 0;
      this.buttonList.add(new GuiOptionButton(201, var6, var1, Lang.get("of.options.details")));
      var6 = width / 2 - 155 + 160;
      this.buttonList.add(new GuiOptionButton(212, var6, var1, Lang.get("of.options.performance")));
      var1 += 21;
      var6 = width / 2 - 155 + 0;
      this.buttonList.add(new GuiOptionButton(211, var6, var1, Lang.get("of.options.animations")));
      var6 = width / 2 - 155 + 160;
      this.buttonList.add(new GuiOptionButton(222, var6, var1, Lang.get("of.options.other")));
      var1 += 21;
      this.buttonList.add(new GuiButton(200, width / 2 - 100, height / 6 + 168 + 11, I18n.format("gui.done")));
   }

   public static void drawGradientRect(GuiScreen var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      GuiScreen.drawGradientRect((double)var1, (double)var2, (float)var3, (float)var4, var5, var6);
   }

   static {
      videoOptions = new GameSettings.Options[]{GameSettings.Options.GRAPHICS, GameSettings.Options.RENDER_DISTANCE, GameSettings.Options.AMBIENT_OCCLUSION, GameSettings.Options.FRAMERATE_LIMIT, GameSettings.Options.AO_LEVEL, GameSettings.Options.VIEW_BOBBING, GameSettings.Options.GUI_SCALE, GameSettings.Options.USE_VBO, GameSettings.Options.GAMMA, GameSettings.Options.BLOCK_ALTERNATIVES, GameSettings.Options.FOG_FANCY, GameSettings.Options.FOG_START};
   }

   public static int getButtonHeight(GuiButton var0) {
      return var0.height;
   }

   protected void actionPerformed(GuiButton var1) throws IOException {
      if (var1.enabled) {
         int var2 = this.guiGameSettings.guiScale;
         if (var1.id < 200 && var1 instanceof GuiOptionButton) {
            this.guiGameSettings.setOptionValue(((GuiOptionButton)var1).returnEnumOptions(), 1);
            var1.displayString = this.guiGameSettings.getKeyBinding(GameSettings.Options.getEnumOptions(var1.id));
         }

         if (var1.id == 200) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(this.parentGuiScreen);
         }

         if (this.guiGameSettings.guiScale != var2) {
            ScaledResolution var3 = new ScaledResolution(this.mc);
            int var4 = var3.getScaledWidth();
            int var5 = ScaledResolution.getScaledHeight();
            this.setWorldAndResolution(this.mc, var4, var5);
         }

         if (var1.id == 201) {
            this.mc.gameSettings.saveOptions();
            GuiDetailSettingsOF var6 = new GuiDetailSettingsOF(this, this.guiGameSettings);
            this.mc.displayGuiScreen(var6);
         }

         if (var1.id == 202) {
            this.mc.gameSettings.saveOptions();
            GuiQualitySettingsOF var7 = new GuiQualitySettingsOF(this, this.guiGameSettings);
            this.mc.displayGuiScreen(var7);
         }

         if (var1.id == 211) {
            this.mc.gameSettings.saveOptions();
            GuiAnimationSettingsOF var8 = new GuiAnimationSettingsOF(this, this.guiGameSettings);
            this.mc.displayGuiScreen(var8);
         }

         if (var1.id == 212) {
            this.mc.gameSettings.saveOptions();
            GuiPerformanceSettingsOF var9 = new GuiPerformanceSettingsOF(this, this.guiGameSettings);
            this.mc.displayGuiScreen(var9);
         }

         if (var1.id == 222) {
            this.mc.gameSettings.saveOptions();
            GuiOtherSettingsOF var10 = new GuiOtherSettingsOF(this, this.guiGameSettings);
            this.mc.displayGuiScreen(var10);
         }

         if (var1.id == 231) {
            if (Config.isAntialiasing() || Config.isAntialiasingConfigured()) {
               Config.showGuiMessage(Lang.get("of.message.shaders.aa1"), Lang.get("of.message.shaders.aa2"));
               return;
            }

            if (Config.isAnisotropicFiltering()) {
               Config.showGuiMessage(Lang.get("of.message.shaders.af1"), Lang.get("of.message.shaders.af2"));
               return;
            }

            if (Config.isFastRender()) {
               Config.showGuiMessage(Lang.get("of.message.shaders.fr1"), Lang.get("of.message.shaders.fr2"));
               return;
            }

            this.mc.gameSettings.saveOptions();
            GuiShaders var11 = new GuiShaders(this, this.guiGameSettings);
            this.mc.displayGuiScreen(var11);
         }
      }

   }

   public static int getButtonWidth(GuiButton var0) {
      return var0.width;
   }

   public GuiVideoSettings(GuiScreen var1, GameSettings var2) {
      this.parentGuiScreen = var1;
      this.guiGameSettings = var2;
   }
}
