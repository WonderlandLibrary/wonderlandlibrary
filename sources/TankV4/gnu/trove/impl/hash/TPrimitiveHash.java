package gnu.trove.impl.hash;

import gnu.trove.impl.HashFunctions;

public abstract class TPrimitiveHash extends THash {
   static final long serialVersionUID = 1L;
   public transient byte[] _states;
   public static final byte FREE = 0;
   public static final byte FULL = 1;
   public static final byte REMOVED = 2;

   public TPrimitiveHash() {
   }

   public TPrimitiveHash(int var1) {
      this(var1, 0.5F);
   }

   public TPrimitiveHash(int var1, float var2) {
      var1 = Math.max(1, var1);
      this._loadFactor = var2;
      this.setUp(HashFunctions.fastCeil((float)var1 / var2));
   }

   public int capacity() {
      return this._states.length;
   }

   protected void removeAt(int var1) {
      this._states[var1] = 2;
      super.removeAt(var1);
   }

   protected int setUp(int var1) {
      int var2 = super.setUp(var1);
      this._states = new byte[var2];
      return var2;
   }
}
