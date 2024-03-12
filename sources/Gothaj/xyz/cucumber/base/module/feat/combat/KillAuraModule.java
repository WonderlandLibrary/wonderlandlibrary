package xyz.cucumber.base.module.feat.combat;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import de.florianmichael.viamcp.fixes.AttackOrder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.util.MathHelper;
import xyz.cucumber.base.Client;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.EventType;
import xyz.cucumber.base.events.ext.EventAttack;
import xyz.cucumber.base.events.ext.EventGameLoop;
import xyz.cucumber.base.events.ext.EventJump;
import xyz.cucumber.base.events.ext.EventLegitPlace;
import xyz.cucumber.base.events.ext.EventLook;
import xyz.cucumber.base.events.ext.EventMotion;
import xyz.cucumber.base.events.ext.EventMoveFlying;
import xyz.cucumber.base.events.ext.EventRender3D;
import xyz.cucumber.base.events.ext.EventRenderRotation;
import xyz.cucumber.base.events.ext.EventRotation;
import xyz.cucumber.base.events.ext.EventTick;
import xyz.cucumber.base.module.ArrayPriority;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.feat.other.TeamsModule;
import xyz.cucumber.base.module.feat.player.AutoArmorModule;
import xyz.cucumber.base.module.feat.player.AutoHealModule;
import xyz.cucumber.base.module.feat.player.InvManagerModule;
import xyz.cucumber.base.module.feat.player.ScaffoldModule;
import xyz.cucumber.base.module.settings.BooleanSettings;
import xyz.cucumber.base.module.settings.ModeSettings;
import xyz.cucumber.base.module.settings.NumberSettings;
import xyz.cucumber.base.utils.MovementUtils;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.Timer;
import xyz.cucumber.base.utils.game.EntityUtils;
import xyz.cucumber.base.utils.game.InventoryUtils;
import xyz.cucumber.base.utils.math.RotationUtils;

@ModuleInfo(category = Category.COMBAT, description = "Automatically attack targets around you", name = "Kill Aura", key = Keyboard.KEY_R, priority = ArrayPriority.HIGH)

public class KillAuraModule extends Mod {

	public EntityLivingBase target;

	private boolean canChangeRotation;

	private int attackTimes;

	public double yawSpeed, pitchSpeed, clickDelay, randomCPS, offsetX, offsetY, offsetZ;

	public Timer clickTimer = new Timer();
	public Timer cpsRandomizationTimer = new Timer();
	public Timer rotationRandomizationTimer = new Timer();

	public long lastTime;

	public int intaveBlockTicks;

	public boolean allowedToWork = false, blockingStatus = false, canSnapRotation;
	public ModeSettings Targets = new ModeSettings("Targets", new String[] { "Everything", "Players" });
	public ModeSettings autoBlock = new ModeSettings("Auto Block", new String[] { "Vanilla", "Legit", "Fake", "None" });
	public ModeSettings rotationMode = new ModeSettings("Rotations",
			new String[] { "Normal", "Legit", "Snap", "None" });
	public NumberSettings minYawSpeed = new NumberSettings("Min Yaw Speed", 50, 10, 180, 1);
	public NumberSettings maxYawSpeed = new NumberSettings("Max Yaw Speed", 50, 10, 180, 1);
	public NumberSettings minPitchSpeed = new NumberSettings("Min Pitch Speed", 50, 10, 180, 1);
	public NumberSettings maxPitchSpeed = new NumberSettings("Max Pitch Speed", 50, 10, 180, 1);

	public ModeSettings cpsMode = new ModeSettings("CPS Mode", new String[] { "Normal", "Advanced" });

	public NumberSettings minCPS = new NumberSettings("Min CPS", () -> cpsMode.getMode().equalsIgnoreCase("Normal"), 13, 1, 20, 1);
	public NumberSettings maxCPS = new NumberSettings("Max CPS", () -> cpsMode.getMode().equalsIgnoreCase("Normal"), 13, 1, 20, 1);
	public NumberSettings minReduceCPS = new NumberSettings("Min Reduce CPS", () -> cpsMode.getMode().equalsIgnoreCase("Advanced"), 20, 1, 40, 1);
	public NumberSettings maxReduceCPS = new NumberSettings("Max Reduce CPS", () -> cpsMode.getMode().equalsIgnoreCase("Advanced"), 20, 1, 40, 1);

