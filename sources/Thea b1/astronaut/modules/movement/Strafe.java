package astronaut.modules.movement;

import astronaut.events.EventUpdate;
import astronaut.modules.Category;
import astronaut.modules.Module;
import astronaut.utils.PlayerUtil;
import eventapi.EventTarget;
import net.minecraft.util.MathHelper;

import java.awt.*;

public class Strafe extends Module {

    public Strafe() {
        super("Strafe", Type.Movement, 0, Category.MOVEMENT, Color.orange, "Lets you strafe");
    }
    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (mc.thePlayer.hurtTime > 0)
            return;

        PlayerUtil.strafe(PlayerUtil.getHorizontalMotion());

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {

    }
}
