package net.optifine.util;

import java.io.IOException;
import net.minecraft.client.renderer.texture.ITickableTextureObject;
import net.minecraft.client.resources.IResourceManager;
import net.optifine.TextureAnimations;
import net.optifine.shaders.MultiTexID;

static final class TextureUtils.2
implements ITickableTextureObject {
    TextureUtils.2() {
    }

    public void tick() {
        TextureAnimations.updateAnimations();
    }

    public void loadTexture(IResourceManager var1) throws IOException {
    }

    public int getGlTextureId() {
        return 0;
    }

    public void setBlurMipmap(boolean p_174936_1, boolean p_174936_2) {
    }

    public void restoreLastBlurMipmap() {
    }

    public MultiTexID getMultiTexID() {
        return null;
    }
}
