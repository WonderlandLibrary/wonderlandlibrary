package gnu.trove.strategy;

public class IdentityHashingStrategy implements HashingStrategy {
   static final long serialVersionUID = -5188534454583764904L;
   public static final IdentityHashingStrategy INSTANCE = new IdentityHashingStrategy();

   public int computeHashCode(Object var1) {
      return System.identityHashCode(var1);
   }

   public boolean equals(Object var1, Object var2) {
      return var1 == var2;
   }
}
