package net.minecraft.client.resources;

import java.util.List;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;

public interface IReloadableResourceManager extends IResourceManager {
   void registerReloadListener(IResourceManagerReloadListener var1);

   void reloadResources(List var1);
}
