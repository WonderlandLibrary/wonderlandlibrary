package org.lwjgl.util.glu.tessellation;

class Mesh {
   static final boolean $assertionsDisabled = !Mesh.class.desiredAssertionStatus();

   private Mesh() {
   }

   static GLUhalfEdge MakeEdge(GLUhalfEdge var0) {
      GLUhalfEdge var1 = new GLUhalfEdge(true);
      GLUhalfEdge var2 = new GLUhalfEdge(false);
      if (!var0.first) {
         var0 = var0.Sym;
      }

      GLUhalfEdge var3 = var0.Sym.next;
      var2.next = var3;
      var3.Sym.next = var1;
      var1.next = var0;
      var0.Sym.next = var2;
      var1.Sym = var2;
      var1.Onext = var1;
      var1.Lnext = var2;
      var1.Org = null;
      var1.Lface = null;
      var1.winding = 0;
      var1.activeRegion = null;
      var2.Sym = var1;
      var2.Onext = var2;
      var2.Lnext = var1;
      var2.Org = null;
      var2.Lface = null;
      var2.winding = 0;
      var2.activeRegion = null;
      return var1;
   }

   static void Splice(GLUhalfEdge var0, GLUhalfEdge var1) {
      GLUhalfEdge var2 = var0.Onext;
      GLUhalfEdge var3 = var1.Onext;
      var2.Sym.Lnext = var1;
      var3.Sym.Lnext = var0;
      var0.Onext = var3;
      var1.Onext = var2;
   }

   static void MakeVertex(GLUvertex var0, GLUhalfEdge var1, GLUvertex var2) {
      GLUvertex var5 = var0;
      if (!$assertionsDisabled && var0 == null) {
         throw new AssertionError();
      } else {
         GLUvertex var4 = var2.prev;
         var0.prev = var4;
         var4.next = var0;
         var0.next = var2;
         var2.prev = var0;
         var0.anEdge = var1;
         var0.data = null;
         GLUhalfEdge var3 = var1;

         do {
            var3.Org = var5;
            var3 = var3.Onext;
         } while(var3 != var1);

      }
   }

   static void MakeFace(GLUface var0, GLUhalfEdge var1, GLUface var2) {
      GLUface var5 = var0;
      if (!$assertionsDisabled && var0 == null) {
         throw new AssertionError();
      } else {
         GLUface var4 = var2.prev;
         var0.prev = var4;
         var4.next = var0;
         var0.next = var2;
         var2.prev = var0;
         var0.anEdge = var1;
         var0.data = null;
         var0.trail = null;
         var0.marked = false;
         var0.inside = var2.inside;
         GLUhalfEdge var3 = var1;

         do {
            var3.Lface = var5;
            var3 = var3.Lnext;
         } while(var3 != var1);

      }
   }

   static void KillEdge(GLUhalfEdge var0) {
      if (!var0.first) {
         var0 = var0.Sym;
      }

      GLUhalfEdge var2 = var0.next;
      GLUhalfEdge var1 = var0.Sym.next;
      var2.Sym.next = var1;
      var1.Sym.next = var2;
   }

   static void KillVertex(GLUvertex var0, GLUvertex var1) {
      GLUhalfEdge var3 = var0.anEdge;
      GLUhalfEdge var2 = var3;

      do {
         var2.Org = var1;
         var2 = var2.Onext;
      } while(var2 != var3);

      GLUvertex var4 = var0.prev;
      GLUvertex var5 = var0.next;
      var5.prev = var4;
      var4.next = var5;
   }

   static void KillFace(GLUface var0, GLUface var1) {
      GLUhalfEdge var3 = var0.anEdge;
      GLUhalfEdge var2 = var3;

      do {
         var2.Lface = var1;
         var2 = var2.Lnext;
      } while(var2 != var3);

      GLUface var4 = var0.prev;
      GLUface var5 = var0.next;
      var5.prev = var4;
      var4.next = var5;
   }

