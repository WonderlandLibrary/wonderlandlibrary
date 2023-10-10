package org.reflections.vfs;

import com.google.common.collect.AbstractIterator;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.VFS;
import org.reflections.Reflections;

public class CommonsVfs2UrlType implements Vfs.UrlType {
   public boolean matches(URL var1) throws Exception {
      try {
         FileSystemManager var2 = VFS.getManager();
         FileObject var3 = var2.resolveFile(var1.toExternalForm());
         return var3.exists() && var3.getType() == FileType.FOLDER;
      } catch (FileSystemException var4) {
         Reflections.log.warn("Could not create CommonsVfs2UrlType from url " + var1.toExternalForm(), var4);
         return false;
      }
   }

   public Vfs.Dir createDir(URL var1) throws Exception {
      FileSystemManager var2 = VFS.getManager();
      FileObject var3 = var2.resolveFile(var1.toExternalForm());
      return new CommonsVfs2UrlType.Dir(var3);
   }

   public static class File implements Vfs.File {
      private final FileObject root;
      private final FileObject file;

      public File(FileObject var1, FileObject var2) {
         this.root = var1;
         this.file = var2;
      }

      public String getName() {
         return this.file.getName().getBaseName();
      }

      public String getRelativePath() {
         String var1 = this.file.getName().getPath().replace("\\", "/");
         return var1.startsWith(this.root.getName().getPath()) ? var1.substring(this.root.getName().getPath().length() + 1) : null;
      }

      public InputStream openInputStream() throws IOException {
         return this.file.getContent().getInputStream();
      }
   }

   public static class Dir implements Vfs.Dir {
      private final FileObject file;

      public Dir(FileObject var1) {
         this.file = var1;
      }

      public String getPath() {
         try {
            return this.file.getURL().getPath();
         } catch (FileSystemException var2) {
            throw new RuntimeException(var2);
         }
      }

      public Iterable getFiles() {
         return new Iterable(this) {
            final CommonsVfs2UrlType.Dir this$0;

            {
               this.this$0 = var1;
            }

            public Iterator iterator() {
               return this.this$0.new FileAbstractIterator(this.this$0);
            }
         };
      }

      public void close() {
         try {
            this.file.close();
         } catch (FileSystemException var2) {
         }

      }

      static FileObject access$100(CommonsVfs2UrlType.Dir var0) {
         return var0.file;
      }

      private class FileAbstractIterator extends AbstractIterator {
         final Stack stack;
         final CommonsVfs2UrlType.Dir this$0;

         private FileAbstractIterator(CommonsVfs2UrlType.Dir var1) {
            this.this$0 = var1;
            this.stack = new Stack();
            this.listDir(CommonsVfs2UrlType.Dir.access$100(this.this$0));
         }

         protected Vfs.File computeNext() {
            while(true) {
               if (!this.stack.isEmpty()) {
                  FileObject var1 = (FileObject)this.stack.pop();

                  try {
                     if (this == var1) {
                        this.listDir(var1);
                        continue;
                     }

                     return this.getFile(var1);
                  } catch (FileSystemException var3) {
                     throw new RuntimeException(var3);
                  }
               }

               return (Vfs.File)this.endOfData();
            }
         }

         private CommonsVfs2UrlType.File getFile(FileObject var1) {
            return new CommonsVfs2UrlType.File(CommonsVfs2UrlType.Dir.access$100(this.this$0), var1);
         }

         private boolean listDir(FileObject var1) {
            return this.stack.addAll(this.listFiles(var1));
         }

         protected List listFiles(FileObject var1) {
            try {
               FileObject[] var2 = var1.getType().hasChildren() ? var1.getChildren() : null;
               return (List)(var2 != null ? Arrays.asList(var2) : new ArrayList());
            } catch (FileSystemException var3) {
               throw new RuntimeException(var3);
            }
         }

         protected Object computeNext() {
            return this.computeNext();
         }

         FileAbstractIterator(CommonsVfs2UrlType.Dir var1, Object var2) {
            this(var1);
         }
      }
   }
}
