// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources.data;

import java.util.Iterator;
import com.google.common.collect.Sets;
import java.util.Set;
import java.util.List;

public class AnimationMetadataSection implements IMetadataSection
{
    private final List<AnimationFrame> animationFrames;
    private final int frameWidth;
    private final int frameHeight;
    private final int frameTime;
    private final boolean interpolate;
    
    public AnimationMetadataSection(final List<AnimationFrame> animationFramesIn, final int frameWidthIn, final int frameHeightIn, final int frameTimeIn, final boolean interpolateIn) {
        this.animationFrames = animationFramesIn;
        this.frameWidth = frameWidthIn;
        this.frameHeight = frameHeightIn;
        this.frameTime = frameTimeIn;
        this.interpolate = interpolateIn;
    }
    
    public int getFrameHeight() {
        return this.frameHeight;
    }
    
    public int getFrameWidth() {
        return this.frameWidth;
    }
    
    public int getFrameCount() {
        return this.animationFrames.size();
    }
    
    public int getFrameTime() {
        return this.frameTime;
    }
    
    public boolean isInterpolate() {
        return this.interpolate;
    }
    
    private AnimationFrame getAnimationFrame(final int frame) {
        return this.animationFrames.get(frame);
    }
    
    public int getFrameTimeSingle(final int frame) {
        final AnimationFrame animationframe = this.getAnimationFrame(frame);
        return animationframe.hasNoTime() ? this.frameTime : animationframe.getFrameTime();
    }
    
    public boolean frameHasTime(final int frame) {
        return !this.animationFrames.get(frame).hasNoTime();
    }
    
    public int getFrameIndex(final int frame) {
        return this.animationFrames.get(frame).getFrameIndex();
    }
    
    public Set<Integer> getFrameIndexSet() {
        final Set<Integer> set = (Set<Integer>)Sets.newHashSet();
        for (final AnimationFrame animationframe : this.animationFrames) {
            set.add(animationframe.getFrameIndex());
        }
        return set;
    }
}