   public static GLUhalfEdge __gl_meshMakeEdge(GLUmesh var0) {
      GLUvertex var1 = new GLUvertex();
      GLUvertex var2 = new GLUvertex();
      GLUface var3 = new GLUface();
      GLUhalfEdge var4 = MakeEdge(var0.eHead);
      if (var4 == null) {
         return null;
      } else {
         MakeVertex(var1, var4, var0.vHead);
         MakeVertex(var2, var4.Sym, var0.vHead);
         MakeFace(var3, var4, var0.fHead);
         return var4;
      }
   }

   public static boolean __gl_meshSplice(GLUhalfEdge var0, GLUhalfEdge var1) {
      boolean var2 = false;
      boolean var3 = false;
      if (var0 == var1) {
         return true;
      } else {
         if (var1.Org != var0.Org) {
            var3 = true;
            KillVertex(var1.Org, var0.Org);
         }

         if (var1.Lface != var0.Lface) {
            var2 = true;
            KillFace(var1.Lface, var0.Lface);
         }

         Splice(var1, var0);
         if (!var3) {
            GLUvertex var4 = new GLUvertex();
            MakeVertex(var4, var1, var0.Org);
            var0.Org.anEdge = var0;
         }

         if (!var2) {
            GLUface var5 = new GLUface();
            MakeFace(var5, var1, var0.Lface);
            var0.Lface.anEdge = var0;
         }

         return true;
      }
   }

   static boolean __gl_meshDelete(GLUhalfEdge var0) {
      GLUhalfEdge var1 = var0.Sym;
      boolean var2 = false;
      if (var0.Lface != var0.Sym.Lface) {
         var2 = true;
         KillFace(var0.Lface, var0.Sym.Lface);
      }

      if (var0.Onext == var0) {
         KillVertex(var0.Org, (GLUvertex)null);
      } else {
         var0.Sym.Lface.anEdge = var0.Sym.Lnext;
         var0.Org.anEdge = var0.Onext;
         Splice(var0, var0.Sym.Lnext);
         if (!var2) {
            GLUface var3 = new GLUface();
            MakeFace(var3, var0, var0.Lface);
         }
      }

      if (var1.Onext == var1) {
         KillVertex(var1.Org, (GLUvertex)null);
         KillFace(var1.Lface, (GLUface)null);
      } else {
         var0.Lface.anEdge = var1.Sym.Lnext;
         var1.Org.anEdge = var1.Onext;
         Splice(var1, var1.Sym.Lnext);
      }

      KillEdge(var0);
      return true;
   }

   static GLUhalfEdge __gl_meshAddEdgeVertex(GLUhalfEdge var0) {
      GLUhalfEdge var2 = MakeEdge(var0);
      GLUhalfEdge var1 = var2.Sym;
      Splice(var2, var0.Lnext);
      var2.Org = var0.Sym.Org;
      GLUvertex var3 = new GLUvertex();
      MakeVertex(var3, var1, var2.Org);
      var2.Lface = var1.Lface = var0.Lface;
      return var2;
   }

   public static GLUhalfEdge __gl_meshSplitEdge(GLUhalfEdge var0) {
      GLUhalfEdge var2 = __gl_meshAddEdgeVertex(var0);
      GLUhalfEdge var1 = var2.Sym;
      Splice(var0.Sym, var0.Sym.Sym.Lnext);
      Splice(var0.Sym, var1);
      var0.Sym.Org = var1.Org;
      var1.Sym.Org.anEdge = var1.Sym;
      var1.Sym.Lface = var0.Sym.Lface;
      var1.winding = var0.winding;
      var1.Sym.winding = var0.Sym.winding;
      return var1;
   }

   static GLUhalfEdge __gl_meshConnect(GLUhalfEdge var0, GLUhalfEdge var1) {
      boolean var3 = false;
      GLUhalfEdge var4 = MakeEdge(var0);
      GLUhalfEdge var2 = var4.Sym;
      if (var1.Lface != var0.Lface) {
         var3 = true;
         KillFace(var1.Lface, var0.Lface);
      }

      Splice(var4, var0.Lnext);
      Splice(var2, var1);
      var4.Org = var0.Sym.Org;
      var2.Org = var1.Org;
      var4.Lface = var2.Lface = var0.Lface;
      var0.Lface.anEdge = var2;
      if (!var3) {
         GLUface var5 = new GLUface();
         MakeFace(var5, var4, var0.Lface);
      }

      return var4;
   }

