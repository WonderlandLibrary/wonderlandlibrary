package org.lwjgl.util.glu.tessellation;

class Dict {
   DictNode head;
   Object frame;
   Dict.DictLeq leq;

   private Dict() {
   }

   static Dict dictNewDict(Object var0, Dict.DictLeq var1) {
      Dict var2 = new Dict();
      var2.head = new DictNode();
      var2.head.key = null;
      var2.head.next = var2.head;
      var2.head.prev = var2.head;
      var2.frame = var0;
      var2.leq = var1;
      return var2;
   }

   static void dictDeleteDict(Dict var0) {
      var0.head = null;
      var0.frame = null;
      var0.leq = null;
   }

   static DictNode dictInsert(Dict var0, Object var1) {
      return dictInsertBefore(var0, var0.head, var1);
   }

   static DictNode dictInsertBefore(Dict var0, DictNode var1, Object var2) {
      do {
         var1 = var1.prev;
      } while(var1.key != null && !var0.leq.leq(var0.frame, var1.key, var2));

      DictNode var3 = new DictNode();
      var3.key = var2;
      var3.next = var1.next;
      var1.next.prev = var3;
      var3.prev = var1;
      var1.next = var3;
      return var3;
   }

   static Object dictKey(DictNode var0) {
      return var0.key;
   }

   static DictNode dictSucc(DictNode var0) {
      return var0.next;
   }

   static DictNode dictPred(DictNode var0) {
      return var0.prev;
   }

   static DictNode dictMin(Dict var0) {
      return var0.head.next;
   }

   static DictNode dictMax(Dict var0) {
      return var0.head.prev;
   }

   static void dictDelete(Dict var0, DictNode var1) {
      var1.next.prev = var1.prev;
      var1.prev.next = var1.next;
   }

   static DictNode dictSearch(Dict var0, Object var1) {
      DictNode var2 = var0.head;

      do {
         var2 = var2.next;
      } while(var2.key != null && !var0.leq.leq(var0.frame, var1, var2.key));

      return var2;
   }

   public interface DictLeq {
      boolean leq(Object var1, Object var2, Object var3);
   }
}
