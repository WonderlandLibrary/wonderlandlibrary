/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.resources.IResource
 *  net.minecraft.client.resources.IResourceManager
 *  net.minecraft.util.ResourceLocation
 */
package liying.fonts.impl;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import liying.fonts.api.FontFamily;
import liying.fonts.api.FontManager;
import liying.fonts.api.FontType;
import liying.fonts.impl.SimpleFontFamily;
import liying.fonts.util.SneakyThrowing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public final class SimpleFontManager
implements FontManager {
    private static final String FONT_DIRECTORY = "More/font/";
    private final FontRegistry fonts = new FontRegistry(null);

    public static FontManager create() {
        return new SimpleFontManager();
    }

    private SimpleFontManager() {
    }

    @Override
    public FontFamily fontFamily(FontType fontType) {
        return FontRegistry.access$100(this.fonts, fontType);
    }

    private static final class FontRegistry
    extends EnumMap {
        FontRegistry(1 var1_1) {
            this();
        }

        private static Font readFontFromResources(FontType fontType) throws IOException {
            Font font;
            block7: {
                InputStream inputStream;
                block8: {
                    IResource iResource;
                    IResourceManager iResourceManager = Minecraft.func_71410_x().func_110442_L();
                    ResourceLocation resourceLocation = new ResourceLocation("More/font/" + fontType.fileName());
                    try {
                        iResource = iResourceManager.func_110536_a(resourceLocation);
                    }
                    catch (IOException iOException) {
                        throw new IOException("Couldn't find resource: " + resourceLocation, iOException);
                    }
                    inputStream = iResource.func_110527_b();
                    Throwable throwable = null;
                    try {
                        font = FontRegistry.readFont(inputStream);
                        if (inputStream == null) break block7;
                        if (throwable == null) break block8;
                    }
                    catch (Throwable throwable2) {
                        throwable = throwable2;
                        throw throwable2;
                    }
                    try {
                        inputStream.close();
                    }
                    catch (Throwable throwable3) {
                        throwable.addSuppressed(throwable3);
                    }
                    break block7;
                }
                inputStream.close();
            }
            return font;
        }

        private FontFamily fontFamily(FontType fontType) {
            return this.computeIfAbsent(fontType, arg_0 -> FontRegistry.lambda$fontFamily$0(fontType, arg_0));
        }

        private FontRegistry() {
            super(FontType.class);
        }

        static FontFamily access$100(FontRegistry fontRegistry, FontType fontType) {
            return fontRegistry.fontFamily(fontType);
        }

        private static Font readFont(InputStream inputStream) {
            Font font;
            try {
                font = Font.createFont(0, inputStream);
            }
            catch (FontFormatException fontFormatException) {
                throw new RuntimeException("Resource does not contain the required font tables for the specified format", fontFormatException);
            }
            catch (IOException iOException) {
                throw new RuntimeException("Couldn't completely read font resource", iOException);
            }
            return font;
        }

        private static FontFamily lambda$fontFamily$0(FontType fontType, FontType fontType2) {
            try {
                return SimpleFontFamily.create(fontType, FontRegistry.readFontFromResources(fontType));
            }
            catch (IOException iOException) {
                throw SneakyThrowing.sneakyThrow(iOException);
            }
        }
    }
}

