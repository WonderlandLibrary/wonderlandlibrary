package astronaut.modules.combat;

import astronaut.events.EventUpdate;
import astronaut.modules.Category;
import astronaut.modules.Module;
import eventapi.EventTarget;
import net.minecraft.network.play.client.C02PacketUseEntity;

import java.awt.*;

public class NoClickDelay extends Module {

    public NoClickDelay() {
        super("No Click Delay", Type.Combat, 0, Category.COMBAT, Color.green, "Removes the clicking delay when missing an attack");
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.gameSettings.keyBindAttack.isKeyDown())
            mc.thePlayer.swingItem();
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {

    }
}
