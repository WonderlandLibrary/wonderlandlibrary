package summer.cheat.cheats.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import summer.Summer;
import summer.base.manager.CheatManager;
import summer.base.manager.Selection;
import summer.cheat.cheats.combat.KillAura;
import summer.cheat.guiutil.Setting;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.player.EventSlowDown;
import summer.cheat.eventsystem.events.player.EventUpdate;
import summer.cheat.eventsystem.events.player.EventWater;
import summer.base.manager.config.Cheats;

public class NoSlowDown extends Cheats {
    private Setting cancel;
    private Setting forward;
    private Setting strafe;
    private Setting hypixel;
    public static Setting noWater;

    public NoSlowDown() {
        super("NoSlowDown", "Prevents you from slowing down", Selection.MOVEMENT, false);
    }

    @Override
    public void onSetup() {
        Summer.INSTANCE.settingsManager.Property(cancel = new Setting("Cancel", this, true));
        Summer.INSTANCE.settingsManager.Property(forward = new Setting("Forward", this, 1, 0.2, 1, false));
        Summer.INSTANCE.settingsManager.Property(strafe = new Setting("Strafe", this, 1, 0.2, 1, false));
        Summer.INSTANCE.settingsManager.Property(hypixel = new Setting("Hypixel", this, false));
        Summer.INSTANCE.settingsManager.Property(noWater = new Setting("NoWater", this, true));
    }

    @EventTarget
    public void onUpdate(EventUpdate eu) {
        KillAura ka = CheatManager.getInstance(KillAura.class);
        if (noWater.getValBoolean()) {
            Minecraft.thePlayer.inWater = false;
        }
    }

    @EventTarget
    public void onSlow(EventSlowDown esd) {
        if (!cancel.getValBoolean()) {
            esd.setForward(forward.getValFloat());
            esd.setStrafe(strafe.getValFloat());
        } else {
            esd.setCancelled(true);
        }
    }

    @EventTarget
    public void onWater(EventWater ew) {
        if (noWater.getValBoolean()) {
            ew.setCancelled(true);
            ew.setCanBePushed(false);
        }
    }

    @Override
    public void updateSettings() {
    }
}
