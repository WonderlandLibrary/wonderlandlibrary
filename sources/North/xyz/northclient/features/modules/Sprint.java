package xyz.northclient.features.modules;

import org.lwjgl.input.Keyboard;
import xyz.northclient.NorthSingleton;
import xyz.northclient.features.AbstractModule;
import xyz.northclient.features.Category;
import xyz.northclient.features.EventLink;
import xyz.northclient.features.ModuleInfo;
import xyz.northclient.features.events.TickEvent;
import xyz.northclient.features.events.UpdateEvent;
import xyz.northclient.features.values.BoolValue;
import xyz.northclient.features.values.DoubleValue;
import xyz.northclient.features.values.ModeValue;
import xyz.northclient.util.MoveUtil;

@ModuleInfo(name = "Sprint", description = "Omni Sprint",category = Category.MOVEMENT)
public class Sprint extends AbstractModule {
    @Override
    public void onEnable() {
        super.onEnable();
    }

    @EventLink
    public void onUpdate(TickEvent event) {
        if(MoveUtil.isMoving() && !NorthSingleton.INSTANCE.getModules().isEnabled("Scaffold")) {
            mc.thePlayer.setSprinting(true);
        }
    }
}
