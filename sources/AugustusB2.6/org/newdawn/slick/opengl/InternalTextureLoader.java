// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.opengl;

import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import java.io.BufferedInputStream;
import java.lang.ref.SoftReference;
import org.newdawn.slick.util.ResourceLoader;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL11;
import java.util.HashMap;

public class InternalTextureLoader
{
    private static final InternalTextureLoader loader;
    private HashMap texturesLinear;
    private HashMap texturesNearest;
    private int dstPixelFormat;
    private boolean deferred;
    
    public static InternalTextureLoader get() {
        return InternalTextureLoader.loader;
    }
    
    private InternalTextureLoader() {
        this.texturesLinear = new HashMap();
        this.texturesNearest = new HashMap();
        this.dstPixelFormat = 32856;
    }
    
    public void setDeferredLoading(final boolean deferred) {
        this.deferred = deferred;
    }
    
    public boolean isDeferredLoading() {
        return this.deferred;
    }
    
    public void clear(final String name) {
        this.texturesLinear.remove(name);
        this.texturesNearest.remove(name);
    }
    
    public void clear() {
        this.texturesLinear.clear();
        this.texturesNearest.clear();
    }
    
    public void set16BitMode() {
        this.dstPixelFormat = 32859;
    }
    
    public static int createTextureID() {
        final IntBuffer tmp = createIntBuffer(1);
        GL11.glGenTextures(tmp);
        return tmp.get(0);
    }
    
    public Texture getTexture(final File source, final boolean flipped, final int filter) throws IOException {
        final String resourceName = source.getAbsolutePath();
        final InputStream in = new FileInputStream(source);
        return this.getTexture(in, resourceName, flipped, filter, null);
    }
    
    public Texture getTexture(final File source, final boolean flipped, final int filter, final int[] transparent) throws IOException {
        final String resourceName = source.getAbsolutePath();
        final InputStream in = new FileInputStream(source);
        return this.getTexture(in, resourceName, flipped, filter, transparent);
    }
    
    public Texture getTexture(final String resourceName, final boolean flipped, final int filter) throws IOException {
        final InputStream in = ResourceLoader.getResourceAsStream(resourceName);
        return this.getTexture(in, resourceName, flipped, filter, null);
    }
    
    public Texture getTexture(final String resourceName, final boolean flipped, final int filter, final int[] transparent) throws IOException {
        final InputStream in = ResourceLoader.getResourceAsStream(resourceName);
        return this.getTexture(in, resourceName, flipped, filter, transparent);
    }
    
    public Texture getTexture(final InputStream in, final String resourceName, final boolean flipped, final int filter) throws IOException {
        return this.getTexture(in, resourceName, flipped, filter, null);
    }
    
    public TextureImpl getTexture(final InputStream in, final String resourceName, final boolean flipped, final int filter, final int[] transparent) throws IOException {
        if (this.deferred) {
            return new DeferredTexture(in, resourceName, flipped, filter, transparent);
        }
        HashMap hash = this.texturesLinear;
        if (filter == 9728) {
            hash = this.texturesNearest;
        }
        String resName = resourceName;
        if (transparent != null) {
            resName = resName + ":" + transparent[0] + ":" + transparent[1] + ":" + transparent[2];
        }
        resName = resName + ":" + flipped;
        final SoftReference ref = hash.get(resName);
        if (ref != null) {
            final TextureImpl tex = ref.get();
            if (tex != null) {
                return tex;
            }
            hash.remove(resName);
        }
        try {
            GL11.glGetError();
        }
        catch (NullPointerException e) {
            throw new RuntimeException("Image based resources must be loaded as part of init() or the game loop. They cannot be loaded before initialisation.");
        }
        final TextureImpl tex = this.getTexture(in, resourceName, 3553, filter, filter, flipped, transparent);
        tex.setCacheName(resName);
        hash.put(resName, new SoftReference<TextureImpl>(tex));
        return tex;
    }
    
