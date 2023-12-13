package com.sun.javafx.tk.quantum;

import com.sun.javafx.geom.PathIterator;

class PathIteratorHelper {
   private PathIterator itr;
   private float[] f = new float[6];

   public PathIteratorHelper(PathIterator var1) {
      this.itr = var1;
   }

   public int getWindingRule() {
      return this.itr.getWindingRule();
   }

   public boolean isDone() {
      return this.itr.isDone();
   }

   public void next() {
      this.itr.next();
   }

   public int currentSegment(Struct var1) {
      int var2 = this.itr.currentSegment(this.f);
      var1.f0 = this.f[0];
      var1.f1 = this.f[1];
      var1.f2 = this.f[2];
      var1.f3 = this.f[3];
      var1.f4 = this.f[4];
      var1.f5 = this.f[5];
      return var2;
   }

   public static final class Struct {
      float f0;
      float f1;
      float f2;
      float f3;
      float f4;
      float f5;
   }
}