   static void __gl_meshZapFace(GLUface var0) {
      GLUhalfEdge var1 = var0.anEdge;
      GLUhalfEdge var3 = var1.Lnext;

      GLUhalfEdge var2;
      do {
         var2 = var3;
         var3 = var3.Lnext;
         var2.Lface = null;
         if (var2.Sym.Lface == null) {
            if (var2.Onext == var2) {
               KillVertex(var2.Org, (GLUvertex)null);
            } else {
               var2.Org.anEdge = var2.Onext;
               Splice(var2, var2.Sym.Lnext);
            }

            GLUhalfEdge var4 = var2.Sym;
            if (var4.Onext == var4) {
               KillVertex(var4.Org, (GLUvertex)null);
            } else {
               var4.Org.anEdge = var4.Onext;
               Splice(var4, var4.Sym.Lnext);
            }

            KillEdge(var2);
         }
      } while(var2 != var1);

      GLUface var5 = var0.prev;
      GLUface var6 = var0.next;
      var6.prev = var5;
      var5.next = var6;
   }

   public static GLUmesh __gl_meshNewMesh() {
      GLUmesh var4 = new GLUmesh();
      GLUvertex var0 = var4.vHead;
      GLUface var1 = var4.fHead;
      GLUhalfEdge var2 = var4.eHead;
      GLUhalfEdge var3 = var4.eHeadSym;
      var0.next = var0.prev = var0;
      var0.anEdge = null;
      var0.data = null;
      var1.next = var1.prev = var1;
      var1.anEdge = null;
      var1.data = null;
      var1.trail = null;
      var1.marked = false;
      var1.inside = false;
      var2.next = var2;
      var2.Sym = var3;
      var2.Onext = null;
      var2.Lnext = null;
      var2.Org = null;
      var2.Lface = null;
      var2.winding = 0;
      var2.activeRegion = null;
      var3.next = var3;
      var3.Sym = var2;
      var3.Onext = null;
      var3.Lnext = null;
      var3.Org = null;
      var3.Lface = null;
      var3.winding = 0;
      var3.activeRegion = null;
      return var4;
   }

   static GLUmesh __gl_meshUnion(GLUmesh var0, GLUmesh var1) {
      GLUface var2 = var0.fHead;
      GLUvertex var3 = var0.vHead;
      GLUhalfEdge var4 = var0.eHead;
      GLUface var5 = var1.fHead;
      GLUvertex var6 = var1.vHead;
      GLUhalfEdge var7 = var1.eHead;
      if (var5.next != var5) {
         var2.prev.next = var5.next;
         var5.next.prev = var2.prev;
         var5.prev.next = var2;
         var2.prev = var5.prev;
      }

      if (var6.next != var6) {
         var3.prev.next = var6.next;
         var6.next.prev = var3.prev;
         var6.prev.next = var3;
         var3.prev = var6.prev;
      }

      if (var7.next != var7) {
         var4.Sym.next.Sym.next = var7.next;
         var7.next.Sym.next = var4.Sym.next;
         var7.Sym.next.Sym.next = var4;
         var4.Sym.next = var7.Sym.next;
      }

      return var0;
   }

   static void __gl_meshDeleteMeshZap(GLUmesh var0) {
      GLUface var1 = var0.fHead;

      while(var1.next != var1) {
         __gl_meshZapFace(var1.next);
      }

      if (!$assertionsDisabled && var0.vHead.next != var0.vHead) {
         throw new AssertionError();
      }
   }

