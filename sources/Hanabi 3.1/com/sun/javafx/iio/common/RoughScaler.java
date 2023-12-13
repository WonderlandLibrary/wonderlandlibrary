package com.sun.javafx.iio.common;

import java.nio.ByteBuffer;

public class RoughScaler implements PushbroomScaler {
   protected int numBands;
   protected int destWidth;
   protected int destHeight;
   protected double scaleY;
   protected ByteBuffer destBuf;
   protected int[] colPositions;
   protected int sourceLine;
   protected int nextSourceLine;
   protected int destLine;

   public RoughScaler(int var1, int var2, int var3, int var4, int var5) {
      if (var1 > 0 && var2 > 0 && var3 > 0 && var4 > 0 && var5 > 0) {
         this.numBands = var3;
         this.destWidth = var4;
         this.destHeight = var5;
         this.destBuf = ByteBuffer.wrap(new byte[var5 * var4 * var3]);
         double var6 = (double)var1 / (double)var4;
         this.scaleY = (double)var2 / (double)var5;
         this.colPositions = new int[var4];

         for(int var8 = 0; var8 < var4; ++var8) {
            int var9 = (int)(((double)var8 + 0.5) * var6);
            this.colPositions[var8] = var9 * var3;
         }

         this.sourceLine = 0;
         this.destLine = 0;
         this.nextSourceLine = (int)(0.5 * this.scaleY);
      } else {
         throw new IllegalArgumentException();
      }
   }

   public ByteBuffer getDestination() {
      return this.destBuf;
   }

   public boolean putSourceScanline(byte[] var1, int var2) {
      if (var2 < 0) {
         throw new IllegalArgumentException("off < 0!");
      } else {
         if (this.destLine < this.destHeight) {
            if (this.sourceLine == this.nextSourceLine) {
               assert this.destBuf.hasArray() : "destBuf.hasArray() == false => destBuf is direct";

               byte[] var3 = this.destBuf.array();
               int var4 = this.destLine * this.destWidth * this.numBands;
               int var5 = var4;
               int var6 = 0;

               while(true) {
                  if (var6 >= this.destWidth) {
                     while((int)(((double)(++this.destLine) + 0.5) * this.scaleY) == this.sourceLine) {
                        System.arraycopy(var3, var4, var3, var5, this.destWidth * this.numBands);
                        var5 += this.destWidth * this.numBands;
                     }

                     this.nextSourceLine = (int)(((double)this.destLine + 0.5) * this.scaleY);
                     break;
                  }

                  int var7 = var2 + this.colPositions[var6];

                  for(int var8 = 0; var8 < this.numBands; ++var8) {
                     var3[var5++] = var1[var7 + var8];
                  }

                  ++var6;
               }
            }

            ++this.sourceLine;
         }

         return this.destLine == this.destHeight;
      }
   }
}
