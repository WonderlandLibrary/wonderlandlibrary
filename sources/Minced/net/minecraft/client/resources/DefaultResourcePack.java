// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources;

import net.minecraft.util.Util;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.renderer.texture.TextureUtil;
import java.awt.image.BufferedImage;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import java.net.URL;
import net.optifine.reflect.ReflectorForge;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import net.minecraft.util.ResourceLocation;
import java.util.Set;

public class DefaultResourcePack implements IResourcePack
{
    public static final Set<String> DEFAULT_RESOURCE_DOMAINS;
    private final ResourceIndex resourceIndex;
    private static final boolean ON_WINDOWS;
    
    public DefaultResourcePack(final ResourceIndex resourceIndexIn) {
        this.resourceIndex = resourceIndexIn;
    }
    
    @Override
    public InputStream getInputStream(final ResourceLocation location) throws IOException {
        final InputStream inputstream = this.getInputStreamAssets(location);
        if (inputstream != null) {
            return inputstream;
        }
        final InputStream inputstream2 = this.getResourceStream(location);
        if (inputstream2 != null) {
            return inputstream2;
        }
        throw new FileNotFoundException(location.getPath());
    }
    
    @Nullable
    public InputStream getInputStreamAssets(final ResourceLocation location) throws IOException, FileNotFoundException {
        final File file1 = this.resourceIndex.getFile(location);
        return (file1 != null && file1.isFile()) ? new FileInputStream(file1) : null;
    }
    
    @Nullable
    private InputStream getResourceStream(final ResourceLocation location) {
        final String s = "/assets/" + location.getNamespace() + "/" + location.getPath();
        final InputStream inputstream = ReflectorForge.getOptiFineResourceStream(s);
        if (inputstream != null) {
            return inputstream;
        }
        try {
            final URL url = DefaultResourcePack.class.getResource(s);
            return (url != null && this.validatePath(new File(url.getFile()), s)) ? DefaultResourcePack.class.getResourceAsStream(s) : null;
        }
        catch (IOException var5) {
            return DefaultResourcePack.class.getResourceAsStream(s);
        }
    }
    
    @Override
    public boolean resourceExists(final ResourceLocation location) {
        return this.getResourceStream(location) != null || this.resourceIndex.isFileExisting(location);
    }
    
    @Override
    public Set<String> getResourceDomains() {
        return DefaultResourcePack.DEFAULT_RESOURCE_DOMAINS;
    }
    
    @Nullable
    @Override
    public <T extends IMetadataSection> T getPackMetadata(final MetadataSerializer metadataSerializer, final String metadataSectionName) throws IOException {
        try {
            final InputStream inputstream = new FileInputStream(this.resourceIndex.getPackMcmeta());
            return AbstractResourcePack.readMetadata(metadataSerializer, inputstream, metadataSectionName);
        }
        catch (RuntimeException var4) {
            return null;
        }
        catch (FileNotFoundException var5) {
            return null;
        }
    }
    
    @Override
    public BufferedImage getPackImage() throws IOException {
        return TextureUtil.readBufferedImage(DefaultResourcePack.class.getResourceAsStream("/" + new ResourceLocation("pack.png").getPath()));
    }
    
    @Override
    public String getPackName() {
        return "Default";
    }
    
    private boolean validatePath(final File p_validatePath_1_, final String p_validatePath_2_) throws IOException {
        String s = p_validatePath_1_.getPath();
        if (s.startsWith("file:")) {
            if (DefaultResourcePack.ON_WINDOWS) {
                s = s.replace("\\", "/");
            }
            return s.endsWith(p_validatePath_2_);
        }
        return FolderResourcePack.validatePath(p_validatePath_1_, p_validatePath_2_);
    }
    
    static {
        DEFAULT_RESOURCE_DOMAINS = (Set)ImmutableSet.of((Object)"minecraft", (Object)"realms");
        ON_WINDOWS = (Util.getOSType() == Util.EnumOS.WINDOWS);
    }
}
