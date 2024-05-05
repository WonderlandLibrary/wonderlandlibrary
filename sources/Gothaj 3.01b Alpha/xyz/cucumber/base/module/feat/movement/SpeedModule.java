package xyz.cucumber.base.module.feat.movement;

import java.util.LinkedList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.potion.Potion;
import xyz.cucumber.base.Client;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.EventType;
import xyz.cucumber.base.events.ext.EventMotion;
import xyz.cucumber.base.events.ext.EventMoveButton;
import xyz.cucumber.base.events.ext.EventReceivePacket;
import xyz.cucumber.base.events.ext.EventSendPacket;
import xyz.cucumber.base.events.ext.EventUpdate;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.feat.combat.KillAuraModule;
import xyz.cucumber.base.module.settings.BooleanSettings;
import xyz.cucumber.base.module.settings.ColorSettings;
import xyz.cucumber.base.module.settings.ModeSettings;
import xyz.cucumber.base.module.settings.NumberSettings;
import xyz.cucumber.base.module.settings.StringSettings;
import xyz.cucumber.base.utils.MovementUtils;
import xyz.cucumber.base.utils.Timer;
import xyz.cucumber.base.utils.math.RotationUtils;
import xyz.cucumber.base.utils.math.Vector3d;



@ModuleInfo(category = Category.MOVEMENT, description = "Allows you to walk faster", name = "Speed", key = 0)
public class SpeedModule extends Mod {

	public KillAuraModule ka;

	public int groundTicks, velocityTicks, ticks, transTicks;

	public boolean forward, backward, left, right, shouldStrafe;

	public double hypixelY;

	public boolean started, done;

	public Timer timer = new Timer();

	public LinkedList<Packet> outPackets = new LinkedList<>();
	public LinkedList<Packet> inPackets = new LinkedList<>();

	public ModeSettings mode = new ModeSettings("Mode",
			new String[] { "Vanilla", "Vulcan","Vulcan-Test", "Verus", "NCP", "Hypixel", "Intave", "Matrix", "Legit"});
	public NumberSettings speed = new NumberSettings("Speed", 0.5, 0.1, 3, 0.01);	
	public NumberSettings intaveStrafeTicks = new NumberSettings("Intave Strafe Ticks", 3, 1, 15, 1);
	public NumberSettings intaveOffset = new NumberSettings("Intave Offset", 0.02, 0, 0.25, 0.01);
	public NumberSettings intaveMinSpeed = new NumberSettings("Intave Min Speed", 0.02, 0, 0.25, 0.01);
	public NumberSettings timerTimer = new NumberSettings("Timer", 1, 1, 2f, 0.001);
	public BooleanSettings intaveSmart = new BooleanSettings("Intave Smart", true);
	public BooleanSettings intaveSmartSafe = new BooleanSettings("Intave Smart Safe", true);
	public NumberSettings intaveSmartDelay = new NumberSettings("Intave Smart Delay", 2, 0, 15, 1);	
	public BooleanSettings vulcanFast = new BooleanSettings("Vulcan Fast", true);
	public BooleanSettings vulcanStrafe = new BooleanSettings("Vulcan Strafe", true);

	public SpeedModule() {
		this.addSettings(mode, speed, intaveStrafeTicks, intaveOffset, intaveMinSpeed, timerTimer, intaveSmart,
				intaveSmartSafe, intaveSmartDelay, vulcanFast, vulcanStrafe);
	}

	public void onDisable() {
		mc.thePlayer.speedInAir = 0.02f;
		mc.timer.timerSpeed = 1f;

		for (Packet p : outPackets) {
			if (p != null) {
				mc.getNetHandler().getNetworkManager().sendPacketNoEvent(p);
			}
		}

		outPackets.clear();

		for (Packet p : inPackets) {
			if (p != null) {
				p.processPacket(mc.getNetHandler().getNetworkManager().getNetHandler());
			}
		}

		inPackets.clear();
	}

	public void onEnable() {
		switch (mode.getMode().toLowerCase()) {
		case "intave":
			started = false;
			break;
		}
		timer.reset();
		ka = (KillAuraModule) Client.INSTANCE.getModuleManager().getModule(KillAuraModule.class);
	}

