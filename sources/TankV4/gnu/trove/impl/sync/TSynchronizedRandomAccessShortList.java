package gnu.trove.impl.sync;

import gnu.trove.list.TShortList;
import java.util.RandomAccess;

public class TSynchronizedRandomAccessShortList extends TSynchronizedShortList implements RandomAccess {
   static final long serialVersionUID = 1530674583602358482L;

   public TSynchronizedRandomAccessShortList(TShortList var1) {
      super(var1);
   }

   public TSynchronizedRandomAccessShortList(TShortList var1, Object var2) {
      super(var1, var2);
   }

   public TShortList subList(int var1, int var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return new TSynchronizedRandomAccessShortList(this.list.subList(var1, var2), this.mutex);
   }

   private Object writeReplace() {
      return new TSynchronizedShortList(this.list);
   }
}
