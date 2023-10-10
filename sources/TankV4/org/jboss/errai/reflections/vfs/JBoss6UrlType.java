package org.jboss.errai.reflections.vfs;

import java.net.URISyntaxException;
import java.net.URL;
import org.jboss.vfs.VFS;
import org.jboss.vfs.VirtualFile;

public class JBoss6UrlType implements Vfs.UrlType {
   final String VFS_PROTOCOL = "vfs";

   public boolean matches(URL var1) {
      return var1.getProtocol().equals("vfs");
   }

   public Vfs.Dir createDir(URL var1) {
      try {
         VirtualFile var2 = VFS.getChild(var1);
         if (!var2.isDirectory()) {
            throw new RuntimeException("URL " + var1 + " doesn't point to a Directory");
         } else {
            return new JBoss6Dir(var2);
         }
      } catch (URISyntaxException var3) {
         throw new RuntimeException(var3);
      }
   }
}
