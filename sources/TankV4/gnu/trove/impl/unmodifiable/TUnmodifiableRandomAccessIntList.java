package gnu.trove.impl.unmodifiable;

import gnu.trove.list.TIntList;
import java.util.RandomAccess;

public class TUnmodifiableRandomAccessIntList extends TUnmodifiableIntList implements RandomAccess {
   private static final long serialVersionUID = -2542308836966382001L;

   public TUnmodifiableRandomAccessIntList(TIntList var1) {
      super(var1);
   }

   public TIntList subList(int var1, int var2) {
      return new TUnmodifiableRandomAccessIntList(this.list.subList(var1, var2));
   }

   private Object writeReplace() {
      return new TUnmodifiableIntList(this.list);
   }
}
