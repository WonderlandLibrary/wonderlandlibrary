package net.minecraft.client.gui;

import java.io.IOException;
import java.util.Random;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

public class GuiCreateWorld extends GuiScreen {
   private boolean field_146337_w;
   private boolean field_146338_v;
   private String gameMode = "survival";
   private GuiButton btnCustomizeType;
   private GuiButton btnMoreOptions;
   private GuiButton btnAllowCommands;
   private boolean field_146339_u;
   private static final String[] disallowedFilenames = new String[]{"CON", "COM", "PRN", "AUX", "CLOCK$", "NUL", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9"};
   private String field_175300_s;
   private String field_146330_J;
   private GuiButton btnBonusItems;
   private String field_146336_i;
   public String chunkProviderSettingsJson = "";
   private boolean field_146341_s = true;
   private GuiTextField field_146333_g;
   private GuiTextField field_146335_h;
   private String field_146329_I;
   private GuiButton btnMapFeatures;
   private int selectedIndex;
   private boolean field_146344_y;
   private GuiScreen parentScreen;
   private String field_146328_H;
   private GuiButton btnMapType;
   private boolean field_146345_x;
   private GuiButton btnGameMode;
   private boolean allowCheats;
   private String field_146323_G;

   private void func_146315_i() {
      this.func_146316_a(!this.field_146344_y);
   }

   protected void actionPerformed(GuiButton var1) throws IOException {
      if (var1.enabled) {
         if (var1.id == 1) {
            this.mc.displayGuiScreen(this.parentScreen);
         } else if (var1.id == 0) {
            this.mc.displayGuiScreen((GuiScreen)null);
            if (this.field_146345_x) {
               return;
            }

            this.field_146345_x = true;
            long var2 = (new Random()).nextLong();
            String var4 = this.field_146335_h.getText();
            if (!StringUtils.isEmpty(var4)) {
               try {
                  long var5 = Long.parseLong(var4);
                  if (var5 != 0L) {
                     var2 = var5;
                  }
               } catch (NumberFormatException var7) {
                  var2 = (long)var4.hashCode();
               }
            }

            WorldSettings.GameType var8 = WorldSettings.GameType.getByName(this.gameMode);
            WorldSettings var6 = new WorldSettings(var2, var8, this.field_146341_s, this.field_146337_w, WorldType.worldTypes[this.selectedIndex]);
            var6.setWorldName(this.chunkProviderSettingsJson);
            if (this.field_146338_v && !this.field_146337_w) {
               var6.enableBonusChest();
            }

            if (this.allowCheats && !this.field_146337_w) {
               var6.enableCommands();
            }

            this.mc.launchIntegratedServer(this.field_146336_i, this.field_146333_g.getText().trim(), var6);
         } else if (var1.id == 3) {
            this.func_146315_i();
         } else if (var1.id == 2) {
            if (this.gameMode.equals("survival")) {
               if (!this.field_146339_u) {
                  this.allowCheats = false;
               }

               this.field_146337_w = false;
               this.gameMode = "hardcore";
               this.field_146337_w = true;
               this.btnAllowCommands.enabled = false;
               this.btnBonusItems.enabled = false;
               this.func_146319_h();
            } else if (this.gameMode.equals("hardcore")) {
               if (!this.field_146339_u) {
                  this.allowCheats = true;
               }

               this.field_146337_w = false;
               this.gameMode = "creative";
               this.func_146319_h();
               this.field_146337_w = false;
               this.btnAllowCommands.enabled = true;
               this.btnBonusItems.enabled = true;
            } else {
               if (!this.field_146339_u) {
                  this.allowCheats = false;
               }

               this.gameMode = "survival";
               this.func_146319_h();
               this.btnAllowCommands.enabled = true;
               this.btnBonusItems.enabled = true;
               this.field_146337_w = false;
            }

            this.func_146319_h();
         } else if (var1.id == 4) {
            this.field_146341_s = !this.field_146341_s;
            this.func_146319_h();
         } else if (var1.id == 7) {
            this.field_146338_v = !this.field_146338_v;
            this.func_146319_h();
         } else if (var1.id == 5) {
            ++this.selectedIndex;
            if (this.selectedIndex >= WorldType.worldTypes.length) {
               this.selectedIndex = 0;
            }

            while(this == false) {
               ++this.selectedIndex;
               if (this.selectedIndex >= WorldType.worldTypes.length) {
                  this.selectedIndex = 0;
               }
            }

            this.chunkProviderSettingsJson = "";
            this.func_146319_h();
            this.func_146316_a(this.field_146344_y);
         } else if (var1.id == 6) {
            this.field_146339_u = true;
            this.allowCheats = !this.allowCheats;
            this.func_146319_h();
         } else if (var1.id == 8) {
            if (WorldType.worldTypes[this.selectedIndex] == WorldType.FLAT) {
               this.mc.displayGuiScreen(new GuiCreateFlatWorld(this, this.chunkProviderSettingsJson));
            } else {
               this.mc.displayGuiScreen(new GuiCustomizeWorldScreen(this, this.chunkProviderSettingsJson));
            }
         }
      }

   }

   protected void mouseClicked(int var1, int var2, int var3) throws IOException {
      super.mouseClicked(var1, var2, var3);
      if (this.field_146344_y) {
         this.field_146335_h.mouseClicked(var1, var2, var3);
      } else {
         this.field_146333_g.mouseClicked(var1, var2, var3);
      }

   }

   public void func_146318_a(WorldInfo var1) {
      this.field_146330_J = I18n.format("selectWorld.newWorld.copyOf", var1.getWorldName());
      this.field_146329_I = String.valueOf(var1.getSeed());
      this.selectedIndex = var1.getTerrainType().getWorldTypeID();
      this.chunkProviderSettingsJson = var1.getGeneratorOptions();
      this.field_146341_s = var1.isMapFeaturesEnabled();
      this.allowCheats = var1.areCommandsAllowed();
      if (var1.isHardcoreModeEnabled()) {
         this.gameMode = "hardcore";
      } else if (var1.getGameType().isSurvivalOrAdventure()) {
         this.gameMode = "survival";
      } else if (var1.getGameType().isCreative()) {
         this.gameMode = "creative";
      }

   }

   private void func_146316_a(boolean var1) {
      this.field_146344_y = var1;
      if (WorldType.worldTypes[this.selectedIndex] == WorldType.DEBUG_WORLD) {
         this.btnGameMode.visible = !this.field_146344_y;
         this.btnGameMode.enabled = false;
         if (this.field_175300_s == null) {
            this.field_175300_s = this.gameMode;
         }

         this.gameMode = "spectator";
         this.btnMapFeatures.visible = false;
         this.btnBonusItems.visible = false;
         this.btnMapType.visible = this.field_146344_y;
         this.btnAllowCommands.visible = false;
         this.btnCustomizeType.visible = false;
      } else {
         this.btnGameMode.visible = !this.field_146344_y;
         this.btnGameMode.enabled = true;
         if (this.field_175300_s != null) {
            this.gameMode = this.field_175300_s;
            this.field_175300_s = null;
         }

         this.btnMapFeatures.visible = this.field_146344_y && WorldType.worldTypes[this.selectedIndex] != WorldType.CUSTOMIZED;
         this.btnBonusItems.visible = this.field_146344_y;
         this.btnMapType.visible = this.field_146344_y;
         this.btnAllowCommands.visible = this.field_146344_y;
         this.btnCustomizeType.visible = this.field_146344_y && (WorldType.worldTypes[this.selectedIndex] == WorldType.FLAT || WorldType.worldTypes[this.selectedIndex] == WorldType.CUSTOMIZED);
      }

      this.func_146319_h();
      if (this.field_146344_y) {
         this.btnMoreOptions.displayString = I18n.format("gui.done");
      } else {
         this.btnMoreOptions.displayString = I18n.format("selectWorld.moreWorldOptions");
      }

   }

   public GuiCreateWorld(GuiScreen var1) {
      this.parentScreen = var1;
      this.field_146329_I = "";
      this.field_146330_J = I18n.format("selectWorld.newWorld");
   }

   private void func_146314_g() {
      this.field_146336_i = this.field_146333_g.getText().trim();
      char[] var4;
      int var3 = (var4 = ChatAllowedCharacters.allowedCharactersArray).length;

      for(int var2 = 0; var2 < var3; ++var2) {
         char var1 = var4[var2];
         this.field_146336_i = this.field_146336_i.replace(var1, '_');
      }

      if (StringUtils.isEmpty(this.field_146336_i)) {
         this.field_146336_i = "World";
      }

      this.field_146336_i = func_146317_a(this.mc.getSaveLoader(), this.field_146336_i);
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   public static String func_146317_a(ISaveFormat var0, String var1) {
      var1 = var1.replaceAll("[\\./\"]", "_");
      String[] var5;
      int var4 = (var5 = disallowedFilenames).length;

      for(int var3 = 0; var3 < var4; ++var3) {
         String var2 = var5[var3];
         if (var1.equalsIgnoreCase(var2)) {
            var1 = "_" + var1 + "_";
         }
      }

      while(var0.getWorldInfo(var1) != null) {
         var1 = var1 + "-";
      }

      return var1;
   }

   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRendererObj, I18n.format("selectWorld.create"), width / 2, 20, -1);
      if (this.field_146344_y) {
         this.drawString(this.fontRendererObj, I18n.format("selectWorld.enterSeed"), width / 2 - 100, 47, -6250336);
         this.drawString(this.fontRendererObj, I18n.format("selectWorld.seedInfo"), width / 2 - 100, 85, -6250336);
         if (this.btnMapFeatures.visible) {
            this.drawString(this.fontRendererObj, I18n.format("selectWorld.mapFeatures.info"), width / 2 - 150, 122, -6250336);
         }

         if (this.btnAllowCommands.visible) {
            this.drawString(this.fontRendererObj, I18n.format("selectWorld.allowCommands.info"), width / 2 - 150, 172, -6250336);
         }

         this.field_146335_h.drawTextBox();
         if (WorldType.worldTypes[this.selectedIndex].showWorldInfoNotice()) {
            this.fontRendererObj.drawSplitString(I18n.format(WorldType.worldTypes[this.selectedIndex].func_151359_c()), this.btnMapType.xPosition + 2, this.btnMapType.yPosition + 22, this.btnMapType.getButtonWidth(), 10526880);
         }
      } else {
         this.drawString(this.fontRendererObj, I18n.format("selectWorld.enterName"), width / 2 - 100, 47, -6250336);
         this.drawString(this.fontRendererObj, I18n.format("selectWorld.resultFolder") + " " + this.field_146336_i, width / 2 - 100, 85, -6250336);
         this.field_146333_g.drawTextBox();
         this.drawString(this.fontRendererObj, this.field_146323_G, width / 2 - 100, 137, -6250336);
         this.drawString(this.fontRendererObj, this.field_146328_H, width / 2 - 100, 149, -6250336);
      }

      super.drawScreen(var1, var2, var3);
   }

