package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundEventAccessorComposite;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.stream.GuiStreamOptions;
import net.minecraft.client.gui.stream.GuiStreamUnavailable;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.stream.IStream;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.EnumDifficulty;

public class GuiOptions extends GuiScreen implements GuiYesNoCallback {
   private GuiButton field_175357_i;
   protected String field_146442_a = "Options";
   private final GuiScreen field_146441_g;
   private GuiLockIconButton field_175356_r;
   private static final GameSettings.Options[] field_146440_f;
   private final GameSettings game_settings_1;

   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRendererObj, this.field_146442_a, width / 2, 15, 16777215);
      super.drawScreen(var1, var2, var3);
   }

   public GuiOptions(GuiScreen var1, GameSettings var2) {
      this.field_146441_g = var1;
      this.game_settings_1 = var2;
   }

   public String func_175355_a(EnumDifficulty var1) {
      ChatComponentText var2 = new ChatComponentText("");
      var2.appendSibling(new ChatComponentTranslation("options.difficulty", new Object[0]));
      var2.appendText(": ");
      var2.appendSibling(new ChatComponentTranslation(var1.getDifficultyResourceKey(), new Object[0]));
      return var2.getFormattedText();
   }

   public void initGui() {
      int var1 = 0;
      this.field_146442_a = I18n.format("options.title");
      GameSettings.Options[] var5;
      int var4 = (var5 = field_146440_f).length;

      for(int var3 = 0; var3 < var4; ++var3) {
         GameSettings.Options var2 = var5[var3];
         if (var2.getEnumFloat()) {
            this.buttonList.add(new GuiOptionSlider(var2.returnEnumOrdinal(), width / 2 - 155 + var1 % 2 * 160, height / 6 - 12 + 24 * (var1 >> 1), var2));
         } else {
            GuiOptionButton var6 = new GuiOptionButton(var2.returnEnumOrdinal(), width / 2 - 155 + var1 % 2 * 160, height / 6 - 12 + 24 * (var1 >> 1), var2, this.game_settings_1.getKeyBinding(var2));
            this.buttonList.add(var6);
         }

         ++var1;
      }

      if (Minecraft.theWorld != null) {
         EnumDifficulty var8 = Minecraft.theWorld.getDifficulty();
         this.field_175357_i = new GuiButton(108, width / 2 - 155 + var1 % 2 * 160, height / 6 - 12 + 24 * (var1 >> 1), 150, 20, this.func_175355_a(var8));
         this.buttonList.add(this.field_175357_i);
         if (this.mc.isSingleplayer() && !Minecraft.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
            this.field_175357_i.setWidth(this.field_175357_i.getButtonWidth() - 20);
            this.field_175356_r = new GuiLockIconButton(109, this.field_175357_i.xPosition + this.field_175357_i.getButtonWidth(), this.field_175357_i.yPosition);
            this.buttonList.add(this.field_175356_r);
            this.field_175356_r.func_175229_b(Minecraft.theWorld.getWorldInfo().isDifficultyLocked());
            this.field_175356_r.enabled = !this.field_175356_r.func_175230_c();
            this.field_175357_i.enabled = !this.field_175356_r.func_175230_c();
         } else {
            this.field_175357_i.enabled = false;
         }
      }

      this.buttonList.add(new GuiButton(110, width / 2 - 155, height / 6 + 48 - 6, 150, 20, I18n.format("options.skinCustomisation")));
      this.buttonList.add(new GuiButton(this, 8675309, width / 2 + 5, height / 6 + 48 - 6, 150, 20, "Super Secret Settings...") {
         final GuiOptions this$0;

         {
            this.this$0 = var1;
         }

         public void playPressSound(SoundHandler var1) {
            SoundEventAccessorComposite var2 = var1.getRandomSoundFromCategories(SoundCategory.ANIMALS, SoundCategory.BLOCKS, SoundCategory.MOBS, SoundCategory.PLAYERS, SoundCategory.WEATHER);
            if (var2 != null) {
               var1.playSound(PositionedSoundRecord.create(var2.getSoundEventLocation(), 0.5F));
            }

         }
      });
      this.buttonList.add(new GuiButton(106, width / 2 - 155, height / 6 + 72 - 6, 150, 20, I18n.format("options.sounds")));
      this.buttonList.add(new GuiButton(107, width / 2 + 5, height / 6 + 72 - 6, 150, 20, I18n.format("options.stream")));
      this.buttonList.add(new GuiButton(101, width / 2 - 155, height / 6 + 96 - 6, 150, 20, I18n.format("options.video")));
      this.buttonList.add(new GuiButton(100, width / 2 + 5, height / 6 + 96 - 6, 150, 20, I18n.format("options.controls")));
      this.buttonList.add(new GuiButton(102, width / 2 - 155, height / 6 + 120 - 6, 150, 20, I18n.format("options.language")));
      this.buttonList.add(new GuiButton(103, width / 2 + 5, height / 6 + 120 - 6, 150, 20, I18n.format("options.chat.title")));
      this.buttonList.add(new GuiButton(105, width / 2 - 155, height / 6 + 144 - 6, 150, 20, I18n.format("options.resourcepack")));
      this.buttonList.add(new GuiButton(104, width / 2 + 5, height / 6 + 144 - 6, 150, 20, I18n.format("options.snooper.view")));
      this.buttonList.add(new GuiButton(200, width / 2 - 100, height / 6 + 168, I18n.format("gui.done")));
   }

   static {
      field_146440_f = new GameSettings.Options[]{GameSettings.Options.FOV};
   }

   protected void actionPerformed(GuiButton var1) throws IOException {
      if (var1.enabled) {
         if (var1.id < 100 && var1 instanceof GuiOptionButton) {
            GameSettings.Options var2 = ((GuiOptionButton)var1).returnEnumOptions();
            this.game_settings_1.setOptionValue(var2, 1);
            var1.displayString = this.game_settings_1.getKeyBinding(GameSettings.Options.getEnumOptions(var1.id));
         }

         if (var1.id == 108) {
            Minecraft.theWorld.getWorldInfo().setDifficulty(EnumDifficulty.getDifficultyEnum(Minecraft.theWorld.getDifficulty().getDifficultyId() + 1));
            this.field_175357_i.displayString = this.func_175355_a(Minecraft.theWorld.getDifficulty());
         }

         if (var1.id == 109) {
            this.mc.displayGuiScreen(new GuiYesNo(this, (new ChatComponentTranslation("difficulty.lock.title", new Object[0])).getFormattedText(), (new ChatComponentTranslation("difficulty.lock.question", new Object[]{new ChatComponentTranslation(Minecraft.theWorld.getWorldInfo().getDifficulty().getDifficultyResourceKey(), new Object[0])})).getFormattedText(), 109));
         }

         if (var1.id == 110) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(new GuiCustomizeSkin(this));
         }

         if (var1.id == 8675309) {
            this.mc.entityRenderer.activateNextShader();
         }

         if (var1.id == 101) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(new GuiVideoSettings(this, this.game_settings_1));
         }

         if (var1.id == 100) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(new GuiControls(this, this.game_settings_1));
         }

         if (var1.id == 102) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(new GuiLanguage(this, this.game_settings_1, this.mc.getLanguageManager()));
         }

         if (var1.id == 103) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(new ScreenChatOptions(this, this.game_settings_1));
         }

         if (var1.id == 104) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(new GuiSnooper(this, this.game_settings_1));
         }

         if (var1.id == 200) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(this.field_146441_g);
         }

         if (var1.id == 105) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(new GuiScreenResourcePacks(this));
         }

         if (var1.id == 106) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(new GuiScreenOptionsSounds(this, this.game_settings_1));
         }

         if (var1.id == 107) {
            this.mc.gameSettings.saveOptions();
            IStream var3 = this.mc.getTwitchStream();
            if (var3.func_152936_l() && var3.func_152928_D()) {
               this.mc.displayGuiScreen(new GuiStreamOptions(this, this.game_settings_1));
            } else {
               GuiStreamUnavailable.func_152321_a(this);
            }
         }
      }

   }

   public void confirmClicked(boolean var1, int var2) {
      this.mc.displayGuiScreen(this);
      if (var2 == 109 && var1 && Minecraft.theWorld != null) {
         Minecraft.theWorld.getWorldInfo().setDifficultyLocked(true);
         this.field_175356_r.func_175229_b(true);
         this.field_175356_r.enabled = false;
         this.field_175357_i.enabled = false;
      }

   }
}
