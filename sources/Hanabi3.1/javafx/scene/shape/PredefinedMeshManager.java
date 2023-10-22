package javafx.scene.shape;

import java.util.HashMap;

final class PredefinedMeshManager {
   private static final PredefinedMeshManager INSTANCE = new PredefinedMeshManager();
   private static final int INITAL_CAPACITY = 17;
   private static final float LOAD_FACTOR = 0.75F;
   private HashMap boxCache = null;
   private HashMap sphereCache = null;
   private HashMap cylinderCache = null;

   private PredefinedMeshManager() {
   }

   static PredefinedMeshManager getInstance() {
      return INSTANCE;
   }

   synchronized TriangleMesh getBoxMesh(float var1, float var2, float var3, int var4) {
      if (this.boxCache == null) {
         this.boxCache = PredefinedMeshManager.BoxCacheLoader.INSTANCE;
      }

      TriangleMesh var5 = (TriangleMesh)this.boxCache.get(var4);
      if (var5 == null) {
         var5 = Box.createMesh(var1, var2, var3);
         this.boxCache.put(var4, var5);
      } else {
         var5.incRef();
      }

      return var5;
   }

   synchronized TriangleMesh getSphereMesh(float var1, int var2, int var3) {
      if (this.sphereCache == null) {
         this.sphereCache = PredefinedMeshManager.SphereCacheLoader.INSTANCE;
      }

      TriangleMesh var4 = (TriangleMesh)this.sphereCache.get(var3);
      if (var4 == null) {
         var4 = Sphere.createMesh(var2, var1);
         this.sphereCache.put(var3, var4);
      } else {
         var4.incRef();
      }

      return var4;
   }

   synchronized TriangleMesh getCylinderMesh(float var1, float var2, int var3, int var4) {
      if (this.cylinderCache == null) {
         this.cylinderCache = PredefinedMeshManager.CylinderCacheLoader.INSTANCE;
      }

      TriangleMesh var5 = (TriangleMesh)this.cylinderCache.get(var4);
      if (var5 == null) {
         var5 = Cylinder.createMesh(var3, var1, var2);
         this.cylinderCache.put(var4, var5);
      } else {
         var5.incRef();
      }

      return var5;
   }

   synchronized void invalidateBoxMesh(int var1) {
      if (this.boxCache != null) {
         TriangleMesh var2 = (TriangleMesh)this.boxCache.get(var1);
         if (var2 != null) {
            var2.decRef();
            int var3 = var2.getRefCount();
            if (var3 == 0) {
               this.boxCache.remove(var1);
            }
         }
      }

   }

   synchronized void invalidateSphereMesh(int var1) {
      if (this.sphereCache != null) {
         TriangleMesh var2 = (TriangleMesh)this.sphereCache.get(var1);
         if (var2 != null) {
            var2.decRef();
            int var3 = var2.getRefCount();
            if (var3 == 0) {
               this.sphereCache.remove(var1);
            }
         }
      }

   }

   synchronized void invalidateCylinderMesh(int var1) {
      if (this.cylinderCache != null) {
         TriangleMesh var2 = (TriangleMesh)this.cylinderCache.get(var1);
         if (var2 != null) {
            var2.decRef();
            int var3 = var2.getRefCount();
            if (var3 == 0) {
               this.cylinderCache.remove(var1);
            }
         }
      }

   }

   synchronized void dispose() {
      if (this.boxCache != null) {
         this.boxCache.clear();
      }

      if (this.sphereCache != null) {
         this.sphereCache.clear();
      }

      if (this.cylinderCache != null) {
         this.cylinderCache.clear();
      }

   }

   synchronized void printStats() {
      if (this.boxCache != null) {
         System.out.println("BoxCache size:  " + this.boxCache.size());
      }

      if (this.sphereCache != null) {
         System.out.println("SphereCache size:    " + this.sphereCache.size());
      }

      if (this.cylinderCache != null) {
         System.out.println("CylinderCache size:    " + this.cylinderCache.size());
      }

   }

   private static final class CylinderCacheLoader {
      private static final HashMap INSTANCE = new HashMap(17, 0.75F);
   }

   private static final class SphereCacheLoader {
      private static final HashMap INSTANCE = new HashMap(17, 0.75F);
   }

   private static final class BoxCacheLoader {
      private static final HashMap INSTANCE = new HashMap(17, 0.75F);
   }
}
