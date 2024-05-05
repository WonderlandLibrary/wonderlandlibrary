package me.aidanmees.trivia.client.modules.Render;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.client.settings.ClientSettings;
import me.aidanmees.trivia.gui.custom.clickgui.CheckBtnSetting;
import me.aidanmees.trivia.gui.custom.clickgui.ModSetting;
import me.aidanmees.trivia.gui.custom.clickgui.SliderSetting;
import me.aidanmees.trivia.gui.custom.clickgui.ValueFormat;
import me.aidanmees.trivia.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityExplodeFX;
import net.minecraft.client.particle.EntityFirework;
import net.minecraft.client.particle.EntityFireworkOverlayFX;
import net.minecraft.client.particle.EntityFireworkSparkFX;
import net.minecraft.client.particle.EntityFireworkStarterFX;
import net.minecraft.client.particle.EntityFireworkStarterFX_Factory;
import net.minecraft.client.particle.EntityHugeExplodeFX;
import net.minecraft.client.particle.EntityLargeExplodeFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFireworkRocket;

public class NoRender extends Module {

	static boolean EorD = mc.gameSettings.ofFireworkParticles;
	
	
	public NoRender() {
		super("NoRender", Keyboard.KEY_NONE, Category.RENDER, "Disables.");
	}



	@Override
	public void onDisable() {
		mc.gameSettings.ofFireworkParticles = EorD;
		super.onDisable();
	}

	@Override
	public void onEnable() {
		EorD = mc.gameSettings.ofFireworkParticles;
		super.onEnable();
	}

	@Override
	public void onUpdate() {
		if (ClientSettings.FireWork) {
		mc.gameSettings.ofFireworkParticles = false;
		}
		super.onUpdate();
	}
	@Override
	public ModSetting[] getModSettings() {
//		BasicSlider slider1 = new BasicSlider("Flight Speed", ClientSettings.FlightdefaultSpeed, 0, 10, 0,
//				ValueDisplay.DECIMAL);
//		SliderListener listener1 = new SliderListener() {
//
//			@Override
//			public void onSliderValueChanged(Slider slider) {
//				ClientSettings.FlightdefaultSpeed = slider.getValue();
//			}
//		};
//		slider1.addSliderListener(listener1);
//		final BasicCheckButton box1 = new BasicCheckButton("Default Smooth Flight");
//		box1.setSelected(ClientSettings.Flightsmooth);
//		ButtonListener listener2 = new ButtonListener() {
//
//			@Override
//			public void onRightButtonPress(Button button) {
//
//			}
//
//			@Override
//			public void onButtonPress(Button button) {
//				ClientSettings.Flightsmooth = box1.isSelected();
//			}
//		};
//		box1.addButtonListener(listener2);
//		final BasicCheckButton box2 = new BasicCheckButton("Flight Kick Bypass");
//		box2.setSelected(ClientSettings.flightkick);
//		ButtonListener listener3 = new ButtonListener() {
//
//			@Override
//			public void onRightButtonPress(Button button) {
//
//			}
//
//			@Override
//			public void onButtonPress(Button button) {
//				ClientSettings.flightkick = box2.isSelected();
//			}
//		};
//		box2.addButtonListener(listener3);
//		
//		final BasicCheckButton box3 = new BasicCheckButton("Glide Damage");
//		box3.setSelected(ClientSettings.glideDmg);
//		ButtonListener listener4 = new ButtonListener() {
//
//			@Override
//			public void onRightButtonPress(Button button) {
//
//			}
//
//			@Override
//			public void onButtonPress(Button button) {
//				ClientSettings.glideDmg = box3.isSelected();
//			}
//		};
//		box3.addButtonListener(listener4);
//		
//		final BasicCheckButton box4 = new BasicCheckButton("onGround Spoof");
//		box4.setSelected(ClientSettings.onGroundSpoofFlight);
//		ButtonListener listener5 = new ButtonListener() {
//
//			@Override
//			public void onRightButtonPress(Button button) {
//
//			}
//
//			@Override
//			public void onButtonPress(Button button) {
//				ClientSettings.onGroundSpoofFlight = box4.isSelected();
//			}
//		};
//		box3.addButtonListener(listener5);
		CheckBtnSetting box = new CheckBtnSetting("Items", "ITEMS");
		CheckBtnSetting box2 = new CheckBtnSetting("Firework", "FireWork");
		CheckBtnSetting box1 = new CheckBtnSetting("Holograms", "HOLOGRAM");
		
		
		return new ModSetting[] { box, box2, box1};
	}

	
}
