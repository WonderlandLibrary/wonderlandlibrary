package net.minecraft.client.renderer.texture;

import java.util.concurrent.Callable;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

class TextureMap.2
implements Callable<String> {
    final /* synthetic */ TextureAtlasSprite val$textureatlassprite1;

    TextureMap.2(TextureAtlasSprite textureAtlasSprite) {
        this.val$textureatlassprite1 = textureAtlasSprite;
    }

    public String call() throws Exception {
        return this.val$textureatlassprite1.getIconWidth() + " x " + this.val$textureatlassprite1.getIconHeight();
    }
}
