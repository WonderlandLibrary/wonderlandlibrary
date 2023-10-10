package com.mojang.realmsclient.client;

import com.mojang.realmsclient.RealmsVersion;
import com.mojang.realmsclient.dto.UploadInfo;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.Args;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileUpload {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final String UPLOAD_PATH = "/upload";
   private static final String PORT = "8080";
   private volatile boolean cancelled = false;
   private volatile boolean finished = false;
   private HttpPost request;
   private int statusCode = -1;
   private RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(120000).setConnectTimeout(120000).build();
   private Thread currentThread;

   public void upload(File var1, long var2, UploadInfo var4, String var5, String var6, String var7, UploadStatus var8) {
      if (this.currentThread == null) {
         this.currentThread = new Thread(this, var4, var2, var5, var6, var7, var8, var1) {
            final UploadInfo val$uploadInfo;
            final long val$worldId;
            final String val$sessionId;
            final String val$username;
            final String val$clientVersion;
            final UploadStatus val$uploadStatus;
            final File val$file;
            final FileUpload this$0;

            {
               this.this$0 = var1;
               this.val$uploadInfo = var2;
               this.val$worldId = var3;
               this.val$sessionId = var5;
               this.val$username = var6;
               this.val$clientVersion = var7;
               this.val$uploadStatus = var8;
               this.val$file = var9;
            }

            public void run() {
               FileUpload.access$002(this.this$0, new HttpPost("http://" + this.val$uploadInfo.getUploadEndpoint() + ":" + "8080" + "/upload" + "/" + this.val$worldId));
               CloseableHttpClient var1 = null;

               try {
                  var1 = HttpClientBuilder.create().setDefaultRequestConfig(FileUpload.access$100(this.this$0)).build();
                  String var2 = RealmsVersion.getVersion();
                  if (var2 != null) {
                     FileUpload.access$000(this.this$0).setHeader("Cookie", "sid=" + this.val$sessionId + ";token=" + this.val$uploadInfo.getToken() + ";user=" + this.val$username + ";version=" + this.val$clientVersion + ";realms_version=" + var2);
                  } else {
                     FileUpload.access$000(this.this$0).setHeader("Cookie", "sid=" + this.val$sessionId + ";token=" + this.val$uploadInfo.getToken() + ";user=" + this.val$username + ";version=" + this.val$clientVersion);
                  }

                  this.val$uploadStatus.totalBytes = this.val$file.length();
                  FileUpload.CustomInputStreamEntity var3 = new FileUpload.CustomInputStreamEntity(new FileInputStream(this.val$file), this.val$file.length(), this.val$uploadStatus);
                  var3.setContentType("application/octet-stream");
                  FileUpload.access$000(this.this$0).setEntity(var3);
                  CloseableHttpResponse var4 = var1.execute(FileUpload.access$000(this.this$0));
                  int var5 = var4.getStatusLine().getStatusCode();
                  if (var5 == 401) {
                     FileUpload.access$200().debug("Realms server returned 401: " + var4.getFirstHeader("WWW-Authenticate"));
                  }

                  FileUpload.access$302(this.this$0, var5);
               } catch (Exception var10) {
                  FileUpload.access$200().error("Caught exception while uploading: " + var10.getMessage());
                  FileUpload.access$000(this.this$0).releaseConnection();
                  FileUpload.access$402(this.this$0, true);
                  if (var1 != null) {
                     try {
                        var1.close();
                     } catch (IOException var8) {
                        FileUpload.access$200().error("Failed to close Realms upload client");
                     }

                     return;
                  }

                  return;
               }

               FileUpload.access$000(this.this$0).releaseConnection();
               FileUpload.access$402(this.this$0, true);
               if (var1 != null) {
                  try {
                     var1.close();
                  } catch (IOException var9) {
                     FileUpload.access$200().error("Failed to close Realms upload client");
                  }
               }

            }
         };
         this.currentThread.start();
      }
   }

   public void cancel() {
      this.cancelled = true;
      if (this.request != null) {
         this.request.abort();
      }

   }

   public boolean isFinished() {
      return this.finished;
   }

   public int getStatusCode() {
      return this.statusCode;
   }

   static HttpPost access$002(FileUpload var0, HttpPost var1) {
      return var0.request = var1;
   }

   static RequestConfig access$100(FileUpload var0) {
      return var0.requestConfig;
   }

   static HttpPost access$000(FileUpload var0) {
      return var0.request;
   }

   static Logger access$200() {
      return LOGGER;
   }

   static int access$302(FileUpload var0, int var1) {
      return var0.statusCode = var1;
   }

   static boolean access$402(FileUpload var0, boolean var1) {
      return var0.finished = var1;
   }

   private static class CustomInputStreamEntity extends InputStreamEntity {
      private final long length;
      private final InputStream content;
      private final UploadStatus uploadStatus;

      public CustomInputStreamEntity(InputStream var1, long var2, UploadStatus var4) {
         super(var1);
         this.content = var1;
         this.length = var2;
         this.uploadStatus = var4;
      }

      public void writeTo(OutputStream var1) throws IOException {
         Args.notNull(var1, "Output stream");
         InputStream var2 = this.content;
         byte[] var3 = new byte[4096];
         int var4;
         if (this.length < 0L) {
            while((var4 = var2.read(var3)) != -1) {
               var1.write(var3, 0, var4);
               UploadStatus var9 = this.uploadStatus;
               var9.bytesWritten = var9.bytesWritten + (long)var4;
            }
         } else {
            long var5 = this.length;

            while(var5 > 0L) {
               var4 = var2.read(var3, 0, (int)Math.min(4096L, var5));
               if (var4 == -1) {
                  break;
               }

               var1.write(var3, 0, var4);
               UploadStatus var7 = this.uploadStatus;
               var7.bytesWritten = var7.bytesWritten + (long)var4;
               var5 -= (long)var4;
               var1.flush();
            }
         }

         var2.close();
      }
   }
}
