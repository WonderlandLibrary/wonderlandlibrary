package com.sun.prism.impl;

import com.sun.javafx.geom.Quat4f;
import com.sun.javafx.geom.Vec2f;
import com.sun.javafx.geom.Vec3f;
import com.sun.prism.Mesh;
import java.util.Arrays;
import java.util.HashMap;
import javafx.scene.shape.VertexFormat;
import sun.util.logging.PlatformLogger;
import sun.util.logging.PlatformLogger.Level;

public abstract class BaseMesh extends BaseGraphicsResource implements Mesh {
   private int nVerts;
   private int nTVerts;
   private int nFaces;
   private float[] pos;
   private float[] uv;
   private int[] faces;
   private int[] smoothing;
   private boolean allSameSmoothing;
   private boolean allHardEdges;
   protected static final int POINT_SIZE = 3;
   protected static final int NORMAL_SIZE = 3;
   protected static final int TEXCOORD_SIZE = 2;
   protected static final int POINT_SIZE_VB = 3;
   protected static final int TEXCOORD_SIZE_VB = 2;
   protected static final int NORMAL_SIZE_VB = 4;
   protected static final int VERTEX_SIZE_VB = 9;
   public static final int FACE_MEMBERS_SIZE = 7;
   private boolean[] dirtyVertices;
   private float[] cachedNormals;
   private float[] cachedTangents;
   private float[] cachedBitangents;
   private float[] vertexBuffer;
   private int[] indexBuffer;
   private short[] indexBufferShort;
   private int indexBufferSize;
   private int numberOfVertices;
   private HashMap point2vbMap;
   private HashMap normal2vbMap;
   private HashMap texCoord2vbMap;

   protected BaseMesh(Disposer.Record var1) {
      super(var1);
   }

   public abstract boolean buildNativeGeometry(float[] var1, int var2, int[] var3, int var4);

   public abstract boolean buildNativeGeometry(float[] var1, int var2, short[] var3, int var4);

   private boolean updateSkipMeshNormalGeometry(int[] var1, int[] var2) {
      int var3 = var2[0] / 2;
      int var4 = var2[1] / 2;
      if (var2[1] % 2 > 0) {
         ++var4;
      }

      int var5;
      int var6;
      int var11;
      if (var4 > 0) {
         for(var5 = 0; var5 < var4; ++var5) {
            var6 = (var3 + var5) * 2;
            MeshGeomComp2VB var7 = (MeshGeomComp2VB)this.texCoord2vbMap.get(var6);

            assert var7 != null;

            if (var7 != null) {
               int[] var8 = var7.getLocs();
               int var9 = var7.getValidLocs();
               int var10;
               if (var8 != null) {
                  for(var10 = 0; var10 < var9; ++var10) {
                     var11 = var8[var10] * 9 + 3;
                     this.vertexBuffer[var11] = this.uv[var6];
                     this.vertexBuffer[var11 + 1] = this.uv[var6 + 1];
                  }
               } else {
                  var10 = var7.getLoc();
                  var11 = var10 * 9 + 3;
                  this.vertexBuffer[var11] = this.uv[var6];
                  this.vertexBuffer[var11 + 1] = this.uv[var6 + 1];
               }
            }
         }
      }

      var5 = var1[0] / 3;
      var6 = var1[1] / 3;
      if (var1[1] % 3 > 0) {
         ++var6;
      }

      if (var6 > 0) {
         for(int var14 = 0; var14 < var6; ++var14) {
            int var15 = (var5 + var14) * 3;
            MeshGeomComp2VB var16 = (MeshGeomComp2VB)this.point2vbMap.get(var15);

            assert var16 != null;

            if (var16 != null) {
               int[] var17 = var16.getLocs();
               var11 = var16.getValidLocs();
               int var12;
               int var13;
               if (var17 != null) {
                  for(var12 = 0; var12 < var11; ++var12) {
                     var13 = var17[var12] * 9;
                     this.vertexBuffer[var13] = this.pos[var15];
                     this.vertexBuffer[var13 + 1] = this.pos[var15 + 1];
                     this.vertexBuffer[var13 + 2] = this.pos[var15 + 2];
                  }
               } else {
                  var12 = var16.getLoc();
                  var13 = var12 * 9;
                  this.vertexBuffer[var13] = this.pos[var15];
                  this.vertexBuffer[var13 + 1] = this.pos[var15 + 1];
                  this.vertexBuffer[var13 + 2] = this.pos[var15 + 2];
               }
            }
         }
      }

      return this.indexBuffer != null ? this.buildNativeGeometry(this.vertexBuffer, this.numberOfVertices * 9, this.indexBuffer, this.nFaces * 3) : this.buildNativeGeometry(this.vertexBuffer, this.numberOfVertices * 9, this.indexBufferShort, this.nFaces * 3);
   }

   private boolean buildSkipMeshNormalGeometry() {
      HashMap var1 = new HashMap();
      if (this.point2vbMap == null) {
         this.point2vbMap = new HashMap();
      } else {
         this.point2vbMap.clear();
      }

      if (this.texCoord2vbMap == null) {
         this.texCoord2vbMap = new HashMap();
      } else {
         this.texCoord2vbMap.clear();
      }

      this.vertexBuffer = new float[this.nVerts * 9];
      this.indexBuffer = new int[this.nFaces * 3];
      int var5 = 0;
      int var6 = 0;

      int var7;
      int var8;
      for(var7 = 0; var7 < this.nFaces; ++var7) {
         var8 = var7 * 6;

         for(int var9 = 0; var9 < 3; ++var9) {
            int var10 = var9 * 2;
            int var11 = var8 + var10;
            int var12 = var11 + 1;
            long var13 = (long)this.faces[var11] << 32 | (long)this.faces[var12];
            Integer var2 = (Integer)var1.get(var13);
            if (var2 == null) {
               var2 = var6 / 9;
               var1.put(var13, var2);
               if (this.vertexBuffer.length <= var6) {
                  float[] var15 = new float[var6 + 90];
                  System.arraycopy(this.vertexBuffer, 0, var15, 0, this.vertexBuffer.length);
                  this.vertexBuffer = var15;
               }

               int var17 = this.faces[var11] * 3;
               int var16 = this.faces[var12] * 2;
               this.vertexBuffer[var6] = this.pos[var17];
               this.vertexBuffer[var6 + 1] = this.pos[var17 + 1];
               this.vertexBuffer[var6 + 2] = this.pos[var17 + 2];
               this.vertexBuffer[var6 + 3] = this.uv[var16];
               this.vertexBuffer[var6 + 4] = this.uv[var16 + 1];
               this.vertexBuffer[var6 + 5] = 0.0F;
               this.vertexBuffer[var6 + 6] = 0.0F;
               this.vertexBuffer[var6 + 7] = 0.0F;
               this.vertexBuffer[var6 + 8] = 0.0F;
               var6 += 9;
               MeshGeomComp2VB var3 = (MeshGeomComp2VB)this.point2vbMap.get(var17);
               if (var3 == null) {
                  var3 = new MeshGeomComp2VB(var17, var2);
                  this.point2vbMap.put(var17, var3);
               } else {
                  var3.addLoc(var2);
               }

               MeshGeomComp2VB var4 = (MeshGeomComp2VB)this.texCoord2vbMap.get(var16);
               if (var4 == null) {
                  var4 = new MeshGeomComp2VB(var16, var2);
                  this.texCoord2vbMap.put(var16, var4);
               } else {
                  var4.addLoc(var2);
               }
            }

            this.indexBuffer[var5++] = var2;
         }
      }

      this.numberOfVertices = var6 / 9;
      if (this.numberOfVertices > 65536) {
         return this.buildNativeGeometry(this.vertexBuffer, this.numberOfVertices * 9, this.indexBuffer, this.nFaces * 3);
      } else {
         if (this.indexBufferShort == null || this.indexBufferShort.length < this.nFaces * 3) {
            this.indexBufferShort = new short[this.nFaces * 3];
         }

         var7 = 0;

         for(var8 = 0; var8 < this.nFaces; ++var8) {
            this.indexBufferShort[var7] = (short)this.indexBuffer[var7++];
            this.indexBufferShort[var7] = (short)this.indexBuffer[var7++];
            this.indexBufferShort[var7] = (short)this.indexBuffer[var7++];
         }

         this.indexBuffer = null;
         return this.buildNativeGeometry(this.vertexBuffer, this.numberOfVertices * 9, this.indexBufferShort, this.nFaces * 3);
      }
   }

   private void convertNormalsToQuats(MeshTempState var1, int var2, float[] var3, float[] var4, float[] var5, float[] var6, boolean[] var7) {
      Vec3f var8 = var1.vec3f1;
      Vec3f var9 = var1.vec3f2;
      Vec3f var10 = var1.vec3f3;
      int var11 = 0;

      for(int var12 = 0; var11 < var2; var12 += 9) {
         if (var7 == null || var7[var11]) {
            int var13 = var11 * 3;
            var8.x = var3[var13];
            var8.y = var3[var13 + 1];
            var8.z = var3[var13 + 2];
            var8.normalize();
            var9.x = var4[var13];
            var9.y = var4[var13 + 1];
            var9.z = var4[var13 + 2];
            var10.x = var5[var13];
            var10.y = var5[var13 + 1];
            var10.z = var5[var13 + 2];
            var1.triNormals[0].set(var8);
            var1.triNormals[1].set(var9);
            var1.triNormals[2].set(var10);
            MeshUtil.fixTSpace(var1.triNormals);
            this.buildVSQuat(var1.triNormals, var1.quat);
            var6[var12 + 5] = var1.quat.x;
            var6[var12 + 6] = var1.quat.y;
            var6[var12 + 7] = var1.quat.z;
            var6[var12 + 8] = var1.quat.w;
         }

         ++var11;
      }

   }

