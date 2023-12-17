// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileNotFoundException;
import net.minecraft.util.ResourceLocation;
import java.util.Collections;
import java.util.Set;
import com.google.common.collect.Lists;
import net.minecraft.client.resources.data.MetadataSerializer;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class FallbackResourceManager implements IResourceManager
{
    private static final Logger LOGGER;
    protected final List<IResourcePack> resourcePacks;
    private final MetadataSerializer frmMetadataSerializer;
    
    public FallbackResourceManager(final MetadataSerializer frmMetadataSerializerIn) {
        this.resourcePacks = (List<IResourcePack>)Lists.newArrayList();
        this.frmMetadataSerializer = frmMetadataSerializerIn;
    }
    
    public void addResourcePack(final IResourcePack resourcePack) {
        this.resourcePacks.add(resourcePack);
    }
    
    @Override
    public Set<String> getResourceDomains() {
        return Collections.emptySet();
    }
    
    @Override
    public IResource getResource(final ResourceLocation location) throws IOException {
        this.checkResourcePath(location);
        IResourcePack iresourcepack = null;
        final ResourceLocation resourcelocation = getLocationMcmeta(location);
        for (int i = this.resourcePacks.size() - 1; i >= 0; --i) {
            final IResourcePack iresourcepack2 = this.resourcePacks.get(i);
            if (iresourcepack == null && iresourcepack2.resourceExists(resourcelocation)) {
                iresourcepack = iresourcepack2;
            }
            if (iresourcepack2.resourceExists(location)) {
                InputStream inputstream = null;
                if (iresourcepack != null) {
                    inputstream = this.getInputStream(resourcelocation, iresourcepack);
                }
                return new SimpleResource(iresourcepack2.getPackName(), location, this.getInputStream(location, iresourcepack2), inputstream, this.frmMetadataSerializer);
            }
        }
        throw new FileNotFoundException(location.toString());
    }
    
    protected InputStream getInputStream(final ResourceLocation location, final IResourcePack resourcePack) throws IOException {
        final InputStream inputstream = resourcePack.getInputStream(location);
        return FallbackResourceManager.LOGGER.isDebugEnabled() ? new InputStreamLeakedResourceLogger(inputstream, location, resourcePack.getPackName()) : inputstream;
    }
    
    private void checkResourcePath(final ResourceLocation p_188552_1_) throws IOException {
        if (p_188552_1_.getPath().contains("..")) {
            throw new IOException("Invalid relative path to resource: " + p_188552_1_);
        }
    }
    
    @Override
    public List<IResource> getAllResources(final ResourceLocation location) throws IOException {
        this.checkResourcePath(location);
        final List<IResource> list = (List<IResource>)Lists.newArrayList();
        final ResourceLocation resourcelocation = getLocationMcmeta(location);
        for (final IResourcePack iresourcepack : this.resourcePacks) {
            if (iresourcepack.resourceExists(location)) {
                final InputStream inputstream = iresourcepack.resourceExists(resourcelocation) ? this.getInputStream(resourcelocation, iresourcepack) : null;
                list.add(new SimpleResource(iresourcepack.getPackName(), location, this.getInputStream(location, iresourcepack), inputstream, this.frmMetadataSerializer));
            }
        }
        if (list.isEmpty()) {
            throw new FileNotFoundException(location.toString());
        }
        return list;
    }
    
    static ResourceLocation getLocationMcmeta(final ResourceLocation location) {
        return new ResourceLocation(location.getNamespace(), location.getPath() + ".mcmeta");
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    static class InputStreamLeakedResourceLogger extends InputStream
    {
        private final InputStream inputStream;
        private final String message;
        private boolean isClosed;
        
        public InputStreamLeakedResourceLogger(final InputStream p_i46093_1_, final ResourceLocation location, final String resourcePack) {
            this.inputStream = p_i46093_1_;
            final ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
            new Exception().printStackTrace(new PrintStream(bytearrayoutputstream));
            this.message = "Leaked resource: '" + location + "' loaded from pack: '" + resourcePack + "'\n" + bytearrayoutputstream;
        }
        
        @Override
        public void close() throws IOException {
            this.inputStream.close();
            this.isClosed = true;
        }
        
        @Override
        protected void finalize() throws Throwable {
            if (!this.isClosed) {
                FallbackResourceManager.LOGGER.warn(this.message);
            }
            super.finalize();
        }
        
        @Override
        public int read() throws IOException {
            return this.inputStream.read();
        }
    }
}
