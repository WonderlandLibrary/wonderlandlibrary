package best.azura.client.impl.module.impl.player;

import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.impl.events.*;
import best.azura.client.impl.module.impl.movement.Sprint;
import best.azura.client.impl.module.impl.other.ClientModule;
import best.azura.client.impl.module.impl.player.scaffold.Direction;
import best.azura.client.impl.ui.font.Fonts;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.impl.value.ModeValue;
import best.azura.client.impl.value.NumberValue;
import best.azura.client.impl.value.dependency.BooleanDependency;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.other.DelayUtil;
import best.azura.client.util.other.ServerUtil;
import best.azura.client.util.player.MovementUtil;
import best.azura.client.util.player.RotationUtil;
import best.azura.client.util.profiler.SimpleProfiler;
import best.azura.client.util.render.RenderUtil;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unused")
@ModuleInfo(name = "Scaffold", description = "Build a bridge automatically.", category = Category.PLAYER)
public class Scaffold extends Module {

	public final BooleanValue legit = new BooleanValue("Legit", "Use raytracing and better rotations.", true);
	public final ModeValue rotationMode = new ModeValue("Rotation Mode", "Choose a rotation mode.", new BooleanDependency(legit, false), "Backwards", "Backwards", "Forwards", "Matrix", "NCP", "Watchdog", "None");
	public final ModeValue legitMode = new ModeValue("Legit Mode", "Choose a rotation mode.", new BooleanDependency(legit, true), "Godbridge", "Godbridge", "AAC3");
	public final BooleanValue movementFix = new BooleanValue("Move fix", "Move to your rotation.", false);
	public final BooleanValue silentFix = new BooleanValue("Silent move fix", "Move to your rotation silently.", new BooleanDependency(movementFix, true), false);
	private final ModeValue placeEvent = new ModeValue("Place Event", "Select the event in which you place blocks", "Pre", "Pre", "Post");
	public final BooleanValue sprint = new BooleanValue("Sprint", "Sprint while scaffolding.", true);
	public final BooleanValue intaveSprint = new BooleanValue("Intave Sprint", "Sprint while scaffolding (for intave).", sprint::getObject, false);
	public final BooleanValue karhuFix = new BooleanValue("More click", "Click more.", false);
	public final NumberValue<Double> expandRange = new NumberValue<>("Expand range", "Select how far the scaffold should expand.", 0.5, 0.1, 0.0, 15.0);
	public final BooleanValue sideCheck = new BooleanValue("Side check", "Checks for blocks on the side. (Good for diagonal)", true);
	public final NumberValue<Double> checkRange = new NumberValue<>("Check range", "Select how far the scaffold should expand.", new BooleanDependency(sideCheck, true), 0.05, 0.01, 0.01, 0.1);
	private final BooleanValue fullSilent = new BooleanValue("Full Silent", "Makes the scaffold silent.", false);
	private final BooleanValue fakeSilent = new BooleanValue("Fake Silent", "Makes the scaffold look like it's silent.", () -> !fullSilent.getObject(), false);
	private final BooleanValue silentSwing = new BooleanValue("Silent Swing", "Silently swing your arm.", false);
	private final ModeValue towerMode = new ModeValue("Tower Mode", "Change the mode of the tower", "None", "None", "Watchdog", "Watchdog New", "Watchdog Test", "TP", "TP2");
	private final NumberValue<Float> watchdogTowerBoost = new NumberValue<>("Tower Boost", "Apply a timer boost to your tower", () -> towerMode.getObject().equals("Watchdog") || towerMode.getObject().equals("Watchdog Test"), 1.0F, 0.1F, 1.0F, 2.0F);
	private final BooleanValue watchdogMovingTower = new BooleanValue("Moving Tower", "Continue towering when moving", () -> towerMode.getObject().equals("Watchdog") || towerMode.getObject().equals("Watchdog Test"), true);
	private final BooleanValue renderDebug = new BooleanValue("Debug Render", "Renders debug boxes", true);
	private final BooleanValue safely = new BooleanValue("Safe walk", "Keeps you from potentially falling of the block", true);
	private final BooleanValue keepRotations = new BooleanValue("Keep Rotations", "Keeps your rotations", true);
	private final BooleanValue hypixelBypass = new BooleanValue("Hypixel Bypass", "Bypass hypixel scaffold checks", ServerUtil::isHypixel, true);
	private boolean calculatedPlacementState;

