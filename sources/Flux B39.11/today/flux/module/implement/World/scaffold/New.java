package today.flux.module.implement.World.scaffold;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.Priority;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
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
public class New extends SubModule {
	private final long switchDelay = 300;
	private BlockData blockData = null;
	private DelayTimer placeTime = new DelayTimer();
	private DelayTimer swapTime = new DelayTimer();
	private DelayTimer switchTiemr = new DelayTimer(switchDelay);

	private int prevSlot;
	private boolean rotated;

	private boolean first;

	public New() {
		super("New", "Scaffold");
	}

	@Override
	public void onEnable() {
		super.onEnable();

		this.prevSlot = this.mc.thePlayer.inventory.currentItem;
		placeTime.reset();
		swapTime.reset();
		first = true;
	}

	@Override
	public void onDisable() {
		super.onDisable();

		mc.thePlayer.onGround = true;
		this.mc.thePlayer.inventory.currentItem = prevSlot;
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

		this.blockData = null;
		if (!mc.thePlayer.isSneaking() && hasBlock()) {
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
			if (this.placeTime.hasPassed(70) && switchTiemr.hasPassed(switchDelay)) {
				this.placeTime.reset();

				this.mc.thePlayer.swingItem();
				this.PlaceBlock(this.blockData.position, this.blockData.face);

				// tower
				if (Scaffold.tower.getValue()) {
					if (this.mc.thePlayer.posY != this.blockData.position.getY() + 1
							&& this.mc.thePlayer.movementInput.jump && switchTiemr.hasPassed(switchDelay)
							&& this.mc.thePlayer.inventory.getCurrentItem() != null
							&& this.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBlock
							&& this.mc.thePlayer.inventory.getCurrentItem().stackSize > 0) { // if Scaffolded to UP
						this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.blockData.position.getY() + 2,
								this.mc.thePlayer.posZ);
						this.mc.thePlayer.motionY = 0;
						this.mc.thePlayer.motionX = 0;
						this.mc.thePlayer.motionZ = 0;
						this.mc.thePlayer.jump();
					}
				}
			}
		}
	}

	@EventTarget
	public void onTick(LoopEvent event) {
		// auto hand block
		final int blockSlot = this.getBlockFromInventory(false);

		if (blockSlot == -1)
			return;

		int stacksize = this.mc.thePlayer.inventory.getCurrentItem() != null
				&& this.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBlock
						? this.mc.thePlayer.inventory.getCurrentItem().stackSize
						: 9999999;

		final int preSwap = 2;

		if (this.mc.thePlayer.inventory.getCurrentItem() == null
				|| !(this.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBlock)
				|| stacksize <= preSwap) {
			if (blockSlot > 8 || stacksize <= preSwap) {
				if (this.swapTime.hasPassed(350)) {
					this.swap(this.getBlockFromInventory(true), findEmptySlot());
					swapTime.reset();
				}
			} else if (blockSlot < 9) {
				this.mc.thePlayer.inventory.currentItem = blockSlot;
				this.mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C09PacketHeldItemChange(blockSlot));

				if (first) {
					first = false;
				} else {
					switchTiemr.reset();
				}
			}
		}
	}

	private int findEmptySlot() {
		for (int i = 0; i < 8; i++) {
			if (this.mc.thePlayer.inventory.mainInventory[i] == null)
				return i;
		}

		return this.mc.thePlayer.inventory.currentItem + (this.mc.thePlayer.inventory.getCurrentItem() == null ? 0
				: ((this.mc.thePlayer.inventory.currentItem < 8) ? 1 : -1));
	}

	@EventTarget
	public void onLoop(LoopEvent event) {
		if (this.mc.thePlayer.inventory.getCurrentItem() != null
				&& this.mc.thePlayer.inventory.getCurrentItem().stackSize <= 0)
			this.mc.thePlayer.inventory.mainInventory[this.mc.thePlayer.inventory.currentItem] = null;
	}

	protected void swap(final int slot, final int hotbarNum) {
		this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2,
				this.mc.thePlayer);
	}

	private int getBlockFromInventory(boolean exceptHanding) {
		for (int i = 0; i < 36; ++i) {
			if (this.mc.thePlayer.inventory.mainInventory[i] != null) {
				final ItemStack itemStack = this.mc.thePlayer.inventory.mainInventory[i];

				if (exceptHanding && i == this.mc.thePlayer.inventory.currentItem)
					continue;

				if (!Scaffold.isScaffoldBlock(itemStack))
					continue;

				return i;
			}
		}
		return -1;
	}

	private void PlaceBlock(BlockPos pos, EnumFacing facing) {
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
				|| this.mc.thePlayer.movementInput.jump || !switchTiemr.hasPassed(switchDelay)) {
			e.x = 0;
			e.z = 0;
		}
	}

	private boolean hasBlock() {
		int BlockInInventory = findBlock(9, 36);
		int BlockInHotbar = findBlock(36, 45);

		if (BlockInInventory == -1 && BlockInHotbar == -1) {
			return false;
		}
		return true;
	}

	private int findBlock(int startSlot, int endSlot) {
		int i = startSlot;
		while (i < endSlot) {
			ItemStack stack = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
			if (stack != null && stack.getItem() instanceof ItemBlock
					&& ((ItemBlock) stack.getItem()).getBlock().isFullBlock()) {
				return i;
			}
			++i;
		}
		return -1;
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
