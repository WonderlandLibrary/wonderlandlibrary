// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources;

import com.google.common.collect.ImmutableSet;
import net.minecraft.client.renderer.texture.TextureUtil;
import java.awt.image.BufferedImage;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.optifine.reflect.ReflectorForge;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import net.minecraft.util.ResourceLocation;
import java.io.File;
import java.util.Map;
import java.util.Set;

public class DefaultResourcePack implements IResourcePack
{
    public static final Set<String> defaultResourceDomains;
    private final Map<String, File> mapAssets;
    
    public DefaultResourcePack(final Map<String, File> mapAssetsIn) {
        this.mapAssets = mapAssetsIn;
    }
    
    @Override
    public InputStream getInputStream(final ResourceLocation location) throws IOException {
        final InputStream inputstream = this.getResourceStream(location);
        if (inputstream != null) {
            return inputstream;
        }
        final InputStream inputstream2 = this.getInputStreamAssets(location);
        if (inputstream2 != null) {
            return inputstream2;
        }
        throw new FileNotFoundException(location.getResourcePath());
    }
    
    public InputStream getInputStreamAssets(final ResourceLocation location) throws IOException, FileNotFoundException {
        final File file1 = this.mapAssets.get(location.toString());
        return (file1 != null && file1.isFile()) ? new FileInputStream(file1) : null;
    }
    
    private InputStream getResourceStream(final ResourceLocation location) {
        final String s = "/assets/" + location.getResourceDomain() + "/" + location.getResourcePath();
        final InputStream inputstream = ReflectorForge.getOptiFineResourceStream(s);
        return (inputstream != null) ? inputstream : DefaultResourcePack.class.getResourceAsStream(s);
    }
    
    @Override
    public boolean resourceExists(final ResourceLocation location) {
        return this.getResourceStream(location) != null || this.mapAssets.containsKey(location.toString());
    }
    
    @Override
    public Set<String> getResourceDomains() {
        return DefaultResourcePack.defaultResourceDomains;
    }
    
    @Override
    public <T extends IMetadataSection> T getPackMetadata(final IMetadataSerializer metadataSerializer, final String metadataSectionName) throws IOException {
        try {
            final InputStream inputstream = new FileInputStream(this.mapAssets.get("pack.mcmeta"));
            return AbstractResourcePack.readMetadata(metadataSerializer, inputstream, metadataSectionName);
        }
        catch (RuntimeException | FileNotFoundException ex2) {
            final Exception ex;
            final Exception var4 = ex;
            return null;
        }
    }
    
    @Override
    public BufferedImage getPackImage() throws IOException {
        return TextureUtil.readBufferedImage(DefaultResourcePack.class.getResourceAsStream("/" + new ResourceLocation("pack.png").getResourcePath()));
    }
    
    @Override
    public String getPackName() {
        return "Default";
    }
    
    static {
        defaultResourceDomains = (Set)ImmutableSet.of((Object)"minecraft");
    }
}
