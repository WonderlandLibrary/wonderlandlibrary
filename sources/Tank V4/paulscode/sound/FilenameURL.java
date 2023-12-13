package paulscode.sound;

import java.net.URL;

public class FilenameURL {
   private SoundSystemLogger logger = SoundSystemConfig.getLogger();
   private String filename = null;
   private URL url = null;

   public FilenameURL(URL var1, String var2) {
      this.filename = var2;
      this.url = var1;
   }

   public FilenameURL(String var1) {
      this.filename = var1;
      this.url = null;
   }

   public String getFilename() {
      return this.filename;
   }

   public URL getURL() {
      if (this.url == null) {
         if (this.filename.matches(SoundSystemConfig.PREFIX_URL)) {
            try {
               this.url = new URL(this.filename);
            } catch (Exception var2) {
               this.errorMessage("Unable to access online URL in method 'getURL'");
               this.printStackTrace(var2);
               return null;
            }
         } else {
            this.url = this.getClass().getClassLoader().getResource(SoundSystemConfig.getSoundFilesPackage() + this.filename);
         }
      }

      return this.url;
   }

   private void errorMessage(String var1) {
      this.logger.errorMessage("MidiChannel", var1, 0);
   }

   private void printStackTrace(Exception var1) {
      this.logger.printStackTrace(var1, 1);
   }
}
