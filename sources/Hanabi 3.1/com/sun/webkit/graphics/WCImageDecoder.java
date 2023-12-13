package com.sun.webkit.graphics;

public abstract class WCImageDecoder {
   protected abstract void addImageData(byte[] var1);

   protected abstract int[] getImageSize();

   protected abstract int getFrameCount();

   protected abstract WCImageFrame getFrame(int var1);

   protected abstract int getFrameDuration(int var1);

   protected abstract int[] getFrameSize(int var1);

   protected abstract boolean getFrameCompleteStatus(int var1);

   protected abstract void loadFromResource(String var1);

   protected abstract void destroy();

   protected abstract String getFilenameExtension();
}
