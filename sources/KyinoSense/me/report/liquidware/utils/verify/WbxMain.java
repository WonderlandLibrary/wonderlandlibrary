/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.Display
 */
package me.report.liquidware.utils.verify;

import net.ccbluex.liquidbounce.cn.Insane.Module.fonts.api.FontManager;
import net.ccbluex.liquidbounce.cn.Insane.Module.fonts.impl.SimpleFontManager;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.SideGui.SideGui;
import org.lwjgl.opengl.Display;

public class WbxMain {
    public static String Name = "KyinoClient";
    public static String Rank = "";
    public static String version = "";
    public static String username;
    private static WbxMain INSTANCE;
    private final SideGui sideGui = new SideGui();
    public static boolean got;
    public static FontManager fontManager;

    public static void LoadingTitle() {
        Display.setTitle((String)"KyinoClient Loading...");
    }

    public SideGui getSideGui() {
        return this.sideGui;
    }

    public static WbxMain getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new WbxMain();
        }
        return INSTANCE;
    }

    public static FontManager getFontManager() {
        return fontManager;
    }

    static {
        got = false;
        fontManager = SimpleFontManager.create();
    }
}

