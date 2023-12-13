// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.texture;

import java.util.Iterator;
import java.util.ArrayList;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.StitcherException;
import java.util.Arrays;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;

public class Stitcher
{
    private final int mipmapLevelStitcher;
    private final Set setStitchHolders;
    private final List stitchSlots;
    private int currentWidth;
    private int currentHeight;
    private final int maxWidth;
    private final int maxHeight;
    private final boolean forcePowerOf2;
    private final int maxTileDimension;
    private static final String __OBFID = "CL_00001054";
    
    public Stitcher(final int p_i45095_1_, final int p_i45095_2_, final boolean p_i45095_3_, final int p_i45095_4_, final int p_i45095_5_) {
        this.setStitchHolders = Sets.newHashSetWithExpectedSize(256);
        this.stitchSlots = Lists.newArrayListWithCapacity(256);
        this.mipmapLevelStitcher = p_i45095_5_;
        this.maxWidth = p_i45095_1_;
        this.maxHeight = p_i45095_2_;
        this.forcePowerOf2 = p_i45095_3_;
        this.maxTileDimension = p_i45095_4_;
    }
    
    public int getCurrentWidth() {
        return this.currentWidth;
    }
    
    public int getCurrentHeight() {
        return this.currentHeight;
    }
    
    public void addSprite(final TextureAtlasSprite p_110934_1_) {
        final Holder var2 = new Holder(p_110934_1_, this.mipmapLevelStitcher);
        if (this.maxTileDimension > 0) {
            var2.setNewDimension(this.maxTileDimension);
        }
        this.setStitchHolders.add(var2);
    }
    
    public void doStitch() {
        final Holder[] var1 = this.setStitchHolders.toArray(new Holder[this.setStitchHolders.size()]);
        Arrays.sort(var1);
        final Holder[] var2 = var1;
        for (int var3 = var1.length, var4 = 0; var4 < var3; ++var4) {
            final Holder var5 = var2[var4];
            if (!this.allocateSlot(var5)) {
                final String var6 = String.format("Unable to fit: %s, size: %dx%d, atlas: %dx%d, atlasMax: %dx%d - Maybe try a lower resolution resourcepack?", var5.getAtlasSprite().getIconName(), var5.getAtlasSprite().getIconWidth(), var5.getAtlasSprite().getIconHeight(), this.currentWidth, this.currentHeight, this.maxWidth, this.maxHeight);
                throw new StitcherException(var5, var6);
            }
        }
        if (this.forcePowerOf2) {
            this.currentWidth = MathHelper.roundUpToPowerOfTwo(this.currentWidth);
            this.currentHeight = MathHelper.roundUpToPowerOfTwo(this.currentHeight);
        }
    }
    
    public List getStichSlots() {
        final ArrayList var1 = Lists.newArrayList();
        for (final Slot var3 : this.stitchSlots) {
            var3.getAllStitchSlots(var1);
        }
        final ArrayList var4 = Lists.newArrayList();
        for (final Slot var6 : var1) {
            final Holder var7 = var6.getStitchHolder();
            final TextureAtlasSprite var8 = var7.getAtlasSprite();
            var8.initSprite(this.currentWidth, this.currentHeight, var6.getOriginX(), var6.getOriginY(), var7.isRotated());
            var4.add(var8);
        }
        return var4;
    }
    
    private static int getMipmapDimension(final int p_147969_0_, final int p_147969_1_) {
        return (p_147969_0_ >> p_147969_1_) + (((p_147969_0_ & (1 << p_147969_1_) - 1) != 0x0) ? 1 : 0) << p_147969_1_;
    }
    
    private boolean allocateSlot(final Holder p_94310_1_) {
        for (int var2 = 0; var2 < this.stitchSlots.size(); ++var2) {
            if (this.stitchSlots.get(var2).addSlot(p_94310_1_)) {
                return true;
            }
            p_94310_1_.rotate();
            if (this.stitchSlots.get(var2).addSlot(p_94310_1_)) {
                return true;
            }
            p_94310_1_.rotate();
        }
        return this.expandAndAllocateSlot(p_94310_1_);
    }
    
