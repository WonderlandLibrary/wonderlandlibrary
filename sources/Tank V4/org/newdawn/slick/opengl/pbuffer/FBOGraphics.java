package org.newdawn.slick.opengl.pbuffer;

import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.InternalTextureLoader;
import org.newdawn.slick.opengl.SlickCallable;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.Log;

public class FBOGraphics extends Graphics {
   private Image image;
   private int FBO;
   private boolean valid = true;

   public FBOGraphics(Image var1) throws SlickException {
      super(var1.getTexture().getTextureWidth(), var1.getTexture().getTextureHeight());
      this.image = var1;
      Log.debug("Creating FBO " + var1.getWidth() + "x" + var1.getHeight());
      boolean var2 = GLContext.getCapabilities().GL_EXT_framebuffer_object;
      if (!var2) {
         throw new SlickException("Your OpenGL card does not support FBO and hence can't handle the dynamic images required for this application.");
      } else {
         this.init();
      }
   }

   private void completeCheck() throws SlickException {
      int var1 = EXTFramebufferObject.glCheckFramebufferStatusEXT(36160);
      switch(var1) {
      case 36053:
         return;
      case 36054:
         throw new SlickException("FrameBuffer: " + this.FBO + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT exception");
      case 36055:
         throw new SlickException("FrameBuffer: " + this.FBO + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT exception");
      case 36056:
      default:
         throw new SlickException("Unexpected reply from glCheckFramebufferStatusEXT: " + var1);
      case 36057:
         throw new SlickException("FrameBuffer: " + this.FBO + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT exception");
      case 36058:
         throw new SlickException("FrameBuffer: " + this.FBO + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT exception");
      case 36059:
         throw new SlickException("FrameBuffer: " + this.FBO + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT exception");
      case 36060:
         throw new SlickException("FrameBuffer: " + this.FBO + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT exception");
      }
   }

   private void init() throws SlickException {
      IntBuffer var1 = BufferUtils.createIntBuffer(1);
      EXTFramebufferObject.glGenFramebuffersEXT(var1);
      this.FBO = var1.get();

      try {
         Texture var2 = InternalTextureLoader.get().createTexture(this.image.getWidth(), this.image.getHeight(), this.image.getFilter());
         EXTFramebufferObject.glBindFramebufferEXT(36160, this.FBO);
         EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36064, 3553, var2.getTextureID(), 0);
         this.completeCheck();
         this.unbind();
         this.clear();
         this.flush();
         this.drawImage(this.image, 0.0F, 0.0F);
         this.image.setTexture(var2);
      } catch (Exception var3) {
         throw new SlickException("Failed to create new texture for FBO");
      }
   }

   private void bind() {
      EXTFramebufferObject.glBindFramebufferEXT(36160, this.FBO);
      GL11.glReadBuffer(36064);
   }

   private void unbind() {
      EXTFramebufferObject.glBindFramebufferEXT(36160, 0);
      GL11.glReadBuffer(1029);
   }

   protected void disable() {
      GL.flush();
      this.unbind();
      GL11.glPopClientAttrib();
      GL11.glPopAttrib();
      GL11.glMatrixMode(5888);
      GL11.glPopMatrix();
      GL11.glMatrixMode(5889);
      GL11.glPopMatrix();
      GL11.glMatrixMode(5888);
      SlickCallable.leaveSafeBlock();
   }

   protected void enable() {
      if (!this.valid) {
         throw new RuntimeException("Attempt to use a destroy()ed offscreen graphics context.");
      } else {
         SlickCallable.enterSafeBlock();
         GL11.glPushAttrib(1048575);
         GL11.glPushClientAttrib(-1);
         GL11.glMatrixMode(5889);
         GL11.glPushMatrix();
         GL11.glMatrixMode(5888);
         GL11.glPushMatrix();
         this.bind();
         this.initGL();
      }
   }

   protected void initGL() {
      GL11.glEnable(3553);
      GL11.glShadeModel(7425);
      GL11.glDisable(2929);
      GL11.glDisable(2896);
      GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
      GL11.glClearDepth(1.0D);
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
      GL11.glOrtho(0.0D, (double)this.screenWidth, 0.0D, (double)this.screenHeight, 1.0D, -1.0D);
      GL11.glMatrixMode(5888);
   }

   public void destroy() {
      super.destroy();
      IntBuffer var1 = BufferUtils.createIntBuffer(1);
      var1.put(this.FBO);
      var1.flip();
      EXTFramebufferObject.glDeleteFramebuffersEXT(var1);
      this.valid = false;
   }

   public void flush() {
      super.flush();
      this.image.flushPixelData();
   }
}
