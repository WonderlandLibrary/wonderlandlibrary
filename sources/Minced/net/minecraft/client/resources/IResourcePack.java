// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources;

import java.awt.image.BufferedImage;
import javax.annotation.Nullable;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import java.util.Set;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.util.ResourceLocation;

public interface IResourcePack
{
    InputStream getInputStream(final ResourceLocation p0) throws IOException;
    
    boolean resourceExists(final ResourceLocation p0);
    
    Set<String> getResourceDomains();
    
    @Nullable
     <T extends IMetadataSection> T getPackMetadata(final MetadataSerializer p0, final String p1) throws IOException;
    
    BufferedImage getPackImage() throws IOException;
    
    String getPackName();
}
