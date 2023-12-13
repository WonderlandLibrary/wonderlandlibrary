package me.theresa.fontRenderer.font.opengl.pbuffer;

import me.theresa.fontRenderer.font.Graphics;
import me.theresa.fontRenderer.font.Image;
import me.theresa.fontRenderer.font.SlickException;
import me.theresa.fontRenderer.font.log.Log;
import me.theresa.fontRenderer.font.opengl.InternalTextureLoader;
import me.theresa.fontRenderer.font.opengl.SlickCallable;
import me.theresa.fontRenderer.font.opengl.Texture;
import me.theresa.fontRenderer.font.opengl.TextureImpl;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.Drawable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.Pbuffer;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.opengl.RenderTexture;

public class PBufferGraphics extends Graphics {
   private Pbuffer pbuffer;
   private final Image image;

   public PBufferGraphics(Image image) throws SlickException {
      super(image.getTexture().getTextureWidth(), image.getTexture().getTextureHeight());
      this.image = image;
      Log.debug("Creating pbuffer(rtt) " + image.getWidth() + "x" + image.getHeight());
      if ((Pbuffer.getCapabilities() & 1) == 0) {
         throw new SlickException("Your OpenGL card does not support PBuffers and hence can't handle the dynamic images required for this application.");
      } else if ((Pbuffer.getCapabilities() & 2) == 0) {
         throw new SlickException("Your OpenGL card does not support Render-To-Texture and hence can't handle the dynamic images required for this application.");
      } else {
         this.init();
      }
   }

   private void init() throws SlickException {
      try {
         Texture tex = InternalTextureLoader.get().createTexture(this.image.getWidth(), this.image.getHeight(), this.image.getFilter());
         RenderTexture rt = new RenderTexture(false, true, false, false, 8314, 0);
         this.pbuffer = new Pbuffer(this.screenWidth, this.screenHeight, new PixelFormat(8, 0, 0), rt, (Drawable)null);
         this.pbuffer.makeCurrent();
         this.initGL();
         GL.glBindTexture(3553, tex.getTextureID());
         this.pbuffer.releaseTexImage(8323);
         this.image.draw(0.0F, 0.0F);
         this.image.setTexture(tex);
         Display.makeCurrent();
      } catch (Exception var3) {
         Log.error((Throwable)var3);
         throw new SlickException("Failed to create PBuffer for dynamic image. OpenGL driver failure?");
      }
   }

   protected void disable() {
      GL.flush();
      GL.glBindTexture(3553, this.image.getTexture().getTextureID());
      this.pbuffer.bindTexImage(8323);

      try {
         Display.makeCurrent();
      } catch (LWJGLException var2) {
         Log.error((Throwable)var2);
      }

      SlickCallable.leaveSafeBlock();
   }

   protected void enable() {
      SlickCallable.enterSafeBlock();

      try {
         if (this.pbuffer.isBufferLost()) {
            this.pbuffer.destroy();
            this.init();
         }

         this.pbuffer.makeCurrent();
      } catch (Exception var2) {
         Log.error("Failed to recreate the PBuffer");
         throw new RuntimeException(var2);
      }

      GL.glBindTexture(3553, this.image.getTexture().getTextureID());
      this.pbuffer.releaseTexImage(8323);
      TextureImpl.unbind();
      this.initGL();
   }

   protected void initGL() {
      GL11.glEnable(3553);
      GL11.glShadeModel(7425);
      GL11.glDisable(2929);
      GL11.glDisable(2896);
      GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
      GL11.glClearDepth(1.0);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glViewport(0, 0, this.screenWidth, this.screenHeight);
      GL11.glMatrixMode(5888);
      GL11.glLoadIdentity();
      this.enterOrtho();
   }

   protected void enterOrtho() {
      GL11.glMatrixMode(5889);
      GL11.glLoadIdentity();
      GL11.glOrtho(0.0, (double)this.screenWidth, 0.0, (double)this.screenHeight, 1.0, -1.0);
      GL11.glMatrixMode(5888);
   }

   public void destroy() {
      super.destroy();
      this.pbuffer.destroy();
   }

   public void flush() {
      super.flush();
      this.image.flushPixelData();
   }
}
