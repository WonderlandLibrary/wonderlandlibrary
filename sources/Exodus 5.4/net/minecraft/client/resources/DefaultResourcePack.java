/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 */
package net.minecraft.client.resources;

import com.google.common.collect.ImmutableSet;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;

public class DefaultResourcePack
implements IResourcePack {
    private final Map<String, File> mapAssets;
    public static final Set<String> defaultResourceDomains = ImmutableSet.of((Object)"minecraft", (Object)"realms");

    @Override
    public Set<String> getResourceDomains() {
        return defaultResourceDomains;
    }

    @Override
    public boolean resourceExists(ResourceLocation resourceLocation) {
        return this.getResourceStream(resourceLocation) != null || this.mapAssets.containsKey(resourceLocation.toString());
    }

    @Override
    public <T extends IMetadataSection> T getPackMetadata(IMetadataSerializer iMetadataSerializer, String string) throws IOException {
        try {
            FileInputStream fileInputStream = new FileInputStream(this.mapAssets.get("pack.mcmeta"));
            return AbstractResourcePack.readMetadata(iMetadataSerializer, fileInputStream, string);
        }
        catch (RuntimeException runtimeException) {
            return null;
        }
        catch (FileNotFoundException fileNotFoundException) {
            return null;
        }
    }

    private InputStream getResourceStream(ResourceLocation resourceLocation) {
        return DefaultResourcePack.class.getResourceAsStream("/assets/" + resourceLocation.getResourceDomain() + "/" + resourceLocation.getResourcePath());
    }

    @Override
    public String getPackName() {
        return "Default";
    }

    @Override
    public BufferedImage getPackImage() throws IOException {
        return TextureUtil.readBufferedImage(DefaultResourcePack.class.getResourceAsStream("/" + new ResourceLocation("pack.png").getResourcePath()));
    }

    @Override
    public InputStream getInputStream(ResourceLocation resourceLocation) throws IOException {
        InputStream inputStream = this.getResourceStream(resourceLocation);
        if (inputStream != null) {
            return inputStream;
        }
        InputStream inputStream2 = this.getInputStreamAssets(resourceLocation);
        if (inputStream2 != null) {
            return inputStream2;
        }
        throw new FileNotFoundException(resourceLocation.getResourcePath());
    }

    public InputStream getInputStreamAssets(ResourceLocation resourceLocation) throws FileNotFoundException, IOException {
        File file = this.mapAssets.get(resourceLocation.toString());
        return file != null && file.isFile() ? new FileInputStream(file) : null;
    }

    public DefaultResourcePack(Map<String, File> map) {
        this.mapAssets = map;
    }
}

