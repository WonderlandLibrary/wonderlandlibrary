package gnu.trove.impl.unmodifiable;

import gnu.trove.list.TShortList;
import java.util.RandomAccess;

public class TUnmodifiableRandomAccessShortList extends TUnmodifiableShortList implements RandomAccess {
   private static final long serialVersionUID = -2542308836966382001L;

   public TUnmodifiableRandomAccessShortList(TShortList var1) {
      super(var1);
   }

   public TShortList subList(int var1, int var2) {
      return new TUnmodifiableRandomAccessShortList(this.list.subList(var1, var2));
   }

   private Object writeReplace() {
      return new TUnmodifiableShortList(this.list);
   }
}
