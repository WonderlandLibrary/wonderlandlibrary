package gnu.trove.impl.sync;

import java.util.Set;

class SynchronizedSet extends SynchronizedCollection implements Set {
   private static final long serialVersionUID = 487447009682186044L;

   SynchronizedSet(Set var1, Object var2) {
      super(var1, var2);
   }

   public boolean equals(Object var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.equals(var1);
   }

   public int hashCode() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.c.hashCode();
   }
}