	public NumberSettings minNormalCPS = new NumberSettings("Min Normal CPS", () -> cpsMode.getMode().equalsIgnoreCase("Advanced"), 20, 0, 40, 1);
	public NumberSettings maxNormalCPS = new NumberSettings("Max Normal CPS", () -> cpsMode.getMode().equalsIgnoreCase("Advanced"), 20, 0, 40, 1);

	public BooleanSettings forceHit = new BooleanSettings("Force Hit", () -> cpsMode.getMode().equalsIgnoreCase("Advanced"), true);

	public BooleanSettings smooth = new BooleanSettings("Smooth", false);
	public BooleanSettings randomize = new BooleanSettings("Randomize", false);
	public BooleanSettings newCombat = new BooleanSettings("New Combat", false);
	public BooleanSettings swingInRange = new BooleanSettings("Swing In Range", true);
	public BooleanSettings jitter = new BooleanSettings("Jitter", true);
	public ModeSettings MoveFix = new ModeSettings("Move Fix", new String[] { "Legit", "Silent", "Off" });
	public NumberSettings Range = new NumberSettings("Range", 4.0D, 3.0D, 8.0D, 0.01);
	public NumberSettings interactRange = new NumberSettings("Interact Range", 4.0D, 3.0D, 8.0D, 0.01);
	public BooleanSettings Switch = new BooleanSettings("Switch", true);
	public BooleanSettings raytrace = new BooleanSettings("Ray Trace", true);
	public NumberSettings switchDelay = new NumberSettings("Switch Delay", 100.0D, 10.0D, 1000.0D, 1);
	public BooleanSettings disableOnDeath = new BooleanSettings("Disable on Death", true);
	public BooleanSettings TroughWalls = new BooleanSettings("Trough Walls", false);
	public BooleanSettings closedInventory = new BooleanSettings("Closed Inventory", false);
	public BooleanSettings attackInvisible = new BooleanSettings("Attack Invisible", false);
	public BooleanSettings attackDead = new BooleanSettings("Attack Dead", false);

	public KillAuraModule() {
		this.addSettings(Targets);
		this.addSettings(autoBlock);
		this.addSettings(rotationMode);
		this.addSettings(minYawSpeed);
		this.addSettings(maxYawSpeed);
		this.addSettings(minPitchSpeed);
		this.addSettings(maxPitchSpeed);
		this.addSettings(smooth);
		this.addSettings(randomize);
		this.addSettings(newCombat);

		this.addSettings(cpsMode);
		this.addSettings(minCPS, maxCPS);
		this.addSettings(minReduceCPS, maxReduceCPS, minNormalCPS, maxNormalCPS);
		this.addSettings(forceHit);

		this.addSettings(swingInRange);
		this.addSettings(MoveFix);
		this.addSettings(Range);
		this.addSettings(interactRange);
		this.addSettings(Switch);
		this.addSettings(raytrace);
		this.addSettings(jitter);
		this.addSettings(switchDelay);
		this.addSettings(disableOnDeath);
		this.addSettings(closedInventory);
		this.addSettings(TroughWalls);
		this.addSettings(attackInvisible);
		this.addSettings(attackDead);
	}

	public void onDisable() {
		mc.timer.timerSpeed = 1f;
		target = null;
		unBlock();
		RotationUtils.customRots = false;
	}

	public void onEnable() {
		intaveBlockTicks = 0;
		target = null;
		RotationUtils.serverYaw = mc.thePlayer.rotationYaw;
		RotationUtils.serverPitch = mc.thePlayer.rotationPitch;
		allowedToWork = false;
		blockingStatus = false;
		canSnapRotation = false;

		if (canWork()) {
			calculateRots();
		}
		
		attackTimes = 0;
	}
	