    private boolean expandAndAllocateSlot(final Holder p_94311_1_) {
        final int var2 = Math.min(p_94311_1_.getWidth(), p_94311_1_.getHeight());
        final boolean var3 = this.currentWidth == 0 && this.currentHeight == 0;
        boolean var12;
        if (this.forcePowerOf2) {
            final int var4 = MathHelper.roundUpToPowerOfTwo(this.currentWidth);
            final int var5 = MathHelper.roundUpToPowerOfTwo(this.currentHeight);
            final int var6 = MathHelper.roundUpToPowerOfTwo(this.currentWidth + var2);
            final int var7 = MathHelper.roundUpToPowerOfTwo(this.currentHeight + var2);
            final boolean var8 = var6 <= this.maxWidth;
            final boolean var9 = var7 <= this.maxHeight;
            if (!var8 && !var9) {
                return false;
            }
            final boolean var10 = var4 != var6;
            final boolean var11 = var5 != var7;
            if (var10 ^ var11) {
                var12 = !var10;
            }
            else {
                var12 = (var8 && var4 <= var5);
            }
        }
        else {
            final boolean var13 = this.currentWidth + var2 <= this.maxWidth;
            final boolean var14 = this.currentHeight + var2 <= this.maxHeight;
            if (!var13 && !var14) {
                return false;
            }
            var12 = (var13 && (var3 || this.currentWidth <= this.currentHeight));
        }
        final int var4 = Math.max(p_94311_1_.getWidth(), p_94311_1_.getHeight());
        if (MathHelper.roundUpToPowerOfTwo((var12 ? this.currentWidth : this.currentHeight) + var4) > (var12 ? this.maxWidth : this.maxHeight)) {
            return false;
        }
        Slot var15;
        if (var12) {
            if (p_94311_1_.getWidth() > p_94311_1_.getHeight()) {
                p_94311_1_.rotate();
            }
            if (this.currentHeight == 0) {
                this.currentHeight = p_94311_1_.getHeight();
            }
            var15 = new Slot(this.currentWidth, 0, p_94311_1_.getWidth(), this.currentHeight);
            this.currentWidth += p_94311_1_.getWidth();
        }
        else {
            var15 = new Slot(0, this.currentHeight, this.currentWidth, p_94311_1_.getHeight());
            this.currentHeight += p_94311_1_.getHeight();
        }
        var15.addSlot(p_94311_1_);
        this.stitchSlots.add(var15);
        return true;
    }
    
    public static class Holder implements Comparable
    {
        private final TextureAtlasSprite theTexture;
        private final int width;
        private final int height;
        private final int mipmapLevelHolder;
        private boolean rotated;
        private float scaleFactor;
        private static final String __OBFID = "CL_00001055";
        
        public Holder(final TextureAtlasSprite p_i45094_1_, final int p_i45094_2_) {
            this.scaleFactor = 1.0f;
            this.theTexture = p_i45094_1_;
            this.width = p_i45094_1_.getIconWidth();
            this.height = p_i45094_1_.getIconHeight();
            this.mipmapLevelHolder = p_i45094_2_;
            this.rotated = (getMipmapDimension(this.height, p_i45094_2_) > getMipmapDimension(this.width, p_i45094_2_));
        }
        
        public TextureAtlasSprite getAtlasSprite() {
            return this.theTexture;
        }
        
        public int getWidth() {
            return this.rotated ? getMipmapDimension((int)(this.height * this.scaleFactor), this.mipmapLevelHolder) : getMipmapDimension((int)(this.width * this.scaleFactor), this.mipmapLevelHolder);
        }
        
        public int getHeight() {
            return this.rotated ? getMipmapDimension((int)(this.width * this.scaleFactor), this.mipmapLevelHolder) : getMipmapDimension((int)(this.height * this.scaleFactor), this.mipmapLevelHolder);
        }
        
        public void rotate() {
            this.rotated = !this.rotated;
        }
        
        public boolean isRotated() {
            return this.rotated;
        }
        
