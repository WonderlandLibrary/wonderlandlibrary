package org.reflections.vfs;

import com.google.common.collect.AbstractIterator;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import org.reflections.Reflections;

public class ZipDir implements Vfs.Dir {
   final java.util.zip.ZipFile jarFile;

   public ZipDir(JarFile var1) {
      this.jarFile = var1;
   }

   public String getPath() {
      return this.jarFile.getName();
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
                  this.entries = this.this$1.this$0.jarFile.entries();
               }

               protected Vfs.File computeNext() {
                  while(true) {
                     if (this.entries.hasMoreElements()) {
                        ZipEntry var1 = (ZipEntry)this.entries.nextElement();
                        if (var1.isDirectory()) {
                           continue;
                        }

                        return new ZipFile(this.this$1.this$0, var1);
                     }

                     return (Vfs.File)this.endOfData();
                  }
               }

               protected Object computeNext() {
                  return this.computeNext();
               }
            };
         }
      };
   }

   public void close() {
      try {
         this.jarFile.close();
      } catch (IOException var2) {
         if (Reflections.log != null) {
            Reflections.log.warn("Could not close JarFile", var2);
         }
      }

   }

   public String toString() {
      return this.jarFile.getName();
   }
}
