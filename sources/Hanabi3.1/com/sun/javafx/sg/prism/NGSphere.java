package com.sun.javafx.sg.prism;

public class NGSphere extends NGShape3D {
   public void updateMesh(NGTriangleMesh var1) {
      this.mesh = var1;
      this.invalidate();
   }
}
