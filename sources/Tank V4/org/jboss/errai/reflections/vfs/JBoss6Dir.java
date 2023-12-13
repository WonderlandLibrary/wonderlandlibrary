package org.jboss.errai.reflections.vfs;

import com.google.common.collect.AbstractIterator;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import org.jboss.vfs.VirtualFile;

class JBoss6Dir implements Vfs.Dir {
   private VirtualFile parent;

   public JBoss6Dir(VirtualFile var1) {
      this.parent = var1;
   }

   public String getPath() {
      return this.parent.getPathName();
   }

   public Iterable getFiles() {
      return new Iterable(this) {
         final JBoss6Dir this$0;

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
                  this.stack.addAll(JBoss6Dir.access$100(this.this$1.this$0, JBoss6Dir.access$000(this.this$1.this$0)));
               }

               protected Vfs.File computeNext() {
                  while(true) {
                     if (!this.stack.isEmpty()) {
                        VirtualFile var1 = (VirtualFile)this.stack.pop();
                        if (var1.isDirectory()) {
                           this.stack.addAll(JBoss6Dir.access$100(this.this$1.this$0, var1));
                           continue;
                        }

                        return new JBoss6File(JBoss6Dir.access$000(this.this$1.this$0), var1);
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

   private List listFiles(VirtualFile var1) {
      return var1.getChildren();
   }

   public void close() {
   }

   static VirtualFile access$000(JBoss6Dir var0) {
      return var0.parent;
   }

   static List access$100(JBoss6Dir var0, VirtualFile var1) {
      return var0.listFiles(var1);
   }
}
