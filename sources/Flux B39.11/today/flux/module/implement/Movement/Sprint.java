package today.flux.module.implement.Movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.potion.Potion;
import today.flux.event.PreUpdateEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.ModuleManager;
import today.flux.module.value.BooleanValue;
import today.flux.utility.PlayerUtils;

public class Sprint extends Module {
    public static BooleanValue MultiDirection = new BooleanValue("Sprint", "MultiDir", true);
    public static BooleanValue Sneak = new BooleanValue("Sprint", "Sneak", true);
    public static BooleanValue Hunger = new BooleanValue("Sprint", "Hunger", true);
    public static BooleanValue Blindness = new BooleanValue("Sprint", "Blindness", true);
    public static BooleanValue UsingItem = new BooleanValue("Sprint", "Using Item", true);
    public static BooleanValue AtWall = new BooleanValue("Sprint", "At Wall", true);

    public Sprint() {
        super("Sprint", Category.Movement, false);
    }

    @EventTarget
    public void onUpdate(PreUpdateEvent event) {
        if (!PlayerUtils.isMoving2() ||
                (mc.thePlayer.moveForward < 0.8F && !MultiDirection.getValue()) ||
                (mc.thePlayer.isSneaking() && !Sneak.getValue()) ||
                (mc.thePlayer.isUsingItem() && !UsingItem.getValue()) ||
                (mc.thePlayer.isCollidedHorizontally && !AtWall.getValue()) ||
                (mc.thePlayer.isPotionActive(Potion.blindness) && !Blindness.getValue()) ||
                (!(mc.thePlayer.getFoodStats().getFoodLevel() > 6.0F) && !Hunger.getValue())) {
            mc.thePlayer.setSprinting(false);
            return;
        }

        if (ModuleManager.scaffoldMod.isEnabled() && (ModuleManager.scaffoldMod.getMode().getValue().equals("AAC") || (ModuleManager.scaffoldMod.getMode().getValue().equals("Hypixel")))) {
            mc.thePlayer.setSprinting(false);
            return;
        }

        if ((mc.thePlayer.movementInput.moveForward != 0 || mc.thePlayer.movementInput.moveStrafe != 0)) {
            mc.thePlayer.setSprinting(true);
        }
    }
}
