package com.sun.javafx.geom;

public final class Edge {
   static final int INIT_PARTS = 4;
   static final int GROW_PARTS = 10;
   Curve curve;
   int ctag;
   int etag;
   double activey;
   int equivalence;
   private Edge lastEdge;
   private int lastResult;
   private double lastLimit;

   public Edge(Curve var1, int var2) {
      this(var1, var2, 0);
   }

   public Edge(Curve var1, int var2, int var3) {
      this.curve = var1;
      this.ctag = var2;
      this.etag = var3;
   }

   public Curve getCurve() {
      return this.curve;
   }

   public int getCurveTag() {
      return this.ctag;
   }

   public int getEdgeTag() {
      return this.etag;
   }

   public void setEdgeTag(int var1) {
      this.etag = var1;
   }

   public int getEquivalence() {
      return this.equivalence;
   }

   public void setEquivalence(int var1) {
      this.equivalence = var1;
   }

   public int compareTo(Edge var1, double[] var2) {
      if (var1 == this.lastEdge && var2[0] < this.lastLimit) {
         if (var2[1] > this.lastLimit) {
            var2[1] = this.lastLimit;
         }

         return this.lastResult;
      } else if (this == var1.lastEdge && var2[0] < var1.lastLimit) {
         if (var2[1] > var1.lastLimit) {
            var2[1] = var1.lastLimit;
         }

         return 0 - var1.lastResult;
      } else {
         int var3 = this.curve.compareTo(var1.curve, var2);
         this.lastEdge = var1;
         this.lastLimit = var2[1];
         this.lastResult = var3;
         return var3;
      }
   }

   public void record(double var1, int var3) {
      this.activey = var1;
      this.etag = var3;
   }

   public boolean isActiveFor(double var1, int var3) {
      return this.etag == var3 && this.activey >= var1;
   }

   public String toString() {
      return "Edge[" + this.curve + ", " + (this.ctag == 0 ? "L" : "R") + ", " + (this.etag == 1 ? "I" : (this.etag == -1 ? "O" : "N")) + "]";
   }
}
