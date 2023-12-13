package vestige.impl.module.movement;

import java.util.ArrayList;

import net.minecraft.block.BlockAir;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import vestige.Vestige;
import vestige.api.event.Listener;
import vestige.api.event.impl.MotionEvent;
import vestige.api.event.impl.MoveEvent;
import vestige.api.event.impl.PacketReceiveEvent;
import vestige.api.event.impl.PacketSendEvent;
import vestige.api.event.impl.UpdateEvent;
import vestige.api.module.Category;
import vestige.api.module.Module;
import vestige.api.module.ModuleInfo;
import vestige.api.setting.impl.BooleanSetting;
import vestige.api.setting.impl.ModeSetting;
import vestige.api.setting.impl.NumberSetting;
import vestige.util.movement.MovementUtils;
import vestige.util.network.PacketUtil;
import vestige.util.world.WorldUtil;

@ModuleInfo(name = "Speed", category = Category.MOVEMENT)
public class Speed extends Module {
	
	public final ModeSetting mode = new ModeSetting("Mode", this, "Vanilla", "Vanilla", "NCP", "CustomNCP", "Watchdog", "AGC", "ColdPvP", "Blocksmc", "Funcraft", "Zonecraft", "Strafe", "GroundStrafe", "Test");
	
	private final NumberSetting vanillaSpeed = new NumberSetting("vanilla-speed", this, 1, 0.1, 9, 0.1, false) {
		@Override
		public String getDisplayName() {
			return "Speed";
		}
		@Override
		public boolean isShown() {
			return mode.is("Vanilla");
		}
 	};
 	
 	private final BooleanSetting autoJump = new BooleanSetting("AutoJump", this, true) {
 		@Override
		public boolean isShown() {
			return mode.is("Vanilla");
		}
 	};
 	
 	private final NumberSetting timerSpeed = new NumberSetting("timer-speed", this, 1, 0.8, 4, 0.1, false) {
		@Override
		public String getDisplayName() {
			return "Timer";
		}
		
		@Override
		public boolean isShown() {
			return mode.is("Watchdog") || mode.is("Funcraft");
		}
 	};
 	
 	private final ModeSetting watchdogMode = new ModeSetting("Watchdog Mode", this, "Strafe", "Strafe", "Ground", "StrafeLess") {
 		@Override
		public boolean isShown() {
			return mode.is("Watchdog");
		}
 	};
 	
 	private final NumberSetting hypixelSpeed = new NumberSetting("hypixel-speed", this, 1, 1, 3, 1, true) {
 		@Override
		public String getDisplayName() {
			return "Speed";
		}
		
		@Override
		public boolean isShown() {
			return mode.is("Watchdog") && watchdogMode.is("Ground");
		}
 	};
 	
 	private final BooleanSetting boostOnDamage = new BooleanSetting("boost-on-hurt", this, true) {
 		@Override
		public String getDisplayName() {
			return "Boost on damage";
		}
		
 		@Override
		public boolean isShown() {
			return mode.is("Watchdog") && !watchdogMode.is("Ground");
		}
 	};
 	
 	private final BooleanSetting fastFall = new BooleanSetting("Fastfall", this, true) {
 		@Override
 		public String getDisplayName() {
 			return watchdogMode.is("StrafeLess") ? "Lowhop" : "FastFall";
 		}
 		
 		@Override
		public boolean isShown() {
			return mode.is("Watchdog") && !watchdogMode.is("Ground");
		}
 	};
 	
 	private final NumberSetting strafeTicksAfterHit = new NumberSetting("Strafe ticks after hit", this, 20, 0, 40, 1, true) {
 		@Override
		public boolean isShown() {
			return mode.is("Watchdog") && watchdogMode.is("StrafeLess");
		}
 	};
 	
 	private final BooleanSetting hogRider = new BooleanSetting("Hog rider", this, true) {
		@Override
		public boolean isShown() {
			return mode.is("Watchdog") && watchdogMode.is("Strafe");
		}
 	};
 	
 	private final BooleanSetting noYawUpdate = new BooleanSetting("No yaw update", this, true) {
		@Override
		public boolean isShown() {
			return mode.is("Watchdog") && watchdogMode.is("Strafe");
		}
 	};
 	
 	private final BooleanSetting pulseBlink = new BooleanSetting("Pulse blink", this, false) {
		@Override
		public boolean isShown() {
			return mode.is("Watchdog") && watchdogMode.is("Strafe");
		}
 	};
 	
