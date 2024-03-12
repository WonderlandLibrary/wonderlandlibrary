package xyz.cucumber.base.module.feat.combat;

import java.io.IOException;
import java.util.LinkedList;

import org.lwjgl.input.Keyboard;

import de.florianmichael.viamcp.fixes.AttackOrder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;
import xyz.cucumber.base.Client;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.ext.EventAttack;
import xyz.cucumber.base.events.ext.EventGameLoop;
import xyz.cucumber.base.events.ext.EventHit;
import xyz.cucumber.base.events.ext.EventSendPacket;
import xyz.cucumber.base.events.ext.EventTick;
import xyz.cucumber.base.events.ext.EventTimeDelay;
import xyz.cucumber.base.events.ext.EventWorldChange;
import xyz.cucumber.base.module.ArrayPriority;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.feat.other.TeamsModule;
import xyz.cucumber.base.module.feat.player.ScaffoldModule;
import xyz.cucumber.base.module.settings.BooleanSettings;
import xyz.cucumber.base.module.settings.NumberSettings;
import xyz.cucumber.base.utils.MovementUtils;
import xyz.cucumber.base.utils.Timer;
import xyz.cucumber.base.utils.game.EntityUtils;
import xyz.cucumber.base.utils.math.RotationUtils;

@ModuleInfo(category = Category.COMBAT, description = "Allows you to teleport to player", name = "Timer Range", key = Keyboard.KEY_NONE, priority = ArrayPriority.HIGH)

public class TimerRangeModule extends Mod {
	private LinkedList<Packet> outPackets = new LinkedList<Packet>();

	private KillAuraModule killAura;

	private double balance, lastBalance, smartMaxBalance;
	private boolean fast;

	private EntityLivingBase target;

	private Timer delayTimer = new Timer();
	private Timer clickTimer = new Timer();

	public NumberSettings minRange = new NumberSettings("Min Range", 6, 3, 8, 0.01);
	public NumberSettings maxRange = new NumberSettings("Max Range", 6, 3, 6, 0.01);
	public NumberSettings maxTimer = new NumberSettings("Timer", 10, 1, 10, 0.1);
	public NumberSettings slowTimer = new NumberSettings("Slow Timer", 0, 0, 1, 0.01);
	public NumberSettings chargeMultiplier = new NumberSettings("Charge Multiplier", 1, 0, 1, 0.01);
	public NumberSettings delay = new NumberSettings("Delay", 200, 0, 3000, 50);
	public BooleanSettings instantTimer = new BooleanSettings("Instant Timer", true);
	public BooleanSettings notInCombo = new BooleanSettings("Not In Combo", true);
	public BooleanSettings onlyForward = new BooleanSettings("Only Forward", true);
	public BooleanSettings preLoad = new BooleanSettings("Pre Load", false);
	public BooleanSettings blink = new BooleanSettings("Blink", false);
	public BooleanSettings onlyOnGround = new BooleanSettings("Only On Ground", false);
	public BooleanSettings noFluid = new BooleanSettings("No Fluid", true);

	public TimerRangeModule() {
		this.addSettings(minRange, maxRange, maxTimer, slowTimer, chargeMultiplier, delay, instantTimer, notInCombo,
				onlyForward, preLoad, blink, onlyOnGround, noFluid);
	}

	public void onEnable() {
		this.killAura = ((KillAuraModule) Client.INSTANCE.getModuleManager().getModule(KillAuraModule.class));
	}

	public void onDisable() {
		outPackets.clear();
	}

	@EventListener
	public void onSendPacket(EventSendPacket e) {
		if (e.getPacket() instanceof C03PacketPlayer) {
			C03PacketPlayer packet = (C03PacketPlayer) e.getPacket();
			if (mc.timer.timerSpeed > 1f && blink.isEnabled()) {
				e.setCancelled(true);
				outPackets.add(e.getPacket());
			}
		}
	}

	@EventListener
	public void onWorldChange(EventWorldChange e) {
		balance = 0;
		lastBalance = 0;
	}

