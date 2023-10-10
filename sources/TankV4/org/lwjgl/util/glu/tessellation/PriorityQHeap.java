package org.lwjgl.util.glu.tessellation;

class PriorityQHeap extends PriorityQ {
   PriorityQ.PQnode[] nodes = new PriorityQ.PQnode[33];
   PriorityQ.PQhandleElem[] handles;
   int size = 0;
   int max = 32;
   int freeList;
   boolean initialized;
   PriorityQ.Leq leq;
   static final boolean $assertionsDisabled = !PriorityQHeap.class.desiredAssertionStatus();

   PriorityQHeap(PriorityQ.Leq var1) {
      int var2;
      for(var2 = 0; var2 < this.nodes.length; ++var2) {
         this.nodes[var2] = new PriorityQ.PQnode();
      }

      this.handles = new PriorityQ.PQhandleElem[33];

      for(var2 = 0; var2 < this.handles.length; ++var2) {
         this.handles[var2] = new PriorityQ.PQhandleElem();
      }

      this.initialized = false;
      this.freeList = 0;
      this.leq = var1;
      this.nodes[1].handle = 1;
      this.handles[1].key = null;
   }

   void pqDeletePriorityQ() {
      this.handles = null;
      this.nodes = null;
   }

   void FloatDown(int var1) {
      PriorityQ.PQnode[] var2 = this.nodes;
      PriorityQ.PQhandleElem[] var3 = this.handles;
      int var4 = var2[var1].handle;

      while(true) {
         int var6 = var1 << 1;
         if (var6 < this.size && LEQ(this.leq, var3[var2[var6 + 1].handle].key, var3[var2[var6].handle].key)) {
            ++var6;
         }

         if (!$assertionsDisabled && var6 > this.max) {
            throw new AssertionError();
         }

         int var5 = var2[var6].handle;
         if (var6 > this.size || LEQ(this.leq, var3[var4].key, var3[var5].key)) {
            var2[var1].handle = var4;
            var3[var4].node = var1;
            return;
         }

         var2[var1].handle = var5;
         var3[var5].node = var1;
         var1 = var6;
      }
   }

   void FloatUp(int var1) {
      PriorityQ.PQnode[] var2 = this.nodes;
      PriorityQ.PQhandleElem[] var3 = this.handles;
      int var4 = var2[var1].handle;

      while(true) {
         int var6 = var1 >> 1;
         int var5 = var2[var6].handle;
         if (var6 == 0 || LEQ(this.leq, var3[var5].key, var3[var4].key)) {
            var2[var1].handle = var4;
            var3[var4].node = var1;
            return;
         }

         var2[var1].handle = var5;
         var3[var5].node = var1;
         var1 = var6;
      }
   }

   boolean pqInit() {
      for(int var1 = this.size; var1 >= 1; --var1) {
         this.FloatDown(var1);
      }

      this.initialized = true;
      return true;
   }

   int pqInsert(Object var1) {
      int var2 = ++this.size;
      if (var2 * 2 > this.max) {
         PriorityQ.PQnode[] var4 = this.nodes;
         PriorityQ.PQhandleElem[] var5 = this.handles;
         this.max <<= 1;
         PriorityQ.PQnode[] var6 = new PriorityQ.PQnode[this.max + 1];
         System.arraycopy(this.nodes, 0, var6, 0, this.nodes.length);

         for(int var7 = this.nodes.length; var7 < var6.length; ++var7) {
            var6[var7] = new PriorityQ.PQnode();
         }

         this.nodes = var6;
         if (this.nodes == null) {
            this.nodes = var4;
            return Integer.MAX_VALUE;
         }

         PriorityQ.PQhandleElem[] var9 = new PriorityQ.PQhandleElem[this.max + 1];
         System.arraycopy(this.handles, 0, var9, 0, this.handles.length);

         for(int var8 = this.handles.length; var8 < var9.length; ++var8) {
            var9[var8] = new PriorityQ.PQhandleElem();
         }

         this.handles = var9;
         if (this.handles == null) {
            this.handles = var5;
            return Integer.MAX_VALUE;
         }
      }

      int var3;
      if (this.freeList == 0) {
         var3 = var2;
      } else {
         var3 = this.freeList;
         this.freeList = this.handles[var3].node;
      }

      this.nodes[var2].handle = var3;
      this.handles[var3].node = var2;
      this.handles[var3].key = var1;
      if (this.initialized) {
         this.FloatUp(var2);
      }

      if (!$assertionsDisabled && var3 == Integer.MAX_VALUE) {
         throw new AssertionError();
      } else {
         return var3;
      }
   }

   Object pqExtractMin() {
      PriorityQ.PQnode[] var1 = this.nodes;
      PriorityQ.PQhandleElem[] var2 = this.handles;
      int var3 = var1[1].handle;
      Object var4 = var2[var3].key;
      if (this.size > 0) {
         var1[1].handle = var1[this.size].handle;
         var2[var1[1].handle].node = 1;
         var2[var3].key = null;
         var2[var3].node = this.freeList;
         this.freeList = var3;
         if (--this.size > 0) {
            this.FloatDown(1);
         }
      }

      return var4;
   }

   void pqDelete(int var1) {
      PriorityQ.PQnode[] var2 = this.nodes;
      PriorityQ.PQhandleElem[] var3 = this.handles;
      if (!$assertionsDisabled && (var1 < 1 || var1 > this.max || var3[var1].key == null)) {
         throw new AssertionError();
      } else {
         int var4 = var3[var1].node;
         var2[var4].handle = var2[this.size].handle;
         var3[var2[var4].handle].node = var4;
         if (var4 <= --this.size) {
            if (var4 > 1 && !LEQ(this.leq, var3[var2[var4 >> 1].handle].key, var3[var2[var4].handle].key)) {
               this.FloatUp(var4);
            } else {
               this.FloatDown(var4);
            }
         }

         var3[var1].key = null;
         var3[var1].node = this.freeList;
         this.freeList = var1;
      }
   }

   Object pqMinimum() {
      return this.handles[this.nodes[1].handle].key;
   }

   boolean pqIsEmpty() {
      return this.size == 0;
   }
}
