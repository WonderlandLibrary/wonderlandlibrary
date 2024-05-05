package astronaut.modules.player;

import astronaut.events.EventUpdate;
import astronaut.modules.Category;
import astronaut.modules.Module;
import eventapi.EventTarget;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.CombatTracker;

import java.awt.*;

public class FastPlace extends Module {
    private CombatTracker time1;

    public FastPlace() {
        super("Fast Place", Type.Player, 0, Category.PLAYER, Color.green, "Removes the delay for placing blocks");
    }

    @Override
    public void onDisable() {
        mc.rightClickDelayTimer = 6;
    }

    @Override
    public void onEnable() {
        mc.rightClickDelayTimer = 0;
    }
}
