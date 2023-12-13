package info.sigmaclient.management.users;

import info.sigmaclient.Client;
import info.sigmaclient.gui.click.ClickGui;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.impl.premium.*;

public class PremiumLoader {

    static boolean loadedOnce = false;
    public static boolean load() {
       if (!loadedOnce) {
           Client.getModuleManager().add(new HypixelScaffoldAddon());
           Client.getModuleManager().add(new PremiumFlyAddon());
           Client.getModuleManager().add(new AutoClutch());
           Client.getModuleManager().add(new LongjumpMineplexAddon());
           Client.getModuleManager().add(new PremiumBhopAddon());
           Client.getModuleManager().add(new PremiumAntiBotAddon());
           Client.getModuleManager().add(new PremiumTeleportAddon());
           Client.getModuleManager().add(new PremiumInfiniteAuraAddon());
           Client.clickGui = new ClickGui();
           Module.loadSettings();
           loadedOnce = true;
       }
        return true;
    }
}
