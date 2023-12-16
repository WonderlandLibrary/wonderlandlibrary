// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.utils.blur;

import java.util.Map;
import java.util.List;
import java.io.IOException;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import java.util.Set;
import net.minecraft.client.resources.IResourceManager;

public class BlurResourceManager implements IResourceManager
{
    private final float BLUR_RADIUS;
    
    @Override
    public Set<String> getResourceDomains() {
        return null;
    }
    
    @Override
    public IResource getResource(final ResourceLocation location) throws IOException {
        return new BlurResource(this.BLUR_RADIUS);
    }
    
    @Override
    public List<IResource> getAllResources(final ResourceLocation location) throws IOException {
        return null;
    }
    
    public BlurResourceManager(final float BLUR_RADIUS) {
        this.BLUR_RADIUS = BLUR_RADIUS;
    }
    
    @Override
    public Map getDomainResourceManagers() {
        return null;
    }
}
