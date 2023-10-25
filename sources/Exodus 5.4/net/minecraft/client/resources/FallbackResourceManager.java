/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
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
    private final IMetadataSerializer frmMetadataSerializer;
    protected final List<IResourcePack> resourcePacks = Lists.newArrayList();
    private static final Logger logger = LogManager.getLogger();

    @Override
    public List<IResource> getAllResources(ResourceLocation resourceLocation) throws IOException {
        ArrayList arrayList = Lists.newArrayList();
        ResourceLocation resourceLocation2 = FallbackResourceManager.getLocationMcmeta(resourceLocation);
        for (IResourcePack iResourcePack : this.resourcePacks) {
            if (!iResourcePack.resourceExists(resourceLocation)) continue;
            InputStream inputStream = iResourcePack.resourceExists(resourceLocation2) ? this.getInputStream(resourceLocation2, iResourcePack) : null;
            arrayList.add(new SimpleResource(iResourcePack.getPackName(), resourceLocation, this.getInputStream(resourceLocation, iResourcePack), inputStream, this.frmMetadataSerializer));
        }
        if (arrayList.isEmpty()) {
            throw new FileNotFoundException(resourceLocation.toString());
        }
        return arrayList;
    }

    protected InputStream getInputStream(ResourceLocation resourceLocation, IResourcePack iResourcePack) throws IOException {
        InputStream inputStream = iResourcePack.getInputStream(resourceLocation);
        return logger.isDebugEnabled() ? new InputStreamLeakedResourceLogger(inputStream, resourceLocation, iResourcePack.getPackName()) : inputStream;
    }

    static ResourceLocation getLocationMcmeta(ResourceLocation resourceLocation) {
        return new ResourceLocation(resourceLocation.getResourceDomain(), String.valueOf(resourceLocation.getResourcePath()) + ".mcmeta");
    }

    public FallbackResourceManager(IMetadataSerializer iMetadataSerializer) {
        this.frmMetadataSerializer = iMetadataSerializer;
    }

    public void addResourcePack(IResourcePack iResourcePack) {
        this.resourcePacks.add(iResourcePack);
    }

    @Override
    public IResource getResource(ResourceLocation resourceLocation) throws IOException {
        IResourcePack iResourcePack = null;
        ResourceLocation resourceLocation2 = FallbackResourceManager.getLocationMcmeta(resourceLocation);
        int n = this.resourcePacks.size() - 1;
        while (n >= 0) {
            IResourcePack iResourcePack2 = this.resourcePacks.get(n);
            if (iResourcePack == null && iResourcePack2.resourceExists(resourceLocation2)) {
                iResourcePack = iResourcePack2;
            }
            if (iResourcePack2.resourceExists(resourceLocation)) {
                InputStream inputStream = null;
                if (iResourcePack != null) {
                    inputStream = this.getInputStream(resourceLocation2, iResourcePack);
                }
                return new SimpleResource(iResourcePack2.getPackName(), resourceLocation, this.getInputStream(resourceLocation, iResourcePack2), inputStream, this.frmMetadataSerializer);
            }
            --n;
        }
        throw new FileNotFoundException(resourceLocation.toString());
    }

    @Override
    public Set<String> getResourceDomains() {
        return null;
    }

    static class InputStreamLeakedResourceLogger
    extends InputStream {
        private boolean field_177329_c = false;
        private final InputStream field_177330_a;
        private final String field_177328_b;

        @Override
        public int read() throws IOException {
            return this.field_177330_a.read();
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

        public InputStreamLeakedResourceLogger(InputStream inputStream, ResourceLocation resourceLocation, String string) {
            this.field_177330_a = inputStream;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            new Exception().printStackTrace(new PrintStream(byteArrayOutputStream));
            this.field_177328_b = "Leaked resource: '" + resourceLocation + "' loaded from pack: '" + string + "'\n" + byteArrayOutputStream.toString();
        }
    }
}

