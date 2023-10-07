// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.resources;

import java.util.List;
import java.io.IOException;
import net.minecraft.util.ResourceLocation;
import java.util.Set;

public interface IResourceManager
{
    Set getResourceDomains();
    
    IResource getResource(final ResourceLocation p0) throws IOException;
    
    List getAllResources(final ResourceLocation p0) throws IOException;
}
