package gnu.trove.impl.unmodifiable;

import gnu.trove.set.TCharSet;
import java.io.Serializable;

public class TUnmodifiableCharSet extends TUnmodifiableCharCollection implements TCharSet, Serializable {
   private static final long serialVersionUID = -9215047833775013803L;

   public TUnmodifiableCharSet(TCharSet var1) {
      super(var1);
   }

   public boolean equals(Object var1) {
      return var1 == this || this.c.equals(var1);
   }

   public int hashCode() {
      return this.c.hashCode();
   }
}
