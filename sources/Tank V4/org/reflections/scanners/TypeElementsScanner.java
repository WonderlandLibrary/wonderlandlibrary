package org.reflections.scanners;

import com.google.common.base.Joiner;
import java.util.Iterator;

public class TypeElementsScanner extends AbstractScanner {
   private boolean includeFields = true;
   private boolean includeMethods = true;
   private boolean includeAnnotations = true;
   private boolean publicOnly = true;

   public void scan(Object var1) {
      String var2 = this.getMetadataAdapter().getClassName(var1);
      if (this.acceptResult(var2)) {
         this.getStore().put(var2, "");
         Iterator var3;
         Object var4;
         String var5;
         if (this.includeFields) {
            var3 = this.getMetadataAdapter().getFields(var1).iterator();

            while(var3.hasNext()) {
               var4 = var3.next();
               var5 = this.getMetadataAdapter().getFieldName(var4);
               this.getStore().put(var2, var5);
            }
         }

         if (this.includeMethods) {
            var3 = this.getMetadataAdapter().getMethods(var1).iterator();

            label39:
            while(true) {
               do {
                  if (!var3.hasNext()) {
                     break label39;
                  }

                  var4 = var3.next();
               } while(this.publicOnly && !this.getMetadataAdapter().isPublic(var4));

               var5 = this.getMetadataAdapter().getMethodName(var4) + "(" + Joiner.on(", ").join((Iterable)this.getMetadataAdapter().getParameterNames(var4)) + ")";
               this.getStore().put(var2, var5);
            }
         }

         if (this.includeAnnotations) {
            var3 = this.getMetadataAdapter().getClassAnnotationNames(var1).iterator();

            while(var3.hasNext()) {
               var4 = var3.next();
               this.getStore().put(var2, "@" + var4);
            }
         }

      }
   }

   public TypeElementsScanner includeFields() {
      return this.includeFields(true);
   }

   public TypeElementsScanner includeFields(boolean var1) {
      this.includeFields = var1;
      return this;
   }

   public TypeElementsScanner includeMethods() {
      return this.includeMethods(true);
   }

   public TypeElementsScanner includeMethods(boolean var1) {
      this.includeMethods = var1;
      return this;
   }

   public TypeElementsScanner includeAnnotations() {
      return this.includeAnnotations(true);
   }

   public TypeElementsScanner includeAnnotations(boolean var1) {
      this.includeAnnotations = var1;
      return this;
   }

   public TypeElementsScanner publicOnly(boolean var1) {
      this.publicOnly = var1;
      return this;
   }

   public TypeElementsScanner publicOnly() {
      return this.publicOnly(true);
   }
}
