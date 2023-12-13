package org.newdawn.slick;

import java.io.IOException;
import java.io.InputStream;
import org.lwjgl.opengl.EXTSecondaryColor;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.newdawn.slick.opengl.ImageData;
import org.newdawn.slick.opengl.InternalTextureLoader;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.opengl.pbuffer.GraphicsFactory;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;
import org.newdawn.slick.util.Log;

public class Image implements Renderable {
   public static final int TOP_LEFT = 0;
   public static final int TOP_RIGHT = 1;
   public static final int BOTTOM_RIGHT = 2;
   public static final int BOTTOM_LEFT = 3;
   protected static SGL GL = Renderer.get();
   protected static Image inUse;
   public static final int FILTER_LINEAR = 1;
   public static final int FILTER_NEAREST = 2;
   protected Texture texture;
   protected int width;
   protected int height;
   protected float textureWidth;
   protected float textureHeight;
   protected float textureOffsetX;
   protected float textureOffsetY;
   protected float angle;
   protected float alpha;
   protected String ref;
   protected boolean inited;
   protected byte[] pixelData;
   protected boolean destroyed;
   protected float centerX;
   protected float centerY;
   protected String name;
   protected Color[] corners;
   private int filter;
   private boolean flipped;
   private Color transparent;

   protected Image(Image var1) {
      this.alpha = 1.0F;
      this.inited = false;
      this.filter = 9729;
      this.width = var1.getWidth();
      this.height = var1.getHeight();
      this.texture = var1.texture;
      this.textureWidth = var1.textureWidth;
      this.textureHeight = var1.textureHeight;
      this.ref = var1.ref;
      this.textureOffsetX = var1.textureOffsetX;
      this.textureOffsetY = var1.textureOffsetY;
      this.centerX = (float)(this.width / 2);
      this.centerY = (float)(this.height / 2);
      this.inited = true;
   }

   protected Image() {
      this.alpha = 1.0F;
      this.inited = false;
      this.filter = 9729;
   }

   public Image(Texture var1) {
      this.alpha = 1.0F;
      this.inited = false;
      this.filter = 9729;
      this.texture = var1;
      this.ref = var1.toString();
      this.clampTexture();
   }

   public Image(String var1) throws SlickException {
      this(var1, false);
   }

   public Image(String var1, Color var2) throws SlickException {
      this(var1, false, 1, var2);
   }

   public Image(String var1, boolean var2) throws SlickException {
      this(var1, var2, 1);
   }

   public Image(String var1, boolean var2, int var3) throws SlickException {
      this(var1, var2, var3, (Color)null);
   }

   public Image(String var1, boolean var2, int var3, Color var4) throws SlickException {
      this.alpha = 1.0F;
      this.inited = false;
      this.filter = 9729;
      this.filter = var3 == 1 ? 9729 : 9728;
      this.transparent = var4;
      this.flipped = var2;

      try {
         this.ref = var1;
         int[] var5 = null;
         if (var4 != null) {
            var5 = new int[]{(int)(var4.r * 255.0F), (int)(var4.g * 255.0F), (int)(var4.b * 255.0F)};
         }

         this.texture = InternalTextureLoader.get().getTexture(var1, var2, this.filter, var5);
      } catch (IOException var6) {
         Log.error((Throwable)var6);
         throw new SlickException("Failed to load image from: " + var1, var6);
      }
   }

   public void setFilter(int var1) throws SlickException {
      this.filter = var1 == 1 ? 9729 : 9728;

      try {
         int[] var2 = null;
         if (this.transparent != null) {
            var2 = new int[]{(int)(this.transparent.r * 255.0F), (int)(this.transparent.g * 255.0F), (int)(this.transparent.b * 255.0F)};
         }

         this.texture = InternalTextureLoader.get().getTexture(this.ref, this.flipped, this.filter, var2);
      } catch (IOException var3) {
         Log.error((Throwable)var3);
         throw new SlickException("Failed to load image from: " + this.ref, var3);
      }
   }

