package vestige.module.impl.combat;

import net.minecraft.entity.EntityLivingBase;
import vestige.Vestige;
import vestige.module.Category;
import vestige.module.Module;
import vestige.setting.impl.BooleanSetting;
import vestige.setting.impl.DoubleSetting;
import vestige.util.player.RotationsUtil;
import vestige.util.world.WorldUtil;

public class TargetStrafe extends Module {

    private final DoubleSetting maxRange = new DoubleSetting("Max range", 3, 1, 6, 0.1);
    public final BooleanSetting whilePressingSpace = new BooleanSetting("While pressing space", false);

    private boolean goingRight;

    private Killaura killaura;

    public TargetStrafe() {
        super("Target Strafe", Category.COMBAT);
        this.addSettings(maxRange, whilePressingSpace);
    }

    @Override
    public void onClientStarted() {
        killaura = Vestige.instance.getModuleManager().getModule(Killaura.class);
    }

    public float getDirection() {
        if(mc.thePlayer.isCollidedHorizontally || !WorldUtil.isBlockUnder(3)) {
            goingRight = !goingRight;
        }

        EntityLivingBase target = killaura.getTarget();

        double distance = killaura.getDistanceToEntity(target);

        float direction;

        if(distance > maxRange.getValue()) {
            direction = RotationsUtil.getRotationsToEntity(target, false)[0];
        } else {
            double offset = (90 - killaura.getDistanceToEntity(target) * 5);

            if(!goingRight) {
                offset = -offset;
            }

            direction = (float) (RotationsUtil.getRotationsToEntity(target, false)[0] + offset);
        }

        return (float) Math.toRadians(direction);
    }

}
