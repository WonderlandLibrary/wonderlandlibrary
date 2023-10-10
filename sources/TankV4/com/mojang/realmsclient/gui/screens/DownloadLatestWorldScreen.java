package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.client.FileDownload;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.realms.RealmsSharedConstants;
import net.minecraft.realms.Tezzelator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class DownloadLatestWorldScreen extends RealmsScreen {
   private static final Logger LOGGER = LogManager.getLogger();
   private final RealmsScreen lastScreen;
   private final String downloadLink;
   private RealmsButton cancelButton;
   private final String worldName;
   private final DownloadLatestWorldScreen.DownloadStatus downloadStatus;
   private volatile String errorMessage = null;
   private volatile String status = null;
   private volatile String progress = null;
   private volatile boolean cancelled = false;
   private volatile boolean showDots = true;
   private volatile boolean finished = false;
   private volatile boolean extracting = false;
   private Long previousWrittenBytes = null;
   private Long previousTimeSnapshot = null;
   private long bytesPersSecond = 0L;
   private int animTick = 0;
   private static final String[] DOTS = new String[]{"", ".", ". .", ". . ."};
   private int dotIndex = 0;
   private static final ReentrantLock downloadLock = new ReentrantLock();

   public DownloadLatestWorldScreen(RealmsScreen var1, String var2, String var3) {
      this.lastScreen = var1;
      this.worldName = var3;
      this.downloadLink = var2;
      this.downloadStatus = new DownloadLatestWorldScreen.DownloadStatus(this);
   }

   public void init() {
      Keyboard.enableRepeatEvents(true);
      this.buttonsClear();
      this.buttonsAdd(this.cancelButton = newButton(0, this.width() / 2 - 100, this.height() - 42, 200, 20, getLocalizedString("gui.cancel")));
      this.downloadSave();
   }

   public void tick() {
      super.tick();
      ++this.animTick;
   }

   public void buttonClicked(RealmsButton var1) {
      if (var1.active()) {
         if (var1.id() == 0) {
            this.cancelled = true;
            this.backButtonClicked();
         }

      }
   }

   public void keyPressed(char var1, int var2) {
      if (var2 == 1) {
         this.cancelled = true;
         this.backButtonClicked();
      }

   }

   private void backButtonClicked() {
      Realms.setScreen(this.lastScreen);
   }

   public void render(int var1, int var2, float var3) {
      this.renderBackground();
      if (this.extracting && !this.finished) {
         this.status = getLocalizedString("mco.download.extracting");
      }

      this.drawCenteredString(getLocalizedString("mco.download.title"), this.width() / 2, 20, 16777215);
      this.drawCenteredString(this.status, this.width() / 2, 50, 16777215);
      if (this.showDots) {
         this.drawDots();
      }

      if (this.downloadStatus.bytesWritten != 0L && !this.cancelled) {
         this.drawProgressBar();
         this.drawDownloadSpeed();
      }

      if (this.errorMessage != null) {
         this.drawCenteredString(this.errorMessage, this.width() / 2, 110, 16711680);
      }

      super.render(var1, var2, var3);
   }

   private void drawDots() {
      int var1 = this.fontWidth(this.status);
      if (this.animTick % 10 == 0) {
         ++this.dotIndex;
      }

      this.drawString(DOTS[this.dotIndex % DOTS.length], this.width() / 2 + var1 / 2 + 5, 50, 16777215);
   }

   private void drawProgressBar() {
      double var1 = this.downloadStatus.bytesWritten.doubleValue() / this.downloadStatus.totalBytes.doubleValue() * 100.0D;
      this.progress = String.format("%.1f", var1);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(3553);
      Tezzelator var3 = Tezzelator.instance;
      var3.begin();
      double var4 = (double)(this.width() / 2 - 100);
      double var6 = 0.5D;
      var3.color(14275282);
      var3.vertex(var4 - var6, 95.0D + var6, 0.0D);
      var3.vertex(var4 + 200.0D * var1 / 100.0D + var6, 95.0D + var6, 0.0D);
      var3.vertex(var4 + 200.0D * var1 / 100.0D + var6, 80.0D - var6, 0.0D);
      var3.vertex(var4 - var6, 80.0D - var6, 0.0D);
      var3.color(8421504);
      var3.vertex(var4, 95.0D, 0.0D);
      var3.vertex(var4 + 200.0D * var1 / 100.0D, 95.0D, 0.0D);
      var3.vertex(var4 + 200.0D * var1 / 100.0D, 80.0D, 0.0D);
      var3.vertex(var4, 80.0D, 0.0D);
      var3.end();
      GL11.glEnable(3553);
      this.drawCenteredString(this.progress + " %", this.width() / 2, 84, 16777215);
   }

   private void drawDownloadSpeed() {
      if (this.animTick % RealmsSharedConstants.TICKS_PER_SECOND == 0) {
         if (this.previousWrittenBytes != null) {
            long var1 = System.currentTimeMillis() - this.previousTimeSnapshot;
            if (var1 == 0L) {
               var1 = 1L;
            }

            this.bytesPersSecond = 1000L * (this.downloadStatus.bytesWritten - this.previousWrittenBytes) / var1;
            this.drawDownloadSpeed0(this.bytesPersSecond);
         }

         this.previousWrittenBytes = this.downloadStatus.bytesWritten;
         this.previousTimeSnapshot = System.currentTimeMillis();
      } else {
         this.drawDownloadSpeed0(this.bytesPersSecond);
      }

   }

   private void drawDownloadSpeed0(long var1) {
      if (var1 > 0L) {
         int var3 = this.fontWidth(this.progress);
         String var4 = "(" + humanReadableByteCount(var1) + ")";
         this.drawString(var4, this.width() / 2 + var3 / 2 + 15, 84, 16777215);
      }

   }

   public static String humanReadableByteCount(long var0) {
      short var2 = 1024;
      if (var0 < (long)var2) {
         return var0 + " B";
      } else {
         int var3 = (int)(Math.log((double)var0) / Math.log((double)var2));
         String var4 = "KMGTPE".charAt(var3 - 1) + "";
         return String.format("%.1f %sB/s", (double)var0 / Math.pow((double)var2, (double)var3), var4);
      }
   }

   public void mouseEvent() {
      super.mouseEvent();
   }

   private void downloadSave() {
      (new Thread(this) {
         final DownloadLatestWorldScreen this$0;

         {
            this.this$0 = var1;
         }

         public void run() {
            label104: {
               label105: {
                  label106: {
                     label107: {
                        try {
                           if (!DownloadLatestWorldScreen.access$000().tryLock(1L, TimeUnit.SECONDS)) {
                              break label105;
                           }

                           DownloadLatestWorldScreen.access$102(this.this$0, RealmsScreen.getLocalizedString("mco.download.preparing"));
                           if (DownloadLatestWorldScreen.access$200(this.this$0)) {
                              DownloadLatestWorldScreen.access$300(this.this$0);
                              break label106;
                           }

                           DownloadLatestWorldScreen.access$102(this.this$0, RealmsScreen.getLocalizedString("mco.download.downloading", DownloadLatestWorldScreen.access$400(this.this$0)));
                           FileDownload var1 = new FileDownload();
                           var1.download(DownloadLatestWorldScreen.access$500(this.this$0), DownloadLatestWorldScreen.access$400(this.this$0), DownloadLatestWorldScreen.access$600(this.this$0), this.this$0.getLevelStorageSource());

                           while(true) {
                              if (var1.isFinished()) {
                                 DownloadLatestWorldScreen.access$1102(this.this$0, true);
                                 DownloadLatestWorldScreen.access$102(this.this$0, RealmsScreen.getLocalizedString("mco.download.done"));
                                 DownloadLatestWorldScreen.access$800(this.this$0).msg(RealmsScreen.getLocalizedString("gui.done"));
                                 break label104;
                              }

                              if (var1.isError()) {
                                 var1.cancel();
                                 DownloadLatestWorldScreen.access$702(this.this$0, RealmsScreen.getLocalizedString("mco.download.failed"));
                                 DownloadLatestWorldScreen.access$800(this.this$0).msg(RealmsScreen.getLocalizedString("gui.done"));
                                 break label107;
                              }

                              if (var1.isExtracting()) {
                                 DownloadLatestWorldScreen.access$902(this.this$0, true);
                              }

                              if (DownloadLatestWorldScreen.access$200(this.this$0)) {
                                 var1.cancel();
                                 DownloadLatestWorldScreen.access$300(this.this$0);
                                 break;
                              }

                              try {
                                 Thread.sleep(500L);
                              } catch (InterruptedException var4) {
                                 DownloadLatestWorldScreen.access$1000().error("Failed to check Realms backup download status");
                              }
                           }
                        } catch (InterruptedException var5) {
                           DownloadLatestWorldScreen.access$1000().error("Could not acquire upload lock");
                           if (!DownloadLatestWorldScreen.access$000().isHeldByCurrentThread()) {
                              return;
                           }

                           DownloadLatestWorldScreen.access$000().unlock();
                           DownloadLatestWorldScreen.access$1202(this.this$0, false);
                           this.this$0.buttonsRemove(DownloadLatestWorldScreen.access$800(this.this$0));
                           return;
                        } catch (Exception var6) {
                           DownloadLatestWorldScreen.access$702(this.this$0, RealmsScreen.getLocalizedString("mco.download.failed"));
                           var6.printStackTrace();
                           if (!DownloadLatestWorldScreen.access$000().isHeldByCurrentThread()) {
                              return;
                           }

                           DownloadLatestWorldScreen.access$000().unlock();
                           DownloadLatestWorldScreen.access$1202(this.this$0, false);
                           this.this$0.buttonsRemove(DownloadLatestWorldScreen.access$800(this.this$0));
                           return;
                        }

                        if (!DownloadLatestWorldScreen.access$000().isHeldByCurrentThread()) {
                           return;
                        }

                        DownloadLatestWorldScreen.access$000().unlock();
                        DownloadLatestWorldScreen.access$1202(this.this$0, false);
                        this.this$0.buttonsRemove(DownloadLatestWorldScreen.access$800(this.this$0));
                        return;
                     }

                     if (!DownloadLatestWorldScreen.access$000().isHeldByCurrentThread()) {
                        return;
                     }

                     DownloadLatestWorldScreen.access$000().unlock();
                     DownloadLatestWorldScreen.access$1202(this.this$0, false);
                     this.this$0.buttonsRemove(DownloadLatestWorldScreen.access$800(this.this$0));
                     return;
                  }

                  if (!DownloadLatestWorldScreen.access$000().isHeldByCurrentThread()) {
                     return;
                  }

                  DownloadLatestWorldScreen.access$000().unlock();
                  DownloadLatestWorldScreen.access$1202(this.this$0, false);
                  this.this$0.buttonsRemove(DownloadLatestWorldScreen.access$800(this.this$0));
                  return;
               }

               if (!DownloadLatestWorldScreen.access$000().isHeldByCurrentThread()) {
                  return;
               }

               DownloadLatestWorldScreen.access$000().unlock();
               DownloadLatestWorldScreen.access$1202(this.this$0, false);
               this.this$0.buttonsRemove(DownloadLatestWorldScreen.access$800(this.this$0));
               return;
            }

            if (DownloadLatestWorldScreen.access$000().isHeldByCurrentThread()) {
               DownloadLatestWorldScreen.access$000().unlock();
               DownloadLatestWorldScreen.access$1202(this.this$0, false);
               this.this$0.buttonsRemove(DownloadLatestWorldScreen.access$800(this.this$0));
            }
         }
      }).start();
   }

   private void downloadCancelled() {
      this.status = getLocalizedString("mco.download.cancelled");
   }

   static ReentrantLock access$000() {
      return downloadLock;
   }

   static String access$102(DownloadLatestWorldScreen var0, String var1) {
      return var0.status = var1;
   }

   static boolean access$200(DownloadLatestWorldScreen var0) {
      return var0.cancelled;
   }

   static void access$300(DownloadLatestWorldScreen var0) {
      var0.downloadCancelled();
   }

   static String access$400(DownloadLatestWorldScreen var0) {
      return var0.worldName;
   }

   static String access$500(DownloadLatestWorldScreen var0) {
      return var0.downloadLink;
   }

   static DownloadLatestWorldScreen.DownloadStatus access$600(DownloadLatestWorldScreen var0) {
      return var0.downloadStatus;
   }

   static String access$702(DownloadLatestWorldScreen var0, String var1) {
      return var0.errorMessage = var1;
   }

   static RealmsButton access$800(DownloadLatestWorldScreen var0) {
      return var0.cancelButton;
   }

   static boolean access$902(DownloadLatestWorldScreen var0, boolean var1) {
      return var0.extracting = var1;
   }

   static Logger access$1000() {
      return LOGGER;
   }

   static boolean access$1102(DownloadLatestWorldScreen var0, boolean var1) {
      return var0.finished = var1;
   }

   static boolean access$1202(DownloadLatestWorldScreen var0, boolean var1) {
      return var0.showDots = var1;
   }

   public class DownloadStatus {
      public volatile Long bytesWritten;
      public volatile Long totalBytes;
      final DownloadLatestWorldScreen this$0;

      public DownloadStatus(DownloadLatestWorldScreen var1) {
         this.this$0 = var1;
         this.bytesWritten = 0L;
         this.totalBytes = 0L;
      }
   }
}
