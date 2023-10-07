package vestige.impl.module.movement;

import java.util.ArrayList;

import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockSlime;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.BlockPos;
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
import vestige.util.network.ServerUtils;
import vestige.util.player.DamageUtil;

@ModuleInfo(name = "Fly", category = Category.MOVEMENT)
public class Fly extends Module {
	
	public final ModeSetting mode = new ModeSetting("Mode", this, "Vanilla", "Vanilla", "Creative", "Airwalk", "Airjump", "Hypixel", "Hypixel Smooth", "Hypixel fireball", "Redesky", "Blocksmc", "Funcraft", "BedwarsPractice", "ColdPvP", "ColdPvP2", "AGC", "Zonecraft", "Supercraft", "Verus", "VerusLatest dmg");
	
	private final NumberSetting vanillaFlySpeed = new NumberSetting("vanilla-fly-speed", this, 1, 0.1, 9, 0.1, false) {
		@Override
		public String getDisplayName() {
			return "Speed";
		}
		@Override
		public boolean isShown() {
			return mode.is("Vanilla");
		}
 	};
 	
 	private final BooleanSetting spoofGround = new BooleanSetting("vanilla-spoofground", this, false) {
 		@Override
		public String getDisplayName() {
			return "Spoofground";
		}
		@Override
		public boolean isShown() {
			return mode.is("Vanilla");
		}
 	};
 	
 	private final NumberSetting funcraftSpeed = new NumberSetting("funcraft-speed", this, 0.9, 0, 2, 0.05, false) {
 		@Override
		public String getDisplayName() {
			return "Speed";
		}
		@Override
		public boolean isShown() {
			return mode.is("Funcraft");
		}
 	};
 	
 	private final NumberSetting timerSpeed = new NumberSetting("timer-speed", this, 1, 0.1, 4, 0.1, false) {
 		@Override
		public String getDisplayName() {
			return "Timer";
		}
		@Override
		public boolean isShown() {
			return mode.is("Funcraft");
		}
 	};
 	
 	private final BooleanSetting timerBoost = new BooleanSetting("Timer boost", this, false) {
 		@Override
 		public boolean isShown() {
 			return mode.is("Zonecraft");
 		}
 	};
 	
 	private final ModeSetting verusMode = new ModeSetting("Verus Mode", this, "Collide", "Collide", "Damage", "Damage latest", "Damage safe", "Damage disabled") {
 		@Override
 		public boolean isShown() {
 			return mode.is("Verus");
 		}
 	};
 	
 	private final NumberSetting agcSpeed = new NumberSetting("agc-speed", this, 9, 0.6, 9, 0.2, false) {
 		@Override
		public String getDisplayName() {
			return "Speed";
		}
		@Override
		public boolean isShown() {
			return mode.is("AGC");
		}
 	};
	
 	private final ArrayList<BlockPos> blocks = new ArrayList<>();
 	private final ArrayList<Packet> packets = new ArrayList<>();
 	
 	private boolean started, done;
 	private int ticks;
 	
 	private double x, y, z;
 	private double lastMotionX, lastMotionY, lastMotionZ;
 	
 	private boolean placedBlock;
 	
 	private double speed;
 	
 	private boolean lagbacked;
 	
	public Fly() {
		this.registerSettings(mode, vanillaFlySpeed, spoofGround, agcSpeed, funcraftSpeed, verusMode, timerSpeed);
	}
	
