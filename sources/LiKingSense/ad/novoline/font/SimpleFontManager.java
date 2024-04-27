/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.resources.IResource
 *  net.minecraft.client.resources.IResourceManager
 *  net.minecraft.util.ResourceLocation
 */
package ad.novoline.font;

import ad.novoline.font.FontFamily;
import ad.novoline.font.FontManager;
import ad.novoline.font.FontType;
import ad.novoline.font.SimpleFontFamily;
import ad.novoline.font.SneakyThrowing;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public final class SimpleFontManager
implements FontManager {
    private static final String FONT_DIRECTORY = "likingsense/font/";
    private final FontRegistry fonts = new FontRegistry();

    private SimpleFontManager() {
    }

    public static FontManager create() {
        return new SimpleFontManager();
    }

    @Override
    public FontFamily fontFamily(FontType fontType) {
        return this.fonts.fontFamily(fontType);
    }

    private static final class FontRegistry
    extends EnumMap<FontType, FontFamily> {
        private FontRegistry() {
            super(FontType.class);
        }

        private FontFamily fontFamily(FontType fontType) {
            return this.computeIfAbsent(fontType, ignored -> {
                try {
                    return SimpleFontFamily.create(fontType, FontRegistry.readFontFromResources(fontType));
                }
                catch (IOException e) {
                    throw SneakyThrowing.sneakyThrow(e);
                }
            });
        }

        private static Font readFontFromResources(FontType fontType) throws IOException {
            IResource resource;
            IResourceManager resourceManager = Minecraft.func_71410_x().func_110442_L();
            ResourceLocation location = new ResourceLocation(SimpleFontManager.FONT_DIRECTORY + fontType.fileName());
            try {
                resource = resourceManager.func_110536_a(location);
            }
            catch (IOException e) {
                throw new IOException("Couldn't find resource: " + location, e);
            }
            try (InputStream resourceInputStream = resource.func_110527_b();){
                Font font = FontRegistry.readFont(resourceInputStream);
                return font;
            }
        }

        private static Font readFont(InputStream resource) {
            Font font;
            try {
                font = Font.createFont(0, resource);
            }
            catch (FontFormatException e) {
                throw new RuntimeException("Resource does not contain the required font tables for the specified format", e);
            }
            catch (IOException e) {
                throw new RuntimeException("Couldn't completely read font resource", e);
            }
            return font;
        }
    }
}

