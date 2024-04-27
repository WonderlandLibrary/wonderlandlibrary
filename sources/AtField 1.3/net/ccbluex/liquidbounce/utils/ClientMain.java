/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils;

import liying.fonts.api.FontManager;
import liying.fonts.impl.SimpleFontManager;
import net.ccbluex.liquidbounce.ui.client.newdropdown.SideGui.SideGui;
import net.ccbluex.liquidbounce.utils.ClientUtils;

public class ClientMain {
    private static ClientMain INSTANCE;
    private final SideGui sideGui;
    public FontManager fontManager = SimpleFontManager.create();

    public ClientMain() {
        this.sideGui = new SideGui();
    }

    public static ClientMain getInstance() {
        try {
            if (INSTANCE == null) {
                INSTANCE = new ClientMain();
            }
            return INSTANCE;
        }
        catch (Throwable throwable) {
            ClientUtils.getLogger().warn((Object)throwable);
            throw throwable;
        }
    }

    public FontManager getFontManager() {
        return this.fontManager;
    }

    public SideGui getSideGui() {
        return this.sideGui;
    }
}

