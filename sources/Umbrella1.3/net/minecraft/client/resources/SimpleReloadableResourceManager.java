/*
 * Decompiled with CFR 0.150.
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
    private static final Logger logger = LogManager.getLogger();
    private static final Joiner joinerResourcePacks = Joiner.on((String)", ");
    private final Map domainResourceManagers = Maps.newHashMap();
    private final List reloadListeners = Lists.newArrayList();
    private final Set setResourceDomains = Sets.newLinkedHashSet();
    private final IMetadataSerializer rmMetadataSerializer;
    private static final String __OBFID = "CL_00001091";

    public SimpleReloadableResourceManager(IMetadataSerializer p_i1299_1_) {
        this.rmMetadataSerializer = p_i1299_1_;
    }

    public void reloadResourcePack(IResourcePack p_110545_1_) {
        for (String var3 : p_110545_1_.getResourceDomains()) {
            this.setResourceDomains.add(var3);
            FallbackResourceManager var4 = (FallbackResourceManager)this.domainResourceManagers.get(var3);
            if (var4 == null) {
                var4 = new FallbackResourceManager(this.rmMetadataSerializer);
                this.domainResourceManagers.put(var3, var4);
            }
            var4.addResourcePack(p_110545_1_);
        }
    }

    @Override
    public Set getResourceDomains() {
        return this.setResourceDomains;
    }

    @Override
    public IResource getResource(ResourceLocation p_110536_1_) throws IOException {
        IResourceManager var2 = (IResourceManager)this.domainResourceManagers.get(p_110536_1_.getResourceDomain());
        if (var2 != null) {
            return var2.getResource(p_110536_1_);
        }
        throw new FileNotFoundException(p_110536_1_.toString());
    }

    @Override
    public List getAllResources(ResourceLocation p_135056_1_) throws IOException {
        IResourceManager var2 = (IResourceManager)this.domainResourceManagers.get(p_135056_1_.getResourceDomain());
        if (var2 != null) {
            return var2.getAllResources(p_135056_1_);
        }
        throw new FileNotFoundException(p_135056_1_.toString());
    }

    private void clearResources() {
        this.domainResourceManagers.clear();
        this.setResourceDomains.clear();
    }

    @Override
    public void reloadResources(List p_110541_1_) {
        this.clearResources();
        logger.info("Reloading ResourceManager: " + joinerResourcePacks.join(Iterables.transform((Iterable)p_110541_1_, (Function)new Function(){
            private static final String __OBFID = "CL_00001092";

            public String apply(IResourcePack p_apply_1_) {
                return p_apply_1_.getPackName();
            }

            public Object apply(Object p_apply_1_) {
                return this.apply((IResourcePack)p_apply_1_);
            }
        })));
        for (IResourcePack var3 : p_110541_1_) {
            this.reloadResourcePack(var3);
        }
        this.notifyReloadListeners();
    }

    @Override
    public void registerReloadListener(IResourceManagerReloadListener p_110542_1_) {
        this.reloadListeners.add(p_110542_1_);
        p_110542_1_.onResourceManagerReload(this);
    }

    private void notifyReloadListeners() {
        for (IResourceManagerReloadListener var2 : this.reloadListeners) {
            var2.onResourceManagerReload(this);
        }
    }
}

