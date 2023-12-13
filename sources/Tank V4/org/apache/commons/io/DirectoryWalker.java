package org.apache.commons.io;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collection;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

public abstract class DirectoryWalker {
   private final FileFilter filter;
   private final int depthLimit;

   protected DirectoryWalker() {
      this((FileFilter)null, -1);
   }

   protected DirectoryWalker(FileFilter var1, int var2) {
      this.filter = var1;
      this.depthLimit = var2;
   }

   protected DirectoryWalker(IOFileFilter var1, IOFileFilter var2, int var3) {
      if (var1 == null && var2 == null) {
         this.filter = null;
      } else {
         var1 = var1 != null ? var1 : TrueFileFilter.TRUE;
         var2 = var2 != null ? var2 : TrueFileFilter.TRUE;
         var1 = FileFilterUtils.makeDirectoryOnly(var1);
         var2 = FileFilterUtils.makeFileOnly(var2);
         this.filter = FileFilterUtils.or(var1, var2);
      }

      this.depthLimit = var3;
   }

   protected final void walk(File var1, Collection var2) throws IOException {
      if (var1 == null) {
         throw new NullPointerException("Start Directory is null");
      } else {
         try {
            this.handleStart(var1, var2);
            this.walk(var1, 0, var2);
            this.handleEnd(var2);
         } catch (DirectoryWalker.CancelException var4) {
            this.handleCancelled(var1, var2, var4);
         }

      }
   }

   private void walk(File var1, int var2, Collection var3) throws IOException {
      this.checkIfCancelled(var1, var2, var3);
      if (this.handleDirectory(var1, var2, var3)) {
         this.handleDirectoryStart(var1, var2, var3);
         int var4 = var2 + 1;
         if (this.depthLimit < 0 || var4 <= this.depthLimit) {
            this.checkIfCancelled(var1, var2, var3);
            File[] var5 = this.filter == null ? var1.listFiles() : var1.listFiles(this.filter);
            var5 = this.filterDirectoryContents(var1, var2, var5);
            if (var5 == null) {
               this.handleRestricted(var1, var4, var3);
            } else {
               File[] var6 = var5;
               int var7 = var5.length;

               for(int var8 = 0; var8 < var7; ++var8) {
                  File var9 = var6[var8];
                  if (var9.isDirectory()) {
                     this.walk(var9, var4, var3);
                  } else {
                     this.checkIfCancelled(var9, var4, var3);
                     this.handleFile(var9, var4, var3);
                     this.checkIfCancelled(var9, var4, var3);
                  }
               }
            }
         }

         this.handleDirectoryEnd(var1, var2, var3);
      }

      this.checkIfCancelled(var1, var2, var3);
   }

   protected final void checkIfCancelled(File var1, int var2, Collection var3) throws IOException {
      if (this.handleIsCancelled(var1, var2, var3)) {
         throw new DirectoryWalker.CancelException(var1, var2);
      }
   }

   protected boolean handleIsCancelled(File var1, int var2, Collection var3) throws IOException {
      return false;
   }

   protected void handleCancelled(File var1, Collection var2, DirectoryWalker.CancelException var3) throws IOException {
      throw var3;
   }

   protected void handleStart(File var1, Collection var2) throws IOException {
   }

   protected boolean handleDirectory(File var1, int var2, Collection var3) throws IOException {
      return true;
   }

   protected void handleDirectoryStart(File var1, int var2, Collection var3) throws IOException {
   }

   protected File[] filterDirectoryContents(File var1, int var2, File[] var3) throws IOException {
      return var3;
   }

   protected void handleFile(File var1, int var2, Collection var3) throws IOException {
   }

   protected void handleRestricted(File var1, int var2, Collection var3) throws IOException {
   }

   protected void handleDirectoryEnd(File var1, int var2, Collection var3) throws IOException {
   }

   protected void handleEnd(Collection var1) throws IOException {
   }

   public static class CancelException extends IOException {
      private static final long serialVersionUID = 1347339620135041008L;
      private final File file;
      private final int depth;

      public CancelException(File var1, int var2) {
         this("Operation Cancelled", var1, var2);
      }

      public CancelException(String var1, File var2, int var3) {
         super(var1);
         this.file = var2;
         this.depth = var3;
      }

      public File getFile() {
         return this.file;
      }

      public int getDepth() {
         return this.depth;
      }
   }
}
