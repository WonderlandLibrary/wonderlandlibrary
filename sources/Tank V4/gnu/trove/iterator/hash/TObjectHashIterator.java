package gnu.trove.iterator.hash;

import gnu.trove.impl.hash.THashIterator;
import gnu.trove.impl.hash.TObjectHash;

public class TObjectHashIterator extends THashIterator {
   protected final TObjectHash _objectHash;

   public TObjectHashIterator(TObjectHash var1) {
      super(var1);
      this._objectHash = var1;
   }

   protected Object objectAtIndex(int var1) {
      Object var2 = this._objectHash._set[var1];
      return var2 != TObjectHash.FREE && var2 != TObjectHash.REMOVED ? var2 : null;
   }
}
