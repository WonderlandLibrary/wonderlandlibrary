package gnu.trove.impl.sync;

import gnu.trove.set.TCharSet;

public class TSynchronizedCharSet extends TSynchronizedCharCollection implements TCharSet {
   private static final long serialVersionUID = 487447009682186044L;

   public TSynchronizedCharSet(TCharSet var1) {
      super(var1);
   }

   public TSynchronizedCharSet(TCharSet var1, Object var2) {
      super(var1, var2);
   }

   public boolean equals(Object var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.equals(var1);
   }

   public int hashCode() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.c.hashCode();
   }
}
