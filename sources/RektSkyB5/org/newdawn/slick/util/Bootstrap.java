/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.util;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Game;

public class Bootstrap {
    public static void runAsApplication(Game game, int width, int height, boolean fullscreen) {
        try {
            AppGameContainer container = new AppGameContainer(game, width, height, fullscreen);
            container.start();
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}

