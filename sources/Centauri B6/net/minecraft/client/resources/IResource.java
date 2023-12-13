package net.minecraft.client.resources;

import java.io.InputStream;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.util.ResourceLocation;

public interface IResource {
   InputStream getInputStream();

   String getResourcePackName();

   IMetadataSection getMetadata(String var1);

   ResourceLocation getResourceLocation();

   boolean hasMetadata();
}