   public void initGui() {
      Keyboard.enableRepeatEvents(true);
      this.buttonList.clear();
      this.buttonList.add(new GuiButton(0, width / 2 - 155, height - 28, 150, 20, I18n.format("selectWorld.create")));
      this.buttonList.add(new GuiButton(1, width / 2 + 5, height - 28, 150, 20, I18n.format("gui.cancel")));
      this.buttonList.add(this.btnGameMode = new GuiButton(2, width / 2 - 75, 115, 150, 20, I18n.format("selectWorld.gameMode")));
      this.buttonList.add(this.btnMoreOptions = new GuiButton(3, width / 2 - 75, 187, 150, 20, I18n.format("selectWorld.moreWorldOptions")));
      this.buttonList.add(this.btnMapFeatures = new GuiButton(4, width / 2 - 155, 100, 150, 20, I18n.format("selectWorld.mapFeatures")));
      this.btnMapFeatures.visible = false;
      this.buttonList.add(this.btnBonusItems = new GuiButton(7, width / 2 + 5, 151, 150, 20, I18n.format("selectWorld.bonusItems")));
      this.btnBonusItems.visible = false;
      this.buttonList.add(this.btnMapType = new GuiButton(5, width / 2 + 5, 100, 150, 20, I18n.format("selectWorld.mapType")));
      this.btnMapType.visible = false;
      this.buttonList.add(this.btnAllowCommands = new GuiButton(6, width / 2 - 155, 151, 150, 20, I18n.format("selectWorld.allowCommands")));
      this.btnAllowCommands.visible = false;
      this.buttonList.add(this.btnCustomizeType = new GuiButton(8, width / 2 + 5, 120, 150, 20, I18n.format("selectWorld.customizeType")));
      this.btnCustomizeType.visible = false;
      this.field_146333_g = new GuiTextField(9, this.fontRendererObj, width / 2 - 100, 60, 200, 20);
      this.field_146333_g.setFocused(true);
      this.field_146333_g.setText(this.field_146330_J);
      this.field_146335_h = new GuiTextField(10, this.fontRendererObj, width / 2 - 100, 60, 200, 20);
      this.field_146335_h.setText(this.field_146329_I);
      this.func_146316_a(this.field_146344_y);
      this.func_146314_g();
      this.func_146319_h();
   }

