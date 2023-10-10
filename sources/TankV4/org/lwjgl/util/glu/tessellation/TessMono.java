package org.lwjgl.util.glu.tessellation;

class TessMono {
   static final boolean $assertionsDisabled = !TessMono.class.desiredAssertionStatus();

   public static boolean __gl_meshTessellateInterior(GLUmesh var0) {
      GLUface var2;
      for(GLUface var1 = var0.fHead.next; var1 != var0.fHead; var1 = var2) {
         var2 = var1.next;
         if (var1.inside && var1 == false) {
            return false;
         }
      }

      return true;
   }

   public static void __gl_meshDiscardExterior(GLUmesh var0) {
      GLUface var2;
      for(GLUface var1 = var0.fHead.next; var1 != var0.fHead; var1 = var2) {
         var2 = var1.next;
         if (!var1.inside) {
            Mesh.__gl_meshZapFace(var1);
         }
      }

   }

   public static boolean __gl_meshSetWindingNumber(GLUmesh var0, int var1, boolean var2) {
      GLUhalfEdge var4;
      for(GLUhalfEdge var3 = var0.eHead.next; var3 != var0.eHead; var3 = var4) {
         var4 = var3.next;
         if (var3.Sym.Lface.inside != var3.Lface.inside) {
            var3.winding = var3.Lface.inside ? var1 : -var1;
         } else if (!var2) {
            var3.winding = 0;
         } else if (!Mesh.__gl_meshDelete(var3)) {
            return false;
         }
      }

      return true;
   }
}
