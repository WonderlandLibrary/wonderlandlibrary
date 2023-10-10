package net.minecraft.client.resources;

import java.io.InputStream;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.util.ResourceLocation;

public interface IResource {
   boolean hasMetadata();

   InputStream getInputStream();

   IMetadataSection getMetadata(String var1);

   ResourceLocation getResourceLocation();

   String getResourcePackName();
}
