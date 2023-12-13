package me.aidanmees.trivia.client.modules.World;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.events.UpdateEvent;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.client.tools.RotationUtil;
import me.aidanmees.trivia.module.Module;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class Scaffold extends Module {
	private boolean tower = false;
	  private BlockEntry blockEntry = null;

	public Scaffold() {
		super("Scaffold", Keyboard.KEY_NONE, Category.MOVEMENT,
				"Magic carpet under u!");
	}

	

	

	@Override
	public void onUpdate(UpdateEvent event) {
		 if (this.getSlotWithBlock() == -1) {
		        return;
		      }
		      
		      
		        if ((this.tower) && (mc.thePlayer.rotationPitch >= 85.0F) && (mc.thePlayer.onGround)) {
		          mc.thePlayer.jump();
		        }
		        this.blockEntry = this.find();
		        if (this.blockEntry == null) {
		          return;
		        }
		        if (mc.thePlayer.isSprinting()) {
		          mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
		        }
		        float[] rotations = RotationUtil.getRotations(this.getPositionByFace(this.blockEntry.getPosition(), this.blockEntry.getFacing()));
		        
		        event.yaw = (rotations[0]);
		        event.pitch = (rotations[1]);
		      
		        if (this.blockEntry == null) {
		          return;
		        }
		        int currentSlot = mc.thePlayer.inventory.currentItem;
		        
		        int slot = this.getSlotWithBlock();
		        
		        mc.thePlayer.inventory.currentItem = slot;
		        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot));
		        
		        mc.thePlayer.swingItem();
		        
		        mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
		        
		        mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), this.blockEntry.getPosition(), 
		          this.blockEntry.getFacing(), new Vec3(this.blockEntry.getPosition().getX(), this.blockEntry.getPosition().getY(), this.blockEntry.getPosition().getZ()));
		        
		        mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
		        
		        mc.thePlayer.inventory.currentItem = currentSlot;
		        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(currentSlot));
		        
		        this.blockEntry = null;
		        if (mc.thePlayer.isSprinting()) {
		          mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
		        }
		      
		      super.onUpdate();
		    }
		  
		  public void onEnabled()
		  {
		   super.onEnable();
		  }
		  
		  public void onDisable()
		  {
		   super.onDisable();
		  }
		  


		    public BlockEntry find() {
		        if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(mc.thePlayer.motionX, -1, mc.thePlayer.motionZ)).isEmpty()) {
		            return null;
		        }
		        EnumFacing[] invert = new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST};
		        BlockPos position = new BlockPos(mc.thePlayer.getPositionVector()).offset(EnumFacing.DOWN);
		        if (!(mc.theWorld.getBlockState(position).getBlock() instanceof BlockAir)) {
		            return null;
		        }
		        for (EnumFacing facing : EnumFacing.values()) {
		            BlockPos offset = position.offset(facing);
		            if (mc.theWorld.getBlockState(offset).getBlock() instanceof BlockAir || !this.rayTrace(mc.thePlayer.getPositionEyes(0.0f), this.getPositionByFace(offset, invert[facing.ordinal()]))) continue;
		            return new BlockEntry(offset, invert[facing.ordinal()]);
		        }
		        BlockPos[] offsets = new BlockPos[]{new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(0, 0, 1)};
		        
		       
		            for (BlockPos offset : offsets) {
		                BlockPos offsetPos = position.add(offset.getX(), 0, offset.getZ());
		                if (!(mc.theWorld.getBlockState(offsetPos).getBlock() instanceof BlockAir)) continue;
		                for (EnumFacing facing : EnumFacing.values()) {
		                    BlockPos offset2 = offsetPos.offset(facing);
		                    if (mc.theWorld.getBlockState(offset2).getBlock() instanceof BlockAir || !this.rayTrace(mc.thePlayer.getPositionEyes(0.0f), this.getPositionByFace(offset, invert[facing.ordinal()]))) continue;
		                    return new BlockEntry(offset2, invert[facing.ordinal()]);
		                
		            }
		        }
		        return null;
		    }

		    private boolean rayTrace(Vec3 origin, Vec3 position) {
		        Vec3 difference = position.subtract(origin);
		        int steps = 10;
		        double x = difference.xCoord / (double)steps;
		        double y = difference.yCoord / (double)steps;
		        double z = difference.zCoord / (double)steps;
		        Vec3 point = origin;
		        for (int i = 0; i < steps; ++i) {
		            BlockPos blockPosition = new BlockPos(point = point.addVector(x, y, z));
		            IBlockState blockState = mc.theWorld.getBlockState(blockPosition);
		            if (blockState.getBlock() instanceof BlockLiquid || blockState.getBlock() instanceof BlockAir) continue;
		            AxisAlignedBB boundingBox = blockState.getBlock().getCollisionBoundingBox(mc.theWorld, blockPosition, blockState);
		            if (boundingBox == null) {
		                boundingBox = new AxisAlignedBB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
		            }
		            if (!(boundingBox = boundingBox.offset(blockPosition)).isVecInside(point)) continue;
		            return false;
		        }
		        return true;
		    }

		    private int getSlotWithBlock() {
		        for (int i = 0; i < 9; ++i) {
		            ItemStack itemStack = mc.thePlayer.inventory.mainInventory[i];
		         
		            if (itemStack == null || !(itemStack.getItem() instanceof ItemBlock)) continue;
		            return i;
		        }
		        return -1;
		    }

		    private Vec3 getPositionByFace(BlockPos position, EnumFacing facing) {
		        Vec3 offset = new Vec3((double)facing.getDirectionVec().getX() / 2.0, (double)facing.getDirectionVec().getY() / 2.0, (double)facing.getDirectionVec().getZ() / 2.0);
		        Vec3 point = new Vec3((double)position.getX() +0.5, (double)position.getY() + 0.75, (double)position.getZ() + 0.5);
		        return point.add(offset);
		    }

		    class BlockEntry {
		        private BlockPos position;
		        private EnumFacing facing;

		        BlockEntry(BlockPos position, EnumFacing facing) {
		            this.position = position;
		            this.facing = facing;
		        }

		        public BlockPos getPosition() {
		            return this.position;
		        }

		        public EnumFacing getFacing() {
		            return this.facing;
		        }
		    }

		}

