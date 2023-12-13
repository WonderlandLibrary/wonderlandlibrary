package dev.monsoon.module.implementation.player;

import dev.monsoon.Monsoon;
import dev.monsoon.event.Event;
import dev.monsoon.event.listeners.*;
import dev.monsoon.module.base.Module;
import dev.monsoon.module.setting.impl.ModeSetting;
import dev.monsoon.module.setting.impl.BooleanSetting;
import dev.monsoon.module.setting.impl.NumberSetting;
import dev.monsoon.util.misc.ServerUtil;
import dev.monsoon.util.misc.Timer;
import dev.monsoon.util.entity.MovementUtil;
import dev.monsoon.util.entity.RotationUtils;
import dev.monsoon.util.render.RenderUtil;
import dev.monsoon.util.world.BlockUtils2;
import dev.monsoon.util.world.WorldUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.potion.Potion;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import dev.monsoon.module.enums.Category;

public class Scaffold extends Module {

	public BooleanSetting sprint = new BooleanSetting("Allow Sprinting", false, this),
			safewalk = new BooleanSetting("Safewalk", true, this),
			timerBoost = new BooleanSetting("Timer Boost", false, this),
			slowDown = new BooleanSetting("Slowdown", true, this);
	public NumberSetting extend = new NumberSetting("Expand", 0.5, 0.1, 5, 0.1, this),
			placeDelay = new NumberSetting("Delay", 70, 0, 200, 1, this),
			timerAmount = new NumberSetting("Boost Amount", 1.15, 1, 6, 0.05, this, false);
	public ModeSetting swing = new ModeSetting("Swing", this, "NoSwing","NoSwing", "Client");
	public ModeSetting rotationMode = new ModeSetting("Rotation", this, "NCP","NCP", "Hypixel");


	public Scaffold() {
		super("Scaffold", Keyboard.KEY_NONE, Category.PLAYER);
		this.addSettings(rotationMode,sprint,safewalk,timerBoost,timerAmount,placeDelay,slowDown,extend,swing);
	}

	public static transient float lastYaw = 0, lastPitch = 0, lastRandX = 0, lastRandY = 0, lastRandZ = 0;
	public static transient BlockPos lastBlockPos = null;
	public static transient EnumFacing lastFacing = null;
	public static transient Timer timer = new Timer(), boosterTimer = new Timer(), towerTimer = new Timer();
	private static transient double keepPosY = 0;

	int currentSlot;
	int currentItem;
	int lastSlot;


	public void onEnable() {
		timer.reset();
		towerTimer.reset();
		//updateHotbarHypixel();
		boosterTimer.reset();
		lastSlot = -1;
		keepPosY = mc.thePlayer.posY - 1;

		BlockPos block = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);

