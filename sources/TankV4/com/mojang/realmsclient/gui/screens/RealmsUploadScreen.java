package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.client.FileUpload;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.client.UploadStatus;
import com.mojang.realmsclient.dto.UploadInfo;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.util.UploadTokenCache;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.zip.GZIPOutputStream;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsLevelSummary;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.realms.RealmsSharedConstants;
import net.minecraft.realms.Tezzelator;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class RealmsUploadScreen extends RealmsScreen {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final int CANCEL_BUTTON = 0;
   private static final int BACK_BUTTON = 1;
   private final RealmsScreen lastScreen;
   private final RealmsLevelSummary selectedLevel;
   private final long worldId;
   private final UploadStatus uploadStatus;
   private volatile String errorMessage = null;
   private volatile String status = null;
   private volatile String progress = null;
   private volatile boolean cancelled = false;
   private volatile boolean uploadFinished = false;
   private volatile boolean showDots = true;
   private RealmsButton backButton;
   private RealmsButton cancelButton;
   private int animTick = 0;
   private static final String[] DOTS = new String[]{"", ".", ". .", ". . ."};
   private int dotIndex = 0;
   private Long previousWrittenBytes = null;
   private Long previousTimeSnapshot = null;
   private long bytesPersSecond = 0L;
   private static final ReentrantLock uploadLock = new ReentrantLock();

   public RealmsUploadScreen(long var1, RealmsScreen var3, RealmsLevelSummary var4) {
      this.worldId = var1;
      this.lastScreen = var3;
      this.selectedLevel = var4;
      this.uploadStatus = new UploadStatus();
   }

   public void init() {
      Keyboard.enableRepeatEvents(true);
      this.buttonsClear();
      this.backButton = newButton(1, this.width() / 2 - 100, this.height() - 42, 200, 20, getLocalizedString("gui.back"));
      this.buttonsAdd(this.cancelButton = newButton(0, this.width() / 2 - 100, this.height() - 42, 200, 20, getLocalizedString("gui.cancel")));
      this.upload();
   }

   public void removed() {
      Keyboard.enableRepeatEvents(false);
   }

   public void buttonClicked(RealmsButton var1) {
      if (var1.active()) {
         if (var1.id() == 1) {
            Realms.setScreen(this.lastScreen);
         } else if (var1.id() == 0) {
            this.cancelled = true;
         }

      }
   }

   public void render(int var1, int var2, float var3) {
      this.renderBackground();
      if (!this.uploadFinished && this.uploadStatus.bytesWritten != 0L && this.uploadStatus.bytesWritten == this.uploadStatus.totalBytes) {
         this.status = getLocalizedString("mco.upload.verifying");
      }

      this.drawCenteredString(this.status, this.width() / 2, 50, 16777215);
      if (this.showDots) {
         this.drawDots();
      }

      if (this.uploadStatus.bytesWritten != 0L && !this.cancelled) {
         this.drawProgressBar();
         this.drawUploadSpeed();
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
      double var1 = this.uploadStatus.bytesWritten.doubleValue() / this.uploadStatus.totalBytes.doubleValue() * 100.0D;
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

   private void drawUploadSpeed() {
      if (this.animTick % RealmsSharedConstants.TICKS_PER_SECOND == 0) {
         if (this.previousWrittenBytes != null) {
            long var1 = System.currentTimeMillis() - this.previousTimeSnapshot;
            if (var1 == 0L) {
               var1 = 1L;
            }

            this.bytesPersSecond = 1000L * (this.uploadStatus.bytesWritten - this.previousWrittenBytes) / var1;
            this.drawUploadSpeed0(this.bytesPersSecond);
         }

         this.previousWrittenBytes = this.uploadStatus.bytesWritten;
         this.previousTimeSnapshot = System.currentTimeMillis();
      } else {
         this.drawUploadSpeed0(this.bytesPersSecond);
      }

   }

   private void drawUploadSpeed0(long var1) {
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

   public void tick() {
      super.tick();
      ++this.animTick;
   }

   private void upload() {
      (new Thread(this) {
         final RealmsUploadScreen this$0;

         {
            this.this$0 = var1;
         }

         public void run() {
            File var1 = null;
            RealmsClient var2 = RealmsClient.createRealmsClient();
            long var3 = RealmsUploadScreen.access$000(this.this$0);

            label242: {
               label266: {
                  label244: {
                     label245: {
                        label246: {
                           label247: {
                              try {
                                 if (!RealmsUploadScreen.access$100().tryLock(1L, TimeUnit.SECONDS)) {
                                    break label247;
                                 }

                                 RealmsUploadScreen.access$202(this.this$0, RealmsScreen.getLocalizedString("mco.upload.preparing"));
                                 UploadInfo var5 = var2.upload(var3, UploadTokenCache.get(var3));
                                 UploadTokenCache.put(var3, var5.getToken());
                                 if (var5.isWorldClosed()) {
                                    if (RealmsUploadScreen.access$300(this.this$0)) {
                                       RealmsUploadScreen.access$400(this.this$0, var3);
                                       break label242;
                                    }

                                    File var6 = new File(Realms.getGameDirectoryPath(), "saves");
                                    var1 = RealmsUploadScreen.access$600(this.this$0, new File(var6, RealmsUploadScreen.access$500(this.this$0).getLevelId()));
                                    if (RealmsUploadScreen.access$300(this.this$0)) {
                                       RealmsUploadScreen.access$400(this.this$0, var3);
                                       break label266;
                                    }

                                    if (!RealmsUploadScreen.access$700(this.this$0, var1)) {
                                       RealmsUploadScreen.access$802(this.this$0, RealmsScreen.getLocalizedString("mco.upload.size.failure", RealmsUploadScreen.access$500(this.this$0).getLevelName()));
                                       break label245;
                                    }

                                    RealmsUploadScreen.access$202(this.this$0, RealmsScreen.getLocalizedString("mco.upload.uploading", RealmsUploadScreen.access$500(this.this$0).getLevelName()));
                                    FileUpload var7 = new FileUpload();
                                    var7.upload(var1, RealmsUploadScreen.access$000(this.this$0), var5, Realms.getSessionId(), Realms.getName(), RealmsSharedConstants.VERSION_STRING, RealmsUploadScreen.access$900(this.this$0));

                                    while(!var7.isFinished()) {
                                       if (RealmsUploadScreen.access$300(this.this$0)) {
                                          var7.cancel();
                                          RealmsUploadScreen.access$400(this.this$0, var3);
                                          break label246;
                                       }

                                       try {
                                          Thread.sleep(500L);
                                       } catch (InterruptedException var21) {
                                          RealmsUploadScreen.access$1000().error("Failed to check Realms file upload status");
                                       }
                                    }

                                    if (var7.getStatusCode() >= 200 && var7.getStatusCode() < 300) {
                                       RealmsUploadScreen.access$1102(this.this$0, true);
                                       RealmsUploadScreen.access$202(this.this$0, RealmsScreen.getLocalizedString("mco.upload.done"));
                                       RealmsUploadScreen.access$1200(this.this$0).msg(RealmsScreen.getLocalizedString("gui.done"));
                                       UploadTokenCache.invalidate(var3);
                                       break label244;
                                    }

                                    RealmsUploadScreen.access$802(this.this$0, RealmsScreen.getLocalizedString("mco.upload.failed", var7.getStatusCode()));
                                    break label244;
                                 }

                                 RealmsUploadScreen.access$202(this.this$0, RealmsScreen.getLocalizedString("mco.upload.close.failure"));
                              } catch (IOException var22) {
                                 RealmsUploadScreen.access$802(this.this$0, RealmsScreen.getLocalizedString("mco.upload.failed", var22.getMessage()));
                                 if (!RealmsUploadScreen.access$100().isHeldByCurrentThread()) {
                                    return;
                                 }

                                 RealmsUploadScreen.access$100().unlock();
                                 RealmsUploadScreen.access$1302(this.this$0, false);
                                 this.this$0.buttonsRemove(RealmsUploadScreen.access$1400(this.this$0));
                                 this.this$0.buttonsAdd(RealmsUploadScreen.access$1200(this.this$0));
                                 if (var1 != null) {
                                    RealmsUploadScreen.access$1000().debug("Deleting file " + var1.getAbsolutePath());
                                    var1.delete();
                                 }

                                 try {
                                    var2.uploadFinished(var3);
                                 } catch (RealmsServiceException var19) {
                                    RealmsUploadScreen.access$1000().error("Failed to request upload-finished to Realms", var19.toString());
                                 }

                                 return;
                              } catch (RealmsServiceException var23) {
                                 RealmsUploadScreen.access$802(this.this$0, RealmsScreen.getLocalizedString("mco.upload.failed", var23.toString()));
                                 if (!RealmsUploadScreen.access$100().isHeldByCurrentThread()) {
                                    return;
                                 }

                                 RealmsUploadScreen.access$100().unlock();
                                 RealmsUploadScreen.access$1302(this.this$0, false);
                                 this.this$0.buttonsRemove(RealmsUploadScreen.access$1400(this.this$0));
                                 this.this$0.buttonsAdd(RealmsUploadScreen.access$1200(this.this$0));
                                 if (var1 != null) {
                                    RealmsUploadScreen.access$1000().debug("Deleting file " + var1.getAbsolutePath());
                                    var1.delete();
                                 }

                                 try {
                                    var2.uploadFinished(var3);
                                 } catch (RealmsServiceException var18) {
                                    RealmsUploadScreen.access$1000().error("Failed to request upload-finished to Realms", var18.toString());
                                 }

                                 return;
                              } catch (InterruptedException var24) {
                                 RealmsUploadScreen.access$1000().error("Could not acquire upload lock");
                                 if (!RealmsUploadScreen.access$100().isHeldByCurrentThread()) {
                                    return;
                                 }

                                 RealmsUploadScreen.access$100().unlock();
                                 RealmsUploadScreen.access$1302(this.this$0, false);
                                 this.this$0.buttonsRemove(RealmsUploadScreen.access$1400(this.this$0));
                                 this.this$0.buttonsAdd(RealmsUploadScreen.access$1200(this.this$0));
                                 if (var1 != null) {
                                    RealmsUploadScreen.access$1000().debug("Deleting file " + var1.getAbsolutePath());
                                    var1.delete();
                                 }

                                 try {
                                    var2.uploadFinished(var3);
                                 } catch (RealmsServiceException var17) {
                                    RealmsUploadScreen.access$1000().error("Failed to request upload-finished to Realms", var17.toString());
                                 }

                                 return;
                              }

                              if (!RealmsUploadScreen.access$100().isHeldByCurrentThread()) {
                                 return;
                              }

                              RealmsUploadScreen.access$100().unlock();
                              RealmsUploadScreen.access$1302(this.this$0, false);
                              this.this$0.buttonsRemove(RealmsUploadScreen.access$1400(this.this$0));
                              this.this$0.buttonsAdd(RealmsUploadScreen.access$1200(this.this$0));
                              if (var1 != null) {
                                 RealmsUploadScreen.access$1000().debug("Deleting file " + var1.getAbsolutePath());
                                 var1.delete();
                              }

                              try {
                                 var2.uploadFinished(var3);
                              } catch (RealmsServiceException var12) {
                                 RealmsUploadScreen.access$1000().error("Failed to request upload-finished to Realms", var12.toString());
                              }

                              return;
                           }

                           if (!RealmsUploadScreen.access$100().isHeldByCurrentThread()) {
                              return;
                           }

                           RealmsUploadScreen.access$100().unlock();
                           RealmsUploadScreen.access$1302(this.this$0, false);
                           this.this$0.buttonsRemove(RealmsUploadScreen.access$1400(this.this$0));
                           this.this$0.buttonsAdd(RealmsUploadScreen.access$1200(this.this$0));
                           if (var1 != null) {
                              RealmsUploadScreen.access$1000().debug("Deleting file " + var1.getAbsolutePath());
                              var1.delete();
                           }

                           try {
                              var2.uploadFinished(var3);
                           } catch (RealmsServiceException var11) {
                              RealmsUploadScreen.access$1000().error("Failed to request upload-finished to Realms", var11.toString());
                           }

                           return;
                        }

                        if (!RealmsUploadScreen.access$100().isHeldByCurrentThread()) {
                           return;
                        }

                        RealmsUploadScreen.access$100().unlock();
                        RealmsUploadScreen.access$1302(this.this$0, false);
                        this.this$0.buttonsRemove(RealmsUploadScreen.access$1400(this.this$0));
                        this.this$0.buttonsAdd(RealmsUploadScreen.access$1200(this.this$0));
                        if (var1 != null) {
                           RealmsUploadScreen.access$1000().debug("Deleting file " + var1.getAbsolutePath());
                           var1.delete();
                        }

                        try {
                           var2.uploadFinished(var3);
                        } catch (RealmsServiceException var16) {
                           RealmsUploadScreen.access$1000().error("Failed to request upload-finished to Realms", var16.toString());
                        }

                        return;
                     }

                     if (!RealmsUploadScreen.access$100().isHeldByCurrentThread()) {
                        return;
                     }

                     RealmsUploadScreen.access$100().unlock();
                     RealmsUploadScreen.access$1302(this.this$0, false);
                     this.this$0.buttonsRemove(RealmsUploadScreen.access$1400(this.this$0));
                     this.this$0.buttonsAdd(RealmsUploadScreen.access$1200(this.this$0));
                     if (var1 != null) {
                        RealmsUploadScreen.access$1000().debug("Deleting file " + var1.getAbsolutePath());
                        var1.delete();
                     }

                     try {
                        var2.uploadFinished(var3);
                     } catch (RealmsServiceException var15) {
                        RealmsUploadScreen.access$1000().error("Failed to request upload-finished to Realms", var15.toString());
                     }

                     return;
                  }

                  if (!RealmsUploadScreen.access$100().isHeldByCurrentThread()) {
                     return;
                  }

                  RealmsUploadScreen.access$100().unlock();
                  RealmsUploadScreen.access$1302(this.this$0, false);
                  this.this$0.buttonsRemove(RealmsUploadScreen.access$1400(this.this$0));
                  this.this$0.buttonsAdd(RealmsUploadScreen.access$1200(this.this$0));
                  if (var1 != null) {
                     RealmsUploadScreen.access$1000().debug("Deleting file " + var1.getAbsolutePath());
                     var1.delete();
                  }

                  try {
                     var2.uploadFinished(var3);
                  } catch (RealmsServiceException var20) {
                     RealmsUploadScreen.access$1000().error("Failed to request upload-finished to Realms", var20.toString());
                  }

                  return;
               }

               if (!RealmsUploadScreen.access$100().isHeldByCurrentThread()) {
                  return;
               }

               RealmsUploadScreen.access$100().unlock();
               RealmsUploadScreen.access$1302(this.this$0, false);
               this.this$0.buttonsRemove(RealmsUploadScreen.access$1400(this.this$0));
               this.this$0.buttonsAdd(RealmsUploadScreen.access$1200(this.this$0));
               if (var1 != null) {
                  RealmsUploadScreen.access$1000().debug("Deleting file " + var1.getAbsolutePath());
                  var1.delete();
               }

               try {
                  var2.uploadFinished(var3);
               } catch (RealmsServiceException var14) {
                  RealmsUploadScreen.access$1000().error("Failed to request upload-finished to Realms", var14.toString());
               }

               return;
            }

            if (RealmsUploadScreen.access$100().isHeldByCurrentThread()) {
               RealmsUploadScreen.access$100().unlock();
               RealmsUploadScreen.access$1302(this.this$0, false);
               this.this$0.buttonsRemove(RealmsUploadScreen.access$1400(this.this$0));
               this.this$0.buttonsAdd(RealmsUploadScreen.access$1200(this.this$0));
               if (var1 != null) {
                  RealmsUploadScreen.access$1000().debug("Deleting file " + var1.getAbsolutePath());
                  var1.delete();
               }

               try {
                  var2.uploadFinished(var3);
               } catch (RealmsServiceException var13) {
                  RealmsUploadScreen.access$1000().error("Failed to request upload-finished to Realms", var13.toString());
               }

            }
         }
      }).start();
   }

   private void uploadCancelled(long var1) {
      this.status = getLocalizedString("mco.upload.cancelled");
      UploadTokenCache.invalidate(var1);
   }

   private boolean verify(File var1) {
      return var1.length() < 524288000L;
   }

   private File tarGzipArchive(File var1) throws IOException {
      TarArchiveOutputStream var2 = null;
      File var3 = File.createTempFile("realms-upload-file", ".tar.gz");
      var2 = new TarArchiveOutputStream(new GZIPOutputStream(new FileOutputStream(var3)));
      this.addFileToTarGz(var2, var1.getAbsolutePath(), "world", true);
      var2.finish();
      if (var2 != null) {
         var2.close();
      }

      return var3;
   }

   private void addFileToTarGz(TarArchiveOutputStream var1, String var2, String var3, boolean var4) throws IOException {
      if (!this.cancelled) {
         File var5 = new File(var2);
         String var6 = var4 ? var3 : var3 + var5.getName();
         TarArchiveEntry var7 = new TarArchiveEntry(var5, var6);
         var1.putArchiveEntry(var7);
         if (var5.isFile()) {
            IOUtils.copy(new FileInputStream(var5), var1);
            var1.closeArchiveEntry();
         } else {
            var1.closeArchiveEntry();
            File[] var8 = var5.listFiles();
            if (var8 != null) {
               File[] var9 = var8;
               int var10 = var8.length;

               for(int var11 = 0; var11 < var10; ++var11) {
                  File var12 = var9[var11];
                  this.addFileToTarGz(var1, var12.getAbsolutePath(), var6 + "/", false);
               }
            }
         }

      }
   }

   static long access$000(RealmsUploadScreen var0) {
      return var0.worldId;
   }

   static ReentrantLock access$100() {
      return uploadLock;
   }

   static String access$202(RealmsUploadScreen var0, String var1) {
      return var0.status = var1;
   }

   static boolean access$300(RealmsUploadScreen var0) {
      return var0.cancelled;
   }

   static void access$400(RealmsUploadScreen var0, long var1) {
      var0.uploadCancelled(var1);
   }

   static RealmsLevelSummary access$500(RealmsUploadScreen var0) {
      return var0.selectedLevel;
   }

   static File access$600(RealmsUploadScreen var0, File var1) throws IOException {
      return var0.tarGzipArchive(var1);
   }

   static boolean access$700(RealmsUploadScreen var0, File var1) {
      return var0.verify(var1);
   }

   static String access$802(RealmsUploadScreen var0, String var1) {
      return var0.errorMessage = var1;
   }

   static UploadStatus access$900(RealmsUploadScreen var0) {
      return var0.uploadStatus;
   }

   static Logger access$1000() {
      return LOGGER;
   }

   static boolean access$1102(RealmsUploadScreen var0, boolean var1) {
      return var0.uploadFinished = var1;
   }

   static RealmsButton access$1200(RealmsUploadScreen var0) {
      return var0.backButton;
   }

   static boolean access$1302(RealmsUploadScreen var0, boolean var1) {
      return var0.showDots = var1;
   }

   static RealmsButton access$1400(RealmsUploadScreen var0) {
      return var0.cancelButton;
   }
}
