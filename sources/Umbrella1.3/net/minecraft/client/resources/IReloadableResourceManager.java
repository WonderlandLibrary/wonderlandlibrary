/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.resources;

import java.util.List;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;

public interface IReloadableResourceManager
extends IResourceManager {
    public void reloadResources(List var1);

    public void registerReloadListener(IResourceManagerReloadListener var1);
}