   private boolean doBuildPNTGeometry(float[] var1, float[] var2, float[] var3, int[] var4) {
      if (this.point2vbMap == null) {
         this.point2vbMap = new HashMap();
      } else {
         this.point2vbMap.clear();
      }

      if (this.normal2vbMap == null) {
         this.normal2vbMap = new HashMap();
      } else {
         this.normal2vbMap.clear();
      }

      if (this.texCoord2vbMap == null) {
         this.texCoord2vbMap = new HashMap();
      } else {
         this.texCoord2vbMap.clear();
      }

      int var5 = VertexFormat.POINT_NORMAL_TEXCOORD.getVertexIndexSize();
      int var6 = var5 * 3;
      int var7 = VertexFormat.POINT_NORMAL_TEXCOORD.getPointIndexOffset();
      int var8 = VertexFormat.POINT_NORMAL_TEXCOORD.getNormalIndexOffset();
      int var9 = VertexFormat.POINT_NORMAL_TEXCOORD.getTexCoordIndexOffset();
      int var10 = var1.length / 3;
      int var11 = var2.length / 3;
      int var12 = var3.length / 2;
      int var13 = var4.length / var6;

      assert var10 > 0 && var11 > 0 && var12 > 0 && var13 > 0;

      this.cachedNormals = new float[var10 * 3];
      this.cachedTangents = new float[var10 * 3];
      this.cachedBitangents = new float[var10 * 3];
      this.vertexBuffer = new float[var10 * 9];
      this.indexBuffer = new int[var13 * 3];
      int var18 = 0;
      int var19 = 0;
      MeshTempState var20 = MeshTempState.getInstance();

      int var21;
      for(var21 = 0; var21 < 3; ++var21) {
         if (var20.triPoints[var21] == null) {
            var20.triPoints[var21] = new Vec3f();
         }

         if (var20.triTexCoords[var21] == null) {
            var20.triTexCoords[var21] = new Vec2f();
         }
      }

      int var22;
      for(var21 = 0; var21 < var13; ++var21) {
         var22 = var21 * var6;

         int var23;
         int var24;
         for(var23 = 0; var23 < 3; ++var23) {
            var24 = var22 + var23 * var5;
            int var25 = var24 + var7;
            int var26 = var24 + var8;
            int var27 = var24 + var9;
            Integer var14 = var19 / 9;
            int var28;
            int var29;
            if (this.vertexBuffer.length <= var19) {
               var28 = var19 / 9;
               var29 = var28 + Math.max(var28 >> 3, 6);
               float[] var30 = new float[var29 * 9];
               System.arraycopy(this.vertexBuffer, 0, var30, 0, this.vertexBuffer.length);
               this.vertexBuffer = var30;
               var30 = new float[var29 * 3];
               System.arraycopy(this.cachedNormals, 0, var30, 0, this.cachedNormals.length);
               this.cachedNormals = var30;
               var30 = new float[var29 * 3];
               System.arraycopy(this.cachedTangents, 0, var30, 0, this.cachedTangents.length);
               this.cachedTangents = var30;
               var30 = new float[var29 * 3];
               System.arraycopy(this.cachedBitangents, 0, var30, 0, this.cachedBitangents.length);
               this.cachedBitangents = var30;
            }

            var28 = var4[var25] * 3;
            var29 = var4[var26] * 3;
            int var32 = var4[var27] * 2;
            var20.triPointIndex[var23] = var28;
            var20.triTexCoordIndex[var23] = var32;
            var20.triVerts[var23] = var19 / 9;
            this.vertexBuffer[var19] = var1[var28];
            this.vertexBuffer[var19 + 1] = var1[var28 + 1];
            this.vertexBuffer[var19 + 2] = var1[var28 + 2];
            this.vertexBuffer[var19 + 3] = var3[var32];
            this.vertexBuffer[var19 + 4] = var3[var32 + 1];
            int var31 = var20.triVerts[var23] * 3;
            this.cachedNormals[var31] = var2[var29];
            this.cachedNormals[var31 + 1] = var2[var29 + 1];
            this.cachedNormals[var31 + 2] = var2[var29 + 2];
            var19 += 9;
            MeshGeomComp2VB var15 = (MeshGeomComp2VB)this.point2vbMap.get(var28);
            if (var15 == null) {
               var15 = new MeshGeomComp2VB(var28, var14);
               this.point2vbMap.put(var28, var15);
            } else {
               var15.addLoc(var14);
            }

            MeshGeomComp2VB var16 = (MeshGeomComp2VB)this.normal2vbMap.get(var29);
            if (var16 == null) {
               var16 = new MeshGeomComp2VB(var29, var14);
               this.normal2vbMap.put(var29, var16);
            } else {
               var16.addLoc(var14);
            }

            MeshGeomComp2VB var17 = (MeshGeomComp2VB)this.texCoord2vbMap.get(var32);
            if (var17 == null) {
               var17 = new MeshGeomComp2VB(var32, var14);
               this.texCoord2vbMap.put(var32, var17);
            } else {
               var17.addLoc(var14);
            }

            this.indexBuffer[var18++] = var14;
         }

         for(var23 = 0; var23 < 3; ++var23) {
            var20.triPoints[var23].x = var1[var20.triPointIndex[var23]];
            var20.triPoints[var23].y = var1[var20.triPointIndex[var23] + 1];
            var20.triPoints[var23].z = var1[var20.triPointIndex[var23] + 2];
            var20.triTexCoords[var23].x = var3[var20.triTexCoordIndex[var23]];
            var20.triTexCoords[var23].y = var3[var20.triTexCoordIndex[var23] + 1];
         }

         MeshUtil.computeTBNNormalized(var20.triPoints[0], var20.triPoints[1], var20.triPoints[2], var20.triTexCoords[0], var20.triTexCoords[1], var20.triTexCoords[2], var20.triNormals);

         for(var23 = 0; var23 < 3; ++var23) {
            var24 = var20.triVerts[var23] * 3;
            this.cachedTangents[var24] = var20.triNormals[1].x;
            this.cachedTangents[var24 + 1] = var20.triNormals[1].y;
            this.cachedTangents[var24 + 2] = var20.triNormals[1].z;
            this.cachedBitangents[var24] = var20.triNormals[2].x;
            this.cachedBitangents[var24 + 1] = var20.triNormals[2].y;
            this.cachedBitangents[var24 + 2] = var20.triNormals[2].z;
         }
      }

      this.numberOfVertices = var19 / 9;
      this.convertNormalsToQuats(var20, this.numberOfVertices, this.cachedNormals, this.cachedTangents, this.cachedBitangents, this.vertexBuffer, (boolean[])null);
      this.indexBufferSize = var13 * 3;
      if (this.numberOfVertices > 65536) {
         return this.buildNativeGeometry(this.vertexBuffer, this.numberOfVertices * 9, this.indexBuffer, this.indexBufferSize);
      } else {
         if (this.indexBufferShort == null || this.indexBufferShort.length < this.indexBufferSize) {
            this.indexBufferShort = new short[this.indexBufferSize];
         }

         var21 = 0;

         for(var22 = 0; var22 < var13; ++var22) {
            this.indexBufferShort[var21] = (short)this.indexBuffer[var21++];
            this.indexBufferShort[var21] = (short)this.indexBuffer[var21++];
            this.indexBufferShort[var21] = (short)this.indexBuffer[var21++];
         }

         this.indexBuffer = null;
         return this.buildNativeGeometry(this.vertexBuffer, this.numberOfVertices * 9, this.indexBufferShort, this.indexBufferSize);
      }
   }

