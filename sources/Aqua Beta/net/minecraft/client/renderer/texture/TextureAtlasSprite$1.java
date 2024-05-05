package net.minecraft.client.renderer.texture;

import java.util.concurrent.Callable;

class TextureAtlasSprite.1
implements Callable<String> {
    final /* synthetic */ int[][] val$aint;

    TextureAtlasSprite.1(int[][] nArray) {
        this.val$aint = nArray;
    }

    public String call() throws Exception {
        StringBuilder stringbuilder = new StringBuilder();
        for (int[] aint1 : this.val$aint) {
            if (stringbuilder.length() > 0) {
                stringbuilder.append(", ");
            }
            stringbuilder.append((Object)(aint1 == null ? "null" : Integer.valueOf((int)aint1.length)));
        }
        return stringbuilder.toString();
    }
}