	@EventListener
	public void onGameLoop(EventGameLoop e) {
		if (canWork()) {
			if (cpsMode.getMode().equalsIgnoreCase("Advanced")) {
				advancedClick();
			} else {
				normalClick();
			}
		}
	}

	@EventListener
	public void onTick(EventTick e) {
		setInfo(Switch.isEnabled() ? "Switch" : "Single");
		if ((mc.thePlayer.getHealth() <= 0 || mc.thePlayer.ticksExisted <= 5) && disableOnDeath.isEnabled()) {
			this.toggle();
			return;
		}
	}

	@EventListener
	public void onClick(EventLegitPlace e) {
		canChangeRotation = true;

		if (canWork()) {
			attackLoop();	
		}
		
		attackTimes = 0;
	}

	@EventListener
	public void onRotationRender(EventRenderRotation e) {
		if (allowedToWork && RotationUtils.customRots) {
			switch (rotationMode.getMode().toLowerCase()) {
			case "normal":
			case "legit":
				e.setYaw(RotationUtils.serverYaw);
				e.setPitch(RotationUtils.serverPitch);
				break;

			case "snap":
				if (canSnapRotation) {
					e.setYaw(RotationUtils.serverYaw);
					e.setPitch(RotationUtils.serverPitch);
				}

				break;
			}
		}
	}

	@EventListener
	public void onLook(EventLook e) {
		if (canChangeRotation || (minYawSpeed.getValue() == 180 && maxYawSpeed.getValue() == 180
				&& minPitchSpeed.getValue() == 180 && maxPitchSpeed.getValue() == 180)) {
			if (!canWork())
				return;
			calculateRots();

			canChangeRotation = false;
		}

		if (allowedToWork && RotationUtils.customRots) {
			EventLook event = (EventLook) e;

			switch (rotationMode.getMode().toLowerCase()) {
			case "normal":
			case "legit":
			case "snap":
				e.setYaw(RotationUtils.serverYaw);
				e.setPitch(RotationUtils.serverPitch);
				break;
			}
		}
	}

	@EventListener
	public void onJump(EventJump e) {
		if (allowedToWork && !MoveFix.getMode().equalsIgnoreCase("Off") && RotationUtils.customRots) {

			switch (rotationMode.getMode().toLowerCase()) {
			case "normal":
				e.setYaw(RotationUtils.serverYaw);
				break;

			case "legit":
				e.setYaw(RotationUtils.serverYaw);
				break;

			case "silent":
				e.setYaw(RotationUtils.serverYaw);
				break;

			case "snap":
				if (canSnapRotation) {
					e.setYaw(RotationUtils.serverYaw);
				}

				break;
			}
		}
	}

	@EventListener
	public void onMotion(EventMotion e) {
		if (allowedToWork && RotationUtils.customRots) {
			if (e.getType() == EventType.PRE) {
				switch (rotationMode.getMode().toLowerCase()) {
				case "normal":
					e.setYaw(RotationUtils.serverYaw);
					e.setPitch(RotationUtils.serverPitch);
					break;
				case "legit":
					e.setYaw(RotationUtils.serverYaw);
					e.setPitch(RotationUtils.serverPitch);
					break;

				case "snap":
					float rotationYaw = mc.thePlayer.rotationYaw;
					float rotationPitch = mc.thePlayer.rotationPitch;
					mc.thePlayer.rotationYaw = RotationUtils.serverYaw;
					mc.thePlayer.rotationPitch = RotationUtils.serverPitch;
					e.setYaw(mc.thePlayer.rotationYaw);
					e.setPitch(mc.thePlayer.rotationPitch);
					RotationUtils.serverYaw = rotationYaw;
					RotationUtils.serverPitch = rotationPitch;
					break;
				}
			} else {
				switch (rotationMode.getMode().toLowerCase()) {
				case "snap":
					mc.thePlayer.rotationYaw = RotationUtils.serverYaw;
					mc.thePlayer.rotationPitch = RotationUtils.serverPitch;
					break;
				}
				if (allowedToWork) {
					block(target, "Post");
				}
			}
		}
	}

