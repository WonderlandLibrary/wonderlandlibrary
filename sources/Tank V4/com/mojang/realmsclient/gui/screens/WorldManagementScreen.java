package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.Backup;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.exception.RetryCallException;
import com.mojang.realmsclient.gui.LongRunningTask;
import java.net.URI;
import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsClickableScrolledSelectionList;
import net.minecraft.realms.RealmsScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class WorldManagementScreen extends RealmsScreen {
   private boolean showUpload = true;
   private static final Logger LOGGER = LogManager.getLogger();
   private static final String PLUS_ICON_LOCATION = "realms:textures/gui/realms/plus_icon.png";
   private static final String RESTORE_ICON_LOCATION = "realms:textures/gui/realms/restore_icon.png";
   private static int lastScrollPosition = -1;
   private final RealmsConfigureWorldScreen lastScreen;
   private final RealmsScreen onlineScreen;
   private List backups = Collections.emptyList();
   private String toolTip = null;
   private WorldManagementScreen.BackupSelectionList backupSelectionList;
   private int selectedBackup = -1;
   private static final int BACK_BUTTON_ID = 0;
   private static final int RESTORE_BUTTON_ID = 1;
   private static final int DOWNLOAD_BUTTON_ID = 2;
   private static final int RESET_BUTTON_ID = 3;
   private static final int UPLOAD_BUTTON_ID = 4;
   private static final int MINUTES = 60;
   private static final int HOURS = 3600;
   private static final int DAYS = 86400;
   private RealmsButton downloadButton;
   private RealmsButton uploadButton;
   private RealmsButton resetButton;
   private Boolean noBackups = false;
   private RealmsServer serverData;
   private static final String UPLOADED_KEY = "Uploaded";

   public WorldManagementScreen(RealmsConfigureWorldScreen var1, RealmsScreen var2, RealmsServer var3) {
      this.lastScreen = var1;
      this.onlineScreen = var2;
      this.serverData = var3;
   }

   public void mouseEvent() {
      super.mouseEvent();
      this.backupSelectionList.mouseEvent();
   }

   public void init() {
      Keyboard.enableRepeatEvents(true);
      this.buttonsClear();
      this.backupSelectionList = new WorldManagementScreen.BackupSelectionList(this);
      if (lastScrollPosition != -1) {
         this.backupSelectionList.scroll(lastScrollPosition);
      }

      (new Thread(this, "Realms-fetch-backups") {
         final WorldManagementScreen this$0;

         {
            this.this$0 = var1;
         }

         public void run() {
            RealmsClient var1 = RealmsClient.createRealmsClient();

            try {
               WorldManagementScreen.access$002(this.this$0, var1.backupsFor(WorldManagementScreen.access$100(this.this$0).id).backups);
               WorldManagementScreen.access$202(this.this$0, WorldManagementScreen.access$000(this.this$0).size() == 0);
               WorldManagementScreen.access$300(this.this$0);
            } catch (RealmsServiceException var3) {
               WorldManagementScreen.access$400().error((String)"Couldn't request backups", (Throwable)var3);
            }

         }
      }).start();
      this.postInit();
   }

   private void generateChangeList() {
      if (this.backups.size() > 1) {
         label42:
         for(int var1 = 0; var1 < this.backups.size() - 1; ++var1) {
            Backup var2 = (Backup)this.backups.get(var1);
            Backup var3 = (Backup)this.backups.get(var1 + 1);
            if (!var2.metadata.isEmpty() && !var3.metadata.isEmpty()) {
               Iterator var4 = var2.metadata.keySet().iterator();

               while(true) {
                  while(true) {
                     if (!var4.hasNext()) {
                        continue label42;
                     }

                     String var5 = (String)var4.next();
                     if (!var5.contains("Uploaded") && var3.metadata.containsKey(var5)) {
                        if (!((String)var2.metadata.get(var5)).equals(var3.metadata.get(var5))) {
                           this.addToChangeList(var2, var5);
                        }
                     } else {
                        this.addToChangeList(var2, var5);
                     }
                  }
               }
            }
         }

      }
   }

   private void addToChangeList(Backup var1, String var2) {
      if (var2.contains("Uploaded")) {
         String var3 = DateFormat.getDateTimeInstance(3, 3).format(var1.lastModifiedDate);
         var1.changeList.put(var2, var3);
         var1.setUploadedVersion(true);
      } else {
         var1.changeList.put(var2, var1.metadata.get(var2));
      }

   }

   private void postInit() {
      this.buttonsAdd(this.resetButton = newButton(3, this.width() - 125, 35, 100, 20, getLocalizedString("mco.backup.button.reset")));
      this.buttonsAdd(this.downloadButton = newButton(2, this.width() - 125, this.showUpload ? 85 : 60, 100, 20, getLocalizedString("mco.backup.button.download")));
      this.buttonsAdd(newButton(0, this.width() - 125, this.height() - 35, 85, 20, getLocalizedString("gui.back")));
      if (this.showUpload) {
         this.buttonsAdd(this.uploadButton = newButton(4, this.width() - 125, 60, 100, 20, getLocalizedString("mco.backup.button.upload")));
      }

   }

   public void tick() {
      super.tick();
   }

   public void buttonClicked(RealmsButton var1) {
      if (var1.active()) {
         if (var1.id() == 0) {
            Realms.setScreen(this.lastScreen);
         } else if (var1.id() == 2) {
            this.downloadClicked();
         } else if (var1.id() == 3) {
            Realms.setScreen(new ResetWorldScreen(this.lastScreen, this.onlineScreen, this, this.serverData));
         } else if (var1.id() == 4 && this.showUpload) {
            Realms.setScreen(new RealmsSelectFileToUploadScreen(this.serverData.id, this));
         }

      }
   }

   public void keyPressed(char var1, int var2) {
      if (var2 == 1) {
         Realms.setScreen(this.lastScreen);
      }

   }

   private void restoreClicked(int var1) {
      if (var1 >= 0 && var1 < this.backups.size()) {
         this.selectedBackup = var1;
         Date var2 = ((Backup)this.backups.get(var1)).lastModifiedDate;
         String var3 = DateFormat.getDateTimeInstance(3, 3).format(var2);
         String var4 = this.convertToAgePresentation(System.currentTimeMillis() - var2.getTime());
         String var5 = getLocalizedString("mco.configure.world.restore.question.line1", new Object[]{var3, var4});
         String var6 = getLocalizedString("mco.configure.world.restore.question.line2");
         Realms.setScreen(new LongConfirmationScreen(this, LongConfirmationScreen.Type.Warning, var5, var6, 1));
      }

   }

   private void downloadClicked() {
      String var1 = getLocalizedString("mco.configure.world.restore.download.question.line1");
      String var2 = getLocalizedString("mco.configure.world.restore.download.question.line2");
      Realms.setScreen(new LongConfirmationScreen(this, LongConfirmationScreen.Type.Info, var1, var2, 2));
   }

   private void downloadWorldData() {
      RealmsClient var1 = RealmsClient.createRealmsClient();

      try {
         String var2 = var1.download(this.serverData.id);
         Realms.setScreen(new DownloadLatestWorldScreen(this, var2, this.serverData.name));
      } catch (RealmsServiceException var3) {
         LOGGER.error("Couldn't download world data");
         Realms.setScreen(new RealmsGenericErrorScreen(var3, this));
      }

   }

   private void browseTo(String var1) {
      try {
         URI var2 = new URI(var1);
         Class var3 = Class.forName("java.awt.Desktop");
         Object var4 = var3.getMethod("getDesktop").invoke((Object)null);
         var3.getMethod("browse", URI.class).invoke(var4, var2);
      } catch (Throwable var5) {
         LOGGER.error("Couldn't open link");
      }

   }

   public void confirmResult(boolean var1, int var2) {
      if (var1 && var2 == 1) {
         this.restore();
      } else if (var1 && var2 == 2) {
         this.downloadWorldData();
      } else {
         Realms.setScreen(this);
      }

   }

   private void restore() {
      Backup var1 = (Backup)this.backups.get(this.selectedBackup);
      WorldManagementScreen.RestoreTask var2 = new WorldManagementScreen.RestoreTask(this, var1);
      LongRunningMcoTaskScreen var3 = new LongRunningMcoTaskScreen(this.lastScreen, var2);
      var3.start();
      Realms.setScreen(var3);
   }

   public void render(int var1, int var2, float var3) {
      this.toolTip = null;
      this.renderBackground();
      this.backupSelectionList.render(var1, var2, var3);
      this.drawCenteredString(getLocalizedString("mco.backup.title"), this.width() / 2, 10, 16777215);
      this.drawString(getLocalizedString("mco.backup.backup"), (this.width() - 150) / 2 - 90, 20, 10526880);
      if (this.noBackups) {
         this.drawString(getLocalizedString("mco.backup.nobackups"), 20, this.height() / 2 - 10, 16777215);
      }

      this.downloadButton.active(!this.noBackups);
      if (this.showUpload) {
         this.uploadButton.active(!this.serverData.expired);
      }

      this.resetButton.active(!this.serverData.expired);
      super.render(var1, var2, var3);
      if (this.toolTip != null) {
         this.renderMousehoverTooltip(this.toolTip, var1, var2);
      }

   }

   private String convertToAgePresentation(Long var1) {
      if (var1 < 0L) {
         return "right now";
      } else {
         long var2 = var1 / 1000L;
         if (var2 < 60L) {
            return (var2 == 1L ? "1 second" : var2 + " seconds") + " ago";
         } else {
            long var4;
            if (var2 < 3600L) {
               var4 = var2 / 60L;
               return (var4 == 1L ? "1 minute" : var4 + " minutes") + " ago";
            } else if (var2 < 86400L) {
               var4 = var2 / 3600L;
               return (var4 == 1L ? "1 hour" : var4 + " hours") + " ago";
            } else {
               var4 = var2 / 86400L;
               return (var4 == 1L ? "1 day" : var4 + " days") + " ago";
            }
         }
      }
   }

   protected void renderMousehoverTooltip(String var1, int var2, int var3) {
      if (var1 != null) {
         int var4 = var2 + 12;
         int var5 = var3 - 12;
         int var6 = this.fontWidth(var1);
         this.fillGradient(var4 - 3, var5 - 3, var4 + var6 + 3, var5 + 8 + 3, -1073741824, -1073741824);
         this.fontDrawShadow(var1, var4, var5, -1);
      }
   }

   static List access$002(WorldManagementScreen var0, List var1) {
      return var0.backups = var1;
   }

   static RealmsServer access$100(WorldManagementScreen var0) {
      return var0.serverData;
   }

   static Boolean access$202(WorldManagementScreen var0, Boolean var1) {
      return var0.noBackups = var1;
   }

   static List access$000(WorldManagementScreen var0) {
      return var0.backups;
   }

   static void access$300(WorldManagementScreen var0) {
      var0.generateChangeList();
   }

   static Logger access$400() {
      return LOGGER;
   }

   static RealmsConfigureWorldScreen access$600(WorldManagementScreen var0) {
      return var0.lastScreen;
   }

   static int access$702(int var0) {
      lastScrollPosition = var0;
      return var0;
   }

   static void access$800(WorldManagementScreen var0, int var1) {
      var0.restoreClicked(var1);
   }

   static String access$900(WorldManagementScreen var0, Long var1) {
      return var0.convertToAgePresentation(var1);
   }

   static String access$1002(WorldManagementScreen var0, String var1) {
      return var0.toolTip = var1;
   }

   private class BackupSelectionList extends RealmsClickableScrolledSelectionList {
      final WorldManagementScreen this$0;

      public BackupSelectionList(WorldManagementScreen var1) {
         super(var1.width() - 150, var1.height(), 32, var1.height() - 15, 36);
         this.this$0 = var1;
      }

      public int getItemCount() {
         return WorldManagementScreen.access$000(this.this$0).size() + 1;
      }

      public int getMaxPosition() {
         return this.getItemCount() * 36;
      }

      public void renderBackground() {
         this.this$0.renderBackground();
      }

      public void customMouseEvent(int var1, int var2, int var3, float var4, int var5) {
         if (Mouse.isButtonDown(0) && this.ym() >= var1 && this.ym() <= var2) {
            int var6 = this.width() / 2 - 92;
            int var7 = this.width();
            int var8 = this.ym() - var1 - var3 + (int)var4 - 4;
            int var9 = var8 / var5;
            if (this.xm() >= var6 && this.xm() <= var7 && var9 >= 0 && var8 >= 0 && var9 < this.getItemCount()) {
               this.itemClicked(var8, var9, this.xm(), this.ym(), this.width());
            }
         }

      }

      public void renderItem(int var1, int var2, int var3, int var4, int var5, int var6) {
         var2 += 16;
         if (var1 < WorldManagementScreen.access$000(this.this$0).size()) {
            this.renderBackupItem(var1, var2, var3, var4, this.this$0.width);
         }

      }

      public int getScrollbarPosition() {
         return this.width() - 5;
      }

      public void itemClicked(int var1, int var2, int var3, int var4, int var5) {
         int var6 = this.width() - 40;
         int var7 = var1 + 30 - this.getScroll();
         int var8 = var6 + 10;
         int var9 = var7 - 3;
         if (var3 >= var6 && var3 <= var6 + 9 && var4 >= var7 && var4 <= var7 + 9) {
            if (!((Backup)WorldManagementScreen.access$000(this.this$0).get(var2)).changeList.isEmpty()) {
               WorldManagementScreen.access$702(this.getScroll());
               Realms.setScreen(new BackupInfoScreen(this.this$0, (Backup)WorldManagementScreen.access$000(this.this$0).get(var2)));
            }
         } else if (var3 >= var8 && var3 <= var8 + 9 && var4 >= var9 && var4 <= var9 + 9) {
            WorldManagementScreen.access$702(this.getScroll());
            WorldManagementScreen.access$800(this.this$0, var2);
         }

      }

      private void renderBackupItem(int var1, int var2, int var3, int var4, int var5) {
         Backup var6 = (Backup)WorldManagementScreen.access$000(this.this$0).get(var1);
         int var7 = var6.isUploadedVersion() ? -8388737 : 16777215;
         this.this$0.drawString("Backup (" + WorldManagementScreen.access$900(this.this$0, System.currentTimeMillis() - var6.lastModifiedDate.getTime()) + ")", var2 + 2, var3 + 1, var7);
         this.this$0.drawString(this.getMediumDatePresentation(var6.lastModifiedDate), var2 + 2, var3 + 12, 7105644);
         int var8 = this.width() - 30;
         byte var9 = -3;
         int var10 = var8 - 10;
         int var11 = var9 + 3;
         if (!WorldManagementScreen.access$100(this.this$0).expired) {
            this.drawRestore(var8, var3 + var9, this.xm(), this.ym());
         }

         if (!var6.changeList.isEmpty()) {
            this.drawInfo(var10, var3 + var11, this.xm(), this.ym());
         }

      }

      private String getMediumDatePresentation(Date var1) {
         return DateFormat.getDateTimeInstance(3, 3).format(var1);
      }

      private void drawRestore(int var1, int var2, int var3, int var4) {
         RealmsScreen.bind("realms:textures/gui/realms/restore_icon.png");
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glPushMatrix();
         GL11.glScalef(0.5F, 0.5F, 0.5F);
         RealmsScreen.blit(var1 * 2, var2 * 2, 0.0F, 0.0F, 23, 28, 23.0F, 28.0F);
         GL11.glPopMatrix();
         if (var3 >= var1 && var3 <= var1 + 9 && var4 >= var2 && var4 <= var2 + 9 && var4 < this.this$0.height() - 15 && var4 > 32) {
            WorldManagementScreen.access$1002(this.this$0, RealmsScreen.getLocalizedString("mco.backup.button.restore"));
         }

      }

      private void drawInfo(int var1, int var2, int var3, int var4) {
         RealmsScreen.bind("realms:textures/gui/realms/plus_icon.png");
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glPushMatrix();
         GL11.glScalef(0.5F, 0.5F, 0.5F);
         RealmsScreen.blit(var1 * 2, var2 * 2, 0.0F, 0.0F, 15, 15, 15.0F, 15.0F);
         GL11.glPopMatrix();
         if (var3 >= var1 && var3 <= var1 + 9 && var4 >= var2 && var4 <= var2 + 9 && var4 < this.this$0.height() - 15 && var4 > 32) {
            WorldManagementScreen.access$1002(this.this$0, RealmsScreen.getLocalizedString("mco.backup.changes.tooltip"));
         }

      }
   }

   private class RestoreTask extends LongRunningTask {
      private final Backup backup;
      final WorldManagementScreen this$0;

      private RestoreTask(WorldManagementScreen var1, Backup var2) {
         this.this$0 = var1;
         this.backup = var2;
      }

      public void run() {
         this.setTitle(RealmsScreen.getLocalizedString("mco.backup.restoring"));
         int var1 = 0;

         while(var1 < 6) {
            try {
               if (this.aborted()) {
                  return;
               }

               RealmsClient var2 = RealmsClient.createRealmsClient();
               var2.restoreWorld(WorldManagementScreen.access$100(this.this$0).id, this.backup.backupId);
               this.pause(1);
               if (this.aborted()) {
                  return;
               }

               Realms.setScreen(WorldManagementScreen.access$600(this.this$0));
               return;
            } catch (RetryCallException var3) {
               if (this.aborted()) {
                  return;
               }

               this.pause(var3.delaySeconds);
               ++var1;
            } catch (RealmsServiceException var4) {
               if (this.aborted()) {
                  return;
               }

               WorldManagementScreen.access$400().error("Couldn't restore backup");
               Realms.setScreen(new RealmsGenericErrorScreen(var4, WorldManagementScreen.access$600(this.this$0)));
               return;
            } catch (Exception var5) {
               if (this.aborted()) {
                  return;
               }

               WorldManagementScreen.access$400().error("Couldn't restore backup");
               this.error(var5.getLocalizedMessage());
               return;
            }
         }

      }

      private void pause(int var1) {
         try {
            Thread.sleep((long)(var1 * 1000));
         } catch (InterruptedException var3) {
            WorldManagementScreen.access$400().error((Object)var3);
         }

      }

      RestoreTask(WorldManagementScreen var1, Backup var2, Object var3) {
         this(var1, var2);
      }
   }
}
