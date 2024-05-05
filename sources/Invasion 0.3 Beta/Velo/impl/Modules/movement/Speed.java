package Velo.impl.Modules.movement;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.lwjgl.input.Keyboard;

import Velo.api.Module.Module;
import Velo.api.Util.Other.BlockUtil;
import Velo.api.Util.Other.MovementUtil;
import Velo.impl.Event.EventMovement;
import Velo.impl.Event.EventUpdate;
import Velo.impl.Settings.BooleanSetting;
import Velo.impl.Settings.ModeSetting;
import Velo.impl.Settings.NumberSetting;
import net.minecraft.block.BlockSlime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IChatComponent;

public class Speed extends Module {

	public static ModeSetting mode = new ModeSetting("Mode", "Custom", "Redesky", "Custom", "Jump", "Watchdog", "Ncp Fast", "Ncp", "Ncp LowHop", "Funcraft LowHop", "Funcraft", "Aac", "Mineplex Smooth", "Mineplex", "Matrix");
	public static NumberSetting customspeed = new NumberSetting("CustomSpeed", 0.2, 0.1, 5, 0.01);
	public static NumberSetting watchdogfriction = new NumberSetting("Watchdog Friction", 0.42, 0.01, 1.00, 0.01);
	public static NumberSetting watchdogspeed = new NumberSetting("Watchdog Speed", 0.42, 0.01, 1.00, 0.01);
	public static NumberSetting speed1 = new NumberSetting("Speed", 0.5, 0.1, 5, 0.01);
	public static NumberSetting acceleration = new NumberSetting("MineplexAcceleration", 0.2, 0.1, 0.4, 0.1);
	public static NumberSetting mineplexspeed = new NumberSetting("MineplexSpeed", 1.0, 0.2, 2, 0.1);
	public static BooleanSetting fastmineplexacceleration = new BooleanSetting("FastMineplexAcceleration", true);

	double moveSpeed3 = 0.0D;
	float ticks = 0.1F;
	float ticks2 = 0F;
	double speed = 0, currentDistance = 0, lastDistance = 0;
	boolean prevOnGround = false;
	public static boolean isEnabled = false;

	private final Queue<Packet> packetQueue = new ConcurrentLinkedQueue();

	public Speed() {
		super("Speed", "Speed", Keyboard.KEY_NONE, Category.MOVEMENT);
		this.loadSettings(mode, customspeed, watchdogfriction, watchdogspeed, mineplexspeed);
	}

	public void onEnable() {
		if(mode.equalsIgnorecase("Ncp")) {
			
		}
		if(mode.equalsIgnorecase("Test")) {
			mc.thePlayer.setSpeed(0);
		}
		if(mode.equalsIgnorecase("Mineplex Smooth")) {
			mc.thePlayer.setSpeed(0);
			ticks = 0.4F;
		}
		if(mode.equalsIgnorecase("Mineplex")) {
			ticks = 0.4F;
		}
		if(mode.equalsIgnorecase("Ncp Fast")) {
			ticks = 0.0F;
		}
	}

	public void onDisable() {
		isEnabled = false;
		speed = 0;
		ticks = 0;
		mc.thePlayer.setSprinting(false);
		mc.timer.timerSpeed = 1F;
		mc.thePlayer.speedInAir = 0.02F;
		if(mode.equalsIgnorecase("Test")) {
			ticks2 = 0F;
			speed = 0;
			currentDistance = 0;
			lastDistance = 0;
			ticks = 0.4F;
		}
		if(mode.equalsIgnorecase("Mineplex")) {
			ticks = 0.4F;
		}
		if(mode.equalsIgnorecase("Ncp Fast")) {
			//ticks = 0.2F;
			mc.thePlayer.setSpeed(0);
		}
		if(mode.equalsIgnorecase("Aac")) {
			ticks = 0.13F;
			ticks2 = 0.13F;
			mc.thePlayer.setSpeed(0);
		}
		if(mode.equalsIgnorecase("Aac")) {
			ticks = 0;
		}
	}