	@EventListener
	public void onMove(EventMoveFlying e) {
		if (allowedToWork && RotationUtils.customRots) {

			switch (MoveFix.getMode().toLowerCase()) {
			case "legit":
				if (rotationMode.getMode().equalsIgnoreCase("Snap")) {
					if (canSnapRotation) {
						e.setYaw(RotationUtils.serverYaw);
					}
				} else {
					e.setYaw(RotationUtils.serverYaw);
				}

				break;

			case "silent":
				e.setCancelled(true);
				MovementUtils.silentMoveFix(e);
				break;
			}
		}
	}

	public void attackLoop() {

		if (Client.INSTANCE.getModuleManager().getModule(ScaffoldModule.class).isEnabled())
			return;

		block(target, "Before");

		if (mc.thePlayer.getDistanceToEntity(target) <= interactRange.value
				|| EntityUtils.getDistanceToEntityBox(target) <= interactRange.value) {
			if (!rotationMode.getMode().equalsIgnoreCase("None")) {
				if (attackTimes > 0) {
					if (mc.thePlayer.getDistanceToEntity(target) <= Range.value
							|| EntityUtils.getDistanceToEntityBox(target) <= Range.value) {
						canSnapRotation = false;
	
						block(target, "Before Attack");
	
						for (int i = 0; i < attackTimes; i++) {
							EventAttack event = new EventAttack(target);
							Client.INSTANCE.getEventBus().call(event);
	
							canSnapRotation = true;
	
							if (raytrace.isEnabled()) {
								Entity rayTracedEntity = RotationUtils.rayTrace(Range.getValue(),
										new float[] { RotationUtils.serverYaw, RotationUtils.serverPitch });
								if (rayTracedEntity != null) {
									AttackOrder.sendFixedAttackEvent(mc.thePlayer, rayTracedEntity);
								}
							} else {
								mc.thePlayer.swingItem();
								AttackOrder.sendFixedAttackEvent(mc.thePlayer, target);
							}
							event.setType(EventType.POST);
							Client.INSTANCE.getEventBus().call(event);
						}
	
						block(target, "After Attack");
	
					}else {
						if(swingInRange.isEnabled()) {
							for (int i = 0; i < attackTimes; i++) {
								mc.thePlayer.swingItem();
							}
						}
					}
					attackTimes = 0;
				}
			} else {
				if (attackTimes > 0) {
					if (mc.thePlayer.getDistanceToEntity(target) <= Range.value
							|| EntityUtils.getDistanceToEntityBox(target) <= Range.value) {
						block(target, "Before Attack");
	
						for (int i = 0; i < attackTimes; i++) {
							EventAttack event = new EventAttack(target);
							Client.INSTANCE.getEventBus().call(event);
	
							mc.thePlayer.swingItem();
							AttackOrder.sendFixedAttackEvent(mc.thePlayer, target);
	
							event.setType(EventType.POST);
							Client.INSTANCE.getEventBus().call(event);
						}
	
						block(target, "After Attack");
					}else {
						if(swingInRange.isEnabled()) {
							for (int i = 0; i < attackTimes; i++) {
								mc.thePlayer.swingItem();
							}
						}
					}

					attackTimes = 0;
				}
			}
		}
		block(target, "After");
	}

	public void normalClick() {
		if (clickTimer.hasTimeElapsed(calculateCPS(minCPS.getValue(), maxCPS.getValue()), false)) {
			clickTimer.reset();
			attackTimes++;
			return;
		}
	}

