package optifine;

import net.minecraft.client.Minecraft;

public class FileDownloadThread extends Thread {
   private String urlString = null;
   private IFileDownloadListener listener = null;

   public FileDownloadThread(String var1, IFileDownloadListener var2) {
      this.urlString = var1;
      this.listener = var2;
   }

   public void run() {
      try {
         byte[] var1 = HttpPipeline.get(this.urlString, Minecraft.getMinecraft().getProxy());
         this.listener.fileDownloadFinished(this.urlString, var1, (Throwable)null);
      } catch (Exception var2) {
         this.listener.fileDownloadFinished(this.urlString, (byte[])null, var2);
      }

   }

   public String getUrlString() {
      return this.urlString;
   }

   public IFileDownloadListener getListener() {
      return this.listener;
   }
}
