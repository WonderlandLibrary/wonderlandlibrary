// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine;

import java.util.Hashtable;
import net.minecraft.client.renderer.texture.ITextureObject;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.optifine.util.TextureUtils;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.src.Config;
import java.util.Properties;
import java.nio.ByteBuffer;
import net.minecraft.util.ResourceLocation;

public class TextureAnimation
{
    private String srcTex;
    private String dstTex;
    ResourceLocation dstTexLoc;
    private int dstTextId;
    private int dstX;
    private int dstY;
    private int frameWidth;
    private int frameHeight;
    private TextureAnimationFrame[] frames;
    private int currentFrameIndex;
    private boolean interpolate;
    private int interpolateSkip;
    private ByteBuffer interpolateData;
    byte[] srcData;
    private ByteBuffer imageData;
    private boolean active;
    private boolean valid;
    
    public TextureAnimation(final String texFrom, final byte[] srcData, final String texTo, final ResourceLocation locTexTo, final int dstX, final int dstY, final int frameWidth, final int frameHeight, final Properties props) {
        this.srcTex = null;
        this.dstTex = null;
        this.dstTexLoc = null;
        this.dstTextId = -1;
        this.dstX = 0;
        this.dstY = 0;
        this.frameWidth = 0;
        this.frameHeight = 0;
        this.frames = null;
        this.currentFrameIndex = 0;
        this.interpolate = false;
        this.interpolateSkip = 0;
        this.interpolateData = null;
        this.srcData = null;
        this.imageData = null;
        this.active = true;
        this.valid = true;
        this.srcTex = texFrom;
        this.dstTex = texTo;
        this.dstTexLoc = locTexTo;
        this.dstX = dstX;
        this.dstY = dstY;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        final int i = frameWidth * frameHeight * 4;
        if (srcData.length % i != 0) {
            Config.warn("Invalid animated texture length: " + srcData.length + ", frameWidth: " + frameWidth + ", frameHeight: " + frameHeight);
        }
        this.srcData = srcData;
        int j = srcData.length / i;
        if (props.get("tile.0") != null) {
            for (int k = 0; props.get("tile." + k) != null; ++k) {
                j = k + 1;
            }
        }
        final String s2 = ((Hashtable<K, String>)props).get("duration");
        final int l = Math.max(Config.parseInt(s2, 1), 1);
        this.frames = new TextureAnimationFrame[j];
        for (int i2 = 0; i2 < this.frames.length; ++i2) {
            final String s3 = ((Hashtable<K, String>)props).get("tile." + i2);
            final int j2 = Config.parseInt(s3, i2);
            final String s4 = ((Hashtable<K, String>)props).get("duration." + i2);
            final int k2 = Math.max(Config.parseInt(s4, l), 1);
            final TextureAnimationFrame textureanimationframe = new TextureAnimationFrame(j2, k2);
            this.frames[i2] = textureanimationframe;
        }
        this.interpolate = Config.parseBoolean(props.getProperty("interpolate"), false);
        this.interpolateSkip = Config.parseInt(props.getProperty("skip"), 0);
        if (this.interpolate) {
            this.interpolateData = GLAllocation.createDirectByteBuffer(i);
        }
    }
    
    public boolean nextFrame() {
        final TextureAnimationFrame textureanimationframe = this.getCurrentFrame();
        if (textureanimationframe == null) {
            return false;
        }
        final TextureAnimationFrame textureAnimationFrame = textureanimationframe;
        ++textureAnimationFrame.counter;
        if (textureanimationframe.counter < textureanimationframe.duration) {
            return this.interpolate;
        }
        textureanimationframe.counter = 0;
        ++this.currentFrameIndex;
        if (this.currentFrameIndex >= this.frames.length) {
            this.currentFrameIndex = 0;
        }
        return true;
    }
    
    public TextureAnimationFrame getCurrentFrame() {
        return this.getFrame(this.currentFrameIndex);
    }
    
    public TextureAnimationFrame getFrame(int index) {
        if (this.frames.length <= 0) {
            return null;
        }
        if (index < 0 || index >= this.frames.length) {
            index = 0;
        }
        final TextureAnimationFrame textureanimationframe = this.frames[index];
        return textureanimationframe;
    }
    
    public int getFrameCount() {
        return this.frames.length;
    }
    
    public void updateTexture() {
        if (this.valid) {
            if (this.dstTextId < 0) {
                final ITextureObject itextureobject = TextureUtils.getTexture(this.dstTexLoc);
                if (itextureobject == null) {
                    this.valid = false;
                    return;
                }
                this.dstTextId = itextureobject.getGlTextureId();
            }
            if (this.imageData == null) {
                (this.imageData = GLAllocation.createDirectByteBuffer(this.srcData.length)).put(this.srcData);
                this.imageData.flip();
                this.srcData = null;
            }
            this.active = (!SmartAnimations.isActive() || SmartAnimations.isTextureRendered(this.dstTextId));
            if (this.nextFrame() && this.active) {
                final int j = this.frameWidth * this.frameHeight * 4;
                final TextureAnimationFrame textureanimationframe = this.getCurrentFrame();
                if (textureanimationframe != null) {
                    final int i = j * textureanimationframe.index;
                    if (i + j <= this.imageData.limit()) {
                        if (this.interpolate && textureanimationframe.counter > 0) {
                            if (this.interpolateSkip <= 1 || textureanimationframe.counter % this.interpolateSkip == 0) {
                                final TextureAnimationFrame textureanimationframe2 = this.getFrame(this.currentFrameIndex + 1);
                                final double d0 = 1.0 * textureanimationframe.counter / textureanimationframe.duration;
                                this.updateTextureInerpolate(textureanimationframe, textureanimationframe2, d0);
                            }
                        }
                        else {
                            this.imageData.position(i);
                            GlStateManager.bindTexture(this.dstTextId);
                            GL11.glTexSubImage2D(3553, 0, this.dstX, this.dstY, this.frameWidth, this.frameHeight, 6408, 5121, this.imageData);
                        }
                    }
                }
            }
        }
    }
    
    private void updateTextureInerpolate(final TextureAnimationFrame frame1, final TextureAnimationFrame frame2, final double dd) {
        final int i = this.frameWidth * this.frameHeight * 4;
        final int j = i * frame1.index;
        if (j + i <= this.imageData.limit()) {
            final int k = i * frame2.index;
            if (k + i <= this.imageData.limit()) {
                this.interpolateData.clear();
                for (int l = 0; l < i; ++l) {
                    final int i2 = this.imageData.get(j + l) & 0xFF;
                    final int j2 = this.imageData.get(k + l) & 0xFF;
                    final int k2 = this.mix(i2, j2, dd);
                    final byte b0 = (byte)k2;
                    this.interpolateData.put(b0);
                }
                this.interpolateData.flip();
                GlStateManager.bindTexture(this.dstTextId);
                GL11.glTexSubImage2D(3553, 0, this.dstX, this.dstY, this.frameWidth, this.frameHeight, 6408, 5121, this.interpolateData);
            }
        }
    }
    
    private int mix(final int col1, final int col2, final double k) {
        return (int)(col1 * (1.0 - k) + col2 * k);
    }
    
    public String getSrcTex() {
        return this.srcTex;
    }
    
    public String getDstTex() {
        return this.dstTex;
    }
    
    public ResourceLocation getDstTexLoc() {
        return this.dstTexLoc;
    }
    
    public boolean isActive() {
        return this.active;
    }
}
