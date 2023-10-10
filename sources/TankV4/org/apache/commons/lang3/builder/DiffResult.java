package org.apache.commons.lang3.builder;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class DiffResult implements Iterable {
   public static final String OBJECTS_SAME_STRING = "";
   private static final String DIFFERS_STRING = "differs from";
   private final List diffs;
   private final Object lhs;
   private final Object rhs;
   private final ToStringStyle style;

   DiffResult(Object var1, Object var2, List var3, ToStringStyle var4) {
      if (var1 == null) {
         throw new IllegalArgumentException("Left hand object cannot be null");
      } else if (var2 == null) {
         throw new IllegalArgumentException("Right hand object cannot be null");
      } else if (var3 == null) {
         throw new IllegalArgumentException("List of differences cannot be null");
      } else {
         this.diffs = var3;
         this.lhs = var1;
         this.rhs = var2;
         if (var4 == null) {
            this.style = ToStringStyle.DEFAULT_STYLE;
         } else {
            this.style = var4;
         }

      }
   }

   public List getDiffs() {
      return Collections.unmodifiableList(this.diffs);
   }

   public int getNumberOfDiffs() {
      return this.diffs.size();
   }

   public ToStringStyle getToStringStyle() {
      return this.style;
   }

   public String toString() {
      return this.toString(this.style);
   }

   public String toString(ToStringStyle var1) {
      if (this.diffs.size() == 0) {
         return "";
      } else {
         ToStringBuilder var2 = new ToStringBuilder(this.lhs, var1);
         ToStringBuilder var3 = new ToStringBuilder(this.rhs, var1);
         Iterator var4 = this.diffs.iterator();

         while(var4.hasNext()) {
            Diff var5 = (Diff)var4.next();
            var2.append(var5.getFieldName(), var5.getLeft());
            var3.append(var5.getFieldName(), var5.getRight());
         }

         return String.format("%s %s %s", var2.build(), "differs from", var3.build());
      }
   }

   public Iterator iterator() {
      return this.diffs.iterator();
   }
}
