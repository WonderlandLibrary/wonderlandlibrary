/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Charsets
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParseException
 *  com.google.gson.JsonParser
 *  org.apache.commons.io.IOUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.resources;

import com.google.common.base.Charsets;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractResourcePack
implements IResourcePack {
    protected final File resourcePackFile;
    private static final Logger resourceLog = LogManager.getLogger();

    @Override
    public BufferedImage getPackImage() throws IOException {
        return TextureUtil.readBufferedImage(this.getInputStreamByName("pack.png"));
    }

    protected abstract InputStream getInputStreamByName(String var1) throws IOException;

    protected void logNameNotLowercase(String string) {
        resourceLog.warn("ResourcePack: ignored non-lowercase namespace: %s in %s", new Object[]{string, this.resourcePackFile});
    }

    @Override
    public boolean resourceExists(ResourceLocation resourceLocation) {
        return this.hasResourceName(AbstractResourcePack.locationToName(resourceLocation));
    }

    public AbstractResourcePack(File file) {
        this.resourcePackFile = file;
    }

    protected static String getRelativeName(File file, File file2) {
        return file.toURI().relativize(file2.toURI()).getPath();
    }

    private static String locationToName(ResourceLocation resourceLocation) {
        return String.format("%s/%s/%s", "assets", resourceLocation.getResourceDomain(), resourceLocation.getResourcePath());
    }

    @Override
    public InputStream getInputStream(ResourceLocation resourceLocation) throws IOException {
        return this.getInputStreamByName(AbstractResourcePack.locationToName(resourceLocation));
    }

    protected abstract boolean hasResourceName(String var1);

    static <T extends IMetadataSection> T readMetadata(IMetadataSerializer iMetadataSerializer, InputStream inputStream, String string) {
        JsonObject jsonObject = null;
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charsets.UTF_8));
            jsonObject = new JsonParser().parse((Reader)bufferedReader).getAsJsonObject();
        }
        catch (RuntimeException runtimeException) {
            throw new JsonParseException((Throwable)runtimeException);
        }
        IOUtils.closeQuietly((Reader)bufferedReader);
        return iMetadataSerializer.parseMetadataSection(string, jsonObject);
    }

    @Override
    public String getPackName() {
        return this.resourcePackFile.getName();
    }

    @Override
    public <T extends IMetadataSection> T getPackMetadata(IMetadataSerializer iMetadataSerializer, String string) throws IOException {
        return AbstractResourcePack.readMetadata(iMetadataSerializer, this.getInputStreamByName("pack.mcmeta"), string);
    }
}

