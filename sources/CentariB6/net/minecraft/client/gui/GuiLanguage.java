package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.GuiLanguage.List;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.settings.GameSettings;

public class GuiLanguage extends GuiScreen {
   protected GuiScreen parentScreen;
   private List list;
   private final GameSettings game_settings_3;
   private final LanguageManager languageManager;
   private GuiOptionButton forceUnicodeFontBtn;
   private GuiOptionButton confirmSettingsBtn;

   public GuiLanguage(GuiScreen screen, GameSettings gameSettingsObj, LanguageManager manager) {
      this.parentScreen = screen;
      this.game_settings_3 = gameSettingsObj;
      this.languageManager = manager;
   }

   // $FF: synthetic method
   static GameSettings access$100(GuiLanguage x0) {
      return x0.game_settings_3;
   }

   // $FF: synthetic method
   static GuiOptionButton access$200(GuiLanguage x0) {
      return x0.confirmSettingsBtn;
   }

   // $FF: synthetic method
   static GuiOptionButton access$300(GuiLanguage x0) {
      return x0.forceUnicodeFontBtn;
   }

   // $FF: synthetic method
   static LanguageManager access$000(GuiLanguage x0) {
      return x0.languageManager;
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      if(button.enabled) {
         switch(button.id) {
         case 5:
            break;
         case 6:
            this.mc.displayGuiScreen(this.parentScreen);
            break;
         case 100:
            if(button instanceof GuiOptionButton) {
               this.game_settings_3.setOptionValue(((GuiOptionButton)button).returnEnumOptions(), 1);
               button.displayString = this.game_settings_3.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT);
               ScaledResolution scaledresolution = new ScaledResolution(this.mc);
               int i = scaledresolution.getScaledWidth();
               int j = scaledresolution.getScaledHeight();
               this.setWorldAndResolution(this.mc, i, j);
            }
            break;
         default:
            this.list.actionPerformed(button);
         }
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.list.drawScreen(mouseX, mouseY, partialTicks);
      this.drawCenteredString(this.fontRendererObj, I18n.format("options.language", new Object[0]), this.width / 2, 16, 16777215);
      this.drawCenteredString(this.fontRendererObj, "(" + I18n.format("options.languageWarning", new Object[0]) + ")", this.width / 2, this.height - 56, 8421504);
      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   public void handleMouseInput() throws IOException {
      super.handleMouseInput();
      this.list.handleMouseInput();
   }

   public void initGui() {
      this.buttonList.add(this.forceUnicodeFontBtn = new GuiOptionButton(100, this.width / 2 - 155, this.height - 38, GameSettings.Options.FORCE_UNICODE_FONT, this.game_settings_3.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT)));
      this.buttonList.add(this.confirmSettingsBtn = new GuiOptionButton(6, this.width / 2 - 155 + 160, this.height - 38, I18n.format("gui.done", new Object[0])));
      this.list = new List(this, this.mc);
      this.list.registerScrollButtons(7, 8);
   }
}
