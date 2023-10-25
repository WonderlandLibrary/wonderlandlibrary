package wtf.evolution.module.impl.Movement;

import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventMotion;
import wtf.evolution.helpers.ChatUtil;
import wtf.evolution.helpers.MovementUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;

@ModuleInfo(name = "Strafe", type = Category.Movement)
public class Strafe extends Module {

    public double boost;
    @EventTarget
    public void onUpdate(EventMotion e) {
        if (!mc.player.onGround && MovementUtil.getPlayerMotion() <= 0.22f) {
            MovementUtil.setSpeed(mc.player.isSneaking() ? (float) MovementUtil.getPlayerMotion() : 0.22f);
        }

        if (!mc.player.onGround && mc.player.motionY == -0.4448259643949201) {
            MovementUtil.setSpeed((float) MovementUtil.getPlayerMotion());
        }



    }


}
