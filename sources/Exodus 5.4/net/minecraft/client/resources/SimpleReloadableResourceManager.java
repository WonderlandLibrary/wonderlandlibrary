/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Function
 *  com.google.common.base.Joiner
 *  com.google.common.collect.Iterables
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
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
    private final IMetadataSerializer rmMetadataSerializer;
    private final Map<String, FallbackResourceManager> domainResourceManagers = Maps.newHashMap();
    private static final Logger logger = LogManager.getLogger();
    private final Set<String> setResourceDomains;
    private static final Joiner joinerResourcePacks = Joiner.on((String)", ");
    private final List<IResourceManagerReloadListener> reloadListeners = Lists.newArrayList();

    @Override
    public List<IResource> getAllResources(ResourceLocation resourceLocation) throws IOException {
        IResourceManager iResourceManager = this.domainResourceManagers.get(resourceLocation.getResourceDomain());
        if (iResourceManager != null) {
            return iResourceManager.getAllResources(resourceLocation);
        }
        throw new FileNotFoundException(resourceLocation.toString());
    }

    @Override
    public Set<String> getResourceDomains() {
        return this.setResourceDomains;
    }

    @Override
    public IResource getResource(ResourceLocation resourceLocation) throws IOException {
        IResourceManager iResourceManager = this.domainResourceManagers.get(resourceLocation.getResourceDomain());
        if (iResourceManager != null) {
            return iResourceManager.getResource(resourceLocation);
        }
        throw new FileNotFoundException(resourceLocation.toString());
    }

    @Override
    public void reloadResources(List<IResourcePack> list) {
        this.clearResources();
        logger.info("Reloading ResourceManager: " + joinerResourcePacks.join(Iterables.transform(list, (Function)new Function<IResourcePack, String>(){

            public String apply(IResourcePack iResourcePack) {
                return iResourcePack.getPackName();
            }
        })));
        for (IResourcePack iResourcePack : list) {
            this.reloadResourcePack(iResourcePack);
        }
        this.notifyReloadListeners();
    }

    public SimpleReloadableResourceManager(IMetadataSerializer iMetadataSerializer) {
        this.setResourceDomains = Sets.newLinkedHashSet();
        this.rmMetadataSerializer = iMetadataSerializer;
    }

    public void reloadResourcePack(IResourcePack iResourcePack) {
        for (String string : iResourcePack.getResourceDomains()) {
            this.setResourceDomains.add(string);
            FallbackResourceManager fallbackResourceManager = this.domainResourceManagers.get(string);
            if (fallbackResourceManager == null) {
                fallbackResourceManager = new FallbackResourceManager(this.rmMetadataSerializer);
                this.domainResourceManagers.put(string, fallbackResourceManager);
            }
            fallbackResourceManager.addResourcePack(iResourcePack);
        }
    }

    private void notifyReloadListeners() {
        for (IResourceManagerReloadListener iResourceManagerReloadListener : this.reloadListeners) {
            iResourceManagerReloadListener.onResourceManagerReload(this);
        }
    }

    @Override
    public void registerReloadListener(IResourceManagerReloadListener iResourceManagerReloadListener) {
        this.reloadListeners.add(iResourceManagerReloadListener);
        iResourceManagerReloadListener.onResourceManagerReload(this);
    }

    private void clearResources() {
        this.domainResourceManagers.clear();
        this.setResourceDomains.clear();
    }
}

