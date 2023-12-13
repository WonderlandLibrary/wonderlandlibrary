package com.sun.prism.sw;

import com.sun.openpisces.AlphaConsumer;
import com.sun.openpisces.Renderer;
import com.sun.pisces.PiscesRenderer;

final class DirectRTPiscesAlphaConsumer implements AlphaConsumer {
   private byte[] alpha_map;
   private int outpix_xmin;
   private int outpix_ymin;
   private int w;
   private int h;
   private int rowNum;
   private PiscesRenderer pr;

   void initConsumer(Renderer var1, PiscesRenderer var2) {
      this.outpix_xmin = var1.getOutpixMinX();
      this.outpix_ymin = var1.getOutpixMinY();
      this.w = var1.getOutpixMaxX() - this.outpix_xmin;
      if (this.w < 0) {
         this.w = 0;
      }

      this.h = var1.getOutpixMaxY() - this.outpix_ymin;
      if (this.h < 0) {
         this.h = 0;
      }

      this.rowNum = 0;
      this.pr = var2;
   }

   public int getOriginX() {
      return this.outpix_xmin;
   }

   public int getOriginY() {
      return this.outpix_ymin;
   }

   public int getWidth() {
      return this.w;
   }

   public int getHeight() {
      return this.h;
   }

   public void setMaxAlpha(int var1) {
      if (this.alpha_map == null || this.alpha_map.length != var1 + 1) {
         this.alpha_map = new byte[var1 + 1];

         for(int var2 = 0; var2 <= var1; ++var2) {
            this.alpha_map[var2] = (byte)((var2 * 255 + var1 / 2) / var1);
         }
      }

   }

   public void setAndClearRelativeAlphas(int[] var1, int var2, int var3, int var4) {
      this.pr.emitAndClearAlphaRow(this.alpha_map, var1, var2, var3, var4, this.rowNum);
      ++this.rowNum;
   }
}
