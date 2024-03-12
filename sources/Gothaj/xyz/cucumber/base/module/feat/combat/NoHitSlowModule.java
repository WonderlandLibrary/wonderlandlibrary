package xyz.cucumber.base.module.feat.combat;

import org.lwjgl.input.Keyboard;

import xyz.cucumber.base.Client;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.ext.EventHit;
import xyz.cucumber.base.events.ext.EventKnockBack;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.settings.BooleanSettings;

@ModuleInfo(category = Category.COMBAT, description = "Allows you to remove hit slowdown", name = "No Hit Slow", key = Keyboard.KEY_NONE)

public class NoHitSlowModule extends Mod {

	public BooleanSettings onlyHurtTime = new BooleanSettings("Only Hurt Time", true);
	
	public NoHitSlowModule() {
		this.addSettings(onlyHurtTime);
	}

	@EventListener
	public void onHit(EventHit e) {
		KillAuraModule ka = (KillAuraModule) Client.INSTANCE.getModuleManager().getModule(KillAuraModule.class);
		VelocityModule velocity = (VelocityModule) Client.INSTANCE.getModuleManager().getModule(VelocityModule.class);

		if (mc.thePlayer.hurtTime <= 0 || ((velocity.mode.getMode().equalsIgnoreCase("Vanilla")
				|| velocity.mode.getMode().equalsIgnoreCase("Vulcan")) && velocity.horizontal.getValue() == 0
				&& velocity.vertical.getValue() == 0 && velocity.isEnabled())) {
			e.setCancelled(true);
		}

		if (onlyHurtTime.isEnabled()) {
			if (mc.thePlayer.hurtTime == 0) {
				e.setCancelled(false);
			}
		}
	}

	@EventListener
	public void onKnockBack(EventKnockBack e) {
		KillAuraModule ka = (KillAuraModule) Client.INSTANCE.getModuleManager().getModule(KillAuraModule.class);
		VelocityModule velocity = (VelocityModule) Client.INSTANCE.getModuleManager().getModule(VelocityModule.class);

		if (mc.thePlayer.hurtTime <= 0 || ((velocity.mode.getMode().equalsIgnoreCase("Vanilla")
				|| velocity.mode.getMode().equalsIgnoreCase("Vulcan")) && velocity.horizontal.getValue() == 0
				&& velocity.vertical.getValue() == 0 && velocity.isEnabled())) {
			e.setCancelled(true);
		}

		if (onlyHurtTime.isEnabled()) {
			if (mc.thePlayer.hurtTime == 0) {
				e.setCancelled(false);
			}
		}
	}
}
