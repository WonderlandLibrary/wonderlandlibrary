package Reality.Realii.mods.modules.world;

import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Vec3;
import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.rendering.EventRender2D;
import Reality.Realii.event.events.rendering.EventRender3D;
import Reality.Realii.event.events.world.EventMove;
import Reality.Realii.event.events.world.EventPacketRecieve;
import Reality.Realii.event.events.world.EventPacketSend;
import Reality.Realii.event.events.world.EventPostUpdate;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.value.Mode;
import Reality.Realii.event.value.Numbers;
import Reality.Realii.event.value.Option;
import Reality.Realii.guis.font.FontLoaders;
import Reality.Realii.managers.ModuleManager;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.utils.cheats.player.PlayerUtils;
import Reality.Realii.utils.cheats.world.TimerUtil;
import Reality.Realii.utils.math.RotationUtil;
import Reality.Realii.utils.render.RenderUtil;
import Reality.Realii.mods.modules.ClientSettings;
import Reality.Realii.mods.modules.movement.Sprint;
import Reality.Realii.mods.modules.render.ArrayList2;
import Reality.Realii.mods.modules.world.Scaffold;
import Reality.Realii.Client;
import Reality.Realii.commands.Command;
import Reality.Realii.mods.Module;
import Reality.Realii.utils.cheats.RenderUtills.Pair;
import Reality.Realii.utils.cheats.RenderUtills.Render2;
import Reality.Realii.utils.cheats.player.Helper;
import Reality.Realii.utils.cheats.player.Motion;
import net.minecraft.util.EnumChatFormatting;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.opengl.GL11;

public class Scaffold extends Module {
	public final Option<Boolean> swing = new Option<>("Swing", true);
	
	public Option<Boolean> down = new Option<>("DownScaffold", true);
	private Mode Sprintdick = new Mode("SprintModes", "SprintModes",
			new String[] { "NoSprint", "Sprint", "Hypixel", "HypixelNew", "Verus", "BlocksMc", "Vulcan", "VulcanFast" },
			"NoSprint");
	private Mode ratation = new Mode("Rotations", "Rotations",
			new String[] { "Hypixel", "none", "BlocksMc", "LegitAsf", "Vulcan" }, "Hypixel");
	private Mode towermodule = new Mode("TowerMode", "TowerMode",
			new String[] { "None", "WatchDog", "Vannila", "Vulcan2", "New", "Vulcan"}, "Vannila");
	private Mode JumpMode = new Mode("JumpMode", "JumpMode", new String[] { "None", "Vannila" ,"Vannila2"}, "None");
	private Mode placevnt = new Mode("PlaceEvent", "PlaceEvent", new String[] { "OnPost", "OnPre" }, "OnPre");
	public static Mode SafeWalk = new Mode("SafeWalk", "SafeWalk", new String[] { "On", "Off" }, "On");
	public final Option<Boolean> samey = new Option<>("SameY", false);
	private Mode itemspadf = new Mode("ItemSpoof", "ItemSpoof", new String[] { "LiteSpoof", "Spoof", "None", "New" },
			"None");
	public static Numbers<Number> speed2 = new Numbers<>("MotionsSpeed", 1.3, 0.9, 1.3, 0.05);
	public static Numbers<Number> Scaffoldplace = new Numbers<>("PlaceDelay", 47L, 0L, 721L, 112L);
	public static Numbers<Number> timerboost = new Numbers<>("TimerBoost", 0.5, 1, 9.3, 1.2);
	private int stage;
	public final static List<Block> blacklist = Arrays.asList(Blocks.air, Blocks.water, Blocks.torch,
			Blocks.redstone_torch, Blocks.ladder, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava,
			Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars,
			Blocks.chest, Blocks.torch, Blocks.anvil, Blocks.web, Blocks.redstone_torch, Blocks.brewing_stand,
			Blocks.waterlily, Blocks.farmland, Blocks.sand, Blocks.beacon, Blocks.double_plant, Blocks.noteblock,
			Blocks.hopper, Blocks.dispenser, Blocks.dropper, Blocks.crafting_table, Blocks.command_block);
	private double y;
	private int time, block2;
	private final TimerUtil timer = new TimerUtil();
	private final TimerUtil autoBlockCoolDown = new TimerUtil();
	private Block block;
	private boolean istowering;
	private BlockData blockData;
	private int slot;
	private float blockPitch;
	private float blockYaw;
	private boolean b;
	public boolean shouldDown;
	public boolean shouldSafeWalk;
	public int oldSloter;

	public Scaffold() {
		super("Scaffold", ModuleType.World);
		this.addValues(this.swing, samey, this.down, Sprintdick, speed2, ratation, towermodule, placevnt, Scaffoldplace,
				timerboost, itemspadf, SafeWalk, JumpMode);
	}

