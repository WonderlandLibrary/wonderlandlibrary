package me.robbanrobbin.jigsaw.client.modules;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.CheckBtnSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ModSetting;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.block.BlockContainer;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemAir;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class ChestStealer extends Module {

	ArrayList<TileEntityChest> openedChests = new ArrayList<TileEntityChest>();
	private boolean stole = true;
	private WaitTimer timer = new WaitTimer();

	@Override
	public ModSetting[] getModSettings() {
		CheckBtnSetting box1 = new CheckBtnSetting("ChestAura", "chestStealerAura");
		CheckBtnSetting box2 = new CheckBtnSetting("Delay", "chestStealDelay");
		return new ModSetting[] { box1, box2 };
	}

	public ChestStealer() {
		super("ChestStealer", Keyboard.KEY_K, Category.PLAYER,
				"Automatically steals items from chests, really useful in survival games.");
	}

	@Override
	public void onDisable() {
		openedChests.clear();
		stole = true;
		super.onDisable();
	}

	@Override
	public void onEnable() {
		openedChests.clear();
		stole = true;
		super.onEnable();
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		if(ClientSettings.chestStealDelay) {
			if ((this.mc.player.openContainer != null) && ((this.mc.player.openContainer instanceof ContainerChest))) {
				ContainerChest container = (ContainerChest) this.mc.player.openContainer;
				for (int i = 0; i < container.getLowerChestInventory().getSizeInventory(); i++) {
					if (container.getLowerChestInventory().getStackInSlot(i) != null && !(container.getLowerChestInventory().getStackInSlot(i).getItem() instanceof ItemAir) && timer.hasTimeElapsed(100, true)) {
						System.out.println(container.getLowerChestInventory().getStackInSlot(i).getItem());
						this.mc.playerController.windowClick(container.windowId, i, 0, ClickType.QUICK_MOVE, this.mc.player);
						this.mc.playerController.windowClick(container.windowId, i, 0, ClickType.PICKUP_ALL, this.mc.player);
					}
				}
			}
		}
		if (ClientSettings.chestStealerAura) {
			for (TileEntity tileEntity : mc.world.loadedTileEntityList) {
				if (!(tileEntity instanceof TileEntityChest)) {
					continue;
				}
				TileEntityChest chestTile = (TileEntityChest) tileEntity;
				if (mc.player.getDistanceSq(tileEntity.getPos()) < 17) {
					if (mc.currentScreen == null) {
						if (openedChests.contains(tileEntity)) {
							continue;
						}
						stole = false;
						float[] rots = Utils.getFacePos(Utils.getVec3d(tileEntity.getPos()));
						event.yaw = rots[0];
						event.pitch = rots[1];
						RayTraceResult rayTrace = mc.world.rayTraceBlocks(Utils.getPlayerVec3dEyeHeight().add(new Vec3d(mc.player.posZ, mc.player.posY, mc.player.posZ)), 
								Utils.getVec3d(tileEntity.getPos().add(0.5, 0.5, 0.5)), false, false, true);
						if(rayTrace != null && rayTrace.getBlockPos() != null) {
							
							mc.playerController.processRightClickBlock(mc.player, mc.world, tileEntity.getPos(),
									rayTrace.sideHit, rayTrace.hitVec, EnumHand.OFF_HAND);
							
							mc.player.swingArm(EnumHand.OFF_HAND);
							openedChests.add(chestTile);
							break;
						}
					}
				}
			}
		}
		super.onUpdate(event);
	}

	@Override
	public void onLateUpdate() {
		if (mc.currentScreen instanceof GuiChest) {
			GuiChest chest = (GuiChest) mc.currentScreen;
			chest.steal();
			if (chest.isEmpty()) {
				mc.player.closeScreen();
				stole = true;
			}
		}
		super.onLateUpdate();
	}

}
