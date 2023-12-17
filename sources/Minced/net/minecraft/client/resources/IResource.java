// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources;

import javax.annotation.Nullable;
import net.minecraft.client.resources.data.IMetadataSection;
import java.io.InputStream;
import net.minecraft.util.ResourceLocation;
import java.io.Closeable;

public interface IResource extends Closeable
{
    ResourceLocation getResourceLocation();
    
    InputStream getInputStream();
    
    boolean hasMetadata();
    
    @Nullable
     <T extends IMetadataSection> T getMetadata(final String p0);
    
    String getResourcePackName();
}
