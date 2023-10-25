package net.minecraft.client.resources;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;

public interface IResourcePack {
  InputStream getInputStream(ResourceLocation paramResourceLocation) throws IOException;
  
  boolean resourceExists(ResourceLocation paramResourceLocation);
  
  Set<String> getResourceDomains();
  
  <T extends net.minecraft.client.resources.data.IMetadataSection> T getPackMetadata(IMetadataSerializer paramIMetadataSerializer, String paramString) throws IOException;
  
  BufferedImage getPackImage() throws IOException;
  
  String getPackName();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\resources\IResourcePack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */