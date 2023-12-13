package vestige.impl.module.world;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import vestige.Vestige;
import vestige.api.event.Listener;
import vestige.api.event.impl.MotionEvent;
import vestige.api.event.impl.MoveEvent;
import vestige.api.event.impl.PacketSendEvent;
import vestige.api.event.impl.PostMotionEvent;
import vestige.api.event.impl.UpdateEvent;
import vestige.api.module.Category;
import vestige.api.module.Module;
import vestige.api.module.ModuleInfo;
import vestige.api.setting.impl.BooleanSetting;
import vestige.api.setting.impl.ModeSetting;
import vestige.api.setting.impl.NumberSetting;
import vestige.impl.module.movement.Speed;
import vestige.util.movement.MovementUtils;
import vestige.util.network.PacketUtil;
import vestige.util.world.BlockUtil;
import vestige.util.world.WorldUtil;

@ModuleInfo(name = "Scaffold", category = Category.WORLD, key = Keyboard.KEY_H)
public class Scaffold extends Module {
	
	private final ModeSetting mode = new ModeSetting("Mode", this, "Normal", "Normal", "Hypixel", "Hypixel2", "Verus", "Redesky", "Redesky2", "Custom");
	private final ModeSetting tower = new ModeSetting("Tower", this, "Vanilla", "Vanilla", "NCP", "NCP2", "Verus", "Hypixel", "Test", "None");
	
	private final NumberSetting vanillaTowerMotion = new NumberSetting("vanilla-tower-motion", this, 0.42, 0.1, 1, 0.05, false) {
		@Override
		public boolean isShown() {
			return tower.is("Vanilla");
		}
		@Override
		public String getDisplayName() {
			return "Tower Motion";
		}
	};
	
	private final NumberSetting timer = new NumberSetting("Timer", this, 1, 0.8, 2.5, 0.05, false);
	private final NumberSetting towerTimer = new NumberSetting("Tower Timer", this, 1, 0.8, 2.5, 0.05, false);
	
	private final BooleanSetting hypixelSprint = new BooleanSetting("hypixel-sprint", this, false) {
		@Override
		public boolean isShown() {
			return mode.is("Hypixel") || mode.is("Hypixel2");
		}
		@Override
		public String getDisplayName() {
			return "Sprint";
		}
	};
	
	private final BooleanSetting jump = new BooleanSetting("Jump", this, false) {
		@Override
		public boolean isShown() {
			return mode.is("Normal") || mode.is("Verus") || mode.is("Custom");
		}
	};
	
	private final ModeSetting hypixelPlaceTiming = new ModeSetting("hypixel-place-timing", this, "Pre", "Pre", "Post") {
		@Override
		public boolean isShown() {
			return mode.is("Hypixel");
		}
		@Override
		public String getDisplayName() {
			return "Place timing";
		}
	};
	
	private final ModeSetting rotations = new ModeSetting("Rotations", this, "Normal", "Normal", "Smooth", "BlockYaw", "AAC", "Back", "Sideaways", "Down", "None") {@Override public boolean isShown() { return mode.is("Custom"); }};
	private final NumberSetting rotationTurnSpeed = new NumberSetting("Rotation turn speed", this, 180, 5, 180, 5, true) {@Override public boolean isShown() { return mode.is("Custom"); }};
	private final NumberSetting rotationRandomisation = new NumberSetting("Rotation randomisation", this, 0, 0, 10, 0.1, false) {@Override public boolean isShown() { return mode.is("Custom"); }};
	private final ModeSetting noSprint = new ModeSetting("No Sprint", this, "Disabled", "Disabled", "Enabled", "Spoof") {@Override public boolean isShown() { return mode.is("Custom"); }};
	private final ModeSetting blockUpdateTiming = new ModeSetting("Block update timing", this, "Update", "Update", "Pre", "Post") {@Override public boolean isShown() { return mode.is("Custom"); }};
	private final ModeSetting placementTiming = new ModeSetting("Placement timing", this, "Update", "Update", "Pre", "Post") {@Override public boolean isShown() { return mode.is("Custom"); }};
	private final NumberSetting negativeExpand = new NumberSetting("Negative Expand", this, 0, 0, 0.24, 0.01, false) {@Override public boolean isShown() { return mode.is("Custom"); }};
	private final NumberSetting motionMult = new NumberSetting("Motion Mult", this, 1, 0, 1.5, 0.1, false) {@Override public boolean isShown() { return mode.is("Custom"); }};
	private final BooleanSetting sneak = new BooleanSetting("Sneak", this, false) {@Override public boolean isShown() { return mode.is("Custom"); }};
	private final BooleanSetting safewalk = new BooleanSetting("Safewalk", this, true) {@Override public boolean isShown() { return mode.is("Custom"); }};
	private final BooleanSetting pulseBlink = new BooleanSetting("Pulse Blink", this, false) {@Override public boolean isShown() { return mode.is("Custom"); }};
	private final BooleanSetting additionalRightClicks = new BooleanSetting("Additional right clicks", this, false) {@Override public boolean isShown() { return mode.is("Custom"); }};
	private final BooleanSetting ignoreSpeedPot = new BooleanSetting("Ignore Speed Pot", this, false) {@Override public boolean isShown() { return mode.is("Custom"); }};
	
