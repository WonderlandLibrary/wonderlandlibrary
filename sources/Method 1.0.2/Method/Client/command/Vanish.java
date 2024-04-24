package Method.Client.command;

import Method.Client.utils.visual.ChatUtils;
import net.minecraft.entity.Entity;

@Command.CommandFeatures(description = "Vanish in a entity", Syntax = "Vanish")

public class Vanish extends Command {

    private static Entity vehicle;

    @Override
    public void runCommand(String s, String[] args) {
        if (mc.player.getRidingEntity() != null && vehicle == null) {
            vehicle = mc.player.getRidingEntity();
            mc.player.dismountRidingEntity();
            mc.world.removeEntityFromWorld(vehicle.getEntityId());
            ChatUtils.message("Vehicle " + vehicle.getName() + " removed.");
        } else if (vehicle != null) {
            vehicle.isDead = false;
            mc.world.addEntityToWorld(vehicle.getEntityId(), vehicle);
            mc.player.startRiding(vehicle, true);
            ChatUtils.message("Vehicle " + vehicle.getName() + " created.");
            vehicle = null;
        } else {
            ChatUtils.message("No Vehicle.");
        }

    }

}