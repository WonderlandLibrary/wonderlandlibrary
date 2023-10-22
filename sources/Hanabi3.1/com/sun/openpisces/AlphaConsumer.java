package com.sun.openpisces;

public interface AlphaConsumer {
   int getOriginX();

   int getOriginY();

   int getWidth();

   int getHeight();

   void setMaxAlpha(int var1);

   void setAndClearRelativeAlphas(int[] var1, int var2, int var3, int var4);
}
