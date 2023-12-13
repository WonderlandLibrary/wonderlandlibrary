package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;

public class ScreenChatOptions extends GuiScreen {
   private final GuiScreen parentScreen;
   private final GameSettings game_settings;
   private String field_146401_i;
   private static final GameSettings.Options[] field_146399_a;

   public void initGui() {
      int var1 = 0;
      this.field_146401_i = I18n.format("options.chat.title");
      GameSettings.Options[] var5;
      int var4 = (var5 = field_146399_a).length;

      for(int var3 = 0; var3 < var4; ++var3) {
         GameSettings.Options var2 = var5[var3];
         if (var2.getEnumFloat()) {
            this.buttonList.add(new GuiOptionSlider(var2.returnEnumOrdinal(), width / 2 - 155 + var1 % 2 * 160, height / 6 + 24 * (var1 >> 1), var2));
         } else {
            this.buttonList.add(new GuiOptionButton(var2.returnEnumOrdinal(), width / 2 - 155 + var1 % 2 * 160, height / 6 + 24 * (var1 >> 1), var2, this.game_settings.getKeyBinding(var2)));
         }

         ++var1;
      }

      this.buttonList.add(new GuiButton(200, width / 2 - 100, height / 6 + 120, I18n.format("gui.done")));
   }

   public ScreenChatOptions(GuiScreen var1, GameSettings var2) {
      this.parentScreen = var1;
      this.game_settings = var2;
   }

   static {
      field_146399_a = new GameSettings.Options[]{GameSettings.Options.CHAT_VISIBILITY, GameSettings.Options.CHAT_COLOR, GameSettings.Options.CHAT_LINKS, GameSettings.Options.CHAT_OPACITY, GameSettings.Options.CHAT_LINKS_PROMPT, GameSettings.Options.CHAT_SCALE, GameSettings.Options.CHAT_HEIGHT_FOCUSED, GameSettings.Options.CHAT_HEIGHT_UNFOCUSED, GameSettings.Options.CHAT_WIDTH, GameSettings.Options.REDUCED_DEBUG_INFO};
   }

   protected void actionPerformed(GuiButton var1) throws IOException {
      if (var1.enabled) {
         if (var1.id < 100 && var1 instanceof GuiOptionButton) {
            this.game_settings.setOptionValue(((GuiOptionButton)var1).returnEnumOptions(), 1);
            var1.displayString = this.game_settings.getKeyBinding(GameSettings.Options.getEnumOptions(var1.id));
         }

         if (var1.id == 200) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(this.parentScreen);
         }
      }

   }

   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRendererObj, this.field_146401_i, width / 2, 20, 16777215);
      super.drawScreen(var1, var2, var3);
   }
}
