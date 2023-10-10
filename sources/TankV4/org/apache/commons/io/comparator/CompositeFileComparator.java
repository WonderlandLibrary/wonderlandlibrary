package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class CompositeFileComparator extends AbstractFileComparator implements Serializable {
   private static final Comparator[] NO_COMPARATORS = new Comparator[0];
   private final Comparator[] delegates;

   public CompositeFileComparator(Comparator... var1) {
      if (var1 == null) {
         this.delegates = (Comparator[])NO_COMPARATORS;
      } else {
         this.delegates = (Comparator[])(new Comparator[var1.length]);
         System.arraycopy(var1, 0, this.delegates, 0, var1.length);
      }

   }

   public CompositeFileComparator(Iterable var1) {
      if (var1 == null) {
         this.delegates = (Comparator[])NO_COMPARATORS;
      } else {
         ArrayList var2 = new ArrayList();
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            Comparator var4 = (Comparator)var3.next();
            var2.add(var4);
         }

         this.delegates = (Comparator[])((Comparator[])var2.toArray(new Comparator[var2.size()]));
      }

   }

   public int compare(File var1, File var2) {
      int var3 = 0;
      Comparator[] var4 = this.delegates;
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Comparator var7 = var4[var6];
         var3 = var7.compare(var1, var2);
         if (var3 != 0) {
            break;
         }
      }

      return var3;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.toString());
      var1.append('{');

      for(int var2 = 0; var2 < this.delegates.length; ++var2) {
         if (var2 > 0) {
            var1.append(',');
         }

         var1.append(this.delegates[var2]);
      }

      var1.append('}');
      return var1.toString();
   }

   public List sort(List var1) {
      return super.sort(var1);
   }

   public File[] sort(File[] var1) {
      return super.sort(var1);
   }

   public int compare(Object var1, Object var2) {
      return this.compare((File)var1, (File)var2);
   }
}
