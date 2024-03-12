package xyz.cucumber.base.module.feat.player;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.ext.EventMoveButton;
import xyz.cucumber.base.events.ext.EventUpdate;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.settings.BooleanSettings;
import xyz.cucumber.base.module.settings.ModeSettings;
import xyz.cucumber.base.module.settings.NumberSettings;
import xyz.cucumber.base.utils.Timer;
import xyz.cucumber.base.utils.game.InventoryUtils;

@ModuleInfo(category = Category.PLAYER, description = "Automatically puts armor on you", name = "Auto Armor")
public class AutoArmorModule extends Mod {
	public Timer startTimer = new Timer();
	public Timer timer = new Timer();

	public ModeSettings mode = new ModeSettings("Mode", new String[] { "Open Inv", "Spoof" });
	public BooleanSettings intave = new BooleanSettings("Intave", true);
	public NumberSettings startDelay = new NumberSettings("Start Delay", 150.0D, 0.0D, 1000.0D, 1);
	public NumberSettings speed = new NumberSettings("Speed", 150, 0, 1000, 1);

	public KeyBinding[] moveKeys = new KeyBinding[] { mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack,
			mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindRight, mc.gameSettings.keyBindJump,
			mc.gameSettings.keyBindSneak };

	public AutoArmorModule() {
		this.addSettings(mode, intave, startDelay, speed);
	}

	@EventListener
	public void onMoveButton(EventMoveButton event) {
		if (InventoryUtils.isInventoryOpen) {
			event.forward = false;
			event.backward = false;
			event.left = false;
			event.right = false;
			event.jump = false;
			event.sneak = false;
		}
	}

	@EventListener
	public void onUpdate(EventUpdate e) {
		setInfo(mode.getMode());
		
		if (mode.getMode().equalsIgnoreCase("Spoof") && mc.currentScreen != null) {
			return;
		}

		if (mode.getMode().equalsIgnoreCase("Open Inv")) {
			if (mc.currentScreen == null) {
				startTimer.reset();
			}
			if (!startTimer.hasTimeElapsed(startDelay.getValue(), false)) {
				return;
			}
		}

		if (InventoryUtils.timer.hasTimeElapsed(speed.value, false)) {
			if (mode.getMode().equalsIgnoreCase("Open Inv")
					&& !(this.mc.currentScreen instanceof net.minecraft.client.gui.inventory.GuiInventory))
				return;

			for (int type = 1; type < 5; type++) {
				if (mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()) {
					ItemStack is = mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack();

					if (InventoryUtils.isBestArmor(is, type))
						continue;

					InventoryUtils.openInv(mode.getMode());
					InventoryUtils.drop(4 + type);

					InventoryUtils.timer.reset();
					if (speed.getValue() != 0)
						break;
				}
			}
			for (int type = 1; type < 5; type++) {
				if (InventoryUtils.timer.getTime() > speed.getValue()) {
					for (int i = 9; i < 45; i++) {
						if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
							ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
							if (InventoryUtils.getProtection(is) > 0.0F) {
								if (InventoryUtils.isBestArmor(is, type)
										&& !InventoryUtils.isBadStack(is, true, true)) {
									InventoryUtils.openInv(mode.getMode());
									InventoryUtils.shiftClick(i);

									InventoryUtils.timer.reset();
									if (speed.getValue() != 0)
										break;
								}
							}
						}
					}
				}
			}
		}
		if (InventoryUtils.timer.getTime() > 75) {
			InventoryUtils.closeInv(mode.getMode());
		}
	}
}
