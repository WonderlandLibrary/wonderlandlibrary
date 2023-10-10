package org.apache.commons.lang3.builder;

import java.util.Collection;
import org.apache.commons.lang3.ClassUtils;

public class RecursiveToStringStyle extends ToStringStyle {
   private static final long serialVersionUID = 1L;

   public void appendDetail(StringBuffer var1, String var2, Object var3) {
      if (!ClassUtils.isPrimitiveWrapper(var3.getClass()) && !String.class.equals(var3.getClass()) && this.accept(var3.getClass())) {
         var1.append(ReflectionToStringBuilder.toString(var3, this));
      } else {
         super.appendDetail(var1, var2, var3);
      }

   }

   protected void appendDetail(StringBuffer var1, String var2, Collection var3) {
      this.appendClassName(var1, var3);
      this.appendIdentityHashCode(var1, var3);
      this.appendDetail(var1, var2, (Object[])var3.toArray());
   }

   protected boolean accept(Class var1) {
      return true;
   }
}
