package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.dto.Backup;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.realms.RealmsSimpleScrolledSelectionList;
import net.minecraft.realms.Tezzelator;
import org.lwjgl.input.Keyboard;

public class BackupInfoScreen extends RealmsScreen {
   private final RealmsScreen lastScreen;
   private final int BUTTON_BACK_ID = 0;
   private final Backup backup;
   private List keys = new ArrayList();
   private BackupInfoScreen.BackupInfoList backupInfoList;
   String[] difficulties = new String[]{getLocalizedString("options.difficulty.peaceful"), getLocalizedString("options.difficulty.easy"), getLocalizedString("options.difficulty.normal"), getLocalizedString("options.difficulty.hard")};
   String[] gameModes = new String[]{getLocalizedString("selectWorld.gameMode.survival"), getLocalizedString("selectWorld.gameMode.creative"), getLocalizedString("selectWorld.gameMode.adventure")};

   public BackupInfoScreen(RealmsScreen var1, Backup var2) {
      this.lastScreen = var1;
      this.backup = var2;
      if (var2.changeList != null) {
         Iterator var3 = var2.changeList.entrySet().iterator();

         while(var3.hasNext()) {
            Entry var4 = (Entry)var3.next();
            this.keys.add(var4.getKey());
         }
      }

   }

   public void mouseEvent() {
      super.mouseEvent();
      this.backupInfoList.mouseEvent();
   }

   public void tick() {
   }

   public void init() {
      Keyboard.enableRepeatEvents(true);
      this.buttonsAdd(newButton(0, this.width() / 2 - 100, this.height() / 4 + 120 + 24, getLocalizedString("gui.back")));
      this.backupInfoList = new BackupInfoScreen.BackupInfoList(this);
   }

   public void removed() {
      Keyboard.enableRepeatEvents(false);
   }

   public void buttonClicked(RealmsButton var1) {
      if (var1.active()) {
         if (var1.id() == 0) {
            Realms.setScreen(this.lastScreen);
         }

      }
   }

   public void keyPressed(char var1, int var2) {
      if (var2 == 1) {
         Realms.setScreen(this.lastScreen);
      }

   }

   public void render(int var1, int var2, float var3) {
      this.renderBackground();
      this.drawCenteredString("Changes from last backup", this.width() / 2, 10, 16777215);
      this.backupInfoList.render(var1, var2, var3);
      super.render(var1, var2, var3);
   }

   private String checkForSpecificMetadata(String var1, String var2) {
      String var3 = var1.toLowerCase();
      if (var3.contains("game") && var3.contains("mode")) {
         return this.gameModeMetadata(var2);
      } else {
         return var3.contains("game") && var3.contains("difficulty") ? this.gameDifficultyMetadata(var2) : var2;
      }
   }

   private String gameDifficultyMetadata(String var1) {
      try {
         return this.difficulties[Integer.parseInt(var1)];
      } catch (Exception var3) {
         return "UNKNOWN";
      }
   }

   private String gameModeMetadata(String var1) {
      try {
         return this.gameModes[Integer.parseInt(var1)];
      } catch (Exception var3) {
         return "UNKNOWN";
      }
   }

   static Backup access$000(BackupInfoScreen var0) {
      return var0.backup;
   }

   static List access$100(BackupInfoScreen var0) {
      return var0.keys;
   }

   static String access$200(BackupInfoScreen var0, String var1, String var2) {
      return var0.checkForSpecificMetadata(var1, var2);
   }

   private class BackupInfoList extends RealmsSimpleScrolledSelectionList {
      final BackupInfoScreen this$0;

      public BackupInfoList(BackupInfoScreen var1) {
         super(var1.width(), var1.height(), 32, var1.height() - 64, 36);
         this.this$0 = var1;
      }

      public int getItemCount() {
         return BackupInfoScreen.access$000(this.this$0).changeList.size();
      }

      public void selectItem(int var1, boolean var2, int var3, int var4) {
      }

      public boolean isSelectedItem(int var1) {
         return false;
      }

      public int getMaxPosition() {
         return this.getItemCount() * 30;
      }

      public void renderBackground() {
      }

      protected void renderItem(int var1, int var2, int var3, int var4, Tezzelator var5, int var6, int var7) {
         String var8 = (String)BackupInfoScreen.access$100(this.this$0).get(var1);
         this.this$0.drawString(var8, this.width() / 2 - 40, var3, 16777215);
         String var9 = (String)BackupInfoScreen.access$000(this.this$0).changeList.get(var8);
         this.this$0.drawString(BackupInfoScreen.access$200(this.this$0, var8, var9), this.width() / 2 - 40, var3 + 12, 10526880);
      }
   }
}