	private float yaw, pitch;
	private BlockPos green, red;
	private EnumFacing facing;
	private int prevSlot = -1;
	private final DelayUtil timer = new DelayUtil();
	private boolean wasTowering;
	private int sprintTicks;

	public static final List<Block> invalidBlocks = Arrays.asList(
			Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.wall_banner,
			Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars,
			Blocks.snow_layer, Blocks.ice, Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore,
			Blocks.chest, Blocks.ender_chest, Blocks.torch, Blocks.redstone_torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox, Blocks.tnt, Blocks.lit_redstone_ore,
			Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate,
			Blocks.stone_button, Blocks.wooden_button, Blocks.lever, Blocks.furnace, Blocks.lit_furnace, Blocks.crafting_table,
			Blocks.acacia_fence, Blocks.acacia_fence_gate, Blocks.birch_fence, Blocks.birch_fence_gate, Blocks.dark_oak_fence, Blocks.dark_oak_fence_gate, Blocks.jungle_fence, Blocks.jungle_fence_gate, Blocks.oak_fence, Blocks.oak_fence_gate,
			Blocks.acacia_door, Blocks.birch_door, Blocks.dark_oak_door, Blocks.iron_door, Blocks.tallgrass, Blocks.brown_mushroom, Blocks.red_mushroom, Blocks.double_plant, Blocks.jungle_door, Blocks.oak_door, Blocks.spruce_door,
			Blocks.rail, Blocks.activator_rail, Blocks.detector_rail, Blocks.golden_rail, Blocks.brewing_stand, Blocks.red_flower, Blocks.yellow_flower, Blocks.flower_pot, Blocks.beacon, Blocks.sand,
			Blocks.web, Blocks.waterlily, Blocks.slime_block, Blocks.cactus, Blocks.anvil, Blocks.sapling, Blocks.tallgrass, Blocks.double_plant, Blocks.brown_mushroom, Blocks.brown_mushroom_block,
			Blocks.red_mushroom_block, Blocks.red_mushroom, Blocks.double_stone_slab, Blocks.stone_slab, Blocks.double_wooden_slab, Blocks.wooden_slab, Blocks.stone_slab2, Blocks.ladder,
			Blocks.standing_banner, Blocks.wall_banner, Blocks.standing_sign, Blocks.wall_sign, Blocks.tripwire_hook, Blocks.tripwire);

	@Override
	public void onEnable() {
		super.onEnable();
		prevSlot = mc.thePlayer.inventory.currentItem;
		red = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
		sprintTicks = 0;
	}

	@EventHandler
	public final Listener<EventJump> eventJumpListener1 = e -> {
		if (movementFix.getObject() && prevSlot != -1) {
			final float[] movementValues = RotationUtil.getSilentMovementValues(yaw);
			final float moveForward = movementValues[0], moveStrafing = movementValues[1];
			float var1 = this.yaw;
			if (silentFix.getObject()) {
				float roundedStrafing = Math.max(-1, Math.min(1, Math.round(moveStrafing * 100))),
						roundedForward = Math.max(-1, Math.min(1, Math.round(moveForward * 100)));
				if (roundedStrafing != 0)
					var1 -= 90 * roundedStrafing * (roundedForward != 0 ? roundedForward * 0.5 : 1);
				if (moveForward <= 0) e.setSpeed(0);
			}
			e.setYaw(var1);
		}
	};

	@EventHandler
	public Listener<EventStrafe> strafeListener = event -> {

		if (movementFix.getObject() && prevSlot != -1) {
			float[] rots = getRotations();
			yaw = rots[0];
			pitch = rots[1];
			event.yaw = yaw;
			if (silentFix.getObject()) RotationUtil.silentMoveFix(event, yaw);
			mc.thePlayer.rotationYawHead = yaw;
			mc.thePlayer.rotationPitchHead = pitch;
			if (!ClientModule.otherRotations.getObject()) mc.thePlayer.renderYawOffset = yaw;

		}

	};

	@Override
	public void onDisable() {
		super.onDisable();
		mc.timer.silentTimer = false;
		if (prevSlot != -1) {
			mc.thePlayer.inventory.currentItem = prevSlot;
			prevSlot = -1;
		}
		mc.gameSettings.keyBindSneak.pressed = false;
		Sprint.disable = false;
		mc.timer.timerSpeed = 1.0f;
		for (Packet<?> packet : packets) {
			mc.thePlayer.sendQueue.addToSendQueue(packet);
		}
		packets.clear();
	}