    private TextureImpl getTexture(final InputStream in, final String resourceName, final int target, final int magFilter, final int minFilter, final boolean flipped, final int[] transparent) throws IOException {
        final int textureID = createTextureID();
        final TextureImpl texture = new TextureImpl(resourceName, target, textureID);
        GL11.glBindTexture(target, textureID);
        final LoadableImageData imageData = ImageDataFactory.getImageDataFor(resourceName);
        final ByteBuffer textureBuffer = imageData.loadImage(new BufferedInputStream(in), flipped, transparent);
        final int width = imageData.getWidth();
        final int height = imageData.getHeight();
        final boolean hasAlpha = imageData.getDepth() == 32;
        texture.setTextureWidth(imageData.getTexWidth());
        texture.setTextureHeight(imageData.getTexHeight());
        final int texWidth = texture.getTextureWidth();
        final int texHeight = texture.getTextureHeight();
        final IntBuffer temp = BufferUtils.createIntBuffer(16);
        GL11.glGetInteger(3379, temp);
        final int max = temp.get(0);
        if (texWidth > max || texHeight > max) {
            throw new IOException("Attempt to allocate a texture to big for the current hardware");
        }
        final int srcPixelFormat = hasAlpha ? 6408 : 6407;
        final int componentCount = hasAlpha ? 4 : 3;
        texture.setWidth(width);
        texture.setHeight(height);
        texture.setAlpha(hasAlpha);
        GL11.glTexParameteri(target, 10241, minFilter);
        GL11.glTexParameteri(target, 10240, magFilter);
        GL11.glTexImage2D(target, 0, this.dstPixelFormat, get2Fold(width), get2Fold(height), 0, srcPixelFormat, 5121, textureBuffer);
        return texture;
    }
    
    public Texture createTexture(final int width, final int height) throws IOException {
        return this.createTexture(width, height, 9728);
    }
    
    public Texture createTexture(final int width, final int height, final int filter) throws IOException {
        final ImageData ds = new EmptyImageData(width, height);
        return this.getTexture(ds, filter);
    }
    
    public Texture getTexture(final ImageData dataSource, final int filter) throws IOException {
        final int target = 3553;
        final int textureID = createTextureID();
        final TextureImpl texture = new TextureImpl("generated:" + dataSource, target, textureID);
        final int minFilter = filter;
        final int magFilter = filter;
        final boolean flipped = false;
        GL11.glBindTexture(target, textureID);
        final ByteBuffer textureBuffer = dataSource.getImageBufferData();
        final int width = dataSource.getWidth();
        final int height = dataSource.getHeight();
        final boolean hasAlpha = dataSource.getDepth() == 32;
        texture.setTextureWidth(dataSource.getTexWidth());
        texture.setTextureHeight(dataSource.getTexHeight());
        final int texWidth = texture.getTextureWidth();
        final int texHeight = texture.getTextureHeight();
        final int srcPixelFormat = hasAlpha ? 6408 : 6407;
        final int componentCount = hasAlpha ? 4 : 3;
        texture.setWidth(width);
        texture.setHeight(height);
        texture.setAlpha(hasAlpha);
        final IntBuffer temp = BufferUtils.createIntBuffer(16);
        GL11.glGetInteger(3379, temp);
        final int max = temp.get(0);
        if (texWidth > max || texHeight > max) {
            throw new IOException("Attempt to allocate a texture to big for the current hardware");
        }
        GL11.glTexParameteri(target, 10241, minFilter);
        GL11.glTexParameteri(target, 10240, magFilter);
        GL11.glTexImage2D(target, 0, this.dstPixelFormat, get2Fold(width), get2Fold(height), 0, srcPixelFormat, 5121, textureBuffer);
        return texture;
    }
    
    public static int get2Fold(final int fold) {
        int ret;
        for (ret = 2; ret < fold; ret *= 2) {}
        return ret;
    }
    
    public static IntBuffer createIntBuffer(final int size) {
        final ByteBuffer temp = ByteBuffer.allocateDirect(4 * size);
        temp.order(ByteOrder.nativeOrder());
        return temp.asIntBuffer();
    }
    
    static {
        loader = new InternalTextureLoader();
    }
}
