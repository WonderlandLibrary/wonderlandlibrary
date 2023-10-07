// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.resources;

import org.apache.logging.log4j.LogManager;
import com.google.common.collect.Iterables;
import com.google.common.base.Function;
import java.io.IOException;
import java.io.FileNotFoundException;
import net.minecraft.util.ResourceLocation;
import java.util.Iterator;
import com.google.common.collect.Sets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.resources.data.IMetadataSerializer;
import java.util.Set;
import java.util.List;
import java.util.Map;
import com.google.common.base.Joiner;
import org.apache.logging.log4j.Logger;

public class SimpleReloadableResourceManager implements IReloadableResourceManager
{
    private static final Logger logger;
    private static final Joiner joinerResourcePacks;
    private final Map domainResourceManagers;
    private final List reloadListeners;
    private final Set setResourceDomains;
    private final IMetadataSerializer rmMetadataSerializer;
    private static final String __OBFID = "CL_00001091";
    
    public SimpleReloadableResourceManager(final IMetadataSerializer p_i1299_1_) {
        this.domainResourceManagers = Maps.newHashMap();
        this.reloadListeners = Lists.newArrayList();
        this.setResourceDomains = Sets.newLinkedHashSet();
        this.rmMetadataSerializer = p_i1299_1_;
    }
    
    public void reloadResourcePack(final IResourcePack p_110545_1_) {
        for (final String var3 : p_110545_1_.getResourceDomains()) {
            this.setResourceDomains.add(var3);
            FallbackResourceManager var4 = this.domainResourceManagers.get(var3);
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
    public IResource getResource(final ResourceLocation p_110536_1_) throws IOException {
        final IResourceManager var2 = this.domainResourceManagers.get(p_110536_1_.getResourceDomain());
        if (var2 != null) {
            return var2.getResource(p_110536_1_);
        }
        throw new FileNotFoundException(p_110536_1_.toString());
    }
    
    @Override
    public List getAllResources(final ResourceLocation p_135056_1_) throws IOException {
        final IResourceManager var2 = this.domainResourceManagers.get(p_135056_1_.getResourceDomain());
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
    public void reloadResources(final List p_110541_1_) {
        this.clearResources();
        SimpleReloadableResourceManager.logger.info("Reloading ResourceManager: " + SimpleReloadableResourceManager.joinerResourcePacks.join(Iterables.transform((Iterable)p_110541_1_, (Function)new Function() {
            private static final String __OBFID = "CL_00001092";
            
            public String apply(final IResourcePack p_apply_1_) {
                return p_apply_1_.getPackName();
            }
            
            public Object apply(final Object p_apply_1_) {
                return this.apply((IResourcePack)p_apply_1_);
            }
        })));
        for (final IResourcePack var3 : p_110541_1_) {
            this.reloadResourcePack(var3);
        }
        this.notifyReloadListeners();
    }
    
    @Override
    public void registerReloadListener(final IResourceManagerReloadListener p_110542_1_) {
        this.reloadListeners.add(p_110542_1_);
        p_110542_1_.onResourceManagerReload(this);
    }
    
    private void notifyReloadListeners() {
        for (final IResourceManagerReloadListener var2 : this.reloadListeners) {
            var2.onResourceManagerReload(this);
        }
    }
    
    static {
        logger = LogManager.getLogger();
        joinerResourcePacks = Joiner.on(", ");
    }
}
