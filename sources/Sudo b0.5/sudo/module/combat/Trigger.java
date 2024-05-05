package sudo.module.combat;

import java.util.Objects;

import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import sudo.module.Mod;

public class Trigger extends Mod{
	
	public Trigger() {
		super("Trigger", "Automaticly attack entities when looking at them", Category.COMBAT, 0);
	}
	
	
	@Override
	public void onTick() {
		if (!(mc.crosshairTarget instanceof EntityHitResult) || Objects.requireNonNull(mc.player)
            	.getAttackCooldownProgress(0) < 1) {
    		return;
    	}
   		if (mc.player.getAttackCooldownProgress(0.5F) == 1) {
    		Objects.requireNonNull(mc.interactionManager)
        	.attackEntity(mc.player, ((EntityHitResult) mc.crosshairTarget).getEntity());
    		mc.player.swingHand(Hand.MAIN_HAND);
    	}
	}
}
