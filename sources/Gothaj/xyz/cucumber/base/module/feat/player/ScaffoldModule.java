package xyz.cucumber.base.module.feat.player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.function.ToDoubleFunction;

import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSnow;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import xyz.cucumber.base.Client;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.EventType;
import xyz.cucumber.base.events.ext.EventJump;
import xyz.cucumber.base.events.ext.EventLegitPlace;
import xyz.cucumber.base.events.ext.EventLook;
import xyz.cucumber.base.events.ext.EventMotion;
import xyz.cucumber.base.events.ext.EventMoveButton;
import xyz.cucumber.base.events.ext.EventMoveFlying;
import xyz.cucumber.base.events.ext.EventRenderRotation;
import xyz.cucumber.base.events.ext.EventRotation;
import xyz.cucumber.base.events.ext.EventSendPacket;
import xyz.cucumber.base.events.ext.EventTick;
import xyz.cucumber.base.events.ext.EventUpdate;
import xyz.cucumber.base.module.ArrayPriority;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.settings.BooleanSettings;
import xyz.cucumber.base.module.settings.ModeSettings;
import xyz.cucumber.base.module.settings.NumberSettings;
import xyz.cucumber.base.utils.MovementUtils;
import xyz.cucumber.base.utils.Timer;
import xyz.cucumber.base.utils.game.InventoryUtils;
import xyz.cucumber.base.utils.math.RotationUtils;

@ModuleInfo(category = Category.PLAYER, description = "Automatically builds for you", name = "Scaffold", priority = ArrayPriority.HIGH)
public class ScaffoldModule extends Mod {

	public int posY, spoofSlot, lastSlot, itemBefore, enabledTicks, ticksExisted, ticks;
	public boolean wasJump, adStrafeDirection;
	public float tellyYaw, lastYaw, lastPitch, scaffoldYaw, scaffoldPitch;

	public BlockPos blockPos;
	public EnumFacing facing;

	public Timer sneakTimer = new Timer();
	public Timer adStrafeTimer = new Timer();

	public ModeSettings rotations = new ModeSettings("Rotations", new String[] { "Static yaw", "Static yaw god",
			"Static god", "Static", "Polar", "Intave", "God", "Keep", "Snap", "Direct", "None" });
	public ModeSettings sprint = new ModeSettings("Sprint",
			new String[] { "Allways", "Off", "Legit", "Off ground speed", "Switch", "Switch No packet", "No packet" });
	public ModeSettings tower = new ModeSettings("Tower", new String[] { "None", "NCP", "Timer", "Intave" });
	public ModeSettings spoof = new ModeSettings("Spoof slot", new String[] { "None", "Normal", "Fake" });

	public BooleanSettings sneakWhenPlace = new BooleanSettings("Sneak when place", false);

	public NumberSettings expand = new NumberSettings("Expand", 0, 0, 8, 1);

	public BooleanSettings sneak = new BooleanSettings("Sneak", false);
	public NumberSettings sneakDelay = new NumberSettings("Sneak Delay", 4, 1, 20, 1);
	public NumberSettings sneakTime = new NumberSettings("Sneak Time", 2, 1, 6, 1);
	public BooleanSettings safeWalk = new BooleanSettings("Safe Walk", true);
	public BooleanSettings moveFix = new BooleanSettings("Move Fix", true);
	public BooleanSettings swing = new BooleanSettings("Swing", false);
	public BooleanSettings jump = new BooleanSettings("Jump", false);
	public BooleanSettings switchBack = new BooleanSettings("Switch Back", true);
	public BooleanSettings adStrafe = new BooleanSettings("A D Strafe", false);
	public BooleanSettings dragClick = new BooleanSettings("Drag Click", false);

	public ScaffoldModule() {

		this.addSettings(rotations, sprint, tower, spoof, expand, moveFix, jump, adStrafe, safeWalk, sneakWhenPlace,
				sneak, sneakDelay, sneakTime, dragClick, swing, switchBack);
	}

