package client.module.impl.render;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.TickEvent;
import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;

@ModuleInfo(name = "Full Bright", description = "Prevents world darkness", category = Category.RENDER)
public class FullBright extends Module {

    @Override
    protected void onDisable() {
        mc.gameSettings.gammaSetting = 0.0F;
    }

    @EventLink
    public final Listener<TickEvent> onTick = event -> {
        mc.gameSettings.gammaSetting = 100.0F;
    };
}