 	private final BooleanSetting zonecraftFast = new BooleanSetting("zonecraft-fast", this, false) {
 		@Override
		public String getDisplayName() {
			return "Fast";
		}
		
		@Override
		public boolean isShown() {
			return mode.is("Zonecraft");
		}
 	};
 	
 	
 	
 	private final NumberSetting ncpJumpMotion = new NumberSetting("ncp-jump-motion", this, 0.42, 0.0625, 0.42, 0.005, false) {
 		@Override
		public String getDisplayName() {
			return "Jump Motion";
		}
		
		@Override
		public boolean isShown() {
			return mode.is("CustomNCP");
		}
 	};
 	
 	private final NumberSetting ncpJumpMult = new NumberSetting("ncp-jump-mult", this, 1.8, 0.8, 3, 0.05, false) {
 		@Override
		public String getDisplayName() {
			return "Jump Mult";
		}
		
		@Override
		public boolean isShown() {
			return mode.is("CustomNCP");
		}
 	};
 	
 	private final NumberSetting ncpLastGroundReduce = new NumberSetting("ncp-lastground-reduce", this, 0.7, 0.4, 1.2, 0.05, false) {
 		@Override
		public String getDisplayName() {
			return "LastGround Reduce";
		}
		
		@Override
		public boolean isShown() {
			return mode.is("CustomNCP");
		}
 	};
 	
 	private final NumberSetting ncpOffGroundFriction = new NumberSetting("ncp-offground-friction", this, 159, 29, 259, 5, true) {
 		@Override
		public String getDisplayName() {
			return "OffGround Friction";
		}
		
		@Override
		public boolean isShown() {
			return mode.is("CustomNCP");
		}
 	};
 	
 	private final BooleanSetting ncpYPort = new BooleanSetting("ncp-yport", this, false) {
 		@Override
		public String getDisplayName() {
			return "YPort";
		}
		
		@Override
		public boolean isShown() {
			return mode.is("CustomNCP");
		}
 	};
 	
 	private final NumberSetting coldpvpSpeed = new NumberSetting("coldpvp-speed", this, 2, 0.4, 9, 0.2, false) {
 		@Override
		public String getDisplayName() {
			return "Speed";
		}
		
		@Override
		public boolean isShown() {
			return mode.is("ColdPvP");
		}
 	};
 	
 	private final NumberSetting coldpvpTPFreq = new NumberSetting("coldpvp-tp-freq", this, 10, 6, 50, 1, true) {
 		@Override
		public String getDisplayName() {
			return "Lagback frequency";
		}
		
		@Override
		public boolean isShown() {
			return mode.is("ColdPvP");
		}
 	};
 	
 	private double speed;
 	private boolean prevOnGround;
 	
 	private float lastDirection, direction;
 	
 	private boolean shouldBlink;
 	
 	private final ArrayList<Packet> packets = new ArrayList<>();
 	
 	private int ticks;
 	
 	private boolean started;
 	
	public Speed() {
		this.registerSettings(mode, vanillaSpeed, autoJump, ncpJumpMotion, ncpJumpMult, ncpLastGroundReduce, ncpOffGroundFriction, ncpYPort, watchdogMode, timerSpeed, hypixelSpeed, boostOnDamage, strafeTicksAfterHit, fastFall, hogRider, noYawUpdate, pulseBlink, zonecraftFast, coldpvpSpeed, coldpvpTPFreq);
	}
	
	public void onEnable() {
		speed = MovementUtils.getBaseMoveSpeed();
		ticks = 0;
		
		direction = lastDirection = MovementUtils.getPlayerDirection();
		prevOnGround = mc.thePlayer.onGround;
		
		started = false;
		
		switch (mode.getMode()) {
			case "Watchdog":
				speed = MovementUtils.getBaseMoveSpeed() - 0.03;
				
				if(watchdogMode.is("Ground")) {
					//mc.thePlayer.setPosition(mc.thePlayer.posX, 0, mc.thePlayer.posZ);
				} else if(watchdogMode.is("Strafe")) {
					direction = mc.thePlayer.rotationYaw;
				} else if(watchdogMode.is("StrafeLess")) {
					ticks = 100;
				}
				break;
			case "ColdPvP":
				ticks = (int) coldpvpTPFreq.getCurrentValue();
				break;
	}
		
		shouldBlink = false;
	} 	
	
