package org.lwjgl.opencl;

import org.lwjgl.LWJGLUtil;

class CLObjectRegistry {
   private FastLongMap registry;

   final boolean isEmpty() {
      return this.registry == null || this.registry.isEmpty();
   }

   final CLObjectChild getObject(long var1) {
      return this.registry == null ? null : (CLObjectChild)this.registry.get(var1);
   }

   final boolean hasObject(long var1) {
      return this.registry != null && this.registry.containsKey(var1);
   }

   final Iterable getAll() {
      return this.registry;
   }

   void registerObject(CLObjectChild var1) {
      FastLongMap var2 = this.getMap();
      Long var3 = var1.getPointer();
      if (LWJGLUtil.DEBUG && var2.containsKey(var3)) {
         throw new IllegalStateException("Duplicate object found: " + var1.getClass() + " - " + var3);
      } else {
         this.getMap().put(var1.getPointer(), var1);
      }
   }

   void unregisterObject(CLObjectChild var1) {
      this.getMap().remove(var1.getPointerUnsafe());
   }

   private FastLongMap getMap() {
      if (this.registry == null) {
         this.registry = new FastLongMap();
      }

      return this.registry;
   }
}