	public void advancedClick() {
		if (forceHit.isEnabled()) {
			Entity rayTracedEntity = RotationUtils.rayTrace(Range.getValue(),
					new float[] { RotationUtils.serverYaw, RotationUtils.serverPitch });

			int perfectHitHurtTime = 2;
			
			if (raytrace.isEnabled()) {
				if (rayTracedEntity != null) {
					if (rayTracedEntity instanceof EntityLivingBase) {
						EntityLivingBase entity = (EntityLivingBase) rayTracedEntity;
						if (entity.hurtTime <= perfectHitHurtTime) {
							attackTimes = 1;
							return;
						}
					}
				}
			} else {
				if (target != null) {
					if (target.hurtTime <= perfectHitHurtTime) {
						attackTimes = 1;
						return;
					}
				}
			}
		}

		if (mc.thePlayer.hurtTime == 0) {
			if (clickTimer.hasTimeElapsed(calculateCPS(minNormalCPS.getValue(), maxNormalCPS.getValue()), false)) {
				clickTimer.reset();
				attackTimes++;
				return;
			}
		} else {
			if (clickTimer.hasTimeElapsed(calculateCPS(minReduceCPS.getValue(), maxReduceCPS.getValue()), false)) {
				clickTimer.reset();
				attackTimes++;
				return;
			}
		}
	}

	public double calculateCPS(double min, double max) {
		double minValue = min;
		double maxValue = max;

		if (minValue == 0 && maxValue == 0) {
			return 100000;
		}

		if (minValue > maxValue)
			minValue = maxValue;
		if (maxValue < minValue)
			maxValue = minValue;

		if (cpsRandomizationTimer.hasTimeElapsed(100, true)) {
			randomCPS = minValue + (new Random().nextDouble() * (maxValue - minValue));
		}
		if (randomCPS < minValue) {
			randomCPS = minValue;
		}
		if (randomCPS > maxValue) {
			randomCPS = maxValue;
		}

		double delay = (1000 / randomCPS);
		delay -= 5;

		if (newCombat.isEnabled()) {
			delay = 4.0D;
			if (mc.thePlayer.getHeldItem() != null) {
				Item item = mc.thePlayer.getHeldItem().getItem();
				if (item instanceof net.minecraft.item.ItemSpade || item == Items.golden_axe
						|| item == Items.diamond_axe || item == Items.wooden_hoe || item == Items.golden_hoe)
					delay = 20.0D;
				if (item == Items.wooden_axe || item == Items.stone_axe)
					delay = 25.0D;
				if (item instanceof net.minecraft.item.ItemSword)
					delay = 12.0D;
				if (item instanceof net.minecraft.item.ItemPickaxe)
					delay = 17.0D;
				if (item == Items.iron_axe)
					delay = 22.0D;
				if (item == Items.stone_hoe)
					delay = 10.0D;
				if (item == Items.iron_hoe)
					delay = 7.0D;
			}
			delay *= 50;
		}

		clickDelay = delay;

		return clickDelay;
	}

