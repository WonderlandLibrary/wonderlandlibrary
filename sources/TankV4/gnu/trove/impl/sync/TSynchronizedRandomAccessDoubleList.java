package gnu.trove.impl.sync;

import gnu.trove.list.TDoubleList;
import java.util.RandomAccess;

public class TSynchronizedRandomAccessDoubleList extends TSynchronizedDoubleList implements RandomAccess {
   static final long serialVersionUID = 1530674583602358482L;

   public TSynchronizedRandomAccessDoubleList(TDoubleList var1) {
      super(var1);
   }

   public TSynchronizedRandomAccessDoubleList(TDoubleList var1, Object var2) {
      super(var1, var2);
   }

   public TDoubleList subList(int var1, int var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return new TSynchronizedRandomAccessDoubleList(this.list.subList(var1, var2), this.mutex);
   }

   private Object writeReplace() {
      return new TSynchronizedDoubleList(this.list);
   }
}
