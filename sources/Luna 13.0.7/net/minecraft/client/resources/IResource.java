package net.minecraft.client.resources;

import java.io.InputStream;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.util.ResourceLocation;

public abstract interface IResource
{
  public abstract ResourceLocation func_177241_a();
  
  public abstract InputStream getInputStream();
  
  public abstract boolean hasMetadata();
  
  public abstract IMetadataSection getMetadata(String paramString);
  
  public abstract String func_177240_d();
}
