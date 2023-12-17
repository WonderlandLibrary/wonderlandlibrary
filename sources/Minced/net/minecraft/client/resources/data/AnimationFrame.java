// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources.data;

public class AnimationFrame
{
    private final int frameIndex;
    private final int frameTime;
    
    public AnimationFrame(final int frameIndexIn) {
        this(frameIndexIn, -1);
    }
    
    public AnimationFrame(final int frameIndexIn, final int frameTimeIn) {
        this.frameIndex = frameIndexIn;
        this.frameTime = frameTimeIn;
    }
    
    public boolean hasNoTime() {
        return this.frameTime == -1;
    }
    
    public int getFrameTime() {
        return this.frameTime;
    }
    
    public int getFrameIndex() {
        return this.frameIndex;
    }
}