	public void onEnable() {
		ticks = 0;
		started = done = false;
		placedBlock = false;
		lagbacked = false;
		packets.clear();
		
		speed = MovementUtils.getBaseMoveSpeed();
		
		x = mc.thePlayer.posX;
		y = mc.thePlayer.posY;
		z = mc.thePlayer.posZ;
		
		switch (mode.getMode()) {
			case "ColdPvP":
				mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 11, mc.thePlayer.posZ);
				break;
			case "Supercraft":
				if(mc.thePlayer.onGround) {
					mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 11, mc.thePlayer.posZ);
				} else {
					this.setEnabled(false);
					return;
				}
				break;
		}
	}
	
	public void onDisable() {
		mc.thePlayer.capabilities.isFlying = false;
		mc.timer.timerSpeed = 1F;
		mc.thePlayer.jumpMovementFactor = 0.02F;
		mc.thePlayer.speedInAir = 0.02F;
		
		switch (mode.getMode()) {
			case "Vanilla":
				MovementUtils.strafe(0);
				mc.thePlayer.motionY = 0;
				break;
			case "ColdPvP":
			case "ColdPvP2":
			case "Funcraft":
				MovementUtils.strafe(0.1);
				break;
			case "VerusLatest dmg":
				MovementUtils.strafe(MovementUtils.getBaseMoveSpeed());
				break;
			case "Hypixel":
				PacketUtil.sendBlocking(false, true);
				PacketUtil.releaseUseItem(false);
				break;
			case "Hypixel Smooth":
				MovementUtils.strafe(0);
				mc.thePlayer.motionY = -0.08;
				mc.thePlayer.setPosition(x, y, z);
				break;
		}
		
		if(!blocks.isEmpty()) {
			for(BlockPos pos : blocks) {
				mc.theWorld.setBlockToAir(pos);
			}
			blocks.clear();
		}
		
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
	public void onMove(MoveEvent event) {
		setSuffix(mode.getMode());
		switch (mode.getMode()) {
			case "Vanilla":
				MovementUtils.strafe(event, vanillaFlySpeed.getCurrentValue());
				if (mc.gameSettings.keyBindJump.isKeyDown()) {
					event.setY(vanillaFlySpeed.getCurrentValue());
				} else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
					event.setY(-vanillaFlySpeed.getCurrentValue());
				} else {
					event.setY(0);
				}
				
				mc.thePlayer.motionY = 0;
				break;
			case "Airwalk":
				event.setY(mc.thePlayer.motionY = 0);
				break;
			case "Blocksmc":
			case "Blocksmc2":
				if (started) {
					event.setY(mc.thePlayer.motionY = 0);
					//move.setSpeed(e, move.getBaseMoveSpeed() * 0.9);
					ticks++;
				} else {
					if (mc.thePlayer.onGround) {
						if (placedBlock) {
							event.setY(mc.thePlayer.motionY = 0);
							started = true;
						} else {
							event.setY(MovementUtils.JUMP_MOTION);
							mc.thePlayer.motionY = 0.42;
						}
					} else {
						boolean foundSlot = false;
						int itemSpoofed = 0;

						for (int i = 0; i < 9; i++) {
							if (mc.thePlayer.inventory.getStackInSlot(i) == null)
								continue;
							if (mc.thePlayer.inventory.getStackInSlot(i).getItem().getUnlocalizedName().contains("slime")) {
								itemSpoofed = i;
								foundSlot = true;
								break;
							}
						}
						
						if(mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ)).getBlock() instanceof BlockSlime) {
							placedBlock = true;
						} else {
							if(event.getY() < -0.3) {
								this.setEnabled(false);
								Vestige.getInstance().addChatMessage("You need to land on a slime to fly !");
								return;
							}
						}

						if(placedBlock) {
							MovementUtils.strafe(event, 0.01);
						}
					}
				}
				break;
			case "ColdPvP":
				if(started) {
					if(!done) {
						MovementUtils.strafe(event, 9);
						event.setY(mc.thePlayer.motionY = 1);
						//event.setY(MovementUtils.JUMP_MOTION);
						//mc.thePlayer.motionY = 0.42;
						done = true;
					} else {
						if(++ticks > 12) {
							event.setY(mc.thePlayer.motionY = 0);
						}
						MovementUtils.strafe(event);
					}
				}
				break;
			case "ColdPvP2":
				MovementUtils.strafe(event, 1.45);
				if(mc.gameSettings.keyBindJump.isKeyDown() && !MovementUtils.isMoving()) {
					event.setY(mc.thePlayer.motionY = 1);
				} else {
					event.setY(mc.thePlayer.motionY = 0);
				}
				break;
			case "Supercraft":
				if(started) {
					if(!done) {
						DamageUtil.vanillaDamage();
						done = true;
					}
					
					if(mc.thePlayer.hurtTime == 9) {
						MovementUtils.strafe(event, 9);
						event.setY(MovementUtils.JUMP_MOTION);
						mc.thePlayer.motionY = 0.42;
					} else if(mc.thePlayer.hurtTime == 8) {
						MovementUtils.strafe(event, 1.2);
					}
					
					if(mc.thePlayer.hurtTime == 5) {
						MovementUtils.strafe(event, MovementUtils.getBaseMoveSpeed());
						this.setEnabled(false);
						return;
					}
				}
				break;
			case "AGC":
				if (mc.thePlayer.hurtTime == 9) {
					MovementUtils.strafe(event, agcSpeed.getCurrentValue());
				} else {
					MovementUtils.strafe(event);
				}
				break;
			case "Hypixel fireball":
				if (mc.thePlayer.hurtTime == 9) {
					MovementUtils.strafe(event, 1);
					event.setY(mc.thePlayer.motionY = 9);
				}
				break;
			case "Funcraft":
				if(!started) {
					if(mc.thePlayer.onGround) {
						mc.thePlayer.jump();
						event.setY(0.41999998688698);
						MovementUtils.strafe(event, MovementUtils.getHorizontalMotion());
						speed = MovementUtils.getBaseMoveSpeed() + funcraftSpeed.getCurrentValue();
					} else {
						speed = MovementUtils.getBaseMoveSpeed();
					}
					started = true;
				} else {
					event.setY(mc.thePlayer.motionY = -1E-10);

					if(mc.thePlayer.isCollidedHorizontally || !MovementUtils.isMoving()) {
						speed = MovementUtils.getBaseMoveSpeed();
					}

					speed -= speed / 159;

					speed = Math.max(speed, MovementUtils.getBaseMoveSpeed());
					MovementUtils.strafe(event, speed);

					ticks++;
				}
				break;
			case "VerusLatest dmg":
				mc.thePlayer.setSprinting(true);
				
				if(ticks == 3) {
					DamageUtil.vanillaDamage();
				}
				
				if(ticks < 5) {
					event.setCancelled(true);
					speed = 9;
				} else if(ticks < 16) {
					if(ticks > 8) {
						speed *= 0.91;
						speed += 0.026;
					}
					MovementUtils.strafe(event, speed);
				} else if(ticks == 16) {
					MovementUtils.strafe(event, Math.min(speed, 1));
				} else if(ticks == 22) {
					MovementUtils.strafe(event, MovementUtils.getBaseMoveSpeed());
					this.setEnabled(false);
					return;
				}
				
				if(ticks == 5) {
					event.setY(mc.thePlayer.motionY = 1);
				} else if(ticks >= 10 && ticks < 16) {
					if(ticks % 2 == 0) {
						event.setY(MovementUtils.JUMP_MOTION);
						mc.thePlayer.motionY = 0.42;
					}
				}
				
				ticks++;
				break;
			case "Hypixel":
				event.setCancelled(true);
				mc.thePlayer.motionX = mc.thePlayer.motionY = mc.thePlayer.motionZ = 0;
				
				mc.thePlayer.setSprinting(false);
				
				if(ticks % 21 == 2) {
					float direction = (float) Math.toRadians(mc.thePlayer.rotationYaw);
					double dist = 7.9;
					
					mc.thePlayer.setPosition(mc.thePlayer.posX - Math.sin(direction) * dist, mc.thePlayer.posY - 1.8, mc.thePlayer.posZ + Math.cos(direction) * dist);
				}
				ticks++;
				break;
			case "Hypixel Smooth":
				/*
				if(ticks % 21 > 2 && ticks % 21 < 20) {
					MovementUtils.strafe(event, 0.4647);
				} else {
					MovementUtils.strafe(event, 0);
				}
				*/
				
				if(ticks % 21 != 0) {
					MovementUtils.strafe(event, 0.395);
				} else {
					MovementUtils.strafe(event, 0);
				}
				
				if(ticks > 0 && ticks % 21 == 0) {
					event.setY(-1.8);
				} else {
					event.setY(mc.thePlayer.motionY = 0);
				}
				
				//event.setY(-1.8 / 20);
				//mc.thePlayer.motionY = 0;
				
				ticks++;
				break;
			case "Verus":
				switch (verusMode.getMode()) {
					case "Collide":
						mc.thePlayer.setSprinting(true);
						if(mc.thePlayer.onGround) {
							event.setY(MovementUtils.JUMP_MOTION);
							mc.thePlayer.motionY = 0.42;
							ticks = 0;
							
							if(!started) {
								speed = MovementUtils.getBaseMoveSpeed() + 0.15;
								started = true;
							} else {
								speed = MovementUtils.getBaseMoveSpeed() + 0.32;
							}
						} else if(ticks == 1) {
							event.setY(mc.thePlayer.motionY = 0);
							if(!started) {
								speed *= 0.75;
							} else {
								speed *= 0.7;
							}
						} else if(ticks < 5) {
							event.setY(mc.thePlayer.motionY = 0);
							speed -= speed / 30;
						}
						
						MovementUtils.strafe(event, speed);
						ticks++;
						break;
				}
				break;
			case "Test":
				if(!mc.thePlayer.onGround) {
					if(!started) {
						event.setY(mc.thePlayer.motionY = -0.14);
						started = true;
					} else {
						event.setY(mc.thePlayer.motionY = 0);
						//MovementUtils.strafe(event, 0.2);
					}
				}
				break;
		}
	}
	
	@Listener
	public void onMotion(MotionEvent event) {
		switch (mode.getMode()) {
			case "Verus":
				switch (verusMode.getMode()) {
					case "Collide":
						if(ticks > 0 && ticks < 5) {
							event.setOnGround(true);
						}
						break;
				}
				break;
			case "Vanilla":
				if(spoofGround.isEnabled()) {
					event.setOnGround(true);
				}
				break;
			case "Airwalk":
				event.setOnGround(true);
				break;
			case "ColdPvP":
				if(ticks > 12) {
					event.setOnGround(true);
				}
				break;
			case "ColdPvP2":
				event.setOnGround(ticks % 20 != 0);
				break;
			case "Blocksmc":
				if(started) {
					mc.thePlayer.onGround = true;
					event.setOnGround(true);
				}
				break;
			case "Blocksmc2":
				if(started && !mc.thePlayer.onGround) {
					PacketUtil.verusRightClick();
				}
				break;
			case "Funcraft":
				event.setOnGround(true);
				break;
			case "Zonecraft":
				event.setYaw(MovementUtils.getPlayerDirection());
				break;
			case "Hypixel":
				//event.setOnGround(true);
				break;
		}
	}
	
	@Listener
	public void onUpdate(UpdateEvent event) {
		switch (mode.getMode()) {
			case "Creative":
				mc.thePlayer.capabilities.isFlying = true;
				break;
			case "Airwalk":
				mc.thePlayer.onGround = true;
				break;
			case "ColdPvP":
				if(ticks > 12) {
					mc.thePlayer.onGround = true;
				}
				break;
			case "Redesky":
				if (mc.thePlayer.onGround) {
					mc.thePlayer.motionY = 0.4;
					mc.thePlayer.jump();
				}
				
				//mc.thePlayer.speedInAir = 0.11F;
				mc.thePlayer.jumpMovementFactor = 0.13F;
				if (mc.thePlayer.ticksExisted % 3 != 0) {
					mc.timer.timerSpeed = 15F;
				} else {
					mc.timer.timerSpeed = 0.35F;
				}
				break;
			case "Blocksmc":
				mc.timer.timerSpeed = 1.1F;
				break;
			case "Airjump":
				if(mc.gameSettings.keyBindJump.isKeyDown() ? mc.thePlayer.fallDistance > 0 : mc.thePlayer.fallDistance > 0.8) {
					BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
					if(mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) {
						if(!blocks.isEmpty()) {
							for(BlockPos pos1 : blocks) {
								mc.theWorld.setBlockToAir(pos1);
							}
							blocks.clear();
						}

						mc.theWorld.setBlockState(pos, Blocks.barrier.getDefaultState());
						blocks.add(pos);
					}
				}
				if(mc.thePlayer.onGround) {
					if(!mc.gameSettings.keyBindJump.isKeyDown()) {
						mc.thePlayer.jump();
					}
				}
				break;
			case "BedwarsPractice":
				if(!mc.gameSettings.keyBindJump.isKeyDown() || mc.thePlayer.fallDistance > 0.6) {
					BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
					if(mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) {
						if(!blocks.isEmpty()) {
							for(BlockPos pos1 : blocks) {
								mc.theWorld.setBlockToAir(pos1);
							}
							blocks.clear();
						}

						mc.theWorld.setBlockState(pos, Blocks.barrier.getDefaultState());
						blocks.add(pos);
					}
				}
				break;
			case "Verus":
				switch (verusMode.getMode()) {
					case "Collide":
						BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
						if(mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) {
							if(!blocks.isEmpty()) {
								for(BlockPos pos1 : blocks) {
									mc.theWorld.setBlockToAir(pos1);
								}
								blocks.clear();
							}

							mc.theWorld.setBlockState(pos, Blocks.barrier.getDefaultState());
							blocks.add(pos);
						}
						break;
				}
				break;
			case "Funcraft":
				//ticks++;
				//mc.timer.timerSpeed = (float) Math.max(2 - (ticks * 0.75 / 75), 1);
				mc.timer.timerSpeed = (float) timerSpeed.getCurrentValue();
				break;
			case "Zonecraft":
				if(!started) {
					if(mc.thePlayer.onGround) {
						if(!mc.gameSettings.keyBindJump.isKeyDown()) {
							mc.thePlayer.jump();
						}
						mc.thePlayer.motionY = 0.42;
					}
					started = true;
				} else {
					if(mc.thePlayer.hurtTime == 10 || mc.thePlayer.posY % 0.0625D == 0) {
						this.setEnabled(false);
						return;
					}
					
					mc.thePlayer.motionY = 0;
				}
				
				if(timerBoost.isEnabled()) {
					if(ticks % 20 > 1 && ticks % 20 < 19) {
						mc.timer.timerSpeed = 4F;
					} else {
						mc.timer.timerSpeed = 0.2F;
					}
				} else {
					mc.timer.timerSpeed = 1F;
				}
				
				ticks++;
				break;
			case "Jartex":
				if (!started) {
					if (mc.thePlayer.onGround) {
						if (!mc.gameSettings.keyBindJump.isKeyDown()) {
							mc.thePlayer.jump();
						}
					} else {
						if (mc.thePlayer.motionY < -0.1) {
							mc.thePlayer.motionY = 1.4;
							MovementUtils.strafe(1);
							started = true;
						}
					}
				} else {
					ticks++;
					
					if(ticks == 22) {
						//ticks = 0;
						//mc.thePlayer.motionY = 0.95;
						//MovementUtils.strafe(0.95);
						//lagbacked = false;
					}
				}
				break;
		}
	}
	
	@Listener
	public void onReceive(PacketReceiveEvent event) {
		switch (mode.getMode()) {
			case "Redesky":
				if (event.getPacket() instanceof S12PacketEntityVelocity) {
					S12PacketEntityVelocity packet = (S12PacketEntityVelocity) event.getPacket();
					
					if (packet.getEntityID() == mc.thePlayer.getEntityId()) {
						event.setCancelled(true);
					}
				}
				break;
			case "BedwarsPractice":
				if (event.getPacket() instanceof S12PacketEntityVelocity) {
					S12PacketEntityVelocity packet = (S12PacketEntityVelocity) event.getPacket();
					
					if (packet.getEntityID() == mc.thePlayer.getEntityId()) {
						this.setEnabled(false);
						return;
					}
				}
				break;
			case "Supercraft":
			case "ColdPvP":
				if (event.getPacket() instanceof S08PacketPlayerPosLook) {
					started = true;
				}
				break;
			case "VerusLatest dmg":
				if (event.getPacket() instanceof S12PacketEntityVelocity) {
					S12PacketEntityVelocity packet = (S12PacketEntityVelocity) event.getPacket();
					
					if (packet.getEntityID() == mc.thePlayer.getEntityId()) {
						event.setCancelled(true);
					}
				}
				break;
			case "Jartex":
				if (event.getPacket() instanceof S08PacketPlayerPosLook && mc.getNetHandler().doneLoadingTerrain) {
					S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.getPacket();
					lagbacked = true;
					lastMotionX = mc.thePlayer.motionX;
					lastMotionY = mc.thePlayer.motionY;
					lastMotionZ = mc.thePlayer.motionZ;
				}
				break;
			case "ColdPvP2":
				if (event.getPacket() instanceof S08PacketPlayerPosLook) {
					S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.getPacket();
					
					if(mc.thePlayer.getDistance(packet.getX(), packet.getY(), packet.getZ()) < 8) {
						event.setCancelled(true);
						PacketUtil.sendPacketNoEvent(new C06PacketPlayerPosLook(packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch(), false));
					}
				}
				break;
		}
	}
	
	@Listener
	public void onSend(PacketSendEvent event) {
		switch (mode.getMode()) {
			case "Blocksmc":
				if(started && mc.thePlayer != null && mc.thePlayer.ticksExisted > 100) {
					event.setCancelled(true);
					packets.add(event.getPacket());
				}
				break;
			case "VerusLatest dmg":
				if(event.getPacket() instanceof C03PacketPlayer) {
					if(ticks < 4) {
						event.setCancelled(true);
					}
				}
				break;
			case "Jartex":
				if (event.getPacket() instanceof C06PacketPlayerPosLook) {
					if (lagbacked) {
						mc.thePlayer.motionX = lastMotionX;
						mc.thePlayer.motionY = lastMotionY;
						mc.thePlayer.motionZ = lastMotionZ;
						lagbacked = false;
					}
				}
				break;
			case "ColdPvP2":
				if (event.getPacket() instanceof C03PacketPlayer) {
					C03PacketPlayer packet = (C03PacketPlayer) event.getPacket();
					if(mc.thePlayer.onGround) {
						if(ticks % 8 == 0) {
							packet.y -= 0.08;
						}
					} else {
						if(ticks % 24 == 0) {
							packet.y -= 12 - Math.random();
						}
					}
					ticks++;
				}
				break;
			case "Hypixel":
				break;
			case "Hypixel Smooth":
				if (event.getPacket() instanceof C03PacketPlayer) {
					//C03PacketPlayer packet = (C03PacketPlayer) event.getPacket();
					
					if(ticks % 21 == 2) {
						event.setCancelled(true);
						PacketUtil.sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.lastTickPosX, mc.thePlayer.lastTickPosY, mc.thePlayer.lastTickPosZ, false));
					} else if(ticks % 21 != 1) {
						event.setCancelled(true);
						//PacketUtil.sendPacketNoEvent(new C03PacketPlayer(false));
					} else {
						if(ticks > 5) {
							x = mc.thePlayer.posX;
							y = mc.thePlayer.posY;
							z = mc.thePlayer.posZ;
							
							event.setCancelled(true);
							
							PacketUtil.sendPacketNoEvent(new C04PacketPlayerPosition(x, y, z, false));
						}
					}
				}
				break;
		}
	}
	
}
