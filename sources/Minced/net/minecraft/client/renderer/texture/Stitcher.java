// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.texture;

import net.optifine.util.MathUtils;
import java.util.Iterator;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.StitcherException;
import java.util.Arrays;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;

public class Stitcher
{
    private final int mipmapLevelStitcher;
    private final Set<Holder> setStitchHolders;
    private final List<Slot> stitchSlots;
    private int currentWidth;
    private int currentHeight;
    private final int maxWidth;
    private final int maxHeight;
    private final int maxTileDimension;
    
    public Stitcher(final int maxWidthIn, final int maxHeightIn, final int maxTileDimensionIn, final int mipmapLevelStitcherIn) {
        this.setStitchHolders = (Set<Holder>)Sets.newHashSetWithExpectedSize(256);
        this.stitchSlots = (List<Slot>)Lists.newArrayListWithCapacity(256);
        this.mipmapLevelStitcher = mipmapLevelStitcherIn;
        this.maxWidth = maxWidthIn;
        this.maxHeight = maxHeightIn;
        this.maxTileDimension = maxTileDimensionIn;
    }
    
    public int getCurrentWidth() {
        return this.currentWidth;
    }
    
    public int getCurrentHeight() {
        return this.currentHeight;
    }
    
    public void addSprite(final TextureAtlasSprite textureAtlas) {
        final Holder stitcher$holder = new Holder(textureAtlas, this.mipmapLevelStitcher);
        if (this.maxTileDimension > 0) {
            stitcher$holder.setNewDimension(this.maxTileDimension);
        }
        this.setStitchHolders.add(stitcher$holder);
    }
    
    public void doStitch() {
        final Holder[] astitcher$holder = this.setStitchHolders.toArray(new Holder[this.setStitchHolders.size()]);
        Arrays.sort(astitcher$holder);
        for (final Holder stitcher$holder : astitcher$holder) {
            if (!this.allocateSlot(stitcher$holder)) {
                final String s = String.format("Unable to fit: %s, size: %dx%d, atlas: %dx%d, atlasMax: %dx%d - Maybe try a lower resolution resourcepack?", stitcher$holder.getAtlasSprite().getIconName(), stitcher$holder.getAtlasSprite().getIconWidth(), stitcher$holder.getAtlasSprite().getIconHeight(), this.currentWidth, this.currentHeight, this.maxWidth, this.maxHeight);
                throw new StitcherException(stitcher$holder, s);
            }
        }
        this.currentWidth = MathHelper.smallestEncompassingPowerOfTwo(this.currentWidth);
        this.currentHeight = MathHelper.smallestEncompassingPowerOfTwo(this.currentHeight);
    }
    
    public List<TextureAtlasSprite> getStichSlots() {
        final List<Slot> list = (List<Slot>)Lists.newArrayList();
        for (final Slot stitcher$slot : this.stitchSlots) {
            stitcher$slot.getAllStitchSlots(list);
        }
        final List<TextureAtlasSprite> list2 = (List<TextureAtlasSprite>)Lists.newArrayList();
        for (final Slot stitcher$slot2 : list) {
            final Holder stitcher$holder = stitcher$slot2.getStitchHolder();
            final TextureAtlasSprite textureatlassprite = stitcher$holder.getAtlasSprite();
            textureatlassprite.initSprite(this.currentWidth, this.currentHeight, stitcher$slot2.getOriginX(), stitcher$slot2.getOriginY(), stitcher$holder.isRotated());
            list2.add(textureatlassprite);
        }
        return list2;
    }
    
    private static int getMipmapDimension(final int p_147969_0_, final int p_147969_1_) {
        return (p_147969_0_ >> p_147969_1_) + (((p_147969_0_ & (1 << p_147969_1_) - 1) != 0x0) ? 1 : 0) << p_147969_1_;
    }
    
    private boolean allocateSlot(final Holder p_94310_1_) {
        final TextureAtlasSprite textureatlassprite = p_94310_1_.getAtlasSprite();
        final boolean flag = textureatlassprite.getIconWidth() != textureatlassprite.getIconHeight();
        for (int i = 0; i < this.stitchSlots.size(); ++i) {
            if (this.stitchSlots.get(i).addSlot(p_94310_1_)) {
                return true;
            }
            if (flag) {
                p_94310_1_.rotate();
                if (this.stitchSlots.get(i).addSlot(p_94310_1_)) {
                    return true;
                }
                p_94310_1_.rotate();
            }
        }
        return this.expandAndAllocateSlot(p_94310_1_);
    }
    
    private boolean expandAndAllocateSlot(final Holder p_94311_1_) {
        final int i = Math.min(p_94311_1_.getWidth(), p_94311_1_.getHeight());
        final int j = Math.max(p_94311_1_.getWidth(), p_94311_1_.getHeight());
        final int k = MathHelper.smallestEncompassingPowerOfTwo(this.currentWidth);
        final int l = MathHelper.smallestEncompassingPowerOfTwo(this.currentHeight);
        final int i2 = MathHelper.smallestEncompassingPowerOfTwo(this.currentWidth + i);
        final int j2 = MathHelper.smallestEncompassingPowerOfTwo(this.currentHeight + i);
        final boolean flag = i2 <= this.maxWidth;
        final boolean flag2 = j2 <= this.maxHeight;
        if (!flag && !flag2) {
            return false;
        }
        final int k2 = MathUtils.roundDownToPowerOfTwo(this.currentHeight);
        boolean flag3 = flag && i2 <= 2 * k2;
        if (this.currentWidth == 0 && this.currentHeight == 0) {
            flag3 = true;
        }
        Slot stitcher$slot;
        if (flag3) {
            if (p_94311_1_.getWidth() > p_94311_1_.getHeight()) {
                p_94311_1_.rotate();
            }
            if (this.currentHeight == 0) {
                this.currentHeight = p_94311_1_.getHeight();
            }
            stitcher$slot = new Slot(this.currentWidth, 0, p_94311_1_.getWidth(), this.currentHeight);
            this.currentWidth += p_94311_1_.getWidth();
        }
        else {
            stitcher$slot = new Slot(0, this.currentHeight, this.currentWidth, p_94311_1_.getHeight());
            this.currentHeight += p_94311_1_.getHeight();
        }
        stitcher$slot.addSlot(p_94311_1_);
        this.stitchSlots.add(stitcher$slot);
        return true;
    }
    