   private boolean updatePNTGeometry(float[] var1, int[] var2, float[] var3, int[] var4, float[] var5, int[] var6) {
      if (this.dirtyVertices == null) {
         this.dirtyVertices = new boolean[this.numberOfVertices];
      }

      Arrays.fill(this.dirtyVertices, false);
      int var7 = var2[0] / 3;
      int var8 = var2[1] / 3;
      if (var2[1] % 3 > 0) {
         ++var8;
      }

      int var9;
      int var10;
      int var14;
      int var15;
      if (var8 > 0) {
         for(var9 = 0; var9 < var8; ++var9) {
            var10 = (var7 + var9) * 3;
            MeshGeomComp2VB var11 = (MeshGeomComp2VB)this.point2vbMap.get(var10);

            assert var11 != null;

            if (var11 != null) {
               int[] var12 = var11.getLocs();
               int var13 = var11.getValidLocs();
               if (var12 != null) {
                  for(var14 = 0; var14 < var13; ++var14) {
                     var15 = var12[var14] * 9;
                     this.vertexBuffer[var15] = var1[var10];
                     this.vertexBuffer[var15 + 1] = var1[var10 + 1];
                     this.vertexBuffer[var15 + 2] = var1[var10 + 2];
                     this.dirtyVertices[var12[var14]] = true;
                  }
               } else {
                  var14 = var11.getLoc();
                  var15 = var14 * 9;
                  this.vertexBuffer[var15] = var1[var10];
                  this.vertexBuffer[var15 + 1] = var1[var10 + 1];
                  this.vertexBuffer[var15 + 2] = var1[var10 + 2];
                  this.dirtyVertices[var14] = true;
               }
            }
         }
      }

      var9 = var6[0] / 2;
      var10 = var6[1] / 2;
      if (var6[1] % 2 > 0) {
         ++var10;
      }

      int var16;
      int var17;
      int var21;
      int var22;
      if (var10 > 0) {
         for(var21 = 0; var21 < var10; ++var21) {
            var22 = (var9 + var21) * 2;
            MeshGeomComp2VB var23 = (MeshGeomComp2VB)this.texCoord2vbMap.get(var22);

            assert var23 != null;

            if (var23 != null) {
               int[] var25 = var23.getLocs();
               var15 = var23.getValidLocs();
               if (var25 != null) {
                  for(var16 = 0; var16 < var15; ++var16) {
                     var17 = var25[var16] * 9 + 3;
                     this.vertexBuffer[var17] = var5[var22];
                     this.vertexBuffer[var17 + 1] = var5[var22 + 1];
                     this.dirtyVertices[var25[var16]] = true;
                  }
               } else {
                  var16 = var23.getLoc();
                  var17 = var16 * 9 + 3;
                  this.vertexBuffer[var17] = var5[var22];
                  this.vertexBuffer[var17 + 1] = var5[var22 + 1];
                  this.dirtyVertices[var16] = true;
               }
            }
         }
      }

      var21 = var4[0] / 3;
      var22 = var4[1] / 3;
      if (var4[1] % 3 > 0) {
         ++var22;
      }

      MeshTempState var24;
      if (var22 > 0) {
         var24 = MeshTempState.getInstance();

         for(var14 = 0; var14 < var22; ++var14) {
            var15 = (var21 + var14) * 3;
            MeshGeomComp2VB var26 = (MeshGeomComp2VB)this.normal2vbMap.get(var15);

            assert var26 != null;

            if (var26 != null) {
               int[] var27 = var26.getLocs();
               int var18 = var26.getValidLocs();
               int var19;
               int var20;
               if (var27 != null) {
                  for(var19 = 0; var19 < var18; ++var19) {
                     var20 = var27[var19] * 3;
                     this.cachedNormals[var20] = var3[var15];
                     this.cachedNormals[var20 + 1] = var3[var15 + 1];
                     this.cachedNormals[var20 + 2] = var3[var15 + 2];
                     this.dirtyVertices[var27[var19]] = true;
                  }
               } else {
                  var19 = var26.getLoc();
                  var20 = var19 * 3;
                  this.cachedNormals[var20] = var3[var15];
                  this.cachedNormals[var20 + 1] = var3[var15 + 1];
                  this.cachedNormals[var20 + 2] = var3[var15 + 2];
                  this.dirtyVertices[var19] = true;
               }
            }
         }
      }

      var24 = MeshTempState.getInstance();

      for(var14 = 0; var14 < 3; ++var14) {
         if (var24.triPoints[var14] == null) {
            var24.triPoints[var14] = new Vec3f();
         }

         if (var24.triTexCoords[var14] == null) {
            var24.triTexCoords[var14] = new Vec2f();
         }
      }

      for(var14 = 0; var14 < this.numberOfVertices; var14 += 3) {
         if (this.dirtyVertices[var14] || this.dirtyVertices[var14 + 1] || this.dirtyVertices[var14 + 2]) {
            var15 = var14 * 9;

            for(var16 = 0; var16 < 3; ++var16) {
               var24.triPoints[var16].x = this.vertexBuffer[var15];
               var24.triPoints[var16].y = this.vertexBuffer[var15 + 1];
               var24.triPoints[var16].z = this.vertexBuffer[var15 + 2];
               var24.triTexCoords[var16].x = this.vertexBuffer[var15 + 3];
               var24.triTexCoords[var16].y = this.vertexBuffer[var15 + 3 + 1];
               var15 += 9;
            }

            MeshUtil.computeTBNNormalized(var24.triPoints[0], var24.triPoints[1], var24.triPoints[2], var24.triTexCoords[0], var24.triTexCoords[1], var24.triTexCoords[2], var24.triNormals);
            var16 = var14 * 3;

            for(var17 = 0; var17 < 3; ++var17) {
               this.cachedTangents[var16] = var24.triNormals[1].x;
               this.cachedTangents[var16 + 1] = var24.triNormals[1].y;
               this.cachedTangents[var16 + 2] = var24.triNormals[1].z;
               this.cachedBitangents[var16] = var24.triNormals[2].x;
               this.cachedBitangents[var16 + 1] = var24.triNormals[2].y;
               this.cachedBitangents[var16 + 2] = var24.triNormals[2].z;
               var16 += 3;
            }
         }
      }

      this.convertNormalsToQuats(var24, this.numberOfVertices, this.cachedNormals, this.cachedTangents, this.cachedBitangents, this.vertexBuffer, this.dirtyVertices);
      if (this.indexBuffer != null) {
         return this.buildNativeGeometry(this.vertexBuffer, this.numberOfVertices * 9, this.indexBuffer, this.indexBufferSize);
      } else {
         return this.buildNativeGeometry(this.vertexBuffer, this.numberOfVertices * 9, this.indexBufferShort, this.indexBufferSize);
      }
   }

