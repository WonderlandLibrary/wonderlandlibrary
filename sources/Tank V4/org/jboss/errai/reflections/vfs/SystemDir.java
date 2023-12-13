package org.jboss.errai.reflections.vfs;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Lists;
import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class SystemDir implements Vfs.Dir {
   private final File file;

   public SystemDir(URL var1) {
      this.file = new File(Vfs.normalizePath(var1));
   }

   public String getPath() {
      return this.file.getPath();
   }

   public Iterable getFiles() {
      return new Iterable(this) {
         final SystemDir this$0;

         {
            this.this$0 = var1;
         }

         public Iterator iterator() {
            return new AbstractIterator(this) {
               final Stack stack;
               final <undefinedtype> this$1;

               {
                  this.this$1 = var1;
                  this.stack = new Stack();
                  this.stack.addAll(SystemDir.access$100(SystemDir.access$000(this.this$1.this$0)));
               }

               protected Vfs.File computeNext() {
                  while(true) {
                     if (!this.stack.isEmpty()) {
                        File var1 = (File)this.stack.pop();
                        if (var1.isDirectory()) {
                           this.stack.addAll(SystemDir.access$100(var1));
                           continue;
                        }

                        return new SystemFile(this.this$1.this$0, var1);
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
   }

   public String toString() {
      return this.file.toString();
   }

   private static List listFiles(File var0) {
      File[] var1 = var0.listFiles();
      return var1 != null ? Lists.newArrayList((Object[])var1) : Lists.newArrayList();
   }

   static File access$000(SystemDir var0) {
      return var0.file;
   }

   static List access$100(File var0) {
      return listFiles(var0);
   }
}
