package org.lwjgl.opengl;

import java.nio.IntBuffer;

final class StateTracker {
   private ReferencesStack references_stack;
   private final StateStack attrib_stack = new StateStack(0);
   private boolean insideBeginEnd;
   private final FastIntMap vaoMap = new FastIntMap();

   void init() {
      this.references_stack = new ReferencesStack();
   }

   static void setBeginEnd(ContextCapabilities var0, boolean var1) {
      var0.tracker.insideBeginEnd = var1;
   }

   boolean isBeginEnd() {
      return this.insideBeginEnd;
   }

   static void popAttrib(ContextCapabilities var0) {
      var0.tracker.doPopAttrib();
   }

   private void doPopAttrib() {
      this.references_stack.popState(this.attrib_stack.popState());
   }

   static void pushAttrib(ContextCapabilities var0, int var1) {
      var0.tracker.doPushAttrib(var1);
   }

   private void doPushAttrib(int var1) {
      this.attrib_stack.pushState(var1);
      this.references_stack.pushState();
   }

   static References getReferences(ContextCapabilities var0) {
      return var0.tracker.references_stack.getReferences();
   }

   static void bindBuffer(ContextCapabilities var0, int var1, int var2) {
      References var3 = getReferences(var0);
      switch(var1) {
      case 34962:
         var3.arrayBuffer = var2;
         break;
      case 34963:
         if (var3.vertexArrayObject != 0) {
            ((StateTracker.VAOState)var0.tracker.vaoMap.get(var3.vertexArrayObject)).elementArrayBuffer = var2;
         } else {
            var3.elementArrayBuffer = var2;
         }
         break;
      case 35051:
         var3.pixelPackBuffer = var2;
         break;
      case 35052:
         var3.pixelUnpackBuffer = var2;
         break;
      case 36671:
         var3.indirectBuffer = var2;
      }

   }

   static void bindVAO(ContextCapabilities var0, int var1) {
      FastIntMap var2 = var0.tracker.vaoMap;
      if (!var2.containsKey(var1)) {
         var2.put(var1, new StateTracker.VAOState());
      }

      getReferences(var0).vertexArrayObject = var1;
   }

   static void deleteVAO(ContextCapabilities var0, IntBuffer var1) {
      for(int var2 = var1.position(); var2 < var1.limit(); ++var2) {
         deleteVAO(var0, var1.get(var2));
      }

   }

   static void deleteVAO(ContextCapabilities var0, int var1) {
      var0.tracker.vaoMap.remove(var1);
      References var2 = getReferences(var0);
      if (var2.vertexArrayObject == var1) {
         var2.vertexArrayObject = 0;
      }

   }

   static int getElementArrayBufferBound(ContextCapabilities var0) {
      References var1 = getReferences(var0);
      return var1.vertexArrayObject == 0 ? var1.elementArrayBuffer : ((StateTracker.VAOState)var0.tracker.vaoMap.get(var1.vertexArrayObject)).elementArrayBuffer;
   }

   private static class VAOState {
      int elementArrayBuffer;

      private VAOState() {
      }

      VAOState(Object var1) {
         this();
      }
   }
}
