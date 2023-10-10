package org.lwjgl.util.glu.tessellation;

import org.lwjgl.util.glu.GLUtessellator;
import org.lwjgl.util.glu.GLUtessellatorCallback;
import org.lwjgl.util.glu.GLUtessellatorCallbackAdapter;

public class GLUtessellatorImpl implements GLUtessellator {
   public static final int TESS_MAX_CACHE = 100;
   private int state = 0;
   private GLUhalfEdge lastEdge;
   GLUmesh mesh;
   double[] normal = new double[3];
   double[] sUnit = new double[3];
   double[] tUnit = new double[3];
   private double relTolerance;
   int windingRule;
   boolean fatalError;
   Dict dict;
   PriorityQ pq;
   GLUvertex event;
   boolean flagBoundary;
   boolean boundaryOnly;
   GLUface lonelyTriList;
   private boolean flushCacheOnNextVertex;
   int cacheCount;
   CachedVertex[] cache = new CachedVertex[100];
   private Object polygonData;
   private GLUtessellatorCallback callBegin;
   private GLUtessellatorCallback callEdgeFlag;
   private GLUtessellatorCallback callVertex;
   private GLUtessellatorCallback callEnd;
   private GLUtessellatorCallback callError;
   private GLUtessellatorCallback callCombine;
   private GLUtessellatorCallback callBeginData;
   private GLUtessellatorCallback callEdgeFlagData;
   private GLUtessellatorCallback callVertexData;
   private GLUtessellatorCallback callEndData;
   private GLUtessellatorCallback callErrorData;
   private GLUtessellatorCallback callCombineData;
   private static final double GLU_TESS_DEFAULT_TOLERANCE = 0.0D;
   private static GLUtessellatorCallback NULL_CB = new GLUtessellatorCallbackAdapter();
   static final boolean $assertionsDisabled = !GLUtessellatorImpl.class.desiredAssertionStatus();

   public GLUtessellatorImpl() {
      this.normal[0] = 0.0D;
      this.normal[1] = 0.0D;
      this.normal[2] = 0.0D;
      this.relTolerance = 0.0D;
      this.windingRule = 100130;
      this.flagBoundary = false;
      this.boundaryOnly = false;
      this.callBegin = NULL_CB;
      this.callEdgeFlag = NULL_CB;
      this.callVertex = NULL_CB;
      this.callEnd = NULL_CB;
      this.callError = NULL_CB;
      this.callCombine = NULL_CB;
      this.callBeginData = NULL_CB;
      this.callEdgeFlagData = NULL_CB;
      this.callVertexData = NULL_CB;
      this.callEndData = NULL_CB;
      this.callErrorData = NULL_CB;
      this.callCombineData = NULL_CB;
      this.polygonData = null;

      for(int var1 = 0; var1 < this.cache.length; ++var1) {
         this.cache[var1] = new CachedVertex();
      }

   }

   public static GLUtessellator gluNewTess() {
      return new GLUtessellatorImpl();
   }

   private void makeDormant() {
      if (this.mesh != null) {
         Mesh.__gl_meshDeleteMesh(this.mesh);
      }

      this.state = 0;
      this.lastEdge = null;
      this.mesh = null;
   }

   private void requireState(int var1) {
      if (this.state != var1) {
         this.gotoState(var1);
      }

   }

   private void gotoState(int var1) {
      while(this.state != var1) {
         if (this.state < var1) {
            if (this.state == 0) {
               this.callErrorOrErrorData(100151);
               this.gluTessBeginPolygon((Object)null);
            } else if (this.state == 1) {
               this.callErrorOrErrorData(100152);
               this.gluTessBeginContour();
            }
         } else if (this.state == 2) {
            this.callErrorOrErrorData(100154);
            this.gluTessEndContour();
         } else if (this.state == 1) {
            this.callErrorOrErrorData(100153);
            this.makeDormant();
         }
      }

   }

   public void gluDeleteTess() {
      this.requireState(0);
   }

