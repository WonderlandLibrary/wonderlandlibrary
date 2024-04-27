/*
 * GlassClient PVP Client
 * A free open-source mixin-based PVP client based on liquidbounce with all cheats removed.
 * https://github.com/GlassClient/GlassClient
 */
package net.ccbluex.liquidbounce.launch.data.modernui.clickgui.fonts.logo;

import net.ccbluex.liquidbounce.launch.data.modernui.clickgui.fonts.api.FontManager;
import net.ccbluex.liquidbounce.launch.data.modernui.clickgui.fonts.impl.SimpleFontManager;

public class info {
    public static String Name = "FDPCLIENT";

    public static String version = "";
    public static String username;
    private static info INSTANCE;
    public static info getInstance() {
        try {
            if (INSTANCE == null) INSTANCE = new info();
            return INSTANCE;
        } catch (Throwable t) {
            //    ClientUtils.getLogger().warn(t);
            throw t;
        }
    }
    public static FontManager fontManager = SimpleFontManager.create();
    public static FontManager getFontManager() {
        return fontManager;
    }
}