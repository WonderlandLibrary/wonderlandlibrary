// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.optifine;

import java.util.Hashtable;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GLAllocation;
import java.util.Properties;
import java.nio.ByteBuffer;

public class TextureAnimation
{
    private String srcTex;
    private String dstTex;
    private int dstTextId;
    private int dstX;
    private int dstY;
    private int frameWidth;
    private int frameHeight;
    private CustomAnimationFrame[] frames;
    private int activeFrame;
    private ByteBuffer imageData;
    
    public TextureAnimation(final String texFrom, final byte[] srcData, final String texTo, final int dstTexId, final int dstX, final int dstY, final int frameWidth, final int frameHeight, final Properties props, final int durDef) {
        this.srcTex = null;
        this.dstTex = null;
        this.dstTextId = -1;
        this.dstX = 0;
        this.dstY = 0;
        this.frameWidth = 0;
        this.frameHeight = 0;
        this.frames = null;
        this.activeFrame = 0;
        this.imageData = null;
        this.srcTex = texFrom;
        this.dstTex = texTo;
        this.dstTextId = dstTexId;
        this.dstX = dstX;
        this.dstY = dstY;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        final int frameLen = frameWidth * frameHeight * 4;
        if (srcData.length % frameLen != 0) {
            Config.warn("Invalid animated texture length: " + srcData.length + ", frameWidth: " + frameHeight + ", frameHeight: " + frameHeight);
        }
        (this.imageData = GLAllocation.createDirectByteBuffer(srcData.length)).put(srcData);
        int numFrames = srcData.length / frameLen;
        if (props.get("tile.0") != null) {
            for (int durationDefStr = 0; props.get("tile." + durationDefStr) != null; ++durationDefStr) {
                numFrames = durationDefStr + 1;
            }
        }
        final String var21 = ((Hashtable<K, String>)props).get("duration");
        final int durationDef = Config.parseInt(var21, durDef);
        this.frames = new CustomAnimationFrame[numFrames];
        for (int i = 0; i < this.frames.length; ++i) {
            final String indexStr = ((Hashtable<K, String>)props).get("tile." + i);
            final int index = Config.parseInt(indexStr, i);
            final String durationStr = ((Hashtable<K, String>)props).get("duration." + i);
            final int duration = Config.parseInt(durationStr, durationDef);
            final CustomAnimationFrame frm = new CustomAnimationFrame(index, duration);
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
        final CustomAnimationFrame customAnimationFrame;
        final CustomAnimationFrame frame = customAnimationFrame = this.frames[this.activeFrame];
        ++customAnimationFrame.counter;
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
        final CustomAnimationFrame frame = this.frames[this.activeFrame];
        return frame.index;
    }
    
    public int getFrameCount() {
        return this.frames.length;
    }
    
    public boolean updateTexture() {
        if (!this.nextFrame()) {
            return false;
        }
        final int frameLen = this.frameWidth * this.frameHeight * 4;
        final int imgNum = this.getActiveFrameIndex();
        final int offset = frameLen * imgNum;
        if (offset + frameLen > this.imageData.capacity()) {
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
    
    public int getDstTextId() {
        return this.dstTextId;
    }
}
