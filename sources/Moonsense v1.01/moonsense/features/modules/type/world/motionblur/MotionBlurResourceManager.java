// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.world.motionblur;

import java.util.Map;
import java.util.List;
import java.io.IOException;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import java.util.Set;
import net.minecraft.client.resources.IResourceManager;

public class MotionBlurResourceManager implements IResourceManager
{
    @Override
    public Set<String> getResourceDomains() {
        return null;
    }
    
    @Override
    public IResource getResource(final ResourceLocation location) throws IOException {
        return new MotionBlurResource();
    }
    
    @Override
    public List<IResource> getAllResources(final ResourceLocation location) throws IOException {
        return null;
    }
    
    @Override
    public Map getDomainResourceManagers() {
        return null;
    }
}