   public boolean buildGeometry(boolean var1, float[] var2, int[] var3, float[] var4, int[] var5, float[] var6, int[] var7, int[] var8, int[] var9, int[] var10, int[] var11) {
      return var1 ? this.buildPNTGeometry(var2, var3, var4, var5, var6, var7, var8, var9) : this.buildPTGeometry(var2, var3, var6, var7, var8, var9, var10, var11);
   }

   private boolean buildPNTGeometry(float[] var1, int[] var2, float[] var3, int[] var4, float[] var5, int[] var6, int[] var7, int[] var8) {
      boolean var9 = var2[1] > 0;
      boolean var10 = var4[1] > 0;
      boolean var11 = var6[1] > 0;
      boolean var12 = var8[1] > 0;
      boolean var13 = !var9 && !var10 && !var11 && !var12;
      if (var12) {
         var13 = true;
      }

      return var13 || this.vertexBuffer == null || this.indexBuffer == null && this.indexBufferShort == null ? this.doBuildPNTGeometry(var1, var3, var5, var7) : this.updatePNTGeometry(var1, var2, var3, var4, var5, var6);
   }

   private boolean buildPTGeometry(float[] var1, int[] var2, float[] var3, int[] var4, int[] var5, int[] var6, int[] var7, int[] var8) {
      this.nVerts = var1.length / 3;
      this.nTVerts = var3.length / 2;
      this.nFaces = var5.length / (VertexFormat.POINT_TEXCOORD.getVertexIndexSize() * 3);

      assert this.nVerts > 0 && this.nFaces > 0 && this.nTVerts > 0;

      this.pos = var1;
      this.uv = var3;
      this.faces = var5;
      this.smoothing = var7.length == this.nFaces ? var7 : null;
      if (PrismSettings.skipMeshNormalComputation) {
         boolean var14 = var2[1] > 0;
         boolean var15 = var4[1] > 0;
         boolean var11 = var6[1] > 0;
         boolean var12 = var8[1] > 0;
         boolean var13 = !var14 && !var15 && !var11 && !var12;
         if (var11 || var12) {
            var13 = true;
         }

         return var13 || this.vertexBuffer == null || this.indexBuffer == null && this.indexBufferShort == null ? this.buildSkipMeshNormalGeometry() : this.updateSkipMeshNormalGeometry(var2, var4);
      } else {
         MeshTempState var9 = MeshTempState.getInstance();
         if (var9.pool == null || var9.pool.length < this.nFaces * 3) {
            var9.pool = new MeshVertex[this.nFaces * 3];
         }

         if (var9.indexBuffer == null || var9.indexBuffer.length < this.nFaces * 3) {
            var9.indexBuffer = new int[this.nFaces * 3];
         }

         if (var9.pVertex != null && var9.pVertex.length >= this.nVerts) {
            Arrays.fill(var9.pVertex, 0, var9.pVertex.length, (Object)null);
         } else {
            var9.pVertex = new MeshVertex[this.nVerts];
         }

         this.checkSmoothingGroup();
         this.computeTBNormal(var9.pool, var9.pVertex, var9.indexBuffer);
         int var10 = MeshVertex.processVertices(var9.pVertex, this.nVerts, this.allHardEdges, this.allSameSmoothing);
         if (var9.vertexBuffer == null || var9.vertexBuffer.length < var10 * 9) {
            var9.vertexBuffer = new float[var10 * 9];
         }

         this.buildVertexBuffer(var9.pVertex, var9.vertexBuffer);
         if (var10 > 65536) {
            this.buildIndexBuffer(var9.pool, var9.indexBuffer, (short[])null);
            return this.buildNativeGeometry(var9.vertexBuffer, var10 * 9, var9.indexBuffer, this.nFaces * 3);
         } else {
            if (var9.indexBufferShort == null || var9.indexBufferShort.length < this.nFaces * 3) {
               var9.indexBufferShort = new short[this.nFaces * 3];
            }

            this.buildIndexBuffer(var9.pool, var9.indexBuffer, var9.indexBufferShort);
            return this.buildNativeGeometry(var9.vertexBuffer, var10 * 9, var9.indexBufferShort, this.nFaces * 3);
         }
      }
   }

