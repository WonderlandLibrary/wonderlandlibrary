package org.reflections.vfs;

import com.google.common.collect.AbstractIterator;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import org.reflections.ReflectionsException;
import org.reflections.util.Utils;

public class JarInputDir implements Vfs.Dir {
   private final URL url;
   JarInputStream jarInputStream;
   long cursor = 0L;
   long nextCursor = 0L;

   public JarInputDir(URL var1) {
      this.url = var1;
   }

   public String getPath() {
      return this.url.getPath();
   }

   public Iterable getFiles() {
      return new Iterable(this) {
         final JarInputDir this$0;

         {
            this.this$0 = var1;
         }

         public Iterator iterator() {
            return new AbstractIterator(this) {
               final <undefinedtype> this$1;

               {
                  this.this$1 = var1;

                  try {
                     this.this$1.this$0.jarInputStream = new JarInputStream(JarInputDir.access$000(this.this$1.this$0).openConnection().getInputStream());
                  } catch (Exception var3) {
                     throw new ReflectionsException("Could not open url connection", var3);
                  }
               }

               protected Vfs.File computeNext() {
                  while(true) {
                     try {
                        JarEntry var1 = this.this$1.this$0.jarInputStream.getNextJarEntry();
                        if (var1 == null) {
                           return (Vfs.File)this.endOfData();
                        }

                        long var2 = var1.getSize();
                        if (var2 < 0L) {
                           var2 += 4294967295L;
                        }

                        JarInputDir var10000 = this.this$1.this$0;
                        var10000.nextCursor += var2;
                        if (!var1.isDirectory()) {
                           return new JarInputFile(var1, this.this$1.this$0, this.this$1.this$0.cursor, this.this$1.this$0.nextCursor);
                        }
                     } catch (IOException var4) {
                        throw new ReflectionsException("could not get next zip entry", var4);
                     }
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
      Utils.close(this.jarInputStream);
   }

   static URL access$000(JarInputDir var0) {
      return var0.url;
   }
}
