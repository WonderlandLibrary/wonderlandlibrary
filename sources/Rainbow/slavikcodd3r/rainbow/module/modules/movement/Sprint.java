package slavikcodd3r.rainbow.module.modules.movement;

import net.minecraft.client.Minecraft;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Module.Mod(displayName = "Sprint")
public class Sprint extends Module
{
    public Sprint() {
        super();
    }
    
    @Override
    public void disable() {
        if (ClientUtils.mc().thePlayer != null && ClientUtils.mc().theWorld != null) {
            ClientUtils.mc().thePlayer.setSprinting(false);
            super.disable();
        }
    }
    
    @EventTarget
    private void onUpdate(final UpdateEvent event) {
        if (ClientUtils.mc().thePlayer.isMoving()) {
            ClientUtils.mc().thePlayer.setSprinting(ClientUtils.mc().thePlayer.getFoodStats().getFoodLevel() > 6 && !ClientUtils.mc().thePlayer.isSneaking() && ClientUtils.mc().gameSettings.keyBindForward.getIsKeyPressed());
        }
    }
}
