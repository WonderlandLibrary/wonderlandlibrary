// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources;

import java.awt.image.BufferedImage;
import javax.annotation.Nullable;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import java.util.Set;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.util.ResourceLocation;

public class LegacyV2Adapter implements IResourcePack
{
    private final IResourcePack pack;
    
    public LegacyV2Adapter(final IResourcePack packIn) {
        this.pack = packIn;
    }
    
    @Override
    public InputStream getInputStream(final ResourceLocation location) throws IOException {
        return this.pack.getInputStream(this.fudgePath(location));
    }
    
    private ResourceLocation fudgePath(final ResourceLocation p_191382_1_) {
        final String s = p_191382_1_.getPath();
        if (!"lang/swg_de.lang".equals(s) && s.startsWith("lang/") && s.endsWith(".lang")) {
            final int i = s.indexOf(95);
            if (i != -1) {
                final String s2 = s.substring(0, i + 1) + s.substring(i + 1, s.indexOf(46, i)).toUpperCase() + ".lang";
                return new ResourceLocation(p_191382_1_.getNamespace(), "") {
                    @Override
                    public String getPath() {
                        return s2;
                    }
                };
            }
        }
        return p_191382_1_;
    }
    
    @Override
    public boolean resourceExists(final ResourceLocation location) {
        return this.pack.resourceExists(this.fudgePath(location));
    }
    
    @Override
    public Set<String> getResourceDomains() {
        return this.pack.getResourceDomains();
    }
    
    @Nullable
    @Override
    public <T extends IMetadataSection> T getPackMetadata(final MetadataSerializer metadataSerializer, final String metadataSectionName) throws IOException {
        return this.pack.getPackMetadata(metadataSerializer, metadataSectionName);
    }
    
    @Override
    public BufferedImage getPackImage() throws IOException {
        return this.pack.getPackImage();
    }
    
    @Override
    public String getPackName() {
        return this.pack.getPackName();
    }
}
