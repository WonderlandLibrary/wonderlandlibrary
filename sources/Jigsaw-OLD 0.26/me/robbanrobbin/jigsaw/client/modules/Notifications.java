package me.robbanrobbin.jigsaw.client.modules;

import org.darkstorm.minecraft.gui.component.Button;
import org.darkstorm.minecraft.gui.component.Component;
import org.darkstorm.minecraft.gui.component.basic.BasicCheckButton;
import org.darkstorm.minecraft.gui.listener.ButtonListener;
import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.network.play.client.C01PacketChatMessage;

public class Notifications extends Module {

	@Override
	public Component[] getModSettings() {
		final BasicCheckButton box1 = new BasicCheckButton("On Hack Enabled");
		box1.setSelected(ClientSettings.notificationModulesEnable);
		box1.addButtonListener(new ButtonListener() {

			@Override
			public void onRightButtonPress(Button button) {

			}

			@Override
			public void onButtonPress(Button button) {
				ClientSettings.notificationModulesEnable = box1.isSelected();
			}
		});
		
		final BasicCheckButton box2 = new BasicCheckButton("On Hack Disabled");
		box2.setSelected(ClientSettings.notificationModulesDisable);
		box2.addButtonListener(new ButtonListener() {

			@Override
			public void onRightButtonPress(Button button) {

			}

			@Override
			public void onButtonPress(Button button) {
				ClientSettings.notificationModulesDisable = box2.isSelected();
			}
		});
		return new Component[]{box1, box2};
	}
	
	public Notifications() {
		super("Notifications", Keyboard.KEY_NONE, Category.SETTINGS);
	}

	@Override
	public void onDisable() {

		super.onDisable();
	}

	@Override
	public void onEnable() {

		super.onEnable();
	}

	@Override
	public void onUpdate() {

		super.onUpdate();
	}

}