	public void calculateRots() {

		if (Client.INSTANCE.getModuleManager().getModule(ScaffoldModule.class).isEnabled())
			return;

		double minYaw = minYawSpeed.getValue();
		double maxYaw = maxYawSpeed.getValue();
		double minPitch = minPitchSpeed.getValue();
		double maxPitch = maxPitchSpeed.getValue();

		if (minYaw > maxYaw)
			minYaw = maxYaw;
		if (maxYaw < minYaw)
			maxYaw = minYaw;
		if (minPitch > maxPitch)
			minPitch = maxPitch;
		if (maxPitch < minPitch)
			maxPitch = minPitch;

		if (rotationRandomizationTimer.hasTimeElapsed(100, true)) {
			yawSpeed = minYaw + (new Random().nextDouble() * (maxYaw - minYaw));
			pitchSpeed = minPitch + (new Random().nextDouble() * (maxPitch - minPitch));

			if (randomize.isEnabled()) {
				offsetX = ThreadLocalRandom.current().nextDouble(-0.14, 0.14);
				offsetY = ThreadLocalRandom.current().nextDouble(-0, 0.14);
				offsetZ = ThreadLocalRandom.current().nextDouble(-0.14, 0.14);
			} else {
				offsetX = 0;
				offsetY = 0;
				offsetZ = 0;
			}
		}

		if (yawSpeed < minYaw)
			yawSpeed = minYaw;
		if (yawSpeed > maxYaw)
			yawSpeed = maxYaw;
		if (pitchSpeed < minPitch)
			pitchSpeed = minPitch;
		if (pitchSpeed > maxPitch)
			pitchSpeed = maxPitch;

		switch (rotationMode.getMode().toLowerCase()) {
		case "normal": {
			float[] rots = RotationUtils.getRots(RotationUtils.serverYaw, RotationUtils.serverPitch,
					(EntityLivingBase) target, (float) yawSpeed, (float) pitchSpeed, offsetX, offsetY, offsetZ);
			rots[0] += jitter.isEnabled() ? ((Math.random() * 2) - (Math.random() * 2)) : 0;
			rots[1] += jitter.isEnabled() ? ((Math.random() * 2) - (Math.random() * 2)) : 0;

			rots = RotationUtils.getFixedRotation(rots,
					new float[] { RotationUtils.serverYaw, RotationUtils.serverPitch });

			if (smooth.isEnabled()) {
				rots[0] = RotationUtils.interpolateRotation(RotationUtils.serverYaw, rots[0],
						(float) (yawSpeed / RandomUtils.nextFloat(170.0f, 180.0f)));
				rots[1] = RotationUtils.interpolateRotation(RotationUtils.serverPitch, rots[1],
						(float) (pitchSpeed / RandomUtils.nextFloat(170.0f, 180.0f)));
			}

			RotationUtils.serverYaw = rots[0];
			RotationUtils.serverPitch = rots[1];
			RotationUtils.customRots = true;
		}
			break;

		case "snap": {
			if (canSnapRotation) {
				float[] rots = RotationUtils.getRots(RotationUtils.serverYaw, RotationUtils.serverPitch,
						(EntityLivingBase) target, (float) yawSpeed, (float) pitchSpeed, offsetX, offsetY, offsetZ);
				rots[0] += jitter.isEnabled() ? ((Math.random() * 2) - (Math.random() * 2)) : 0;
				rots[1] += jitter.isEnabled() ? ((Math.random() * 2) - (Math.random() * 2)) : 0;

				rots = RotationUtils.getFixedRotation(rots,
						new float[] { RotationUtils.serverYaw, RotationUtils.serverPitch });

				if (smooth.isEnabled()) {
					rots[0] = RotationUtils.interpolateRotation(RotationUtils.serverYaw, rots[0],
							(float) (yawSpeed / RandomUtils.nextFloat(170.0f, 180.0f)));
					rots[1] = RotationUtils.interpolateRotation(RotationUtils.serverPitch, rots[1],
							(float) (pitchSpeed / RandomUtils.nextFloat(170.0f, 180.0f)));
				}

				RotationUtils.serverYaw = rots[0];
				RotationUtils.serverPitch = rots[1];
				RotationUtils.customRots = true;
			}
		}
			break;
		case "legit": {
			float rots[] = new float[] { RotationUtils.serverYaw, RotationUtils.serverPitch };

			Entity rayTracedEntity = RotationUtils.rayTrace(Range.getValue() + 1,
					new float[] { RotationUtils.serverYaw, RotationUtils.serverPitch });
			if (rayTracedEntity == null) {
				rots = RotationUtils.getRots(RotationUtils.serverYaw, RotationUtils.serverPitch,
						(EntityLivingBase) target, (float) yawSpeed, (float) pitchSpeed, offsetX, offsetY, offsetZ);
			}

			rots[0] += jitter.isEnabled() ? ((Math.random() * 2) - (Math.random() * 2)) : 0;
			rots[1] += jitter.isEnabled() ? ((Math.random() * 2) - (Math.random() * 2)) : 0;

			rots = RotationUtils.getFixedRotation(rots,
					new float[] { RotationUtils.serverYaw, RotationUtils.serverPitch });

			if (smooth.isEnabled()) {
				rots[0] = RotationUtils.interpolateRotation(RotationUtils.serverYaw, rots[0],
						(float) (yawSpeed / RandomUtils.nextFloat(170.0f, 180.0f)));
				rots[1] = RotationUtils.interpolateRotation(RotationUtils.serverPitch, rots[1],
						(float) (pitchSpeed / RandomUtils.nextFloat(170.0f, 180.0f)));
			}

			RotationUtils.serverYaw = rots[0];
			RotationUtils.serverPitch = rots[1];

			RotationUtils.customRots = true;
		}
			break;
		}

		if (RotationUtils.serverPitch > 90) {
			RotationUtils.serverPitch = 90;
		}

		if (RotationUtils.serverPitch < -90) {
			RotationUtils.serverPitch = -90;
		}
	}