	public void onDisable() {
		mc.timer.timerSpeed = 1F;
		mc.thePlayer.speedInAir = 0.02F;
		
		this.sendPackets();
	}
	
	private void sendPackets() {
		if(!packets.isEmpty()) {
			for(Packet p : packets) {
				PacketUtil.sendPacketNoEvent(p);
			}
			packets.clear();
		}
	}
	
	@Listener
	public void onUpdate(UpdateEvent event) {
		setSuffix(mode.getMode());
		switch (mode.getMode()) {
			case "Watchdog":
				mc.timer.timerSpeed = (float) timerSpeed.getCurrentValue();
				break;
		/*
			case "Watchdog":
				if(watchdogMode.is("Strafe") || watchdogMode.is("Strafe2") || watchdogMode.is("Test")) {
					if(shouldBlink) {
						mc.timer.timerSpeed = (float) timerSpeed.getCurrentValue();
					} else {
						mc.timer.timerSpeed = 1F;
					}
				} else {
					mc.timer.timerSpeed = (float) timerSpeed.getCurrentValue();
				}
				
				switch (watchdogMode.getMode()) {
					case "Strafe":
					case "StrafeLess":
						if(mc.thePlayer.onGround) {
							prevOnGround = true;
							if(MovementUtils.isMoving()) {
								mc.thePlayer.jump();
								mc.thePlayer.motionY = Math.min(mc.thePlayer.motionY, 0.42);
								
								if(!mc.thePlayer.isSprinting()) {
									float f = MovementUtils.getPlayerDirection() * 0.017453292F;
						            mc.thePlayer.motionX -= (double)(MathHelper.sin(f) * 0.2);
						            mc.thePlayer.motionZ += (double)(MathHelper.cos(f) * 0.2);
								}
								
								MovementUtils.motionMult(mc.thePlayer.isPotionActive(Potion.moveSpeed) ?
										1.18 + mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() * 0.12 :
										1.03);
							}
						} else if(prevOnGround) {
							prevOnGround = false;
						} else {
							if (!mc.thePlayer.isSprinting()) {
								float f = MovementUtils.getPlayerDirection() * 0.017453292F;
					            mc.thePlayer.motionX -= (double)(MathHelper.sin(f) * 0.006F);
					            mc.thePlayer.motionZ += (double)(MathHelper.cos(f) * 0.006F);
							}
							
							if(Math.abs(mc.thePlayer.motionY) < 0.005) {
								mc.thePlayer.motionY = -0.16;
							}
						}
						
						if(mc.thePlayer.onGround) {
							MovementUtils.strafe(Math.max(MovementUtils.getHorizontalMotion(), MovementUtils.getBaseMoveSpeed() + 0.2));
						} else if(boostOnDamage.isEnabled() && mc.thePlayer.hurtTime > 0) {
							if(mc.thePlayer.hurtTime > 8) {
								MovementUtils.strafe(MovementUtils.getHorizontalMotion() + (watchdogMode.is("Strafe") ? 0.08 : 0.12));
							} else {
								MovementUtils.strafe(MovementUtils.getHorizontalMotion());
							}
						} else if(watchdogMode.is("Strafe")) {
							MovementUtils.strafe(MovementUtils.getHorizontalMotion());
						}
						break;
					case "Strafe2":
						if(mc.thePlayer.onGround) {
							prevOnGround = true;
							if(MovementUtils.isMoving()) {
								speed = MovementUtils.getBaseMoveSpeed() + (mc.thePlayer.isPotionActive(Potion.moveSpeed) ?
										0.2277 + mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() * 0.02 :
										0.2077);
								mc.thePlayer.motionY = 0.42;
							}
						} else if(prevOnGround) {
							speed *= 0.68;
							prevOnGround = false;
						} else {
							speed -= speed / 100;
						}
						
						if(mc.thePlayer.hurtTime > 8) {
							speed += 0.08;
						}
						
						strafeHypixel(Math.max(speed, MovementUtils.getBaseMoveSpeed() - 0.2));
				}
				break;
				*/
			case "Zonecraft":
				if(!zonecraftFast.isEnabled()) {
					mc.thePlayer.setSprinting(MovementUtils.isMoving());
					if(mc.thePlayer.hurtTime != 10) {
						if(mc.thePlayer.onGround) {
							prevOnGround = true;
							if(!mc.gameSettings.keyBindJump.isKeyDown() && MovementUtils.isMoving()) {
								mc.thePlayer.jump();
								mc.thePlayer.motionY = Math.random() * 0.05 + 0.005;
							}
						} else if(prevOnGround) {
							if(WorldUtil.isBlockUnder() && !mc.gameSettings.keyBindJump.isKeyDown()) {
								mc.thePlayer.motionY = -0.1;
							}
							prevOnGround = false;
						}
						
						MovementUtils.motionMult(1.05);
						
						MovementUtils.strafe(MovementUtils.getHorizontalMotion());
					}
				}
				break;
			case "Funcraft":
				mc.timer.timerSpeed = (float) timerSpeed.getCurrentValue();
				break;
			case "Test":
				if(mc.thePlayer.onGround) {
					mc.thePlayer.jump();
				} else {
					if(Math.abs(mc.thePlayer.motionY) < 0.005) {
						mc.thePlayer.motionY = 0.42;
					}
				}
				break;
		}
	}
	
