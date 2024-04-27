/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.resources.IResourceManager
 *  net.minecraft.client.resources.IResourceManagerReloadListener
 */
package net.dev.important.patcher.util.enhancement;

import net.dev.important.patcher.hooks.font.FontRendererHook;
import net.dev.important.patcher.util.enhancement.text.EnhancedFontRenderer;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;

public class ReloadListener
implements IResourceManagerReloadListener {
    public void func_110549_a(IResourceManager resourceManager) {
        for (EnhancedFontRenderer enhancedFontRenderer : EnhancedFontRenderer.getInstances()) {
            enhancedFontRenderer.invalidateAll();
        }
        FontRendererHook.forceRefresh = true;
    }
}

