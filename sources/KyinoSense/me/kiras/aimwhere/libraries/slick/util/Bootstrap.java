/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.util;

import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.Game;

public class Bootstrap {
    public static void runAsApplication(Game game, int width, int height, boolean fullscreen) {
        try {
            AppGameContainer container = new AppGameContainer(game, width, height, fullscreen);
            container.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

