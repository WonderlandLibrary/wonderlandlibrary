// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.resources;

import java.util.List;

public interface IReloadableResourceManager extends IResourceManager
{
    void reloadResources(final List p0);
    
    void registerReloadListener(final IResourceManagerReloadListener p0);
}
