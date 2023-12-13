package shadersmod.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import optifine.StrUtils;

public class ShaderPackZip implements IShaderPack {
   protected File packFile;
   protected ZipFile packZipFile;

   public ShaderPackZip(String var1, File var2) {
      this.packFile = var2;
      this.packZipFile = null;
   }

   public void close() {
      if (this.packZipFile != null) {
         try {
            this.packZipFile.close();
         } catch (Exception var2) {
         }

         this.packZipFile = null;
      }

   }

   public InputStream getResourceAsStream(String var1) {
      try {
         if (this.packZipFile == null) {
            this.packZipFile = new ZipFile(this.packFile);
         }

         String var2 = StrUtils.removePrefix(var1, "/");
         ZipEntry var3 = this.packZipFile.getEntry(var2);
         return var3 == null ? null : this.packZipFile.getInputStream(var3);
      } catch (Exception var4) {
         return null;
      }
   }

   public boolean hasDirectory(String var1) {
      try {
         if (this.packZipFile == null) {
            this.packZipFile = new ZipFile(this.packFile);
         }

         String var2 = StrUtils.removePrefix(var1, "/");
         ZipEntry var3 = this.packZipFile.getEntry(var2);
         return var3 != null;
      } catch (IOException var4) {
         return false;
      }
   }

   public String getName() {
      return this.packFile.getName();
   }
}
