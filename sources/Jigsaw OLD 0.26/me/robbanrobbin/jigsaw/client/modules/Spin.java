package me.robbanrobbin.jigsaw.client.modules;

import org.darkstorm.minecraft.gui.component.BoundedRangeComponent.ValueDisplay;
import org.darkstorm.minecraft.gui.component.Component;
import org.darkstorm.minecraft.gui.component.Slider;
import org.darkstorm.minecraft.gui.component.basic.BasicSlider;
import org.darkstorm.minecraft.gui.listener.SliderListener;
import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;

public class Spin extends Module {

	private float spin = 0;

	@Override
	public Component[] getModSettings() {
		BasicSlider slider1 = new BasicSlider("Spin Speed", ClientSettings.Spinspeed, 10, 180, 0, ValueDisplay.DECIMAL);
		slider1.addSliderListener(new SliderListener() {
			@Override
			public void onSliderValueChanged(Slider slider) {
				ClientSettings.Spinspeed = slider.getValue();
			}
		});
		return new Component[] { slider1 };
	}

	public Spin() {
		super("Spin", Keyboard.KEY_NONE, Category.FUN, "You do 360s for other players!");
	}
	
	@Override
	public void onUpdate(UpdateEvent event) {
		if(Jigsaw.doDisablePacketSwitch()) {
			return;
		}
		spin += ClientSettings.Spinspeed;
		spin = Utils.normalizeAngle(spin);
		event.yaw = spin;
		super.onUpdate(event);
	}

}
