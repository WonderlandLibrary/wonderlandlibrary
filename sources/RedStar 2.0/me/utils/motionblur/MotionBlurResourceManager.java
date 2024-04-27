package me.utils.motionblur;

import java.util.List;
import java.util.Set;
import me.utils.motionblur.MotionBlurResource;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class MotionBlurResourceManager
implements IResourceManager {
    public Set<String> getResourceDomains() {
        return null;
    }

    public IResource getResource(ResourceLocation location) {
        return new MotionBlurResource();
    }

    public List<IResource> getAllResources(ResourceLocation location) {
        return null;
    }
}
