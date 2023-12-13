package com.sun.javafx.sg.prism;

import com.sun.javafx.collections.FloatArraySyncer;
import com.sun.javafx.collections.IntegerArraySyncer;
import com.sun.prism.Mesh;
import com.sun.prism.ResourceFactory;

public class NGTriangleMesh {
   private boolean meshDirty = true;
   private Mesh mesh;
   private boolean userDefinedNormals = false;
   private float[] points;
   private int[] pointsFromAndLengthIndices = new int[2];
   private float[] normals;
   private int[] normalsFromAndLengthIndices = new int[2];
   private float[] texCoords;
   private int[] texCoordsFromAndLengthIndices = new int[2];
   private int[] faces;
   private int[] facesFromAndLengthIndices = new int[2];
   private int[] faceSmoothingGroups;
   private int[] faceSmoothingGroupsFromAndLengthIndices = new int[2];

   Mesh createMesh(ResourceFactory var1) {
      if (this.mesh == null) {
         this.mesh = var1.createMesh();
         this.meshDirty = true;
      }

      return this.mesh;
   }

   boolean validate() {
      if (this.points == null || this.texCoords == null || this.faces == null || this.faceSmoothingGroups == null || this.userDefinedNormals && this.normals == null) {
         return false;
      } else {
         if (this.meshDirty) {
            if (!this.mesh.buildGeometry(this.userDefinedNormals, this.points, this.pointsFromAndLengthIndices, this.normals, this.normalsFromAndLengthIndices, this.texCoords, this.texCoordsFromAndLengthIndices, this.faces, this.facesFromAndLengthIndices, this.faceSmoothingGroups, this.faceSmoothingGroupsFromAndLengthIndices)) {
               throw new RuntimeException("NGTriangleMesh: buildGeometry failed");
            }

            this.meshDirty = false;
         }

         return true;
      }
   }

   void setPointsByRef(float[] var1) {
      this.meshDirty = true;
      this.points = var1;
   }

   void setNormalsByRef(float[] var1) {
      this.meshDirty = true;
      this.normals = var1;
   }

   void setTexCoordsByRef(float[] var1) {
      this.meshDirty = true;
      this.texCoords = var1;
   }

   void setFacesByRef(int[] var1) {
      this.meshDirty = true;
      this.faces = var1;
   }

   void setFaceSmoothingGroupsByRef(int[] var1) {
      this.meshDirty = true;
      this.faceSmoothingGroups = var1;
   }

   public void setUserDefinedNormals(boolean var1) {
      this.userDefinedNormals = var1;
   }

   public boolean isUserDefinedNormals() {
      return this.userDefinedNormals;
   }

   public void syncPoints(FloatArraySyncer var1) {
      this.meshDirty = true;
      this.points = var1 != null ? var1.syncTo(this.points, this.pointsFromAndLengthIndices) : null;
   }

   public void syncNormals(FloatArraySyncer var1) {
      this.meshDirty = true;
      this.normals = var1 != null ? var1.syncTo(this.normals, this.normalsFromAndLengthIndices) : null;
   }

   public void syncTexCoords(FloatArraySyncer var1) {
      this.meshDirty = true;
      this.texCoords = var1 != null ? var1.syncTo(this.texCoords, this.texCoordsFromAndLengthIndices) : null;
   }

   public void syncFaces(IntegerArraySyncer var1) {
      this.meshDirty = true;
      this.faces = var1 != null ? var1.syncTo(this.faces, this.facesFromAndLengthIndices) : null;
   }

   public void syncFaceSmoothingGroups(IntegerArraySyncer var1) {
      this.meshDirty = true;
      this.faceSmoothingGroups = var1 != null ? var1.syncTo(this.faceSmoothingGroups, this.faceSmoothingGroupsFromAndLengthIndices) : null;
   }

   int[] test_getFaceSmoothingGroups() {
      return this.faceSmoothingGroups;
   }

   int[] test_getFaces() {
      return this.faces;
   }

   float[] test_getPoints() {
      return this.points;
   }

   float[] test_getNormals() {
      return this.normals;
   }

   float[] test_getTexCoords() {
      return this.texCoords;
   }

   Mesh test_getMesh() {
      return this.mesh;
   }
}