   public static void __gl_meshDeleteMesh(GLUmesh var0) {
      GLUface var2;
      for(GLUface var1 = var0.fHead.next; var1 != var0.fHead; var1 = var2) {
         var2 = var1.next;
      }

      GLUvertex var4;
      for(GLUvertex var3 = var0.vHead.next; var3 != var0.vHead; var3 = var4) {
         var4 = var3.next;
      }

      GLUhalfEdge var6;
      for(GLUhalfEdge var5 = var0.eHead.next; var5 != var0.eHead; var5 = var6) {
         var6 = var5.next;
      }

   }

   public static void __gl_meshCheckMesh(GLUmesh var0) {
      GLUface var1 = var0.fHead;
      GLUvertex var2 = var0.vHead;
      GLUhalfEdge var3 = var0.eHead;

      GLUface var4;
      GLUface var5;
      GLUhalfEdge var8;
      for(var5 = var1; (var4 = var5.next) != var1; var5 = var4) {
         if (!$assertionsDisabled && var4.prev != var5) {
            throw new AssertionError();
         }

         var8 = var4.anEdge;

         do {
            if (!$assertionsDisabled && var8.Sym == var8) {
               throw new AssertionError();
            }

            if (!$assertionsDisabled && var8.Sym.Sym != var8) {
               throw new AssertionError();
            }

            if (!$assertionsDisabled && var8.Lnext.Onext.Sym != var8) {
               throw new AssertionError();
            }

            if (!$assertionsDisabled && var8.Onext.Sym.Lnext != var8) {
               throw new AssertionError();
            }

            if (!$assertionsDisabled && var8.Lface != var4) {
               throw new AssertionError();
            }

            var8 = var8.Lnext;
         } while(var8 != var4.anEdge);
      }

      if (!$assertionsDisabled && (var4.prev != var5 || var4.anEdge != null || var4.data != null)) {
         throw new AssertionError();
      } else {
         GLUvertex var6;
         GLUvertex var7;
         for(var7 = var2; (var6 = var7.next) != var2; var7 = var6) {
            if (!$assertionsDisabled && var6.prev != var7) {
               throw new AssertionError();
            }

            var8 = var6.anEdge;

            do {
               if (!$assertionsDisabled && var8.Sym == var8) {
                  throw new AssertionError();
               }

               if (!$assertionsDisabled && var8.Sym.Sym != var8) {
                  throw new AssertionError();
               }

               if (!$assertionsDisabled && var8.Lnext.Onext.Sym != var8) {
                  throw new AssertionError();
               }

               if (!$assertionsDisabled && var8.Onext.Sym.Lnext != var8) {
                  throw new AssertionError();
               }

               if (!$assertionsDisabled && var8.Org != var6) {
                  throw new AssertionError();
               }

               var8 = var8.Onext;
            } while(var8 != var6.anEdge);
         }

         if (!$assertionsDisabled && (var6.prev != var7 || var6.anEdge != null || var6.data != null)) {
            throw new AssertionError();
         } else {
            GLUhalfEdge var9;
            for(var9 = var3; (var8 = var9.next) != var3; var9 = var8) {
               if (!$assertionsDisabled && var8.Sym.next != var9.Sym) {
                  throw new AssertionError();
               }

               if (!$assertionsDisabled && var8.Sym == var8) {
                  throw new AssertionError();
               }

               if (!$assertionsDisabled && var8.Sym.Sym != var8) {
                  throw new AssertionError();
               }

               if (!$assertionsDisabled && var8.Org == null) {
                  throw new AssertionError();
               }

               if (!$assertionsDisabled && var8.Sym.Org == null) {
                  throw new AssertionError();
               }

               if (!$assertionsDisabled && var8.Lnext.Onext.Sym != var8) {
                  throw new AssertionError();
               }

               if (!$assertionsDisabled && var8.Onext.Sym.Lnext != var8) {
                  throw new AssertionError();
               }
            }

            if (!$assertionsDisabled && (var8.Sym.next != var9.Sym || var8.Sym != var0.eHeadSym || var8.Sym.Sym != var8 || var8.Org != null || var8.Sym.Org != null || var8.Lface != null || var8.Sym.Lface != null)) {
               throw new AssertionError();
            }
         }
      }
   }
}
