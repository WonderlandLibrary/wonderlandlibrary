package pw.cinque.motionblur.resource;

import net.minecraft.client.resources.FallbackResourceManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Set;

public class MotionBlurResourceManager extends FallbackResourceManager implements IResourceManager {
    
    private static final String author = "https://2pi.pw/";
    
    public MotionBlurResourceManager(IMetadataSerializer frmMetadataSerializerIn) {
        super(frmMetadataSerializerIn);
    }

    @Override
    public Set<String> getResourceDomains() {
        return null;
    }

    @Override
    public IResource getResource(ResourceLocation location) {
        return new MotionBlurResource();
    }

    @Override
    public List<IResource> getAllResources(ResourceLocation location) {
        return null;
    }
}