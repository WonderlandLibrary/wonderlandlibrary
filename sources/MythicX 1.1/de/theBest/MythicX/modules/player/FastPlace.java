package de.theBest.MythicX.modules.player;

import de.theBest.MythicX.modules.Category;
import de.theBest.MythicX.modules.Module;
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
