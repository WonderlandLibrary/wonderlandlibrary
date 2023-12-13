package org.jboss.errai.reflections.vfs;

import java.io.IOException;
import java.io.InputStream;
import org.jboss.vfs.VirtualFile;

class JBoss6File implements Vfs.File {
   private VirtualFile file;
   private VirtualFile parent;

   public JBoss6File(VirtualFile var1, VirtualFile var2) {
      this.file = var2;
      this.parent = var1;
   }

   public String getName() {
      return this.file.getName();
   }

   public String getRelativePath() {
      return this.file.getPathNameRelativeTo(this.parent);
   }

   public String getFullPath() {
      return this.file.getPathName();
   }

   public InputStream openInputStream() throws IOException {
      return this.file.openStream();
   }
}
