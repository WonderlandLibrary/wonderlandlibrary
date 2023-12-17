// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.realms;

import org.apache.logging.log4j.LogManager;
import net.minecraft.client.gui.GuiScreenRealmsProxy;
import java.lang.reflect.Constructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.apache.logging.log4j.Logger;

public class RealmsBridge extends RealmsScreen
{
    private static final Logger LOGGER;
    private GuiScreen previousScreen;
    
    public void switchToRealms(final GuiScreen p_switchToRealms_1_) {
        this.previousScreen = p_switchToRealms_1_;
        try {
            final Class<?> oclass = Class.forName("com.mojang.realmsclient.RealmsMainScreen");
            final Constructor<?> constructor = oclass.getDeclaredConstructor(RealmsScreen.class);
            constructor.setAccessible(true);
            final Object object = constructor.newInstance(this);
            Minecraft.getMinecraft().displayGuiScreen(((RealmsScreen)object).getProxy());
        }
        catch (ClassNotFoundException var5) {
            RealmsBridge.LOGGER.error("Realms module missing");
        }
        catch (Exception exception) {
            RealmsBridge.LOGGER.error("Failed to load Realms module", (Throwable)exception);
        }
    }
    
    public GuiScreenRealmsProxy getNotificationScreen(final GuiScreen p_getNotificationScreen_1_) {
        try {
            this.previousScreen = p_getNotificationScreen_1_;
            final Class<?> oclass = Class.forName("com.mojang.realmsclient.gui.screens.RealmsNotificationsScreen");
            final Constructor<?> constructor = oclass.getDeclaredConstructor(RealmsScreen.class);
            constructor.setAccessible(true);
            final Object object = constructor.newInstance(this);
            return ((RealmsScreen)object).getProxy();
        }
        catch (ClassNotFoundException var5) {
            RealmsBridge.LOGGER.error("Realms module missing");
        }
        catch (Exception exception) {
            RealmsBridge.LOGGER.error("Failed to load Realms module", (Throwable)exception);
        }
        return null;
    }
    
    @Override
    public void init() {
        Minecraft.getMinecraft().displayGuiScreen(this.previousScreen);
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
