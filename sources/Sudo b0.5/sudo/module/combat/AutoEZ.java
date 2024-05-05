package sudo.module.combat;

import sudo.module.Mod;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class AutoEZ extends Mod {

	
	public AutoEZ() {
		super("AutoEZ", "Automatically sends EZ in chat", Category.COMBAT, 0);
		addSettings();
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}
	int cooldown = 50;
	static PlayerEntity target = null;
	@Override
	public void onTick() {
		HitResult hit = mc.crosshairTarget;

		if (mc.player != null) {
			if (hit != null && hit.getType() == HitResult.Type.ENTITY) {
			    if (((EntityHitResult) hit).getEntity() instanceof PlayerEntity player) {
			        target = player;
			        if (target.isDead()) {
			        	if (cooldown<0) mc.player.sendChatMessage("Just killed " + target.getName().getString() + ", EZ");
			        	cooldown = 50;
			        	target=null;
			        }
			    }
			} else if (target == null) return;
		}
		cooldown--;
		super.onTick();
	}
}