	@Listener
	public void onSend(PacketSendEvent event) {
		switch (mode.getMode()) {
			case "Watchdog":
				if(watchdogMode.is("Strafe")) {
					if(event.getPacket() instanceof C03PacketPlayer) {
						if(pulseBlink.isEnabled()) {
							if(ticks > 4) {
								this.sendPackets();
								ticks = 0;
								return;
							}
							
							ticks++;
							event.setCancelled(true);
							packets.add(event.getPacket());
						} else {
							this.sendPackets();
						}
					} else if(event.getPacket() instanceof C0FPacketConfirmTransaction || event.getPacket() instanceof C00PacketKeepAlive) {
						if(pulseBlink.isEnabled()) {
							event.setCancelled(true);
							packets.add(event.getPacket());
						}
					} else if(event.getPacket() instanceof C0BPacketEntityAction) {
						if(pulseBlink.isEnabled()) {
							C0BPacketEntityAction packet = (C0BPacketEntityAction) event.getPacket();
							
							if(packet.getAction() == Action.RIDING_JUMP) {
								event.setCancelled(true);
								packets.add(event.getPacket());
							}
						}
					}
				}
				break;
		}
		/*
		switch (mode.getMode()) {
			case "Watchdog":
				if(watchdogMode.is("Strafe") || watchdogMode.is("Strafe2") || watchdogMode.is("Test")) {
					if (event.getPacket() instanceof C03PacketPlayer) {
						if(ticks > 3 || mc.thePlayer.onGround) {
							this.sendPackets();
							shouldBlink = false;
						}
						
						if(mc.thePlayer.onGround) {
							ticks = 0;
						} else if(!mc.thePlayer.onGround && ticks == 0) {
							shouldBlink = true;
						}
						
						if(shouldBlink) {
							event.setCancelled(true);
							packets.add(event.getPacket());
							ticks++;
						}
					}
				} else if(watchdogMode.is("StrafeLess") || watchdogMode.is("Ground")) {
					if(ticks > 4) {
						this.sendPackets();
					}
					
					event.setCancelled(true);
					packets.add(event.getPacket());
					ticks++;
				}
				break;
		}
		*/
	}
	
	@Listener
	public void onReceive(PacketReceiveEvent event) {
		switch (mode.getMode()) {
			case "Zonecraft":
				if(zonecraftFast.isEnabled()) {
					if(event.getPacket() instanceof S08PacketPlayerPosLook) {
	    				S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.getPacket();
	    				
	    				double dist = mc.thePlayer.getDistance(packet.getX(), packet.getY(), packet.getZ());
	    				
	    				if(dist <= 8 && 
	    						mc.getNetHandler().doneLoadingTerrain && 
	    						!Vestige.getInstance().getModuleManager().getModule(Fly.class).isEnabled() && 
	    						mc.thePlayer.fallDistance < 2) {
	    					event.setCancelled(true);
	    				}
	    			}
				}
				break;
			case "ColdPvP":
				if(event.getPacket() instanceof S08PacketPlayerPosLook) {
    				S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.getPacket();
    				
					if(mc.thePlayer.getDistance(packet.getX(), packet.getY(), packet.getZ()) < 8) {
						//event.setCancelled(true);
						//PacketUtil.sendPacketNoEvent(new C06PacketPlayerPosLook(packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch(), false));
					}
					
    				started = true;
    			}
				break;
		}
	}
	
