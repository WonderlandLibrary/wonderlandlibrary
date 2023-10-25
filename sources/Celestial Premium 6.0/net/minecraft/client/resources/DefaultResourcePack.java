/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.resources;

import com.google.common.collect.ImmutableSet;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.ResourceIndex;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import optifine.ReflectorForge;

public class DefaultResourcePack
implements IResourcePack {
    public static final Set<String> DEFAULT_RESOURCE_DOMAINS = ImmutableSet.of("minecraft", "realms");
    private final ResourceIndex resourceIndex;
    private static final boolean ON_WINDOWS = Util.getOSType() == Util.EnumOS.WINDOWS;

    public DefaultResourcePack(ResourceIndex resourceIndexIn) {
        this.resourceIndex = resourceIndexIn;
    }

    @Override
    public InputStream getInputStream(ResourceLocation location) throws IOException {
        InputStream inputstream = this.getInputStreamAssets(location);
        if (inputstream != null) {
            return inputstream;
        }
        InputStream inputstream1 = this.getResourceStream(location);
        if (inputstream1 != null) {
            return inputstream1;
        }
        throw new FileNotFoundException(location.getPath());
    }

    @Nullable
    public InputStream getInputStreamAssets(ResourceLocation location) throws IOException, FileNotFoundException {
        File file1 = this.resourceIndex.getFile(location);
        return file1 != null && file1.isFile() ? new FileInputStream(file1) : null;
    }

    @Nullable
    private InputStream getResourceStream(ResourceLocation location) {
        String s = "/assets/" + location.getNamespace() + "/" + location.getPath();
        InputStream inputstream = ReflectorForge.getOptiFineResourceStream(s);
        if (inputstream != null) {
            return inputstream;
        }
        try {
            URL url = DefaultResourcePack.class.getResource(s);
            return url != null && this.validatePath(new File(url.getFile()), s) ? DefaultResourcePack.class.getResourceAsStream(s) : null;
        }
        catch (IOException var5) {
            return DefaultResourcePack.class.getResourceAsStream(s);
        }
    }

    @Override
    public boolean resourceExists(ResourceLocation location) {
        return this.getResourceStream(location) != null || this.resourceIndex.isFileExisting(location);
    }

    @Override
    public Set<String> getResourceDomains() {
        return DEFAULT_RESOURCE_DOMAINS;
    }

    @Override
    @Nullable
    public <T extends IMetadataSection> T getPackMetadata(MetadataSerializer metadataSerializer, String metadataSectionName) throws IOException {
        try {
            FileInputStream inputstream = new FileInputStream(this.resourceIndex.getPackMcmeta());
            return AbstractResourcePack.readMetadata(metadataSerializer, inputstream, metadataSectionName);
        }
        catch (RuntimeException var4) {
            return (T)((IMetadataSection)null);
        }
        catch (FileNotFoundException var51) {
            return (T)((IMetadataSection)null);
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

    private boolean validatePath(File p_validatePath_1_, String p_validatePath_2_) throws IOException {
        String s = p_validatePath_1_.getPath();
        if (s.startsWith("file:")) {
            if (ON_WINDOWS) {
                s = s.replace("\\", "/");
            }
            return s.endsWith(p_validatePath_2_);
        }
        return FolderResourcePack.func_191384_a(p_validatePath_1_, p_validatePath_2_);
    }
}

