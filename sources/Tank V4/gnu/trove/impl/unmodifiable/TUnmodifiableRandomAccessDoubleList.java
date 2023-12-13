package gnu.trove.impl.unmodifiable;

import gnu.trove.list.TDoubleList;
import java.util.RandomAccess;

public class TUnmodifiableRandomAccessDoubleList extends TUnmodifiableDoubleList implements RandomAccess {
   private static final long serialVersionUID = -2542308836966382001L;

   public TUnmodifiableRandomAccessDoubleList(TDoubleList var1) {
      super(var1);
   }

   public TDoubleList subList(int var1, int var2) {
      return new TUnmodifiableRandomAccessDoubleList(this.list.subList(var1, var2));
   }

   private Object writeReplace() {
      return new TUnmodifiableDoubleList(this.list);
   }
}
