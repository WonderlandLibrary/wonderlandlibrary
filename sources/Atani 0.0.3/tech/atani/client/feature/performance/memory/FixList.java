package tech.atani.client.feature.performance.memory;

import java.util.ArrayList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class FixList
extends ArrayList<int[][]> {
    final TextureAtlasSprite sprite;
    boolean reload = true;

    public FixList(TextureAtlasSprite textureAtlasSprite) {
        this.sprite = textureAtlasSprite;
    }

    @Override
    public int size() {
        if (this.reload) {
            this.reload();
        }
        return super.size();
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public int[][] get(int n) {
        if (this.reload) {
            this.reload();
        }
        return (int[][])super.get(n);
    }

    @Override
    public void clear() {
        this.reload = true;
        super.clear();
    }

    public void reload() {
        this.reload = false;
        TextureFix.reloadTextureData(this.sprite);
        TextureFix.markForUnload(this.sprite);
    }
}
