package net.minecraft.client.resources;

import com.google.common.collect.Sets;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

public class FolderResourcePack extends AbstractResourcePack {
   protected InputStream getInputStreamByName(String var1) throws IOException {
      return new BufferedInputStream(new FileInputStream(new File(this.resourcePackFile, var1)));
   }

   public FolderResourcePack(File var1) {
      super(var1);
   }

   protected boolean hasResourceName(String var1) {
      return (new File(this.resourcePackFile, var1)).isFile();
   }

   public Set getResourceDomains() {
      HashSet var1 = Sets.newHashSet();
      File var2 = new File(this.resourcePackFile, "assets/");
      if (var2.isDirectory()) {
         File[] var6;
         int var5 = (var6 = var2.listFiles(DirectoryFileFilter.DIRECTORY)).length;

         for(int var4 = 0; var4 < var5; ++var4) {
            File var3 = var6[var4];
            String var7 = getRelativeName(var2, var3);
            if (!var7.equals(var7.toLowerCase())) {
               this.logNameNotLowercase(var7);
            } else {
               var1.add(var7.substring(0, var7.length() - 1));
            }
         }
      }

      return var1;
   }
}
