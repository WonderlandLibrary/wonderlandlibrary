package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class OrFileFilter extends AbstractFileFilter implements ConditionalFileFilter, Serializable {
   private final List fileFilters;

   public OrFileFilter() {
      this.fileFilters = new ArrayList();
   }

   public OrFileFilter(List var1) {
      if (var1 == null) {
         this.fileFilters = new ArrayList();
      } else {
         this.fileFilters = new ArrayList(var1);
      }

   }

   public OrFileFilter(IOFileFilter var1, IOFileFilter var2) {
      if (var1 != null && var2 != null) {
         this.fileFilters = new ArrayList(2);
         this.addFileFilter(var1);
         this.addFileFilter(var2);
      } else {
         throw new IllegalArgumentException("The filters must not be null");
      }
   }

   public void addFileFilter(IOFileFilter var1) {
      this.fileFilters.add(var1);
   }

   public List getFileFilters() {
      return Collections.unmodifiableList(this.fileFilters);
   }

   public boolean removeFileFilter(IOFileFilter var1) {
      return this.fileFilters.remove(var1);
   }

   public void setFileFilters(List var1) {
      this.fileFilters.clear();
      this.fileFilters.addAll(var1);
   }

   public boolean accept(File var1) {
      Iterator var2 = this.fileFilters.iterator();

      IOFileFilter var3;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         var3 = (IOFileFilter)var2.next();
      } while(!var3.accept(var1));

      return true;
   }

   public boolean accept(File var1, String var2) {
      Iterator var3 = this.fileFilters.iterator();

      IOFileFilter var4;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         var4 = (IOFileFilter)var3.next();
      } while(!var4.accept(var1, var2));

      return true;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.toString());
      var1.append("(");
      if (this.fileFilters != null) {
         for(int var2 = 0; var2 < this.fileFilters.size(); ++var2) {
            if (var2 > 0) {
               var1.append(",");
            }

            Object var3 = this.fileFilters.get(var2);
            var1.append(var3 == null ? "null" : var3.toString());
         }
      }

      var1.append(")");
      return var1.toString();
   }
}
