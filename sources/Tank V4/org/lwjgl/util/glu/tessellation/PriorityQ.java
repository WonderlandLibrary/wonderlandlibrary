package org.lwjgl.util.glu.tessellation;

abstract class PriorityQ {
   public static final int INIT_SIZE = 32;

   public static boolean LEQ(PriorityQ.Leq var0, Object var1, Object var2) {
      return Geom.VertLeq((GLUvertex)var1, (GLUvertex)var2);
   }

   static PriorityQ pqNewPriorityQ(PriorityQ.Leq var0) {
      return new PriorityQSort(var0);
   }

   abstract void pqDeletePriorityQ();

   abstract boolean pqInit();

   abstract int pqInsert(Object var1);

   abstract Object pqExtractMin();

   abstract void pqDelete(int var1);

   abstract Object pqMinimum();

   abstract boolean pqIsEmpty();

   public interface Leq {
      boolean leq(Object var1, Object var2);
   }

   public static class PQhandleElem {
      Object key;
      int node;
   }

   public static class PQnode {
      int handle;
   }
}
