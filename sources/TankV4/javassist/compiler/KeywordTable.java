package javassist.compiler;

import java.util.HashMap;

public final class KeywordTable extends HashMap {
   public int lookup(String var1) {
      Object var2 = this.get(var1);
      return var2 == null ? -1 : (Integer)var2;
   }

   public void append(String var1, int var2) {
      this.put(var1, new Integer(var2));
   }
}
