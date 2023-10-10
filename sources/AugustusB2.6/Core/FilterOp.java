// 
// Decompiled by Procyon v0.5.36
// 

package Core;

import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL15;
import java.awt.image.WritableRaster;
import java.util.Hashtable;
import java.awt.Point;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import java.awt.image.DataBufferByte;
import java.awt.image.ImageObserver;
import java.awt.Image;
import org.lwjgl.opengl.ARBBufferObject;
import java.awt.image.ComponentColorModel;
import java.awt.color.ColorSpace;
import java.nio.ByteBuffer;
import java.awt.image.ColorModel;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class FilterOp
{
    private static int WIDTH;
    private static int HEIGHT;
    private ShaderManager shader;
    private static int[] FBO_ID;
    private static int VAO_ID;
    private static int VBO_ID;
    private static int[] PBO;
    private TextureMap TM;
    private static boolean PBO_setup;
    private BufferedImage clean;
    private Graphics2D gfx;
    private static int srcTexture;
    private static int destTexture;
    private static ColorModel colorModel;
    private static BufferedImage imgDest;
    private int locx;
    private int locy;
    private static ByteBuffer[] texFBO;
    private static int[] color_tex;
    private static boolean FBO_setup;
    
    static {
        FilterOp.FBO_ID = new int[2];
        FilterOp.VAO_ID = 0;
        FilterOp.VBO_ID = 0;
        FilterOp.PBO = new int[2];
        FilterOp.PBO_setup = false;
        FilterOp.srcTexture = 0;
        FilterOp.destTexture = 1;
        FilterOp.colorModel = new ComponentColorModel(ColorSpace.getInstance(1000), new int[] { 8, 8, 8 }, false, false, 3, 0);
        FilterOp.imgDest = new BufferedImage(1, 1, 5);
        FilterOp.texFBO = new ByteBuffer[2];
        FilterOp.color_tex = null;
        FilterOp.FBO_setup = false;
    }
    
    public FilterOp(final String programName) {
        if (!Engine.isInit()) {
            Engine.initGL(1, 1);
        }
        (this.shader = new ShaderManager(programName)).load();
        if (this.TM == null) {
            FilterOp.WIDTH = -1;
            FilterOp.HEIGHT = -1;
            this.TM = new TextureMap();
            this.setupQuad();
        }
    }
    
    private void setupPBO() {
        if (FilterOp.PBO_setup) {
            return;
        }
        FilterOp.PBO_setup = true;
        if (FilterOp.PBO[1] > 0) {
            ARBBufferObject.glDeleteBuffersARB(FilterOp.PBO[0]);
            ARBBufferObject.glDeleteBuffersARB(FilterOp.PBO[1]);
        }
        FilterOp.PBO[0] = ARBBufferObject.glGenBuffersARB();
        FilterOp.PBO[1] = ARBBufferObject.glGenBuffersARB();
        ARBBufferObject.glBindBufferARB(35052, FilterOp.PBO[0]);
        ARBBufferObject.glBufferDataARB(35052, FilterOp.WIDTH * FilterOp.HEIGHT * 3, 35040);
        ARBBufferObject.glBindBufferARB(35051, FilterOp.PBO[1]);
        ARBBufferObject.glBufferDataARB(35051, FilterOp.WIDTH * FilterOp.HEIGHT * 3, 35041);
        ARBBufferObject.glBindBufferARB(35052, 0);
        ARBBufferObject.glBindBufferARB(35051, 0);
    }
    
    public void loadImage(final BufferedImage bi) {
        if (!Engine.isInit()) {
            Engine.initGL(bi.getWidth(), bi.getHeight());
        }
        else if (bi.getWidth() != Engine.getWidth() || bi.getHeight() != Engine.getHeight()) {
            FilterOp.FBO_setup = false;
            FilterOp.PBO_setup = false;
            Engine.initGL(bi.getWidth(), bi.getHeight());
        }
        this.imagePreloader(bi);
        this.pushLoadToSRC();
    }
    
    public void imagePreloader(final BufferedImage bi) {
        if (this.clean == null) {
            this.clean = new BufferedImage(bi.getWidth(), bi.getHeight(), 5);
            this.gfx = (Graphics2D)this.clean.getGraphics();
            FilterOp.WIDTH = this.clean.getWidth();
            FilterOp.HEIGHT = this.clean.getHeight();
            this.setupPBO();
            this.setupFBO();
            this.TM.prepare(FilterOp.WIDTH, FilterOp.HEIGHT);
        }
        else if (this.clean.getWidth() != bi.getWidth() || this.clean.getHeight() != bi.getHeight()) {
            this.clean = new BufferedImage(bi.getWidth(), bi.getHeight(), 5);
            this.gfx = (Graphics2D)this.clean.getGraphics();
            FilterOp.WIDTH = this.clean.getWidth();
            FilterOp.HEIGHT = this.clean.getHeight();
            this.setupPBO();
            this.setupFBO();
            this.TM.prepare(FilterOp.WIDTH, FilterOp.HEIGHT);
        }
        this.gfx.drawImage(bi, 0, 0, null);
    }
    
    public void pushLoadToSRC() {
        final byte[] data = ((DataBufferByte)this.clean.getData().getDataBuffer()).getData();
        ARBBufferObject.glBindBufferARB(35052, FilterOp.PBO[0]);
        ARBBufferObject.glBufferDataARB(35052, FilterOp.WIDTH * FilterOp.HEIGHT * 3, 35040);
        final ByteBuffer bb = ARBBufferObject.glMapBufferARB(35052, 35001, null);
        bb.put(data);
        bb.flip();
        ARBBufferObject.glUnmapBufferARB(35052);
        GL11.glBindTexture(3553, FilterOp.color_tex[FilterOp.srcTexture]);
        ARBBufferObject.glBindBufferARB(35052, FilterOp.PBO[0]);
        GL11.glTexSubImage2D(3553, 0, 0, 0, FilterOp.WIDTH, FilterOp.HEIGHT, 6407, 5121, 0L);
        ARBBufferObject.glBindBufferARB(35052, 0);
    }
    
    public void apply(final BufferedImage bi) {
        this.loadImage(bi);
        this.render();
    }
    
    public void apply() {
        if (this.clean == null) {
            this.clean = new BufferedImage(FilterOp.WIDTH, FilterOp.HEIGHT, 5);
            this.gfx = (Graphics2D)this.clean.getGraphics();
            this.setupPBO();
            this.setupFBO();
            this.TM.prepare(FilterOp.WIDTH, FilterOp.HEIGHT);
        }
        if (this.clean.getWidth() != FilterOp.WIDTH || this.clean.getHeight() != FilterOp.HEIGHT) {
            this.clean = new BufferedImage(FilterOp.WIDTH, FilterOp.HEIGHT, 5);
            this.gfx = (Graphics2D)this.clean.getGraphics();
            this.setupPBO();
            this.setupFBO();
            this.TM.prepare(FilterOp.WIDTH, FilterOp.HEIGHT);
        }
        this.render();
    }
    
    private void render() {
        EXTFramebufferObject.glBindFramebufferEXT(36160, FilterOp.FBO_ID[FilterOp.destTexture]);
        GL13.glActiveTexture(33984);
        GL11.glBindTexture(3553, FilterOp.color_tex[FilterOp.srcTexture]);
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glClear(16640);
        GL11.glViewport(0, 0, FilterOp.WIDTH, FilterOp.HEIGHT);
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0, FilterOp.WIDTH, 0.0, FilterOp.HEIGHT, 0.1, 20.0);
        GLU.gluLookAt(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f);
        GL11.glTranslatef(0.0f, 0.0f, 0.0f);
        GL11.glScalef((float)(-FilterOp.WIDTH), (float)FilterOp.HEIGHT, 1.0f);
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        ARBShaderObjects.glUseProgramObjectARB(this.shader.getShaderID());
        GL20.glUniform1f(this.shader.getWidthLoc(), (float)FilterOp.WIDTH);
        GL20.glUniform1f(this.shader.getHeightLoc(), (float)FilterOp.HEIGHT);
        this.shader.setUniforms();
        GL30.glBindVertexArray(FilterOp.VAO_ID);
        GL20.glEnableVertexAttribArray(this.locx);
        GL20.glEnableVertexAttribArray(this.locy);
        GL11.glDrawArrays(7, 0, 4);
        ARBShaderObjects.glUseProgramObjectARB(0);
        GL20.glDisableVertexAttribArray(this.locx);
        GL20.glDisableVertexAttribArray(this.locy);
        GL30.glBindVertexArray(0);
        EXTFramebufferObject.glBindFramebufferEXT(36160, 0);
        Display.update();
        FilterOp.srcTexture = (FilterOp.srcTexture + 1) % 2;
        FilterOp.destTexture = (FilterOp.srcTexture + 1) % 2;
    }
    
    private static ByteBuffer getImageByteBuffer() {
        GL11.glBindTexture(3553, FilterOp.color_tex[FilterOp.srcTexture]);
        final ByteBuffer ret = BufferUtils.createByteBuffer(3 * FilterOp.WIDTH * FilterOp.HEIGHT);
        GL11.glGetTexImage(3553, 0, 32992, 5121, ret);
        return ret;
    }
    
    public static BufferedImage getImage() {
        if (FilterOp.imgDest.getWidth() != FilterOp.WIDTH || FilterOp.imgDest.getHeight() != FilterOp.HEIGHT) {
            FilterOp.imgDest = new BufferedImage(FilterOp.WIDTH, FilterOp.HEIGHT, 5);
        }
        final ByteBuffer b = getImageByteBuffer();
        final byte[] bz = new byte[b.remaining()];
        b.get(bz);
        final WritableRaster raster = Raster.createInterleavedRaster(new DataBufferByte(bz, bz.length), FilterOp.WIDTH, FilterOp.HEIGHT, FilterOp.WIDTH * 3, 3, new int[] { 0, 1, 2 }, null);
        return FilterOp.imgDest = new BufferedImage(FilterOp.colorModel, raster, false, null);
    }
    
    private void setupQuad() {
        final float[] vertices = { 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f };
        final FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
        verticesBuffer.put(vertices);
        verticesBuffer.flip();
        this.locx = GL20.glGetAttribLocation(this.shader.getShaderID(), "px");
        this.locy = GL20.glGetAttribLocation(this.shader.getShaderID(), "py");
        GL30.glBindVertexArray(FilterOp.VAO_ID = GL30.glGenVertexArrays());
        GL15.glBindBuffer(34962, FilterOp.VBO_ID = GL15.glGenBuffers());
        GL15.glBufferData(34962, verticesBuffer, 35044);
        GL20.glVertexAttribPointer(this.locx, 1, 5126, false, 8, 0L);
        GL20.glVertexAttribPointer(this.locy, 1, 5126, false, 8, 4L);
        GL15.glBindBuffer(34962, 0);
        GL30.glBindVertexArray(0);
    }
    
    private void setupFBO() {
        if (FilterOp.FBO_setup) {
            return;
        }
        FilterOp.FBO_setup = true;
        if (FilterOp.color_tex == null) {
            (FilterOp.color_tex = new int[2])[0] = GL11.glGenTextures();
            FilterOp.color_tex[1] = GL11.glGenTextures();
        }
        if (FilterOp.FBO_ID[1] > 0) {
            EXTFramebufferObject.glDeleteFramebuffersEXT(FilterOp.FBO_ID[0]);
            EXTFramebufferObject.glDeleteFramebuffersEXT(FilterOp.FBO_ID[1]);
        }
        GL11.glBindTexture(3553, FilterOp.color_tex[0]);
        GL11.glTexParameteri(3553, 10242, 33071);
        GL11.glTexParameteri(3553, 10243, 33071);
        GL11.glTexParameteri(3553, 10241, 9728);
        GL11.glTexParameteri(3553, 10240, 9728);
        FilterOp.texFBO[0] = ByteBuffer.allocateDirect(FilterOp.WIDTH * FilterOp.HEIGHT * 4);
        GL11.glTexImage2D(3553, 0, 32856, FilterOp.WIDTH, FilterOp.HEIGHT, 0, 6408, 5121, FilterOp.texFBO[0]);
        GL11.glBindTexture(3553, FilterOp.color_tex[1]);
        GL11.glTexParameteri(3553, 10242, 33071);
        GL11.glTexParameteri(3553, 10243, 33071);
        GL11.glTexParameteri(3553, 10241, 9728);
        GL11.glTexParameteri(3553, 10240, 9728);
        FilterOp.texFBO[1] = ByteBuffer.allocateDirect(FilterOp.WIDTH * FilterOp.HEIGHT * 4);
        GL11.glTexImage2D(3553, 0, 32856, FilterOp.WIDTH, FilterOp.HEIGHT, 0, 6408, 5121, FilterOp.texFBO[1]);
        EXTFramebufferObject.glBindFramebufferEXT(36160, FilterOp.FBO_ID[0] = EXTFramebufferObject.glGenFramebuffersEXT());
        EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36064, 3553, FilterOp.color_tex[0], 0);
        EXTFramebufferObject.glBindFramebufferEXT(36160, FilterOp.FBO_ID[1] = EXTFramebufferObject.glGenFramebuffersEXT());
        EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36064, 3553, FilterOp.color_tex[1], 0);
    }
    
    public void setFloat(final String name, final double value) {
        this.shader.setFloat(name, (float)value);
    }
    
    public void setInt(final String name, final int value) {
        this.shader.setInt(name, value);
    }
    
    public void setFloatArray(final String name, final float[] array) {
        this.shader.setFloatArray(name, array);
    }
}
