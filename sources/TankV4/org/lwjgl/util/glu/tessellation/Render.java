package org.lwjgl.util.glu.tessellation;

class Render {
   private static final boolean USE_OPTIMIZED_CODE_PATH = false;
   private static final Render.RenderFan renderFan = new Render.RenderFan();
   private static final Render.RenderStrip renderStrip = new Render.RenderStrip();
   private static final Render.RenderTriangle renderTriangle = new Render.RenderTriangle();
   private static final int SIGN_INCONSISTENT = 2;
   static final boolean $assertionsDisabled = !Render.class.desiredAssertionStatus();

   private Render() {
   }

   public static void __gl_renderMesh(GLUtessellatorImpl var0, GLUmesh var1) {
      var0.lonelyTriList = null;

      GLUface var2;
      for(var2 = var1.fHead.next; var2 != var1.fHead; var2 = var2.next) {
         var2.marked = false;
      }

      for(var2 = var1.fHead.next; var2 != var1.fHead; var2 = var2.next) {
         if (var2.inside && !var2.marked) {
            RenderMaximumFaceGroup(var0, var2);
            if (!$assertionsDisabled && !var2.marked) {
               throw new AssertionError();
            }
         }
      }

      if (var0.lonelyTriList != null) {
         RenderLonelyTriangles(var0, var0.lonelyTriList);
         var0.lonelyTriList = null;
      }

   }

   static void RenderMaximumFaceGroup(GLUtessellatorImpl var0, GLUface var1) {
      GLUhalfEdge var2 = var1.anEdge;
      Render.FaceCount var3 = new Render.FaceCount();
      var3.size = 1L;
      var3.eStart = var2;
      var3.render = renderTriangle;
      if (!var0.flagBoundary) {
         Render.FaceCount var4 = MaximumFan(var2);
         if (var4.size > var3.size) {
            var3 = var4;
         }

         var4 = MaximumFan(var2.Lnext);
         if (var4.size > var3.size) {
            var3 = var4;
         }

         var4 = MaximumFan(var2.Onext.Sym);
         if (var4.size > var3.size) {
            var3 = var4;
         }

         var4 = MaximumStrip(var2);
         if (var4.size > var3.size) {
            var3 = var4;
         }

         var4 = MaximumStrip(var2.Lnext);
         if (var4.size > var3.size) {
            var3 = var4;
         }

         var4 = MaximumStrip(var2.Onext.Sym);
         if (var4.size > var3.size) {
            var3 = var4;
         }
      }

      var3.render.render(var0, var3.eStart, var3.size);
   }

   private static GLUface AddToTrail(GLUface var0, GLUface var1) {
      var0.trail = var1;
      var0.marked = true;
      return var0;
   }

   private static void FreeTrail(GLUface var0) {
      while(var0 != null) {
         var0.marked = false;
         var0 = var0.trail;
      }

   }

   static Render.FaceCount MaximumFan(GLUhalfEdge var0) {
      Render.FaceCount var1 = new Render.FaceCount(0L, (GLUhalfEdge)null, renderFan);
      GLUface var2 = null;

      GLUhalfEdge var3;
      for(var3 = var0; var3.Lface != false; var3 = var3.Onext) {
         var2 = AddToTrail(var3.Lface, var2);
         ++var1.size;
      }

      for(var3 = var0; var3.Sym.Lface != false; var3 = var3.Sym.Lnext) {
         var2 = AddToTrail(var3.Sym.Lface, var2);
         ++var1.size;
      }

      var1.eStart = var3;
      FreeTrail(var2);
      return var1;
   }

