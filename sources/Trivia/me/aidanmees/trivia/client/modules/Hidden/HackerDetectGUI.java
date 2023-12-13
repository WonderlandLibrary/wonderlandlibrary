package me.aidanmees.trivia.client.modules.Hidden;

import java.util.ArrayList;
import java.util.HashMap;

import org.darkstorm.minecraft.gui.component.Button;
import org.darkstorm.minecraft.gui.component.basic.BasicCheckButton;
import org.darkstorm.minecraft.gui.listener.ButtonListener;
import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.gui.custom.clickgui.ModSetting;
import me.aidanmees.trivia.hackerdetect.Hacker;
import me.aidanmees.trivia.hackerdetect.checks.Check;
import me.aidanmees.trivia.hackerdetect.gui.GuitriviaHackerDetect;
import me.aidanmees.trivia.module.Module;

public class HackerDetectGUI extends Module {

	public static HashMap<String, Hacker> players = new HashMap<String, Hacker>();
	public static ArrayList<String> muted = new ArrayList<String>();

//	@Override
//	public ModSetting[] getModSettings() {
//		ArrayList<BasicCheckButton> btns = new ArrayList<BasicCheckButton>();
//		for (final Check check : HackerDetect.checks) {
//			final BasicCheckButton btn = new BasicCheckButton(check.getName());
//			btn.addButtonListener(new ButtonListener() {
//				@Override
//				public void onRightButtonPress(Button button) {
//
//				}
//
//				@Override
//				public void onButtonPress(Button button) {
//					for (Check check1 : HackerDetect.checks) {
//						if (check1.getName().equals(check.getName())) {
//							check1.setEnabled(btn.isSelected());
//							break;
//						}
//					}
//					HackerDetect.updateEnabledChecks();
//				}
//			});
//			btn.setSelected(check.isEnabled());
//			btns.add(btn);
//		}
//		return btns.toArray(new ModSetting[btns.size()]);
//	}

	public HackerDetectGUI() {
		super("HackerDetectGUI", Keyboard.KEY_NONE, Category.WORLD, "Opens a list of all hacker detections made by the trivia Hacker Detect.");
	}

	@Override
	public void onClientLoad() {

		super.onClientLoad();
	}

	@Override
	public void onEnable() {
		mc.displayGuiScreen(new GuitriviaHackerDetect(mc.currentScreen));
		this.setToggled(false, true);
		super.onEnable();
	}
}
