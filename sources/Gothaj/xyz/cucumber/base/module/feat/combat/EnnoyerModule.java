package xyz.cucumber.base.module.feat.combat;

import org.lwjgl.input.Keyboard;

import net.minecraft.entity.EntityLivingBase;
import xyz.cucumber.base.Client;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.EventType;
import xyz.cucumber.base.events.ext.EventJump;
import xyz.cucumber.base.events.ext.EventLook;
import xyz.cucumber.base.events.ext.EventMotion;
import xyz.cucumber.base.events.ext.EventMoveFlying;
import xyz.cucumber.base.events.ext.EventRenderRotation;
import xyz.cucumber.base.events.ext.EventRotation;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.feat.other.TeamsModule;
import xyz.cucumber.base.module.settings.NumberSettings;
import xyz.cucumber.base.utils.Timer;
import xyz.cucumber.base.utils.game.EntityUtils;
import xyz.cucumber.base.utils.game.InventoryUtils;
import xyz.cucumber.base.utils.math.RotationUtils;

@ModuleInfo(category = Category.COMBAT, description = "Automatically blocks player with blocks", name = "Ennoyer", key = Keyboard.KEY_NONE)
public class EnnoyerModule extends Mod {
	public EntityLivingBase target;

	public boolean cancel;

	public KillAuraModule ka;

	public Timer timer = new Timer();

	public NumberSettings delay = new NumberSettings("Delay", 1000, 500, 5000, 250);

	public EnnoyerModule() {
		this.addSettings(delay);
	}

	public void onEnable() {
		ka = (KillAuraModule) Client.INSTANCE.getModuleManager().getModule(KillAuraModule.class);
	}

	public void onDisable() {
		RotationUtils.customRots = false;
	}

	@EventListener
	public void onRotation(EventRotation e) {
		int newSlot = InventoryUtils.getBlockSlot();
		if (newSlot == -1) {
			cancel = false;
			return;
		}

		target = EntityUtils.getTarget(5, "Players", "Single", (int) 400, Client.INSTANCE.getModuleManager().getModule(TeamsModule.class).isEnabled(),
				ka.TroughWalls.isEnabled(), ka.attackInvisible.isEnabled(), ka.attackDead.isEnabled());

		if (target == null) {
			cancel = false;
			return;
		}

		if (EntityUtils.getDistanceToEntityBox(target) <= 3) {
			target = null;
			cancel = false;
			return;
		}

		if (!ka.isEnabled()) {
			target = null;
			cancel = false;
			return;
		}

		if (!timer.hasTimeElapsed(delay.getValue(), false)) {
			if (cancel) {
				cancel = false;
				RotationUtils.customRots = false;
			}
			return;
		}

		float rots[] = RotationUtils.getRotations(target.posX, target.posY - 1.6, target.posZ);

		RotationUtils.customRots = true;
		RotationUtils.serverYaw = rots[0];
		RotationUtils.serverPitch = rots[1];

		mc.thePlayer.inventory.currentItem = newSlot;

		cancel = true;
	}

	@EventListener
	public void onMotion(EventMotion e) {
		if (cancel) {
			if (e.getType() == EventType.PRE) {
				e.setYaw(RotationUtils.serverYaw);
				e.setPitch(RotationUtils.serverPitch);
			} else {
				if (mc.thePlayer.inventory.currentItem == InventoryUtils.getBlockSlot())
					mc.rightClickMouse();
				timer.reset();
			}
		}
	}

	@EventListener
	public void onMove(EventMoveFlying e) {
		if (cancel) {
			e.setYaw(RotationUtils.serverYaw);
		}
	}

	@EventListener
	public void onJump(EventJump e) {
		if (cancel) {
			e.setYaw(RotationUtils.serverYaw);
		}
	}

	@EventListener
	public void onLook(EventLook e) {
		if (cancel) {
			e.setYaw(RotationUtils.serverYaw);
			e.setPitch(RotationUtils.serverPitch);
		}
	}

	@EventListener
	public void onRotationRender(EventRenderRotation e) {
		if (cancel) {
			e.setYaw(RotationUtils.serverYaw);
			e.setPitch(RotationUtils.serverPitch);
		}
	}
}