   public Image(int var1, int var2) throws SlickException {
      this.alpha = 1.0F;
      this.inited = false;
      this.filter = 9729;
      this.ref = super.toString();

      try {
         this.texture = InternalTextureLoader.get().createTexture(var1, var2);
      } catch (IOException var4) {
         Log.error((Throwable)var4);
         throw new SlickException("Failed to create empty image " + var1 + "x" + var2);
      }

      this.init();
   }

   public Image(InputStream var1, String var2, boolean var3) throws SlickException {
      this(var1, var2, var3, 1);
   }

   public Image(InputStream var1, String var2, boolean var3, int var4) throws SlickException {
      this.alpha = 1.0F;
      this.inited = false;
      this.filter = 9729;
      this.load(var1, var2, var3, var4, (Color)null);
   }

   Image(ImageBuffer var1) {
      this((ImageBuffer)var1, 1);
      TextureImpl.bindNone();
   }

   Image(ImageBuffer var1, int var2) {
      this((ImageData)var1, var2);
      TextureImpl.bindNone();
   }

   public Image(ImageData var1) {
      this((ImageData)var1, 1);
   }

   public Image(ImageData var1, int var2) {
      this.alpha = 1.0F;
      this.inited = false;
      this.filter = 9729;

      try {
         this.filter = var2 == 1 ? 9729 : 9728;
         this.texture = InternalTextureLoader.get().getTexture(var1, this.filter);
         this.ref = this.texture.toString();
      } catch (IOException var4) {
         Log.error((Throwable)var4);
      }

   }

   public int getFilter() {
      return this.filter;
   }

   public String getResourceReference() {
      return this.ref;
   }

   public void setColor(int var1, float var2, float var3, float var4, float var5) {
      if (this.corners == null) {
         this.corners = new Color[]{new Color(1.0F, 1.0F, 1.0F, 1.0F), new Color(1.0F, 1.0F, 1.0F, 1.0F), new Color(1.0F, 1.0F, 1.0F, 1.0F), new Color(1.0F, 1.0F, 1.0F, 1.0F)};
      }

      this.corners[var1].r = var2;
      this.corners[var1].g = var3;
      this.corners[var1].b = var4;
      this.corners[var1].a = var5;
   }

