package vestige.impl.module.world;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import vestige.api.event.Listener;
import vestige.api.event.impl.MotionEvent;
import vestige.api.event.impl.MoveEvent;
import vestige.api.event.impl.UpdateEvent;
import vestige.api.module.Category;
import vestige.api.module.Module;
import vestige.api.module.ModuleInfo;
import vestige.api.setting.impl.BooleanSetting;
import vestige.api.setting.impl.ModeSetting;
import vestige.util.movement.MovementUtils;
import vestige.util.network.PacketUtil;
import vestige.util.world.BlockUtil;
import vestige.util.world.WorldUtil;

@ModuleInfo(name = "Scaffold", category = Category.WORLD, key = Keyboard.KEY_H)
public class OldScaffold extends Module {

	private final BooleanSetting noSprint = new BooleanSetting("No Sprint", this, false);
	private final ModeSetting rotations = new ModeSetting("Rotations", this, "Normal", "Normal", "Randomised", "None");
	private final BooleanSetting diagonal = new BooleanSetting("Diagonal", this, true);
	private final BooleanSetting hypixel = new BooleanSetting("Hypixel", this, false);
	private final ModeSetting tower = new ModeSetting("Tower", this, "None", "NCP", "Hypixel", "None");
	
	private BlockPos pos;
	private EnumFacing facing;
	
	private int slot, oldSlot;
	private boolean changedSlot;
	
	private float blockYaw;
	private float yaw, pitch;
	
	private boolean wasAirUnder;
	
	private boolean prevOnGround;
	
	private int towerTicks;
	
	public OldScaffold() {
		this.registerSettings(noSprint, rotations, diagonal, hypixel, tower);
	}
	
	public void onEnable() {
		oldSlot = mc.thePlayer.inventory.currentItem;
		
		pos = null;
		facing = null;
		
		towerTicks = 0;	
		
		blockYaw = mc.thePlayer.rotationYaw - 180;
		
		prevOnGround = mc.thePlayer.onGround;
	}
	
	public void onDisable() {
		switchToOriginalSlot();
		mc.timer.timerSpeed = 1F;
	}
	
	@Listener
	public void onMove(MoveEvent event) {
		switch(tower.getMode()) {
			case "NCP":
				if(mc.thePlayer.onGround && mc.gameSettings.keyBindJump.isKeyDown()) {
					event.setY(MovementUtils.JUMP_MOTION);
					mc.thePlayer.motionY = 0.42;
					towerTicks = 0;
				} else if(towerTicks == 2) {
					mc.thePlayer.setPosition(mc.thePlayer.posX, Math.round(mc.thePlayer.posY), mc.thePlayer.posZ);
					event.setY(mc.thePlayer.motionY = 0);
					mc.thePlayer.onGround = true;
				}
				towerTicks++;
				break;
			case "Hypixel":
				if(mc.gameSettings.keyBindJump.isKeyDown()) {
					event.setY(0.399998);
					mc.thePlayer.motionY = 0.4;
				}
				break;
		}
	}
	
	@Listener
	public void onUpdate(UpdateEvent event) {
		if(noSprint.isEnabled()) {
			mc.gameSettings.keyBindSprint.pressed = false;
			mc.thePlayer.setSprinting(false);
		}
		
		pickBlock();
		
		if (isAirUnder()) {
			setBlockFacing(getBlockPosUnder());
			
			if (pos != null && facing != null) {
				placeBlock(hypixel.isEnabled() ? 0.16 : 0.08);
			}
		}
		
		if (hypixel.isEnabled()) {
			if (mc.thePlayer.onGround) {
				prevOnGround = true;
			} else if (prevOnGround) {
				if (mc.thePlayer.motionY < 0) {
					//mc.thePlayer.onGround = true;
					//mc.thePlayer.motionY = 0;
				}
				prevOnGround = false;
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
			
			if(!this.isAirUnder() && mc.thePlayer.onGround) {
				mc.timer.timerSpeed = 1.1F;
			} else {
				mc.timer.timerSpeed = 1F;
			}
		}
	}
	
	@Listener
	public void onMotion(MotionEvent event) {
		switch(rotations.getMode()) {
			case "Normal":
				yaw = blockYaw;
				if(mc.thePlayer.onGround) {
					pitch = 81.5F;
				} else {
					pitch = 90.0F;
				}
				break;
			case "Randomised":
				if(isAirUnder() && !wasAirUnder) {
					yaw = (float) (blockYaw + Math.random() * 80 - 40);
					if(mc.thePlayer.onGround) {
						pitch = (float) (80 + Math.random() * 3);
					} else {
						pitch = 90.0F;
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
		
		if(!rotations.is("None")) {
			event.setYaw(mc.thePlayer.rotationYawHead = mc.thePlayer.renderYawOffset = yaw);
			event.setPitch(mc.thePlayer.rotationPitchHead = pitch);
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
		return new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
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
	
	private void setBlockFacing(BlockPos pos) {
		if(diagonal.isEnabled()) {
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
			}
			else {
				pos = null;
				facing = null;
			}
		} else {
			if (mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock() != Blocks.air) {
				this.pos = pos.add(0, -1, 0);
				facing = EnumFacing.UP;
			} else if (mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock() != Blocks.air) {
				this.pos = pos.add(-1, 0, 0);
				facing = EnumFacing.EAST;
			} else if (mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock() != Blocks.air) {
				this.pos = pos.add(1, 0, 0);
				facing = EnumFacing.WEST;
			} else if (mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock() != Blocks.air) {
				this.pos = pos.add(0, 0, -1);
				facing = EnumFacing.SOUTH;
			} else if (mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock() != Blocks.air) {
				this.pos = pos.add(0, 0, 1);
				facing = EnumFacing.NORTH;
			} else {
				pos = null;
				facing = null;
			}
		}
	}
	
}