   public void gluTessProperty(int var1, double var2) {
      label31: {
         switch(var1) {
         case 100140:
            int var4 = (int)var2;
            if ((double)var4 == var2) {
               switch(var4) {
               case 100130:
               case 100131:
               case 100132:
               case 100133:
               case 100134:
                  this.windingRule = var4;
                  return;
               default:
                  break label31;
               }
            }
            break;
         case 100141:
            break label31;
         case 100142:
            if (!(var2 < 0.0D) && !(var2 > 1.0D)) {
               this.relTolerance = var2;
               return;
            }
            break;
         default:
            this.callErrorOrErrorData(100900);
            return;
         }

         this.callErrorOrErrorData(100901);
         return;
      }

      this.boundaryOnly = var2 != 0.0D;
   }

   public void gluGetTessProperty(int var1, double[] var2, int var3) {
      switch(var1) {
      case 100140:
         if (!$assertionsDisabled && this.windingRule != 100130 && this.windingRule != 100131 && this.windingRule != 100132 && this.windingRule != 100133 && this.windingRule != 100134) {
            throw new AssertionError();
         }

         var2[var3] = (double)this.windingRule;
         break;
      case 100141:
         if (!$assertionsDisabled && !this.boundaryOnly && this.boundaryOnly) {
            throw new AssertionError();
         }

         var2[var3] = this.boundaryOnly ? 1.0D : 0.0D;
         break;
      case 100142:
         if (!$assertionsDisabled && (!(0.0D <= this.relTolerance) || !(this.relTolerance <= 1.0D))) {
            throw new AssertionError();
         }

         var2[var3] = this.relTolerance;
         break;
      default:
         var2[var3] = 0.0D;
         this.callErrorOrErrorData(100900);
      }

   }

   public void gluTessNormal(double var1, double var3, double var5) {
      this.normal[0] = var1;
      this.normal[1] = var3;
      this.normal[2] = var5;
   }

   public void gluTessCallback(int var1, GLUtessellatorCallback var2) {
      switch(var1) {
      case 100100:
         this.callBegin = var2 == null ? NULL_CB : var2;
         return;
      case 100101:
         this.callVertex = var2 == null ? NULL_CB : var2;
         return;
      case 100102:
         this.callEnd = var2 == null ? NULL_CB : var2;
         return;
      case 100103:
         this.callError = var2 == null ? NULL_CB : var2;
         return;
      case 100104:
         this.callEdgeFlag = var2 == null ? NULL_CB : var2;
         this.flagBoundary = var2 != null;
         return;
      case 100105:
         this.callCombine = var2 == null ? NULL_CB : var2;
         return;
      case 100106:
         this.callBeginData = var2 == null ? NULL_CB : var2;
         return;
      case 100107:
         this.callVertexData = var2 == null ? NULL_CB : var2;
         return;
      case 100108:
         this.callEndData = var2 == null ? NULL_CB : var2;
         return;
      case 100109:
         this.callErrorData = var2 == null ? NULL_CB : var2;
         return;
      case 100110:
         this.callEdgeFlagData = this.callBegin = var2 == null ? NULL_CB : var2;
         this.flagBoundary = var2 != null;
         return;
      case 100111:
         this.callCombineData = var2 == null ? NULL_CB : var2;
         return;
      default:
         this.callErrorOrErrorData(100900);
      }
   }

   private void cacheVertex(double[] var1, Object var2) {
      if (this.cache[this.cacheCount] == null) {
         this.cache[this.cacheCount] = new CachedVertex();
      }

      CachedVertex var3 = this.cache[this.cacheCount];
      var3.data = var2;
      var3.coords[0] = var1[0];
      var3.coords[1] = var1[1];
      var3.coords[2] = var1[2];
      ++this.cacheCount;
   }

   public void gluTessVertex(double[] var1, int var2, Object var3) {
      boolean var5 = false;
      double[] var8 = new double[3];
      this.requireState(2);
      if (this.flushCacheOnNextVertex) {
         if (this == null) {
            this.callErrorOrErrorData(100902);
            return;
         }

         this.lastEdge = null;
      }

      for(int var4 = 0; var4 < 3; ++var4) {
         double var6 = var1[var4 + var2];
         if (var6 < -1.0E150D) {
            var6 = -1.0E150D;
            var5 = true;
         }

         if (var6 > 1.0E150D) {
            var6 = 1.0E150D;
            var5 = true;
         }

         var8[var4] = var6;
      }

      if (var5) {
         this.callErrorOrErrorData(100155);
      }

      if (this.mesh == null) {
         if (this.cacheCount < 100) {
            this.cacheVertex(var8, var3);
            return;
         }

         if (this == null) {
            this.callErrorOrErrorData(100902);
            return;
         }
      }

      if (var3 == null) {
         this.callErrorOrErrorData(100902);
      }

   }

