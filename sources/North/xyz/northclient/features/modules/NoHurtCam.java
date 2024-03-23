package xyz.northclient.features.modules;

import xyz.northclient.features.AbstractModule;
import xyz.northclient.features.Category;
import xyz.northclient.features.EventLink;
import xyz.northclient.features.ModuleInfo;
import xyz.northclient.features.events.HurtCamEvent;

@ModuleInfo(name = "NoHurtCam",description = "Disable shake after hurt",category = Category.RENDER)
public class NoHurtCam extends AbstractModule {
    @EventLink
    public void onHurt(HurtCamEvent e) {
        e.setCancelled(true);
    }
}
