package com.sun.webkit.graphics;

public abstract class WCImageFrame extends Ref {
   public abstract WCImage getFrame();

   public abstract int[] getSize();

   protected void destroyDecodedData() {
   }
}
