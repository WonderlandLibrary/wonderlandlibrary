package gnu.trove.impl.unmodifiable;

import gnu.trove.list.TLongList;
import java.util.RandomAccess;

public class TUnmodifiableRandomAccessLongList extends TUnmodifiableLongList implements RandomAccess {
   private static final long serialVersionUID = -2542308836966382001L;

   public TUnmodifiableRandomAccessLongList(TLongList var1) {
      super(var1);
   }

   public TLongList subList(int var1, int var2) {
      return new TUnmodifiableRandomAccessLongList(this.list.subList(var1, var2));
   }

   private Object writeReplace() {
      return new TUnmodifiableLongList(this.list);
   }
}
