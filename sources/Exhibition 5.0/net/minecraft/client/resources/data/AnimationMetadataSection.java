// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.resources.data;

import java.util.Iterator;
import java.util.HashSet;
import com.google.common.collect.Sets;
import java.util.Set;
import java.util.List;

public class AnimationMetadataSection implements IMetadataSection
{
    private final List animationFrames;
    private final int frameWidth;
    private final int frameHeight;
    private final int frameTime;
    private final boolean field_177220_e;
    private static final String __OBFID = "CL_00001106";
    
    public AnimationMetadataSection(final List p_i46088_1_, final int p_i46088_2_, final int p_i46088_3_, final int p_i46088_4_, final boolean p_i46088_5_) {
        this.animationFrames = p_i46088_1_;
        this.frameWidth = p_i46088_2_;
        this.frameHeight = p_i46088_3_;
        this.frameTime = p_i46088_4_;
        this.field_177220_e = p_i46088_5_;
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
    
    public boolean func_177219_e() {
        return this.field_177220_e;
    }
    
    private AnimationFrame getAnimationFrame(final int p_130072_1_) {
        return this.animationFrames.get(p_130072_1_);
    }
    
    public int getFrameTimeSingle(final int p_110472_1_) {
        final AnimationFrame var2 = this.getAnimationFrame(p_110472_1_);
        return var2.hasNoTime() ? this.frameTime : var2.getFrameTime();
    }
    
    public boolean frameHasTime(final int p_110470_1_) {
        return !this.animationFrames.get(p_110470_1_).hasNoTime();
    }
    
    public int getFrameIndex(final int p_110468_1_) {
        return this.animationFrames.get(p_110468_1_).getFrameIndex();
    }
    
    public Set getFrameIndexSet() {
        final HashSet var1 = Sets.newHashSet();
        for (final AnimationFrame var3 : this.animationFrames) {
            var1.add(var3.getFrameIndex());
        }
        return var1;
    }
}
