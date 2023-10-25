package net.minecraft.client.renderer.texture;

import net.minecraft.client.renderer.texture.Stitcher;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

/*
 * Exception performing whole class analysis ignored.
 */
public static class Stitcher.Holder
implements Comparable<Stitcher.Holder> {
    private final TextureAtlasSprite theTexture;
    private final int width;
    private final int height;
    private final int mipmapLevelHolder;
    private boolean rotated;
    private float scaleFactor = 1.0f;

    public Stitcher.Holder(TextureAtlasSprite p_i45094_1_, int p_i45094_2_) {
        this.theTexture = p_i45094_1_;
        this.width = p_i45094_1_.getIconWidth();
        this.height = p_i45094_1_.getIconHeight();
        this.mipmapLevelHolder = p_i45094_2_;
        this.rotated = Stitcher.access$000((int)this.height, (int)p_i45094_2_) > Stitcher.access$000((int)this.width, (int)p_i45094_2_);
    }

    public TextureAtlasSprite getAtlasSprite() {
        return this.theTexture;
    }

    public int getWidth() {
        return this.rotated ? Stitcher.access$000((int)((int)((float)this.height * this.scaleFactor)), (int)this.mipmapLevelHolder) : Stitcher.access$000((int)((int)((float)this.width * this.scaleFactor)), (int)this.mipmapLevelHolder);
    }

    public int getHeight() {
        return this.rotated ? Stitcher.access$000((int)((int)((float)this.width * this.scaleFactor)), (int)this.mipmapLevelHolder) : Stitcher.access$000((int)((int)((float)this.height * this.scaleFactor)), (int)this.mipmapLevelHolder);
    }

    public void rotate() {
        this.rotated = !this.rotated;
    }

    public boolean isRotated() {
        return this.rotated;
    }

    public void setNewDimension(int p_94196_1_) {
        if (this.width > p_94196_1_ && this.height > p_94196_1_) {
            this.scaleFactor = (float)p_94196_1_ / (float)Math.min((int)this.width, (int)this.height);
        }
    }

    public String toString() {
        return "Holder{width=" + this.width + ", height=" + this.height + '}';
    }

    public int compareTo(Stitcher.Holder p_compareTo_1_) {
        int i;
        if (this.getHeight() == p_compareTo_1_.getHeight()) {
            if (this.getWidth() == p_compareTo_1_.getWidth()) {
                if (this.theTexture.getIconName() == null) {
                    return p_compareTo_1_.theTexture.getIconName() == null ? 0 : -1;
                }
                return this.theTexture.getIconName().compareTo(p_compareTo_1_.theTexture.getIconName());
            }
            i = this.getWidth() < p_compareTo_1_.getWidth() ? 1 : -1;
        } else {
            i = this.getHeight() < p_compareTo_1_.getHeight() ? 1 : -1;
        }
        return i;
    }
}
