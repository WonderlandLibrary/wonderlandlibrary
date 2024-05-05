package net.optifine.shaders;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.resources.data.AnimationMetadataSectionSerializer;
import net.minecraft.client.resources.data.FontMetadataSection;
import net.minecraft.client.resources.data.FontMetadataSectionSerializer;
import net.minecraft.client.resources.data.IMetadataSectionSerializer;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.client.resources.data.LanguageMetadataSection;
import net.minecraft.client.resources.data.LanguageMetadataSectionSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.client.resources.data.PackMetadataSectionSerializer;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.client.resources.data.TextureMetadataSectionSerializer;
import net.optifine.shaders.SMCLog;
import net.optifine.shaders.Shaders;
import org.apache.commons.io.IOUtils;

public class SimpleShaderTexture
extends AbstractTexture {
    private String texturePath;
    private static final IMetadataSerializer METADATA_SERIALIZER = SimpleShaderTexture.makeMetadataSerializer();

    public SimpleShaderTexture(String texturePath) {
        this.texturePath = texturePath;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void loadTexture(IResourceManager resourceManager) throws IOException {
        this.deleteGlTexture();
        InputStream inputstream = Shaders.getShaderPackResourceStream((String)this.texturePath);
        if (inputstream == null) {
            throw new FileNotFoundException("Shader texture not found: " + this.texturePath);
        }
        try {
            BufferedImage bufferedimage = TextureUtil.readBufferedImage((InputStream)inputstream);
            TextureMetadataSection texturemetadatasection = SimpleShaderTexture.loadTextureMetadataSection(this.texturePath, new TextureMetadataSection(false, false, (List)new ArrayList()));
            TextureUtil.uploadTextureImageAllocate((int)this.getGlTextureId(), (BufferedImage)bufferedimage, (boolean)texturemetadatasection.getTextureBlur(), (boolean)texturemetadatasection.getTextureClamp());
        }
        finally {
            IOUtils.closeQuietly((InputStream)inputstream);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static TextureMetadataSection loadTextureMetadataSection(String texturePath, TextureMetadataSection def) {
        String s = texturePath + ".mcmeta";
        String s1 = "texture";
        InputStream inputstream = Shaders.getShaderPackResourceStream((String)s);
        if (inputstream != null) {
            TextureMetadataSection texturemetadatasection1;
            IMetadataSerializer imetadataserializer = METADATA_SERIALIZER;
            BufferedReader bufferedreader = new BufferedReader((Reader)new InputStreamReader(inputstream));
            try {
                JsonObject jsonobject = new JsonParser().parse((Reader)bufferedreader).getAsJsonObject();
                TextureMetadataSection texturemetadatasection = (TextureMetadataSection)imetadataserializer.parseMetadataSection(s1, jsonobject);
                if (texturemetadatasection == null) {
                    TextureMetadataSection textureMetadataSection = def;
                    return textureMetadataSection;
                }
                texturemetadatasection1 = texturemetadatasection;
            }
            catch (RuntimeException runtimeexception) {
                SMCLog.warning((String)("Error reading metadata: " + s));
                SMCLog.warning((String)("" + runtimeexception.getClass().getName() + ": " + runtimeexception.getMessage()));
                TextureMetadataSection textureMetadataSection = def;
                return textureMetadataSection;
            }
            finally {
                IOUtils.closeQuietly((Reader)bufferedreader);
                IOUtils.closeQuietly((InputStream)inputstream);
            }
            return texturemetadatasection1;
        }
        return def;
    }

    private static IMetadataSerializer makeMetadataSerializer() {
        IMetadataSerializer imetadataserializer = new IMetadataSerializer();
        imetadataserializer.registerMetadataSectionType((IMetadataSectionSerializer)new TextureMetadataSectionSerializer(), TextureMetadataSection.class);
        imetadataserializer.registerMetadataSectionType((IMetadataSectionSerializer)new FontMetadataSectionSerializer(), FontMetadataSection.class);
        imetadataserializer.registerMetadataSectionType((IMetadataSectionSerializer)new AnimationMetadataSectionSerializer(), AnimationMetadataSection.class);
        imetadataserializer.registerMetadataSectionType((IMetadataSectionSerializer)new PackMetadataSectionSerializer(), PackMetadataSection.class);
        imetadataserializer.registerMetadataSectionType((IMetadataSectionSerializer)new LanguageMetadataSectionSerializer(), LanguageMetadataSection.class);
        return imetadataserializer;
    }
}
