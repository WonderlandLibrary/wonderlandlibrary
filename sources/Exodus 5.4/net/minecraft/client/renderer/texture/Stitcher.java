/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Sets
 */
package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import net.minecraft.client.renderer.StitcherException;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.MathHelper;

public class Stitcher {
    private final int maxHeight;
    private final List<Slot> stitchSlots;
    private final int mipmapLevelStitcher;
    private final int maxTileDimension;
    private final boolean forcePowerOf2;
    private int currentHeight;
    private int currentWidth;
    private final Set<Holder> setStitchHolders = Sets.newHashSetWithExpectedSize((int)256);
    private final int maxWidth;

    public Stitcher(int n, int n2, boolean bl, int n3, int n4) {
        this.stitchSlots = Lists.newArrayListWithCapacity((int)256);
        this.mipmapLevelStitcher = n4;
        this.maxWidth = n;
        this.maxHeight = n2;
        this.forcePowerOf2 = bl;
        this.maxTileDimension = n3;
    }

    private boolean expandAndAllocateSlot(Holder holder) {
        Slot slot;
        boolean bl;
        int n;
        int n2;
        boolean bl2;
        int n3 = Math.min(holder.getWidth(), holder.getHeight());
        boolean bl3 = bl2 = this.currentWidth == 0 && this.currentHeight == 0;
        if (this.forcePowerOf2) {
            boolean bl4;
            boolean bl5;
            n2 = MathHelper.roundUpToPowerOfTwo(this.currentWidth);
            n = MathHelper.roundUpToPowerOfTwo(this.currentHeight);
            int n4 = MathHelper.roundUpToPowerOfTwo(this.currentWidth + n3);
            int n5 = MathHelper.roundUpToPowerOfTwo(this.currentHeight + n3);
            boolean bl6 = n4 <= this.maxWidth;
            boolean bl7 = bl5 = n5 <= this.maxHeight;
            if (!bl6 && !bl5) {
                return false;
            }
            boolean bl8 = n2 != n4;
            boolean bl9 = bl4 = n != n5;
            bl = bl8 ^ bl4 ? !bl8 : bl6 && n2 <= n;
        } else {
            n2 = this.currentWidth + n3 <= this.maxWidth ? 1 : 0;
            int n6 = n = this.currentHeight + n3 <= this.maxHeight ? 1 : 0;
            if (n2 == 0 && n == 0) {
                return false;
            }
            bl = n2 != 0 && (bl2 || this.currentWidth <= this.currentHeight);
        }
        n2 = Math.max(holder.getWidth(), holder.getHeight());
        if (MathHelper.roundUpToPowerOfTwo((bl ? this.currentHeight : this.currentWidth) + n2) > (bl ? this.maxHeight : this.maxWidth)) {
            return false;
        }
        if (bl) {
            if (holder.getWidth() > holder.getHeight()) {
                holder.rotate();
            }
            if (this.currentHeight == 0) {
                this.currentHeight = holder.getHeight();
            }
            slot = new Slot(this.currentWidth, 0, holder.getWidth(), this.currentHeight);
            this.currentWidth += holder.getWidth();
        } else {
            slot = new Slot(0, this.currentHeight, this.currentWidth, holder.getHeight());
            this.currentHeight += holder.getHeight();
        }
        slot.addSlot(holder);
        this.stitchSlots.add(slot);
        return true;
    }

    public void doStitch() {
        Object[] objectArray = this.setStitchHolders.toArray(new Holder[this.setStitchHolders.size()]);
        Arrays.sort(objectArray);
        Object[] objectArray2 = objectArray;
        int n = objectArray.length;
        int n2 = 0;
        while (n2 < n) {
            Object object = objectArray2[n2];
            if (!this.allocateSlot((Holder)object)) {
                String string = String.format("Unable to fit: %s - size: %dx%d - Maybe try a lowerresolution resourcepack?", ((Holder)object).getAtlasSprite().getIconName(), ((Holder)object).getAtlasSprite().getIconWidth(), ((Holder)object).getAtlasSprite().getIconHeight());
                throw new StitcherException((Holder)object, string);
            }
            ++n2;
        }
        if (this.forcePowerOf2) {
            this.currentWidth = MathHelper.roundUpToPowerOfTwo(this.currentWidth);
            this.currentHeight = MathHelper.roundUpToPowerOfTwo(this.currentHeight);
        }
    }

    private boolean allocateSlot(Holder holder) {
        int n = 0;
        while (n < this.stitchSlots.size()) {
            if (this.stitchSlots.get(n).addSlot(holder)) {
                return true;
            }
            holder.rotate();
            if (this.stitchSlots.get(n).addSlot(holder)) {
                return true;
            }
            holder.rotate();
            ++n;
        }
        return this.expandAndAllocateSlot(holder);
    }

    public int getCurrentWidth() {
        return this.currentWidth;
    }

    public List<TextureAtlasSprite> getStichSlots() {
        ArrayList arrayList = Lists.newArrayList();
        for (Slot object2 : this.stitchSlots) {
            object2.getAllStitchSlots(arrayList);
        }
        ArrayList arrayList2 = Lists.newArrayList();
        for (Object object3 : arrayList) {
            Holder holder = ((Slot)object3).getStitchHolder();
            TextureAtlasSprite textureAtlasSprite = holder.getAtlasSprite();
            textureAtlasSprite.initSprite(this.currentWidth, this.currentHeight, ((Slot)object3).getOriginX(), ((Slot)object3).getOriginY(), holder.isRotated());
            arrayList2.add(textureAtlasSprite);
        }
        return arrayList2;
    }

