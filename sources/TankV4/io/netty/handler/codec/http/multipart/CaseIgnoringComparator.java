package io.netty.handler.codec.http.multipart;

import java.io.Serializable;
import java.util.Comparator;

final class CaseIgnoringComparator implements Comparator, Serializable {
   private static final long serialVersionUID = 4582133183775373862L;
   static final CaseIgnoringComparator INSTANCE = new CaseIgnoringComparator();

   private CaseIgnoringComparator() {
   }

   public int compare(String var1, String var2) {
      return var1.compareToIgnoreCase(var2);
   }

   private Object readResolve() {
      return INSTANCE;
   }

   public int compare(Object var1, Object var2) {
      return this.compare((String)var1, (String)var2);
   }
}
