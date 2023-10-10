package optifine;

import java.util.Map;

public class FileUploadThread extends Thread {
   private String urlString;
   private Map headers;
   private byte[] content;
   private IFileUploadListener listener;

   public FileUploadThread(String var1, Map var2, byte[] var3, IFileUploadListener var4) {
      this.urlString = var1;
      this.headers = var2;
      this.content = var3;
      this.listener = var4;
   }

   public void run() {
      try {
         HttpUtils.post(this.urlString, this.headers, this.content);
         this.listener.fileUploadFinished(this.urlString, this.content, (Throwable)null);
      } catch (Exception var2) {
         this.listener.fileUploadFinished(this.urlString, this.content, var2);
      }

   }

   public String getUrlString() {
      return this.urlString;
   }

   public byte[] getContent() {
      return this.content;
   }

   public IFileUploadListener getListener() {
      return this.listener;
   }
}
