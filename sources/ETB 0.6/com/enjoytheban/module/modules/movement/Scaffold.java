package com.enjoytheban.module.modules.movement;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.world.EventPostUpdate;
import com.enjoytheban.api.events.world.EventPreUpdate;
import com.enjoytheban.api.value.Option;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import com.enjoytheban.utils.math.RotationUtil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

/**
 * I have no clue why this bypasses hypixel please help
 * 
 * @author purity
 */

public class Scaffold extends Module {

	private Option<Boolean> tower = new Option("Tower", "tower", true);
	private Option<Boolean> silent = new Option("Silent", "Silent", true);
	private Option<Boolean> aac = new Option("AAC", "AAC", false);

	// list of blocks we don't want to place
	private List<Block> invalid = Arrays
			.asList(new Block[] { Blocks.air, Blocks.water, Blocks.fire, Blocks.flowing_water, Blocks.lava,
					Blocks.flowing_lava, Blocks.chest, Blocks.enchanting_table, Blocks.tnt });

	// will be holding our block data
	private BlockCache blockCache;

	// hold the currentitem in case peeps dont want a silent scaffold
	private int currentItem;

	public Scaffold() {
		super("Scaffold", new String[] { "magiccarpet", "blockplacer", "airwalk" }, ModuleType.Movement);
		addValues(tower, silent, aac);
		currentItem = 0;
		setColor(new Color(244, 119, 194).getRGB());
	}

	@Override
	public void onEnable() {
		this.currentItem = mc.thePlayer.inventory.currentItem;
	}

	@Override
	public void onDisable() {
		mc.thePlayer.inventory.currentItem = this.currentItem;
	}

	/**
	 * //is the block a good or BAD block that we dont want placed private boolean
	 * contains(Block block) { return invalid.stream().anyMatch(e ->
	 * e.equals(block)); }
	 **/

	// heads shoudler knees and toes
	@EventHandler
	private void onUpdate(EventPreUpdate event) {
		if (aac.getValue()) {
			mc.thePlayer.setSprinting(false);
		}
		if (this.grabBlockSlot() == -1) {
			return;
		}
		this.blockCache = this.grab();
		if (this.blockCache == null) {
			return;
		}
		float[] rotations = RotationUtil.grabBlockRotations(this.blockCache.getPosition());
		event.setYaw(rotations[0]);
		event.setPitch(RotationUtil
				.getVecRotation(this.grabPosition(this.blockCache.getPosition(), this.blockCache.getFacing()))[1]
				- 3.0f);
	}

	@EventHandler
	private void onPostUpdate(EventPostUpdate event) {
		if (this.blockCache == null) {
			return;
		}
		if (mc.gameSettings.keyBindJump.getIsKeyPressed() && tower.getValue()) {
			mc.thePlayer.setSprinting(false);
			mc.thePlayer.motionY = 0.0;
			mc.thePlayer.motionX = 0.0;
			mc.thePlayer.motionZ = 0.0;
			mc.thePlayer.jump();
		}

		int currentSlot = mc.thePlayer.inventory.currentItem;
		int slot = this.grabBlockSlot();
		mc.thePlayer.inventory.currentItem = slot;
		if (placeBlock(blockCache.position, blockCache.facing)) {
			if (silent.getValue()) {
				mc.thePlayer.inventory.currentItem = currentSlot;
				mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(currentSlot));
			}

			this.blockCache = null;
		}
	}

	// place the block
	private boolean placeBlock(BlockPos pos, EnumFacing facing) {
		Vec3 eyesPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight(),
				mc.thePlayer.posZ);
		if (mc.playerController.func_178890_a(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), pos, facing,
				new Vec3(blockCache.position).addVector(0.5, 0.5, 0.5)
						.add(new Vec3(blockCache.facing.getDirectionVec()).scale(0.5)))) {
			mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
			return true;
		}
		return false;
	}

	// gets the block position by face
	private Vec3 grabPosition(BlockPos position, EnumFacing facing) {
		Vec3 offset = new Vec3(facing.getDirectionVec().getX() / 2.0D, facing.getDirectionVec().getY() / 2.0D,
				facing.getDirectionVec().getZ() / 2.0D);

		Vec3 point = new Vec3(position.getX() + 0.5D, position.getY() + 0.5D, position.getZ() + 0.5D);

		return point.add(offset);
	}

	// grab the block
	private BlockCache grab() {
		EnumFacing[] invert = { EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.EAST,
				EnumFacing.WEST };
		BlockPos position = new BlockPos(mc.thePlayer.getPositionVector()).offset(EnumFacing.DOWN);
		if (!(mc.theWorld.getBlockState(position).getBlock() instanceof BlockAir)) {
			return null;
		}
		for (EnumFacing facing : EnumFacing.values()) {
			BlockPos offset = position.offset(facing);
			IBlockState blockState = mc.theWorld.getBlockState(offset);
			if (!(mc.theWorld.getBlockState(offset).getBlock() instanceof BlockAir)) {
				return new BlockCache(offset, invert[facing.ordinal()]);
			}
		}
		BlockPos[] offsets = { new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, -1),
				new BlockPos(0, 0, 1) };
		if (mc.thePlayer.onGround) {
			for (BlockPos offset : offsets) {
				BlockPos offsetPos = position.add(offset.getX(), 0, offset.getZ());
				IBlockState blockState2 = mc.theWorld.getBlockState(offsetPos);
				if (mc.theWorld.getBlockState(offsetPos).getBlock() instanceof BlockAir) {
					for (EnumFacing facing2 : EnumFacing.values()) {
						BlockPos offset2 = offsetPos.offset(facing2);
						IBlockState blockState3 = mc.theWorld.getBlockState(offset2);
						if (!(mc.theWorld.getBlockState(offset2).getBlock() instanceof BlockAir)) {
							return new BlockCache(offset2, invert[facing2.ordinal()]);
						}
					}
				}
			}
		}
		return null;
	}

	// grab the slot with a block
	private int grabBlockSlot() {
		for (int i = 0; i < 9; ++i) {
			ItemStack itemStack = mc.thePlayer.inventory.mainInventory[i];
			if (itemStack != null && itemStack.getItem() instanceof ItemBlock) {
				return i;
			}
		}
		return -1;
	}

	// the class for our BLOCK cache
	private class BlockCache {
		private BlockPos position;
		private EnumFacing facing;

		private BlockCache(BlockPos position, EnumFacing facing) {
			this.position = position;
			this.facing = facing;
		}

		private BlockPos getPosition() {
			return this.position;
		}

		private EnumFacing getFacing() {
			return this.facing;
		}
	}
}
