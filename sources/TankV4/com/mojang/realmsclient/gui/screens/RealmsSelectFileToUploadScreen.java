package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsAnvilLevelStorageSource;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsLevelSummary;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.realms.RealmsScrolledSelectionList;
import net.minecraft.realms.Tezzelator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class RealmsSelectFileToUploadScreen extends RealmsScreen {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final int CANCEL_BUTTON = 1;
   private static final int UPLOAD_BUTTON = 2;
   private final RealmsScreen lastScreen;
   private final long worldId;
   private RealmsButton uploadButton;
   private final DateFormat DATE_FORMAT = new SimpleDateFormat();
   private List levelList = new ArrayList();
   private int selectedWorld = -1;
   private RealmsSelectFileToUploadScreen.WorldSelectionList worldSelectionList;
   private String worldLang;
   private String conversionLang;
   private String[] gameModesLang = new String[4];
   private String errorMessage = null;

   public RealmsSelectFileToUploadScreen(long var1, RealmsScreen var3) {
      this.lastScreen = var3;
      this.worldId = var1;
   }

   private void loadLevelList() throws Exception {
      RealmsAnvilLevelStorageSource var1 = this.getLevelStorageSource();
      this.levelList = var1.getLevelList();
      Collections.sort(this.levelList);
   }

   public void init() {
      Keyboard.enableRepeatEvents(true);
      this.buttonsClear();

      try {
         this.loadLevelList();
      } catch (Exception var3) {
         LOGGER.error((String)"Couldn't load level list", (Throwable)var3);
         Realms.setScreen(new RealmsErrorScreen("Unable to load worlds", var3.getMessage()));
         return;
      }

      this.worldLang = getLocalizedString("selectWorld.world");
      this.conversionLang = getLocalizedString("selectWorld.conversion");
      this.gameModesLang[Realms.survivalId()] = getLocalizedString("gameMode.survival");
      this.gameModesLang[Realms.creativeId()] = getLocalizedString("gameMode.creative");
      this.gameModesLang[Realms.adventureId()] = getLocalizedString("gameMode.adventure");
      this.gameModesLang[3] = getLocalizedString("gameMode.spectator");
      int var1 = (this.width() / 2 - 170) / 2;
      int var2 = var1 + this.width() / 2;
      this.buttonsAdd(this.uploadButton = newButton(2, var1, this.height() - 42, 170, 20, getLocalizedString("mco.upload.button.name")));
      this.buttonsAdd(newButton(1, var2, this.height() - 42, 170, 20, getLocalizedString("gui.back")));
      this.uploadButton.active(this.selectedWorld >= 0 && this.selectedWorld < this.levelList.size());
      this.worldSelectionList = new RealmsSelectFileToUploadScreen.WorldSelectionList(this);
   }

   public void removed() {
      Keyboard.enableRepeatEvents(false);
   }

   public void buttonClicked(RealmsButton var1) {
      if (var1.active()) {
         if (var1.id() == 1) {
            Realms.setScreen(this.lastScreen);
         } else if (var1.id() == 2) {
            this.upload();
         }

      }
   }

   private void upload() {
      if (this.selectedWorld != -1 && !((RealmsLevelSummary)this.levelList.get(this.selectedWorld)).isHardcore()) {
         RealmsLevelSummary var1 = (RealmsLevelSummary)this.levelList.get(this.selectedWorld);
         Realms.setScreen(new RealmsUploadScreen(this.worldId, this.lastScreen, var1));
      }

   }

   public void render(int var1, int var2, float var3) {
      this.renderBackground();
      this.worldSelectionList.render(var1, var2, var3);
      this.drawCenteredString(getLocalizedString("mco.upload.select.world.title"), this.width() / 2, 9, 16777215);
      if (this.errorMessage != null) {
         this.drawCenteredString(this.errorMessage, this.width() / 2, this.uploadButton.y() - 13, 16711680);
      }

      super.render(var1, var2, var3);
   }

   public void keyPressed(char var1, int var2) {
   }

   public void mouseEvent() {
      super.mouseEvent();
      this.worldSelectionList.mouseEvent();
   }

   public void tick() {
      super.tick();
   }

   static List access$000(RealmsSelectFileToUploadScreen var0) {
      return var0.levelList;
   }

   static int access$102(RealmsSelectFileToUploadScreen var0, int var1) {
      return var0.selectedWorld = var1;
   }

   static int access$100(RealmsSelectFileToUploadScreen var0) {
      return var0.selectedWorld;
   }

   static RealmsButton access$200(RealmsSelectFileToUploadScreen var0) {
      return var0.uploadButton;
   }

   static String access$302(RealmsSelectFileToUploadScreen var0, String var1) {
      return var0.errorMessage = var1;
   }

   static String access$400(RealmsSelectFileToUploadScreen var0) {
      return var0.worldLang;
   }

   static DateFormat access$500(RealmsSelectFileToUploadScreen var0) {
      return var0.DATE_FORMAT;
   }

   static String access$600(RealmsSelectFileToUploadScreen var0) {
      return var0.conversionLang;
   }

   static String[] access$700(RealmsSelectFileToUploadScreen var0) {
      return var0.gameModesLang;
   }

   private class WorldSelectionList extends RealmsScrolledSelectionList {
      final RealmsSelectFileToUploadScreen this$0;

      public WorldSelectionList(RealmsSelectFileToUploadScreen var1) {
         super(var1.width(), var1.height(), 32, var1.height() - 64, 36);
         this.this$0 = var1;
      }

      public int getItemCount() {
         return RealmsSelectFileToUploadScreen.access$000(this.this$0).size();
      }

      public void selectItem(int var1, boolean var2, int var3, int var4) {
         RealmsSelectFileToUploadScreen.access$102(this.this$0, var1);
         RealmsSelectFileToUploadScreen.access$200(this.this$0).active(RealmsSelectFileToUploadScreen.access$100(this.this$0) >= 0 && RealmsSelectFileToUploadScreen.access$100(this.this$0) < this.getItemCount() && !((RealmsLevelSummary)RealmsSelectFileToUploadScreen.access$000(this.this$0).get(RealmsSelectFileToUploadScreen.access$100(this.this$0))).isHardcore());
         RealmsSelectFileToUploadScreen.access$302(this.this$0, (String)null);
      }

      public boolean isSelectedItem(int var1) {
         return var1 == RealmsSelectFileToUploadScreen.access$100(this.this$0);
      }

      public int getMaxPosition() {
         return RealmsSelectFileToUploadScreen.access$000(this.this$0).size() * 36;
      }

      public void renderBackground() {
         this.this$0.renderBackground();
      }

      protected void renderItem(int var1, int var2, int var3, int var4, Tezzelator var5, int var6, int var7) {
         RealmsLevelSummary var8 = (RealmsLevelSummary)RealmsSelectFileToUploadScreen.access$000(this.this$0).get(var1);
         String var9 = var8.getLevelName();
         if (var9 == null || var9.isEmpty()) {
            var9 = RealmsSelectFileToUploadScreen.access$400(this.this$0) + " " + (var1 + 1);
         }

         String var10 = var8.getLevelId();
         var10 = var10 + " (" + RealmsSelectFileToUploadScreen.access$500(this.this$0).format(new Date(var8.getLastPlayed()));
         var10 = var10 + ")";
         String var11 = "";
         if (var8.isRequiresConversion()) {
            var11 = RealmsSelectFileToUploadScreen.access$600(this.this$0) + " " + var11;
         } else {
            var11 = RealmsSelectFileToUploadScreen.access$700(this.this$0)[var8.getGameMode()];
            if (var8.isHardcore()) {
               var11 = ChatFormatting.DARK_RED + RealmsScreen.getLocalizedString("mco.upload.hardcore") + ChatFormatting.RESET;
            }

            if (var8.hasCheats()) {
               var11 = var11 + ", " + RealmsScreen.getLocalizedString("selectWorld.cheats");
            }
         }

         this.this$0.drawString(var9, var2 + 2, var3 + 1, 16777215);
         this.this$0.drawString(var10, var2 + 2, var3 + 12, 8421504);
         this.this$0.drawString(var11, var2 + 2, var3 + 12 + 10, 8421504);
      }
   }
}
