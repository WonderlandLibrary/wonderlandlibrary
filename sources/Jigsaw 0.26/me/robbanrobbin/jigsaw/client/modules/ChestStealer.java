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
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class ChestStealer extends Module {

	ArrayList<TileEntityChest> openedChests = new ArrayList<TileEntityChest>();
	private boolean stole = true;
	private WaitTimer timer = new WaitTimer();

	@Override
	public ModSetting[] getModSettings() {
		// final BasicCheckButton box1 = new BasicCheckButton("ChestAura");
		// box1.setSelected(ClientSettings.chestStealerAura);
		// box1.addButtonListener(new ButtonListener() {
		//
		// @Override
		// public void onRightButtonPress(Button button) {
		//
		// }
		//
		// @Override
		// public void onButtonPress(Button button) {
		// ClientSettings.chestStealerAura = box1.isSelected();
		// }
		// });
		//
		// final BasicCheckButton box2 = new BasicCheckButton("Delay");
		// box2.setSelected(ClientSettings.chestStealDelay);
		// box2.addButtonListener(new ButtonListener() {
		//
		// @Override
		// public void onRightButtonPress(Button button) {
		//
		// }
		//
		// @Override
		// public void onButtonPress(Button button) {
		// ClientSettings.chestStealDelay = box2.isSelected();
		// }
		// });
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
			if ((this.mc.thePlayer.openContainer != null) && ((this.mc.thePlayer.openContainer instanceof ContainerChest))) {
				ContainerChest container = (ContainerChest) this.mc.thePlayer.openContainer;
				for (int i = 0; i < container.getLowerChestInventory().getSizeInventory(); i++) {
					if ((container.getLowerChestInventory().getStackInSlot(i) != null) && (timer.hasTimeElapsed(100, true))) {
						this.mc.playerController.windowClick(container.windowId, i, 0, 1, this.mc.thePlayer);
					}
				}
			}
		}
		if (!stole) {
			return;
		}
		if (ClientSettings.chestStealerAura) {
			for (TileEntity tileEntity : mc.theWorld.loadedTileEntityList) {
				if (!(tileEntity instanceof TileEntityChest)) {
					continue;
				}
				TileEntityChest chestTile = (TileEntityChest) tileEntity;
				if (mc.thePlayer.getDistanceSq(tileEntity.getPos()) < 17) {
					if (mc.currentScreen == null) {
						if (openedChests.contains(tileEntity)) {
							continue;
						}
						stole = false;
						float[] rots = Utils.getFacePos(Utils.getVec3(tileEntity.getPos()));
						event.yaw = rots[0];
						event.pitch = rots[1];
						mc.thePlayer.swingItem();
						mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld,
								mc.thePlayer.getCurrentEquippedItem(),
								tileEntity.getPos(), EnumFacing.getFacingFromVector((float) mc.thePlayer.posX,
										(float) mc.thePlayer.posY, (float) mc.thePlayer.posZ),
								new Vec3(tileEntity.getPos()));
						openedChests.add(chestTile);
						break;
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
				mc.thePlayer.closeScreen();
				stole = true;
			}
		}
		super.onLateUpdate();
	}

}
