package org.reflections.vfs;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Lists;
import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class SystemDir implements Vfs.Dir {
   private final File file;

   public SystemDir(File var1) {
      if (var1 == null || var1.isDirectory() && var1.canRead()) {
         this.file = var1;
      } else {
         throw new RuntimeException("cannot use dir " + var1);
      }
   }

   public String getPath() {
      return this.file == null ? "/NO-SUCH-DIRECTORY/" : this.file.getPath().replace("\\", "/");
   }

   public Iterable getFiles() {
      return (Iterable)(this.file != null && this.file.exists() ? new Iterable(this) {
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
      } : Collections.emptyList());
   }

   private static List listFiles(File var0) {
      File[] var1 = var0.listFiles();
      return var1 != null ? Lists.newArrayList((Object[])var1) : Lists.newArrayList();
   }

   public void close() {
   }

   public String toString() {
      return this.getPath();
   }

   static File access$000(SystemDir var0) {
      return var0.file;
   }

   static List access$100(File var0) {
      return listFiles(var0);
   }
}
