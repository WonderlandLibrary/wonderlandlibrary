package net.minecraft.client.resources;

import java.util.List;

public abstract interface IReloadableResourceManager
  extends IResourceManager
{
  public abstract void reloadResources(List paramList);
  
  public abstract void registerReloadListener(IResourceManagerReloadListener paramIResourceManagerReloadListener);
}
