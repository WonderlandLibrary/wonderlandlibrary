package me.aidanmees.trivia.client.modules.Player;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.client.settings.ClientSettings;
import me.aidanmees.trivia.gui.custom.clickgui.ModSetting;
import me.aidanmees.trivia.gui.custom.clickgui.SliderSetting;
import me.aidanmees.trivia.gui.custom.clickgui.ValueFormat;
import me.aidanmees.trivia.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Regen extends Module {

	@Override
	public ModSetting[] getModSettings() {
		// regenSpeedSlider

//		Slider regenSpeedSlider = new BasicSlider("Regen Packets", Regen.getSpeed(), 10.0, 1000.0, 0.0,
//				ValueDisplay.INTEGER);
//
//		regenSpeedSlider.addSliderListener(new SliderListener() {
//
//			@Override
//			public void onSliderValueChanged(Slider slider) {
//
//				Regen.setSpeed(slider.getValue());
//
//			}
//		});
		SliderSetting<Number> regenSpeedSlider = new SliderSetting<Number>("Regen Packets", ClientSettings.Regenspeed, 10.0, 1000.0, 0.0, ValueFormat.INT);
		return new ModSetting[] { regenSpeedSlider };
	}

	public Regen() {
		super("Regen", Keyboard.KEY_NONE, Category.COMBAT, "Regens health (and drains food) more quickly");
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

		// if(mc.gameSettings.keyBindUseItem.isKeyDown()) {
		// return;
		// }
		if (currentMode.equals("Packet")) {
		if (!mc.thePlayer.capabilities.isCreativeMode && mc.thePlayer.getFoodStats().getFoodLevel() > 17
				&& mc.thePlayer.getHealth() < mc.thePlayer.getMaxHealth() && mc.thePlayer.getHealth() != 0
				&& !trivia.getModuleByName("Blink").isToggled()) {

			for (int i = 0; i < ClientSettings.Regenspeed; i++) {
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
			}
		}
		}
		if (currentMode.equals("Guardian")) {
            if(mc.thePlayer.getHealth() < mc.thePlayer.getMaxHealth() && mc.thePlayer.getFoodStats().getFoodLevel() > 17 && mc.thePlayer.getHealth() != 0) {
             for (int i = 0; i < 11; i++) {
                    try {
                        Thread.sleep(1l);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
                            mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                }
        }

		}

		super.onUpdate();
	}

	public static void setSpeed(double set) {
		ClientSettings.Regenspeed = set;
	}

	public static double getSpeed() {
		return ClientSettings.Regenspeed;
	}
	@Override
	public String[] getModes() {
		return new String[] { "Packet", "Guardian"};
	}

	@Override
	public String getAddonText() {
		return currentMode;
	}


}
