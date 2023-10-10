/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.resources;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.resources.FallbackResourceManager;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimpleReloadableResourceManager
implements IReloadableResourceManager {
    private static final Logger logger = LogManager.getLogger();
    private static final Joiner joinerResourcePacks = Joiner.on(", ");
    private final Map<String, FallbackResourceManager> domainResourceManagers = Maps.newHashMap();
    private final List<IResourceManagerReloadListener> reloadListeners = Lists.newArrayList();
    private final Set<String> setResourceDomains = Sets.newLinkedHashSet();
    private final IMetadataSerializer rmMetadataSerializer;

    public SimpleReloadableResourceManager(IMetadataSerializer rmMetadataSerializerIn) {
        this.rmMetadataSerializer = rmMetadataSerializerIn;
    }

    public void reloadResourcePack(IResourcePack resourcePack) {
        for (String s2 : resourcePack.getResourceDomains()) {
            this.setResourceDomains.add(s2);
            FallbackResourceManager fallbackresourcemanager = this.domainResourceManagers.get(s2);
            if (fallbackresourcemanager == null) {
                fallbackresourcemanager = new FallbackResourceManager(this.rmMetadataSerializer);
                this.domainResourceManagers.put(s2, fallbackresourcemanager);
            }
            fallbackresourcemanager.addResourcePack(resourcePack);
        }
    }

    @Override
    public Set<String> getResourceDomains() {
        return this.setResourceDomains;
    }

    @Override
    public IResource getResource(ResourceLocation location) throws IOException {
        IResourceManager iresourcemanager = this.domainResourceManagers.get(location.getResourceDomain());
        if (iresourcemanager != null) {
            return iresourcemanager.getResource(location);
        }
        throw new FileNotFoundException(location.toString());
    }

    @Override
    public List<IResource> getAllResources(ResourceLocation location) throws IOException {
        IResourceManager iresourcemanager = this.domainResourceManagers.get(location.getResourceDomain());
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
    public void reloadResources(List<IResourcePack> p_110541_1_) {
        this.clearResources();
        logger.info("Reloading ResourceManager: " + joinerResourcePacks.join(Iterables.transform(p_110541_1_, new Function<IResourcePack, String>(){

            @Override
            public String apply(IResourcePack p_apply_1_) {
                return p_apply_1_.getPackName();
            }
        })));
        for (IResourcePack iresourcepack : p_110541_1_) {
            this.reloadResourcePack(iresourcepack);
        }
        this.notifyReloadListeners();
    }

    @Override
    public void registerReloadListener(IResourceManagerReloadListener reloadListener) {
        this.reloadListeners.add(reloadListener);
        reloadListener.onResourceManagerReload(this);
    }

    private void notifyReloadListeners() {
        for (IResourceManagerReloadListener iresourcemanagerreloadlistener : this.reloadListeners) {
            iresourcemanagerreloadlistener.onResourceManagerReload(this);
        }
    }
}

