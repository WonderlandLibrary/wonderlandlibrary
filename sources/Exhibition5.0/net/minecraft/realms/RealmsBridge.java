// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.realms;

import org.apache.logging.log4j.LogManager;
import java.lang.reflect.Constructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.apache.logging.log4j.Logger;

public class RealmsBridge extends RealmsScreen
{
    private static final Logger LOGGER;
    private GuiScreen previousScreen;
    private static final String __OBFID = "CL_00001869";
    
    public void switchToRealms(final GuiScreen p_switchToRealms_1_) {
        this.previousScreen = p_switchToRealms_1_;
        try {
            final Class var2 = Class.forName("com.mojang.realmsclient.RealmsMainScreen");
            final Constructor var3 = var2.getDeclaredConstructor(RealmsScreen.class);
            var3.setAccessible(true);
            final Object var4 = var3.newInstance(this);
            Minecraft.getMinecraft().displayGuiScreen(((RealmsScreen)var4).getProxy());
        }
        catch (Exception var5) {
            RealmsBridge.LOGGER.error("Realms module missing", (Throwable)var5);
        }
    }
    
    @Override
    public void init() {
        Minecraft.getMinecraft().displayGuiScreen(this.previousScreen);
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