	@Listener
	public void onMove(MoveEvent event) {
		switch (mode.getMode()) {
			case "Vanilla":
				if(mc.thePlayer.onGround && MovementUtils.isMoving() && autoJump.isEnabled()) {
					event.setY(MovementUtils.JUMP_MOTION);
					mc.thePlayer.motionY = 0.42;
				}
				MovementUtils.strafe(event, vanillaSpeed.getCurrentValue());
				break;
			case "NCP":
				if(mc.thePlayer.onGround) {
					prevOnGround = true;
					if(MovementUtils.isMoving()) {
						event.setY(0.41999998688698);
						mc.thePlayer.motionY = 0.42;
						speed *= 1.63;
					}
				} else if(prevOnGround) {
					speed -= 0.75 * (speed - MovementUtils.getBaseMoveSpeed());
					prevOnGround = false;
				} else {
					speed -= speed / 159;
				}

				if(!MovementUtils.isMoving() || mc.thePlayer.isCollidedHorizontally) {
					speed = MovementUtils.getBaseMoveSpeed() - 0.1;
				}

				speed = Math.max(speed, MovementUtils.getBaseMoveSpeed() - 0.1);
				MovementUtils.strafe(event, speed);
				break;
			case "CustomNCP":
				if(mc.thePlayer.onGround) {
					prevOnGround = true;
					if(MovementUtils.isMoving()) {
						if(ncpJumpMotion.getCurrentValue() == 0.42) {
							event.setY(MovementUtils.JUMP_MOTION);
							mc.thePlayer.motionY = 0.42;
						} else {
							event.setY(mc.thePlayer.motionY = ncpJumpMotion.getCurrentValue());
						}
						speed *= ncpJumpMult.getCurrentValue();
					}
				} else if(prevOnGround) {
					speed -= ncpLastGroundReduce.getCurrentValue() * (speed - MovementUtils.getBaseMoveSpeed());
					prevOnGround = false;
					if(ncpYPort.isEnabled()) {
						event.setY(mc.thePlayer.motionY = event.getY() - 1);
					}
				} else {
					speed -= speed / ncpOffGroundFriction.getCurrentValue();
				}

				if(!MovementUtils.isMoving() || mc.thePlayer.isCollidedHorizontally) {
					speed = MovementUtils.getBaseMoveSpeed();
				}

				speed = Math.max(speed, MovementUtils.getBaseMoveSpeed());
				MovementUtils.strafe(event, speed);
				break;
			case "AGC":
				if (mc.thePlayer.onGround) {
					prevOnGround = true;
					if (MovementUtils.isMoving()) {
						event.setY(MovementUtils.JUMP_MOTION);
						mc.thePlayer.motionY = 0.42;
						speed += 0.2;
						//speed /= 0.91;
					}
				} else if (prevOnGround) {
					//speed *= 0.6;
					speed *= 0.7;
					prevOnGround = false;
				} else {
					speed *= 0.91;
					speed += 0.026;
				}
				
				if(mc.thePlayer.hurtTime < 6) {
					MovementUtils.strafe(event, speed);
				} else {
					speed = MovementUtils.getHorizontalMotion();
				}
				break;
			case "Blocksmc":
				if(MovementUtils.isMoving()) {
					if(mc.thePlayer.onGround) {
						event.setY(MovementUtils.JUMP_MOTION);
						mc.thePlayer.motionY = 0.42;
						
						//speed = MovementUtils.getBaseMoveSpeed() + 0.2827;
						speed = MovementUtils.getBaseMoveSpeed() + 0.2727;
						
						if(mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
							speed += 0.04 + mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() * 0.04;
						}
						prevOnGround = true;
					} else {
						if(prevOnGround) {
							speed *= 0.6 - Math.random() * 0.00001;
							prevOnGround = false;
						} else {
							speed -= speed / 159;
						}
					}
				}
				
				speed = Math.max(speed, MovementUtils.getBaseMoveSpeed());
				
				MovementUtils.strafe(event, speed);
				break;
			case "Blocksmc1":
				if (mc.thePlayer.onGround) {
					prevOnGround = true;
					if (MovementUtils.isMoving()) {
						mc.thePlayer.jump();
						event.setY(MovementUtils.JUMP_MOTION);
						mc.thePlayer.motionY = 0.42;
						speed += 0.2;
					}
				} else if (prevOnGround) {
					speed *= 0.68;
					prevOnGround = false;
				} else {
					speed *= 0.98;
				}
				
				MovementUtils.strafe(event, Math.max(MovementUtils.getHorizontalMotion(), Math.max(speed, 0.27 + MovementUtils.getSpeedBoost())));
				break;
			case "Strafe":
			case "GroundStrafe":
				if(mc.thePlayer.hurtTime != 10) {
					if (mc.thePlayer.onGround) {
						if (MovementUtils.isMoving()) {
							if(!mc.gameSettings.keyBindJump.isKeyDown()) {
								event.setY(MovementUtils.JUMP_MOTION);
								mc.thePlayer.motionY = 0.42;
								
								float jumpSpeed = 0.2F;
								
								if(mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
									jumpSpeed += 0.03 + mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() * 0.03;
								}
								
								float f = MovementUtils.getPlayerDirection() * 0.017453292F;
					            mc.thePlayer.motionX -= (double)(MathHelper.sin(f) * jumpSpeed);
					            mc.thePlayer.motionZ += (double)(MathHelper.cos(f) * jumpSpeed);
								
					            event.setX(mc.thePlayer.motionX);
					            event.setZ(mc.thePlayer.motionZ);
							}
						}
					} else {
						if (!mc.thePlayer.isSprinting()) {
							float f = MovementUtils.getPlayerDirection() * 0.017453292F;
				            mc.thePlayer.motionX -= (double)(MathHelper.sin(f) * 0.006F);
				            mc.thePlayer.motionZ += (double)(MathHelper.cos(f) * 0.006F);
						}
						
						event.setX(mc.thePlayer.motionX);
						event.setZ(mc.thePlayer.motionZ);
					}
					
					if(mc.thePlayer.onGround || mode.is("Strafe")) {
						MovementUtils.strafe(event);
					}
				}
				break;
			case "Zonecraft":
				if(zonecraftFast.isEnabled()) {
					if(mc.thePlayer.onGround) {
						if(mc.gameSettings.keyBindJump.isKeyDown()) {
							prevOnGround = false;
							event.setY(MovementUtils.JUMP_MOTION);
							mc.thePlayer.motionY = 0.42;
							speed += 0.2;
						} else if(MovementUtils.isMoving()) {
							prevOnGround = true;
							event.setY(mc.thePlayer.motionY = 0.005 + Math.random() * 0.05);
							speed += 0.2;
						}
					} else if(prevOnGround) {
						speed *= 0.86;
						prevOnGround = false;
						event.setY(mc.thePlayer.motionY = -0.1);
					} else {
						speed *= 0.91;
						speed += 0.026;
					}
					
					MovementUtils.strafe(event, speed);
				} 
				break;
			case "Funcraft":
				if(mc.thePlayer.onGround) {
					prevOnGround = true;
					if(MovementUtils.isMoving()) {
						event.setY(0.41999998688698);
						mc.thePlayer.motionY = 0.42;
						speed *= 2.13;
					}
				} else if(prevOnGround) {
					speed -= 0.66 * (speed - MovementUtils.getBaseMoveSpeed());
					prevOnGround = false;
					if(mc.gameSettings.keyBindJump.isKeyDown()) {
						event.setY(mc.thePlayer.motionY = event.getY() - 1);
					}
				} else {
					speed -= speed / 159;
				}

				if(!MovementUtils.isMoving() || mc.thePlayer.isCollidedHorizontally) {
					speed = MovementUtils.getBaseMoveSpeed();
				}

				speed = Math.max(speed, MovementUtils.getBaseMoveSpeed());
				MovementUtils.strafe(event, speed);
				break;
			case "Watchdog":
				switch (watchdogMode.getMode()) {
					case "StrafeLess":
						if(mc.thePlayer.onGround) {
							prevOnGround = true;
							if(MovementUtils.isMoving()) {
								event.setY(MovementUtils.JUMP_MOTION);
								mc.thePlayer.motionY = 0.42;
								
								double jumpSpeed = 0.2;
								
								if(mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
									jumpSpeed += 0.03 + mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() * 0.03;
								}
								
								double jumpSpeed2 = 0.23;
								
								if(mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
									jumpSpeed2 += 0.085 + mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() * 0.065;
								}
								
								MovementUtils.strafe(event, Math.max(MovementUtils.getBaseMoveSpeed() + jumpSpeed2, MovementUtils.getHorizontalMotion() + jumpSpeed));
								//MovementUtils.strafe(event, MovementUtils.getBaseMoveSpeed() + jumpSpeed);
							}
						} else {
							if(prevOnGround) {
								double mult = 1;
								
								if(mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
									mult += 0.04 + mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() * 0.04;
								}
								
								event.setX(mc.thePlayer.motionX *= mult);
								event.setZ(mc.thePlayer.motionZ *= mult);
								
								prevOnGround = false;
							}
							if(Math.round(event.getY() * 1000) / 1000.0 == 0.165 && fastFall.isEnabled()) {
								event.setY(mc.thePlayer.motionY = 0);
							}
							
							if(fastFall.isEnabled() && Math.abs(event.getY()) < 0.005) {
								//event.setY(mc.thePlayer.motionY = -0.16);
							}
							
							if(!mc.thePlayer.isSprinting()) {
								float f = MovementUtils.getPlayerDirection() * 0.017453292F;
					            event.setX(mc.thePlayer.motionX - MathHelper.sin(f) * 0.026F);
					            event.setZ(mc.thePlayer.motionZ + MathHelper.cos(f) * 0.026F);
							}
						}
						
						if(mc.thePlayer.hurtTime > 8) {
							ticks = 0;
						}
						
						if(mc.thePlayer.hurtTime > 8 && boostOnDamage.isEnabled()) {
							MovementUtils.strafe(event, MovementUtils.getHorizontalMotion() + 0.1);
						} else if(++ticks < strafeTicksAfterHit.getCurrentValue()) {
							MovementUtils.strafe(event, MovementUtils.getHorizontalMotion());
						} else {
							if(!mc.thePlayer.onGround && WorldUtil.isBlockUnder()) {
								//MovementUtils.strafe(event, MovementUtils.getHorizontalMotion());
							}
						}
						break;
				}
				break;
			case "ColdPvP":
				if(ticks == 0 && started) {
					MovementUtils.strafe(event, coldpvpSpeed.getCurrentValue());
					started = false;
					event.setY(MovementUtils.JUMP_MOTION);
					mc.thePlayer.motionY = 0.42;
					ticks++;
				} else {
					MovementUtils.strafe(event);
				}
				break;
				/*
			case "Watchdog":
				switch (watchdogMode.getMode()) {
					case "Test":
						if(mc.thePlayer.onGround) {
							prevOnGround = true;
							if(MovementUtils.isMoving()) {
								event.setY(MovementUtils.JUMP_MOTION);
								mc.thePlayer.motionY = 0.42;
								speed += 0.2;
							}
						} else if(prevOnGround) {
							speed *= 0.7;
							prevOnGround = false;
						} else {
							speed -= speed / 100;
						}
						
						this.strafeHypixel(event, speed, 15);
						break;
				}
				break;
				*/
		}
	}
	
