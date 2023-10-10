package gnu.trove.impl.sync;

import gnu.trove.list.TIntList;
import java.util.RandomAccess;

public class TSynchronizedRandomAccessIntList extends TSynchronizedIntList implements RandomAccess {
   static final long serialVersionUID = 1530674583602358482L;

   public TSynchronizedRandomAccessIntList(TIntList var1) {
      super(var1);
   }

   public TSynchronizedRandomAccessIntList(TIntList var1, Object var2) {
      super(var1, var2);
   }

   public TIntList subList(int var1, int var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return new TSynchronizedRandomAccessIntList(this.list.subList(var1, var2), this.mutex);
   }

   private Object writeReplace() {
      return new TSynchronizedIntList(this.list);
   }
}
