/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.resources;

import com.google.common.collect.Lists;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.SimpleResource;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FallbackResourceManager
implements IResourceManager {
    private static final Logger logger = LogManager.getLogger();
    protected final List<IResourcePack> resourcePacks = Lists.newArrayList();
    private final IMetadataSerializer frmMetadataSerializer;

    public FallbackResourceManager(IMetadataSerializer frmMetadataSerializerIn) {
        this.frmMetadataSerializer = frmMetadataSerializerIn;
    }

    public void addResourcePack(IResourcePack resourcePack) {
        this.resourcePacks.add(resourcePack);
    }

    @Override
    public Set<String> getResourceDomains() {
        return null;
    }

    @Override
    public IResource getResource(ResourceLocation location) throws IOException {
        IResourcePack iresourcepack = null;
        ResourceLocation resourcelocation = FallbackResourceManager.getLocationMcmeta(location);
        int i = this.resourcePacks.size() - 1;
        while (i >= 0) {
            IResourcePack iresourcepack1 = this.resourcePacks.get(i);
            if (iresourcepack == null && iresourcepack1.resourceExists(resourcelocation)) {
                iresourcepack = iresourcepack1;
            }
            if (iresourcepack1.resourceExists(location)) {
                InputStream inputstream = null;
                if (iresourcepack == null) return new SimpleResource(iresourcepack1.getPackName(), location, this.getInputStream(location, iresourcepack1), inputstream, this.frmMetadataSerializer);
                inputstream = this.getInputStream(resourcelocation, iresourcepack);
                return new SimpleResource(iresourcepack1.getPackName(), location, this.getInputStream(location, iresourcepack1), inputstream, this.frmMetadataSerializer);
            }
            --i;
        }
        throw new FileNotFoundException(location.toString());
    }

    protected InputStream getInputStream(ResourceLocation location, IResourcePack resourcePack) throws IOException {
        InputStream inputStream;
        InputStream inputstream = resourcePack.getInputStream(location);
        if (logger.isDebugEnabled()) {
            inputStream = new InputStreamLeakedResourceLogger(inputstream, location, resourcePack.getPackName());
            return inputStream;
        }
        inputStream = inputstream;
        return inputStream;
    }

    @Override
    public List<IResource> getAllResources(ResourceLocation location) throws IOException {
        ArrayList<IResource> list = Lists.newArrayList();
        ResourceLocation resourcelocation = FallbackResourceManager.getLocationMcmeta(location);
        Iterator<IResourcePack> iterator = this.resourcePacks.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                if (!list.isEmpty()) return list;
                throw new FileNotFoundException(location.toString());
            }
            IResourcePack iresourcepack = iterator.next();
            if (!iresourcepack.resourceExists(location)) continue;
            InputStream inputstream = iresourcepack.resourceExists(resourcelocation) ? this.getInputStream(resourcelocation, iresourcepack) : null;
            list.add(new SimpleResource(iresourcepack.getPackName(), location, this.getInputStream(location, iresourcepack), inputstream, this.frmMetadataSerializer));
        }
    }

    static ResourceLocation getLocationMcmeta(ResourceLocation location) {
        return new ResourceLocation(location.getResourceDomain(), location.getResourcePath() + ".mcmeta");
    }

    static class InputStreamLeakedResourceLogger
    extends InputStream {
        private final InputStream field_177330_a;
        private final String field_177328_b;
        private boolean field_177329_c = false;

        public InputStreamLeakedResourceLogger(InputStream p_i46093_1_, ResourceLocation location, String p_i46093_3_) {
            this.field_177330_a = p_i46093_1_;
            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
            new Exception().printStackTrace(new PrintStream(bytearrayoutputstream));
            this.field_177328_b = "Leaked resource: '" + location + "' loaded from pack: '" + p_i46093_3_ + "'\n" + bytearrayoutputstream.toString();
        }

        @Override
        public void close() throws IOException {
            this.field_177330_a.close();
            this.field_177329_c = true;
        }

        protected void finalize() throws Throwable {
            if (!this.field_177329_c) {
                logger.warn(this.field_177328_b);
            }
            super.finalize();
        }

        @Override
        public int read() throws IOException {
            return this.field_177330_a.read();
        }
    }
}