	private BlockPos pos, lastPos;
	private EnumFacing facing, lastFacing;
	
	private float blockYaw;
	private float yaw, pitch;
	
	private int slot, oldSlot;
	private boolean changedSlot;
	
	private boolean wasAirUnder;
	
	private int ticks, towerTicks;
	
	private final ArrayList<Packet> packets = new ArrayList<>();
	
	private double lastY;
	
	public Scaffold() {
		this.registerSettings(mode);
		this.registerSettings(rotations, rotationTurnSpeed, rotationRandomisation, noSprint, blockUpdateTiming, placementTiming, negativeExpand, motionMult, sneak, safewalk, pulseBlink, additionalRightClicks, ignoreSpeedPot);
		this.registerSettings(hypixelSprint, hypixelPlaceTiming, jump, timer, tower, towerTimer, vanillaTowerMotion);
	}
	
	public void onEnable() {
		oldSlot = mc.thePlayer.inventory.currentItem;
		
		ticks = towerTicks = 0;
		
		lastY = mc.thePlayer.posY;
		
		if(mode.is("Hypixel") || mode.is("Hypixel2")) {
			MovementUtils.motionMult(0.4);
		} else if(mode.is("Custom") && noSprint.is("Spoof")) {
			if(mc.thePlayer.isSprinting()) {
				PacketUtil.sendPacketNoEvent(new C0BPacketEntityAction(mc.thePlayer, Action.STOP_SPRINTING));
			}
		}
		
		yaw = MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw);
		pitch = mc.thePlayer.rotationPitch;
		
