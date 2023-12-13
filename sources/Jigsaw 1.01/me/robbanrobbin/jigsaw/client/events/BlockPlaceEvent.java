package me.robbanrobbin.jigsaw.client.events;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class BlockPlaceEvent extends Event {

	public BlockPlaceEvent(ItemStack heldStack, BlockPos hitPos, EnumFacing side, Vec3d hitVec) {
		this.heldStack = heldStack;
		this.hitPos = hitPos;
		this.side = side;
		this.hitVec = hitVec;
	}

	public ItemStack heldStack;
	public BlockPos hitPos;
	public EnumFacing side;
	public Vec3d hitVec;

}
