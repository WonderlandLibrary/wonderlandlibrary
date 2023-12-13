package net.minecraft.client.resources;

import java.util.List;

public interface IReloadableResourceManager extends IResourceManager {
   void registerReloadListener(IResourceManagerReloadListener var1);

   void reloadResources(List var1);
}
