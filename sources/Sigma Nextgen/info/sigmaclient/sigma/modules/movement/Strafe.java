package info.sigmaclient.sigma.modules.movement;

import info.sigmaclient.sigma.event.player.MoveEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.utils.player.MovementUtils;
import net.minecraft.util.math.MathHelper;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class Strafe extends Module {
    public Strafe() {
        super("Strafe", Category.Movement,"Strafe in air");
    }

    @Override
    public void onMoveEvent(MoveEvent event) {
        MovementUtils.strafing(event, MathHelper.sqrt(event.getX() * event.getX() + event.getZ() * event.getZ()));
        super.onMoveEvent(event);
    }
}