	public boolean canWork() {
		if (closedInventory.isEnabled()) {
			InvManagerModule invManager = (InvManagerModule) Client.INSTANCE.getModuleManager()
					.getModule(InvManagerModule.class);
			AutoArmorModule autoArmor = (AutoArmorModule) Client.INSTANCE.getModuleManager()
					.getModule(AutoArmorModule.class);
			AutoHealModule autoHeal = (AutoHealModule) Client.INSTANCE.getModuleManager()
					.getModule(AutoHealModule.class);

			if (mc.currentScreen != null) {
				if (allowedToWork) {
					RotationUtils.customRots = false;
					unBlock();
				}
				allowedToWork = false;
				return false;
			}

			if (invManager.mode.getMode().equalsIgnoreCase("Spoof")
					|| autoArmor.mode.getMode().equalsIgnoreCase("Spoof")
					|| autoHeal.mode.getMode().equalsIgnoreCase("Spoof")) {
				if (InventoryUtils.isInventoryOpen) {
					if (allowedToWork) {
						RotationUtils.customRots = false;
						unBlock();
					}
					allowedToWork = false;
					return false;
				}
			}
		}

		EntityLivingBase lastTarget = target;
		target = EntityUtils.getTarget(Math.max(Range.getValue(), interactRange.getValue()), Targets.getMode(),
				Switch.isEnabled() ? "Switch" : "Single", (int) switchDelay.getValue(),
				Client.INSTANCE.getModuleManager().getModule(TeamsModule.class).isEnabled(), TroughWalls.isEnabled(),
				attackInvisible.isEnabled(), attackDead.isEnabled());
		if (target == null) {
			target = EntityUtils.getTargetBox(Math.max(Range.getValue(), interactRange.getValue()), Targets.getMode(),
					Switch.isEnabled() ? "Switch" : "Single", (int) switchDelay.getValue(),
					Client.INSTANCE.getModuleManager().getModule(TeamsModule.class).isEnabled(),
					TroughWalls.isEnabled(), attackInvisible.isEnabled(), attackDead.isEnabled());
		}

		if (target == null) {
			if (allowedToWork) {
				RotationUtils.customRots = false;
				unBlock();
			}

			allowedToWork = false;
			return false;
		} else {
			if (!allowedToWork) {
				RotationUtils.serverYaw = mc.thePlayer.rotationYaw;
				RotationUtils.serverPitch = mc.thePlayer.rotationPitch;
			}
			allowedToWork = true;
			return true;
		}
	}

	public void unBlock() {
		mc.gameSettings.keyBindUseItem.pressed = false;
		blockingStatus = false;
	}

	public void block(Entity ent, String timing) {
		if (mc.thePlayer == null || mc.theWorld == null) {
			return;
		}

		if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() != null
				&& mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
			switch (autoBlock.getMode().toLowerCase()) {
			case "legit":
				if (timing.equalsIgnoreCase("Before")) {
					if (mc.thePlayer.hurtTime >= 6) {
						mc.gameSettings.keyBindUseItem.pressed = true;
						blockingStatus = true;
					} else if (mc.thePlayer.hurtTime > 0) {
						mc.gameSettings.keyBindUseItem.pressed = false;
						blockingStatus = false;
					}
				}
				break;

			case "vanilla":
				mc.gameSettings.keyBindUseItem.pressed = true;
				blockingStatus = true;
				break;

			case "fake":
				mc.thePlayer.getHeldItem().useItemRightClick(mc.theWorld, mc.thePlayer);
				blockingStatus = true;
				break;
			}
		}
	}
}