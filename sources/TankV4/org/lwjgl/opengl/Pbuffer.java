package org.lwjgl.opengl;

import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.PointerBuffer;
import org.lwjgl.Sys;

public final class Pbuffer extends DrawableGL {
   public static final int PBUFFER_SUPPORTED = 1;
   public static final int RENDER_TEXTURE_SUPPORTED = 2;
   public static final int RENDER_TEXTURE_RECTANGLE_SUPPORTED = 4;
   public static final int RENDER_DEPTH_TEXTURE_SUPPORTED = 8;
   public static final int MIPMAP_LEVEL = 8315;
   public static final int CUBE_MAP_FACE = 8316;
   public static final int TEXTURE_CUBE_MAP_POSITIVE_X = 8317;
   public static final int TEXTURE_CUBE_MAP_NEGATIVE_X = 8318;
   public static final int TEXTURE_CUBE_MAP_POSITIVE_Y = 8319;
   public static final int TEXTURE_CUBE_MAP_NEGATIVE_Y = 8320;
   public static final int TEXTURE_CUBE_MAP_POSITIVE_Z = 8321;
   public static final int TEXTURE_CUBE_MAP_NEGATIVE_Z = 8322;
   public static final int FRONT_LEFT_BUFFER = 8323;
   public static final int FRONT_RIGHT_BUFFER = 8324;
   public static final int BACK_LEFT_BUFFER = 8325;
   public static final int BACK_RIGHT_BUFFER = 8326;
   public static final int DEPTH_BUFFER = 8359;
   private final int width;
   private final int height;

   public Pbuffer(int var1, int var2, PixelFormat var3, Drawable var4) throws LWJGLException {
      this(var1, var2, var3, (RenderTexture)null, var4);
   }

   public Pbuffer(int var1, int var2, PixelFormat var3, RenderTexture var4, Drawable var5) throws LWJGLException {
      this(var1, var2, var3, var4, var5, (ContextAttribs)null);
   }

   public Pbuffer(int var1, int var2, PixelFormat var3, RenderTexture var4, Drawable var5, ContextAttribs var6) throws LWJGLException {
      if (var3 == null) {
         throw new NullPointerException("Pixel format must be non-null");
      } else {
         this.width = var1;
         this.height = var2;
         this.peer_info = createPbuffer(var1, var2, var3, var6, var4);
         Context var7 = null;
         if (var5 == null) {
            var5 = Display.getDrawable();
         }

         if (var5 != null) {
            var7 = ((DrawableLWJGL)var5).getContext();
         }

         this.context = new ContextGL(this.peer_info, var6, (ContextGL)var7);
      }
   }

   private static PeerInfo createPbuffer(int var0, int var1, PixelFormat var2, ContextAttribs var3, RenderTexture var4) throws LWJGLException {
      if (var4 == null) {
         IntBuffer var5 = BufferUtils.createIntBuffer(1);
         return Display.getImplementation().createPbuffer(var0, var1, var2, var3, (IntBuffer)null, var5);
      } else {
         return Display.getImplementation().createPbuffer(var0, var1, var2, var3, var4.pixelFormatCaps, var4.pBufferAttribs);
      }
   }

   public synchronized boolean isBufferLost() {
      this.checkDestroyed();
      return Display.getImplementation().isBufferLost(this.peer_info);
   }

   public static int getCapabilities() {
      return Display.getImplementation().getPbufferCapabilities();
   }

   public synchronized void setAttrib(int var1, int var2) {
      this.checkDestroyed();
      Display.getImplementation().setPbufferAttrib(this.peer_info, var1, var2);
   }

   public synchronized void bindTexImage(int var1) {
      this.checkDestroyed();
      Display.getImplementation().bindTexImageToPbuffer(this.peer_info, var1);
   }

   public synchronized void releaseTexImage(int var1) {
      this.checkDestroyed();
      Display.getImplementation().releaseTexImageFromPbuffer(this.peer_info, var1);
   }

   public synchronized int getHeight() {
      this.checkDestroyed();
      return this.height;
   }

   public synchronized int getWidth() {
      this.checkDestroyed();
      return this.width;
   }

   public void setCLSharingProperties(PointerBuffer var1) throws LWJGLException {
      super.setCLSharingProperties(var1);
   }

   public void destroy() {
      super.destroy();
   }

   public void releaseContext() throws LWJGLException {
      super.releaseContext();
   }

   public void makeCurrent() throws LWJGLException {
      super.makeCurrent();
   }

   public boolean isCurrent() throws LWJGLException {
      return super.isCurrent();
   }

   public void initContext(float var1, float var2, float var3) {
      super.initContext(var1, var2, var3);
   }

   public void swapBuffers() throws LWJGLException {
      super.swapBuffers();
   }

   public void setSwapInterval(int var1) {
      super.setSwapInterval(var1);
   }

   public void checkGLError() {
      super.checkGLError();
   }

   public ContextGL createSharedContext() throws LWJGLException {
      return super.createSharedContext();
   }

   public ContextGL getContext() {
      return super.getContext();
   }

   public PixelFormatLWJGL getPixelFormat() {
      return super.getPixelFormat();
   }

   public void setPixelFormat(PixelFormatLWJGL var1, ContextAttribs var2) throws LWJGLException {
      super.setPixelFormat(var1, var2);
   }

   public void setPixelFormat(PixelFormatLWJGL var1) throws LWJGLException {
      super.setPixelFormat(var1);
   }

   static {
      Sys.initialize();
   }
}
