// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.client.renderer.texture.ITextureObject;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GLAllocation;
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
    private int activeFrame;
    byte[] srcData;
    private ByteBuffer imageData;
    
    public TextureAnimation(final String texFrom, final byte[] srcData, final String texTo, final ResourceLocation locTexTo, final int dstX, final int dstY, final int frameWidth, final int frameHeight, final Properties props, final int durDef) {
        this.srcTex = null;
        this.dstTex = null;
        this.dstTexLoc = null;
        this.dstTextId = -1;
        this.dstX = 0;
        this.dstY = 0;
        this.frameWidth = 0;
        this.frameHeight = 0;
        this.frames = null;
        this.activeFrame = 0;
        this.srcData = null;
        this.imageData = null;
        this.srcTex = texFrom;
        this.dstTex = texTo;
        this.dstTexLoc = locTexTo;
        this.dstX = dstX;
        this.dstY = dstY;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        final int frameLen = frameWidth * frameHeight * 4;
        if (srcData.length % frameLen != 0) {
            Config.warn("Invalid animated texture length: " + srcData.length + ", frameWidth: " + frameWidth + ", frameHeight: " + frameHeight);
        }
        this.srcData = srcData;
        int numFrames = srcData.length / frameLen;
        if (props.get("tile.0") != null) {
            for (int durationDefStr = 0; props.get("tile." + durationDefStr) != null; ++durationDefStr) {
                numFrames = durationDefStr + 1;
            }
        }
        final String var21 = (String)props.get("duration");
        final int durationDef = Config.parseInt(var21, durDef);
        this.frames = new TextureAnimationFrame[numFrames];
        for (int i = 0; i < this.frames.length; ++i) {
            final String indexStr = (String)props.get("tile." + i);
            final int index = Config.parseInt(indexStr, i);
            final String durationStr = (String)props.get("duration." + i);
            final int duration = Config.parseInt(durationStr, durationDef);
            final TextureAnimationFrame frm = new TextureAnimationFrame(index, duration);
            this.frames[i] = frm;
        }
    }
    
    public boolean nextFrame() {
        if (this.frames.length <= 0) {
            return false;
        }
        if (this.activeFrame >= this.frames.length) {
            this.activeFrame = 0;
        }
        final TextureAnimationFrame textureAnimationFrame;
        final TextureAnimationFrame frame = textureAnimationFrame = this.frames[this.activeFrame];
        ++textureAnimationFrame.counter;
        if (frame.counter < frame.duration) {
            return false;
        }
        frame.counter = 0;
        ++this.activeFrame;
        if (this.activeFrame >= this.frames.length) {
            this.activeFrame = 0;
        }
        return true;
    }
    
    public int getActiveFrameIndex() {
        if (this.frames.length <= 0) {
            return 0;
        }
        if (this.activeFrame >= this.frames.length) {
            this.activeFrame = 0;
        }
        final TextureAnimationFrame frame = this.frames[this.activeFrame];
        return frame.index;
    }
    
    public int getFrameCount() {
        return this.frames.length;
    }
    
    public boolean updateTexture() {
        if (this.dstTextId < 0) {
            final ITextureObject frameLen = TextureUtils.getTexture(this.dstTexLoc);
            if (frameLen == null) {
                return false;
            }
            this.dstTextId = frameLen.getGlTextureId();
        }
        if (this.imageData == null) {
            (this.imageData = GLAllocation.createDirectByteBuffer(this.srcData.length)).put(this.srcData);
            this.srcData = null;
        }
        if (!this.nextFrame()) {
            return false;
        }
        final int frameLen2 = this.frameWidth * this.frameHeight * 4;
        final int imgNum = this.getActiveFrameIndex();
        final int offset = frameLen2 * imgNum;
        if (offset + frameLen2 > this.imageData.capacity()) {
            return false;
        }
        this.imageData.position(offset);
        GlStateManager.func_179144_i(this.dstTextId);
        GL11.glTexSubImage2D(3553, 0, this.dstX, this.dstY, this.frameWidth, this.frameHeight, 6408, 5121, this.imageData);
        return true;
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
}
