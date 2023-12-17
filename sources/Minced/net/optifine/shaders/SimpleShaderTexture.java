// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.shaders;

import net.minecraft.client.resources.data.LanguageMetadataSection;
import net.minecraft.client.resources.data.LanguageMetadataSectionSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.client.resources.data.PackMetadataSectionSerializer;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.resources.data.AnimationMetadataSectionSerializer;
import net.minecraft.client.resources.data.FontMetadataSection;
import net.minecraft.client.resources.data.FontMetadataSectionSerializer;
import net.minecraft.client.resources.data.IMetadataSectionSerializer;
import net.minecraft.client.resources.data.TextureMetadataSectionSerializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.client.renderer.texture.TextureUtil;
import java.io.FileNotFoundException;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.client.renderer.texture.AbstractTexture;

public class SimpleShaderTexture extends AbstractTexture
{
    private String texturePath;
    private static final MetadataSerializer METADATA_SERIALIZER;
    
    public SimpleShaderTexture(final String texturePath) {
        this.texturePath = texturePath;
    }
    
    @Override
    public void loadTexture(final IResourceManager resourceManager) throws IOException {
        this.deleteGlTexture();
        final InputStream inputstream = Shaders.getShaderPackResourceStream(this.texturePath);
        if (inputstream == null) {
            throw new FileNotFoundException("Shader texture not found: " + this.texturePath);
        }
        try {
            final BufferedImage bufferedimage = TextureUtil.readBufferedImage(inputstream);
            final TextureMetadataSection texturemetadatasection = loadTextureMetadataSection(this.texturePath, new TextureMetadataSection(false, false));
            TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), bufferedimage, texturemetadatasection.getTextureBlur(), texturemetadatasection.getTextureClamp());
        }
        finally {
            IOUtils.closeQuietly(inputstream);
        }
    }
    
    public static TextureMetadataSection loadTextureMetadataSection(final String texturePath, final TextureMetadataSection def) {
        final String s = texturePath + ".mcmeta";
        final String s2 = "texture";
        final InputStream inputstream = Shaders.getShaderPackResourceStream(s);
        if (inputstream != null) {
            final MetadataSerializer metadataserializer = SimpleShaderTexture.METADATA_SERIALIZER;
            final BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream));
            TextureMetadataSection texturemetadatasection2;
            try {
                final JsonObject jsonobject = new JsonParser().parse((Reader)bufferedreader).getAsJsonObject();
                final TextureMetadataSection texturemetadatasection = metadataserializer.parseMetadataSection(s2, jsonobject);
                if (texturemetadatasection == null) {
                    return def;
                }
                texturemetadatasection2 = texturemetadatasection;
            }
            catch (RuntimeException runtimeexception) {
                SMCLog.warning("Error reading metadata: " + s);
                SMCLog.warning("" + runtimeexception.getClass().getName() + ": " + runtimeexception.getMessage());
                return def;
            }
            finally {
                IOUtils.closeQuietly((Reader)bufferedreader);
                IOUtils.closeQuietly(inputstream);
            }
            return texturemetadatasection2;
        }
        return def;
    }
    
    private static MetadataSerializer makeMetadataSerializer() {
        final MetadataSerializer metadataserializer = new MetadataSerializer();
        metadataserializer.registerMetadataSectionType(new TextureMetadataSectionSerializer(), TextureMetadataSection.class);
        metadataserializer.registerMetadataSectionType(new FontMetadataSectionSerializer(), FontMetadataSection.class);
        metadataserializer.registerMetadataSectionType(new AnimationMetadataSectionSerializer(), AnimationMetadataSection.class);
        metadataserializer.registerMetadataSectionType(new PackMetadataSectionSerializer(), PackMetadataSection.class);
        metadataserializer.registerMetadataSectionType(new LanguageMetadataSectionSerializer(), LanguageMetadataSection.class);
        return metadataserializer;
    }
    
    static {
        METADATA_SERIALIZER = makeMetadataSerializer();
    }
}