		for (double i = mc.thePlayer.posY - 1; i > mc.thePlayer.posY - 5; i -= 0.5) {

			try {
				if (mc.theWorld.getBlockState(block).getBlock() != Blocks.air) {

					keepPosY = block.getY();
					break;

				}
			} catch (Exception e) {

			}
			block = block.add(0, -1, 0);

		}

	}

	public void onDisable() {
		mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
		lastSlot = -1;
		mc.gameSettings.keyBindSneak.pressed = false;
		mc.timer.timerSpeed = 1F;
	}


	public void onEvent(Event e) {

		if (e instanceof EventPacket && e.isIncoming()) {

			if (Monsoon.killAura.isEnabled() && Monsoon.killAura.target != null) {
				return;
			}

			if (((EventPacket)e).packet instanceof S2FPacketSetSlot) {
				lastSlot = ((S2FPacketSetSlot)((EventPacket)e).packet).field_149177_b;
			}

		}

		if (e instanceof EventPacket && e.isOutgoing()) {

			if (Monsoon.killAura.isEnabled() && Monsoon.killAura.target != null) {
				return;
			}

			if (((EventPacket)e).packet instanceof C09PacketHeldItemChange) {
				lastSlot = ((C09PacketHeldItemChange)((EventPacket)e).packet).getSlotId();
			}

		}

		if(e instanceof EventUpdate) {


			if(timerBoost.isEnabled() && !mc.gameSettings.keyBindJump.isKeyDown())
				mc.timer.timerSpeed = (float) timerAmount.getValue();

			if(mc.gameSettings.keyBindJump.isKeyDown() && !(mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ)) == Blocks.air) && !MovementUtil.isMoving() && !ServerUtil.isHypixel()) {
				//SpeedUtil.setSpeed(0);
				mc.thePlayer.motionY -= 0.8;
				mc.thePlayer.motionY = 0.22f;
				mc.timer.timerSpeed = 1.0f;
			}

			if(slowDown.isEnabled() || mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
				if(ServerUtil.isHypixel()) {
					mc.thePlayer.motionX *= 0.81;
					mc.thePlayer.motionZ *= 0.81;
				}
				if(ServerUtil.isRedesky()) {
					mc.thePlayer.motionX *= 0.87;
					mc.thePlayer.motionZ *= 0.87;
				}
				if(ServerUtil.isMineplex()) {
					mc.thePlayer.motionX *= 0.88;
					mc.thePlayer.motionZ *= 0.88;
				}
			}
		}

		if(e instanceof EventRender3D) {
			BlockPos renderPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
			for (double h = 0; h < extend.getValue(); h += 0.1) {
				renderPos = WorldUtil.getForwardBlock(h).add(0, -1, 0);
			}
			if(mc.theWorld.getBlockState(renderPos).getBlock() == Blocks.air) {
				RenderUtil.drawBox(renderPos, 1, 0, 0, true);
			} else {
				RenderUtil.drawBox(renderPos, 0, 1, 0, true);
			}
		}


		if (e instanceof EventMotion && e.isPre()) {

			BlockPos playerPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
			BlockUtils2.BlockData info = getBlockData(playerPos);

			if(!sprint.isEnabled())
				mc.thePlayer.setSprinting(false);


			if (lastBlockPos != null && lastFacing != null) {
				float[] keepRots = getRotationsNCP(info.pos, info.facing);
				if(rotationMode.is("NCP")) {
					keepRots = getRotationsNCP(info.pos, info.facing);
				} if(rotationMode.is("Hypixel")){
					keepRots = getRotationsHypixel(new Vec3(info.pos.getX(), info.pos.getY(), info.pos.getZ()));
				}
				lastYaw = keepRots[0];
				lastPitch = keepRots[1];
			}

			if(ServerUtil.isRedesky()) {
				if (mc.thePlayer.moveForward > 0) {
					((EventMotion) e).setYaw(lastYaw - 180);
				} else {
					((EventMotion) e).setYaw(lastYaw + 180);
				}
			} else {
				((EventMotion) e).setYaw(lastYaw);
			}
			((EventMotion) e).setPitch(lastPitch);

			if(playerPos == null) {
				//playerPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
				//if(!mc.gameSettings.keyBindSneak.isKeyDown()) {
					for (double h = 0; h < extend.getValue(); h += 0.1) {
						playerPos = WorldUtil.getForwardBlock(h).add(0, -1, 0);
					}
					//attempt at downward lmao
				/*} else {
					if(mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ)).getBlock() == Blocks.air) {
						playerPos = WorldUtil.getForwardBlock(1).add(0, -1, 0);
					}
				}*/
			}




			ItemStack stackToPlace = setStackToPlace();

			if (playerPos == null || info == null) {
				return;
			}

			if (mc.theWorld == null || mc.thePlayer == null)
				return;
			float[] rots = getRotationsNCP(info.pos, info.facing);
			if(rotationMode.is("NCP")) {
				rots = getRotationsNCP(info.pos, info.facing);
			} if(rotationMode.is("Hypixel")){
				rots = getRotationsHypixel(new Vec3(info.pos.getX(), info.pos.getY(), info.pos.getZ()));
			}
			((EventMotion) e).setYaw(rots[0]);
			((EventMotion) e).setPitch(rots[1]); //77.15837f
			lastYaw = ((EventMotion) e).yaw;
			lastPitch = ((EventMotion) e).pitch;
			if(timer.hasTimeElapsed((long) placeDelay.getValue(), false)) {
				mc.playerController.func_178890_a(mc.thePlayer, mc.theWorld, stackToPlace, info.pos, info.facing, RotationUtils.getVectorForRotation(((EventMotion) e).yaw, ((EventMotion) e).pitch));
				swingItem();
			}
		}
		if(e instanceof EventRenderPlayer) {
			BlockPos playerPos = null;
			if(playerPos == null) {
				for (double h = 0; h < extend.getValue(); h += 0.1) {
					playerPos = WorldUtil.getForwardBlock(h).add(0, -1, 0);
				}
			}
			assert playerPos != null;
			BlockUtils2.BlockData info = getBlockData(playerPos);
			float[] keepRots = getRotationsNCP(info.pos, info.facing);
			if(rotationMode.is("NCP")) {
				keepRots = getRotationsNCP(info.pos, info.facing);
			} if(rotationMode.is("Hypixel")){
				keepRots = getRotationsHypixel(new Vec3(info.pos.getX(), info.pos.getY(), info.pos.getZ()));
			}
			float renderYaw = keepRots[0];
			float renderPitch= keepRots[1];

			((EventRenderPlayer) e).setYaw(renderYaw);
			((EventRenderPlayer) e).setPitch(renderPitch);
		}

	}

	public float[] getRotationsHypixel(Vec3 hitVec) {

		EntityPlayerSP player = mc.thePlayer;
		double xDist = hitVec.xCoord - player.posX;
		double zDist = hitVec.zCoord - player.posZ;

		double yDist = hitVec.yCoord - player.posY + player.getEyeHeight();
		double fDist = MathHelper.sqrt_double(xDist * xDist + zDist * zDist);
		float rotationYaw = mc.thePlayer.rotationYaw;
		float var1 = (float)(StrictMath.atan2(zDist, xDist) * 180.0D / Math.PI) - 90.0F;

		float yaw = rotationYaw + MathHelper.wrapAngleTo180_float(var1 - rotationYaw);
		float rotationPitch = mc.thePlayer.rotationPitch;

		float var2 = (float)-(StrictMath.atan2(yDist, fDist) * 180.0D / Math.PI);
		float pitch = rotationPitch + MathHelper.wrapAngleTo180_float(var2 - rotationPitch);

		if(mc.gameSettings.keyBindJump.isKeyDown()) {
			pitch = -90;
		} else {
			pitch = rotationPitch + MathHelper.wrapAngleTo180_float(var2 - rotationPitch);
		}

		return new float[] { yaw, -pitch };

	}

	public float[] getRotationsNCP(BlockPos paramBlockPos, EnumFacing paramEnumFacing) {

		double d1 = paramBlockPos.getX() + 0.5D - mc.thePlayer.posX + paramEnumFacing.getFrontOffsetX() / 2.0D;
		double d2 = paramBlockPos.getZ() + 0.5D - mc.thePlayer.posZ + paramEnumFacing.getFrontOffsetZ() / 2.0D;
		double d3 = mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - (paramBlockPos.getY() + 0.5D);
		double d4 = MathHelper.sqrt_double(d1 * d1 + d2 * d2);
		float f1 = (float) (Math.atan2(d2, d1) * 180.0D / Math.PI) - 90.0F;
		float f2 = (float) (Math.atan2(d3, d4) * 180.0D / Math.PI);
		if (f1 < 0.0F) {
			f1 += 360.0F;
		}
		return new float[]{f1, f2};

	}

	private void swingItem() {
		if(swing.is("NoSwing")) {
			mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
		} else if(swing.is("Client")) {
			mc.thePlayer.swingItem();
		}
	}

	public BlockUtils2.BlockData getBlockData(BlockPos pos) {
		return (mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock() != Blocks.air) ?
				new BlockUtils2.BlockData(pos.add(0, -1, 0), EnumFacing.UP) : (
				(mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock() != Blocks.air) ?
						new BlockUtils2.BlockData(pos.add(-1, 0, 0), EnumFacing.EAST) : (
						(mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock() != Blocks.air) ?
								new BlockUtils2.BlockData(pos.add(1, 0, 0), EnumFacing.WEST) : (
								(mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock() != Blocks.air) ?
										new BlockUtils2.BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH) : (
										(mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock() != Blocks.air) ?
												new BlockUtils2.BlockData(pos.add(0, 0, 1), EnumFacing.NORTH) : null))));
	}

	public void updateHotbarHypixel() {
		ItemStack localItemStack = new ItemStack(Item.getItemById(261));
		try {
			for (int i = 36; i < 45; i++) {
				int j = i - 36;
				if ((!Container.canAddItemToSlot(mc.thePlayer.inventoryContainer.getSlot(i), localItemStack, true))
						&& ((mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemBlock)) && (mc.thePlayer.inventoryContainer.getSlot(i).getStack() != null)) {

					if (mc.thePlayer.inventory.currentItem == j) {
						break;
					}
					//mc.thePlayer.inventory.currentItem = j;
					this.currentItem = j;
					mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
					mc.playerController.updateController();
					break;
				}
			}
		} catch (Exception ignored) {
		}
	}

	public ItemStack setStackToPlace() {

		ItemStack block = mc.thePlayer.getCurrentEquippedItem();

		if (block != null && block.getItem() != null && !(block.getItem() instanceof ItemBlock)) {
			block = null;
		}

		int slot = mc.thePlayer.inventory.currentItem;

		for (short g = 0; g < 9; g++) {

			if (mc.thePlayer.inventoryContainer.getSlot(g + 36).getHasStack()
					&& mc.thePlayer.inventoryContainer.getSlot(g + 36).getStack().getItem() instanceof ItemBlock
					&& mc.thePlayer.inventoryContainer.getSlot(g + 36).getStack().stackSize != 0
					&& !((ItemBlock) mc.thePlayer.inventoryContainer.getSlot(g + 36).getStack().getItem()).getBlock()
					.getLocalizedName().toLowerCase().contains("chest")
					&& !((ItemBlock) mc.thePlayer.inventoryContainer.getSlot(g + 36).getStack().getItem()).getBlock()
					.getLocalizedName().toLowerCase().contains("table")
					&& (block == null
					|| (block.getItem() instanceof ItemBlock && mc.thePlayer.inventoryContainer.getSlot(g + 36).getStack().stackSize >= block.stackSize))) {

				//mc.thePlayer.inventory.currentItem = g;
				slot = g;
				block = mc.thePlayer.inventoryContainer.getSlot(g + 36).getStack();

			}

		}
		if (lastSlot != slot) {
			mc.getNetHandler().getNetworkManager().sendPacket(new C09PacketHeldItemChange(slot));
			lastSlot = slot;
		}
		return block;
	}

}