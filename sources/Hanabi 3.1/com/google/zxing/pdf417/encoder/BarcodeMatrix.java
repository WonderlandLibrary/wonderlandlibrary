package com.google.zxing.pdf417.encoder;

final class BarcodeMatrix {
   private final BarcodeRow[] matrix;
   private int currentRow;
   private final int height;
   private final int width;

   BarcodeMatrix(int height, int width) {
      this.matrix = new BarcodeRow[height + 2];
      int i = 0;

      for(int matrixLength = this.matrix.length; i < matrixLength; ++i) {
         this.matrix[i] = new BarcodeRow((width + 4) * 17 + 1);
      }

      this.width = width * 17;
      this.height = height + 2;
      this.currentRow = 0;
   }

   void set(int x, int y, byte value) {
      this.matrix[y].set(x, value);
   }

   void setMatrix(int x, int y, boolean black) {
      this.set(x, y, (byte)(black ? 1 : 0));
   }

   void startRow() {
      ++this.currentRow;
   }

   BarcodeRow getCurrentRow() {
      return this.matrix[this.currentRow];
   }

   byte[][] getMatrix() {
      return this.getScaledMatrix(1, 1);
   }

   byte[][] getScaledMatrix(int Scale) {
      return this.getScaledMatrix(Scale, Scale);
   }

   byte[][] getScaledMatrix(int xScale, int yScale) {
      byte[][] matrixOut = new byte[this.height * yScale][this.width * xScale];
      int yMax = this.height * yScale;

      for(int ii = 0; ii < yMax; ++ii) {
         matrixOut[yMax - ii - 1] = this.matrix[ii / yScale].getScaledRow(xScale);
      }

      return matrixOut;
   }
}
