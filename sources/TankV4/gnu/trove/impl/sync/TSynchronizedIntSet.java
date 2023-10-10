package gnu.trove.impl.sync;

import gnu.trove.set.TIntSet;

public class TSynchronizedIntSet extends TSynchronizedIntCollection implements TIntSet {
   private static final long serialVersionUID = 487447009682186044L;

   public TSynchronizedIntSet(TIntSet var1) {
      super(var1);
   }

   public TSynchronizedIntSet(TIntSet var1, Object var2) {
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
