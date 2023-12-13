package today.flux.module.implement.World.scaffold;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.Priority;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import today.flux.event.LoopEvent;
import today.flux.event.MoveEvent;
import today.flux.event.PostUpdateEvent;
import today.flux.event.PreUpdateEvent;
import today.flux.module.SubModule;
import today.flux.module.implement.World.Scaffold;
import today.flux.utility.DelayTimer;
import today.flux.utility.RotationUtils;

/**
 * Created by John on 2017/05/03.
 */
public class Legit extends SubModule {
	private BlockData blockData = null;
	private DelayTimer placeTime = new DelayTimer();
	private boolean rotated;

	public Legit() {
		super("Legit", "Scaffold");
	}

	@Override
	public void onEnable() {
		super.onEnable();

		placeTime.reset();
	}

	@Override
	public void onDisable() {
		super.onDisable();

		if (this.mc.theWorld == null)
			return;

		mc.thePlayer.onGround = true;
	}

	private BlockData getBlockData(final BlockPos input) {
		BlockPos pos = input;
		if (!Scaffold.blacklist
				.contains(mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock())) {
			return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
		}
		if (!Scaffold.blacklist
				.contains(mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock())) {
			return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
		}
		if (!Scaffold.blacklist
				.contains(mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock())) {
			return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
		}
		if (!Scaffold.blacklist
				.contains(mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock())) {
			return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
		}
		if (!Scaffold.blacklist
				.contains(mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock())) {
			return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
		}

		pos = input.add(-1, 0, 0);

		if (!Scaffold.blacklist
				.contains(mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock())) {
			return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
		}
		if (!Scaffold.blacklist
				.contains(mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock())) {
			return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
		}
		if (!Scaffold.blacklist
				.contains(mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock())) {
			return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
		}
		if (!Scaffold.blacklist
				.contains(mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock())) {
			return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
		}

		pos = input.add(1, 0, 0);

		if (!Scaffold.blacklist
				.contains(mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock())) {
			return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
		}
		if (!Scaffold.blacklist
				.contains(mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock())) {
			return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
		}
		if (!Scaffold.blacklist
				.contains(mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock())) {
			return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
		}
		if (!Scaffold.blacklist
				.contains(mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock())) {
			return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
		}

		pos = input.add(0, 0, -1);

		if (!Scaffold.blacklist
				.contains(mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock())) {
			return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
		}
		if (!Scaffold.blacklist
				.contains(mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock())) {
			return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
		}
		if (!Scaffold.blacklist
				.contains(mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock())) {
			return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
		}
		if (!Scaffold.blacklist
				.contains(mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock())) {
			return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
		}

		pos = input.add(0, 0, 1);

		if (!Scaffold.blacklist
				.contains(mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock())) {
			return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
		}
		if (!Scaffold.blacklist
				.contains(mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock())) {
			return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
		}
		if (!Scaffold.blacklist
				.contains(mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock())) {
			return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
		}
		if (!Scaffold.blacklist
				.contains(mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock())) {
			return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
		}

		return null;
	}

	@EventTarget(Priority.HIGHEST) // scaffold->aura
	public void onUpdate(PreUpdateEvent event) {
		if (event.isModified()) {
			rotated = false;
			return;
		}

		event.setPitch(90);

		this.blockData = null;
		if (!this.mc.thePlayer.isSneaking()) {
			BlockPos blockBelow;
			if (this.mc.thePlayer.onGround) {
				double x2 = Math.cos(Math.toRadians((double) (this.mc.thePlayer.rotationYaw + 90.0F)));
				double z2 = Math.sin(Math.toRadians((double) (this.mc.thePlayer.rotationYaw + 90.0F)));
				double var18 = (double) this.mc.thePlayer.movementInput.moveForward * 0.4D * x2;
				double xOffset = var18 + (double) this.mc.thePlayer.movementInput.moveStrafe * 0.4D * z2;
				var18 = (double) this.mc.thePlayer.movementInput.moveForward * 0.4D * z2;
				double zOffset = var18 - (double) this.mc.thePlayer.movementInput.moveStrafe * 0.4D * x2;
				double x = this.mc.thePlayer.posX + xOffset;
				double y = this.mc.thePlayer.posY - 1.0D;
				double z = this.mc.thePlayer.posZ + zOffset;
				blockBelow = new BlockPos(x, y, z);
			} else {
				blockBelow = new BlockPos(this.mc.getRenderViewEntity().posX,
						this.mc.getRenderViewEntity().getEntityBoundingBox().minY - 1.0D,
						this.mc.getRenderViewEntity().posZ);
			}

			if (mc.theWorld.getBlockState(blockBelow).getBlock() == Blocks.air) {
				this.blockData = this.getBlockData(blockBelow);

				float[] rots = RotationUtils.getRotations(this.blockData.position, this.blockData.face);
				event.setYaw(rots[0]);
				event.setPitch(rots[1]);

				rotated = true;
			}
		}
	}

	@EventTarget(Priority.LOW)
	public void onPostUpdate(PostUpdateEvent e) {
		if (!rotated)
			return;

		rotated = false;

		if (this.blockData != null && this.mc.thePlayer.inventory.getCurrentItem() != null
				&& this.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBlock
				&& this.mc.thePlayer.inventory.getCurrentItem().stackSize > 0) {
			if (this.placeTime.hasPassed(300)) {
				this.placeTime.reset();

				this.mc.thePlayer.swingItem();
				this.placeBlock(this.blockData.position, this.blockData.face);

				// tower
				/*
				 * if(Scaffold.tower.getValue()) { if (this.mc.thePlayer.posY !=
				 * this.blockData.position.getY() + 1 && this.mc.thePlayer.movementInput.jump) {
				 * //if Scaffolded to UP this.mc.thePlayer.setPosition(this.mc.thePlayer.posX,
				 * this.blockData.position.getY() + 2, this.mc.thePlayer.posZ);
				 * this.mc.thePlayer.motionY = 0; this.mc.thePlayer.motionX = 0;
				 * this.mc.thePlayer.motionZ = 0; this.mc.thePlayer.jump(); } }
				 */
			}
		}
	}

	/**
	 * Anti ZERO
	 **/
	@EventTarget
	public void onLoop(LoopEvent event) {
		for (int i = 0; i < 8; i++) {
			if (this.mc.thePlayer.inventory.mainInventory[i] != null
					&& this.mc.thePlayer.inventory.mainInventory[i].stackSize <= 0)
				this.mc.thePlayer.inventory.mainInventory[i] = null;
		}
	}

	private void placeBlock(BlockPos pos, EnumFacing facing) {
		ItemStack heldItem = this.mc.thePlayer.inventory.getCurrentItem();
		if (heldItem == null)
			return;

		this.mc.thePlayer.inventory.mainInventory[this.mc.thePlayer.inventory.currentItem].stackSize -= 1;
		mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, heldItem, pos, facing,
				new Vec3((double) pos.getX(), (double) pos.getY(), (double) pos.getZ()));
	}

	@EventTarget
	public void onMove(MoveEvent e) {
		if (!Scaffold.safewalk.getValue())
			return;

		e.setSafeWalk(true);

		if (!this.mc.thePlayer.onGround || !this.mc.thePlayer.isCollidedVertically
				|| this.mc.thePlayer.movementInput.jump) {
			e.x = 0;
			e.z = 0;
		}
	}

	private static class BlockData {
		public BlockPos position;
		public EnumFacing face;

		private BlockData(BlockPos position, EnumFacing face) {
			this.position = position;
			this.face = face;
		}
	}
}
