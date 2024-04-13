package dev.tenacity.hackerdetector.checks;

import dev.tenacity.hackerdetector.Category;
import dev.tenacity.hackerdetector.Detection;
import dev.tenacity.hackerdetector.utils.MovementUtils;
import dev.tenacity.util.player.MovementUtil;
import net.minecraft.entity.player.EntityPlayer;

public class FlightB extends Detection {

    public FlightB() {
        super("Flight B", Category.MOVEMENT);
    }

    @Override
    public boolean runCheck(EntityPlayer player) {
        return player.getAir() > 20 && player.motionY == 0 && MovementUtil.isMoving();
    }
}
