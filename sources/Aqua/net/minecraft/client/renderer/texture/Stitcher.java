package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import net.minecraft.client.renderer.StitcherException;
import net.minecraft.client.renderer.texture.Stitcher;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.MathHelper;

public class Stitcher {
    private final int mipmapLevelStitcher;
    private final Set<Holder> setStitchHolders = Sets.newHashSetWithExpectedSize((int)256);
    private final List<Slot> stitchSlots = Lists.newArrayListWithCapacity((int)256);
    private int currentWidth;
    private int currentHeight;
    private final int maxWidth;
    private final int maxHeight;
    private final boolean forcePowerOf2;
    private final int maxTileDimension;

    public Stitcher(int maxTextureWidth, int maxTextureHeight, boolean p_i45095_3_, int p_i45095_4_, int mipmapLevel) {
        this.mipmapLevelStitcher = mipmapLevel;
        this.maxWidth = maxTextureWidth;
        this.maxHeight = maxTextureHeight;
        this.forcePowerOf2 = p_i45095_3_;
        this.maxTileDimension = p_i45095_4_;
    }

    public int getCurrentWidth() {
        return this.currentWidth;
    }

    public int getCurrentHeight() {
        return this.currentHeight;
    }

    public void addSprite(TextureAtlasSprite p_110934_1_) {
        Holder stitcher$holder = new Holder(p_110934_1_, this.mipmapLevelStitcher);
        if (this.maxTileDimension > 0) {
            stitcher$holder.setNewDimension(this.maxTileDimension);
        }
        this.setStitchHolders.add((Object)stitcher$holder);
    }

    public void doStitch() {
        Holder[] astitcher$holder = (Holder[])this.setStitchHolders.toArray((Object[])new Holder[this.setStitchHolders.size()]);
        Arrays.sort((Object[])astitcher$holder);
        for (Holder stitcher$holder : astitcher$holder) {
            if (this.allocateSlot(stitcher$holder)) continue;
            String s = String.format((String)"Unable to fit: %s, size: %dx%d, atlas: %dx%d, atlasMax: %dx%d - Maybe try a lower resolution resourcepack?", (Object[])new Object[]{stitcher$holder.getAtlasSprite().getIconName(), stitcher$holder.getAtlasSprite().getIconWidth(), stitcher$holder.getAtlasSprite().getIconHeight(), this.currentWidth, this.currentHeight, this.maxWidth, this.maxHeight});
            throw new StitcherException(stitcher$holder, s);
        }
        if (this.forcePowerOf2) {
            this.currentWidth = MathHelper.roundUpToPowerOfTwo((int)this.currentWidth);
            this.currentHeight = MathHelper.roundUpToPowerOfTwo((int)this.currentHeight);
        }
    }

    public List<TextureAtlasSprite> getStichSlots() {
        ArrayList list = Lists.newArrayList();
        for (Slot stitcher$slot : this.stitchSlots) {
            stitcher$slot.getAllStitchSlots((List)list);
        }
        ArrayList list1 = Lists.newArrayList();
        for (Slot stitcher$slot1 : list) {
            Holder stitcher$holder = stitcher$slot1.getStitchHolder();
            TextureAtlasSprite textureatlassprite = stitcher$holder.getAtlasSprite();
            textureatlassprite.initSprite(this.currentWidth, this.currentHeight, stitcher$slot1.getOriginX(), stitcher$slot1.getOriginY(), stitcher$holder.isRotated());
            list1.add((Object)textureatlassprite);
        }
        return list1;
    }

    private static int getMipmapDimension(int p_147969_0_, int p_147969_1_) {
        return (p_147969_0_ >> p_147969_1_) + ((p_147969_0_ & (1 << p_147969_1_) - 1) == 0 ? 0 : 1) << p_147969_1_;
    }

    private boolean allocateSlot(Holder p_94310_1_) {
        for (int i = 0; i < this.stitchSlots.size(); ++i) {
            if (((Slot)this.stitchSlots.get(i)).addSlot(p_94310_1_)) {
                return true;
            }
            p_94310_1_.rotate();
            if (((Slot)this.stitchSlots.get(i)).addSlot(p_94310_1_)) {
                return true;
            }
            p_94310_1_.rotate();
        }
        return this.expandAndAllocateSlot(p_94310_1_);
    }

    private boolean expandAndAllocateSlot(Holder p_94311_1_) {
        Slot stitcher$slot;
        boolean flag1;
        boolean flag;
        int i = Math.min((int)p_94311_1_.getWidth(), (int)p_94311_1_.getHeight());
        boolean bl = flag = this.currentWidth == 0 && this.currentHeight == 0;
        if (this.forcePowerOf2) {
            boolean flag5;
            boolean flag3;
            int j = MathHelper.roundUpToPowerOfTwo((int)this.currentWidth);
            int k = MathHelper.roundUpToPowerOfTwo((int)this.currentHeight);
            int l = MathHelper.roundUpToPowerOfTwo((int)(this.currentWidth + i));
            int i1 = MathHelper.roundUpToPowerOfTwo((int)(this.currentHeight + i));
            boolean flag2 = l <= this.maxWidth;
            boolean bl2 = flag3 = i1 <= this.maxHeight;
            if (!flag2 && !flag3) {
                return false;
            }
            boolean flag4 = j != l;
            boolean bl3 = flag5 = k != i1;
            flag1 = flag4 ^ flag5 ? !flag4 : flag2 && j <= k;
        } else {
            boolean flag7;
            boolean flag6 = this.currentWidth + i <= this.maxWidth;
            boolean bl4 = flag7 = this.currentHeight + i <= this.maxHeight;
            if (!flag6 && !flag7) {
                return false;
            }
            flag1 = flag6 && (flag || this.currentWidth <= this.currentHeight);
        }
        int j1 = Math.max((int)p_94311_1_.getWidth(), (int)p_94311_1_.getHeight());
        if (MathHelper.roundUpToPowerOfTwo((int)((!flag1 ? this.currentHeight : this.currentWidth) + j1)) > (!flag1 ? this.maxHeight : this.maxWidth)) {
            return false;
        }
        if (flag1) {
            if (p_94311_1_.getWidth() > p_94311_1_.getHeight()) {
                p_94311_1_.rotate();
            }
            if (this.currentHeight == 0) {
                this.currentHeight = p_94311_1_.getHeight();
            }
            stitcher$slot = new Slot(this.currentWidth, 0, p_94311_1_.getWidth(), this.currentHeight);
            this.currentWidth += p_94311_1_.getWidth();
        } else {
            stitcher$slot = new Slot(0, this.currentHeight, this.currentWidth, p_94311_1_.getHeight());
            this.currentHeight += p_94311_1_.getHeight();
        }
        stitcher$slot.addSlot(p_94311_1_);
        this.stitchSlots.add((Object)stitcher$slot);
        return true;
    }

    static /* synthetic */ int access$000(int x0, int x1) {
        return Stitcher.getMipmapDimension(x0, x1);
    }
}