    public void addSprite(TextureAtlasSprite textureAtlasSprite) {
        Holder holder = new Holder(textureAtlasSprite, this.mipmapLevelStitcher);
        if (this.maxTileDimension > 0) {
            holder.setNewDimension(this.maxTileDimension);
        }
        this.setStitchHolders.add(holder);
    }

    private static int getMipmapDimension(int n, int n2) {
        return (n >> n2) + ((n & (1 << n2) - 1) == 0 ? 0 : 1) << n2;
    }

    public int getCurrentHeight() {
        return this.currentHeight;
    }

    public static class Slot {
        private final int width;
        private Holder holder;
        private final int height;
        private final int originY;
        private List<Slot> subSlots;
        private final int originX;

        public Slot(int n, int n2, int n3, int n4) {
            this.originX = n;
            this.originY = n2;
            this.width = n3;
            this.height = n4;
        }

        public boolean addSlot(Holder holder) {
            if (this.holder != null) {
                return false;
            }
            int n = holder.getWidth();
            int n2 = holder.getHeight();
            if (n <= this.width && n2 <= this.height) {
                if (n == this.width && n2 == this.height) {
                    this.holder = holder;
                    return true;
                }
                if (this.subSlots == null) {
                    this.subSlots = Lists.newArrayListWithCapacity((int)1);
                    this.subSlots.add(new Slot(this.originX, this.originY, n, n2));
                    int n3 = this.width - n;
                    int n4 = this.height - n2;
                    if (n4 > 0 && n3 > 0) {
                        int n5;
                        int n6 = Math.max(this.height, n3);
                        if (n6 >= (n5 = Math.max(this.width, n4))) {
                            this.subSlots.add(new Slot(this.originX, this.originY + n2, n, n4));
                            this.subSlots.add(new Slot(this.originX + n, this.originY, n3, this.height));
                        } else {
                            this.subSlots.add(new Slot(this.originX + n, this.originY, n3, n2));
                            this.subSlots.add(new Slot(this.originX, this.originY + n2, this.width, n4));
                        }
                    } else if (n3 == 0) {
                        this.subSlots.add(new Slot(this.originX, this.originY + n2, n, n4));
                    } else if (n4 == 0) {
                        this.subSlots.add(new Slot(this.originX + n, this.originY, n3, n2));
                    }
                }
                for (Slot slot : this.subSlots) {
                    if (!slot.addSlot(holder)) continue;
                    return true;
                }
                return false;
            }
            return false;
        }

        public String toString() {
            return "Slot{originX=" + this.originX + ", originY=" + this.originY + ", width=" + this.width + ", height=" + this.height + ", texture=" + this.holder + ", subSlots=" + this.subSlots + '}';
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

        public void getAllStitchSlots(List<Slot> list) {
            if (this.holder != null) {
                list.add(this);
            } else if (this.subSlots != null) {
                for (Slot slot : this.subSlots) {
                    slot.getAllStitchSlots(list);
                }
            }
        }
    }

    public static class Holder
    implements Comparable<Holder> {
        private final int width;
        private float scaleFactor = 1.0f;
        private boolean rotated;
        private final int mipmapLevelHolder;
        private final int height;
        private final TextureAtlasSprite theTexture;

        public void rotate() {
            this.rotated = !this.rotated;
        }

        public boolean isRotated() {
            return this.rotated;
        }

        public int getWidth() {
            return this.rotated ? Stitcher.getMipmapDimension((int)((float)this.height * this.scaleFactor), this.mipmapLevelHolder) : Stitcher.getMipmapDimension((int)((float)this.width * this.scaleFactor), this.mipmapLevelHolder);
        }

        public TextureAtlasSprite getAtlasSprite() {
            return this.theTexture;
        }

        public Holder(TextureAtlasSprite textureAtlasSprite, int n) {
            this.theTexture = textureAtlasSprite;
            this.width = textureAtlasSprite.getIconWidth();
            this.height = textureAtlasSprite.getIconHeight();
            this.mipmapLevelHolder = n;
            this.rotated = Stitcher.getMipmapDimension(this.height, n) > Stitcher.getMipmapDimension(this.width, n);
        }

        public int getHeight() {
            return this.rotated ? Stitcher.getMipmapDimension((int)((float)this.width * this.scaleFactor), this.mipmapLevelHolder) : Stitcher.getMipmapDimension((int)((float)this.height * this.scaleFactor), this.mipmapLevelHolder);
        }

        public String toString() {
            return "Holder{width=" + this.width + ", height=" + this.height + '}';
        }

        @Override
        public int compareTo(Holder holder) {
            int n;
            if (this.getHeight() == holder.getHeight()) {
                if (this.getWidth() == holder.getWidth()) {
                    if (this.theTexture.getIconName() == null) {
                        return holder.theTexture.getIconName() == null ? 0 : -1;
                    }
                    return this.theTexture.getIconName().compareTo(holder.theTexture.getIconName());
                }
                n = this.getWidth() < holder.getWidth() ? 1 : -1;
            } else {
                n = this.getHeight() < holder.getHeight() ? 1 : -1;
            }
            return n;
        }

        public void setNewDimension(int n) {
            if (this.width > n && this.height > n) {
                this.scaleFactor = (float)n / (float)Math.min(this.width, this.height);
            }
        }
    }
}

