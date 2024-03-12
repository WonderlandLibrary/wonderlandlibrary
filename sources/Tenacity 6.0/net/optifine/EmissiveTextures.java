// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import net.optifine.util.PropertiesOrdered;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.src.Config;
import net.minecraft.client.renderer.texture.SimpleTexture;
import java.util.Map;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.ResourceLocation;

public class EmissiveTextures
{
    private static String suffixEmissive;
    private static String suffixEmissivePng;
    private static boolean active;
    private static boolean render;
    private static boolean hasEmissive;
    private static boolean renderEmissive;
    private static float lightMapX;
    private static float lightMapY;
    private static final String SUFFIX_PNG = ".png";
    private static final ResourceLocation LOCATION_EMPTY;
    
    public static boolean isActive() {
        return EmissiveTextures.active;
    }
    
    public static String getSuffixEmissive() {
        return EmissiveTextures.suffixEmissive;
    }
    
    public static void beginRender() {
        EmissiveTextures.render = true;
        EmissiveTextures.hasEmissive = false;
    }
    
    public static ITextureObject getEmissiveTexture(final ITextureObject texture, final Map<ResourceLocation, ITextureObject> mapTextures) {
        if (!EmissiveTextures.render) {
            return texture;
        }
        if (!(texture instanceof SimpleTexture)) {
            return texture;
        }
        final SimpleTexture simpletexture = (SimpleTexture)texture;
        ResourceLocation resourcelocation = simpletexture.locationEmissive;
        if (!EmissiveTextures.renderEmissive) {
            if (resourcelocation != null) {
                EmissiveTextures.hasEmissive = true;
            }
            return texture;
        }
        if (resourcelocation == null) {
            resourcelocation = EmissiveTextures.LOCATION_EMPTY;
        }
        ITextureObject itextureobject = mapTextures.get(resourcelocation);
        if (itextureobject == null) {
            itextureobject = new SimpleTexture(resourcelocation);
            final TextureManager texturemanager = Config.getTextureManager();
            texturemanager.loadTexture(resourcelocation, itextureobject);
        }
        return itextureobject;
    }
    
    public static boolean hasEmissive() {
        return EmissiveTextures.hasEmissive;
    }
    
    public static void beginRenderEmissive() {
        EmissiveTextures.lightMapX = OpenGlHelper.lastBrightnessX;
        EmissiveTextures.lightMapY = OpenGlHelper.lastBrightnessY;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, EmissiveTextures.lightMapY);
        EmissiveTextures.renderEmissive = true;
    }
    
    public static void endRenderEmissive() {
        EmissiveTextures.renderEmissive = false;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, EmissiveTextures.lightMapX, EmissiveTextures.lightMapY);
    }
    
    public static void endRender() {
        EmissiveTextures.render = false;
        EmissiveTextures.hasEmissive = false;
    }
    
    public static void update() {
        EmissiveTextures.active = false;
        EmissiveTextures.suffixEmissive = null;
        EmissiveTextures.suffixEmissivePng = null;
        if (Config.isEmissiveTextures()) {
            try {
                final String s = "optifine/emissive.properties";
                final ResourceLocation resourcelocation = new ResourceLocation(s);
                final InputStream inputstream = Config.getResourceStream(resourcelocation);
                if (inputstream == null) {
                    return;
                }
                dbg("Loading " + s);
                final Properties properties = new PropertiesOrdered();
                properties.load(inputstream);
                inputstream.close();
                EmissiveTextures.suffixEmissive = properties.getProperty("suffix.emissive");
                if (EmissiveTextures.suffixEmissive != null) {
                    EmissiveTextures.suffixEmissivePng = EmissiveTextures.suffixEmissive + ".png";
                }
                EmissiveTextures.active = (EmissiveTextures.suffixEmissive != null);
            }
            catch (FileNotFoundException var4) {}
            catch (IOException ioexception) {
                ioexception.printStackTrace();
            }
        }
    }
    
    private static void dbg(final String str) {
        Config.dbg("EmissiveTextures: " + str);
    }
    
    private static void warn(final String str) {
        Config.warn("EmissiveTextures: " + str);
    }
    
    public static boolean isEmissive(final ResourceLocation loc) {
        return EmissiveTextures.suffixEmissivePng != null && loc.getResourcePath().endsWith(EmissiveTextures.suffixEmissivePng);
    }
    
    public static void loadTexture(final ResourceLocation loc, final SimpleTexture tex) {
        if (loc != null && tex != null) {
            tex.isEmissive = false;
            tex.locationEmissive = null;
            if (EmissiveTextures.suffixEmissivePng != null) {
                final String s = loc.getResourcePath();
                if (s.endsWith(".png")) {
                    if (s.endsWith(EmissiveTextures.suffixEmissivePng)) {
                        tex.isEmissive = true;
                    }
                    else {
                        final String s2 = s.substring(0, s.length() - ".png".length()) + EmissiveTextures.suffixEmissivePng;
                        final ResourceLocation resourcelocation = new ResourceLocation(loc.getResourceDomain(), s2);
                        if (Config.hasResource(resourcelocation)) {
                            tex.locationEmissive = resourcelocation;
                        }
                    }
                }
            }
        }
    }
    
    static {
        EmissiveTextures.suffixEmissive = null;
        EmissiveTextures.suffixEmissivePng = null;
        EmissiveTextures.active = false;
        EmissiveTextures.render = false;
        EmissiveTextures.hasEmissive = false;
        EmissiveTextures.renderEmissive = false;
        LOCATION_EMPTY = new ResourceLocation("mcpatcher/ctm/default/empty.png");
    }
}
