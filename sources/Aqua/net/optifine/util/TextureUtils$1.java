package net.optifine.util;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.optifine.util.TextureUtils;

/*
 * Exception performing whole class analysis ignored.
 */
static final class TextureUtils.1
implements IResourceManagerReloadListener {
    TextureUtils.1() {
    }

    public void onResourceManagerReload(IResourceManager var1) {
        TextureUtils.resourcesReloaded((IResourceManager)var1);
    }
}
