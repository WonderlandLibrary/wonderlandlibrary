// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.resources;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import java.util.ArrayList;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileNotFoundException;
import net.minecraft.util.ResourceLocation;
import java.util.Set;
import com.google.common.collect.Lists;
import net.minecraft.client.resources.data.IMetadataSerializer;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class FallbackResourceManager implements IResourceManager
{
    private static final Logger field_177246_b;
    protected final List resourcePacks;
    private final IMetadataSerializer frmMetadataSerializer;
    private static final String __OBFID = "CL_00001074";
    
    public FallbackResourceManager(final IMetadataSerializer p_i1289_1_) {
        this.resourcePacks = Lists.newArrayList();
        this.frmMetadataSerializer = p_i1289_1_;
    }
    
    public void addResourcePack(final IResourcePack p_110538_1_) {
        this.resourcePacks.add(p_110538_1_);
    }
    
    @Override
    public Set getResourceDomains() {
        return null;
    }
    
    @Override
    public IResource getResource(final ResourceLocation p_110536_1_) throws IOException {
        IResourcePack var2 = null;
        final ResourceLocation var3 = getLocationMcmeta(p_110536_1_);
        for (int var4 = this.resourcePacks.size() - 1; var4 >= 0; --var4) {
            final IResourcePack var5 = this.resourcePacks.get(var4);
            if (var2 == null && var5.resourceExists(var3)) {
                var2 = var5;
            }
            if (var5.resourceExists(p_110536_1_)) {
                InputStream var6 = null;
                if (var2 != null) {
                    var6 = this.func_177245_a(var3, var2);
                }
                return new SimpleResource(var5.getPackName(), p_110536_1_, this.func_177245_a(p_110536_1_, var5), var6, this.frmMetadataSerializer);
            }
        }
        throw new FileNotFoundException(p_110536_1_.toString());
    }
    
    protected InputStream func_177245_a(final ResourceLocation p_177245_1_, final IResourcePack p_177245_2_) throws IOException {
        final InputStream var3 = p_177245_2_.getInputStream(p_177245_1_);
        return FallbackResourceManager.field_177246_b.isDebugEnabled() ? new ImputStreamLeakedResourceLogger(var3, p_177245_1_, p_177245_2_.getPackName()) : var3;
    }
    
    @Override
    public List getAllResources(final ResourceLocation p_135056_1_) throws IOException {
        final ArrayList var2 = Lists.newArrayList();
        final ResourceLocation var3 = getLocationMcmeta(p_135056_1_);
        for (final IResourcePack var5 : this.resourcePacks) {
            if (var5.resourceExists(p_135056_1_)) {
                final InputStream var6 = var5.resourceExists(var3) ? this.func_177245_a(var3, var5) : null;
                var2.add(new SimpleResource(var5.getPackName(), p_135056_1_, this.func_177245_a(p_135056_1_, var5), var6, this.frmMetadataSerializer));
            }
        }
        if (var2.isEmpty()) {
            throw new FileNotFoundException(p_135056_1_.toString());
        }
        return var2;
    }
    
    static ResourceLocation getLocationMcmeta(final ResourceLocation p_110537_0_) {
        return new ResourceLocation(p_110537_0_.getResourceDomain(), p_110537_0_.getResourcePath() + ".mcmeta");
    }
    
    static {
        field_177246_b = LogManager.getLogger();
    }
    
    static class ImputStreamLeakedResourceLogger extends InputStream
    {
        private final InputStream field_177330_a;
        private final String field_177328_b;
        private boolean field_177329_c;
        private static final String __OBFID = "CL_00002395";
        
        public ImputStreamLeakedResourceLogger(final InputStream p_i46093_1_, final ResourceLocation p_i46093_2_, final String p_i46093_3_) {
            this.field_177329_c = false;
            this.field_177330_a = p_i46093_1_;
            final ByteArrayOutputStream var4 = new ByteArrayOutputStream();
            new Exception().printStackTrace(new PrintStream(var4));
            this.field_177328_b = "Leaked resource: '" + p_i46093_2_ + "' loaded from pack: '" + p_i46093_3_ + "'\n" + var4.toString();
        }
        
        @Override
        public void close() throws IOException {
            this.field_177330_a.close();
            this.field_177329_c = true;
        }
        
        @Override
        protected void finalize() throws Throwable {
            if (!this.field_177329_c) {
                FallbackResourceManager.field_177246_b.warn(this.field_177328_b);
            }
            super.finalize();
        }
        
        @Override
        public int read() throws IOException {
            return this.field_177330_a.read();
        }
    }
}