	@EventListener
	public void onMotion(EventMotion e) {
		setInfo(mode.getMode());
		
		switch (mode.getMode().toLowerCase()) {
		case "vanilla":
			if (e.getType() == EventType.PRE) {
				mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;

				if (MovementUtils.isMoving()) {
					MovementUtils.strafe((float) speed.getValue());
				}
				if (mc.thePlayer.onGround && MovementUtils.isMoving()) {
					mc.thePlayer.jump();
				}
			}
			break;
		case "legit":
			if (e.getType() == EventType.PRE) {				
				if (mc.thePlayer.onGround && MovementUtils.isMoving()) {
					mc.thePlayer.jump();
                   }
			}
					break;
					
		case "intave":
			if (e.getType() == EventType.POST) {
				if (MovementUtils.isMoving()) {

					float[] inputs = MovementUtils.silentStrafe(mc.thePlayer.movementInput.moveStrafe,
							mc.thePlayer.movementInput.moveForward, RotationUtils.serverYaw, true);

					if (!RotationUtils.customRots) {
						inputs[1] = mc.thePlayer.movementInput.moveForward;
						inputs[0] = mc.thePlayer.movementInput.moveStrafe;
					}

					if (!ka.MoveFix.getMode().equalsIgnoreCase("Silent")) {
						inputs[1] = mc.thePlayer.movementInput.moveForward;
						inputs[0] = mc.thePlayer.movementInput.moveStrafe;
					}

					mc.timer.timerSpeed = (float) timerTimer.getValue();

					float intaveSpeed = (float) Math.max(intaveMinSpeed.getValue(), MovementUtils.getSpeed());

					if (!mc.thePlayer.isSprinting()) {
						// intaveSpeed *= 1.1;
					}

					if (mc.thePlayer.onGround || mc.thePlayer.ticksExisted % intaveStrafeTicks.getValue() == 0) {
						if (mc.thePlayer.onGround) {

						} else {
							if ((velocityTicks >= 10 || mc.thePlayer.hurtTime == 0)
									&& !mc.thePlayer.isPotionActive(Potion.moveSpeed)
									&& ticks > intaveStrafeTicks.getValue()) {
								if (MovementUtils.getSpeed() <= MovementUtils.getBaseMoveSpeed()
										/ (1 + intaveOffset.getValue())) {
									MovementUtils.strafe(intaveSpeed, RotationUtils.customRots ? RotationUtils.serverYaw
											: mc.thePlayer.rotationYaw, inputs[1], inputs[0]);
									ticks = 0;
									timer.reset();
									shouldStrafe = false;
								}
							}
						}
					}

					ticks++;

					if ((velocityTicks >= 10 || mc.thePlayer.hurtTime == 0)
							&& !mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
						if (shouldStrafe && timer.hasTimeElapsed(intaveSmartDelay.getValue() * 50, false)) {
							if (!intaveSmartSafe.isEnabled()) {
								MovementUtils.strafe(intaveSpeed,
										RotationUtils.customRots ? RotationUtils.serverYaw : mc.thePlayer.rotationYaw,
										inputs[1], inputs[0]);
								timer.reset();
								ticks = 0;
								shouldStrafe = false;
							} else {
								if (MovementUtils.getSpeed() <= MovementUtils.getBaseMoveSpeed()
										/ (1 + intaveOffset.getValue())) {
									MovementUtils.strafe(intaveSpeed, RotationUtils.customRots ? RotationUtils.serverYaw
											: mc.thePlayer.rotationYaw, inputs[1], inputs[0]);
									timer.reset();
									ticks = 0;
									shouldStrafe = false;
								}
							}
						}
					}
				} else {
					mc.timer.timerSpeed = 1f;
				}
			}
			break;
		case "verus":
			if (MovementUtils.isMoving()) {
				if (mc.thePlayer.onGround) {
					mc.thePlayer.jump();
				}

				MovementUtils.strafe((float) (mc.gameSettings.keyBindForward.pressed ? MovementUtils.getBaseMoveSpeed() * 1.3 : MovementUtils.getBaseMoveSpeed()));
			} else {
				mc.thePlayer.motionX = 0;
				mc.thePlayer.motionZ = 0;
			}
			break;
		case "vulcan":
			if (e.getType() == EventType.PRE) {
				if (mc.thePlayer.onGround) {
					groundTicks = 0;
				} else {
					groundTicks++;
				}

				float yaw = RotationUtils.customRots
						? ka.MoveFix.getMode().equalsIgnoreCase("Legit") ? RotationUtils.serverYaw
								: mc.thePlayer.rotationYaw
						: mc.thePlayer.rotationYaw;
				float speedModifier = vulcanFast.isEnabled() ? 0f : 0.05f;

				if (MovementUtils.isMoving()) {
					switch (groundTicks) {
					case 0:
						if (!mc.gameSettings.keyBindJump.pressed)
							mc.thePlayer.jump();
						if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
							MovementUtils.strafe(0.6f - speedModifier, (float) MovementUtils.getDirection(yaw));
						} else {
							MovementUtils.strafe(0.485f - speedModifier, (float) MovementUtils.getDirection(yaw));
						}
						break;

					case 9:
						MovementUtils.strafe((float) MovementUtils.getSpeed(), (float) MovementUtils.getDirection(yaw));
						break;

					case 2:
					case 1:
						MovementUtils.strafe((float) MovementUtils.getSpeed(), (float) MovementUtils.getDirection(yaw));
						break;

					case 5:
						mc.thePlayer.motionY = MovementUtils.getPredictedMotionY(mc.thePlayer.motionY);
						mc.thePlayer.motionY = MovementUtils.getPredictedMotionY(mc.thePlayer.motionY);
						break;

					default:
						if (vulcanStrafe.isEnabled())
							MovementUtils.strafe((float) Math.max(MovementUtils.getSpeed(), 0.22f),
									(float) MovementUtils.getDirection(yaw));
						break;
					}
				}
			}
			break;
		case "vulcan-test":
			if (e.getType() == EventType.PRE) {
				if (mc.thePlayer.onGround) {
					groundTicks = 0;
				} else {
					groundTicks++;
				}
				float yaw = RotationUtils.customRots
						? ka.MoveFix.getMode().equalsIgnoreCase("Legit") ? RotationUtils.serverYaw
								: mc.thePlayer.rotationYaw
						: mc.thePlayer.rotationYaw;
				if (MovementUtils.isMoving()) {
					if(mc.thePlayer.onGround) {
						mc.timer.timerSpeed = 0.7f;
						MovementUtils.strafe(0);
						mc.thePlayer.jump();
						MovementUtils.strafe(0.485f , (float) MovementUtils.getDirection(yaw));
					}else {
						
						switch (groundTicks) {
						case 3:
							mc.timer.timerSpeed =3f;
							break;
						case 5:
							mc.timer.timerSpeed = 1f;
							break;
						case 9:
							break;

						default:
							break;
						}
					}
				}else mc.timer.timerSpeed = 1f;
			}
			break;
		case "ncp":
			if (e.getType() == EventType.PRE) {
				if (MovementUtils.isMoving()) {
					if (mc.thePlayer.onGround) {
						mc.thePlayer.jump();
					}

					MovementUtils
							.strafe((float) Math.max(MovementUtils.getBaseMoveSpeed() / 1.1, MovementUtils.getSpeed()));
				} else {
					mc.thePlayer.motionX = 0;
					mc.thePlayer.motionZ = 0;
				}
			}
			break;
		case "hypixel":
			if (e.getType() == EventType.PRE) {
				if (MovementUtils.isMoving()) {
					if (mc.thePlayer.onGround) {
						MovementUtils.strafe((float) Math.max(mc.thePlayer.isSprinting() ? MovementUtils.getBaseMoveSpeed() : MovementUtils.getBaseMoveSpeed() * 1.25, MovementUtils.getSpeed()));
						mc.thePlayer.jump();
					}
				}
			}
			break;
		case "matrix":
			if (e.getType() == EventType.PRE) {
				float speed = (float) Math.max(MovementUtils.getBaseMoveSpeed() / 1.25, MovementUtils.getSpeed());
				if (mc.thePlayer.isSprinting()) {
					speed = (float) MovementUtils.getSpeed();
				}

				if (MovementUtils.isMoving()) {
					if (mc.thePlayer.onGround) {
						MovementUtils.strafe(speed);
						mc.thePlayer.jump();

						shouldStrafe = true;
					}

					if (mc.thePlayer.fallDistance > 0 && shouldStrafe) {
						shouldStrafe = false;
						MovementUtils.strafe(speed);
					}
				}
			}
			break;
		}
	}

	@EventListener
	public void onMoveButton(EventMoveButton e) {
		switch (mode.getMode().toLowerCase()) {
		case "vanilla":
			break;
		case "intave":
			if (MovementUtils.isMoving()) {
				e.jump = true;

				if (intaveSmart.isEnabled()) {
					if ((forward && !e.forward) || (!forward && e.forward)) {
						shouldStrafe = true;
					}
					if ((backward && !e.backward) || (!forward && e.backward)) {
						shouldStrafe = true;
					}
					if ((left && !e.left) || (!forward && e.left)) {
						shouldStrafe = true;
					}
					if ((right && !e.right) || (!forward && e.right)) {
						shouldStrafe = true;
					}
				}
				forward = e.forward;
				backward = e.backward;
				left = e.left;
				right = e.right;
			}
			break;
		case "verus":
			break;
		case "vulcan":
			break;
		case "ncp":
			break;
		case "hypixel":
			break;
		case "matrix":
			break;
		}
	}
}