	public void onDisable() {		
		if (switchBack.isEnabled() && spoof.getMode().equalsIgnoreCase("None")) {
			mc.thePlayer.inventory.currentItem = itemBefore;
		}
		if (spoof.getMode().equalsIgnoreCase("Fake")) {
			if (mc.thePlayer.inventory.currentItem != lastSlot)
				mc.getNetHandler().getNetworkManager()
						.sendPacketNoEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
		}

		if (!Client.INSTANCE.getModuleManager().getModule(SmoothRotationModule.class).isEnabled())
			RotationUtils.customRots = false;
		mc.gameSettings.keyBindSneak.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode());
		mc.timer.timerSpeed = 1f;

		enabledTicks = 11;
	}

	public void onEnable() {
		lastSlot = mc.thePlayer.inventory.currentItem;

		enabledTicks = 0;

		if (!RotationUtils.customRots) {
			RotationUtils.serverYaw = mc.thePlayer.rotationYaw;
			RotationUtils.serverPitch = mc.thePlayer.rotationPitch;
		}

		itemBefore = mc.thePlayer.inventory.currentItem;
		lastYaw = mc.thePlayer.rotationYaw - 180;
		lastPitch = rotations.getMode().equalsIgnoreCase("Polar") ? 82f : 90f;
		posY = (int) (mc.thePlayer.posY - 1);
		spoofSlot = mc.thePlayer.inventory.currentItem;

		scaffoldYaw = mc.thePlayer.rotationYaw - 180;
		scaffoldPitch = rotations.getMode().equalsIgnoreCase("Polar") ? 82f : 90f;
		tellyYaw = 0;

		sprint();

		int item = InventoryUtils.getBlockSlot();
		if (item == -1) {
			return;
		}

		processBlockData();
		rotations();
	}

	@EventListener
	public void onMoveButton(EventMoveButton e) {
		int item = InventoryUtils.getBlockSlot();
		if (item == -1) {
			return;
		}

		if (sneakTimer.hasTimeElapsed(sneakDelay.getValue() * 100, false) && sneak.isEnabled()) {
			e.sneak = true;
			if (sneakTimer.hasTimeElapsed(((sneakDelay.getValue() * 100) + (sneakTime.getValue()) * 50), true)) {
			}
		}
		if (safeWalk.isEnabled() && mc.thePlayer.onGround) {
			if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
					mc.thePlayer.getEntityBoundingBox()
							.addCoord(mc.thePlayer.motionX, mc.thePlayer.motionY, mc.thePlayer.motionZ)
							.expand(-0.175, 0, -0.175))
					.isEmpty()) {
				e.sneak = true;
			}
		}
		if (adStrafe.isEnabled()) {
			if (adStrafeTimer.hasTimeElapsed(125, true)) {
				adStrafeDirection = !adStrafeDirection;
			}
			if (MovementUtils.isMoving() && !Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode())
					&& !Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode())) {
				if (adStrafeDirection) {
					e.left = true;
				} else {
					e.right = true;
				}
			}
		}
	}

	@EventListener
	public void onSendPacket(EventSendPacket e) {
		if (e.getPacket() instanceof C09PacketHeldItemChange && spoof.getMode().equalsIgnoreCase("Fake")) {
			e.setCancelled(true);
		}
	}

	@EventListener
	public void onLook(EventLook e) {
		if (RotationUtils.customRots) {
			e.setYaw(RotationUtils.serverYaw);
			e.setPitch(RotationUtils.serverPitch);
		}
	}

	@EventListener
	public void onRenderRotation(EventRenderRotation e) {
		if (RotationUtils.customRots) {
			e.setYaw(RotationUtils.serverYaw);
			e.setPitch(RotationUtils.serverPitch);
		}
	}

	@EventListener
	public void onMotion(EventMotion e) {
		int item = InventoryUtils.getBlockSlot();
		if (item == -1) {
			return;
		}

		if (e.getType() == EventType.PRE) {
			switch (tower.getMode().toLowerCase()) {
			case "ncp":
				if (mc.thePlayer.posY % 1 <= 0.00153598 && mc.gameSettings.keyBindJump.pressed) {
					mc.thePlayer.setPosition(mc.thePlayer.posX, Math.floor(mc.thePlayer.posY), mc.thePlayer.posZ);
					mc.thePlayer.motionY = 0.41998;
				} else if (mc.thePlayer.posY % 1 < 0.1
						&& (mc.thePlayer.onGround && mc.gameSettings.keyBindJump.pressed)) {
					mc.thePlayer.setPosition(mc.thePlayer.posX, Math.floor(mc.thePlayer.posY), mc.thePlayer.posZ);
				}
				break;
			case "timer":
				if (mc.gameSettings.keyBindJump.pressed) {
					mc.timer.timerSpeed = 1.25f;
				}
				break;
			}

			EventMotion event = (EventMotion) e;

			if (RotationUtils.customRots) {
				event.setYaw(RotationUtils.serverYaw);
				event.setPitch(RotationUtils.serverPitch);
			}
		}

	}

	@EventListener
	public void onJump(EventJump e) {
		int item = InventoryUtils.getBlockSlot();
		if (item == -1) {
			return;
		}

		if (tower.getMode().toLowerCase().equals("intave")) {
			e.setMotionY(0.41);
		}
	}

	@EventListener
	public void onTick(EventTick e) {
				
		int item = InventoryUtils.getBlockSlot();
		if (item == -1) {
			return;
		}

		enabledTicks++;
		mc.timer.timerSpeed = 1f;
		mc.thePlayer.jumpTicks = 0;

		sprint();

		// posY
		if (jump.isEnabled()) {
			if (mc.gameSettings.keyBindJump.pressed) {
				posY = (int) (mc.thePlayer.posY - 1);
			}
		} else {
			posY = (int) (mc.thePlayer.posY - 1);
		}

		// jump
		if ((jump.isEnabled())) {
			if (mc.thePlayer.motionX != 0 && mc.thePlayer.motionZ != 0 && mc.thePlayer.onGround) {
				if (!Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode())) {
					mc.thePlayer.jump();
				}
				wasJump = true;
			}
		}
	}

	@EventListener
	public void onMoveFlying(EventMoveFlying e) {
		ticksExisted = mc.thePlayer.ticksExisted;

		processBlockData();

		int item = InventoryUtils.getBlockSlot();
		if (item == -1) {
			return;
		}

		rotations();

		if (moveFix.isEnabled() && RotationUtils.customRots) {
			e.setCancelled(true);
			MovementUtils.silentMoveFix(e);
		}

		if (dragClick.isEnabled())
			fakeClick();

		place();
	}

	public void fakeClick() {
		if (ticks++ <= 2) {
			mc.getNetHandler().getNetworkManager()
					.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
		}
	}

	public void sprint() {

		switch (sprint.getMode().toLowerCase()) {
		case "legit":
			if (MovementUtils.isMoving() && mc.gameSettings.keyBindForward.pressed
					&& Math.abs(MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw)
							- MathHelper.wrapAngleTo180_float(RotationUtils.serverYaw)) < 66.5) {
				mc.thePlayer.setSprinting(true);
				mc.gameSettings.keyBindSprint.pressed = true;
			} else {
				mc.gameSettings.keyBindSprint.pressed = false;
				mc.thePlayer.setSprinting(false);
			}
			break;
		case "off":
			mc.gameSettings.keyBindSprint.pressed = false;
			mc.thePlayer.setSprinting(false);
			break;
		case "allways":
			mc.gameSettings.keyBindSprint.pressed = true;
			if (mc.thePlayer.moveForward > 0) {
				mc.thePlayer.setSprinting(true);
			}
			break;
		case "off ground speed":
			if (mc.thePlayer.ticksExisted % 3 == 0 && MovementUtils.isMoving() && MovementUtils.getSpeed() < 0.23f
					&& !mc.thePlayer.onGround && !mc.gameSettings.keyBindJump.pressed && mc.thePlayer.hurtTime == 0) {
				MovementUtils.strafe(0.25f);
			}

			mc.gameSettings.keyBindSprint.pressed = false;
			mc.thePlayer.setSprinting(false);
			break;
		case "no packet":
			mc.gameSettings.keyBindSprint.pressed = true;
			mc.thePlayer.setSprinting(false);
			break;

		case "switch":
			mc.gameSettings.keyBindSprint.pressed = mc.thePlayer.ticksExisted % 2 == 0;
			mc.thePlayer.setSprinting(mc.thePlayer.ticksExisted % 2 == 0);
			break;
		case "switch no packet":
			mc.gameSettings.keyBindSprint.pressed = mc.thePlayer.ticksExisted % 2 == 0;
			mc.thePlayer.setSprinting(false);
			break;
		}
	}

	public void rotations() {

		boolean stop = false;

		float currentYaw = (float) Math.toDegrees(MovementUtils.getDirectionKeybinds(mc.thePlayer.rotationYaw - 180));

		float currentPitch = lastPitch;

		switch (rotations.getMode().toLowerCase()) {
		case "direct":
			currentYaw += 180;
			if (blockPos != null && facing != null) {
				for (float pitch = 90; pitch > 30; pitch -= 1) {
					if (RotationUtils.lookingAtBlock(blockPos, mc.thePlayer.rotationYaw, pitch, facing, false)) {
						currentPitch = pitch;
					}
				}
			}
			break;
		case "snap":
			if (mc.theWorld.isAirBlock(new BlockPos(mc.thePlayer.posX, posY, mc.thePlayer.posZ))) {
				if (blockPos != null && facing != null) {
					float[] rots = RotationUtils.getDirectionToBlock(blockPos.getX(), blockPos.getY(), blockPos.getZ(),
							facing);
					currentYaw = rots[0];
					currentPitch = rots[1];
				}
			} else {
				currentYaw = mc.thePlayer.rotationYaw;
				currentPitch = mc.thePlayer.rotationPitch;
			}
			break;
		case "keep":
			boolean found = false;
			if (mc.theWorld.isAirBlock(new BlockPos(mc.thePlayer.posX, posY, mc.thePlayer.posZ))) {
				if (blockPos != null && facing != null) {
					float[] rots = RotationUtils.getDirectionToBlock(blockPos.getX(), blockPos.getY(), blockPos.getZ(),
							facing);
					scaffoldYaw = rots[0];
					scaffoldPitch = RotationUtils.getYawBasedPitch(blockPos, facing, scaffoldYaw, lastPitch, false);
				}
			}
			currentYaw = scaffoldYaw;
			currentPitch = scaffoldPitch;
			break;
		case "intave":
			int ticks = 0;

			if (blockPos != null && facing != null) {
				scaffoldPitch = RotationUtils.getYawBasedPitch(blockPos, facing, currentYaw, lastPitch, false);

				if (!RotationUtils.lookingAtBlock(blockPos, currentYaw, scaffoldPitch, facing, false)) {
					float[] rots = RotationUtils.getDirectionToBlock(blockPos.getX(), blockPos.getY(), blockPos.getZ(),
							facing);

					int maxTicks = (int) Math.abs(MathHelper.wrapAngleTo180_float(scaffoldYaw - rots[0]) / 4);

					while (ticks <= maxTicks && !stop) {
						scaffoldYaw = RotationUtils.updateRotation(scaffoldYaw, rots[0], 5);
						scaffoldPitch = RotationUtils.getYawBasedPitch(blockPos, facing, scaffoldYaw, lastPitch, false);

						if (RotationUtils.lookingAtBlock(blockPos, scaffoldYaw, scaffoldPitch, facing, true)) {
							stop = true;
						}
						ticks++;
					}
				}
			}

			if (!stop) {
				if (ticksExisted == mc.thePlayer.ticksExisted) {
					scaffoldYaw = RotationUtils.updateRotation(scaffoldYaw, currentYaw, 20);
					ticksExisted++;
				}
			}

			currentYaw = scaffoldYaw;
			currentPitch = scaffoldPitch;
			break;
		case "god":
			ticks = 0;

			currentYaw += 45;

			if (blockPos != null && facing != null) {
				scaffoldPitch = RotationUtils.getYawBasedPitch(blockPos, facing, currentYaw, lastPitch, false);

				if (!RotationUtils.lookingAtBlock(blockPos, currentYaw, scaffoldPitch, facing, false)) {
					float[] rots = RotationUtils.getDirectionToBlock(blockPos.getX(), blockPos.getY(), blockPos.getZ(),
							facing);

					int maxTicks = (int) Math.abs(MathHelper.wrapAngleTo180_float(scaffoldYaw - rots[0]) / 4);

					while (ticks <= maxTicks && !stop) {
						scaffoldYaw = RotationUtils.updateRotation(scaffoldYaw, rots[0], 5);
						scaffoldPitch = RotationUtils.getYawBasedPitch(blockPos, facing, scaffoldYaw, lastPitch, false);

						if (RotationUtils.lookingAtBlock(blockPos, scaffoldYaw, scaffoldPitch, facing, true)) {
							stop = true;
						}
						ticks++;
					}
				}
			}

			if (!stop) {
				if (ticksExisted == mc.thePlayer.ticksExisted) {
					scaffoldYaw = RotationUtils.updateRotation(scaffoldYaw, currentYaw, 20);
					ticksExisted++;
				}
			}

			currentYaw = scaffoldYaw;
			currentPitch = scaffoldPitch;
			break;
		case "static yaw":
			if (blockPos != null && facing != null) {
				if (!RotationUtils.lookingAtBlock(blockPos, currentYaw, lastPitch, facing, true)) {
					scaffoldPitch = RotationUtils.getYawBasedPitch(blockPos, facing, currentYaw, lastPitch, false);
				}
			}
			if (ticksExisted == mc.thePlayer.ticksExisted) {
				scaffoldYaw = RotationUtils.updateRotation(scaffoldYaw, currentYaw, 20);
				ticksExisted++;
			}

			currentYaw = scaffoldYaw;
			currentPitch = scaffoldPitch;
			break;
		case "static yaw god":
			currentYaw += 45;
			if (blockPos != null && facing != null) {
				if (!RotationUtils.lookingAtBlock(blockPos, currentYaw, lastPitch, facing, true)) {
					scaffoldPitch = RotationUtils.getYawBasedPitch(blockPos, facing, currentYaw, lastPitch, true);
				}
			}

			if (ticksExisted == mc.thePlayer.ticksExisted) {
				scaffoldYaw = RotationUtils.updateRotation(scaffoldYaw, currentYaw, 20);
				ticksExisted++;
			}

			currentYaw = scaffoldYaw;
			currentPitch = scaffoldPitch;
			break;
		case "static god":
			currentYaw += 45;
			if (blockPos != null && facing != null) {
				if (RotationUtils.lookingAtBlock(blockPos, currentYaw, lastPitch, facing, true))
					return;
				scaffoldPitch = MovementUtils.isGoingDiagonally() ? 75.5f : 77f;
			}

			if (ticksExisted == mc.thePlayer.ticksExisted) {
				scaffoldYaw = RotationUtils.updateRotation(scaffoldYaw, currentYaw, 20);
				ticksExisted++;
			}

			currentYaw = scaffoldYaw;
			currentPitch = scaffoldPitch;
			break;
		case "static":
			if (blockPos != null && facing != null) {
				scaffoldPitch = MovementUtils.isGoingDiagonally() ? 77.5f : 79.5f;
			}

			if (ticksExisted == mc.thePlayer.ticksExisted) {
				scaffoldYaw = RotationUtils.updateRotation(scaffoldYaw, currentYaw, 20);
				ticksExisted++;
			}

			currentYaw = scaffoldYaw;
			currentPitch = scaffoldPitch;
			break;
		case "polar":

			// currentYaw += 45;

			if (blockPos != null && facing != null) {
				if (!RotationUtils.lookingAtBlock(blockPos, currentYaw, lastPitch, facing, true)) {
					scaffoldPitch = RotationUtils.getYawBasedPitch(blockPos, facing, currentYaw, lastPitch, true);
				}
			}

			if (ticksExisted == mc.thePlayer.ticksExisted) {
				scaffoldYaw = RotationUtils.updateRotation(scaffoldYaw, currentYaw, 20);
				ticksExisted++;
			}

			if (lastPitch != scaffoldPitch) {
				mc.thePlayer.rotationYaw += Math.random() - Math.random();
			}

			if (lastYaw != scaffoldYaw) {
				scaffoldPitch += Math.random() - Math.random();
			}

			currentYaw = scaffoldYaw;
			currentPitch = scaffoldPitch;
			break;
		}

		if (!Client.INSTANCE.getModuleManager().getModule(SmoothRotationModule.class).isEnabled()
				|| enabledTicks > 10) {
			RotationUtils.serverYaw = currentYaw;
			RotationUtils.serverPitch = currentPitch;
			RotationUtils.customRots = true;
		}

		lastYaw = currentYaw;
		lastPitch = currentPitch;
	}

	public void place() {
		if (sneakWhenPlace.isEnabled()) {
			mc.gameSettings.keyBindSneak.pressed = false;
		}

		if (blockPos == null || facing == null)
			return;

		MovingObjectPosition ray = mc.thePlayer.rayTraceCustom(mc.playerController.getBlockReachDistance(),
				mc.timer.renderPartialTicks, RotationUtils.serverYaw, RotationUtils.serverPitch);

		BlockPos bp = blockPos;
		EnumFacing ef = facing;
		Vec3 hv = ray.hitVec;

		if (!rotations.getMode().equalsIgnoreCase("Direct") && !rotations.getMode().equalsIgnoreCase("None")
				&& expand.getValue() == 0) {
			if (!RotationUtils.lookingAtBlock(bp, RotationUtils.serverYaw, RotationUtils.serverPitch, ef, true)) {
				return;
			}
		}

		int item = InventoryUtils.getBlockSlot();
		if (item == -1) {
			RotationUtils.serverYaw = mc.thePlayer.rotationYaw;
			RotationUtils.serverPitch = mc.thePlayer.rotationPitch;
			RotationUtils.customRots = false;
			return;
		}
		ItemStack stack = mc.thePlayer.inventory.getStackInSlot(item);

		if (!spoof.getMode().equalsIgnoreCase("Fake")) {
			if(spoof.getMode().equalsIgnoreCase("Normal")) {
				spoofSlot = mc.thePlayer.inventory.currentItem;
				if(spoofSlot != item)mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C09PacketHeldItemChange(item));
			}else {
				mc.thePlayer.inventory.currentItem = item;
			}
		} else {
			if (item != lastSlot) {
				mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C09PacketHeldItemChange(item));
				lastSlot = item;
			}
		}

		if (sneakWhenPlace.isEnabled()) {
			mc.gameSettings.keyBindSneak.pressed = true;
		}

		if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, stack, bp, ef, hv)) {
			if (swing.isEnabled()) {
				mc.thePlayer.swingItem();
			} else {
				mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C0APacketAnimation());
			}

			ticks = 0;
		}

		if (spoof.getMode().equalsIgnoreCase("Normal")) {
			if(item != spoofSlot)mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C09PacketHeldItemChange(spoofSlot));
		}
	}

	public void processBlockData() {
		BlockPos position = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);

		if (expand.getValue() == 0) {
			blockPos = getBlockPos(mc.thePlayer.posX, mc.thePlayer.posZ);
		} else {
			Vec3 vec = expand(new Vec3(mc.thePlayer.posX, posY, mc.thePlayer.posZ));
			setBlockFacingOld(new BlockPos(vec.xCoord, vec.yCoord + 1, vec.zCoord));
		}

		if (blockPos != null) {
			if (expand.getValue() == 0) {
				facing = getPlaceSide(mc.thePlayer.posX, mc.thePlayer.posZ);
			}
		}
	}

	public static boolean isPosSolid(BlockPos pos) {
		Block block = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
		if ((block.getMaterial().isSolid() || !block.isTranslucent() || block instanceof BlockLadder
				|| block instanceof BlockCarpet || block instanceof BlockSnow || block instanceof BlockSkull)
				&& !block.getMaterial().isLiquid() && !(block instanceof BlockContainer)) {
			return true;
		}
		return false;
	}

	private class BlockData {
		public BlockPos position, targetPos;
		public EnumFacing face;
	}

	private Vec3 expand(Vec3 position) {
		if (expand.getValue() > 0) {
			final double direction = MovementUtils.getDirection(mc.thePlayer.rotationYaw);
			final Vec3 expandVector = new Vec3(-Math.sin(direction), 0, Math.cos(direction));
			int bestExpand = 0;
			for (int i = 0; i < expand.getValue(); i++) {
				if (!MovementUtils.isMoving())
					break;

				Vec3 vec = position.addVector(0, -1, 0).add(expandVector.multiply(i));

				setBlockFacingOld(new BlockPos(vec.xCoord, posY, vec.zCoord));

				if (blockPos != null && facing != EnumFacing.UP) {
					bestExpand = i;
				}
			}
			position = position.add(expandVector.multiply(bestExpand));
			position.yCoord = posY - 1;
		}
		return position;
	}

	public void setBlockFacingOld(BlockPos pos) {
		if (mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock() != Blocks.air) {
			this.blockPos = pos.add(0, -1, 0);
			facing = EnumFacing.UP;
		} else if (mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock() != Blocks.air) {
			this.blockPos = pos.add(-1, 0, 0);
			facing = EnumFacing.EAST;
		} else if (mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock() != Blocks.air) {
			this.blockPos = pos.add(1, 0, 0);
			facing = EnumFacing.WEST;
		} else if (mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock() != Blocks.air) {
			this.blockPos = pos.add(0, 0, -1);
			facing = EnumFacing.SOUTH;
		} else if (mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock() != Blocks.air) {
			this.blockPos = pos.add(0, 0, 1);
			facing = EnumFacing.NORTH;
		} else if (mc.theWorld.getBlockState(pos.add(-1, 0, -1)).getBlock() != Blocks.air) {
			facing = EnumFacing.EAST;
			this.blockPos = pos.add(-1, 0, -1);
		} else if (mc.theWorld.getBlockState(pos.add(1, 0, 1)).getBlock() != Blocks.air) {
			facing = EnumFacing.WEST;
			this.blockPos = pos.add(1, 0, 1);
		} else if (mc.theWorld.getBlockState(pos.add(1, 0, -1)).getBlock() != Blocks.air) {
			facing = EnumFacing.SOUTH;
			this.blockPos = pos.add(1, 0, -1);
		} else if (mc.theWorld.getBlockState(pos.add(-1, 0, 1)).getBlock() != Blocks.air) {
			facing = EnumFacing.NORTH;
			this.blockPos = pos.add(-1, 0, 1);
		} else if (mc.theWorld.getBlockState(pos.add(0, -1, 1)).getBlock() != Blocks.air) {
			this.blockPos = pos.add(0, -1, 1);
			facing = EnumFacing.UP;
		} else if (mc.theWorld.getBlockState(pos.add(0, -1, -1)).getBlock() != Blocks.air) {
			this.blockPos = pos.add(0, -1, -1);
			facing = EnumFacing.UP;
		} else if (mc.theWorld.getBlockState(pos.add(1, -1, 0)).getBlock() != Blocks.air) {
			this.blockPos = pos.add(1, -1, 0);
			facing = EnumFacing.UP;
		} else if (mc.theWorld.getBlockState(pos.add(-1, -1, 0)).getBlock() != Blocks.air) {
			this.blockPos = pos.add(-1, -1, 0);
			facing = EnumFacing.UP;
		}
	}

	private BlockPos getBlockPos(double posX, double posZ) {
		BlockPos playerPos = new BlockPos(posX, posY, posZ);
		ArrayList<Vec3> positions = new ArrayList<Vec3>();
		HashMap<Vec3, BlockPos> hashMap = new HashMap<Vec3, BlockPos>();

		for (int y = playerPos.getY() - 1; y <= playerPos.getY(); ++y) {
			for (int x = playerPos.getX() - 5; x <= playerPos.getX() + 5; ++x) {
				for (int z = playerPos.getZ() - 5; z <= playerPos.getZ() + 5; ++z) {
					if (isValidBock(new BlockPos(x, y, z))) {
						BlockPos blockPos = new BlockPos(x, y, z);
						Block block = mc.theWorld.getBlockState(blockPos).getBlock();
						double ex = MathHelper.clamp_double(posX, blockPos.getX(),
								blockPos.getX() + block.getBlockBoundsMaxX());
						double ey = MathHelper.clamp_double(posY + 1, blockPos.getY(),
								blockPos.getY() + block.getBlockBoundsMaxY());
						double ez = MathHelper.clamp_double(posZ, blockPos.getZ(),
								blockPos.getZ() + block.getBlockBoundsMaxZ());
						Vec3 vec3 = new Vec3(ex, ey, ez);
						positions.add(vec3);
						hashMap.put(vec3, blockPos);
					}
				}
			}
		}
		if (positions.isEmpty()) {
			return null;
		}
		positions.sort(Comparator.comparingDouble((ToDoubleFunction<? super Vec3>) this::getBestBlock));
		return hashMap.get(positions.get(0));
	}

	private EnumFacing getPlaceSide(double posX, double posZ) {
		ArrayList<Vec3> positions = new ArrayList<Vec3>();
		HashMap<Vec3, EnumFacing> hashMap = new HashMap<Vec3, EnumFacing>();
		BlockPos playerPos = new BlockPos(posX, posY + 1, posZ);
		if (!isPosSolid(blockPos.add(0, 1, 0)) && !blockPos.add(0, 1, 0).equalsBlockPos(playerPos)
				&& !mc.thePlayer.onGround) {
			BlockPos pos = new BlockPos(posX, posY, posZ);
			if (jump.isEnabled()) {
				if (mc.gameSettings.keyBindJump.pressed) {
					BlockPos bp = blockPos.add(0, 1, 0);
					Vec3 vec4 = this.getBestHitFeet(bp);
					positions.add(vec4);
					hashMap.put(vec4, EnumFacing.UP);
				}
			} else {
				BlockPos bp = blockPos.add(0, 1, 0);
				Vec3 vec4 = this.getBestHitFeet(bp);
				positions.add(vec4);
				hashMap.put(vec4, EnumFacing.UP);
			}
		}
		if (!isPosSolid(blockPos.add(1, 0, 0)) && !blockPos.add(1, 0, 0).equalsBlockPos(playerPos)) {
			BlockPos bp = blockPos.add(1, 0, 0);
			Vec3 vec4 = this.getBestHitFeet(bp);
			positions.add(vec4);
			hashMap.put(vec4, EnumFacing.EAST);
		}
		if (!isPosSolid(blockPos.add(-1, 0, 0)) && !blockPos.add(-1, 0, 0).equalsBlockPos(playerPos)) {
			BlockPos bp = blockPos.add(-1, 0, 0);
			Vec3 vec4 = this.getBestHitFeet(bp);
			positions.add(vec4);
			hashMap.put(vec4, EnumFacing.WEST);
		}
		if (!isPosSolid(blockPos.add(0, 0, 1)) && !blockPos.add(0, 0, 1).equalsBlockPos(playerPos)) {
			BlockPos bp = blockPos.add(0, 0, 1);
			Vec3 vec4 = this.getBestHitFeet(bp);
			positions.add(vec4);
			hashMap.put(vec4, EnumFacing.SOUTH);
		}
		if (!isPosSolid(blockPos.add(0, 0, -1)) && !blockPos.add(0, 0, -1).equalsBlockPos(playerPos)) {
			BlockPos bp = blockPos.add(0, 0, -1);
			Vec3 vec4 = this.getBestHitFeet(bp);
			positions.add(vec4);
			hashMap.put(vec4, EnumFacing.NORTH);
		}
		positions.sort(
				Comparator.comparingDouble(vec3 -> mc.thePlayer.getDistance(vec3.xCoord, vec3.yCoord, vec3.zCoord)));
		if (!positions.isEmpty()) {
			Vec3 vec5 = this.getBestHitFeet(blockPos);
			if (mc.thePlayer.getDistance(vec5.xCoord, vec5.yCoord, vec5.zCoord) >= mc.thePlayer
					.getDistance(positions.get(0).xCoord, positions.get(0).yCoord, positions.get(0).zCoord)) {
				return hashMap.get(positions.get(0));
			}
		}
		return null;
	}

	private Vec3 getBestHitFeet(final BlockPos blockPos) {
		final Block block = mc.theWorld.getBlockState(blockPos).getBlock();
		final double ex = MathHelper.clamp_double(mc.thePlayer.posX, blockPos.getX(),
				blockPos.getX() + block.getBlockBoundsMaxX());
		final double ey = MathHelper.clamp_double(mc.thePlayer.posY, blockPos.getY(),
				blockPos.getY() + block.getBlockBoundsMaxY());
		final double ez = MathHelper.clamp_double(mc.thePlayer.posZ, blockPos.getZ(),
				blockPos.getZ() + block.getBlockBoundsMaxZ());
		return new Vec3(ex, ey, ez);
	}

	private double getBestBlock(Vec3 vec3) {
		return mc.thePlayer.getDistanceSq(vec3.xCoord, vec3.yCoord, vec3.zCoord);
	}

	public static boolean isValidBock(BlockPos blockPos) {
		Block block = Minecraft.getMinecraft().theWorld.getBlockState(blockPos).getBlock();
		return !(block instanceof BlockLiquid) && !(block instanceof BlockAir) && !(block instanceof BlockChest)
				&& !(block instanceof BlockFurnace);
	}
}
