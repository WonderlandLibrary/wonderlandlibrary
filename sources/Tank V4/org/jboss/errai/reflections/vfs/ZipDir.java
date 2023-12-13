package org.jboss.errai.reflections.vfs;

import com.google.common.collect.AbstractIterator;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.ZipEntry;

public class ZipDir implements Vfs.Dir {
   final java.util.zip.ZipFile zipFile;
   private String path;

   public ZipDir(URL var1) {
      this(var1.getPath());
   }

   public ZipDir(String var1) {
      this.path = var1;
      if (this.path.startsWith("jar:")) {
         this.path = this.path.substring(4);
      }

      if (this.path.startsWith("file:")) {
         this.path = this.path.substring(5);
      }

      if (this.path.endsWith("!/")) {
         this.path = this.path.substring(0, this.path.lastIndexOf("!/")) + "/";
      }

      try {
         this.zipFile = new java.util.zip.ZipFile(this.path);
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public String getPath() {
      return this.path;
   }

   public Iterable getFiles() {
      return new Iterable(this) {
         final ZipDir this$0;

         {
            this.this$0 = var1;
         }

         public Iterator iterator() {
            return new AbstractIterator(this) {
               final Enumeration entries;
               final <undefinedtype> this$1;

               {
                  this.this$1 = var1;
                  this.entries = this.this$1.this$0.zipFile.entries();
               }

               protected Vfs.File computeNext() {
                  return (Vfs.File)(this.entries.hasMoreElements() ? new ZipFile(this.this$1.this$0, (ZipEntry)this.entries.nextElement()) : (Vfs.File)this.endOfData());
               }

               protected Object computeNext() {
                  return this.computeNext();
               }
            };
         }
      };
   }

   public void close() {
      if (this.zipFile != null) {
         try {
            this.zipFile.close();
         } catch (IOException var2) {
            throw new RuntimeException("could not close zip file " + this.path, var2);
         }
      }

   }

   public String toString() {
      return this.zipFile.getName();
   }
}