   private void computeTBNormal(MeshVertex[] var1, MeshVertex[] var2, int[] var3) {
      MeshTempState var4 = MeshTempState.getInstance();
      int[] var5 = var4.smFace;
      int[] var6 = var4.triVerts;
      Vec3f[] var7 = var4.triPoints;
      Vec2f[] var8 = var4.triTexCoords;
      Vec3f[] var9 = var4.triNormals;
      String var10 = BaseMesh.class.getName();
      int var11 = 0;
      int var12 = 0;

      for(int var13 = 0; var11 < this.nFaces; ++var11) {
         int var14 = var11 * 3;
         var5 = this.getFace(var11, var5);
         var6[0] = var5[BaseMesh.FaceMembers.POINT0.ordinal()];
         var6[1] = var5[BaseMesh.FaceMembers.POINT1.ordinal()];
         var6[2] = var5[BaseMesh.FaceMembers.POINT2.ordinal()];
         if (MeshUtil.isDeadFace(var6) && PlatformLogger.getLogger(var10).isLoggable(Level.FINE)) {
            ++var12;
            PlatformLogger.getLogger(var10).fine("Dead face [" + var6[0] + ", " + var6[1] + ", " + var6[2] + "] @ face group " + var11 + "; nEmptyFaces = " + var12);
         }

         int var15;
         for(var15 = 0; var15 < 3; ++var15) {
            var7[var15] = this.getVertex(var6[var15], var7[var15]);
         }

         var6[0] = var5[BaseMesh.FaceMembers.TEXCOORD0.ordinal()];
         var6[1] = var5[BaseMesh.FaceMembers.TEXCOORD1.ordinal()];
         var6[2] = var5[BaseMesh.FaceMembers.TEXCOORD2.ordinal()];

         for(var15 = 0; var15 < 3; ++var15) {
            var8[var15] = this.getTVertex(var6[var15], var8[var15]);
         }

         MeshUtil.computeTBNNormalized(var7[0], var7[1], var7[2], var8[0], var8[1], var8[2], var9);

         for(var15 = 0; var15 < 3; ++var15) {
            var1[var13] = var1[var13] == null ? new MeshVertex() : var1[var13];

            int var16;
            for(var16 = 0; var16 < 3; ++var16) {
               var1[var13].norm[var16].set(var9[var16]);
            }

            var1[var13].smGroup = var5[BaseMesh.FaceMembers.SMOOTHING_GROUP.ordinal()];
            var1[var13].fIdx = var11;
            var1[var13].tVert = var6[var15];
            var1[var13].index = -1;
            var16 = var15 == 0 ? BaseMesh.FaceMembers.POINT0.ordinal() : (var15 == 1 ? BaseMesh.FaceMembers.POINT1.ordinal() : BaseMesh.FaceMembers.POINT2.ordinal());
            int var17 = var5[var16];
            var1[var13].pVert = var17;
            var3[var14 + var15] = var17;
            var1[var13].next = var2[var17];
            var2[var17] = var1[var13];
            ++var13;
         }
      }

   }