	@EventHandler
	public final Listener<EventUpdateRenderItem> eventRenderItemListener = e -> {
		if (!fakeSilent.getObject() || fullSilent.getObject()) return;
		e.stack = mc.thePlayer.inventory.getStackInSlot(prevSlot);
	};

	@EventHandler
	public final Listener<EventMove> eventMoveListener = eventMove -> {
		if (safely.getObject())
			eventMove.setSafeWalk(true);
	};

	@EventHandler
	public final Listener<EventRender2D> eventRender2DListener = eventRender2D -> {
		RenderUtil.INSTANCE.scaleFix(1.0);
		Fonts.INSTANCE.guiIngameFont.drawStringWithShadow("Blocks: " + getBlocksAmount(), mc.displayWidth / 2F + 25, mc.displayHeight / 2F, -1);
		RenderUtil.INSTANCE.invertScaleFix(1.0);
	};


	@EventHandler
	public final Listener<EventFOVModifier> eventFOVModifierListener = e -> {
		if (!sprint.getObject()) return;
		if (intaveSprint.getObject() || (ServerUtil.isHypixel() && hypixelBypass.getObject())) {
			if (mc.thePlayer.isSprinting() || (sprint.getObject() && mc.thePlayer.isMovingForward()))
				e.setModifier(1.15f);
		}
	};

	@EventHandler
	public final Listener<EventUpdate> updateListener = event -> {
		this.setSuffix(rotationMode.getObject());
		for (int i = 0; i < 9; i++) {
			if (i < mc.gameSettings.keyBindings.length && mc.gameSettings.keyBindings[i] != null) {
				if (mc.gameSettings.keyBindings[i].pressed) prevSlot = i;
			}
		}

		if (ServerUtil.isHypixel()) {
			final PotionEffect speed = this.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed);
			final int moveSpeedAmp = speed == null ? 0 : speed.getAmplifier() + 1;
			if (moveSpeedAmp > 0 && mc.thePlayer.onGround && expandRange.getObject() <= 1.) {
				final double multiplier = sprint.getObject() ? 1.0 - moveSpeedAmp / 30.0 : 0.85 - moveSpeedAmp / 10.0;
				mc.thePlayer.motionX *= multiplier;
				mc.thePlayer.motionZ *= multiplier;
			}
		}

