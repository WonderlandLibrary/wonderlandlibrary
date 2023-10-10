package gnu.trove.impl.sync;

import gnu.trove.list.TCharList;
import java.util.RandomAccess;

public class TSynchronizedRandomAccessCharList extends TSynchronizedCharList implements RandomAccess {
   static final long serialVersionUID = 1530674583602358482L;

   public TSynchronizedRandomAccessCharList(TCharList var1) {
      super(var1);
   }

   public TSynchronizedRandomAccessCharList(TCharList var1, Object var2) {
      super(var1, var2);
   }

   public TCharList subList(int var1, int var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return new TSynchronizedRandomAccessCharList(this.list.subList(var1, var2), this.mutex);
   }

   private Object writeReplace() {
      return new TSynchronizedCharList(this.list);
   }
}
