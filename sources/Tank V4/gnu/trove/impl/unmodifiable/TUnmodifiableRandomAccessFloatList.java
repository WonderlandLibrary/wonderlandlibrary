package gnu.trove.impl.unmodifiable;

import gnu.trove.list.TFloatList;
import java.util.RandomAccess;

public class TUnmodifiableRandomAccessFloatList extends TUnmodifiableFloatList implements RandomAccess {
   private static final long serialVersionUID = -2542308836966382001L;

   public TUnmodifiableRandomAccessFloatList(TFloatList var1) {
      super(var1);
   }

   public TFloatList subList(int var1, int var2) {
      return new TUnmodifiableRandomAccessFloatList(this.list.subList(var1, var2));
   }

   private Object writeReplace() {
      return new TUnmodifiableFloatList(this.list);
   }
}
