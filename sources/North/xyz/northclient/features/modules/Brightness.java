package xyz.northclient.features.modules;

import xyz.northclient.features.AbstractModule;
import xyz.northclient.features.Category;
import xyz.northclient.features.EventLink;
import xyz.northclient.features.ModuleInfo;
import xyz.northclient.features.events.UpdateEvent;

@ModuleInfo(name = "Brightness",description = "Set full bright",category = Category.RENDER)
public class Brightness extends AbstractModule {

    @EventLink
    public void onUpdate(UpdateEvent event) {
        mc.gameSettings.gammaSetting = 1f;
    }
}