   private void buildVSQuat(Vec3f[] var1, Quat4f var2) {
      Vec3f var3 = MeshTempState.getInstance().vec3f1;
      var3.cross(var1[1], var1[2]);
      float var4 = var1[0].dot(var3);
      if (var4 < 0.0F) {
         var1[2].mul(-1.0F);
      }

      MeshUtil.buildQuat(var1, var2);
      if (var4 < 0.0F) {
         if (var2.w == 0.0F) {
            var2.w = 1.0E-10F;
         }

         var2.scale(-1.0F);
      }

   }

   private void buildVertexBuffer(MeshVertex[] var1, float[] var2) {
      Quat4f var3 = MeshTempState.getInstance().quat;
      int var4 = 0;
      int var5 = 0;

      for(int var6 = 0; var5 < this.nVerts; ++var5) {
         for(MeshVertex var7 = var1[var5]; var7 != null; var7 = var7.next) {
            if (var7.index == var4) {
               int var8 = var7.pVert * 3;
               var2[var6++] = this.pos[var8];
               var2[var6++] = this.pos[var8 + 1];
               var2[var6++] = this.pos[var8 + 2];
               var8 = var7.tVert * 2;
               var2[var6++] = this.uv[var8];
               var2[var6++] = this.uv[var8 + 1];
               this.buildVSQuat(var7.norm, var3);
               var2[var6++] = var3.x;
               var2[var6++] = var3.y;
               var2[var6++] = var3.z;
               var2[var6++] = var3.w;
               ++var4;
            }
         }
      }

   }

