package gnu.trove.impl.hash;

import gnu.trove.strategy.HashingStrategy;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public abstract class TCustomObjectHash extends TObjectHash {
   static final long serialVersionUID = 8766048185963756400L;
   protected HashingStrategy strategy;

   public TCustomObjectHash() {
   }

   public TCustomObjectHash(HashingStrategy var1) {
      this.strategy = var1;
   }

   public TCustomObjectHash(HashingStrategy var1, int var2) {
      super(var2);
      this.strategy = var1;
   }

   public TCustomObjectHash(HashingStrategy var1, int var2, float var3) {
      super(var2, var3);
      this.strategy = var1;
   }

   protected int hash(Object var1) {
      return this.strategy.computeHashCode(var1);
   }

   protected boolean equals(Object var1, Object var2) {
      return var2 != REMOVED && this.strategy.equals(var1, var2);
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeByte(0);
      super.writeExternal(var1);
      var1.writeObject(this.strategy);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      var1.readByte();
      super.readExternal(var1);
      this.strategy = (HashingStrategy)var1.readObject();
   }
}