   private void func_146319_h() {
      this.btnGameMode.displayString = I18n.format("selectWorld.gameMode") + ": " + I18n.format("selectWorld.gameMode." + this.gameMode);
      this.field_146323_G = I18n.format("selectWorld.gameMode." + this.gameMode + ".line1");
      this.field_146328_H = I18n.format("selectWorld.gameMode." + this.gameMode + ".line2");
      this.btnMapFeatures.displayString = I18n.format("selectWorld.mapFeatures") + " ";
      if (this.field_146341_s) {
         this.btnMapFeatures.displayString = this.btnMapFeatures.displayString + I18n.format("options.on");
      } else {
         this.btnMapFeatures.displayString = this.btnMapFeatures.displayString + I18n.format("options.off");
      }

      this.btnBonusItems.displayString = I18n.format("selectWorld.bonusItems") + " ";
      if (this.field_146338_v && !this.field_146337_w) {
         this.btnBonusItems.displayString = this.btnBonusItems.displayString + I18n.format("options.on");
      } else {
         this.btnBonusItems.displayString = this.btnBonusItems.displayString + I18n.format("options.off");
      }

      this.btnMapType.displayString = I18n.format("selectWorld.mapType") + " " + I18n.format(WorldType.worldTypes[this.selectedIndex].getTranslateName());
      this.btnAllowCommands.displayString = I18n.format("selectWorld.allowCommands") + " ";
      if (this.allowCheats && !this.field_146337_w) {
         this.btnAllowCommands.displayString = this.btnAllowCommands.displayString + I18n.format("options.on");
      } else {
         this.btnAllowCommands.displayString = this.btnAllowCommands.displayString + I18n.format("options.off");
      }

   }

   protected void keyTyped(char var1, int var2) throws IOException {
      if (this.field_146333_g.isFocused() && !this.field_146344_y) {
         this.field_146333_g.textboxKeyTyped(var1, var2);
         this.field_146330_J = this.field_146333_g.getText();
      } else if (this.field_146335_h.isFocused() && this.field_146344_y) {
         this.field_146335_h.textboxKeyTyped(var1, var2);
         this.field_146329_I = this.field_146335_h.getText();
      }

      if (var2 == 28 || var2 == 156) {
         this.actionPerformed((GuiButton)this.buttonList.get(0));
      }

      ((GuiButton)this.buttonList.get(0)).enabled = this.field_146333_g.getText().length() > 0;
      this.func_146314_g();
   }

   public void updateScreen() {
      this.field_146333_g.updateCursorCounter();
      this.field_146335_h.updateCursorCounter();
   }
}
