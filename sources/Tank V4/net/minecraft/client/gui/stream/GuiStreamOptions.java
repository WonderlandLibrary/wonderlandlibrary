package net.minecraft.client.gui.stream;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiOptionSlider;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.EnumChatFormatting;

public class GuiStreamOptions extends GuiScreen {
   private String field_152319_i;
   private final GameSettings field_152318_h;
   private String field_152313_r;
   private static final GameSettings.Options[] field_152316_f;
   private static final GameSettings.Options[] field_152312_a;
   private int field_152314_s;
   private boolean field_152315_t = false;
   private final GuiScreen parentScreen;

   protected void actionPerformed(GuiButton var1) throws IOException {
      if (var1.enabled) {
         if (var1.id < 100 && var1 instanceof GuiOptionButton) {
            GameSettings.Options var2 = ((GuiOptionButton)var1).returnEnumOptions();
            this.field_152318_h.setOptionValue(var2, 1);
            var1.displayString = this.field_152318_h.getKeyBinding(GameSettings.Options.getEnumOptions(var1.id));
            if (this.mc.getTwitchStream().isBroadcasting() && var2 != GameSettings.Options.STREAM_CHAT_ENABLED && var2 != GameSettings.Options.STREAM_CHAT_USER_FILTER) {
               this.field_152315_t = true;
            }
         } else if (var1 instanceof GuiOptionSlider) {
            if (var1.id == GameSettings.Options.STREAM_VOLUME_MIC.returnEnumOrdinal()) {
               this.mc.getTwitchStream().updateStreamVolume();
            } else if (var1.id == GameSettings.Options.STREAM_VOLUME_SYSTEM.returnEnumOrdinal()) {
               this.mc.getTwitchStream().updateStreamVolume();
            } else if (this.mc.getTwitchStream().isBroadcasting()) {
               this.field_152315_t = true;
            }
         }

         if (var1.id == 200) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(this.parentScreen);
         } else if (var1.id == 201) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(new GuiIngestServers(this));
         }
      }

   }

   public void initGui() {
      int var1 = 0;
      this.field_152319_i = I18n.format("options.stream.title");
      this.field_152313_r = I18n.format("options.stream.chat.title");
      GameSettings.Options[] var5;
      int var4 = (var5 = field_152312_a).length;

      GameSettings.Options var2;
      int var3;
      for(var3 = 0; var3 < var4; ++var3) {
         var2 = var5[var3];
         if (var2.getEnumFloat()) {
            this.buttonList.add(new GuiOptionSlider(var2.returnEnumOrdinal(), width / 2 - 155 + var1 % 2 * 160, height / 6 + 24 * (var1 >> 1), var2));
         } else {
            this.buttonList.add(new GuiOptionButton(var2.returnEnumOrdinal(), width / 2 - 155 + var1 % 2 * 160, height / 6 + 24 * (var1 >> 1), var2, this.field_152318_h.getKeyBinding(var2)));
         }

         ++var1;
      }

      if (var1 % 2 == 1) {
         ++var1;
      }

      this.field_152314_s = height / 6 + 24 * (var1 >> 1) + 6;
      var1 += 2;
      var4 = (var5 = field_152316_f).length;

      for(var3 = 0; var3 < var4; ++var3) {
         var2 = var5[var3];
         if (var2.getEnumFloat()) {
            this.buttonList.add(new GuiOptionSlider(var2.returnEnumOrdinal(), width / 2 - 155 + var1 % 2 * 160, height / 6 + 24 * (var1 >> 1), var2));
         } else {
            this.buttonList.add(new GuiOptionButton(var2.returnEnumOrdinal(), width / 2 - 155 + var1 % 2 * 160, height / 6 + 24 * (var1 >> 1), var2, this.field_152318_h.getKeyBinding(var2)));
         }

         ++var1;
      }

      this.buttonList.add(new GuiButton(200, width / 2 - 155, height / 6 + 168, 150, 20, I18n.format("gui.done")));
      GuiButton var6 = new GuiButton(201, width / 2 + 5, height / 6 + 168, 150, 20, I18n.format("options.stream.ingestSelection"));
      var6.enabled = this.mc.getTwitchStream().isReadyToBroadcast() && this.mc.getTwitchStream().func_152925_v().length > 0 || this.mc.getTwitchStream().func_152908_z();
      this.buttonList.add(var6);
   }

   public GuiStreamOptions(GuiScreen var1, GameSettings var2) {
      this.parentScreen = var1;
      this.field_152318_h = var2;
   }

   static {
      field_152312_a = new GameSettings.Options[]{GameSettings.Options.STREAM_BYTES_PER_PIXEL, GameSettings.Options.STREAM_FPS, GameSettings.Options.STREAM_KBPS, GameSettings.Options.STREAM_SEND_METADATA, GameSettings.Options.STREAM_VOLUME_MIC, GameSettings.Options.STREAM_VOLUME_SYSTEM, GameSettings.Options.STREAM_MIC_TOGGLE_BEHAVIOR, GameSettings.Options.STREAM_COMPRESSION};
      field_152316_f = new GameSettings.Options[]{GameSettings.Options.STREAM_CHAT_ENABLED, GameSettings.Options.STREAM_CHAT_USER_FILTER};
   }

   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRendererObj, this.field_152319_i, width / 2, 20, 16777215);
      this.drawCenteredString(this.fontRendererObj, this.field_152313_r, width / 2, this.field_152314_s, 16777215);
      if (this.field_152315_t) {
         this.drawCenteredString(this.fontRendererObj, EnumChatFormatting.RED + I18n.format("options.stream.changes"), width / 2, 20 + this.fontRendererObj.FONT_HEIGHT, 16777215);
      }

      super.drawScreen(var1, var2, var3);
   }
}
