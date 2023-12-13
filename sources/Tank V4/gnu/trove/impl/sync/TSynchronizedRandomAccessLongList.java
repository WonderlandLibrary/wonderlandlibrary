package gnu.trove.impl.sync;

import gnu.trove.list.TLongList;
import java.util.RandomAccess;

public class TSynchronizedRandomAccessLongList extends TSynchronizedLongList implements RandomAccess {
   static final long serialVersionUID = 1530674583602358482L;

   public TSynchronizedRandomAccessLongList(TLongList var1) {
      super(var1);
   }

   public TSynchronizedRandomAccessLongList(TLongList var1, Object var2) {
      super(var1, var2);
   }

   public TLongList subList(int var1, int var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return new TSynchronizedRandomAccessLongList(this.list.subList(var1, var2), this.mutex);
   }

   private Object writeReplace() {
      return new TSynchronizedLongList(this.list);
   }
}
