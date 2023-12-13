package com.sun.webkit.graphics;

public abstract class WCPageBackBuffer extends Ref {
   public abstract WCGraphicsContext createGraphics();

   public abstract void disposeGraphics(WCGraphicsContext var1);

   public abstract void flush(WCGraphicsContext var1, int var2, int var3, int var4, int var5);

   protected abstract void copyArea(int var1, int var2, int var3, int var4, int var5, int var6);

   public abstract boolean validate(int var1, int var2);
}
