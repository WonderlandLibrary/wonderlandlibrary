package info.sigmaclient.sigma.modules;

import info.sigmaclient.sigma.gui.hud.notification.NotificationManager;
import info.sigmaclient.sigma.premium.PremiumManager;
import top.fl0wowp4rty.phantomshield.annotations.Native;

@Native
public class PremiumModule extends Module {
    public PremiumModule(String name, String desc) {
        super(name, desc);
    }

    public PremiumModule(String name, Category category, String desc, boolean notJello) {
        super(name, category, desc, notJello);
    }

    public PremiumModule(String name, Category category, String desc) {
        super(name, category, desc);
    }

    public PremiumModule(String name, Category category, String desc, int key) {
        super(name, category, desc, key);
    }

    @Override
    public void onChangeStatus() {
        if(enabled && !PremiumManager.isPremium()){
            enabled = false;
            onDisable();
            NotificationManager.notify(remapName, "not yet supported free version");
        }
        super.onChangeStatus();
    }
}
