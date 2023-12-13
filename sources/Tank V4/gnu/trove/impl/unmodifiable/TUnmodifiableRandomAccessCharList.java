package gnu.trove.impl.unmodifiable;

import gnu.trove.list.TCharList;
import java.util.RandomAccess;

public class TUnmodifiableRandomAccessCharList extends TUnmodifiableCharList implements RandomAccess {
   private static final long serialVersionUID = -2542308836966382001L;

   public TUnmodifiableRandomAccessCharList(TCharList var1) {
      super(var1);
   }

   public TCharList subList(int var1, int var2) {
      return new TUnmodifiableRandomAccessCharList(this.list.subList(var1, var2));
   }

   private Object writeReplace() {
      return new TUnmodifiableCharList(this.list);
   }
}