   private void buildIndexBuffer(MeshVertex[] var1, int[] var2, short[] var3) {
      for(int var4 = 0; var4 < this.nFaces; ++var4) {
         int var5 = var4 * 3;
         int var6;
         if (var2[var5] != -1) {
            for(var6 = 0; var6 < 3; ++var6) {
               assert var1[var5].fIdx == var4;

               if (var3 != null) {
                  var3[var5 + var6] = (short)var1[var5 + var6].index;
               } else {
                  var2[var5 + var6] = var1[var5 + var6].index;
               }

               var1[var5 + var6].next = null;
            }
         } else {
            for(var6 = 0; var6 < 3; ++var6) {
               if (var3 != null) {
                  var3[var5 + var6] = 0;
               } else {
                  var2[var5 + var6] = 0;
               }
            }
         }
      }

   }

   public int getNumVerts() {
      return this.nVerts;
   }

   public int getNumTVerts() {
      return this.nTVerts;
   }

   public int getNumFaces() {
      return this.nFaces;
   }

   public Vec3f getVertex(int var1, Vec3f var2) {
      if (var2 == null) {
         var2 = new Vec3f();
      }

      int var3 = var1 * 3;
      var2.set(this.pos[var3], this.pos[var3 + 1], this.pos[var3 + 2]);
      return var2;
   }

   public Vec2f getTVertex(int var1, Vec2f var2) {
      if (var2 == null) {
         var2 = new Vec2f();
      }

      int var3 = var1 * 2;
      var2.set(this.uv[var3], this.uv[var3 + 1]);
      return var2;
   }

   private void checkSmoothingGroup() {
      if (this.smoothing != null && this.smoothing.length != 0) {
         for(int var1 = 0; var1 + 1 < this.smoothing.length; ++var1) {
            if (this.smoothing[var1] != this.smoothing[var1 + 1]) {
               this.allSameSmoothing = false;
               this.allHardEdges = false;
               return;
            }
         }

         if (this.smoothing[0] == 0) {
            this.allSameSmoothing = false;
            this.allHardEdges = true;
         } else {
            this.allSameSmoothing = true;
            this.allHardEdges = false;
         }

      } else {
         this.allSameSmoothing = true;
         this.allHardEdges = false;
      }
   }

   public int[] getFace(int var1, int[] var2) {
      int var3 = var1 * 6;
      if (var2 == null || var2.length < 7) {
         var2 = new int[7];
      }

      for(int var4 = 0; var4 < 6; ++var4) {
         var2[var4] = this.faces[var3 + var4];
      }

      var2[6] = this.smoothing != null ? this.smoothing[var1] : 1;
      return var2;
   }

   boolean test_isVertexBufferNull() {
      return this.vertexBuffer == null;
   }

   int test_getVertexBufferLength() {
      return this.vertexBuffer.length;
   }

   int test_getNumberOfVertices() {
      return this.numberOfVertices;
   }

   class MeshGeomComp2VB {
      private final int key;
      private final int loc;
      private int[] locs;
      private int validLocs;

      MeshGeomComp2VB(int var2, int var3) {
         assert var3 >= 0;

         this.key = var2;
         this.loc = var3;
         this.locs = null;
         this.validLocs = 0;
      }

      void addLoc(int var1) {
         if (this.locs == null) {
            this.locs = new int[3];
            this.locs[0] = this.loc;
            this.locs[1] = var1;
            this.validLocs = 2;
         } else if (this.locs.length > this.validLocs) {
            this.locs[this.validLocs] = var1;
            ++this.validLocs;
         } else {
            int[] var2 = new int[this.validLocs * 2];
            System.arraycopy(this.locs, 0, var2, 0, this.locs.length);
            this.locs = var2;
            this.locs[this.validLocs] = var1;
            ++this.validLocs;
         }

      }

      int getKey() {
         return this.key;
      }

      int getLoc() {
         return this.loc;
      }

      int[] getLocs() {
         return this.locs;
      }

      int getValidLocs() {
         return this.validLocs;
      }
   }

   public static enum FaceMembers {
      POINT0,
      TEXCOORD0,
      POINT1,
      TEXCOORD1,
      POINT2,
      TEXCOORD2,
      SMOOTHING_GROUP;
   }
}
