package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.events.PreMotionEvent;
import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.CheckBtnSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ModSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.SliderSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ValueFormat;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class Flight extends Module {

	int wait = 6;
	double MACvelY = 0.02;
	double startingHeight;
	double fallSpeed = 0.05;
	double maxY;
	boolean damaging = false;
	private WaitTimer timer = new WaitTimer();
	public double flyHeight;
	private boolean aac;
	private double aad;
	boolean Up = false;
	boolean Start = false;
	private WaitTimer cubeTimer = new WaitTimer();
	private WaitTimer hypixelTimer = new WaitTimer();
	
	boolean simulateFall = false;

	public Flight() {
		super("Flight", Keyboard.KEY_G, Category.MOVEMENT, "Allows you to fly.");
	}

	@Override
	public void onDisable() {
		mc.player.capabilities.isFlying = false;
		Utils.resetMcTimerTPS();
		mc.player.stepHeight = 0.6F;
		super.onDisable();
	}

	public void onPreMotion(PreMotionEvent event) {
		if (this.currentMode.equals("AAC") || currentMode.equals("AAC2")) {
			mc.player.setSprinting(false);
			if ((mc.player.fallDistance >= 4.0F) && (!this.aac)) {
				this.aac = true;
				this.aad = (mc.player.posY + 3.0D);
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX,
						mc.player.posY, mc.player.posZ, true));
			}
			mc.player.capabilities.isFlying = false;
			if (this.aac) {
				if (mc.player.onGround) {
					this.aac = false;
				}
				if (mc.player.posY < this.aad) {
					mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX,
							mc.player.posY, mc.player.posZ, true));
					if (mc.gameSettings.keyBindSneak.pressed) {
						this.aad -= 2.0D;
					} else if ((mc.gameSettings.keyBindSneak.pressed) && (mc.player.posY < this.aad + 0.8D)) {
						this.aad += 2.0D;
					} else {
						mc.player.motionY = 0.7D;
						if(currentMode.equals("AAC2")) {
							mc.player.setPosition(mc.player.posX, mc.player.posY + 0.1, mc.player.posZ);
							mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX,
									mc.player.posY + 0.1, mc.player.posZ, true));
						}
						gijabgioagbpwigbpihbpisbsrlkgbaoighbaig(0.8f);
					}
				}
				else {
					if(currentMode.equals("AAC2")) {
						if(mc.player.motionY <= 0) {
							event.y = 0.01;
						}
					}
				}
			} else {
				mc.player.capabilities.isFlying = false;
			}
		}
		if (this.currentMode.equals("AAC3") && Start) {
			if (!Up) {
				//event.y = 0.01;
				mc.player.motionY = 1;
				Up = true;
			}
			else {
				event.y = -0.05;
				event.x *= 3;
				event.z *= 3;
				Up = false;
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX,
						mc.player.posY, mc.player.posZ, true));
			}
		}
	}

	public void gijabgioagbpwigbpihbpisbsrlkgbaoighbaig(float speed) {
		mc.player.motionX = (-(Math.sin(aan()) * speed));
		mc.player.motionZ = (Math.cos(aan()) * speed);
	}

	public float aan() {
		float var1 = mc.player.rotationYaw;
		if (mc.player.moveForward < 0.0F) {
			var1 += 180.0F;
		}
		float forward = 1.0F;
		if (mc.player.moveForward < 0.0F) {
			forward = -0.5F;
		} else if (mc.player.moveForward > 0.0F) {
			forward = 0.5F;
		}
		if (mc.player.moveStrafing > 0.0F) {
			var1 -= 90.0F * forward;
		}
		if (mc.player.moveStrafing < 0.0F) {
			var1 += 90.0F * forward;
		}
		var1 *= 0.017453292F;

		return var1;
	}

	@Override
	public ModSetting[] getModSettings() {
		SliderSetting slider1 = new SliderSetting("Flight Speed", "FlightdefaultSpeed", 0.1, 9, 0.0, ValueFormat.DECIMAL);
//		CheckBtnSetting box5 = new CheckBtnSetting("Hypixel Offset on Enable", "hypixelFlightOffsetOnEnable");
		CheckBtnSetting box2 = new CheckBtnSetting("Smooth Flight", "Flightsmooth");
		CheckBtnSetting box1 = new CheckBtnSetting("Vanilla Kick Bypass", "flightkick");
		CheckBtnSetting box3 = new CheckBtnSetting("Glide Mode - Damage", "glideDmg");
		CheckBtnSetting box4 = new CheckBtnSetting("onGround Spoof", "onGroundSpoofFlight");
		return new ModSetting[] { slider1, box2, box1, box3, box4 };
	}

	@Override
	public void onEnable() {
		timer.reset();
		if (currentMode.equals("AirWalk") || currentMode.equals("Hypixel")) {
			maxY = mc.player.posY + 10;
			damaging = true;
			hypixelTimer.reset();
			// Atlas.sendChatMessage(".damage");
		}
		if(currentMode.equals("Hypixel")) {
//			mc.player.setPosition (mc.player.posX, mc.player.posY + 0.7, mc.player.posZ);
			if(ClientSettings.hypixelFlightOffsetOnEnable) {
				double posX = mc.player.posX;
				double posY = mc.player.posY;
				double posZ = mc.player.posZ;
				sendPacket(new CPacketPlayer.Position(posX, posY + 0.41999998688698D, posZ, mc.player.onGround));
				sendPacket(new CPacketPlayer.Position(posX, posY + 0.7531999805212D, posZ, mc.player.onGround));
				sendPacket(new CPacketPlayer.Position(posX, posY + 1.00133597911214D, posZ, mc.player.onGround));
				sendPacket(new CPacketPlayer.Position(posX, posY + 1.16610926093821D, posZ, mc.player.onGround));
				sendPacket(new CPacketPlayer.Position(posX, posY + 1.24918707874468D, posZ, mc.player.onGround));
				sendPacket(new CPacketPlayer.Position(posX, posY + 1.1707870772188D, posZ, mc.player.onGround));
				mc.player.setPosition(posX, posY + 1, posZ);
			}
		}
		if (currentMode.equals("Glide")) {
			this.startingHeight = mc.player.posY + 1000;
			if(ClientSettings.glideDmg) {
				Jigsaw.sendChatMessage(".damage");
			}
		}
		if (currentMode.equalsIgnoreCase("MAC")) {
			wait = 6;
			mc.player.motionY = 0.25;
		}
		if(currentMode.equals("AAC3")) {
			Up = true;
			Start = false;
		}
		super.onEnable();
	}

	public void updateFlyHeight() {
		double h = 1;
		AxisAlignedBB box = mc.player.getEntityBoundingBox().expand(0.0625, 0.0625, 0.0625);
		for (flyHeight = 0; flyHeight < mc.player.posY; flyHeight += h) {
			AxisAlignedBB nextBox = box.offset(0, -flyHeight, 0);

			if (mc.world.checkBlockCollision(nextBox)) {
				if (h < 0.0625)
					break;

				flyHeight -= h;
				h /= 2;
			}
		}
	}

	public void goToGround() {
		if (flyHeight > 320)
			return;

		double minY = mc.player.posY - flyHeight;

		if (minY <= 0)
			return;

		for (double y = mc.player.posY; y > minY;) {
			y -= 9.9;
			if (y < minY)
				y = minY;

			CPacketPlayer.Position packet = new CPacketPlayer.Position(mc.player.posX, y, mc.player.posZ, true);
			mc.player.connection.sendPacket(packet);
		}

		for (double y = minY; y < mc.player.posY;) {
			y += 9.9;
			if (y > mc.player.posY)
				y = mc.player.posY;

			CPacketPlayer.Position packet = new CPacketPlayer.Position(mc.player.posX, y, mc.player.posZ, true);
			mc.player.connection.sendPacket(packet);
		}
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		super.onUpdate(event);
		if(currentMode.equals("AAC3")) {
			event.onGround = true;
			Start = true;
		}
		if (currentMode.equalsIgnoreCase("Default")) {
			if (!ClientSettings.Flightsmooth) {
				mc.player.motionX = 0;
				mc.player.motionZ = 0;
			}
			mc.player.capabilities.isFlying = false;
			mc.player.motionY = 0;
			if (ClientSettings.Flightsmooth) {
				mc.player.jumpMovementFactor = (float) (ClientSettings.FlightdefaultSpeed / 10);
			} else {
				mc.player.jumpMovementFactor = (float) (ClientSettings.FlightdefaultSpeed);
			}

			if (mc.gameSettings.keyBindJump.isKeyDown()) {
				mc.player.motionY += ClientSettings.FlightdefaultSpeed / 2;
			}
			if (mc.gameSettings.keyBindSneak.isKeyDown()) {
				mc.player.motionY += -ClientSettings.FlightdefaultSpeed / 2;
			}
			if (ClientSettings.flightkick) {
				mc.player.connection.sendPacket(new CPacketPlayer(true));
				updateFlyHeight();
				if (flyHeight <= 290 && timer.hasTimeElapsed(500, true)
						|| flyHeight > 290 && timer.hasTimeElapsed(100, true)) {
					goToGround();
				}
			}
		}
		if (currentMode.equalsIgnoreCase("MAC")) {
			if (wait < 6) {
				wait++;
				if (mc.player.motionY < 0// If falling
						&& !mc.player.onGround) {
					mc.player.motionY = -MACvelY;
				}
				return;
			}
			if (mc.player.motionY < 0// If falling
					&& !mc.player.onGround) {
				if (mc.gameSettings.keyBindJump.isKeyDown()) {
					mc.player.setPosition(mc.player.posX, mc.player.posY + 2, mc.player.posZ);
					wait = 3;
					return;
				} else if (mc.gameSettings.keyBindSneak.pressed) {
					mc.player.motionY = -0.4;
					return;
				}
				mc.player.motionY = -MACvelY;

			}
			if (ClientSettings.flightkick) {
				mc.player.connection.sendPacket(new CPacketPlayer(true));
				updateFlyHeight();
				if (flyHeight <= 290 && timer.hasTimeElapsed(500, true)
						|| flyHeight > 290 && timer.hasTimeElapsed(100, true)) {
					goToGround();
				}
			}
		}
		if (currentMode.equalsIgnoreCase("Creative")) {
			mc.player.capabilities.isFlying = true;
			if (ClientSettings.flightkick) {
				mc.player.connection.sendPacket(new CPacketPlayer(true));
				updateFlyHeight();
				if (flyHeight <= 290 && timer.hasTimeElapsed(500, true)
						|| flyHeight > 290 && timer.hasTimeElapsed(100, true)) {
					goToGround();
				}
			}
		}
		if (currentMode.equals("Glide")) {
			// if (mc.player.onGround) {
			// this.startingHeight = mc.player.posY;
			// }
			if (!mc.player.onGround) {
				if (mc.gameSettings.keyBindJump.isKeyDown() && mc.player.posY + 0.5 < startingHeight) {
					mc.player.setPosition(mc.player.posX, mc.player.posY + 0.5, mc.player.posZ);
				} else if (mc.gameSettings.keyBindSneak.pressed) {
					mc.player.motionY = -0.2;
					return;
				}
				mc.player.motionY = -fallSpeed;
				
				if(simulateFall) {
					simulateFall = false;
					
					double posX = mc.player.posX;
					double posY = mc.player.posY;
					double posZ = mc.player.posZ;
					
					mc.player.connection.sendPacket(new CPacketPlayer.Position(posX, posY + 0.049D, posZ, false));
					mc.player.connection.sendPacket(new CPacketPlayer.Position(posX, posY, posZ, false));
					mc.player.connection.sendPacket(new CPacketPlayer.Position(posX, posY + 0.049D, posZ, false));
					mc.player.connection.sendPacket(new CPacketPlayer.Position(posX, posY, posZ, false));
					event.onGround = false;
				}
				else {
					simulateFall = true;
					event.onGround = true;
				}
				
			}
			if (ClientSettings.flightkick) {
				mc.player.connection.sendPacket(new CPacketPlayer(true));
				updateFlyHeight();
				if (flyHeight <= 290 && timer.hasTimeElapsed(500, true)
						|| flyHeight > 290 && timer.hasTimeElapsed(100, true)) {
					goToGround();
				}
			}
		}
		if(currentMode.equals("Mineplex")) {
			sendPacket(new CPacketPlayerTryUseItemOnBlock(new BlockPos(mc.player.posX, mc.player.posY - 1, mc.player.posZ), EnumFacing.DOWN, EnumHand.MAIN_HAND, 0.35692567f, 1f, 0.42487156f));
			mc.player.motionY = 0;
			if (mc.gameSettings.keyBindJump.isKeyDown()) {
				mc.player.setPosition(mc.player.posX, mc.player.posY + 1, mc.player.posZ);
			} else if (mc.gameSettings.keyBindSneak.pressed) {
				mc.player.setPosition(mc.player.posX, mc.player.posY - 1, mc.player.posZ);
			}
			event.onGround = true;
		}
		if(ClientSettings.onGroundSpoofFlight) {
			event.onGround = true;
		}
		if (currentMode.equals("AirWalk") || currentMode.equals("Hypixel")) {
			mc.player.motionY = 0;
			if(ClientSettings.hypixelFlightOffsetOnEnable) {
				if (mc.player.ticksExisted % 3 == 0 && !Utils.isEntityOnGround(mc.player)) {
	            	event.y += 1.0E-9D;
	            }
	            mc.player.stepHeight = 0.1f;
			}
			else {
				if (mc.player.ticksExisted % 3 == 0 && !Utils.isEntityOnGround(mc.player)) {
	            	mc.player.setPosition (mc.player.posX, mc.player.posY + 1.0E-9D, mc.player.posZ);
	            	mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX, mc.player.posY + 1.0E-9D, mc.player.posZ, mc.player.rotationYaw, mc.player.rotationPitch, true));
	            }
			}
			
			if (ClientSettings.flightkick && !currentMode.equals("Hypixel")) {
				mc.player.connection.sendPacket(new CPacketPlayer(true));
				updateFlyHeight();
				if (flyHeight <= 290 && timer.hasTimeElapsed(500, true)
						|| flyHeight > 290 && timer.hasTimeElapsed(100, true)) {
					goToGround();
				}
			}
			if(currentMode.equals("Hypixel")) {
				event.onGround = true;
			}
		}
	}

	@Override
	public void onBasicUpdates() {
		if (damaging) {
			damaging = false;
		}

		if (currentMode.equals("AirWalk") || currentMode.equals("Hypixel")) {
			mc.player.onGround = true;
		}
		super.onBasicUpdates();
	}

	@Override
	public String[] getModes() {
		return new String[] { "Default", "Creative", "Glide", "AirWalk", "Mineplex" };
	}

	@Override
	public String getAddonText() {
		return currentMode;
	}

}