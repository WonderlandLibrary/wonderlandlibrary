package today.flux.module.implement.World.scaffold;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import today.flux.event.MoveEvent;
import today.flux.event.PostUpdateEvent;
import today.flux.event.PreUpdateEvent;
import today.flux.event.TickEvent;
import today.flux.module.SubModule;
import today.flux.utility.BlockUtils;
import today.flux.utility.DelayTimer;

public class AAC extends SubModule {
	private BlockPos blockUnder = null;
	private BlockPos blockBef = null;
	private EnumFacing facing = null;
	private DelayTimer placeTimer = new DelayTimer();

	public AAC() {
		super("AAC", "Scaffold");
	}

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public void onDisable() {
		this.mc.gameSettings.keyBindSneak.pressed = false;
		super.onDisable();
	}

	@EventTarget
	public void onUpdate(PreUpdateEvent event) {
		blockUnder = null;
		blockBef = null;
		if (mc.thePlayer.getCurrentEquippedItem() == null || mc.thePlayer.getCurrentEquippedItem().getItem() == null
				|| !(mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock)) {
			return;
		}

		this.mc.thePlayer.setSprinting(false);

		BlockPos under = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 0.01, mc.thePlayer.posZ);
		if (BlockUtils.getBlock(under).getMaterial() == Material.air) {
			blockUnder = under;
			for (EnumFacing facing : EnumFacing.values()) {
				BlockPos offset = blockUnder.offset(facing);
				if (BlockUtils.getBlock(offset).getMaterial() != Material.air) {
					this.facing = facing;
					blockBef = offset;
					break;
				}
			}
		}

		event.setYaw(this.mc.thePlayer.rotationYaw > 0.0F ? this.mc.thePlayer.rotationYaw - 180.0F
				: this.mc.thePlayer.rotationYaw + 180.0F);
		event.setPitch(82.0F);
	}

	@EventTarget
	public void onTick(TickEvent event) {
		BlockPos expanded = new BlockPos(mc.thePlayer.posX + this.mc.thePlayer.motionX * 2, mc.thePlayer.posY - 0.01,
				mc.thePlayer.posZ + this.mc.thePlayer.motionZ * 2);
		BlockPos under = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 0.01, mc.thePlayer.posZ);
		if (BlockUtils.getBlock(expanded).getMaterial() == Material.air
				|| BlockUtils.getBlock(under).getMaterial() == Material.air) {
			// this.mc.gameSettings.keyBindSneak.pressed = true;
		} else {
			this.mc.gameSettings.keyBindSneak.pressed = false;
		}
	}

	@EventTarget
	public void onMove(MoveEvent e) {
		e.setSafeWalk(true);

		if (!this.mc.thePlayer.onGround || !this.mc.thePlayer.isCollidedVertically
				|| this.mc.thePlayer.movementInput.jump) {
			e.x = 0;
			e.z = 0;
		}
	}

	@EventTarget
	public void onLateUpdate(PostUpdateEvent event) {

		MovingObjectPosition pos = mc.theWorld.rayTraceBlocks(this.getVec3(blockUnder).addVector(0.5, 0.5, 0.5),
				this.getVec3(blockBef).addVector(0.5, 0.5, 0.5));
		if (pos == null) {
			return;
		}

		Vec3 hitVec = pos.hitVec;
		mc.thePlayer.swingItem();
		mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(),
				blockBef, pos.sideHit, hitVec);

		this.placeTimer.reset();
		// sendPacket(new C08PacketPlayerBlockPlacement(blockBef,
		// pos.sideHit.getIndex(), mc.thePlayer.getCurrentEquippedItem(), f, f1, f2));
	}

	private Vec3 getVec3(BlockPos blockPos) {
		return new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}
}
