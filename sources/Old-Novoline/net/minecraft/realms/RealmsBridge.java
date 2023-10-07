package net.minecraft.realms;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;

public class RealmsBridge extends RealmsScreen {

    private static final Logger LOGGER = LogManager.getLogger();

    private GuiScreen previousScreen;

    public void switchToRealms(GuiScreen previousScreen) {
        this.previousScreen = previousScreen;

        try {
            Class<?> clazz = Class.forName("com.mojang.realmsclient.RealmsMainScreen");
            Constructor<?> constructor = clazz.getDeclaredConstructor(RealmsScreen.class);
            constructor.setAccessible(true);
            Object object = constructor.newInstance(this);

            Minecraft.getInstance().displayGuiScreen(((RealmsScreen) object).getProxy());
        } catch (Exception e) {
            LOGGER.error("Realms module missing", e);
        }
    }

    public void init() {
        Minecraft.getInstance().displayGuiScreen(this.previousScreen);
    }

}
