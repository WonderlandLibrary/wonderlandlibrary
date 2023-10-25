/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 */
package net.minecraft.client.resources.data;

import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.client.resources.data.AnimationFrame;
import net.minecraft.client.resources.data.IMetadataSection;

public class AnimationMetadataSection
implements IMetadataSection {
    private final int frameHeight;
    private final List<AnimationFrame> animationFrames;
    private final int frameWidth;
    private final int frameTime;
    private final boolean interpolate;

    public int getFrameHeight() {
        return this.frameHeight;
    }

    public AnimationMetadataSection(List<AnimationFrame> list, int n, int n2, int n3, boolean bl) {
        this.animationFrames = list;
        this.frameWidth = n;
        this.frameHeight = n2;
        this.frameTime = n3;
        this.interpolate = bl;
    }

    public boolean frameHasTime(int n) {
        return !this.animationFrames.get(n).hasNoTime();
    }

    public boolean isInterpolate() {
        return this.interpolate;
    }

    public int getFrameTimeSingle(int n) {
        AnimationFrame animationFrame = this.getAnimationFrame(n);
        return animationFrame.hasNoTime() ? this.frameTime : animationFrame.getFrameTime();
    }

    public Set<Integer> getFrameIndexSet() {
        HashSet hashSet = Sets.newHashSet();
        for (AnimationFrame animationFrame : this.animationFrames) {
            hashSet.add(animationFrame.getFrameIndex());
        }
        return hashSet;
    }

    public int getFrameTime() {
        return this.frameTime;
    }

    private AnimationFrame getAnimationFrame(int n) {
        return this.animationFrames.get(n);
    }

    public int getFrameIndex(int n) {
        return this.animationFrames.get(n).getFrameIndex();
    }

    public int getFrameWidth() {
        return this.frameWidth;
    }

    public int getFrameCount() {
        return this.animationFrames.size();
    }
}

