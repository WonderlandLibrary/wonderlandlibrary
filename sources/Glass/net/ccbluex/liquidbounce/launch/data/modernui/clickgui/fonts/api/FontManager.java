/*
 * GlassClient PVP Client
 * A free open-source mixin-based PVP client based on liquidbounce with all cheats removed.
 * https://github.com/GlassClient/GlassClient
 */
package net.ccbluex.liquidbounce.launch.data.modernui.clickgui.fonts.api;
@FunctionalInterface
public interface FontManager {
	FontFamily fontFamily(FontType fontType);
	default FontRenderer font(FontType fontType, int size) {
		return fontFamily(fontType).ofSize(size);
	}
}
