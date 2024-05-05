package club.dortware.client.module.impl.movement;

import club.dortware.client.Client;
import club.dortware.client.manager.impl.PropertyManager;
import club.dortware.client.module.annotations.ModuleData;
import club.dortware.client.module.enums.ModuleCategory;
import club.dortware.client.property.impl.BooleanProperty;
import club.dortware.client.event.impl.UpdateEvent;
import club.dortware.client.module.Module;
import com.google.common.eventbus.Subscribe;

/**
 * @author Auth
 */

@ModuleData(name = "Sprint", description = "Makes you automatically sprint", category = ModuleCategory.MOVEMENT, defaultKeyBind = 0)
public class Sprint extends Module {

    @Override
    public void setup() {
        PropertyManager propertyManager = Client.INSTANCE.getPropertyManager();
        propertyManager.add(new BooleanProperty<>("Omnidirectional", this, false));
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        mc.thePlayer.setSprinting((Boolean) Client.INSTANCE.getPropertyManager().getProperty(this, "Omnidirectional").getValue() ? mc.thePlayer.isMoving() : mc.gameSettings.keyBindForward.getIsKeyPressed() && !mc.thePlayer.isCollidedHorizontally);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.thePlayer.setSprinting(mc.gameSettings.keyBindSprint.getIsKeyPressed());
    }
}
