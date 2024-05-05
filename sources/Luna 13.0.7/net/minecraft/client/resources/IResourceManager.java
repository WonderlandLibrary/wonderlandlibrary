package net.minecraft.client.resources;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import net.minecraft.util.ResourceLocation;

public abstract interface IResourceManager
{
  public abstract Set getResourceDomains();
  
  public abstract IResource getResource(ResourceLocation paramResourceLocation)
    throws IOException;
  
  public abstract List getAllResources(ResourceLocation paramResourceLocation)
    throws IOException;
}