    public static class Holder implements Comparable<Holder>
    {
        private final TextureAtlasSprite sprite;
        private final int width;
        private final int height;
        private final int mipmapLevelHolder;
        private boolean rotated;
        private float scaleFactor;
        
        public Holder(final TextureAtlasSprite theTextureIn, final int mipmapLevelHolderIn) {
            this.scaleFactor = 1.0f;
            this.sprite = theTextureIn;
            this.width = theTextureIn.getIconWidth();
            this.height = theTextureIn.getIconHeight();
            this.mipmapLevelHolder = mipmapLevelHolderIn;
            this.rotated = (getMipmapDimension(this.height, mipmapLevelHolderIn) > getMipmapDimension(this.width, mipmapLevelHolderIn));
        }
        
        public TextureAtlasSprite getAtlasSprite() {
            return this.sprite;
        }
        
        public int getWidth() {
            final int i = this.rotated ? this.height : this.width;
            return getMipmapDimension((int)(i * this.scaleFactor), this.mipmapLevelHolder);
        }
        
        public int getHeight() {
            final int i = this.rotated ? this.width : this.height;
            return getMipmapDimension((int)(i * this.scaleFactor), this.mipmapLevelHolder);
        }
        
        public void rotate() {
            this.rotated = !this.rotated;
        }
        
        public boolean isRotated() {
            return this.rotated;
        }
        
        public void setNewDimension(final int p_94196_1_) {
            if (this.width > p_94196_1_ && this.height > p_94196_1_) {
                this.scaleFactor = p_94196_1_ / (float)Math.min(this.width, this.height);
            }
        }
        
        @Override
        public String toString() {
            return "Holder{width=" + this.width + ", height=" + this.height + ", name=" + this.sprite.getIconName() + '}';
        }
        
        @Override
        public int compareTo(final Holder p_compareTo_1_) {
            int i;
            if (this.getHeight() == p_compareTo_1_.getHeight()) {
                if (this.getWidth() == p_compareTo_1_.getWidth()) {
                    if (this.sprite.getIconName() == null) {
                        return (p_compareTo_1_.sprite.getIconName() == null) ? 0 : -1;
                    }
                    return this.sprite.getIconName().compareTo(p_compareTo_1_.sprite.getIconName());
                }
                else {
                    i = ((this.getWidth() < p_compareTo_1_.getWidth()) ? 1 : -1);
                }
            }
            else {
                i = ((this.getHeight() < p_compareTo_1_.getHeight()) ? 1 : -1);
            }
            return i;
        }
    }
    
    public static class Slot
    {
        private final int originX;
        private final int originY;
        private final int width;
        private final int height;
        private List<Slot> subSlots;
        private Holder holder;
        
        public Slot(final int originXIn, final int originYIn, final int widthIn, final int heightIn) {
            this.originX = originXIn;
            this.originY = originYIn;
            this.width = widthIn;
            this.height = heightIn;
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
        
        public boolean addSlot(final Holder holderIn) {
            if (this.holder != null) {
                return false;
            }
            final int i = holderIn.getWidth();
            final int j = holderIn.getHeight();
            if (i > this.width || j > this.height) {
                return false;
            }
            if (i == this.width && j == this.height) {
                this.holder = holderIn;
                return true;
            }
            if (this.subSlots == null) {
                (this.subSlots = (List<Slot>)Lists.newArrayListWithCapacity(1)).add(new Slot(this.originX, this.originY, i, j));
                final int k = this.width - i;
                final int l = this.height - j;
                if (l > 0 && k > 0) {
                    final int i2 = Math.max(this.height, k);
                    final int j2 = Math.max(this.width, l);
                    if (i2 >= j2) {
                        this.subSlots.add(new Slot(this.originX, this.originY + j, i, l));
                        this.subSlots.add(new Slot(this.originX + i, this.originY, k, this.height));
                    }
                    else {
                        this.subSlots.add(new Slot(this.originX + i, this.originY, k, j));
                        this.subSlots.add(new Slot(this.originX, this.originY + j, this.width, l));
                    }
                }
                else if (k == 0) {
                    this.subSlots.add(new Slot(this.originX, this.originY + j, i, l));
                }
                else if (l == 0) {
                    this.subSlots.add(new Slot(this.originX + i, this.originY, k, j));
                }
            }
            for (final Slot stitcher$slot : this.subSlots) {
                if (stitcher$slot.addSlot(holderIn)) {
                    return true;
                }
            }
            return false;
        }
        
        public void getAllStitchSlots(final List<Slot> p_94184_1_) {
            if (this.holder != null) {
                p_94184_1_.add(this);
            }
            else if (this.subSlots != null) {
                for (final Slot stitcher$slot : this.subSlots) {
                    stitcher$slot.getAllStitchSlots(p_94184_1_);
                }
            }
        }
        
        @Override
        public String toString() {
            return "Slot{originX=" + this.originX + ", originY=" + this.originY + ", width=" + this.width + ", height=" + this.height + ", texture=" + this.holder + ", subSlots=" + this.subSlots + '}';
        }
    }
}
