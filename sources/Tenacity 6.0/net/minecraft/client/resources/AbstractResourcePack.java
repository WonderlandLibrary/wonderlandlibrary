// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources;

import org.apache.logging.log4j.LogManager;
import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.awt.Image;
import net.minecraft.client.renderer.texture.TextureUtil;
import java.awt.image.BufferedImage;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.google.common.base.Charsets;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.util.ResourceLocation;
import java.io.File;
import org.apache.logging.log4j.Logger;

public abstract class AbstractResourcePack implements IResourcePack
{
    private static final Logger resourceLog;
    public final File resourcePackFile;
    
    public AbstractResourcePack(final File resourcePackFileIn) {
        this.resourcePackFile = resourcePackFileIn;
    }
    
    private static String locationToName(final ResourceLocation location) {
        return String.format("%s/%s/%s", "assets", location.getResourceDomain(), location.getResourcePath());
    }
    
    protected static String getRelativeName(final File p_110595_0_, final File p_110595_1_) {
        return p_110595_0_.toURI().relativize(p_110595_1_.toURI()).getPath();
    }
    
    @Override
    public InputStream getInputStream(final ResourceLocation location) throws IOException {
        return this.getInputStreamByName(locationToName(location));
    }
    
    @Override
    public boolean resourceExists(final ResourceLocation location) {
        return this.hasResourceName(locationToName(location));
    }
    
    protected abstract InputStream getInputStreamByName(final String p0) throws IOException;
    
    protected abstract boolean hasResourceName(final String p0);
    
    protected void logNameNotLowercase(final String name) {
        AbstractResourcePack.resourceLog.warn("ResourcePack: ignored non-lowercase namespace: {} in {}", new Object[] { name, this.resourcePackFile });
    }
    
    @Override
    public <T extends IMetadataSection> T getPackMetadata(final IMetadataSerializer metadataSerializer, final String metadataSectionName) throws IOException {
        return readMetadata(metadataSerializer, this.getInputStreamByName("pack.mcmeta"), metadataSectionName);
    }
    
    static <T extends IMetadataSection> T readMetadata(final IMetadataSerializer p_110596_0_, final InputStream p_110596_1_, final String p_110596_2_) {
        JsonObject jsonobject = null;
        BufferedReader bufferedreader = null;
        try {
            bufferedreader = new BufferedReader(new InputStreamReader(p_110596_1_, Charsets.UTF_8));
            jsonobject = new JsonParser().parse((Reader)bufferedreader).getAsJsonObject();
        }
        catch (RuntimeException runtimeexception) {
            throw new JsonParseException((Throwable)runtimeexception);
        }
        finally {
            IOUtils.closeQuietly((Reader)bufferedreader);
        }
        return p_110596_0_.parseMetadataSection(p_110596_2_, jsonobject);
    }
    
    @Override
    public BufferedImage getPackImage() throws IOException {
        final BufferedImage image = TextureUtil.readBufferedImage(this.getInputStreamByName("pack.png"));
        if (image == null) {
            return null;
        }
        if (image.getWidth() <= 64 && image.getHeight() <= 64) {
            return image;
        }
        final BufferedImage downscaledIcon = new BufferedImage(64, 64, 2);
        final Graphics graphics = downscaledIcon.getGraphics();
        graphics.drawImage(image, 0, 0, 64, 64, null);
        graphics.dispose();
        return downscaledIcon;
    }
    
    @Override
    public String getPackName() {
        return this.resourcePackFile.getName();
    }
    
    static {
        resourceLog = LogManager.getLogger();
    }
}