        public void setNewDimension(final int p_94196_1_) {
            if (this.width > p_94196_1_ && this.height > p_94196_1_) {
                this.scaleFactor = p_94196_1_ / Math.min(this.width, this.height);
            }
        }
        
        @Override
        public String toString() {
            return "Holder{width=" + this.width + ", height=" + this.height + '}';
        }
        
        public int compareTo(final Holder p_compareTo_1_) {
            int var2;
            if (this.getHeight() == p_compareTo_1_.getHeight()) {
                if (this.getWidth() == p_compareTo_1_.getWidth()) {
                    if (this.theTexture.getIconName() == null) {
                        return (p_compareTo_1_.theTexture.getIconName() == null) ? 0 : -1;
                    }
                    return this.theTexture.getIconName().compareTo(p_compareTo_1_.theTexture.getIconName());
                }
                else {
                    var2 = ((this.getWidth() < p_compareTo_1_.getWidth()) ? 1 : -1);
                }
            }
            else {
                var2 = ((this.getHeight() < p_compareTo_1_.getHeight()) ? 1 : -1);
            }
            return var2;
        }
        
        @Override
        public int compareTo(final Object p_compareTo_1_) {
            return this.compareTo((Holder)p_compareTo_1_);
        }
    }
    
    public static class Slot
    {
        private final int originX;
        private final int originY;
        private final int width;
        private final int height;
        private List subSlots;
        private Holder holder;
        private static final String __OBFID = "CL_00001056";
        
        public Slot(final int p_i1277_1_, final int p_i1277_2_, final int p_i1277_3_, final int p_i1277_4_) {
            this.originX = p_i1277_1_;
            this.originY = p_i1277_2_;
            this.width = p_i1277_3_;
            this.height = p_i1277_4_;
        }
        
        public Holder getStitchHolder() {
            return this.holder;
        }
        
        public int getOriginX() {
            return this.originX;
        }
        
        public int getOriginY() {
            return this.originY;
        }
        
        public boolean addSlot(final Holder p_94182_1_) {
            if (this.holder != null) {
                return false;
            }
            final int var2 = p_94182_1_.getWidth();
            final int var3 = p_94182_1_.getHeight();
            if (var2 > this.width || var3 > this.height) {
                return false;
            }
            if (var2 == this.width && var3 == this.height) {
                this.holder = p_94182_1_;
                return true;
            }
            if (this.subSlots == null) {
                (this.subSlots = Lists.newArrayListWithCapacity(1)).add(new Slot(this.originX, this.originY, var2, var3));
                final int var4 = this.width - var2;
                final int var5 = this.height - var3;
                if (var5 > 0 && var4 > 0) {
                    final int var6 = Math.max(this.height, var4);
                    final int var7 = Math.max(this.width, var5);
                    if (var6 >= var7) {
                        this.subSlots.add(new Slot(this.originX, this.originY + var3, var2, var5));
                        this.subSlots.add(new Slot(this.originX + var2, this.originY, var4, this.height));
                    }
                    else {
                        this.subSlots.add(new Slot(this.originX + var2, this.originY, var4, var3));
                        this.subSlots.add(new Slot(this.originX, this.originY + var3, this.width, var5));
                    }
                }
                else if (var4 == 0) {
                    this.subSlots.add(new Slot(this.originX, this.originY + var3, var2, var5));
                }
                else if (var5 == 0) {
                    this.subSlots.add(new Slot(this.originX + var2, this.originY, var4, var3));
                }
            }
            for (final Slot var9 : this.subSlots) {
                if (var9.addSlot(p_94182_1_)) {
                    return true;
                }
            }
            return false;
        }
        
        public void getAllStitchSlots(final List p_94184_1_) {
            if (this.holder != null) {
                p_94184_1_.add(this);
            }
            else if (this.subSlots != null) {
                for (final Slot var3 : this.subSlots) {
                    var3.getAllStitchSlots(p_94184_1_);
                }
            }
        }
        
        @Override
        public String toString() {
            return "Slot{originX=" + this.originX + ", originY=" + this.originY + ", width=" + this.width + ", height=" + this.height + ", texture=" + this.holder + ", subSlots=" + this.subSlots + '}';
        }
    }
}
