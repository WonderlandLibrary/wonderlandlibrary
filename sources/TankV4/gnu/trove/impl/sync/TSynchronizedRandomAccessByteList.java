package gnu.trove.impl.sync;

import gnu.trove.list.TByteList;
import java.util.RandomAccess;

public class TSynchronizedRandomAccessByteList extends TSynchronizedByteList implements RandomAccess {
   static final long serialVersionUID = 1530674583602358482L;

   public TSynchronizedRandomAccessByteList(TByteList var1) {
      super(var1);
   }

   public TSynchronizedRandomAccessByteList(TByteList var1, Object var2) {
      super(var1, var2);
   }

   public TByteList subList(int var1, int var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return new TSynchronizedRandomAccessByteList(this.list.subList(var1, var2), this.mutex);
   }

   private Object writeReplace() {
      return new TSynchronizedByteList(this.list);
   }
}