   static Render.FaceCount MaximumStrip(GLUhalfEdge var0) {
      Render.FaceCount var1 = new Render.FaceCount(0L, (GLUhalfEdge)null, renderStrip);
      long var2 = 0L;
      long var4 = 0L;
      GLUface var6 = null;

      GLUhalfEdge var7;
      for(var7 = var0; var7.Lface != false; var7 = var7.Onext) {
         var6 = AddToTrail(var7.Lface, var6);
         ++var4;
         var7 = var7.Lnext.Sym;
         if (var7.Lface != false) {
            break;
         }

         var6 = AddToTrail(var7.Lface, var6);
         ++var4;
      }

      GLUhalfEdge var8 = var7;

      for(var7 = var0; var7.Sym.Lface != false; var7 = var7.Sym.Onext.Sym) {
         var6 = AddToTrail(var7.Sym.Lface, var6);
         ++var2;
         var7 = var7.Sym.Lnext;
         if (var7.Sym.Lface != false) {
            break;
         }

         var6 = AddToTrail(var7.Sym.Lface, var6);
         ++var2;
      }

      var1.size = var4 + var2;
      if (var4 == false) {
         var1.eStart = var8.Sym;
      } else if (var2 == false) {
         var1.eStart = var7;
      } else {
         --var1.size;
         var1.eStart = var7.Onext;
      }

      FreeTrail(var6);
      return var1;
   }

   static void RenderLonelyTriangles(GLUtessellatorImpl var0, GLUface var1) {
      int var4 = -1;
      var0.callBeginOrBeginData(4);

      while(var1 != null) {
         GLUhalfEdge var2 = var1.anEdge;

         do {
            if (var0.flagBoundary) {
               int var3 = !var2.Sym.Lface.inside ? 1 : 0;
               if (var4 != var3) {
                  var4 = var3;
                  var0.callEdgeFlagOrEdgeFlagData(var3 != 0);
               }
            }

            var0.callVertexOrVertexData(var2.Org.data);
            var2 = var2.Lnext;
         } while(var2 != var1.anEdge);

         var1 = var1.trail;
      }

      var0.callEndOrEndData();
   }

   public static void __gl_renderBoundary(GLUtessellatorImpl var0, GLUmesh var1) {
      for(GLUface var2 = var1.fHead.next; var2 != var1.fHead; var2 = var2.next) {
         if (var2.inside) {
            var0.callBeginOrBeginData(2);
            GLUhalfEdge var3 = var2.anEdge;

            do {
               var0.callVertexOrVertexData(var3.Org.data);
               var3 = var3.Lnext;
            } while(var3 != var2.anEdge);

            var0.callEndOrEndData();
         }
      }

   }

   static int ComputeNormal(GLUtessellatorImpl var0, double[] var1, boolean var2) {
      CachedVertex[] var3 = var0.cache;
      int var4 = var0.cacheCount;
      double[] var20 = new double[3];
      byte var21 = 0;
      if (!var2) {
         var1[0] = var1[1] = var1[2] = 0.0D;
      }

      int var5 = 1;
      double var8 = var3[var5].coords[0] - var3[0].coords[0];
      double var10 = var3[var5].coords[1] - var3[0].coords[1];
      double var12 = var3[var5].coords[2] - var3[0].coords[2];

      while(true) {
         ++var5;
         if (var5 >= var4) {
            return var21;
         }

         double var14 = var8;
         double var16 = var10;
         double var18 = var12;
         var8 = var3[var5].coords[0] - var3[0].coords[0];
         var10 = var3[var5].coords[1] - var3[0].coords[1];
         var12 = var3[var5].coords[2] - var3[0].coords[2];
         var20[0] = var16 * var12 - var18 * var10;
         var20[1] = var18 * var8 - var14 * var12;
         var20[2] = var14 * var10 - var16 * var8;
         double var6 = var20[0] * var1[0] + var20[1] * var1[1] + var20[2] * var1[2];
         if (!var2) {
            if (var6 >= 0.0D) {
               var1[0] += var20[0];
               var1[1] += var20[1];
               var1[2] += var20[2];
            } else {
               var1[0] -= var20[0];
               var1[1] -= var20[1];
               var1[2] -= var20[2];
            }
         } else if (var6 != 0.0D) {
            if (var6 > 0.0D) {
               if (var21 < 0) {
                  return 2;
               }

               var21 = 1;
            } else {
               if (var21 > 0) {
                  return 2;
               }

               var21 = -1;
            }
         }
      }
   }

