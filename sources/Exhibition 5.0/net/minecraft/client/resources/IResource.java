// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.resources;

import net.minecraft.client.resources.data.IMetadataSection;
import java.io.InputStream;
import net.minecraft.util.ResourceLocation;

public interface IResource
{
    ResourceLocation func_177241_a();
    
    InputStream getInputStream();
    
    boolean hasMetadata();
    
    IMetadataSection getMetadata(final String p0);
    
    String func_177240_d();
}
