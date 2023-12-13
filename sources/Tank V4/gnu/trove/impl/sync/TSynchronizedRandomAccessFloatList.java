package gnu.trove.impl.sync;

import gnu.trove.list.TFloatList;
import java.util.RandomAccess;

public class TSynchronizedRandomAccessFloatList extends TSynchronizedFloatList implements RandomAccess {
   static final long serialVersionUID = 1530674583602358482L;

   public TSynchronizedRandomAccessFloatList(TFloatList var1) {
      super(var1);
   }

   public TSynchronizedRandomAccessFloatList(TFloatList var1, Object var2) {
      super(var1, var2);
   }

   public TFloatList subList(int var1, int var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return new TSynchronizedRandomAccessFloatList(this.list.subList(var1, var2), this.mutex);
   }

   private Object writeReplace() {
      return new TSynchronizedFloatList(this.list);
   }
}
