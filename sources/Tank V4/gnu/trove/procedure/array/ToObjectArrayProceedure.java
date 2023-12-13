package gnu.trove.procedure.array;

import gnu.trove.procedure.TObjectProcedure;

public final class ToObjectArrayProceedure implements TObjectProcedure {
   private final Object[] target;
   private int pos = 0;

   public ToObjectArrayProceedure(Object[] var1) {
      this.target = var1;
   }

   public final boolean execute(Object var1) {
      this.target[this.pos++] = var1;
      return true;
   }
}
