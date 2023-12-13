package com.sun.prism.impl;

import com.sun.javafx.geom.Vec3f;

class MeshVertex {
   int smGroup;
   int pVert;
   int tVert;
   int fIdx;
   int index;
   Vec3f[] norm = new Vec3f[3];
   MeshVertex next = null;
   static final int IDX_UNDEFINED = -1;
   static final int IDX_SET_SMOOTH = -2;
   static final int IDX_UNITE = -3;

   MeshVertex() {
      for(int var1 = 0; var1 < this.norm.length; ++var1) {
         this.norm[var1] = new Vec3f();
      }

   }

   static void avgSmNormals(MeshVertex var0) {
      for(Vec3f var1 = MeshTempState.getInstance().vec3f1; var0 != null; var0 = var0.next) {
         if (var0.index == -1) {
            var1.set(var0.norm[0]);
            int var2 = var0.smGroup;

            MeshVertex var3;
            for(var3 = var0.next; var3 != null; var3 = var3.next) {
               if (var3.smGroup == var2) {
                  assert var3.index == -1;

                  var3.index = -2;
                  var1.add(var3.norm[0]);
               }
            }

            if (MeshUtil.isNormalOkAfterWeld(var1)) {
               var1.normalize();

               for(var3 = var0; var3 != null; var3 = var3.next) {
                  if (var3.smGroup == var2) {
                     var3.norm[0].set(var1);
                  }
               }
            }
         }
      }

   }

   static boolean okToWeldVertsTB(MeshVertex var0, MeshVertex var1) {
      return var0.tVert == var1.tVert && MeshUtil.isTangentOk(var0.norm, var1.norm);
   }

   static int weldWithTB(MeshVertex var0, int var1) {
      for(Vec3f[] var2 = MeshTempState.getInstance().triNormals; var0 != null; var0 = var0.next) {
         if (var0.index < 0) {
            int var3 = 0;

            int var4;
            for(var4 = 0; var4 < 3; ++var4) {
               var2[var4].set(var0.norm[var4]);
            }

            MeshVertex var6;
            for(var6 = var0.next; var6 != null; var6 = var6.next) {
               if (var6.index < 0 && okToWeldVertsTB(var0, var6)) {
                  var6.index = -3;
                  ++var3;

                  for(int var5 = 0; var5 < 3; ++var5) {
                     var2[var5].add(var6.norm[var5]);
                  }
               }
            }

            if (var3 != 0) {
               if (MeshUtil.isTangentOK(var2)) {
                  MeshUtil.fixTSpace(var2);
                  var0.index = var1;

                  for(var4 = 0; var4 < 3; ++var4) {
                     var0.norm[var4].set(var2[var4]);
                  }

                  for(var6 = var0.next; var6 != null; var6 = var6.next) {
                     if (var6.index == -3) {
                        var6.index = var1;
                        var6.norm[0].set(0.0F, 0.0F, 0.0F);
                     }
                  }
               } else {
                  var3 = 0;
               }
            }

            if (var3 == 0) {
               MeshUtil.fixTSpace(var0.norm);
               var0.index = var1;
            }

            ++var1;
         }
      }

      return var1;
   }

   static void mergeSmIndexes(MeshVertex var0) {
      MeshVertex var1 = var0;

      while(var1 != null) {
         boolean var2 = false;

         for(MeshVertex var3 = var1.next; var3 != null; var3 = var3.next) {
            if ((var1.smGroup & var3.smGroup) != 0 && var1.smGroup != var3.smGroup) {
               var1.smGroup |= var3.smGroup;
               var3.smGroup = var1.smGroup;
               var2 = true;
            }
         }

         if (!var2) {
            var1 = var1.next;
         }
      }

   }

   static void correctSmNormals(MeshVertex var0) {
      for(MeshVertex var1 = var0; var1 != null; var1 = var1.next) {
         if (var1.smGroup != 0) {
            for(MeshVertex var2 = var1.next; var2 != null; var2 = var2.next) {
               if ((var2.smGroup & var1.smGroup) != 0 && MeshUtil.isOppositeLookingNormals(var2.norm, var1.norm)) {
                  var1.smGroup = 0;
                  var2.smGroup = 0;
                  break;
               }
            }
         }
      }

   }

   static int processVertices(MeshVertex[] var0, int var1, boolean var2, boolean var3) {
      int var4 = 0;
      Vec3f var5 = MeshTempState.getInstance().vec3f1;

      for(int var6 = 0; var6 < var1; ++var6) {
         if (var0[var6] != null) {
            if (!var2) {
               if (var3) {
                  var5.set(var0[var6].norm[0]);

                  MeshVertex var7;
                  for(var7 = var0[var6].next; var7 != null; var7 = var7.next) {
                     var5.add(var7.norm[0]);
                  }

                  if (MeshUtil.isNormalOkAfterWeld(var5)) {
                     var5.normalize();

                     for(var7 = var0[var6]; var7 != null; var7 = var7.next) {
                        var7.norm[0].set(var5);
                     }
                  }
               } else {
                  mergeSmIndexes(var0[var6]);
                  avgSmNormals(var0[var6]);
               }
            }

            var4 = weldWithTB(var0[var6], var4);
         }
      }

      return var4;
   }

   public String toString() {
      return "MeshVertex : " + this.getClass().getName() + "@0x" + Integer.toHexString(this.hashCode()) + ":: smGroup = " + this.smGroup + "\n\tnorm[0] = " + this.norm[0] + "\n\tnorm[1] = " + this.norm[1] + "\n\tnorm[2] = " + this.norm[2] + "\n\ttIndex = " + this.tVert + ", fIndex = " + this.fIdx + "\n\tpIdx = " + this.index + "\n\tnext = " + (this.next == null ? this.next : this.next.getClass().getName() + "@0x" + Integer.toHexString(this.next.hashCode())) + "\n";
   }

   static void dumpInfo(MeshVertex var0) {
      System.err.println("** dumpInfo: ");

      for(MeshVertex var1 = var0; var1 != null; var1 = var1.next) {
         System.err.println(var1);
      }

      System.err.println("***********************************");
   }
}