	@EventListener
	public void onTick(EventTick e) {
		this.setInfo(maxRange.getValue() + "");

		if (preLoad.isEnabled()) {
			if (mc.timer.timerSpeed != 1f) {
				balance += chargeMultiplier.getValue();
			} else {
				balance++;
			}
		} else {
			if (fast) {
				balance += chargeMultiplier.getValue();
			} else {
				balance++;
			}
		}

		if (shouldStop()) {
			if (fast) {
				release();
				mc.timer.timerSpeed = 1f;
				fast = false;
			}
		}
	}

	@EventListener
	public void onTickDelay(EventTimeDelay e) {
		balance--;
	}

	@EventListener
	public void onGameLoop(EventGameLoop e) {
		target = EntityUtils.getTarget(maxRange.getValue() + 10, killAura.Targets.getMode(), "Single",
				(int) killAura.switchDelay.getValue(),
				Client.INSTANCE.getModuleManager().getModule(TeamsModule.class).isEnabled(),
				killAura.TroughWalls.isEnabled(), killAura.attackInvisible.isEnabled(),
				killAura.attackDead.isEnabled());

		if (mc == null || mc.thePlayer == null || mc.theWorld == null || killAura == null || !killAura.isEnabled()
				|| Client.INSTANCE.getModuleManager().getModule(ScaffoldModule.class).isEnabled()
				|| mc.thePlayer.ticksExisted < 10) {
			if (mc.thePlayer != null) {
				if (mc.thePlayer.ticksExisted < 20) {
					mc.timer.timerSpeed = 1f;
				}
			}
			if (mc.timer.timerSpeed == slowTimer.getValue()) {
				mc.timer.timerSpeed = 1f;
			}
			target = null;
			return;
		}

		if (target != null && outOfRange()) {
			target = null;
		}

		if (fast) {
			if (preLoad.isEnabled() ? balance < lastBalance : balance < (smartMaxBalance + lastBalance)) {
				if (target != null) {
					if (!isTargetCloseOrVisible()) {
						if (isHurtTime()) {
							if (instantTimer.isEnabled()) {
								try {
									boolean shouldStop = false;
									while (!shouldStop) {

										if (isTargetCloseOrVisible() || !isHurtTime()
												|| (!preLoad.isEnabled() && shouldStop())
												|| (preLoad.isEnabled() ? balance >= lastBalance
														: balance >= smartMaxBalance + lastBalance)) {
											shouldStop = true;
											delayTimer.reset();

											release();
											mc.timer.timerSpeed = 1f;
											fast = false;

											if (preLoad.isEnabled())
												delayTimer.reset();

											if (clickTimer.hasTimeElapsed(350, true)) {
												Client.INSTANCE.getEventBus().call(new EventAttack(target));

												Entity rayTracedEntity = RotationUtils.rayTrace(3, new float[] {
														RotationUtils.serverYaw, RotationUtils.serverPitch });
												if (rayTracedEntity != null) {
													AttackOrder.sendFixedAttackEvent(mc.thePlayer, rayTracedEntity);
												}
											}
										}
										if (!shouldStop) {
											mc.runTick();
											balance += chargeMultiplier.getValue();
										}

									}
								} catch (IOException e1) {
								}
							} else {
								mc.timer.timerSpeed = (float) maxTimer.getValue();

								if (shouldStop()) {
									if (fast) {
										release();
										mc.timer.timerSpeed = 1f;
										fast = false;

										if (preLoad.isEnabled())
											delayTimer.reset();

										if (clickTimer.hasTimeElapsed(350, true)) {
											Client.INSTANCE.getEventBus().call(new EventAttack(target));

											Entity rayTracedEntity = RotationUtils.rayTrace(3,
													new float[] { RotationUtils.serverYaw, RotationUtils.serverPitch });
											if (rayTracedEntity != null) {
												AttackOrder.sendFixedAttackEvent(mc.thePlayer, rayTracedEntity);
											}
										}
									}
								}
							}
						} else {
							if (!preLoad.isEnabled()) {
								mc.timer.timerSpeed = 1f;
								fast = false;
								if (preLoad.isEnabled())
									delayTimer.reset();
							}
						}
					} else {
						release();
						mc.timer.timerSpeed = 1f;
						fast = false;

						if (preLoad.isEnabled())
							delayTimer.reset();

						if (clickTimer.hasTimeElapsed(350, true)) {
							Client.INSTANCE.getEventBus().call(new EventAttack(target));

							Entity rayTracedEntity = RotationUtils.rayTrace(3,
									new float[] { RotationUtils.serverYaw, RotationUtils.serverPitch });
							if (rayTracedEntity != null) {
								AttackOrder.sendFixedAttackEvent(mc.thePlayer, rayTracedEntity);
							}
						}
					}
				} else {
					mc.timer.timerSpeed = 1f;
					fast = false;
					if (preLoad.isEnabled())
						delayTimer.reset();
				}
			} else {
				mc.timer.timerSpeed = 1f;
				fast = false;
				if (preLoad.isEnabled())
					delayTimer.reset();
			}
		}
		if (!fast) {
			if (preLoad.isEnabled()) {
				if (!delayTimer.hasTimeElapsed(delay.getValue(), false)) {
					return;
				}
				if (target != null) {
					if (!shouldStop()) {
						if (mc.timer.timerSpeed == 1f)
							setSmartBalance();
					}

					if (!isTargetCloseOrVisible() && isHurtTime()) {
						if (balance > -smartMaxBalance + lastBalance) {
							if (shouldStop()) {
								if (mc.timer.timerSpeed != (float) slowTimer.getValue())
									lastBalance = balance;
								mc.timer.timerSpeed = 1f;
								return;
							}

							mc.timer.timerSpeed = (float) slowTimer.getValue();
						} else {
							fast = true;
							mc.timer.timerSpeed = 1f;
						}
					} else {
						if (mc.timer.timerSpeed != (float) slowTimer.getValue())
							lastBalance = balance;
						mc.timer.timerSpeed = 1f;
					}
				} else {
					if (mc.timer.timerSpeed != (float) slowTimer.getValue())
						lastBalance = balance;
					mc.timer.timerSpeed = 1f;
				}
				release();
			} else {
				if (balance > lastBalance) {
					mc.timer.timerSpeed = (float) slowTimer.getValue();
				} else {
					if (mc.timer.timerSpeed == (float) slowTimer.getValue()) {
						mc.timer.timerSpeed = 1f;
					}

					if (!delayTimer.hasTimeElapsed(delay.getValue(), false)) {
						return;
					}

					if (target != null) {
						if (!isTargetCloseOrVisible() && isHurtTime()) {
							fast = true;
							delayTimer.reset();
							lastBalance = balance;
						}
					}
				}
				release();
			}
			if (fast) {
				if (!preLoad.isEnabled()) {
					setSmartBalance();
				}
			}
		}
		if (mc.thePlayer.ticksExisted <= 20) {
			mc.timer.timerSpeed = 1f;
		}
	}

