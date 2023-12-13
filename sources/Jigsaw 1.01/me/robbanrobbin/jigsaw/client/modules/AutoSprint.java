package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.CheckBtnSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ModSetting;
import me.robbanrobbin.jigsaw.module.Module;

public class AutoSprint extends Module {

	public AutoSprint() {
		super("AutoSprint", Keyboard.KEY_V, Category.MOVEMENT, "Sprints everytime you walk.");
	}

	@Override
	public ModSetting[] getModSettings() {
		return new ModSetting[]{new CheckBtnSetting("Disable when too hungry", "autoSprintStopWhenHungry")};
	}
	
	@Override
	public void onDisable() {
		mc.player.setSprinting(false);
		super.onDisable();
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
	}
	
	@Override
	public void onLivingUpdate() {
		if(Jigsaw.disableSprint) {
			mc.player.setSprinting(false);
			return;
		}
		if(ClientSettings.autoSprintStopWhenHungry && this.mc.player.getFoodStats().getFoodLevel() <= 6) {
			mc.player.setSprinting(false);
			return;
		}
		if (currentMode.equals("Forwards")) {
			if(Jigsaw.getModuleByName("NoSlowdown").isToggled()) {
				if (mc.gameSettings.keyBindForward.isKeyDown()) {
					mc.player.setSprinting(true);
				} else {
					mc.player.setSprinting(false);
				}
			}
			else {
				if (mc.gameSettings.keyBindForward.isKeyDown() && !mc.player.isHandActive() && !mc.player.isSneaking()) {
					mc.player.setSprinting(true);
				} else {
					mc.player.setSprinting(false);
				}
			}
		} else {
			if(Jigsaw.getModuleByName("NoSlowdown").isToggled()) {
				if ((mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown()
						|| mc.gameSettings.keyBindRight.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown())) {
					mc.player.setSprinting(true);
				} else {
					mc.player.setSprinting(false);
				}
			}
			else {
				if ((mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown()
						|| mc.gameSettings.keyBindRight.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown()) && !mc.player.isHandActive() && !mc.player.isSneaking()) {
					mc.player.setSprinting(true);
				} else {
					mc.player.setSprinting(false);
				}
			}
			
		}
		super.onLivingUpdate();
	}

	@Override
	public void onLateUpdate() {
		
		super.onLateUpdate();
	}

	@Override
	public String[] getModes() {
		return new String[] { "Forwards", "MultiDir" };
	}

}