   public void clampTexture() {
      if (GLContext.getCapabilities().GL_EXT_texture_mirror_clamp) {
         GL11.glTexParameteri(3553, 10242, 34627);
         GL11.glTexParameteri(3553, 10243, 34627);
      } else {
         GL11.glTexParameteri(3553, 10242, 10496);
         GL11.glTexParameteri(3553, 10243, 10496);
      }

   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getName() {
      return this.name;
   }

   public Graphics getGraphics() throws SlickException {
      return GraphicsFactory.getGraphicsForImage(this);
   }

   private void load(InputStream var1, String var2, boolean var3, int var4, Color var5) throws SlickException {
      this.filter = var4 == 1 ? 9729 : 9728;

      try {
         this.ref = var2;
         int[] var6 = null;
         if (var5 != null) {
            var6 = new int[]{(int)(var5.r * 255.0F), (int)(var5.g * 255.0F), (int)(var5.b * 255.0F)};
         }

         this.texture = InternalTextureLoader.get().getTexture(var1, var2, var3, this.filter, var6);
      } catch (IOException var7) {
         Log.error((Throwable)var7);
         throw new SlickException("Failed to load image from: " + var2, var7);
      }
   }

   public void bind() {
      this.texture.bind();
   }

   protected void reinit() {
      this.inited = false;
      this.init();
   }

   protected final void init() {
      if (!this.inited) {
         this.inited = true;
         if (this.texture != null) {
            this.width = this.texture.getImageWidth();
            this.height = this.texture.getImageHeight();
            this.textureOffsetX = 0.0F;
            this.textureOffsetY = 0.0F;
            this.textureWidth = this.texture.getWidth();
            this.textureHeight = this.texture.getHeight();
         }

         this.initImpl();
         this.centerX = (float)(this.width / 2);
         this.centerY = (float)(this.height / 2);
      }
   }

   protected void initImpl() {
   }

   public void draw() {
      this.draw(0.0F, 0.0F);
   }

   public void drawCentered(float var1, float var2) {
      this.draw(var1 - (float)(this.getWidth() / 2), var2 - (float)(this.getHeight() / 2));
   }

   public void draw(float var1, float var2) {
      this.init();
      this.draw(var1, var2, (float)this.width, (float)this.height);
   }

   public void draw(float var1, float var2, Color var3) {
      this.init();
      this.draw(var1, var2, (float)this.width, (float)this.height, var3);
   }

   public void drawEmbedded(float var1, float var2, float var3, float var4) {
      this.init();
      if (this.corners == null) {
         GL.glTexCoord2f(this.textureOffsetX, this.textureOffsetY);
         GL.glVertex3f(var1, var2, 0.0F);
         GL.glTexCoord2f(this.textureOffsetX, this.textureOffsetY + this.textureHeight);
         GL.glVertex3f(var1, var2 + var4, 0.0F);
         GL.glTexCoord2f(this.textureOffsetX + this.textureWidth, this.textureOffsetY + this.textureHeight);
         GL.glVertex3f(var1 + var3, var2 + var4, 0.0F);
         GL.glTexCoord2f(this.textureOffsetX + this.textureWidth, this.textureOffsetY);
         GL.glVertex3f(var1 + var3, var2, 0.0F);
      } else {
         this.corners[0].bind();
         GL.glTexCoord2f(this.textureOffsetX, this.textureOffsetY);
         GL.glVertex3f(var1, var2, 0.0F);
         this.corners[3].bind();
         GL.glTexCoord2f(this.textureOffsetX, this.textureOffsetY + this.textureHeight);
         GL.glVertex3f(var1, var2 + var4, 0.0F);
         this.corners[2].bind();
         GL.glTexCoord2f(this.textureOffsetX + this.textureWidth, this.textureOffsetY + this.textureHeight);
         GL.glVertex3f(var1 + var3, var2 + var4, 0.0F);
         this.corners[1].bind();
         GL.glTexCoord2f(this.textureOffsetX + this.textureWidth, this.textureOffsetY);
         GL.glVertex3f(var1 + var3, var2, 0.0F);
      }

   }

   public float getTextureOffsetX() {
      this.init();
      return this.textureOffsetX;
   }

   public float getTextureOffsetY() {
      this.init();
      return this.textureOffsetY;
   }

   public float getTextureWidth() {
      this.init();
      return this.textureWidth;
   }

   public float getTextureHeight() {
      this.init();
      return this.textureHeight;
   }

   public void draw(float var1, float var2, float var3) {
      this.init();
      this.draw(var1, var2, (float)this.width * var3, (float)this.height * var3, Color.white);
   }

   public void draw(float var1, float var2, float var3, Color var4) {
      this.init();
      this.draw(var1, var2, (float)this.width * var3, (float)this.height * var3, var4);
   }

   public void draw(float var1, float var2, float var3, float var4) {
      this.init();
      this.draw(var1, var2, var3, var4, Color.white);
   }

   public void drawSheared(float var1, float var2, float var3, float var4) {
      Color.white.bind();
      this.texture.bind();
      GL.glTranslatef(var1, var2, 0.0F);
      if (this.angle != 0.0F) {
         GL.glTranslatef(this.centerX, this.centerY, 0.0F);
         GL.glRotatef(this.angle, 0.0F, 0.0F, 1.0F);
         GL.glTranslatef(-this.centerX, -this.centerY, 0.0F);
      }

      GL.glBegin(7);
      this.init();
      GL.glTexCoord2f(this.textureOffsetX, this.textureOffsetY);
      GL.glVertex3f(0.0F, 0.0F, 0.0F);
      GL.glTexCoord2f(this.textureOffsetX, this.textureOffsetY + this.textureHeight);
      GL.glVertex3f(var3, (float)this.height, 0.0F);
      GL.glTexCoord2f(this.textureOffsetX + this.textureWidth, this.textureOffsetY + this.textureHeight);
      GL.glVertex3f((float)this.width + var3, (float)this.height + var4, 0.0F);
      GL.glTexCoord2f(this.textureOffsetX + this.textureWidth, this.textureOffsetY);
      GL.glVertex3f((float)this.width, var4, 0.0F);
      GL.glEnd();
      if (this.angle != 0.0F) {
         GL.glTranslatef(this.centerX, this.centerY, 0.0F);
         GL.glRotatef(-this.angle, 0.0F, 0.0F, 1.0F);
         GL.glTranslatef(-this.centerX, -this.centerY, 0.0F);
      }

      GL.glTranslatef(-var1, -var2, 0.0F);
   }

   public void draw(float var1, float var2, float var3, float var4, Color var5) {
      if (this.alpha != 1.0F) {
         if (var5 == null) {
            var5 = Color.white;
         }

         var5 = new Color(var5);
         var5.a *= this.alpha;
      }

      if (var5 != null) {
         var5.bind();
      }

      this.texture.bind();
      GL.glTranslatef(var1, var2, 0.0F);
      if (this.angle != 0.0F) {
         GL.glTranslatef(this.centerX, this.centerY, 0.0F);
         GL.glRotatef(this.angle, 0.0F, 0.0F, 1.0F);
         GL.glTranslatef(-this.centerX, -this.centerY, 0.0F);
      }

      GL.glBegin(7);
      this.drawEmbedded(0.0F, 0.0F, var3, var4);
      GL.glEnd();
      if (this.angle != 0.0F) {
         GL.glTranslatef(this.centerX, this.centerY, 0.0F);
         GL.glRotatef(-this.angle, 0.0F, 0.0F, 1.0F);
         GL.glTranslatef(-this.centerX, -this.centerY, 0.0F);
      }

      GL.glTranslatef(-var1, -var2, 0.0F);
   }

   public void drawFlash(float var1, float var2, float var3, float var4) {
      this.drawFlash(var1, var2, var3, var4, Color.white);
   }

   public void setCenterOfRotation(float var1, float var2) {
      this.centerX = var1;
      this.centerY = var2;
   }

   public float getCenterOfRotationX() {
      this.init();
      return this.centerX;
   }

   public float getCenterOfRotationY() {
      this.init();
      return this.centerY;
   }

   public void drawFlash(float var1, float var2, float var3, float var4, Color var5) {
      this.init();
      var5.bind();
      this.texture.bind();
      if (GLContext.getCapabilities().GL_EXT_secondary_color) {
         GL.glEnable(33880);
         EXTSecondaryColor.glSecondaryColor3ubEXT((byte)((int)(var5.r * 255.0F)), (byte)((int)(var5.g * 255.0F)), (byte)((int)(var5.b * 255.0F)));
      }

      GL.glTexEnvi(8960, 8704, 8448);
      GL.glTranslatef(var1, var2, 0.0F);
      if (this.angle != 0.0F) {
         GL.glTranslatef(this.centerX, this.centerY, 0.0F);
         GL.glRotatef(this.angle, 0.0F, 0.0F, 1.0F);
         GL.glTranslatef(-this.centerX, -this.centerY, 0.0F);
      }

      GL.glBegin(7);
      this.drawEmbedded(0.0F, 0.0F, var3, var4);
      GL.glEnd();
      if (this.angle != 0.0F) {
         GL.glTranslatef(this.centerX, this.centerY, 0.0F);
         GL.glRotatef(-this.angle, 0.0F, 0.0F, 1.0F);
         GL.glTranslatef(-this.centerX, -this.centerY, 0.0F);
      }

      GL.glTranslatef(-var1, -var2, 0.0F);
      if (GLContext.getCapabilities().GL_EXT_secondary_color) {
         GL.glDisable(33880);
      }

   }

   public void drawFlash(float var1, float var2) {
      this.drawFlash(var1, var2, (float)this.getWidth(), (float)this.getHeight());
   }

   public void setRotation(float var1) {
      this.angle = var1 % 360.0F;
   }

   public float getRotation() {
      return this.angle;
   }

   public float getAlpha() {
      return this.alpha;
   }

   public void setAlpha(float var1) {
      this.alpha = var1;
   }

   public void rotate(float var1) {
      this.angle += var1;
      this.angle %= 360.0F;
   }

   public Image getSubImage(int var1, int var2, int var3, int var4) {
      this.init();
      float var5 = (float)var1 / (float)this.width * this.textureWidth + this.textureOffsetX;
      float var6 = (float)var2 / (float)this.height * this.textureHeight + this.textureOffsetY;
      float var7 = (float)var3 / (float)this.width * this.textureWidth;
      float var8 = (float)var4 / (float)this.height * this.textureHeight;
      Image var9 = new Image();
      var9.inited = true;
      var9.texture = this.texture;
      var9.textureOffsetX = var5;
      var9.textureOffsetY = var6;
      var9.textureWidth = var7;
      var9.textureHeight = var8;
      var9.width = var3;
      var9.height = var4;
      var9.ref = this.ref;
      var9.centerX = (float)(var3 / 2);
      var9.centerY = (float)(var4 / 2);
      return var9;
   }

   public void draw(float var1, float var2, float var3, float var4, float var5, float var6) {
      this.draw(var1, var2, var1 + (float)this.width, var2 + (float)this.height, var3, var4, var5, var6);
   }

   public void draw(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      this.draw(var1, var2, var3, var4, var5, var6, var7, var8, Color.white);
   }

   public void draw(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, Color var9) {
      this.init();
      if (this.alpha != 1.0F) {
         if (var9 == null) {
            var9 = Color.white;
         }

         var9 = new Color(var9);
         var9.a *= this.alpha;
      }

      var9.bind();
      this.texture.bind();
      GL.glTranslatef(var1, var2, 0.0F);
      if (this.angle != 0.0F) {
         GL.glTranslatef(this.centerX, this.centerY, 0.0F);
         GL.glRotatef(this.angle, 0.0F, 0.0F, 1.0F);
         GL.glTranslatef(-this.centerX, -this.centerY, 0.0F);
      }

      GL.glBegin(7);
      this.drawEmbedded(0.0F, 0.0F, var3 - var1, var4 - var2, var5, var6, var7, var8);
      GL.glEnd();
      if (this.angle != 0.0F) {
         GL.glTranslatef(this.centerX, this.centerY, 0.0F);
         GL.glRotatef(-this.angle, 0.0F, 0.0F, 1.0F);
         GL.glTranslatef(-this.centerX, -this.centerY, 0.0F);
      }

      GL.glTranslatef(-var1, -var2, 0.0F);
   }

   public void drawEmbedded(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      this.drawEmbedded(var1, var2, var3, var4, var5, var6, var7, var8, (Color)null);
   }

   public void drawEmbedded(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, Color var9) {
      if (var9 != null) {
         var9.bind();
      }

      float var10 = var3 - var1;
      float var11 = var4 - var2;
      float var12 = var7 - var5;
      float var13 = var8 - var6;
      float var14 = var5 / (float)this.width * this.textureWidth + this.textureOffsetX;
      float var15 = var6 / (float)this.height * this.textureHeight + this.textureOffsetY;
      float var16 = var12 / (float)this.width * this.textureWidth;
      float var17 = var13 / (float)this.height * this.textureHeight;
      GL.glTexCoord2f(var14, var15);
      GL.glVertex3f(var1, var2, 0.0F);
      GL.glTexCoord2f(var14, var15 + var17);
      GL.glVertex3f(var1, var2 + var11, 0.0F);
      GL.glTexCoord2f(var14 + var16, var15 + var17);
      GL.glVertex3f(var1 + var10, var2 + var11, 0.0F);
      GL.glTexCoord2f(var14 + var16, var15);
      GL.glVertex3f(var1 + var10, var2, 0.0F);
   }

   public int getWidth() {
      this.init();
      return this.width;
   }

   public int getHeight() {
      this.init();
      return this.height;
   }

   public Image copy() {
      this.init();
      return this.getSubImage(0, 0, this.width, this.height);
   }

   public Image getScaledCopy(float var1) {
      this.init();
      return this.getScaledCopy((int)((float)this.width * var1), (int)((float)this.height * var1));
   }

   public Image getScaledCopy(int var1, int var2) {
      this.init();
      Image var3 = this.copy();
      var3.width = var1;
      var3.height = var2;
      var3.centerX = (float)(var1 / 2);
      var3.centerY = (float)(var2 / 2);
      return var3;
   }

   public void ensureInverted() {
      if (this.textureHeight > 0.0F) {
         this.textureOffsetY += this.textureHeight;
         this.textureHeight = -this.textureHeight;
      }

   }

   public Image getFlippedCopy(boolean var1, boolean var2) {
      this.init();
      Image var3 = this.copy();
      if (var1) {
         var3.textureOffsetX = this.textureOffsetX + this.textureWidth;
         var3.textureWidth = -this.textureWidth;
      }

      if (var2) {
         var3.textureOffsetY = this.textureOffsetY + this.textureHeight;
         var3.textureHeight = -this.textureHeight;
      }

      return var3;
   }

   public void endUse() {
      if (inUse != this) {
         throw new RuntimeException("The sprite sheet is not currently in use");
      } else {
         inUse = null;
         GL.glEnd();
      }
   }

   public void startUse() {
      if (inUse != null) {
         throw new RuntimeException("Attempt to start use of a sprite sheet before ending use with another - see endUse()");
      } else {
         inUse = this;
         this.init();
         Color.white.bind();
         this.texture.bind();
         GL.glBegin(7);
      }
   }

   public String toString() {
      this.init();
      return "[Image " + this.ref + " " + this.width + "x" + this.height + "  " + this.textureOffsetX + "," + this.textureOffsetY + "," + this.textureWidth + "," + this.textureHeight + "]";
   }

   public Texture getTexture() {
      return this.texture;
   }

   public void setTexture(Texture var1) {
      this.texture = var1;
      this.reinit();
   }

   private int translate(byte var1) {
      return var1 < 0 ? 256 + var1 : var1;
   }

   public Color getColor(int var1, int var2) {
      if (this.pixelData == null) {
         this.pixelData = this.texture.getTextureData();
      }

      int var3 = (int)(this.textureOffsetX * (float)this.texture.getTextureWidth());
      int var4 = (int)(this.textureOffsetY * (float)this.texture.getTextureHeight());
      if (this.textureWidth < 0.0F) {
         var1 = var3 - var1;
      } else {
         var1 += var3;
      }

      if (this.textureHeight < 0.0F) {
         var2 = var4 - var2;
      } else {
         var2 += var4;
      }

      int var5 = var1 + var2 * this.texture.getTextureWidth();
      var5 *= this.texture.hasAlpha() ? 4 : 3;
      return this.texture.hasAlpha() ? new Color(this.translate(this.pixelData[var5]), this.translate(this.pixelData[var5 + 1]), this.translate(this.pixelData[var5 + 2]), this.translate(this.pixelData[var5 + 3])) : new Color(this.translate(this.pixelData[var5]), this.translate(this.pixelData[var5 + 1]), this.translate(this.pixelData[var5 + 2]));
   }

   public boolean isDestroyed() {
      return this.destroyed;
   }

   public void destroy() throws SlickException {
      if (!this.isDestroyed()) {
         this.destroyed = true;
         this.texture.release();
         GraphicsFactory.releaseGraphicsForImage(this);
      }
   }

   public void flushPixelData() {
      this.pixelData = null;
   }
}
