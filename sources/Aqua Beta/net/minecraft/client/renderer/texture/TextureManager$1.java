package net.minecraft.client.renderer.texture;

import java.util.concurrent.Callable;
import net.minecraft.client.renderer.texture.ITextureObject;

class TextureManager.1
implements Callable<String> {
    final /* synthetic */ ITextureObject val$textureObjf;

    TextureManager.1(ITextureObject iTextureObject) {
        this.val$textureObjf = iTextureObject;
    }

    public String call() throws Exception {
        return this.val$textureObjf.getClass().getName();
    }
}