		if (mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 2,
				mc.thePlayer.posZ)).getBlock() != Blocks.air) {
			green = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 2, mc.thePlayer.posZ);
		}

		if (mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1,
				mc.thePlayer.posZ)).getBlock() != Blocks.air) {
			green = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
		}

		if (mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY,
				mc.thePlayer.posZ)).getBlock() != Blocks.air) {
			green = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
		}

		if (!ServerUtil.isHypixel())
			mc.thePlayer.setSprinting(mc.thePlayer.isMoving() && sprint.getObject());
		else mc.thePlayer.setSprinting(false);

		if (sprint.getObject() && mc.thePlayer.isMoving() && intaveSprint.getObject()) {
			mc.thePlayer.setSprinting(false);
			if (mc.thePlayer.ticksExisted % 2 == 0) {
				mc.thePlayer.setSprinting(true);
				sprintTicks++;
			}
			if (sprintTicks > 20) {
				mc.thePlayer.setSprinting(false);
				if (sprintTicks > 50) sprintTicks = 0;
				if (mc.thePlayer.ticksExisted % 4 == 0) mc.thePlayer.setSprinting(true);
			}
		}

		float[] rots = getRotations();
		yaw = rots[0];
		pitch = rots[1];
	};

	@EventHandler
	public final Listener<EventMotion> eventMotionListener = e -> {
		if (!((e.isUpdate() && placeEvent.getObject().equals("Pre")) || (e.isPost() && placeEvent.getObject().equals("Post")))) return;
		int slot = getSlot();

		if (slot == -1) {
			if (prevSlot != -1) {
				mc.thePlayer.inventory.currentItem = prevSlot;
				prevSlot = -1;
			}
			return;
		} else if (prevSlot == -1) prevSlot = mc.thePlayer.inventory.currentItem;
		if (!fullSilent.getObject()) mc.thePlayer.inventory.currentItem = slot;

		boolean found = false;
		double offset = checkRange.getObject();
		double max = expandRange.getObject();
		for (double d = (legit.getObject() || max == 0 ? -1 : 0); d <= (legit.getObject() ? 1.5 : 20.0) && !found; d += 0.5) {
			double expandX = mc.thePlayer.motionX * d;
			double expandZ = mc.thePlayer.motionZ * d;
			if ((expandX > max || expandZ > max || expandX < -max || expandZ < -max) && d >= 0) {
				break;
			}
			if (sideCheck.getObject()) {
				for (double d2 = -offset; d2 <= offset; d2 += offset) {
					for (double d3 = -offset; d3 <= offset; d3 += offset) {
						if (mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX + expandX + d2, mc.thePlayer.posY - 1,
								mc.thePlayer.posZ + expandZ + d3)).getBlock() == Blocks.air) {
							BlockPos maybe = new BlockPos(mc.thePlayer.posX + expandX + d2, mc.thePlayer.posY - 1, mc.thePlayer.posZ + expandZ + d3);
							for (EnumFacing facing : EnumFacing.values()) {
								if (mc.theWorld.getBlockState(maybe.offset(facing)).getBlock() != Blocks.air) {
									green = maybe.offset(facing);
									red = maybe;
									found = true;
								}
							}
						}
					}
				}
			} else if (mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX + expandX, mc.thePlayer.posY - 1,
					mc.thePlayer.posZ + expandZ)).getBlock() == Blocks.air) {
				BlockPos maybe = new BlockPos(mc.thePlayer.posX + expandX, mc.thePlayer.posY - 1, mc.thePlayer.posZ + expandZ);
				for (EnumFacing facing : EnumFacing.values()) {
					if (mc.theWorld.getBlockState(maybe.offset(facing)).getBlock() != Blocks.air) {
						green = maybe.offset(facing);
						red = maybe;
						found = true;
					}
				}
			}
		}

		calculatedPlacementState = found;

		if (!found) return;


		if (rotationMode.getObject().equals("Matrix")) {
			for (double x = -0.1; x <= 0.1; x += 0.1) {
				for (double z = -0.1; z <= 0.1; z += 0.1) {
					if (mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY - 1, mc.thePlayer.posZ + z)).getBlock().isFullBlock()) {
						return;
					}
				}
			}
		}


		EnumFacing prevFacing = facing;
		EnumFacing placeFacing = null;
		for (EnumFacing facing : EnumFacing.values()) {
			if (red.offset(facing).getX() == green.getX() && red.offset(facing).getZ() == green.getZ()) {
				placeFacing = facing;
			}
		}
		facing = placeFacing;
		calculatedPlacementState = calculatedPlacementState && placeFacing != null && red != null;

		if (placeFacing != null && red != null) {

			if (karhuFix.getObject() && timer.hasReached(100)) {
				mc.rightClickMouse();
				timer.reset();
			}

			if (!legit.getObject()) {
				if (rotationMode.getObject().equals("Matrix")) {
					mc.gameSettings.keyBindSneak.pressed = !mc.gameSettings.keyBindSneak.pressed;
				}

				EnumFacing facing = placeFacing == EnumFacing.UP ? placeFacing : placeFacing.getOpposite();
				Vec3i direction = facing.getDirectionVec();
				Vec3 vec = new Vec3(green).add(new Vec3(Math.max(direction.getX(), -direction.getX()), Math.max(direction.getY(), -direction.getY()), Math.max(direction.getZ(), -direction.getZ())));

				if (fullSilent.getObject()) {
					prevSlot = mc.thePlayer.inventory.currentItem;
					mc.thePlayer.inventory.currentItem = slot;
				}
				if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), green, facing, vec)) {
					if (silentSwing.getObject()) mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
					else mc.thePlayer.swingItem();
					if (fullSilent.getObject()) mc.thePlayer.inventory.currentItem = prevSlot;
				}
			}
		}
	};

	@EventHandler
	public Listener<EventJump> eventJumpListener = e -> {
		boolean canTower = !mc.thePlayer.isPotionActive(Potion.jump.getId()) && !mc.thePlayer.isMoving() && mc.gameSettings.keyBindJump.pressed;
		if (ServerUtil.isHypixel() && sprint.getObject() && mc.thePlayer.isSprinting() && prevSlot != -1) {
			e.setSpeed(0F);
			mc.thePlayer.setSpeed(0.25);
		}
		if (sprint.getObject() && intaveSprint.getObject())
			e.setSpeed(0F);

		if (towerMode.getObject().equals("TP") && canTower)
			e.setCancelled(true);

		if (towerMode.getObject().equals("TP2") && canTower)
			e.setCancelled(true);

	};

	private int toweringTicks = 0;

	private final ArrayList<Packet<?>> packets = new ArrayList<>();

	@EventHandler
	private final Listener<EventSentPacket> eventSentPacketListener = e -> {
		if (e.getPacket() instanceof C03PacketPlayer && ServerUtil.isHypixel() && hypixelBypass.getObject() && prevSlot != -1) {
			if (sprint.getObject()) {
				for (Packet<?> packet : packets) {
					mc.thePlayer.sendQueue.addToSendQueue(packet);
				}
				packets.clear();
				return;
			}
			if (packets.contains(e.getPacket())) return;
			//e.setCancelled(true);
			//packets.add(e.getPacket());
		}
	};

	@EventHandler
	private final Listener<EventMotion> motionListener = event -> {
		if (prevSlot != -1) {
			if (ServerUtil.isHypixel() && hypixelBypass.getObject() && event.isPost() && !packets.isEmpty() && !sprint.getObject()) {
				//get the packet to avoid having to use addToSendQueueNoEvent
				final Packet<?> packet = packets.get(0);
				mc.thePlayer.sendQueue.addToSendQueue(packet);
				packets.remove(packet);
			}
			if (ServerUtil.isHypixel() && hypixelBypass.getObject() && event.isPost() && mc.thePlayer.onGround && sprint.getObject()) {
				double x = mc.thePlayer.posX - mc.thePlayer.lastTickPosX;
				double z = mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ;
				mc.timer.timerSpeed = 1f;
				mc.timer.silentTimer = false;
				if (x == 0 && z == 0) return;
				mc.timer.silentTimer = true;
				mc.timer.timerSpeed = 0.5f;
				mc.thePlayer.setSprinting(true);
				mc.thePlayer.spawnRunningParticles();
				mc.thePlayer.spawnRunningParticles();
				mc.thePlayer.setSprinting(false);
				for (int i = 0; i < 1; i++) {
					if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(x, 0, z)).isEmpty())
						break;
					MovementUtil.spoof(x, 0, z, mc.thePlayer.onGround);
					mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
				}
				if (!mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
					final double multiplier = 0.6;
					if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(x * multiplier, 0, z * multiplier)).isEmpty())
						mc.thePlayer.setPosition(mc.thePlayer.posX + x * multiplier, mc.thePlayer.posY, mc.thePlayer.posZ + z * multiplier);
				}
			} else if (event.isPost())
				mc.timer.silentTimer = false;
			if (event.isPre() && towerMode.getObject().equals("TP") && !mc.thePlayer.isPotionActive(Potion.jump.getId()) && !mc.thePlayer.isMoving() && mc.thePlayer.onGround
					&& mc.gameSettings.keyBindJump.pressed) {
				MovementUtil.vClip(1.1);
			}
			if (event.isPre() && towerMode.getObject().equals("TP2") && !mc.thePlayer.isPotionActive(Potion.jump.getId()) && !mc.thePlayer.isMoving() && mc.thePlayer.onGround
					&& mc.gameSettings.keyBindJump.pressed) {
				MovementUtil.spoof(0.42f, true);
				MovementUtil.spoof(0.753f, true);
				MovementUtil.vClip(1.1);
			}
			if (event.isPre() && towerMode.getObject().equals("Watchdog") && !mc.thePlayer.isPotionActive(Potion.jump.getId())) {
				if (mc.gameSettings.keyBindJump.pressed && (watchdogMovingTower.getObject() || !mc.thePlayer.isMoving())) {
					if (mc.theWorld.isAirBlock(new BlockPos(mc.thePlayer.posX,
							mc.thePlayer.getEntityBoundingBox().minY - 1, mc.thePlayer.posZ)) &&
							!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0, -2, 0)).isEmpty()) {
						mc.thePlayer.motionY = (float) 0.42;
						if (mc.thePlayer.ticksExisted % 20 == 0) mc.thePlayer.motionY = -mc.thePlayer.motionY;
					}
					if (toweringTicks < 20) {
						mc.timer.timerSpeed = watchdogTowerBoost.getObject();
						toweringTicks++;
					} else if (toweringTicks < 27) {
						mc.timer.timerSpeed = 1;
						toweringTicks++;
					} else {
						toweringTicks = 0;
					}
					if (mc.thePlayer.isMoving()) mc.thePlayer.setSpeed(0.15);
					else {
						mc.thePlayer.motionX = 0;
						mc.thePlayer.motionZ = 0;
					}

					wasTowering = true;
				} else {
					if (wasTowering) mc.timer.timerSpeed = 1.0f;
					wasTowering = false;
					toweringTicks = 0;
				}
			}

			if (event.isPost() && towerMode.getObject().equals("Watchdog Test") && !mc.thePlayer.isPotionActive(Potion.jump.getId())) {
				if (mc.gameSettings.keyBindJump.pressed && (watchdogMovingTower.getObject() || !mc.thePlayer.isMoving()) &&
						!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0, -2, 0)).isEmpty()) {
					if (toweringTicks++ > 2) {
						if (ServerUtil.isHypixel()) {
							final int slot = getSlot();
							if (slot != -1 && mc.thePlayer.inventory.currentItem != slot)
								mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot));
							mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ).down(), EnumFacing.UP.getIndex(), null, 0, (float) Math.random() / 5f, 0));
							mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
							if (slot != -1 && mc.thePlayer.inventory.currentItem != slot)
								mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
						}
						mc.timer.timerSpeed = watchdogTowerBoost.getObject();
						mc.thePlayer.jump();
						mc.thePlayer.setSpeed(mc.thePlayer.isMoving() ? 0.15 : 0);
						wasTowering = true;
					}
				} else {
					if (wasTowering) mc.timer.timerSpeed = 1.0f;
					wasTowering = false;
					toweringTicks = 0;
				}
			}
			if (event.isPre() && towerMode.getObject().equals("Watchdog New") && !mc.thePlayer.isPotionActive(Potion.jump.getId())) {
				if (mc.gameSettings.keyBindJump.pressed && !mc.thePlayer.isMoving()) {
					mc.thePlayer.jumpTicks = 0;
					if (mc.thePlayer.onGround) toweringTicks++;
					if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0, -2, 0)).isEmpty()) {
						if (mc.thePlayer.motionY > 0 && mc.thePlayer.motionY < 0.2 && toweringTicks % 3 == 0) {
							mc.thePlayer.motionY = -0.1;
							toweringTicks = 1;
						}
					}
					mc.thePlayer.motionX = 0;
					mc.thePlayer.motionZ = 0;
					wasTowering = true;
				} else {
					wasTowering = false;
					toweringTicks = 0;
				}
			}
			event.yaw = yaw;
			event.pitch = pitch;
			if (!ClientModule.otherRotations.getObject()) mc.thePlayer.renderYawOffset = yaw;
		}
	};

	private float[] getRotations() {
		if (!keepRotations.getObject() && !calculatedPlacementState)
			return new float[]{mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch};
		float[] rotations = new float[]{legitMode.getObject().equals("Godbridge") ? mc.thePlayer.rotationYaw + 180f : yaw, pitch};

		if (legit.getObject()) {

			if (green == null)
				return legitMode.getObject().equals("Godbridge") ? new float[]{mc.thePlayer.rotationYaw + 180f, pitch} : rotations;

			float[] base = new float[]{mc.thePlayer.rotationYaw + getStrafeMultiplier() + 180f, 75f};

			MovingObjectPosition position;
			if (legitMode.getObject().equals("Godbridge")) {
				for (float pitch = -20; pitch <= 20; pitch += 1) {

					float[] rots = RotationUtil.mouseFix(yaw, pitch, base[0], base[1] + pitch);
					float baseYaw = mc.thePlayer.rotationYaw + 180f;
					float basePitch = rots[1];

					if (basePitch > 90) break;
					if (pitch == basePitch) basePitch += 1;
					Vec3 vec3 = Minecraft.getMinecraft().thePlayer.getPositionEyes(1.0f);
					float f = MathHelper.cos(-baseYaw * 0.017453292F - (float) Math.PI);
					float f1 = MathHelper.sin(-baseYaw * 0.017453292F - (float) Math.PI);
					float f2 = -MathHelper.cos(-basePitch * 0.017453292F);
					float f3 = MathHelper.sin(-basePitch * 0.017453292F);
					Vec3 rotationVec = new Vec3((f1 * f2), f3, (f * f2));
					Vec3 vec32 = vec3.addVector(rotationVec.xCoord * 3, rotationVec.yCoord * 3, rotationVec.zCoord * 3);
					MovingObjectPosition rayTrace = Minecraft.getMinecraft().theWorld.rayTraceBlocks(vec3, vec32, false, false, true);
					if (rayTrace.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) continue;
					if (rayTrace.sideHit == EnumFacing.UP && facing != EnumFacing.UP) continue;
					if (rayTrace.getBlockPos().getY() != green.getY()) continue;
					if (rayTrace.sideHit == EnumFacing.DOWN) continue;
					if (!(rayTrace.getBlockPos().getX() == green.getX() && rayTrace.getBlockPos().getZ() == green.getZ()) &&
							!(rayTrace.getBlockPos().offset(rayTrace.sideHit).getX() == green.getX() && rayTrace.getBlockPos().offset(rayTrace.sideHit).getZ() == green.getZ()))
						continue;
					position = rayTrace;
					rotations = new float[]{baseYaw, basePitch};
					if (position.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && (position.sideHit == facing.getOpposite() || position.sideHit == facing || (facing == EnumFacing.UP && !mc.thePlayer.onGround))) {
						if (fullSilent.getObject()) {
							prevSlot = mc.thePlayer.inventory.currentItem;
							mc.thePlayer.inventory.currentItem = getSlot();
						}
						if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), green, position.sideHit, position.hitVec)) {
							if (silentSwing.getObject())
								mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
							else mc.thePlayer.swingItem();
							if (fullSilent.getObject()) mc.thePlayer.inventory.currentItem = prevSlot;
							return rotations;
						}
					}
					break;

				}

				//mc.thePlayer.addChatMessage(new ChatComponentText((diff / 1000000.0) + "ms | " + tries));
			} else {
				Vec3 vec3 = Minecraft.getMinecraft().thePlayer.getPositionEyes(1.0f);
				base = RotationUtil.getNeededRotations(RotationUtil.getBlockVecCenter(green));
				for (float yaw = -50; yaw <= 50; yaw += 10) {
					for (float pitch = -30; pitch <= 30; pitch += 2f) {

						float baseYaw = base[0] + yaw;
						float basePitch = base[1] + pitch;
						if (basePitch > 90) basePitch = 90;
						float f = MathHelper.cos(-baseYaw * 0.017453292F - (float) Math.PI);
						float f1 = MathHelper.sin(-baseYaw * 0.017453292F - (float) Math.PI);
						float f2 = -MathHelper.cos(-basePitch * 0.017453292F);
						float f3 = MathHelper.sin(-basePitch * 0.017453292F);
						Vec3 rotationVec = new Vec3((f1 * f2), f3, (f * f2));
						Vec3 vec32 = vec3.addVector(rotationVec.xCoord * 3, rotationVec.yCoord * 3, rotationVec.zCoord * 3);
						MovingObjectPosition rayTrace = Minecraft.getMinecraft().theWorld.rayTraceBlocks(vec3, vec32, false, false, true);
						if (rayTrace.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) continue;
						if (rayTrace.sideHit == EnumFacing.UP && facing != EnumFacing.UP) continue;
						if (rayTrace.sideHit != facing && rayTrace.sideHit != facing.getOpposite()) continue;
						if (rayTrace.getBlockPos().getY() != green.getY()) continue;
						if (rayTrace.sideHit == EnumFacing.DOWN) continue;
						if (!(rayTrace.getBlockPos().getX() == green.getX() && rayTrace.getBlockPos().getZ() == green.getZ()) &&
								!(rayTrace.getBlockPos().offset(rayTrace.sideHit).getX() == green.getX() && rayTrace.getBlockPos().offset(rayTrace.sideHit).getZ() == green.getZ()))
							continue;
						position = rayTrace;
						rotations = new float[]{baseYaw, basePitch};
						if (position.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && (position.sideHit == facing.getOpposite() || position.sideHit == facing || (facing == EnumFacing.UP && !mc.thePlayer.onGround))) {
							if (fullSilent.getObject()) {
								prevSlot = mc.thePlayer.inventory.currentItem;
								mc.thePlayer.inventory.currentItem = getSlot();
							}
							if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), green, position.sideHit, position.hitVec)) {
								if (silentSwing.getObject())
									mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
								else mc.thePlayer.swingItem();
								if (fullSilent.getObject()) mc.thePlayer.inventory.currentItem = prevSlot;
								return rotations;
							}
						}
						break;

					}
				}

			}

		} else {
			switch (rotationMode.getObject()) {
				case "Matrix":
					rotations = new float[]{mc.thePlayer.rotationYaw + getStrafeMultiplier() + 170f + ThreadLocalRandom.current().nextFloat() * 10f, mc.thePlayer.onGround ? 80f + ThreadLocalRandom.current().nextFloat() * 3f : 90f};
					break;
				case "Backwards":
					rotations = new float[]{mc.thePlayer.rotationYaw + getStrafeMultiplier() + 180f, mc.thePlayer.onGround ? 81.5f : 73};
					break;
				case "Forwards":
					rotations = new float[]{mc.thePlayer.rotationYaw, mc.thePlayer.onGround ? 81.5f : 73};
					break;
				case "NCP":
					if (red == null || facing == null)
						rotations = new float[]{mc.thePlayer.rotationYaw + getStrafeMultiplier() + 180f, mc.thePlayer.onGround ? 81.5f : 85f};
					else
						rotations = RotationUtil.getNeededRotations(RotationUtil.getBlockVecCenter(red).add(new Vec3(facing.getDirectionVec())));
					break;
				case "Watchdog":
					if (red == null || facing == null)
						return new float[]{mc.thePlayer.rotationYaw + getStrafeMultiplier(), mc.thePlayer.onGround ? 81.5f : 75};
					rotations = RotationUtil.getNeededRotations(RotationUtil.getBlockVecCenter(red).add(new Vec3(facing.getDirectionVec())));
					rotations[0] -= (rotations[0] - yaw) % 30;
					rotations[1] = 79.9F + rotations[1] / 180;
					rotations[0] += MathUtil.getRandom_float(1, 3);
					rotations[1] += MathUtil.getRandom_float(1, 3);

					break;
				case "None":
					rotations = new float[]{mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch};
					break;
			}
		}

		return rotations;
	}

	public float getStrafeMultiplier() {
		float multiplier = 0F;
		Direction dir = Direction.NONE;
		if (mc.thePlayer.moveStrafing > 0) {
			multiplier += 270F;
			dir = Direction.RIGHT;
		} else if (mc.thePlayer.moveStrafing < 0) {
			multiplier += 90F;
			dir = Direction.LEFT;
		} else if (mc.thePlayer.moveForward < 0) {
			multiplier += 180F;
			dir = Direction.BACKWARD;
		}

		if (dir == Direction.LEFT && mc.thePlayer.isMovingBackward()) multiplier += 45F;
		else if (dir == Direction.RIGHT && mc.thePlayer.isMovingBackward()) multiplier -= 45F;
		else if (dir == Direction.LEFT && mc.thePlayer.isMovingForward()) multiplier -= 45F;
		else if (dir == Direction.RIGHT && mc.thePlayer.isMovingForward()) multiplier += 45F;

		return multiplier;
	}

	private int getSlot() {
		for (int i = 0; i < 9; i++) {
			if (mc.thePlayer.inventory.getStackInSlot(i) != null && mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemBlock
					&& !invalidBlocks.contains(((ItemBlock) mc.thePlayer.inventory.getStackInSlot(i).getItem()).getBlock())) {
				return i;
			}
		}
		return -1;
	}

	private int getBlocksAmount() {
		int amount = 0;
		for (int i = 0; i < 9; i++) {
			if (mc.thePlayer.inventory.getStackInSlot(i) != null &&
					mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemBlock &&
					!invalidBlocks.contains(Block.getBlockFromItem(mc.thePlayer.inventory.getStackInSlot(i).getItem())) &&
					mc.thePlayer.inventory.getStackInSlot(i).stackSize > 0) {
				amount += mc.thePlayer.inventory.getStackInSlot(i).stackSize;
			}
		}
		return amount;
	}

	@EventHandler
	public final Listener<EventRender3D> render3DListener = event -> {
		if (renderDebug.getObject()) {
			RenderUtil.INSTANCE.drawBox(green.getX() + 0.5, green.getY(), green.getZ() + 0.5, 0.5, 1.0, new Color(0, 255, 0, 50), false);
			RenderUtil.INSTANCE.drawBox(red.getX() + 0.5, red.getY(), red.getZ() + 0.5, 0.5, 1.0, new Color(255, 0, 0, 50), false);
		}
		GlStateManager.resetColor();
	};

}
