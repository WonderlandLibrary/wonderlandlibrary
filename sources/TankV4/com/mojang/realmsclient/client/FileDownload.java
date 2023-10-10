package com.mojang.realmsclient.client;

import com.mojang.realmsclient.gui.screens.DownloadLatestWorldScreen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsAnvilLevelStorageSource;
import net.minecraft.realms.RealmsLevelSummary;
import net.minecraft.realms.RealmsSharedConstants;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.CountingOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileDownload {
   private static final Logger LOGGER = LogManager.getLogger();
   private volatile boolean cancelled = false;
   private volatile boolean finished = false;
   private volatile boolean error = false;
   private volatile boolean extracting = false;
   private volatile File tempFile;
   private volatile HttpGet request;
   private Thread currentThread;
   private RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(120000).setConnectTimeout(120000).build();
   private static final String[] INVALID_FILE_NAMES = new String[]{"CON", "COM", "PRN", "AUX", "CLOCK$", "NUL", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9"};

   public void download(String var1, String var2, DownloadLatestWorldScreen.DownloadStatus var3, RealmsAnvilLevelStorageSource var4) {
      if (this.currentThread == null) {
         this.currentThread = new Thread(this, var1, var3, var2, var4) {
            final String val$downloadLink;
            final DownloadLatestWorldScreen.DownloadStatus val$downloadStatus;
            final String val$worldName;
            final RealmsAnvilLevelStorageSource val$levelStorageSource;
            final FileDownload this$0;

            {
               this.this$0 = var1;
               this.val$downloadLink = var2;
               this.val$downloadStatus = var3;
               this.val$worldName = var4;
               this.val$levelStorageSource = var5;
            }

            public void run() {
               CloseableHttpClient var1 = null;

               label77: {
                  try {
                     FileDownload.access$002(this.this$0, File.createTempFile("backup", ".tar.gz"));
                     FileDownload.access$102(this.this$0, new HttpGet(this.val$downloadLink));
                     var1 = HttpClientBuilder.create().setDefaultRequestConfig(FileDownload.access$200(this.this$0)).build();
                     CloseableHttpResponse var2 = var1.execute(FileDownload.access$100(this.this$0));
                     this.val$downloadStatus.totalBytes = Long.parseLong(var2.getFirstHeader("Content-Length").getValue());
                     if (var2.getStatusLine().getStatusCode() != 200) {
                        FileDownload.access$302(this.this$0, true);
                        FileDownload.access$100(this.this$0).abort();
                        break label77;
                     }

                     FileOutputStream var3 = new FileOutputStream(FileDownload.access$000(this.this$0));
                     FileDownload.ProgressListener var4 = this.this$0.new ProgressListener(this.this$0, this.val$worldName.trim(), FileDownload.access$000(this.this$0), this.val$levelStorageSource, this.val$downloadStatus);
                     FileDownload.DownloadCountingOutputStream var5 = this.this$0.new DownloadCountingOutputStream(this.this$0, var3);
                     var5.setListener(var4);
                     IOUtils.copy((InputStream)var2.getEntity().getContent(), (OutputStream)var5);
                  } catch (Exception var11) {
                     FileDownload.access$500().error("Caught exception while downloading: " + var11.getMessage());
                     FileDownload.access$302(this.this$0, true);
                     FileDownload.access$100(this.this$0).releaseConnection();
                     if (FileDownload.access$000(this.this$0) != null) {
                        FileDownload.access$000(this.this$0).delete();
                     }

                     if (var1 != null) {
                        try {
                           var1.close();
                        } catch (IOException var9) {
                           FileDownload.access$500().error("Failed to close Realms download client");
                        }

                        return;
                     }

                     return;
                  }

                  FileDownload.access$100(this.this$0).releaseConnection();
                  if (FileDownload.access$000(this.this$0) != null) {
                     FileDownload.access$000(this.this$0).delete();
                  }

                  if (var1 != null) {
                     try {
                        var1.close();
                     } catch (IOException var10) {
                        FileDownload.access$500().error("Failed to close Realms download client");
                     }
                  }

                  return;
               }

               FileDownload.access$100(this.this$0).releaseConnection();
               if (FileDownload.access$000(this.this$0) != null) {
                  FileDownload.access$000(this.this$0).delete();
               }

               if (var1 != null) {
                  try {
                     var1.close();
                  } catch (IOException var8) {
                     FileDownload.access$500().error("Failed to close Realms download client");
                  }
               }

            }
         };
         this.currentThread.start();
      }
   }

   public void cancel() {
      if (this.request != null) {
         this.request.abort();
      }

      if (this.tempFile != null) {
         this.tempFile.delete();
      }

      this.cancelled = true;
   }

   public boolean isFinished() {
      return this.finished;
   }

   public boolean isError() {
      return this.error;
   }

   public boolean isExtracting() {
      return this.extracting;
   }

   public static String findAvailableFolderName(String var0) {
      var0 = var0.replaceAll("[\\./\"]", "_");
      String[] var1 = INVALID_FILE_NAMES;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String var4 = var1[var3];
         if (var0.equalsIgnoreCase(var4)) {
            var0 = "_" + var0 + "_";
         }
      }

      return var0;
   }

   private void untarGzipArchive(String var1, File var2, RealmsAnvilLevelStorageSource var3) throws IOException {
      int var4 = 1;
      char[] var5 = RealmsSharedConstants.ILLEGAL_FILE_CHARACTERS;
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         char var8 = var5[var7];
         var1 = var1.replace(var8, '_');
      }

      if (StringUtils.isEmpty(var1)) {
         var1 = "Realm";
      }

      var1 = findAvailableFolderName(var1);

      Iterator var16;
      try {
         var16 = var3.getLevelList().iterator();

         while(var16.hasNext()) {
            RealmsLevelSummary var18 = (RealmsLevelSummary)var16.next();
            if (var18.getLevelName().toLowerCase().startsWith(var1.toLowerCase())) {
               ++var4;
            }
         }
      } catch (Exception var15) {
         this.error = true;
         return;
      }

      var1 = var1 + (var4 == 1 ? "" : "-" + var4);
      var16 = null;
      File var19 = new File(Realms.getGameDirectoryPath(), "saves");

      TarArchiveInputStream var17;
      try {
         var19.mkdir();
         var17 = new TarArchiveInputStream(new GzipCompressorInputStream(new BufferedInputStream(new FileInputStream(var2))));

         for(TarArchiveEntry var20 = var17.getNextTarEntry(); var20 != null; var20 = var17.getNextTarEntry()) {
            File var21 = new File(var19, var20.getName().replace("world", var1));
            if (var20.isDirectory()) {
               var21.mkdirs();
            } else {
               var21.createNewFile();
               byte[] var9 = new byte[1024];
               BufferedOutputStream var10 = new BufferedOutputStream(new FileOutputStream(var21));
               boolean var11 = false;

               int var23;
               while((var23 = var17.read(var9)) != -1) {
                  var10.write(var9, 0, var23);
               }

               var10.close();
               Object var22 = null;
            }
         }
      } catch (Exception var14) {
         this.error = true;
         if (var16 != null) {
            var16.close();
         }

         if (var2 != null) {
            var2.delete();
         }

         var3.renameLevel(var1, var1.trim());
         this.finished = true;
         return;
      }

      if (var17 != null) {
         var17.close();
      }

      if (var2 != null) {
         var2.delete();
      }

      var3.renameLevel(var1, var1.trim());
      this.finished = true;
   }

   static File access$002(FileDownload var0, File var1) {
      return var0.tempFile = var1;
   }

   static HttpGet access$102(FileDownload var0, HttpGet var1) {
      return var0.request = var1;
   }

   static RequestConfig access$200(FileDownload var0) {
      return var0.requestConfig;
   }

   static HttpGet access$100(FileDownload var0) {
      return var0.request;
   }

   static boolean access$302(FileDownload var0, boolean var1) {
      return var0.error = var1;
   }

   static File access$000(FileDownload var0) {
      return var0.tempFile;
   }

   static Logger access$500() {
      return LOGGER;
   }

   static boolean access$600(FileDownload var0) {
      return var0.cancelled;
   }

   static boolean access$702(FileDownload var0, boolean var1) {
      return var0.extracting = var1;
   }

   static void access$800(FileDownload var0, String var1, File var2, RealmsAnvilLevelStorageSource var3) throws IOException {
      var0.untarGzipArchive(var1, var2, var3);
   }

   private class DownloadCountingOutputStream extends CountingOutputStream {
      private ActionListener listener;
      final FileDownload this$0;

      public DownloadCountingOutputStream(FileDownload var1, OutputStream var2) {
         super(var2);
         this.this$0 = var1;
         this.listener = null;
      }

      public void setListener(ActionListener var1) {
         this.listener = var1;
      }

      protected void afterWrite(int var1) throws IOException {
         super.afterWrite(var1);
         if (this.listener != null) {
            this.listener.actionPerformed(new ActionEvent(this, 0, (String)null));
         }

      }
   }

   private class ProgressListener implements ActionListener {
      private volatile String worldName;
      private volatile File tempFile;
      private volatile RealmsAnvilLevelStorageSource levelStorageSource;
      private volatile DownloadLatestWorldScreen.DownloadStatus downloadStatus;
      final FileDownload this$0;

      private ProgressListener(FileDownload var1, String var2, File var3, RealmsAnvilLevelStorageSource var4, DownloadLatestWorldScreen.DownloadStatus var5) {
         this.this$0 = var1;
         this.worldName = var2;
         this.tempFile = var3;
         this.levelStorageSource = var4;
         this.downloadStatus = var5;
      }

      public void actionPerformed(ActionEvent var1) {
         this.downloadStatus.bytesWritten = ((FileDownload.DownloadCountingOutputStream)var1.getSource()).getByteCount();
         if (this.downloadStatus.bytesWritten >= this.downloadStatus.totalBytes && !FileDownload.access$600(this.this$0)) {
            try {
               FileDownload.access$702(this.this$0, true);
               FileDownload.access$800(this.this$0, this.worldName, this.tempFile, this.levelStorageSource);
            } catch (IOException var3) {
               FileDownload.access$302(this.this$0, true);
            }
         }

      }

      ProgressListener(FileDownload var1, String var2, File var3, RealmsAnvilLevelStorageSource var4, DownloadLatestWorldScreen.DownloadStatus var5, Object var6) {
         this(var1, var2, var3, var4, var5);
      }
   }
}
