package tech.atani.client.feature.module.impl.player;

import tech.atani.client.listener.event.minecraft.player.movement.UpdateMotionEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.value.impl.CheckBoxValue;
import tech.atani.client.utility.interfaces.Methods;

@ModuleData(name = "AutoRespawn", description = "Automatically respawn after dying.", category = Category.PLAYER)
public class AutoRespawn extends Module {
    private final CheckBoxValue instant = new CheckBoxValue("Instant", "Do you want to respawn faster?", this, true);

    @Listen
    public void onMotionEvent(UpdateMotionEvent event) {
        if(Methods.mc.thePlayer == null || Methods.mc.theWorld == null)
            return;

        if(event.getType() == UpdateMotionEvent.Type.MID) {
            if (this.isDead())
                mc.thePlayer.respawnPlayer();
        }
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    private boolean isDead() {
        return instant.getValue() ? mc.thePlayer.getHealth() <= 0 : mc.thePlayer.isDead;
    }

}