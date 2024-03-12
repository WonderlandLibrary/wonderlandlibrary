package dev.tenacity.hackerdetector.checks;

import dev.tenacity.hackerdetector.Category;
import dev.tenacity.hackerdetector.Detection;
import dev.tenacity.hackerdetector.utils.MovementUtils;
import net.minecraft.entity.player.EntityPlayer;

public class FlightA extends Detection {

    public FlightA() {
        super("Flight A", Category.MOVEMENT);
    }

    @Override
    public boolean runCheck(EntityPlayer player) {
        return !player.onGround && player.motionY == 0 && MovementUtils.isMoving(player);
    }
}