   public static boolean __gl_renderCache(GLUtessellatorImpl var0) {
      CachedVertex[] var1 = var0.cache;
      int var2 = var0.cacheCount;
      double[] var4 = new double[3];
      if (var0.cacheCount < 3) {
         return true;
      } else {
         var4[0] = var0.normal[0];
         var4[1] = var0.normal[1];
         var4[2] = var0.normal[2];
         if (var4[0] == 0.0D && var4[1] == 0.0D && var4[2] == 0.0D) {
            ComputeNormal(var0, var4, false);
         }

         int var5 = ComputeNormal(var0, var4, true);
         if (var5 == 2) {
            return false;
         } else {
            return var5 == 0;
         }
      }
   }

   static GLUface access$500(GLUface var0, GLUface var1) {
      return AddToTrail(var0, var1);
   }

   static boolean access$600(GLUface var0) {
      return Marked(var0);
   }

   private static class RenderStrip implements Render.renderCallBack {
      static final boolean $assertionsDisabled = !Render.class.desiredAssertionStatus();

      private RenderStrip() {
      }

      public void render(GLUtessellatorImpl var1, GLUhalfEdge var2, long var3) {
         var1.callBeginOrBeginData(5);
         var1.callVertexOrVertexData(var2.Org.data);
         var1.callVertexOrVertexData(var2.Sym.Org.data);

         while(!Render.access$600(var2.Lface)) {
            var2.Lface.marked = true;
            --var3;
            var2 = var2.Lnext.Sym;
            var1.callVertexOrVertexData(var2.Org.data);
            if (Render.access$600(var2.Lface)) {
               break;
            }

            var2.Lface.marked = true;
            --var3;
            var2 = var2.Onext;
            var1.callVertexOrVertexData(var2.Sym.Org.data);
         }

         if (!$assertionsDisabled && var3 != 0L) {
            throw new AssertionError();
         } else {
            var1.callEndOrEndData();
         }
      }

      RenderStrip(Object var1) {
         this();
      }
   }

   private static class RenderFan implements Render.renderCallBack {
      static final boolean $assertionsDisabled = !Render.class.desiredAssertionStatus();

      private RenderFan() {
      }

      public void render(GLUtessellatorImpl var1, GLUhalfEdge var2, long var3) {
         var1.callBeginOrBeginData(6);
         var1.callVertexOrVertexData(var2.Org.data);
         var1.callVertexOrVertexData(var2.Sym.Org.data);

         while(!Render.access$600(var2.Lface)) {
            var2.Lface.marked = true;
            --var3;
            var2 = var2.Onext;
            var1.callVertexOrVertexData(var2.Sym.Org.data);
         }

         if (!$assertionsDisabled && var3 != 0L) {
            throw new AssertionError();
         } else {
            var1.callEndOrEndData();
         }
      }

      RenderFan(Object var1) {
         this();
      }
   }

   private static class RenderTriangle implements Render.renderCallBack {
      static final boolean $assertionsDisabled = !Render.class.desiredAssertionStatus();

      private RenderTriangle() {
      }

      public void render(GLUtessellatorImpl var1, GLUhalfEdge var2, long var3) {
         if (!$assertionsDisabled && var3 != 1L) {
            throw new AssertionError();
         } else {
            var1.lonelyTriList = Render.access$500(var2.Lface, var1.lonelyTriList);
         }
      }

      RenderTriangle(Object var1) {
         this();
      }
   }

   private interface renderCallBack {
      void render(GLUtessellatorImpl var1, GLUhalfEdge var2, long var3);
   }

   private static class FaceCount {
      long size;
      GLUhalfEdge eStart;
      Render.renderCallBack render;

      private FaceCount() {
      }

      private FaceCount(long var1, GLUhalfEdge var3, Render.renderCallBack var4) {
         this.size = var1;
         this.eStart = var3;
         this.render = var4;
      }

      FaceCount(Object var1) {
         this();
      }

      FaceCount(long var1, GLUhalfEdge var3, Render.renderCallBack var4, Object var5) {
         this(var1, var3, var4);
      }
   }
}
