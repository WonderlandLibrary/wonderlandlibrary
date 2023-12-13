package gnu.trove.impl.unmodifiable;

import gnu.trove.set.TIntSet;
import java.io.Serializable;

public class TUnmodifiableIntSet extends TUnmodifiableIntCollection implements TIntSet, Serializable {
   private static final long serialVersionUID = -9215047833775013803L;

   public TUnmodifiableIntSet(TIntSet var1) {
      super(var1);
   }

   public boolean equals(Object var1) {
      return var1 == this || this.c.equals(var1);
   }

   public int hashCode() {
      return this.c.hashCode();
   }
}
