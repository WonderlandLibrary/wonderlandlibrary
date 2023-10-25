/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.realms;

import java.lang.reflect.Constructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.realms.RealmsScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RealmsBridge
extends RealmsScreen {
    private GuiScreen previousScreen;
    private static final Logger LOGGER = LogManager.getLogger();

    public void switchToRealms(GuiScreen guiScreen) {
        this.previousScreen = guiScreen;
        try {
            Class<?> clazz = Class.forName("com.mojang.realmsclient.RealmsMainScreen");
            Constructor<?> constructor = clazz.getDeclaredConstructor(RealmsScreen.class);
            constructor.setAccessible(true);
            Object obj = constructor.newInstance(this);
            Minecraft.getMinecraft().displayGuiScreen(((RealmsScreen)obj).getProxy());
        }
        catch (Exception exception) {
            LOGGER.error("Realms module missing", (Throwable)exception);
        }
    }

    @Override
    public void init() {
        Minecraft.getMinecraft().displayGuiScreen(this.previousScreen);
    }
}

