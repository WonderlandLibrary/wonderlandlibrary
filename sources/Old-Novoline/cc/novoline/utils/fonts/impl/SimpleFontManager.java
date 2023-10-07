package cc.novoline.utils.fonts.impl;

import cc.novoline.utils.SneakyThrowing;
import cc.novoline.utils.fonts.api.FontFamily;
import cc.novoline.utils.fonts.api.FontManager;
import cc.novoline.utils.fonts.api.FontType;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

/**
 * @author Artyom Popov
 * @since June 30, 2020
 */
public final class SimpleFontManager implements FontManager {

	//region instantiating
	private SimpleFontManager() {}

	public static FontManager create() {
		return new SimpleFontManager();
	}
	//endregion

	private static final String FONT_DIRECTORY = "novoline/fonts/";
	private final FontRegistry fonts = new FontRegistry();

	@Override
	public FontFamily fontFamily(FontType fontType) {
		return fonts.fontFamily(fontType);
	}

	private static final class FontRegistry extends EnumMap<FontType, FontFamily> {

		private FontRegistry() { super(FontType.class); }

		private FontFamily fontFamily(FontType fontType) {
			return computeIfAbsent(fontType, ignored -> {
				try {
					return SimpleFontFamily.create(fontType, readFontFromResources(fontType));
				} catch(IOException e) {
					throw SneakyThrowing.sneakyThrow(e);
				}
			});
		}

		//region reading font
		private static java.awt.Font readFontFromResources(FontType fontType) throws IOException {
			IResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
			ResourceLocation location = new ResourceLocation(FONT_DIRECTORY + fontType.fileName());
			IResource resource;

			try {
				resource = resourceManager.getResource(location);
			} catch(IOException e) {
				throw new IOException("Couldn't find resource: " + location, e);
			}

			try(InputStream resourceInputStream = resource.getInputStream()) {
				return readFont(resourceInputStream);
			}
		}

		private static java.awt.Font readFont(InputStream resource) {
			java.awt.Font font;

			try {
				font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, resource);
			} catch(FontFormatException e) {
				throw new RuntimeException("Resource does not contain the required font tables for the specified format", e);
			} catch(IOException e) {
				throw new RuntimeException("Couldn't completely read font resource", e);
			}

			return font;
		}
		//endregion
	}
}
