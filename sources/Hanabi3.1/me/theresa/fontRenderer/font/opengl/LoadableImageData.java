package me.theresa.fontRenderer.font.opengl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public interface LoadableImageData extends ImageData {
   void configureEdging(boolean var1);

   ByteBuffer loadImage(InputStream var1) throws IOException;

   ByteBuffer loadImage(InputStream var1, boolean var2, int[] var3) throws IOException;

   ByteBuffer loadImage(InputStream var1, boolean var2, boolean var3, int[] var4) throws IOException;
}
