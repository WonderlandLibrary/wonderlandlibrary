// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources;

import org.apache.logging.log4j.LogManager;
import com.google.common.collect.Iterables;
import javax.annotation.Nullable;
import com.google.common.base.Function;
import java.io.IOException;
import java.io.FileNotFoundException;
import net.minecraft.util.ResourceLocation;
import java.util.Iterator;
import com.google.common.collect.Sets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.resources.data.MetadataSerializer;
import java.util.Set;
import java.util.List;
import java.util.Map;
import com.google.common.base.Joiner;
import org.apache.logging.log4j.Logger;

public class SimpleReloadableResourceManager implements IReloadableResourceManager
{
    private static final Logger LOGGER;
    private static final Joiner JOINER_RESOURCE_PACKS;
    private final Map<String, FallbackResourceManager> domainResourceManagers;
    private final List<IResourceManagerReloadListener> reloadListeners;
    private final Set<String> setResourceDomains;
    private final MetadataSerializer rmMetadataSerializer;
    
    public SimpleReloadableResourceManager(final MetadataSerializer rmMetadataSerializerIn) {
        this.domainResourceManagers = (Map<String, FallbackResourceManager>)Maps.newHashMap();
        this.reloadListeners = (List<IResourceManagerReloadListener>)Lists.newArrayList();
        this.setResourceDomains = (Set<String>)Sets.newLinkedHashSet();
        this.rmMetadataSerializer = rmMetadataSerializerIn;
    }
    
    public void reloadResourcePack(final IResourcePack resourcePack) {
        for (final String s : resourcePack.getResourceDomains()) {
            this.setResourceDomains.add(s);
            FallbackResourceManager fallbackresourcemanager = this.domainResourceManagers.get(s);
            if (fallbackresourcemanager == null) {
                fallbackresourcemanager = new FallbackResourceManager(this.rmMetadataSerializer);
                this.domainResourceManagers.put(s, fallbackresourcemanager);
            }
            fallbackresourcemanager.addResourcePack(resourcePack);
        }
    }
    
    @Override
    public Set<String> getResourceDomains() {
        return this.setResourceDomains;
    }
    
    @Override
    public IResource getResource(final ResourceLocation location) throws IOException {
        final IResourceManager iresourcemanager = this.domainResourceManagers.get(location.getNamespace());
        if (iresourcemanager != null) {
            return iresourcemanager.getResource(location);
        }
        throw new FileNotFoundException(location.toString());
    }
    
    @Override
    public List<IResource> getAllResources(final ResourceLocation location) throws IOException {
        final IResourceManager iresourcemanager = this.domainResourceManagers.get(location.getNamespace());
        if (iresourcemanager != null) {
            return iresourcemanager.getAllResources(location);
        }
        throw new FileNotFoundException(location.toString());
    }
    
    private void clearResources() {
        this.domainResourceManagers.clear();
        this.setResourceDomains.clear();
    }
    
    @Override
    public void reloadResources(final List<IResourcePack> resourcesPacksList) {
        this.clearResources();
        SimpleReloadableResourceManager.LOGGER.info("Reloading ResourceManager: {}", (Object)SimpleReloadableResourceManager.JOINER_RESOURCE_PACKS.join(Iterables.transform((Iterable)resourcesPacksList, (Function)new Function<IResourcePack, String>() {
            public String apply(@Nullable final IResourcePack p_apply_1_) {
                return (p_apply_1_ == null) ? "<NULL>" : p_apply_1_.getPackName();
            }
        })));
        for (final IResourcePack iresourcepack : resourcesPacksList) {
            this.reloadResourcePack(iresourcepack);
        }
        this.notifyReloadListeners();
    }
    
    @Override
    public void registerReloadListener(final IResourceManagerReloadListener reloadListener) {
        this.reloadListeners.add(reloadListener);
        reloadListener.onResourceManagerReload(this);
    }
    
    private void notifyReloadListeners() {
        for (final IResourceManagerReloadListener iresourcemanagerreloadlistener : this.reloadListeners) {
            iresourcemanagerreloadlistener.onResourceManagerReload(this);
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
        JOINER_RESOURCE_PACKS = Joiner.on(", ");
    }
}