	public void release() {
		try {
			while (!outPackets.isEmpty()) {
				mc.getNetHandler().getNetworkManager().sendPacketNoEvent(outPackets.poll());
			}
		} catch (Exception ex) {

		}
	}

	public void setSmartBalance() {
		double predictX = target.posX + ((target.posX - target.lastTickPosX) * 2);
		double predictZ = target.posZ + ((target.posZ - target.lastTickPosZ) * 2);

		double distance = mc.thePlayer.getDistanceToEntity(target) - Math.abs(distanceAdjust());

		if (target == null) {
			smartMaxBalance = 0;
			return;
		}
		if (shouldStop()) {
			smartMaxBalance = 0;
			return;
		}

		double playerBPS = Math
				.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);

		double targetMotionX = Math.abs(target.lastTickPosX - target.posX);
		double targetMotionZ = Math.abs(target.lastTickPosZ - target.posZ);
		double targetBPS = Math.sqrt(targetMotionX * targetMotionX + targetMotionZ * targetMotionZ);

		playerBPS = Math.max(0.15, playerBPS);
		targetBPS = Math.max(preLoad.isEnabled() ? 0.15 : 0, targetBPS);

		double finalDistance = distance - 3;

		if (preLoad.isEnabled()) {
			smartMaxBalance = finalDistance / (playerBPS + (targetBPS * 3));
		} else {
			smartMaxBalance = finalDistance / (playerBPS * 2);
		}
	}

	public boolean shouldStop() {
		boolean stop = false;
		
		if (target == null) {
			stop = true;
			return stop;
		}
		
		double predictX = mc.thePlayer.posX + ((mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * 2);
		double predictZ = mc.thePlayer.posZ + ((mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * 2);

		float f = (float) (predictX - target.posX);
		float f1 = (float) (mc.thePlayer.posY - target.posY);
		float f2 = (float) (predictZ - target.posZ);

		double predictedDistance = MathHelper.sqrt_float(f * f + f1 * f1 + f2 * f2);

		if (onlyOnGround.isEnabled() && !mc.thePlayer.onGround) {
			stop = true;
		}
		
		if (EntityUtils.getDistanceToEntityBox(target) < minRange.getValue()) {
			if (preLoad.isEnabled()) {
				if (!fast) {
					stop = true;
				}
			} else {
				if (!fast) {
					if (mc.timer.timerSpeed != slowTimer.getValue()) {
						stop = true;
					}
				}
			}
		}

		if (isTargetCloseOrVisible()) {
			stop = true;
		}

		if (!isHurtTime()) {
			stop = true;
		}

		if (outOfRange()) {
			stop = true;
		}

		if ((MovementUtils.getSpeed() <= 0.08 || !mc.gameSettings.keyBindForward.pressed
				|| predictedDistance > mc.thePlayer.getDistanceToEntity(target) + 0.08) && onlyForward.isEnabled()) {
			stop = true;
		}

		if (outOfRange()) {
			stop = true;
		}
		if (noFluid.isEnabled() && (mc.thePlayer.isInWater() || mc.thePlayer.isInLava() || mc.thePlayer.isInWeb)) {
			stop = true;
		}

		if (mc.thePlayer.getDistance(target.lastTickPosX, target.lastTickPosY, target.lastTickPosZ) < mc.thePlayer
				.getDistance(target.posX, target.posY, target.posZ)) {
			stop = notInCombo.isEnabled();
		}

		if (Client.INSTANCE.getModuleManager().getModule(BackTrackModule.class).isEnabled()
				&& !BackTrackModule.packets.isEmpty()) {
			if (mc.thePlayer.getDistance(target.posX, target.posY, target.posZ) < mc.thePlayer
					.getDistance(target.realPosX, target.realPosY, target.realPosZ)) {
				stop = notInCombo.isEnabled();
			}
		}

		return fast ? stop : preLoad.isEnabled() ? stop : false;
	}

	public boolean outOfRange() {
		return mc.thePlayer.getDistanceToEntity(target) > getMaxDistance() + distanceAdjust();
	}

	public boolean isTargetCloseOrVisible() {
		return mc.objectMouseOver.entityHit != null || EntityUtils.getDistanceToEntityBox(target) <= 3;
	}

	public boolean isHurtTime() {
		return mc.thePlayer.hurtTime <= (!preLoad.isEnabled() ? 10 : 10);
	}

	public double distanceAdjust() {
		if (mc.thePlayer.getDistance(target.lastTickPosX, target.lastTickPosY,
				target.lastTickPosZ) < mc.thePlayer.getDistance(target.posX, target.posY, target.posZ) - 0.05) {
			return -0.5;
		} else if (mc.thePlayer.getDistance(target.lastTickPosX, target.lastTickPosY,
				target.lastTickPosZ) > mc.thePlayer.getDistance(target.posX, target.posY, target.posZ) + 0.1) {
			return 0.3;
		}

		return 0;
	}
	
	public double getMaxDistance() {
		return maxRange.getValue() + 1;
	}
}