		pos = lastPos = null;
		facing = lastFacing = null;
	}
	
	public void onDisable() {
		this.switchToOriginalSlot();
		mc.timer.timerSpeed = 1F;
		
		mc.gameSettings.keyBindSneak.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode());
		
		this.sendPackets();
		
		if(mode.is("Custom") && noSprint.is("Spoof")) {
			if(mc.thePlayer.isSprinting()) {
				PacketUtil.sendPacketNoEvent(new C0BPacketEntityAction(mc.thePlayer, Action.START_SPRINTING));
			}
		}
	}
	
	private void sendPackets() {
		if(!packets.isEmpty()) {
			for(Packet p : packets) {
				PacketUtil.sendPacketNoEvent(p);
			}
			packets.clear();
		}
	}
	
	public void tower() {
		switch (tower.getMode()) {
			case "Vanilla":
				if(mc.gameSettings.keyBindJump.isKeyDown()) {
					if(!(mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ)).getBlock() instanceof BlockAir)) {
						mc.thePlayer.motionY = vanillaTowerMotion.getCurrentValue();
					}
				} else {
					mc.thePlayer.motionY = Math.min(mc.thePlayer.motionY, 0.34);
				}
				break;
			case "Verus":
				if(mc.gameSettings.keyBindJump.isKeyDown()) {
					if(!(mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.2, mc.thePlayer.posZ)).getBlock() instanceof BlockAir)) {
						mc.thePlayer.motionY = 0.42;
					}
				}
				break;
			case "Hypixel":
				if (mc.thePlayer.onGround && MovementUtils.isMoving() && mc.gameSettings.keyBindJump.isKeyDown()) {
                    mc.thePlayer.motionX *= 0.8;
                    mc.thePlayer.motionZ *= 0.8;
                    //mc.thePlayer.jump();
               }
				break;
			case "Test":
				if(mc.gameSettings.keyBindJump.isKeyDown()) {
					if(!(mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.2, mc.thePlayer.posZ)).getBlock() instanceof BlockAir)) {
						if(mc.thePlayer.onGround) {
							mc.thePlayer.motionY = 0.42;
						} else {
							if(Math.abs(mc.thePlayer.motionY) < 0.005) {
								mc.thePlayer.motionY = -0.16;
							}
						}
					}
				}
				break;
			case "NCP":
				if(mc.thePlayer.onGround) {
					towerTicks = 0;
				}
				
				if(mc.gameSettings.keyBindJump.isKeyDown() && !MovementUtils.isMoving() && !(mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.1, mc.thePlayer.posZ)).getBlock() instanceof BlockAir)) {
					int IntPosY = (int) mc.thePlayer.posY;
					if(mc.thePlayer.posY - IntPosY < 0.05) {
						mc.thePlayer.setPosition(mc.thePlayer.posX, IntPosY, mc.thePlayer.posZ);
						mc.thePlayer.motionY = 0.42;
						towerTicks = 1;
					} else if(towerTicks == 1) {
						mc.thePlayer.motionY = 0.34;
						towerTicks++;
					} else if(towerTicks == 2) {
						mc.thePlayer.motionY = 0.25;
						towerTicks++;
					}
				}
				break;
		}
	}
	
	@Listener
	public void onMove(MoveEvent event) {
		if (tower.is("NCP2")) {
			if(mc.gameSettings.keyBindJump.isKeyDown()) {
				if(mc.thePlayer.onGround) {
					event.setY(MovementUtils.JUMP_MOTION);
					mc.thePlayer.motionY = 0.42;
					towerTicks = 0;
				} else if(towerTicks == 2) {
					mc.thePlayer.setPosition(mc.thePlayer.posX, Math.round(mc.thePlayer.posY), mc.thePlayer.posZ);
					event.setY(mc.thePlayer.motionY = 0);
					mc.thePlayer.onGround = true;
				}
			}
			towerTicks++;
		}
	}
	
	@Listener
	public void onUpdate(UpdateEvent event) {
		if(mc.thePlayer.ticksExisted < 5) {
			this.setEnabled(false);
			return;
		}
		
		this.setSuffix(mode.getMode());
		this.pickBlock();
		this.tower();
		
		switch (mode.getMode()) {
			case "Normal":
			case "Verus":
			case "Custom":
				if(mc.thePlayer.onGround && !mc.gameSettings.keyBindJump.isKeyDown() && !Vestige.getInstance().getModuleManager().getModule(Speed.class).isEnabled() && jump.isEnabled()) {
					mc.thePlayer.jump();
					mc.thePlayer.motionY = Math.min(mc.thePlayer.motionY, 0.42);
				}
				break;
		}
		
		if(mode.is("Custom")) {
			if(noSprint.is("Enabled")) {
				mc.thePlayer.setSprinting(false);
			}
			
			if(blockUpdateTiming.is("Update")) {
				this.updateCustomBlockPos();
			}
			
			if(placementTiming.is("Update")) {
				this.placeCustom();
			}
			
			if (ignoreSpeedPot.isEnabled() && mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
				double mult = 1 - ((mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1) * 0.167);
				mc.thePlayer.motionX *= mult;
				mc.thePlayer.motionZ *= mult;
			}
		}
		
		switch (mode.getMode()) {
			case "Normal":
				mc.timer.timerSpeed = (float) timer.getCurrentValue();
				
				if(isAirUnder()) {
					setBlockFacing(getBlockPosUnder());
					
					if(pos != null && facing != null) {
						float rots[] = BlockUtil.getDirectionToBlock(pos.getX(), pos.getY(), pos.getZ(), facing);
						
						yaw = rots[0];
						pitch = rots[1];
						
						placeBlock(0);
					} else {
						yaw = MovementUtils.getPlayerDirection() - 180;
					}
				} else {
					yaw = MovementUtils.getPlayerDirection() - 180;
				}
				break;
			case "Verus":
				mc.timer.timerSpeed = (float) timer.getCurrentValue();
				
				if(isAirUnder()) {
					setBlockFacing(getBlockPosUnder());
					
					if(pos != null && facing != null) {
						placeBlock(0);
					}
				}
				break;
			case "Hypixel":
				if(!hypixelSprint.isEnabled() || (Math.abs(mc.thePlayer.motionX) > 0.04 && Math.abs(mc.thePlayer.motionZ) > 0.04)) {
					mc.thePlayer.setSprinting(false);
				}
				
				if(!isAirUnder() && mc.thePlayer.onGround) {
					mc.timer.timerSpeed = (float) timer.getCurrentValue();
				} else {
					if(timer.getCurrentValue() > 1.5) {
						mc.timer.timerSpeed = 0.85F;
					} else {
						mc.timer.timerSpeed = 0.96F;
					}
				}
				
				if(isAirUnder()) {
					setBlockFacing(getBlockPosUnder());
					
					if(hypixelSprint.isEnabled()) {						
						if(!(Math.abs(mc.thePlayer.motionX) > 0.08 && Math.abs(mc.thePlayer.motionZ) > 0.08)) {
							MovementUtils.motionMult(0.8);
						}
					}
					
					if(pos != null && facing != null) {
						if(!mc.gameSettings.keyBindSneak.isKeyDown() && mc.thePlayer.onGround) {
							PacketUtil.startSneaking();
						}
						
						if(hypixelPlaceTiming.is("Pre")) {
							placeBlock(MovementUtils.isGoingDiagonally() ? 0.2 : 0.12);
						}
					}
				}
				
				if(Math.abs(mc.thePlayer.motionX) > 0.08 && Math.abs(mc.thePlayer.motionZ) > 0.08) {
					MovementUtils.motionMult(0.85);
				}
				
				if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
					double mult = 1 - ((mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1) * 0.167);
					mc.thePlayer.motionX *= mult;
					mc.thePlayer.motionZ *= mult;
				}
				break;
			case "Hypixel jump":
				//mc.thePlayer.setSprinting(false);
				
				if(mc.thePlayer.onGround && !mc.gameSettings.keyBindJump.isKeyDown() && !Vestige.getInstance().getModuleManager().getModule(Speed.class).isEnabled()) {
					mc.thePlayer.jump();
					mc.thePlayer.motionY = 0.42;
				}
				
				mc.timer.timerSpeed = (float) timer.getCurrentValue();
				
				if(isAirUnder()) {
					mc.timer.timerSpeed = 1F;
					setBlockFacing(getBlockPosUnder());
					
					mc.thePlayer.motionX *= 0.8;
					mc.thePlayer.motionZ *= 0.8;
					
					if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
						double mult = 1 - ((mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1) * 0.167);
						mc.thePlayer.motionX *= mult;
						mc.thePlayer.motionZ *= mult;
					}
					
					if(pos != null && facing != null) {
						placeBlock(0.05);
					}
				}
				break;
			case "Hypixel2":
				if(!hypixelSprint.isEnabled()) {
					mc.thePlayer.setSprinting(false);
				} else {
					if(mc.thePlayer.moveForward > 0) {
						mc.thePlayer.setSprinting(true);
					}
				}
				
				if(!isAirUnder() && mc.thePlayer.onGround) {
					mc.timer.timerSpeed = (float) timer.getCurrentValue();
				} else {
					if(timer.getCurrentValue() > 1.5) {
						mc.timer.timerSpeed = 0.9F;
					} else if(timer.getCurrentValue() > 1) {
						mc.timer.timerSpeed = 0.95F;
					} else {
						mc.timer.timerSpeed = 1F;
					}
				}
				
				mc.gameSettings.keyBindSneak.pressed = isAirUnder() && mc.thePlayer.onGround;
				
				if(MovementUtils.getHorizontalMotion() < 0.2 && !hypixelSprint.isEnabled()) {
					MovementUtils.motionMult(MovementUtils.isGoingDiagonally() ? 1.05 : 1.15);
				}
				
				if(isAirUnder()) {
					setBlockFacing(getBlockPosUnder());
					
					if(pos != null && facing != null) {
						placeBlock(MovementUtils.isGoingDiagonally() ? 0.24 : 0.05);
					}
				}
				
				if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
					double mult = 1 - ((mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1) * 0.167);
					mc.thePlayer.motionX *= mult;
					mc.thePlayer.motionZ *= mult;
				}
				break;
			case "Hypixel3":
				mc.thePlayer.setSprinting(false);
				
				mc.timer.timerSpeed = (float) timer.getCurrentValue();
				
				if(isAirUnder()) {
					mc.timer.timerSpeed = 1F;
					setBlockFacing(getBlockPosUnder());
					
					if(pos != null && facing != null) {
						placeBlock(0.16);
					}
				}
				
				if (Math.abs(mc.thePlayer.motionX) > 0.065 && Math.abs(mc.thePlayer.motionZ) > 0.065) {
					if (WorldUtil.negativeExpand(-0.1)) {
						mc.thePlayer.motionX *= 0.5;
						mc.thePlayer.motionZ *= 0.5;
					}
				}
				
				if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
					double mult = 1 - ((mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1) * 0.167);
					mc.thePlayer.motionX *= mult;
					mc.thePlayer.motionZ *= mult;
				}
				break;
			case "Redesky":
			case "Redesky2":
				if(mode.is("Redesky")) {
					mc.thePlayer.setSprinting(false);
				} else if(mode.is("Redesky2")) {
					if(++ticks % 60 < 15 || !mc.thePlayer.onGround || mc.gameSettings.keyBindJump.isKeyDown() || mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
						mc.thePlayer.setSprinting(false);
					}
				}
				
				if(timer.getCurrentValue() > 1.0) {
					if(mc.thePlayer.ticksExisted % 2 == 0) {
						mc.timer.timerSpeed = (float) timer.getCurrentValue();
					} else {
						mc.timer.timerSpeed = 0.99F;
					}
				} else {
					mc.timer.timerSpeed = 1F;
				}
				break;
			case "Legit":
				mc.thePlayer.setSprinting(false);
				mc.timer.timerSpeed = (float) timer.getCurrentValue();
				
				mc.gameSettings.keyBindSneak.pressed = this.isAirUnder();
				
				if(isAirUnder()) {
					setBlockFacing(getBlockPosUnder());
					
					MovementUtils.motionMult(0.6);
					
					if(pos != null && facing != null) {
						yaw = blockYaw;
						if(mc.thePlayer.onGround) {
							pitch = 81.5F;
						} else {
							pitch = 90.0F;
						}
						
						placeBlock(0.24);
					}
				}
				break;
		}
		
		if (isAirUnder()) {
			if(!wasAirUnder) {
				wasAirUnder = true;
			}
		} else {
			wasAirUnder = false;
		}
		
		if(mc.gameSettings.keyBindJump.isKeyDown() && MovementUtils.getHorizontalMotion() < 0.1) {
			mc.timer.timerSpeed = (float) towerTimer.getCurrentValue();
		}
	}
	
	@Listener
	public void onMotion(MotionEvent event) {
		float aacRots[] = this.getAACRotations();
		
		if(mode.is("Custom")) {
			if(blockUpdateTiming.is("Pre")) {
				this.updateCustomBlockPos();
			}
			
			if(placementTiming.is("Pre")) {
				this.placeCustom();
			}
		}
		
		switch (mode.getMode()) {
			case "Redesky":
			case "Redesky2":
				if(isAirUnder()) {
					setBlockFacing(getBlockPosUnder());
					
					float rots[] = this.getAACRotations();
					
					float diff = Math.abs(yaw - rots[0]);
					
					yaw = rots[0];
					pitch = rots[1];
					
					MovementUtils.motionMult(1 - (diff * 0.001));
				}
				break;
		}
		
		switch (mode.getMode()) {
			case "Normal":
			case "Legit":
				event.setYaw(mc.thePlayer.rotationYawHead = mc.thePlayer.renderYawOffset = yaw);
				event.setPitch(mc.thePlayer.rotationPitchHead = pitch);
				break;
			case "Hypixel":
			case "Hypixel2":
			case "Hypixel3":
			case "Hypixel jump":
				yaw = MovementUtils.getPlayerDirection() - 150;
				pitch = aacRots[1];
				event.setYaw(mc.thePlayer.rotationYawHead = mc.thePlayer.renderYawOffset = yaw);
				event.setPitch(mc.thePlayer.rotationPitchHead = pitch);
				break;
			case "Redesky":
			case "Redesky2":
			case "Custom":
				event.setYaw(mc.thePlayer.rotationYawHead = mc.thePlayer.renderYawOffset = yaw);
				event.setPitch(mc.thePlayer.rotationPitchHead = pitch);
				break;
		}
		
		if(mode.is("Legit")) {
			//event.setOnGround(false);
		}
	}
	
	private void updateCustomBlockPos() {
		if(!this.isAirUnder() || !pulseBlink.isEnabled()) {
			mc.timer.timerSpeed = (float) timer.getCurrentValue();
		} else {
			mc.timer.timerSpeed = 1F;
		}
		
		if(sneak.isEnabled()) {
			mc.gameSettings.keyBindSneak.pressed = this.isAirUnder();
		}
		
		if(isAirUnder()) {
			setBlockFacing(getBlockPosUnder());
			
			if(pos != null && facing != null) {
				MovementUtils.motionMult(motionMult.getCurrentValue());
				
				lastPos = pos;
				lastFacing = facing;
			}
		} else {
			if(mc.thePlayer.ticksExisted % 2 == 0 && additionalRightClicks.isEnabled()) {
				PacketUtil.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
			}
		}
		
		this.updateCustomRots();
	}
	
	private void updateCustomRots() {
		float yaw1 = 0;
		float pitch1 = 0;
		
		float aacRots[] = this.getAACRotations();
		
		switch (rotations.getMode()) {
			case "Normal":
				if(pos != null && facing != null) {
					float rots[] = BlockUtil.getDirectionToBlock(pos.getX(), pos.getY(), pos.getZ(), facing);
					
					yaw1 = rots[0];
					pitch1 = rots[1];
				} else {
					yaw1 = yaw;
					pitch1 = pitch;
				}
				break;
			case "Smooth":
				if(lastPos != null && lastFacing != null) {
					float rots[] = BlockUtil.getDirectionToBlock(lastPos.getX(), lastPos.getY(), lastPos.getZ(), lastFacing);
					
					yaw1 = rots[0];
					pitch1 = rots[1];
				} else {
					yaw1 = MovementUtils.getPlayerDirection() - 180;
					pitch1 = aacRots[1];
				}
				break;
			case "BlockYaw":
				yaw1 = blockYaw;
				if(mc.thePlayer.onGround) {
					pitch1 = 81.5F;
				} else {
					pitch1 = 90.0F;
				}
				break;
			case "AAC":
				float diff = Math.abs(yaw - aacRots[0]);
				
				yaw1 = aacRots[0];
				pitch1 = aacRots[1];
				break;
			case "Back":
				yaw1 = MovementUtils.getPlayerDirection() - 180;
				pitch1 = aacRots[1];
				break;
			case "Sideaways":
				yaw1 = MovementUtils.getPlayerDirection() - 145;
				pitch1 = aacRots[1];
				break;
			case "Down":
				yaw1 = mc.thePlayer.rotationYaw;
				pitch1 = aacRots[1];
				break;
			case "None":
				yaw1 = mc.thePlayer.rotationYaw;
				pitch1 = mc.thePlayer.rotationPitch;
				break;
		}
		
		double diff = Math.abs(yaw1 - yaw);
		double rotTurnSpeed = rotationTurnSpeed.getCurrentValue();
		double randomisation = rotationRandomisation.getCurrentValue();
		
		if(randomisation > 0) {
			yaw1 = yaw1 + (float) (Math.random() * randomisation - randomisation * 0.5);
			
			randomisation *= 0.5;
			pitch1 = pitch1 + (float) (Math.random() * randomisation - randomisation * 0.5);
		}
		
		pitch1 = Math.min(pitch1, 90);
		pitch1 = Math.max(pitch1, -90);
		
		if(diff > rotTurnSpeed && diff < 360 - rotTurnSpeed) {
			if(diff < 180) {
				if(yaw > yaw1) {
					yaw -= rotTurnSpeed;
				} else {
					yaw += rotTurnSpeed;
				}
			} else {
				if(yaw > yaw1) {
					yaw += rotTurnSpeed;
				} else {
					yaw -= rotTurnSpeed;
				}
			}
		} else {
			yaw = yaw1;
		}
		
		pitch = pitch1;
	}
	
	private void placeCustom() {
		if(pos != null && facing != null) {
			boolean placed = placeBlock(negativeExpand.getCurrentValue());
			
			if(!placed) {
				if(mc.thePlayer.ticksExisted % 2 == 0 && additionalRightClicks.isEnabled()) {
					PacketUtil.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
				}
			}
		}
	}
	
	@Listener
	public void onPostMotion(PostMotionEvent event) {
		if(mode.is("Custom")) {
			if(blockUpdateTiming.is("Post")) {
				this.updateCustomBlockPos();
			}
			
			if(placementTiming.is("Post")) {
				this.placeCustom();
			}
		}
		
		switch (mode.getMode()) {
			case "Hypixel":
				if(!mc.gameSettings.keyBindSneak.isKeyDown() && mc.thePlayer.onGround) {
					PacketUtil.stopSneaking();
				}
				
				if(hypixelSprint.isEnabled()) {
					placeBlock(MovementUtils.isGoingDiagonally() ? 0.2 : 0);
				} else {
					placeBlock(MovementUtils.isGoingDiagonally() ? 0.2 : 0.12);
				}
				break;
			case "Redesky":
			case "Redesky2":
				if(isAirUnder()) {
					if(pos != null && facing != null) {
						placeBlock(mc.thePlayer.isSprinting() || mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 0.06 : 0.16);
					}
				}
				break;
		}
	}
	
	@Listener
	public void onSend(PacketSendEvent event) {
		switch (mode.getMode()) {
			case "Hypixel":
			case "Hypixel2":
				if (event.getPacket() instanceof C03PacketPlayer) {
					if(this.isAirUnder() || !mc.thePlayer.onGround) {
						this.sendPackets();
						return;
					}
					
					event.setCancelled(true);
					packets.add(event.getPacket());
				}
				break;
			case "Custom":
				if(noSprint.is("Spoof")) {
					if (event.getPacket() instanceof C0BPacketEntityAction) {
						C0BPacketEntityAction packet = (C0BPacketEntityAction) event.getPacket();
						
						if(packet.getAction() == Action.START_SPRINTING || packet.getAction() == Action.STOP_SPRINTING) {
							event.setCancelled(true);
						}
					}
				}
				
				if(pulseBlink.isEnabled()) {
					if (event.getPacket() instanceof C03PacketPlayer || event.getPacket() instanceof C0FPacketConfirmTransaction || event.getPacket() instanceof C00PacketKeepAlive) {
						if(this.isAirUnder() || !mc.thePlayer.onGround) {
							this.sendPackets();
							return;
						}
						
						event.setCancelled(true);
						packets.add(event.getPacket());
					}
				}
				break;
		}
	}
	
	private boolean placeBlock(double negativeExpand) {
		if(!WorldUtil.negativeExpand(negativeExpand)) {
			return false;
		}
		
		if(currentSlotIsBlock()) {
			return sendPlacing();
		}
		return false;
	}
	
	private boolean sendPlacing() {
		boolean placed = mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), pos, facing, BlockUtil.getVec3(pos, facing));
		if(placed) {
			PacketUtil.sendPacketNoEvent(new C0APacketAnimation());

			pos = null;
			facing = null;
		}
		return placed;
	}
	
	private BlockPos getBlockPosUnder() {
		switch (mode.getMode()) {
			case "Normal":
			case "Verus":
			case "Custom":
				if((!Vestige.getInstance().getModuleManager().getModule(Speed.class).isEnabled() && !jump.isEnabled()) || mc.thePlayer.onGround || mc.gameSettings.keyBindJump.isKeyDown()) {
					lastY = mc.thePlayer.posY;
				}
				break;
			case "Hypixel jump":
				if(mc.thePlayer.onGround || mc.gameSettings.keyBindJump.isKeyDown()) {
					lastY = mc.thePlayer.posY;
				}
				break;
			default:
				lastY = mc.thePlayer.posY;
				break;
		}
		
		return new BlockPos(mc.thePlayer.posX, lastY - 1.0, mc.thePlayer.posZ);
	}
	
	private boolean isAirUnder() {
		Block block = mc.theWorld.getBlockState(getBlockPosUnder()).getBlock();
		return block instanceof BlockAir || block instanceof BlockTallGrass || block instanceof BlockLiquid;
	}
	
	private boolean currentSlotIsBlock() {
		return mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock;
	}
	
	private void pickBlock() {
		for(int i = 0; i < 9; i++) {
            if (mc.thePlayer.inventory.getStackInSlot(i) == null)
                continue;
            if (mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemBlock && !BlockUtil.blockBlacklist.contains(((ItemBlock) mc.thePlayer.inventory.getStackInSlot(i).getItem()).getBlock())) {
            	if(i != slot || mc.thePlayer.inventory.currentItem != i) {
            		changedSlot = true;
            	}
        		slot = i;
				break;
            }
        }
		
		mc.thePlayer.inventory.currentItem = slot;
	}
	
	private void switchToOriginalSlot() {
		mc.thePlayer.inventory.currentItem = oldSlot;
	}
	
	private float[] getAACRotations() {
		float clientYaw = MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw);
		float diff = (int) ((blockYaw - clientYaw) / 45.0F);
		
		float yaw1 = clientYaw + diff * 45;
		float pitch1;
		
		if(!mc.thePlayer.onGround || mc.gameSettings.keyBindJump.isKeyDown()) {
			pitch1 = 90;
		} else if(MovementUtils.isGoingDiagonally()) {
			pitch1 = 83;
		} else {
			pitch1 = 81.5F;
		}
		return new float[] {yaw1, pitch1};
	}
	
	private void setBlockFacing(BlockPos pos) {
		if (mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock() != Blocks.air) {
			this.pos = pos.add(0, -1, 0);
			facing = EnumFacing.UP;
		} else if (mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock() != Blocks.air) {
			this.pos = pos.add(-1, 0, 0);
			facing = EnumFacing.EAST;
			blockYaw = 90;
		} else if (mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock() != Blocks.air) {
			this.pos = pos.add(1, 0, 0);
			facing = EnumFacing.WEST;
			blockYaw = -90;
		} else if (mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock() != Blocks.air) {
			this.pos = pos.add(0, 0, -1);
			facing = EnumFacing.SOUTH;
			blockYaw = 180;
		} else if (mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock() != Blocks.air) {
			this.pos = pos.add(0, 0, 1);
			facing = EnumFacing.NORTH;
			blockYaw = 0;
		}
		else if (mc.theWorld.getBlockState(pos.add(-1, 0, -1)).getBlock() != Blocks.air) {
			facing = EnumFacing.EAST;
			this.pos = pos.add(-1, 0, -1);
			blockYaw = 135;
		} else if (mc.theWorld.getBlockState(pos.add(1, 0, 1)).getBlock() != Blocks.air) {
			facing = EnumFacing.WEST;
			this.pos = pos.add(1, 0, 1);
			blockYaw = -45;
		} else if (mc.theWorld.getBlockState(pos.add(1, 0, -1)).getBlock() != Blocks.air) {
			facing = EnumFacing.SOUTH;
			this.pos = pos.add(1, 0, -1);
			blockYaw = -135;
		} else if (mc.theWorld.getBlockState(pos.add(-1, 0, 1)).getBlock() != Blocks.air) {
			facing = EnumFacing.NORTH;
			this.pos = pos.add(-1, 0, 1);
			blockYaw = 45;
		}
		else if (mc.theWorld.getBlockState(pos.add(0, -1, 1)).getBlock() != Blocks.air) {
			this.pos = pos.add(0, -1, 1);
			facing = EnumFacing.UP;
		}
		else if (mc.theWorld.getBlockState(pos.add(0, -1, -1)).getBlock() != Blocks.air) {
			this.pos = pos.add(0, -1, -1);
			facing = EnumFacing.UP;
		}
		else if (mc.theWorld.getBlockState(pos.add(1, -1, 0)).getBlock() != Blocks.air) {
			this.pos = pos.add(1, -1, 0);
			facing = EnumFacing.UP;
		}
		else if (mc.theWorld.getBlockState(pos.add(-1, -1, 0)).getBlock() != Blocks.air) {
			this.pos = pos.add(-1, -1, 0);
			facing = EnumFacing.UP;
		} else {
			pos = null;
			facing = null;
		}
	}
	
	public boolean shouldSafewalk() {
		if(mode.is("Custom")) {
			return safewalk.isEnabled();
		}
		return mode.is("Normal") || mode.is("Hypixel") || mode.is("Hypixel2") || mode.is("Verus") || mode.is("Redesky") || mode.is("Redesky2");
	}
	
}