	@Override
	public void onEnable() {
		this.y = mc.thePlayer.posY;
		// this.y = mc.thePlayer.posY;
		if (this.ratation.getValue().equals("Hypixel")) {
			this.blockYaw = this.getRotation()[0];
			this.blockPitch = 76.0f;

			if (this.Sprintdick.getValue().equals("Hypixel")) {
				mc.gameSettings.keyBindSneak.pressed = false;

			}

		}

		if (this.ratation.getValue().equals("BlocksMc")) {
			this.blockYaw = this.getRotation()[0];
			this.blockPitch = 0f;
		}
		mc.timer.timerSpeed = 1.0F;
		this.slot = mc.thePlayer.inventory.currentItem;
		this.b = false;
		if (this.slot != this.getBlockSlot()) {
			if (this.itemspadf.getValue().equals("Spoof")) {
				mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.getBlockSlot()));
			}
			if (this.itemspadf.getValue().equals("New")) {
				oldSloter = mc.thePlayer.inventory.currentItem;
			}

		}

	}

	@Override
	public void onDisable() {
		if (this.JumpMode.getValue().equals("Vannila2")) {
			mc.gameSettings.keyBindJump.pressed = false;
		}
		if (this.Sprintdick.getValue().equals("VulcanFast")) {
			mc.thePlayer.sendQueue.addToSendQueue(
					new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
		}
		if (mc.thePlayer.isSwingInProgress) {
			mc.thePlayer.swingProgress = 0.0f;
			mc.thePlayer.swingProgressInt = 0;
			mc.thePlayer.isSwingInProgress = false;
		}
		if (mc.thePlayer.inventory.currentItem != this.slot) {
			if (this.itemspadf.getValue().equals("Spoof")) {
				mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
			}

			if (this.itemspadf.getValue().equals("New")) {
				mc.thePlayer.inventory.currentItem = oldSloter;
			}

			this.slot = mc.thePlayer.inventory.currentItem;
		}
		this.blockYaw = mc.thePlayer.rotationYaw;
		this.blockPitch = mc.thePlayer.rotationPitch;
		mc.timer.timerSpeed = 1.0F;
	}

	@EventHandler
	public void onPre(EventPreUpdate event) {

		this.moveBlock();
		this.shouldDown = mc.gameSettings.keyBindAttack.isKeyDown() && PlayerUtils.isMoving() && this.down.getValue();
		double x = mc.thePlayer.posX;
		double z = mc.thePlayer.posZ;
		if ((boolean) !samey.getValue()) {

			this.y = mc.thePlayer.posY;

		} else {
			if (!PlayerUtils.isMoving()) {
				this.y = mc.thePlayer.posY;
			}
		}
		this.shouldSafeWalk = !this.shouldDown;
		if (this.isAirBlock(
				mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ))
						.getBlock())) {
			x = mc.thePlayer.posX;
			z = mc.thePlayer.posZ;
		}
		BlockPos blockPos = new BlockPos(x, this.y - 1.0 - (this.shouldDown ? 0.01 : 0.0), z);
		this.block = this.getBlockByPos(blockPos);
		this.blockData = this.getBlockData(blockPos);
		if (this.getBlockCount() <= 0) {
			return;
		}
		if (this.blockData != null) {
			if (this.isAirBlock(this.block)) {
				float yaw = RotationUtil.getRotations(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ,
						mc.thePlayer.getEyeHeight(), blockData.position, blockData.face)[0];
				ArrayList<RotationData> rotationDataList = new ArrayList<>();
				float[] rotationFrom = this.getRotations();
				for (int i = 0; i < rotationFrom.length; i++) {
					rotationDataList.add(new RotationData(Math.abs(rotationFrom[i] - yaw), i));
				}
				rotationDataList.sort(Comparator.comparing(rotationData -> rotationData.distance));
				this.blockYaw = this.getRotation()[0];
				this.blockPitch = RotationUtil.getRotations(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ,
						mc.thePlayer.getEyeHeight(), blockData.position, blockData.face)[1];
				if (!mc.gameSettings.keyBindJump.isKeyDown() && !this.shouldDown && mc.thePlayer.onGround
						&& PlayerUtils.isOnGround(0.001) && mc.thePlayer.isCollidedVertically) {
					event.setOnground(false);

				}
			}
		}
		this.doRotate(event);
		if (this.placevnt.getValue().equals("OnPre")) {

			if ((!this.timer.delay(Scaffoldplace.getValue().intValue()) && PlayerUtils.isOnGround(0.01)
					&& !mc.gameSettings.keyBindJump.isKeyDown()) || this.getBlockCount() <= 0) {
				return;
			}
			this.timer.reset();
			this.b = this.slot != this.getBlockSlot();
			if (this.itemspadf.getValue().equals("Spoof")) {
				boolean b2 = false;
				if (blockData != null) {
					if (this.isAirBlock(this.block)) {
						if (this.autoBlockCoolDown.delay(100L)) {
							if (this.b) {
								// mc.playerController.updateController();
								// mc.thePlayer.inventory.setField(getBlockSlot(), getBlockSlot());

								// if(this.getBlockSlot() < 1) {
								// maybe = 1 or something
								// mc.thePlayer.sendQueue.addToSendQueue(new
								// C09PacketHeldItemChange(this.getBlockSlot()));
								// }
								mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.getBlockSlot()));
								b2 = true;
							}
						}
						if (b2 || this.slot == this.getBlockSlot()) {
							if (swing.getValue()) {
								mc.thePlayer.swingItem();
							} else {
								mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
							}
							if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld,
									mc.thePlayer.inventory.getStackInSlot(this.slot), blockData.position,
									blockData.face, getVec3(blockData.position, blockData.face))) {
								this.block = null;
								this.blockData = null;
							}
						}

					}
				}
			}

			if (this.itemspadf.getValue().equals("New")) {

				boolean b2 = false;

				if (this.isAirBlock(this.block)) {
					if (slot != getBlockSlot()) {
						if (this.b) {
							// mc.thePlayer.sendQueue.addToSendQueue(new
							// C09PacketHeldItemChange(this.getBlockSlot()));
							mc.thePlayer.inventory.currentItem = this.getBlockSlot();

							// mc.playerController.updateController();
							// mc.thePlayer.inventory.setField(getBlockSlot(), getBlockSlot());
							b2 = true;
						}
					}
					if (b2 || this.slot == this.getBlockSlot()) {
						if (swing.getValue()) {
							mc.thePlayer.swingItem();
						} else {

						}

						if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld,
								mc.thePlayer.inventory.getStackInSlot(this.slot), blockData.position, blockData.face,
								getVec3(blockData.position, blockData.face))) {
							// mc.thePlayer.sendQueue.addToSendQueue(new
							// C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getStackInSlot(this.slot)));
						}

					}

				}

				// if (!this.isAirBlock(this.block)) {
				// if (!this.autoBlockCoolDown.delay(100L)) {
				// if (!this.b) {
				// mc.thePlayer.sendQueue.addToSendQueue(new
				// C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
				// b2 = true;
				// }
				// }
				// }
			}
		}
		this.b = this.slot != this.getBlockSlot();
		if (this.itemspadf.getValue().equals("LiteSpoof")) {
			boolean b2 = false;
			if (blockData != null) {
				if (this.isAirBlock(this.block)) {
					if (this.autoBlockCoolDown.delay(100L)) {
						if (this.b) {
							mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.getBlockSlot()));

							b2 = true;
						}
					}
					if (b2 || this.slot == this.getBlockSlot()) {
						if (swing.getValue()) {
							mc.thePlayer.swingItem();
						} else {
							mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
						}
						if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld,
								mc.thePlayer.inventory.getStackInSlot(this.slot), blockData.position, blockData.face,
								getVec3(blockData.position, blockData.face))) {
							this.block = null;
							this.blockData = null;
						}
					}

				}
			}
		}
		this.b = this.slot != this.getBlockSlot();
		if (this.itemspadf.getValue().equals("None")) {
			boolean b2 = false;
			if (blockData != null) {
				if (this.isAirBlock(this.block)) {
					if (this.autoBlockCoolDown.delay(100L)) {
						if (this.b) {
							// mc.thePlayer.sendQueue.addToSendQueue(new
							// C09PacketHeldItemChange(this.getBlockSlot()));
							// mc.thePlayer.inventory.setField(getBlockSlot(), getBlockSlot());
							// mc.thePlayer.inventory.setInventorySlotContents(1, null);

							b2 = true;
						}
					}
					if (b2 || this.slot == this.getBlockSlot()) {
						if (swing.getValue()) {
							mc.thePlayer.swingItem();
						} else {

						}
						if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld,
								mc.thePlayer.inventory.getStackInSlot(this.slot), blockData.position, blockData.face,
								getVec3(blockData.position, blockData.face))) {
							this.block = null;

							this.blockData = null;
						}
					}

				}
			}
		}

	}

	private float[] getRotations() {
		if (this.ratation.getValue().equals("Hypixel")) {
			if (MovementInput.moveForward != 180.0f && MovementInput.moveStrafe == 180.0f) {
				return new float[] { mc.thePlayer.rotationYaw - 180.0f, mc.thePlayer.rotationYaw,
						mc.thePlayer.rotationYaw + 180.0f };
			} else if (MovementInput.moveForward == 180.0f && MovementInput.moveStrafe != 180.0f) {
				return new float[] { mc.thePlayer.rotationYaw - 180.0f, mc.thePlayer.rotationYaw + 180.0f,
						mc.thePlayer.rotationYaw - 180.0f, mc.thePlayer.rotationYaw + 180.0f };
			} else if (MovementInput.moveForward != 180.0f && MovementInput.moveStrafe != 180.0f) {
			}
		}

		if (this.ratation.getValue().equals("Vulcan")) {

			if (MovementInput.moveForward != 180.0f && MovementInput.moveStrafe == 180.0f) {
				return new float[] { mc.thePlayer.rotationYaw - 180.0f, mc.thePlayer.rotationYaw,
						mc.thePlayer.rotationYaw + 180.0f };
			} else if (MovementInput.moveForward == 180.0f && MovementInput.moveStrafe != 180.0f) {
				return new float[] { mc.thePlayer.rotationYaw - 180.0f, mc.thePlayer.rotationYaw + 180.0f,
						mc.thePlayer.rotationYaw - 180.0f, mc.thePlayer.rotationYaw + 180.0f };
			} else if (MovementInput.moveForward != 180.0f && MovementInput.moveStrafe != 180.0f) {
			}
		}

		else if (this.ratation.getValue().equals("BlocksMc")) {
			if (MovementInput.moveForward != 120f && MovementInput.moveStrafe == 120f) {
				return new float[] { mc.thePlayer.rotationYaw - 120f, mc.thePlayer.rotationYaw,
						mc.thePlayer.rotationYaw + 120f };
			} else if (MovementInput.moveForward == 120f && MovementInput.moveStrafe != 120f) {
				return new float[] { mc.thePlayer.rotationYaw - 180.0f, mc.thePlayer.rotationYaw + 120f,
						mc.thePlayer.rotationYaw - 120f, mc.thePlayer.rotationYaw + 120f };
			} else if (MovementInput.moveForward != 120f && MovementInput.moveStrafe != 120f) {
			}
		}

		else if (this.ratation.getValue().equals("LegitAsf")) {
			if (MovementInput.moveForward != 180.0f && MovementInput.moveStrafe == 180.0f) {
				return new float[] { mc.thePlayer.rotationYaw - 180.0f, mc.thePlayer.rotationYaw,
						mc.thePlayer.rotationYaw + 180.0f };
			} else if (MovementInput.moveForward == 180.0f && MovementInput.moveStrafe != 180.0f) {
				return new float[] { mc.thePlayer.rotationYaw - 180.0f, mc.thePlayer.rotationYaw + 180.0f,
						mc.thePlayer.rotationYaw - 180.0f, mc.thePlayer.rotationYaw + 180.0f };
			} else if (MovementInput.moveForward != 180.0f && MovementInput.moveStrafe != 180.0f) {
			}
		}

		// LegitAsf

		return new float[] { this.blockYaw };

	}

	private void doRotate(EventPreUpdate e) {
		if (this.ratation.getValue().equals("Hypixel")) {
			e.setYaw(this.blockYaw);
			e.setPitch(80);
			// PlayerUtils.rotate(this.blockYaw, this.blockPitch);
		}
		if (this.ratation.getValue().equals("BlocksMc")) {
			e.setYawhead(this.blockYaw);
			e.setPitch(70);
			// PlayerUtils.rotate(this.blockYaw, this.blockPitch);

		}

		if (this.ratation.getValue().equals("Vulcan")) {

			e.setYaw(this.blockYaw);
			e.setPitch(20);
			// PlayerUtils.rotate(this.blockYaw, this.blockPitch);

		}

		if (this.ratation.getValue().equals("LegitAsf")) {
			e.setYaw(this.blockYaw);
			e.setPitch(this.blockPitch);
			PlayerUtils.rotate(this.blockYaw, this.blockPitch);

		}
	}

	private float getPitch() {
		return this.blockPitch;
	}

	private float[] getRotation() {
		if (this.ratation.getValue().equals("Hypixel")) {
			if (mc.gameSettings.keyBindForward.isKeyDown()) {
				if (MovementInput.moveStrafe == 0.0) {
					return new float[] { mc.thePlayer.rotationYaw - 180.0f, this.getPitch() };
				} else if (mc.gameSettings.keyBindLeft.isKeyDown()) {
					return new float[] { mc.thePlayer.rotationYaw - 180.0f, this.getPitch() };
				} else if (mc.gameSettings.keyBindRight.isKeyDown()) {
					return new float[] { mc.thePlayer.rotationYaw - 180.0f, this.getPitch() };
				}
			} else if (mc.gameSettings.keyBindBack.isKeyDown()) {
				if (MovementInput.moveStrafe == 0.0) {
					return new float[] { mc.thePlayer.rotationYaw, this.getPitch() };
				} else if (mc.gameSettings.keyBindLeft.isKeyDown()) {
					return new float[] { mc.thePlayer.rotationYaw - 180.0f, this.getPitch() };
				} else if (mc.gameSettings.keyBindRight.isKeyDown()) {
					return new float[] { mc.thePlayer.rotationYaw - 180.0f, this.getPitch() };
				}
			} else if (mc.gameSettings.keyBindLeft.isKeyDown()) {
				return new float[] { mc.thePlayer.rotationYaw - 180.0f, this.getPitch() };
			} else if (mc.gameSettings.keyBindRight.isKeyDown()) {
				return new float[] { mc.thePlayer.rotationYaw - 180.0f, this.getPitch() };
			}
		}
		
		if (this.ratation.getValue().equals("Vulcan")) {

			if (mc.gameSettings.keyBindForward.isKeyDown()) {
				if (MovementInput.moveStrafe == 0.0) {
					return new float[] { mc.thePlayer.rotationYaw - 180.0f, this.getPitch() };
				} else if (mc.gameSettings.keyBindLeft.isKeyDown()) {
					return new float[] { mc.thePlayer.rotationYaw - 180.0f, this.getPitch() };
				} else if (mc.gameSettings.keyBindRight.isKeyDown()) {
					return new float[] { mc.thePlayer.rotationYaw - 180.0f, this.getPitch() };
				}
			} else if (mc.gameSettings.keyBindBack.isKeyDown()) {
				if (MovementInput.moveStrafe == 0.0) {
					return new float[] { mc.thePlayer.rotationYaw, this.getPitch() };
				} else if (mc.gameSettings.keyBindLeft.isKeyDown()) {
					return new float[] { mc.thePlayer.rotationYaw - 180.0f, this.getPitch() };
				} else if (mc.gameSettings.keyBindRight.isKeyDown()) {
					return new float[] { mc.thePlayer.rotationYaw - 180.0f, this.getPitch() };
				}
			} else if (mc.gameSettings.keyBindLeft.isKeyDown()) {
				return new float[] { mc.thePlayer.rotationYaw - 180.0f, this.getPitch() };
			} else if (mc.gameSettings.keyBindRight.isKeyDown()) {
				return new float[] { mc.thePlayer.rotationYaw - 180.0f, this.getPitch() };
			}

		}
		if (this.ratation.getValue().equals("BlocksMc")) {
			if (mc.gameSettings.keyBindForward.isKeyDown()) {
				if (MovementInput.moveStrafe == 0.0) {
					return new float[] { mc.thePlayer.rotationYaw - 120f, this.getPitch() };

				} else if (mc.gameSettings.keyBindLeft.isKeyDown()) {
					return new float[] { mc.thePlayer.rotationYaw - 120f, this.getPitch() };
				} else if (mc.gameSettings.keyBindRight.isKeyDown()) {
					return new float[] { mc.thePlayer.rotationYaw - 120f, this.getPitch() };

				}
			} else if (mc.gameSettings.keyBindBack.isKeyDown()) {
				if (MovementInput.moveStrafe == 0.0) {
					return new float[] { mc.thePlayer.rotationYaw, this.getPitch() };
				} else if (mc.gameSettings.keyBindLeft.isKeyDown()) {
					return new float[] { mc.thePlayer.rotationYaw - 120f, this.getPitch() };
				} else if (mc.gameSettings.keyBindRight.isKeyDown()) {
					return new float[] { mc.thePlayer.rotationYaw - 120f, this.getPitch() };

				}
			} else if (mc.gameSettings.keyBindLeft.isKeyDown()) {
				return new float[] { mc.thePlayer.rotationYaw - 120f, this.getPitch() };
			} else if (mc.gameSettings.keyBindRight.isKeyDown()) {
				return new float[] { mc.thePlayer.rotationYaw - 120f, this.getPitch() };
			}

		}

		if (this.ratation.getValue().equals("LegitAsf")) {
			if (mc.gameSettings.keyBindForward.isKeyDown()) {
				if (MovementInput.moveStrafe == 0.0) {
					PlayerUtils.rotate(180, 180);
				} else if (mc.gameSettings.keyBindLeft.isKeyDown()) {

				} else if (mc.gameSettings.keyBindRight.isKeyDown()) {
					return new float[] { mc.thePlayer.rotationYaw - 180.0f, this.getPitch() };
				}
			} else if (mc.gameSettings.keyBindBack.isKeyDown()) {
				if (MovementInput.moveStrafe == 0.0) {
					return new float[] { mc.thePlayer.rotationYaw, this.getPitch() };
				} else if (mc.gameSettings.keyBindLeft.isKeyDown()) {
					return new float[] { mc.thePlayer.rotationYaw - 180.0f, this.getPitch() };
				} else if (mc.gameSettings.keyBindRight.isKeyDown()) {
					return new float[] { mc.thePlayer.rotationYaw - 180.0f, this.getPitch() };
				}
			} else if (mc.gameSettings.keyBindLeft.isKeyDown()) {
				return new float[] { mc.thePlayer.rotationYaw - 180.0f, this.getPitch() };
			} else if (mc.gameSettings.keyBindRight.isKeyDown()) {
				return new float[] { mc.thePlayer.rotationYaw - 180.0f, this.getPitch() };
			}
		}
		return new float[] { this.blockYaw, this.blockPitch };
	}

	private int moveHot() {
		int slot = -1;
		for (int i = 36; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				Item item = mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				if (item instanceof ItemBlock && this.isValid(item) && is.stackSize > slot) {
					slot = i;
					return slot;
				}
			}
		}
		return slot;
	}
	
	
	

	@EventHandler
	public void onUpdate(EventPreUpdate e) {
		
		if (this.towermodule.getValue().equals("WatchDog")) {
			 if (!mc.gameSettings.keyBindJump.isKeyDown()) return;

			
			 if (mc.thePlayer.onGround) {
		            mc.thePlayer.motionY = 0.41999998688697815F;
		            mc.thePlayer.motionX *= .65;
		            mc.thePlayer.motionZ *= .65;
		        }
			   
		 }
		if (this.towermodule.getValue().equals("Vannila")) {
			if (mc.gameSettings.keyBindJump.isKeyDown()) {
				mc.thePlayer.motionY = 0.42;

			}
		}

		if (this.towermodule.getValue().equals("Vulcan")) {

			if (mc.gameSettings.keyBindJump.isKeyDown()) {
				if (mc.thePlayer.posY % 1 <= 0.00153598) {
					mc.thePlayer.setPosition(mc.thePlayer.posX, Math.floor(mc.thePlayer.posY), mc.thePlayer.posZ);
					mc.thePlayer.motionY = 0.42F;
				}
			}
			if (PlayerUtils.isMoving()) {
				if (mc.gameSettings.keyBindJump.isKeyDown()) {
					if (mc.thePlayer.onGround && mc.thePlayer.posY % 1 <= 0.00153598) {
						mc.thePlayer.motionY = 0.42F;
					}
				}
			}

		}

		if (this.towermodule.getValue().equals("Vulcan2")) {

			if (mc.gameSettings.keyBindJump.isKeyDown()) {
				if (mc.thePlayer.posY % 1 <= 0.00153598) {
					mc.thePlayer.setPosition(mc.thePlayer.posX, Math.floor(mc.thePlayer.posY), mc.thePlayer.posZ);
					mc.thePlayer.motionY = 0.42F;
				}

			}

		}

		if (this.towermodule.getValue().equals("New")) {

			if (mc.gameSettings.keyBindJump.isKeyDown()) {

				this.mc.thePlayer.sendQueue
						.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX,
								this.mc.thePlayer.posY + 0.42, this.mc.thePlayer.posZ, this.mc.thePlayer.onGround));

				this.mc.thePlayer.sendQueue
						.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX,
								this.mc.thePlayer.posY + 0.300, this.mc.thePlayer.posZ, this.mc.thePlayer.onGround));

				this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.42,
						this.mc.thePlayer.posZ);
				this.mc.timer.timerSpeed = 0.5f;

			}
		}

		mc.timer.timerSpeed = timerboost.getValue().floatValue();

	}

	private boolean hotBlockError() {
		int i = 36;
		while (i < 45) {
			try {
				ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				if (stack == null || stack.getItem() == null || !(stack.getItem() instanceof ItemBlock)
						|| !isValid(stack.getItem())) {
					i++;
					continue;
				}
				return true;
			} catch (Exception ignored) {
			}
		}
		return false;
	}

	private void moveBlock() {
		if (this.getBlockCount() == 0)
			return;
		int slot = -1;
		int size = 0;
		getBlockCount();
		for (int i = 9; i < 36; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				Item item = mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				if (item instanceof ItemBlock && this.isValid(item) && is.stackSize > size) {
					size = is.stackSize;
					slot = i;
				}
			}
		}
		for (int i = 36; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				Item item = mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
				if (item instanceof ItemBlock) {
					this.isValid(item);
				}
			}
		}
		ItemStack is = new ItemStack(Item.getItemById(261));
		int bestInvSlot = slot;
		int bestHotbarSlot = moveHot();
		if (bestHotbarSlot > 0 && bestInvSlot > 0 && mc.thePlayer.inventoryContainer.getSlot(bestInvSlot).getHasStack()
				&& mc.thePlayer.inventoryContainer.getSlot(bestHotbarSlot).getHasStack()) {
			mc.thePlayer.inventoryContainer.getSlot(bestHotbarSlot).getStack();
			mc.thePlayer.inventoryContainer.getSlot(bestInvSlot).getStack();
		}
		if (hotBlockError()) {
			for (int a = 36; a < 45; a++) {
				if (!mc.thePlayer.inventoryContainer.getSlot(a).getHasStack()) {
					break;
				}
			}
		} else {
			for (int i = 9; i < 36; i++) {
				if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
					Item item = mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
					int count = 0;
					if (item instanceof ItemBlock && isValid(item)) {
						for (int a = 36; a < 45; a++) {
							if (Container.canAddItemToSlot(mc.thePlayer.inventoryContainer.getSlot(a), is, true)) {
								swap(i, a - 36);
								count++;
								break;
							}
						}
						if (count == 0) {
							swap(i, 7);
						}
						break;
					}
				}
			}
		}
	}

	private void swap(int slot, int hotbarNum) {
		mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, mc.thePlayer);
	}

	@EventHandler
	public void onMove(EventMove e) {

		if (this.Sprintdick.getValue().equals("Sprint")) {

			if (mc.gameSettings.keyBindForward.isPressed()) {

				this.mc.thePlayer.setSprinting(true);
				// this.mc.rightClickDelayTimer = 0;
			}

		}

		if (this.Sprintdick.getValue().equals("VulcanFast")) {
			
			this.mc.thePlayer.setSprinting(false);
			if (mc.thePlayer.onGround) {
				e.setY(mc.thePlayer.motionY = 0.012500047683714F);
			}

			mc.thePlayer.setSprinting(false);

			final double speed = 0.6F;
		
//     if (!mc.gameSettings.keyBindJump.isKeyDown() && speed > 0.2) {
			if (!mc.gameSettings.keyBindJump.isKeyDown() && speed > 0.3) {
				if (mc.thePlayer.onGround && mc.thePlayer.ticksExisted % 2 >= 0 && block2 <= 10) {
				
					mc.thePlayer.jump();
					mc.thePlayer.motionY = 0.012500047683714;
				}
			}
			time++;
			block2++;
			
			if(!mc.thePlayer.onGround) {
				mc.thePlayer.setMoveSpeed(e, speed);
			}
			switch (time) {
			case 1:
				mc.thePlayer.sendQueue.addToSendQueue(
						new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
				break;

			case 10:
				time = 0;
				break;
			}

		}

		else if (this.Sprintdick.getValue().equals("NoSprint")) {
			
			 this.mc.thePlayer.setSprinting(false);
			    mc.gameSettings.keyBindSprint.pressed = false;

		}

		else if (this.Sprintdick.getValue().equals("Hypixel")) {
			// if(mc.thePlayer.ticksExisted % 8 == 0) {

			// Helper.sendMessage("Insane Scaffold lol");
			// mc.gameSettings.keyBindSprint.pressed = true;
			// this.mc.thePlayer.setSprinting(true);
			// mc.gameSettings.keyBindSneak.pressed = true;

			// }

			// if (mc.thePlayer.isSneaking()) {

			// mc.gameSettings.keyBindSneak.pressed = false;

			// }
			///if (mc.thePlayer.onGround) {
				//mc.thePlayer.setMoveSpeed(e, 0.209);
				//0.209
				// 0.205
				// 7
		//	}
			
			if(!mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			if (mc.thePlayer.onGround && PlayerUtils.isMoving()) {
				mc.thePlayer.setMoveSpeed(e, 0.200);
				
            //   mc.thePlayer.setSpeed(PlayerUtils.isMoving() ? (0.16F) : 0);
               
               //25
               //20
//               / mc.thePlayer.setSpeed(PlayerUtils.isMoving() ? (0.13F) : 0);
                //0.13
			}	
			}
			
			    
			
			
			
			//if(PlayerUtils.isMoving() && mc.thePlayer.onGround) {
			//mc.thePlayer.setSpeed(0.22);
			//}

			// or mc.thePlayer.setMoveSpeed(e, 0.20);
			if(!mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			mc.gameSettings.keyBindSprint.pressed = false;
			this.mc.thePlayer.setSprinting(false);
			} else {
				if(PlayerUtils.isMoving()) {
					if(mc.thePlayer.onGround) {
					 mc.thePlayer.setSpeed(PlayerUtils.isMoving() ? (0.20F) : 0);
					}
				mc.gameSettings.keyBindSprint.pressed = true;
				this.mc.thePlayer.setSprinting(true);
				}
			}
			if (mc.thePlayer.hurtTime > 2) {
				mc.timer.timerSpeed = 1.0f;
			}

		}

		else if (this.Sprintdick.getValue().equals("HypixelNew")) {

			
			// int randomNumbercps = random.nextInt(max13 - min13 +
			// minimumcps.getValue().intValue()) + min13;
			
				//if (PlayerUtils.isMoving()) {
					if (mc.thePlayer.ticksExisted % 15 == 0) {

						mc.gameSettings.keyBindSneak.pressed = true;

					}if (mc.thePlayer.ticksExisted % 17 == 0) {
						mc.gameSettings.keyBindSneak.pressed = false;
					}
			//if (mc.thePlayer.ticksExisted % 10 == 0) {

				//mc.gameSettings.keyBindSneak.pressed = true;

				//}if (mc.thePlayer.ticksExisted % 12 == 0) {
				//mc.gameSettings.keyBindSneak.pressed = false;
		//	}
					//mc.thePlayer.setMoveSpeed(e, 0.10);
				     //     mc.thePlayer.setSpeed(PlayerUtils.isMoving() ? (0.19F) : 0);
	              //mc.thePlayer.setSpeed(PlayerUtils.isMoving() ? (0.25F) : 0);
					//0.24 0.30
					//if(mc.thePlayer.ticksExisted % 2 == 0) {
					//	mc.thePlayer.setSpeed(PlayerUtils.isMoving() ? (0.20) : 0);
					//}
					//if(mc.thePlayer.ticksExisted % 2 == 0) {
				//	mc.thePlayer.setSpeed(PlayerUtils.isMoving() ? (0.16) : 0);
					//mc.thePlayer.setSpeed(0.21);
					
					//0.21-0.25
					
				//	}
					//or just use 0.28 or 0.25 or 0.30
				//	mc.thePlayer.setSpeed(PlayerUtils.isMoving() ? (RandomUtils.nextDouble(0.27, 0.37)) : 0);
					 //27
					 //ONLY USE WITH SPEED
					// mc.thePlayer.setSpeed(PlayerUtils.isMoving() ? (RandomUtils.nextDouble(0.34, 0.45)) : 0);
	             // mc.thePlayer.setSpeed(PlayerUtils.isMoving() ? (0.35F) : 0);			
	            //  mc.gameSettings.keyBindSprint.pressed = false;
  			//	this.mc.thePlayer.setSprinting(false);
	               //26
	               //24
	              //25
	               //18
	               //19
	               //20
	               //22
			//	}
			
			
			
			
				
				//if(PlayerUtils.isMoving() && !mc.thePlayer.onGround) {
	               if(PlayerUtils.isMoving()) {
	            		mc.gameSettings.keyBindSprint.pressed = true;
	    				this.mc.thePlayer.setSprinting(true);
	               }
	               //25
	               //20
//	               / mc.thePlayer.setSpeed(PlayerUtils.isMoving() ? (0.13F) : 0);
	                //0.13
				
				

		}

		// esp.getValue()

		else if (this.Sprintdick.getValue().equals("Verus")) {
			mc.thePlayer.setMoveSpeed(e, 0.4);

		} else if (this.Sprintdick.getValue().equals("Vulcan")) {

			this.mc.rightClickDelayTimer = 1;
			mc.gameSettings.keyBindSprint.pressed = false;
			this.mc.thePlayer.setSprinting(false);
			if (this.canJump()) {
				mc.thePlayer.setMoveSpeed(e, 0.35);
				e.setY(mc.thePlayer.motionY = 0.20F);
				// or e.setY(mc.thePlayer.motionY = 0.19F);

			}

		}

		if (this.JumpMode.getValue().equals("Vannila")) {
			if (mc.gameSettings.keyBindForward.isKeyDown()) {
				if (mc.thePlayer.onGround) {
					e.setY(mc.thePlayer.motionY = 0.42F);
				}
			}
		}
			if (this.JumpMode.getValue().equals("None")) {

			}
			
			if (this.JumpMode.getValue().equals("Vannila2")) {
				if(mc.thePlayer.onGround) {
					mc.gameSettings.keyBindJump.pressed = true;
				} else {
					mc.gameSettings.keyBindJump.pressed = false;
				}
			}

		

	}

	private boolean canJump() {
		if (this.mc.thePlayer.moving() && mc.thePlayer.onGround) {
			return true;
		}
		return false;
	}

	@EventHandler
	public void onPost(EventPostUpdate eventPostUpdate) {
		if (this.placevnt.getValue().equals("OnPost")) {
			if ((!this.timer.delay(Scaffoldplace.getValue().intValue()) && PlayerUtils.isOnGround(0.01)
					&& !mc.gameSettings.keyBindJump.isKeyDown()) || this.getBlockCount() <= 0) {
				return;
			}
			this.timer.reset();
			this.b = this.slot != this.getBlockSlot();
			this.b = this.slot != this.getBlockSlot();
			if (this.itemspadf.getValue().equals("Spoof")) {
				boolean b2 = false;
				if (blockData != null) {
					if (this.isAirBlock(this.block)) {
						if (this.autoBlockCoolDown.delay(100L)) {
							if (this.b) {
								mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.getBlockSlot()));
								b2 = true;
							}
						}
						if (b2 || this.slot == this.getBlockSlot()) {
							if (swing.getValue()) {
								mc.thePlayer.swingItem();
							} else {
								mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
							}
							if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld,
									mc.thePlayer.inventory.getStackInSlot(this.slot), blockData.position,
									blockData.face, getVec3(blockData.position, blockData.face))) {
								this.block = null;
								this.blockData = null;
							}
						}

					}
				}
			}

			if (this.itemspadf.getValue().equals("New")) {
				boolean b2 = false;
				if (blockData != null) {
					if (this.isAirBlock(this.block)) {
						// if (this.autoBlockCoolDown.delay(100L)) {
						if (this.b) {
							mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.getBlockSlot()));
							b2 = true;
						}
					}
					if (b2 || this.slot == this.getBlockSlot()) {
						if (swing.getValue()) {
							mc.thePlayer.swingItem();
						} else {
							mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
						}
						if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld,
								mc.thePlayer.inventory.getStackInSlot(this.slot), blockData.position, blockData.face,
								getVec3(blockData.position, blockData.face))) {
							this.block = null;
							this.blockData = null;
						}
					}

				}
				// }
			}
			this.b = this.slot != this.getBlockSlot();
			if (this.itemspadf.getValue().equals("LiteSpoof")) {
				boolean b2 = false;
				if (blockData != null) {
					if (this.isAirBlock(this.block)) {
						if (this.autoBlockCoolDown.delay(100L)) {
							if (this.b) {
								mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.getBlockSlot()));

								b2 = true;
							}
						}
						if (b2 || this.slot == this.getBlockSlot()) {
							if (swing.getValue()) {
								mc.thePlayer.swingItem();
							} else {
								mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
							}
							if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld,
									mc.thePlayer.inventory.getStackInSlot(this.slot), blockData.position,
									blockData.face, getVec3(blockData.position, blockData.face))) {
								this.block = null;
								this.blockData = null;
							}
						}

					}
				}
			}
			this.b = this.slot != this.getBlockSlot();
			if (this.itemspadf.getValue().equals("None")) {
				boolean b2 = false;
				if (blockData != null) {
					if (this.isAirBlock(this.block)) {
						if (this.autoBlockCoolDown.delay(100L)) {
							if (this.b) {

								b2 = true;
							}
						}
						if (b2 || this.slot == this.getBlockSlot()) {
							if (swing.getValue()) {
								mc.thePlayer.swingItem();
							} else {

							}
							if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld,
									mc.thePlayer.inventory.getStackInSlot(this.slot), blockData.position,
									blockData.face, getVec3(blockData.position, blockData.face))) {
								this.block = null;
								this.blockData = null;
							}
						}

					}
				}
			}
		}
	}

	private int getBlockSlot() {
		int slot = -1;
		if (mc.thePlayer.getHeldItem() == null || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock)) {
			for (int i = 36; i < 45; ++i) {
				slot = i - 36;
				if (!Container.canAddItemToSlot(mc.thePlayer.inventoryContainer.getSlot(i),
						new ItemStack(Item.getItemById(261)), true)
						&& mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemBlock
						&& mc.thePlayer.inventoryContainer.getSlot(i).getStack() != null
						&& this.isValid(mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem())
						&& mc.thePlayer.inventoryContainer.getSlot(i).getStack().stackSize != 0) {
					break;
				}
			}
		} else {
			slot = mc.thePlayer.inventory.currentItem;
		}
		return slot;
	}

	@EventHandler
	public void onPacketRecive(EventPacketSend e) {

		// if (this.itemspadf.getValue().equals("New")) {
		// final Packet<?> packet = e.getPacket();

		// if (packet instanceof S2FPacketSetSlot) {
		// final S2FPacketSetSlot wrapper = ((S2FPacketSetSlot) packet);

		// if (wrapper.func_149174_e() == null) {
		// e.setCancelled(true);
		// } else {
		// try {
		// int slot = wrapper.func_149173_d() - 36;
		// if (slot < 0) return;
		// final ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(slot);
		// final Item item = wrapper.func_149174_e().getItem();

		// if ((itemStack == null && wrapper.func_149174_e().stackSize <= 6 && item
		// instanceof ItemBlock) ||
		// itemStack != null && Math.abs(Objects.requireNonNull(itemStack).stackSize -
		// wrapper.func_149174_e().stackSize) <= 6 ||
		// /l/ wrapper.func_149174_e() == null) {
		// e.setCancelled(true);
		// }
		// } catch (ArrayIndexOutOfBoundsException exception) {
		// exception.printStackTrace();
		// }
		// }
		// }
	}

	@EventHandler
	public void onPacketSend(EventPacketSend e) {
		// if (this.itemspadf.getValue().equals("New")) {
		// if (e.getPacket() instanceof C09PacketHeldItemChange)
		// e.setCancelled(true);
		// }
		
		if (this.towermodule.getValue().equals("WatchDog")) {
	    final Packet<?> packet = e.getPacket();
	    
        if (mc.thePlayer.motionY > -0.0784000015258789 && !mc.thePlayer.isPotionActive(Potion.jump) && packet instanceof C08PacketPlayerBlockPlacement && PlayerUtils.isMoving()) {
            final C08PacketPlayerBlockPlacement wrapper = ((C08PacketPlayerBlockPlacement) packet);
           
            if (wrapper.getPosition().equals(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.4, mc.thePlayer.posZ))) {
                mc.thePlayer.motionY = -0.0784000015258789;
            }
        } 
		}
		if (this.Sprintdick.getValue().equals("VulcanFast")) {
			if (e.getPacket() instanceof C08PacketPlayerBlockPlacement) {
				final C08PacketPlayerBlockPlacement wrapper = (C08PacketPlayerBlockPlacement) e.getPacket();

				if (wrapper.getPlacedBlockDirection() != 255) {
					block2 = 0;
				}
			}
		

		}

		if (e.getPacket() instanceof C09PacketHeldItemChange) {
			this.slot = ((C09PacketHeldItemChange) e.getPacket()).getSlotId();
		} else if (e.getPacket() instanceof C07PacketPlayerDigging) {
			if (this.slot != mc.thePlayer.inventory.currentItem) {
				mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
				this.slot = mc.thePlayer.inventory.currentItem;
			}
		}

	}

	private Vec3 getVec3(BlockPos blockPos, EnumFacing enumFacing) {
		double n = blockPos.getX() + 0.5;
		double n2 = blockPos.getY() + 0.5;
		double n3 = blockPos.getZ() + 0.5;
		double n4 = n + enumFacing.getFrontOffsetX() / 2.0;
		double n5 = n3 + enumFacing.getFrontOffsetZ() / 2.0;
		double n6 = n2 + enumFacing.getFrontOffsetY() / 2.0;
		if (enumFacing == EnumFacing.UP || enumFacing == EnumFacing.DOWN) {
			n4 += Math.random() * 0.6 - 0.3;
			n5 += Math.random() * 0.6 - 0.3;
		} else {
			n6 += Math.random() * 0.6 - 0.3;
		}
		if (enumFacing == EnumFacing.WEST || enumFacing == EnumFacing.EAST) {
			n5 += Math.random() * 0.6 - 0.3;
		}
		if (enumFacing == EnumFacing.SOUTH || enumFacing == EnumFacing.NORTH) {
			n4 += Math.random() * 0.6 - 0.3;
		}
		return new Vec3(n4, n6, n5);
	}

	private boolean isValid(Item item) {
		return item instanceof ItemBlock && !blacklist.contains(((ItemBlock) item).getBlock());
	}

	private Block getBlockByPos(BlockPos blockPos) {
		return mc.theWorld.getBlockState(blockPos).getBlock();
	}

	private boolean isNotAir(BlockPos blockPos) {
		return this.getBlockByPos(blockPos) != Blocks.air && !(this.getBlockByPos(blockPos) instanceof BlockLiquid);
	}

	private BlockData getBlockData(BlockPos down) {
		BlockData blockData = null;
		int n = 0;
		while (true) {
			if (n >= 2) {
				break;
			}
			if (this.isNotAir(down.add(0, 0, 1))) {
				blockData = new BlockData(down.add(0, 0, 1), EnumFacing.NORTH);
				break;
			}
			if (this.isNotAir(down.add(0, 0, -1))) {
				blockData = new BlockData(down.add(0, 0, -1), EnumFacing.SOUTH);
				break;
			}
			if (this.isNotAir(down.add(1, 0, 0))) {
				blockData = new BlockData(down.add(1, 0, 0), EnumFacing.WEST);
				break;
			}
			if (this.isNotAir(down.add(-1, 0, 0))) {
				blockData = new BlockData(down.add(-1, 0, 0), EnumFacing.EAST);
				break;
			}
			if (this.isNotAir(down.add(0, -1, 0))) {
				blockData = new BlockData(down.add(0, -1, 0), EnumFacing.UP);
				break;
			}
			if (this.isNotAir(down.add(0, 1, 0)) && this.shouldDown) {
				blockData = new Scaffold.BlockData(down.add(0, 1, 0), EnumFacing.DOWN);
				break;
			}
			if (this.isNotAir(down.add(0, 1, 1)) && this.shouldDown) {
				blockData = new Scaffold.BlockData(down.add(0, 1, 1), EnumFacing.DOWN);
				break;
			}
			if (this.isNotAir(down.add(0, 1, -1)) && this.shouldDown) {
				blockData = new Scaffold.BlockData(down.add(0, 1, -1), EnumFacing.DOWN);
				break;
			}
			if (this.isNotAir(down.add(1, 1, 0)) && this.shouldDown) {
				blockData = new Scaffold.BlockData(down.add(1, 1, 0), EnumFacing.DOWN);
				break;
			}
			if (this.isNotAir(down.add(-1, 1, 0)) && this.shouldDown) {
				blockData = new Scaffold.BlockData(down.add(-1, 1, 0), EnumFacing.DOWN);
				break;
			}
			if (this.isNotAir(down.add(1, 0, 1))) {
				blockData = new BlockData(down.add(1, 0, 1), EnumFacing.NORTH);
				break;
			}
			if (this.isNotAir(down.add(-1, 0, -1))) {
				blockData = new BlockData(down.add(-1, 0, -1), EnumFacing.SOUTH);
				break;
			}
			if (this.isNotAir(down.add(1, 0, 1))) {
				blockData = new BlockData(down.add(1, 0, 1), EnumFacing.WEST);
				break;
			}
			if (this.isNotAir(down.add(-1, 0, -1))) {
				blockData = new BlockData(down.add(-1, 0, -1), EnumFacing.EAST);
				break;
			}
			if (this.isNotAir(down.add(-1, 0, 1))) {
				blockData = new BlockData(down.add(-1, 0, 1), EnumFacing.NORTH);
				break;
			}
			if (this.isNotAir(down.add(1, 0, -1))) {
				blockData = new BlockData(down.add(1, 0, -1), EnumFacing.SOUTH);
				break;
			}
			if (this.isNotAir(down.add(1, 0, -1))) {
				blockData = new BlockData(down.add(1, 0, -1), EnumFacing.WEST);
				break;
			}
			if (this.isNotAir(down.add(-1, 0, 1))) {
				blockData = new BlockData(down.add(-1, 0, 1), EnumFacing.EAST);
				break;
			}
			down = down.down();
			++n;
		}
		return blockData;
	}

	public boolean isAirBlock(Block block) {
		return block.getMaterial().isReplaceable()
				&& (!(block instanceof BlockSnow) || block.getBlockBoundsMaxY() <= 0.125);
	}

	public int getBlockCount() {
		int blockCount = 0;
		for (int i = 0; i < 45; ++i) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				Item item = is.getItem();
				if (is.getItem() instanceof ItemBlock && !blacklist.contains(((ItemBlock) item).getBlock())) {
					blockCount += is.stackSize;
				}
			}
		}
		return blockCount;
	}

	static class BlockData {
		public BlockPos position;
		public EnumFacing face;

		private BlockData(BlockPos position, EnumFacing face) {
			this.position = position;
			this.face = face;
		}
	}
	
	

	@EventHandler
	public void renderHud(EventRender2D event) {
		int i3 = 0;
		Color startColor = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(),
				ClientSettings.b.getValue().intValue(), 150);
		Color endColor = new Color(ClientSettings.r2.getValue().intValue(), ClientSettings.g2.getValue().intValue(),
				ClientSettings.b2.getValue().intValue(), 150);
		Pair<Color, Color> colors = Pair.of(startColor, endColor);
		Color c = Render2.interpolateColorsBackAndForth(7, 3 + (i3 * 20), colors.getFirst(), colors.getSecond(), false);
		//RenderUtil.drawRect(456, 320, 510, 300, new Color(000, 000, 000, 150).getRGB());
		//RenderUtil.drawRect(456, 298, 510, 300, c.getRGB());
		// RenderUtil.drawRect(455, 320, 510, 300, new Color
		// (000,000,000,200).getRGB());
		// Color c = new Color(ClientSettings.r.getValue().intValue(),
		// ClientSettings.g.getValue().intValue(),
		// ClientSettings.b.getValue().intValue());
		
		
		FontLoaders.arial18.drawStringWithShadow(getBlockCount() + " Blocks", 460, 306,	new Color(255, 255, 255).getRGB());
			
		

	}

	static class RotationData {
		public float distance;
		public int index;

		private RotationData(float distance, int index) {
			this.distance = distance;
			this.index = index;
		}
	}
}