	public void onUpdate(EventUpdate event) {
		isEnabled = true;
		this.setDisplayName("Speed " + mode.modes.get(mode.index));
		
		
		if(mode.equalsIgnorecase("Aac")) {
			if(mc.thePlayer.isMoving()) {
				if(mc.thePlayer.onGround) {
					mc.thePlayer.motionY = 0.42F;
					//mc.thePlayer.setSpeed(0.7);
					if(ticks2 < 0.34) {
						ticks2 += 0.12F;
					}
					if(ticks2 > 0.34) {
						ticks2 = 0.34F;
					}
					ticks = ticks2;
				} else {
					ticks *= 0.98F;
					mc.thePlayer.setSpeed(ticks);
					mc.timer.timerSpeed = 1F;
					//mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 1.0E-10, mc.thePlayer.posZ);
				}
			}
		}
		
		if(mode.equalsIgnorecase("MineplexOnGround")) {
			if (this.airSlot() == -10)
				return;
			if (mc.thePlayer.isMoving()) {
				this.mc.getNetHandler().getNetworkManager().sendPacket(new C09PacketHeldItemChange(this.airSlot()));
				BlockUtil.placeHeldItemUnderPlayer();
			} else {
				this.mc.getNetHandler().getNetworkManager().sendPacket(new C09PacketHeldItemChange(this.mc.thePlayer.inventory.currentItem));
			}
			double targetSpeed = this.speed1.getValue() / 2;
			if (this.moveSpeed3 < targetSpeed) {
				this.moveSpeed3 += 0.12412D;
			} else {
				this.moveSpeed3 = targetSpeed;
			}
			mc.thePlayer.setSpeed(0.28);
		}
		if(mode.equalsIgnorecase("Ncp")) {
			if(mc.thePlayer.isMoving() && !mc.thePlayer.isCollidedHorizontally) {
				if(mc.thePlayer.onGround) {
					mc.thePlayer.jump();
					mc.timer.timerSpeed = 1.05F;
					if(!mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
						mc.thePlayer.motionX *= 1.0718F;
						mc.thePlayer.motionZ *= 1.0718F;
					} else {
						if(mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 0) {
							mc.thePlayer.motionX *= 1.0918F;
							mc.thePlayer.motionZ *= 1.0918F;
						}
						if(mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 1) {
							mc.thePlayer.motionX *= 1.2718F;
							mc.thePlayer.motionZ *= 1.2718F;
						}
					}
				} else {
					mc.thePlayer.jumpMovementFactor = 0.0265F;
					mc.timer.timerSpeed = 1.05F;
					double direction = mc.thePlayer.getDirection();
					double speed = 1.0;
					if(mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
						if(mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() > 0) {
							//speed = 1.0 + (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 0.02 : 0);
						}
						if(mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() > 1) {
							//speed = 1.0 + (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 0.03 : 0);
						}
					}
					
					double currentMotion = Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
					
					mc.thePlayer.setSpeed(currentMotion * speed);
				}
			} else {
				mc.thePlayer.setSpeed(0);
			}
		}
		if(mode.equalsIgnorecase("Ncp Fast")) {
			if(mc.thePlayer.isMoving() && !mc.thePlayer.isCollidedHorizontally) {
				if(mc.thePlayer.onGround) {
					mc.thePlayer.jump();
					mc.thePlayer.motionY = 0.42F;
					mc.timer.timerSpeed = 1.05F;
					if(!mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
						mc.thePlayer.setSpeed(0.48+ticks);
					} else {
						if(mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 0) {
							mc.thePlayer.setSpeed(0.525+ticks);
						}
						if(mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 1) {
							mc.thePlayer.setSpeed(0.68+ticks);
						}
						if(mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 2) {
							mc.thePlayer.setSpeed(0.73+ticks);
						}
					}
					prevOnGround = true;
				} else {
					if(prevOnGround) {
						prevOnGround = false;
						if(ticks < 0.035) {
							ticks += 0.04F;
						}
					}
					if(ticks > 0.035) {
						ticks -= 0.001F;
					}
					mc.thePlayer.jumpMovementFactor = 0.0265F;
					mc.timer.timerSpeed = 1.05F;
					double direction = mc.thePlayer.getDirection();
					double speed = 1.0;
					if(mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
						if(mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 0) {
							speed = 1.0 + (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 0.015 : 0);
						}
						if(mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 1) {
							speed = 1.0 + (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 0.025 : 0);
						}
						if(mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 2) {
							speed = 1.0 + (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 0.03 : 0);
						}
					}
					
					//mc.thePlayer.motionY = -0.42F;
					
					double currentMotion = Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
					
					mc.thePlayer.setSpeed(currentMotion * speed);
					
					//mc.thePlayer.motionY = -0.42F;
				}
			} else {
				ticks = 0;
				mc.thePlayer.setSpeed(0);
			}
		}
		if(mode.equalsIgnorecase("Funcraft LowHop")) {
			if(mc.thePlayer.isMoving() && !mc.thePlayer.isCollidedHorizontally) {
				if(mc.thePlayer.onGround) {
					mc.thePlayer.jump();
					mc.thePlayer.motionY = 0.42F;
					mc.timer.timerSpeed = 1.05F;
					if(!mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
						mc.thePlayer.setSpeed(0.48+ticks);
					} else {
						if(mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 0) {
							mc.thePlayer.setSpeed(0.525+ticks);
						}
						if(mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 1) {
							mc.thePlayer.setSpeed(0.62+ticks);
						}
						if(mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 2) {
							mc.thePlayer.setSpeed(0.73+ticks);
						}
					}
					prevOnGround = true;
				} else {
					if(prevOnGround) {
						if(!mc.gameSettings.keyBindJump.pressed) {
							mc.thePlayer.motionY = -0.42F;
						}
						prevOnGround = false;
						if(ticks < 0.035) {
							ticks += 0.04F;
						}
					}
					if(ticks > 0.035) {
						ticks -= 0.001F;
					}
					mc.thePlayer.jumpMovementFactor = 0.0265F;
					mc.timer.timerSpeed = 1.05F;
					double direction = mc.thePlayer.getDirection();
					double speed = 1.0;
					if(mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
						if(mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 0) {
							speed = 1.0 + (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 0.015 : 0);
						}
						if(mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 1) {
							speed = 1.0 + (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 0.025 : 0);
						}
						if(mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 2) {
							speed = 1.0 + (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 0.03 : 0);
						}
					}
					
					
					double currentMotion = Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
					
					mc.thePlayer.setSpeed(currentMotion * speed - 1.0E-10);
					
					//mc.thePlayer.motionY = -0.42F;
				}
				mc.timer.timerSpeed = 1.0F;
			} else {
				ticks = 0;
				mc.thePlayer.setSpeed(0);
			}
		}
		if(mode.equalsIgnorecase("Funcraft")) {
			if(mc.thePlayer.isMoving()) {
				if(mc.thePlayer.onGround) {
					mc.thePlayer.jump();
					ticks = 0;
				} else {
					if(ticks < 0.4) {
						ticks += 0.01F;
					}
					mc.thePlayer.setSpeed(0.7 - ticks);
				}
			}
		}
		if(mode.equalsIgnorecase("Watchdog")) {
			if(mc.thePlayer.isMoving() && !mc.thePlayer.isCollidedHorizontally) {
				if(mc.thePlayer.onGround) {
					mc.thePlayer.jump();
					mc.timer.timerSpeed = 1F;
				} else {
					mc.timer.timerSpeed = 1.3F;
				}
			}
		}
		if(mode.equalsIgnorecase("Cubecraft")) {
			if(mc.thePlayer.isMoving()) {
				mc.timer.timerSpeed = 1F;
			} else {
				mc.timer.timerSpeed = 1F;
			}
		}
		
		if(mode.equalsIgnorecase("Mineplex Lowhop")) {
			this.mc.thePlayer.motionY -= 0.04000000000001D;
			mc.thePlayer.setSpeed(0.2873D);
			if (this.mc.thePlayer.onGround )
				this.mc.thePlayer.motionY = 0.4299999D;
		}
		
		if(mode.equalsIgnorecase("Mineplex Smooth")) {
			if(mc.thePlayer.isMoving()) {
				if(mc.thePlayer.onGround) {
					speed = 1;
					prevOnGround = true;
					//mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.lastTickPosX, mc.thePlayer.posY, mc.thePlayer.lastTickPosZ, true));
					mc.thePlayer.setSpeed(0);
					mc.timer.timerSpeed = 3.5F;
					//mc.thePlayer.onGround = false;
					ticks2 = 0.0F;
					speed = 0;
				} else {
					ticks2 += 0.02F;
					if(prevOnGround) {
						mc.thePlayer.motionY = 0.42F;
						//mc.thePlayer.motionY = 0.42F;
						if(ticks < mineplexspeed.getValue()) {
							//mc.thePlayer.motionY -= 0.05F;
							ticks += 0.25;
						} else {
							
						}
						prevOnGround = false;
					}
					if(speed < 10) {
						speed += 1;
						mc.thePlayer.motionY = 0.0F;
					}
					mc.timer.timerSpeed = 1F;
					mc.thePlayer.setSpeed(ticks-ticks2);
				}
			} else {
				ticks = 0.4F;
				mc.thePlayer.setSpeed(0);
			}
			if(speed == 1) {
				speed = 0;
				mc.timer.timerSpeed = 1.0F;
				mc.thePlayer.motionY = 0.42F;
			}
		}
		
		
		
		if(mode.equalsIgnorecase("NCP")) {
			
		}
		
		
		if(mode.equalsIgnorecase("Mineplex")) {
			if(mc.thePlayer.isMoving()) {
				if(mc.thePlayer.onGround) {
					prevOnGround = true;
					mc.timer.timerSpeed = 4.0F;
					//mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.lastTickPosX, mc.thePlayer.posY, mc.thePlayer.lastTickPosZ, true));
					mc.thePlayer.motionY = 0.4F;
					mc.thePlayer.setSpeed(0);
					//mc.thePlayer.setPosition(mc.thePlayer.posX + -Math.sin(mc.thePlayer.getDirection()) * 0.05, mc.thePlayer.posY, mc.thePlayer.posZ + Math.cos(mc.thePlayer.getDirection()) * 0.05);
					speed += 1;
					if(ticks < 0.4) {	
						//mc.thePlayer.motionY = 0.05F;
					}
					ticks2 = 0.0F;
				} else {
					ticks2 += 0.02F;
					mc.timer.timerSpeed = 1F;
					speed = 0;
					if(prevOnGround) {
						if(ticks < mineplexspeed.getValue()) {
							//mc.thePlayer.motionY -= 0.05F;
							ticks += 0.25;
						} else {
							
						}
						prevOnGround = false;
					}
					mc.thePlayer.setSpeed(ticks-ticks2);
					//mc.thePlayer.motionY = -0.42F;
				}
				if(speed > 1) {
					
				}
			} else {
				ticks = 0.4F;
				mc.thePlayer.setSpeed(0);
			}
		}
		if(mode.equalsIgnorecase("Redesky")) {
			if(mc.thePlayer.onGround) {
				ticks = 0;
				mc.thePlayer.jump();
			} else {
				ticks += 0.01F;
				mc.thePlayer.jumpMovementFactor = 0.105F - ticks;
			}
		}
		if(mode.equalsIgnorecase("Custom")) {
			if(mc.thePlayer.isMoving()) {
                mc.thePlayer.setSpeed(customspeed.getValue());
                if(mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                }
            } else {
                mc.thePlayer.setSpeed(0);
            }
		}
		if(mode.equalsIgnorecase("Watchdog Fast")) {
			if(mc.thePlayer.isMoving() && !mc.thePlayer.isCollidedHorizontally) {
				if(mc.thePlayer.onGround) {
					mc.thePlayer.motionY = 0.42F;
					ticks = 0.15F;
				} else {
					ticks -= ticks/87F;
					mc.thePlayer.setSpeed(ticks);
				}
			} else {
				mc.thePlayer.setSpeed(0);
			}
		}
		if(mode.equalsIgnorecase("Ncp LowHop")) {
			if(mc.thePlayer != null && mc.theWorld != null) {
				if(mc.thePlayer.isMoving()) {
					mc.timer.timerSpeed = 1.05F;
					if(mc.thePlayer.onGround) {
						mc.thePlayer.jump();
						mc.thePlayer.motionY = 0.4F;
						if(!mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
							mc.thePlayer.setSpeed(0.39);
						} else {
							if(mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 0) {
								mc.thePlayer.setSpeed(0.49);
							}
							if(mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 1) {
								mc.thePlayer.setSpeed(0.575);
							}
							if(mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 2) {
								mc.thePlayer.setSpeed(0.73);
							}
						}
					} else {
						mc.timer.timerSpeed = 1.05F;
						mc.thePlayer.setSpeed(mc.thePlayer.getCurrentMotion()*1.0);
						mc.thePlayer.motionY = -0.4F;
					}
				} else {
					mc.thePlayer.setSpeed(0);
				}
			}
		}
		if(mode.equalsIgnorecase("Watchdog LowHop")) {
			if(mc.thePlayer != null && mc.theWorld != null) {
				if(mc.thePlayer.isMoving()) {
					mc.timer.timerSpeed = 1.05F;
					if(mc.thePlayer.onGround) {
						mc.thePlayer.jump();
						mc.thePlayer.motionY = 0.42F;
						if(!mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
							mc.thePlayer.setSpeed(0.4);
						} else {
							if(mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 0) {
								mc.thePlayer.setSpeed(0.45);
							}
							if(mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 1) {
								mc.thePlayer.setSpeed(0.55);
							}
							if(mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 2) {
								mc.thePlayer.setSpeed(0.7);
							}
						}
					} else {
						mc.thePlayer.setSpeed(mc.thePlayer.getCurrentMotion());
						mc.thePlayer.motionY = -0.1F;
					}
				} else {
					mc.thePlayer.setSpeed(0);
				}
			}
		}
		if(mode.equalsIgnorecase("Aac")) {
			if(mc.thePlayer != null && mc.theWorld != null) {
				if(mc.thePlayer.onGround) {
					try {
						//mc.thePlayer.getEntityBoundingBox().minY = 0;
						//mc.thePlayer.lastTickPosY = 57;
					} catch(NullPointerException e1) {
						
					}
					if(ticks < 0.3) {
						ticks += 0.01F;
					}
					//mc.thePlayer.setSpeed(0+ticks);
				}
			}
		}
	}
	@Override
	public void onMovementUpdate(EventMovement e) {
	if(mode.equalsIgnorecase("NCP")) {
		MovementUtil.setSpeed1(e, .2f);
	}
		super.onMovementUpdate(e);
	}
	public static int airSlot() {
		for (int x = 0; x < 8; x++) {
			if (Minecraft.getMinecraft().thePlayer.inventory.mainInventory[x] == null) {
				return x;
			}
		}

		return -10;
	}
}