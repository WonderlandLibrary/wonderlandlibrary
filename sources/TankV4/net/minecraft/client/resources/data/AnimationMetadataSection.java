package net.minecraft.client.resources.data;

import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class AnimationMetadataSection implements IMetadataSection {
   private final List animationFrames;
   private final boolean interpolate;
   private final int frameWidth;
   private final int frameHeight;
   private final int frameTime;

   public int getFrameTime() {
      return this.frameTime;
   }

   public boolean isInterpolate() {
      return this.interpolate;
   }

   private AnimationFrame getAnimationFrame(int var1) {
      return (AnimationFrame)this.animationFrames.get(var1);
   }

   public boolean frameHasTime(int var1) {
      return !((AnimationFrame)this.animationFrames.get(var1)).hasNoTime();
   }

   public int getFrameIndex(int var1) {
      return ((AnimationFrame)this.animationFrames.get(var1)).getFrameIndex();
   }

   public int getFrameTimeSingle(int var1) {
      AnimationFrame var2 = this.getAnimationFrame(var1);
      return var2.hasNoTime() ? this.frameTime : var2.getFrameTime();
   }

   public int getFrameCount() {
      return this.animationFrames.size();
   }

   public AnimationMetadataSection(List var1, int var2, int var3, int var4, boolean var5) {
      this.animationFrames = var1;
      this.frameWidth = var2;
      this.frameHeight = var3;
      this.frameTime = var4;
      this.interpolate = var5;
   }

   public int getFrameHeight() {
      return this.frameHeight;
   }

   public int getFrameWidth() {
      return this.frameWidth;
   }

   public Set getFrameIndexSet() {
      HashSet var1 = Sets.newHashSet();
      Iterator var3 = this.animationFrames.iterator();

      while(var3.hasNext()) {
         AnimationFrame var2 = (AnimationFrame)var3.next();
         var1.add(var2.getFrameIndex());
      }

      return var1;
   }
}