   public void gluTessBeginPolygon(Object var1) {
      this.requireState(0);
      this.state = 1;
      this.cacheCount = 0;
      this.flushCacheOnNextVertex = false;
      this.mesh = null;
      this.polygonData = var1;
   }

   public void gluTessBeginContour() {
      this.requireState(1);
      this.state = 2;
      this.lastEdge = null;
      if (this.cacheCount > 0) {
         this.flushCacheOnNextVertex = true;
      }

   }

   public void gluTessEndContour() {
      this.requireState(2);
      this.state = 1;
   }

   public void gluTessEndPolygon() {
      try {
         this.requireState(1);
         this.state = 0;
         if (this.mesh == null) {
            if (!this.flagBoundary && Render.__gl_renderCache(this)) {
               this.polygonData = null;
               return;
            }

            if (this == null) {
               throw new RuntimeException();
            }
         }

         Normal.__gl_projectPolygon(this);
         if (!Sweep.__gl_computeInterior(this)) {
            throw new RuntimeException();
         }

         GLUmesh var1 = this.mesh;
         if (!this.fatalError) {
            boolean var2 = true;
            if (this.boundaryOnly) {
               var2 = TessMono.__gl_meshSetWindingNumber(var1, 1, true);
            } else {
               var2 = TessMono.__gl_meshTessellateInterior(var1);
            }

            if (!var2) {
               throw new RuntimeException();
            }

            Mesh.__gl_meshCheckMesh(var1);
            if (this.callBegin != NULL_CB || this.callEnd != NULL_CB || this.callVertex != NULL_CB || this.callEdgeFlag != NULL_CB || this.callBeginData != NULL_CB || this.callEndData != NULL_CB || this.callVertexData != NULL_CB || this.callEdgeFlagData != NULL_CB) {
               if (this.boundaryOnly) {
                  Render.__gl_renderBoundary(this, var1);
               } else {
                  Render.__gl_renderMesh(this, var1);
               }
            }
         }

         Mesh.__gl_meshDeleteMesh(var1);
         this.polygonData = null;
         var1 = null;
      } catch (Exception var3) {
         var3.printStackTrace();
         this.callErrorOrErrorData(100902);
      }

   }

   public void gluBeginPolygon() {
      this.gluTessBeginPolygon((Object)null);
      this.gluTessBeginContour();
   }

   public void gluNextContour(int var1) {
      this.gluTessEndContour();
      this.gluTessBeginContour();
   }

   public void gluEndPolygon() {
      this.gluTessEndContour();
      this.gluTessEndPolygon();
   }

   void callBeginOrBeginData(int var1) {
      if (this.callBeginData != NULL_CB) {
         this.callBeginData.beginData(var1, this.polygonData);
      } else {
         this.callBegin.begin(var1);
      }

   }

   void callVertexOrVertexData(Object var1) {
      if (this.callVertexData != NULL_CB) {
         this.callVertexData.vertexData(var1, this.polygonData);
      } else {
         this.callVertex.vertex(var1);
      }

   }

   void callEdgeFlagOrEdgeFlagData(boolean var1) {
      if (this.callEdgeFlagData != NULL_CB) {
         this.callEdgeFlagData.edgeFlagData(var1, this.polygonData);
      } else {
         this.callEdgeFlag.edgeFlag(var1);
      }

   }

   void callEndOrEndData() {
      if (this.callEndData != NULL_CB) {
         this.callEndData.endData(this.polygonData);
      } else {
         this.callEnd.end();
      }

   }

   void callCombineOrCombineData(double[] var1, Object[] var2, float[] var3, Object[] var4) {
      if (this.callCombineData != NULL_CB) {
         this.callCombineData.combineData(var1, var2, var3, var4, this.polygonData);
      } else {
         this.callCombine.combine(var1, var2, var3, var4);
      }

   }

   void callErrorOrErrorData(int var1) {
      if (this.callErrorData != NULL_CB) {
         this.callErrorData.errorData(var1, this.polygonData);
      } else {
         this.callError.error(var1);
      }

   }
}