	@Listener
	public void onMotion(MotionEvent event) {
		switch (mode.getMode()) {
			case "Watchdog":
				switch (watchdogMode.getMode()) {
					case "Strafe":
						if(hogRider.isEnabled()) {
							PacketUtil.sendPacket(new C0BPacketEntityAction(mc.thePlayer, Action.RIDING_JUMP));
						}
						
						if(mc.thePlayer.onGround) {
							if(MovementUtils.isMoving()) {
								mc.thePlayer.motionY = 0.42;
								
								double jumpSpeed = 0.22;
								
								if(mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
									jumpSpeed += 0.03 + mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() * 0.03;
								}
								
								MovementUtils.strafe(Math.min(MovementUtils.getHorizontalMotion() + jumpSpeed, MovementUtils.getBaseMoveSpeed() + 0.2));
							}
						} else {
							if(fastFall.isEnabled() && Math.abs(mc.thePlayer.motionY) < 0.005) {
								mc.thePlayer.motionY = -0.16;
							}
							
							MovementUtils.strafe(MovementUtils.getHorizontalMotion());
						}
						
						if(mc.thePlayer.hurtTime > 8 && boostOnDamage.isEnabled()) {
							MovementUtils.strafe(MovementUtils.getHorizontalMotion() + 0.13);
						}
						
						if(noYawUpdate.isEnabled()) {
							event.setYaw(mc.thePlayer.rotationYawHead = mc.thePlayer.renderYawOffset = direction);
						}
						//event.setYaw(mc.thePlayer.rotationYawHead = mc.thePlayer.renderYawOffset = mc.thePlayer.rotationYaw + mc.thePlayer.ticksExisted % 3 * 120);
						break;
					case "Ground":
						if(mc.thePlayer.onGround) {
							double distX = mc.thePlayer.posX - mc.thePlayer.lastTickPosX;
							double distZ = mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ;
							
							for(int i = 0; i < (int) hypixelSpeed.getCurrentValue(); i++) {
								PacketUtil.sendPacket(new C04PacketPlayerPosition(event.getX(), event.getY(), event.getZ(), true));
								
								if(mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX + distX * 3, mc.thePlayer.posY, mc.thePlayer.posZ + distZ * 3)).getBlock() instanceof BlockAir) {
									mc.thePlayer.setPosition(mc.thePlayer.posX + distX, mc.thePlayer.posY, mc.thePlayer.posZ + distZ);
									event.setX(mc.thePlayer.posX);
									event.setZ(mc.thePlayer.posZ);
								}
							}
						}
						break;
					case "StrafeLess":
						if(!mc.thePlayer.onGround && WorldUtil.isBlockUnder() && mc.thePlayer.fallDistance < 0.3) {
							//event.setOnGround(mc.thePlayer.ticksExisted % 2 == 0);
							//this.strafeHypixel(MovementUtils.getHorizontalMotion());
						}
						break;
				}
				break;
			case "ColdPvP":
				if(ticks >= coldpvpTPFreq.getCurrentValue()) {
					if(mc.thePlayer.onGround) {
						ticks = 0;
						event.setY(event.getY() - 0.08);
					}
				} else if(ticks > 0) {
					ticks++;
				}
				break;
		}
	}
	
	private void strafeHypixel(MoveEvent event, double speed, float change) {
		float direction = (float) Math.toRadians(this.getHypixelDirection(change));
		if (MovementUtils.isMoving()) {
			//mc.thePlayer.motionX = -Math.sin(direction) * speed;
			//mc.thePlayer.motionZ = Math.cos(direction) * speed;
			event.setX(mc.thePlayer.motionX = -Math.sin(direction) * speed);
			event.setZ(mc.thePlayer.motionZ = Math.cos(direction) * speed);
		} else {
			//mc.thePlayer.motionX = 0;
			//mc.thePlayer.motionZ = 0;
			event.setX(mc.thePlayer.motionX = 0);
			event.setZ(mc.thePlayer.motionZ = 0);
		}
	}
	
	private void strafeHypixel(double speed) {
		//float direction = (float) Math.toRadians(getHypixelDirection());
		float direction = (float) Math.toRadians(getHypixelDirection());
		float diff = Math.abs(lastDirection - this.direction);
		
		if (MovementUtils.isMoving()) {
			mc.thePlayer.motionX = -Math.sin(direction) * speed;
			mc.thePlayer.motionZ = Math.cos(direction) * speed;
		} else {
			mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
		}
		
		if(diff >= 30 && diff <= 330 && !mc.thePlayer.onGround) {
			//this.speed *= 0.75;
			//MovementUtils.motionMult(0.75);
		}
	}
	
	public float getHypixelDirection() {
		lastDirection = direction;
		//direction = MovementUtils.getPlayerDirection();
		
	    direction = mc.thePlayer.rotationYaw;
	    
	    if (mc.thePlayer.moveStrafing > 0) {
    		direction -= 45;
    	} else if (mc.thePlayer.moveStrafing < 0) {
    		direction += 45;
    	}
	    
	    return direction;
	}
	
	private float getHypixelDirection(float change) {
		float direction = MovementUtils.getPlayerDirection();
		
		float diff = Math.abs(direction - lastDirection);

		if(diff >= 20 && diff <= 340) {
			if(diff > 180) {
				if(direction > lastDirection) {
					direction = lastDirection + (360 - change);
				} else {
					direction = lastDirection - (360 - change);
				}
			} else {
				if(direction > lastDirection) {
					direction = lastDirection + change;
				} else {
					direction = lastDirection - change;
				}
			}
		}
		
		this.lastDirection = direction;
		return direction;
	}
	
}