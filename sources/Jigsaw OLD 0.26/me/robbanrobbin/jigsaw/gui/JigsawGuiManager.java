/*
 * Copyright (c) 2013, DarkStorm (darkstorm@evilminecraft.net)
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: 
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package me.robbanrobbin.jigsaw.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.darkstorm.minecraft.gui.AbstractGuiManager;
import org.darkstorm.minecraft.gui.component.BoundedRangeComponent.ValueDisplay;
import org.darkstorm.minecraft.gui.component.Button;
import org.darkstorm.minecraft.gui.component.CheckButton;
import org.darkstorm.minecraft.gui.component.ComboBox;
import org.darkstorm.minecraft.gui.component.Component;
import org.darkstorm.minecraft.gui.component.Frame;
import org.darkstorm.minecraft.gui.component.Slider;
import org.darkstorm.minecraft.gui.component.basic.BasicButton;
import org.darkstorm.minecraft.gui.component.basic.BasicCheckButton;
import org.darkstorm.minecraft.gui.component.basic.BasicComboBox;
import org.darkstorm.minecraft.gui.component.basic.BasicFrame;
import org.darkstorm.minecraft.gui.component.basic.BasicLabel;
import org.darkstorm.minecraft.gui.component.basic.BasicSlider;
import org.darkstorm.minecraft.gui.layout.GridLayoutManager;
import org.darkstorm.minecraft.gui.layout.GridLayoutManager.HorizontalGridConstraint;
import org.darkstorm.minecraft.gui.listener.ButtonListener;
import org.darkstorm.minecraft.gui.listener.ComboBoxListener;
import org.darkstorm.minecraft.gui.listener.SliderListener;
import org.darkstorm.minecraft.gui.theme.Theme;
import org.darkstorm.minecraft.gui.theme.simple.SimpleTheme;
import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.bypasses.Bypass;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.modules.target.AuraUtils;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.Minecraft;

/**
 * Minecraft GUI API
 * 
 * This class is not actually intended for use; rather, you should use this as a
 * template for your actual GuiManager, as the creation of frames is highly
 * implementation-specific.
 * 
 * @author DarkStorm (darkstorm@evilminecraft.net)
 */
public final class JigsawGuiManager extends AbstractGuiManager {
	public class ModuleFrame extends BasicFrame {
		public Module module;

		private ModuleFrame() {
		}

		private ModuleFrame(String title) {
			super(title);
		}

		private ModuleFrame(Module module) {
			this.module = module;
		}
	}

	private final AtomicBoolean setup;

	public JigsawGuiManager() {
		setup = new AtomicBoolean();
	}

	@Override
	public void setup() {
		if (!setup.compareAndSet(false, true)) {
			this.frames.clear();
		}

		// Sample module frame setup

		final Map<Category, ModuleFrame> categoryFrames = new HashMap<Category, ModuleFrame>();
		for (Module module : Jigsaw.getModules()) {
			if (module.getCategory() == Category.HIDDEN) {
				continue;
			}
			ModuleFrame frame = categoryFrames.get(module.getCategory());
			if (frame == null) {
				String name = module.getCategory().name().toLowerCase();
				name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
				frame = new ModuleFrame(name);
				// Frame not null

				frame.setTheme(theme);
				frame.setLayoutManager(new GridLayoutManager(1, 0));
				frame.setVisible(true);
				frame.setClosable(false);
				frame.setMinimized(true);
				frame.setPinnable(false);
				addFrame(frame);
				categoryFrames.put(module.getCategory(), frame);
			}

			final Module updateModule = module;
			if (updateModule.isCheckbox()) {
				final BasicCheckButton checkButton = new BasicCheckButton() {
					@Override
					public void update() {
						setSelected(updateModule.isToggled());
						if (updateModule.getKeyboardKey() == Keyboard.KEY_NONE) {
							setText(updateModule.getName());
						} else {
							setText(updateModule.getName() + " [" + Keyboard.getKeyName(updateModule.getKeyboardKey())
									+ "]");
						}
					}
				};

				checkButton.addButtonListener(new ButtonListener() {
					@Override
					public void onButtonPress(Button button) {
						updateModule.setToggled(checkButton.isSelected(), true);
					}

					@Override
					public void onRightButtonPress(Button button) {
						//

						if (!(updateModule.getModSettings() != null || updateModule.getModes().length > 1)) {
							displayKeyBindScreen(updateModule);
							return;
						}
						desplaySettingsScreen(updateModule, button);
					}
				});
				if (updateModule.getKeyboardKey() == Keyboard.KEY_NONE) {
					checkButton.setText(updateModule.getName());
				} else {
					checkButton.setText(
							updateModule.getName() + " [" + Keyboard.getKeyName(updateModule.getKeyboardKey()) + "]");
				}
				checkButton.setModule(updateModule);
				frame.add(checkButton, HorizontalGridConstraint.FILL);
			} else {
				final BasicButton button = new BasicButton() {
					@Override
					public void update() {
						setBackgroundColor(updateModule.isToggled() ? ClientSettings.buttonSelectedColor
								: ClientSettings.guiBackgroundColor);
						setForegroundColor(
								updateModule.isToggled() ? new Color(1f, 1f, 1f, 1f) : new Color(0.8f, 0.8f, 0.8f, 1f));
						if (updateModule.getKeyboardKey() == Keyboard.KEY_NONE) {
							setText(updateModule.getName());
						} else {
							setText(updateModule.getName() + " [" + Keyboard.getKeyName(updateModule.getKeyboardKey())
									+ "]");
						}
					}
				};

				button.addButtonListener(new ButtonListener() {
					@Override
					public void onButtonPress(Button button) {
						updateModule.toggle();
					}

					@Override
					public void onRightButtonPress(Button button) {
						//
						if (!(updateModule.getModSettings() != null || updateModule.getModes().length > 1)) {
							displayKeyBindScreen(updateModule);
							return;
						}
						desplaySettingsScreen(updateModule, button);
					}
				});
				if (updateModule.getKeyboardKey() == Keyboard.KEY_NONE) {
					button.setText(updateModule.getName());
				} else {
					button.setText(
							updateModule.getName() + " [" + Keyboard.getKeyName(updateModule.getKeyboardKey()) + "]");
				}
				button.setModule(updateModule);
				frame.add(button, HorizontalGridConstraint.FILL);
			}

		}
		ModuleFrame bypassFrame;
		bypassFrame = new ModuleFrame("Bypasses");
		bypassFrame.setTheme(theme);
		bypassFrame.setLayoutManager(new GridLayoutManager(1, 0));
		bypassFrame.setVisible(true);
		bypassFrame.setClosable(false);
		bypassFrame.setMinimized(true);
		bypassFrame.setMinimizable(true);
		bypassFrame.setPinnable(false);
		addFrame(bypassFrame);
		
		for(final Bypass bypass : Jigsaw.getBypassManager().getBypasses()) {
			BasicButton btn = new BasicButton(bypass.getName()) {
				@Override
				public void update() {
					setBackgroundColor(bypass.isEnabled() ? ClientSettings.buttonSelectedColor
							: ClientSettings.guiBackgroundColor);
					setForegroundColor(
							bypass.isEnabled() ? new Color(1f, 1f, 1f, 1f) : new Color(0.8f, 0.8f, 0.8f, 1f));
					super.update();
				}
			};
			btn.addButtonListener(new ButtonListener() {
				
				@Override
				public void onRightButtonPress(Button button) {
					
				}
				
				@Override
				public void onButtonPress(Button button) {
					Jigsaw.getBypassManager().toggleBypass(button.getText());
				}
			});
			bypassFrame.add(btn, HorizontalGridConstraint.FILL);
		}
		
		
		// Settings frame
		Frame settingsFrame = null;
		for(Frame frame : frames) {
			if(frame.getTitle().equals("Settings")) {
				settingsFrame = frame;
			}
		}
		if(settingsFrame != null) {
			Slider smoothAimSpeedSlider = new BasicSlider("SmoothAim Smoothing", AuraUtils.getSmoothAimSpeed(), 1.5, 5.0,
					0.0, ValueDisplay.DECIMAL);
			smoothAimSpeedSlider.addSliderListener(new SliderListener() {

				@Override
				public void onSliderValueChanged(Slider slider) {

					AuraUtils.setSmoothAimSpeed(slider.getValue());

				}
			});
			settingsFrame.add(smoothAimSpeedSlider, HorizontalGridConstraint.FILL);

			// Smooth aim
			final CheckButton smoothAimCB = new BasicCheckButton("Smooth Aim");
			smoothAimCB.addButtonListener(new ButtonListener() {

				@Override
				public void onButtonPress(Button button) {
					AuraUtils.setSmoothAim(smoothAimCB.isSelected());
				}

				@Override
				public void onRightButtonPress(Button button) {

				}

			});
			smoothAimCB.setSelected(AuraUtils.getSmoothAim());
			settingsFrame.add(smoothAimCB, HorizontalGridConstraint.FILL);
		}

		// smoothaimspeedslider

		

		// Optional equal sizing and auto-positioning
		resizeComponents();
		Minecraft minecraft = Minecraft.getMinecraft();
		Dimension maxSize = recalculateSizes();
		maxSize.width = 100;
		int offsetX = 5, offsetY = 5;
		int scale = minecraft.gameSettings.guiScale;
		if (scale == 0)
			scale = 1000;
		int scaleFactor = 0;
		while (scaleFactor < scale && minecraft.displayWidth / (scaleFactor + 1) >= 320
				&& minecraft.displayHeight / (scaleFactor + 1) >= 240)
			scaleFactor++;
		for (Frame frame : getFrames()) {
			frame.setX(offsetX);
			frame.setY(offsetY);
			offsetX += maxSize.width + 5;
			if (offsetX + maxSize.width + 5 > minecraft.displayWidth / scaleFactor) {
				offsetX = 5;
				offsetY += maxSize.height + 5;
			}
		}

	}

	@Override
	protected void resizeComponents() {
		Theme theme = getTheme();
		Frame[] frames = getFrames();
		Button enable = new BasicButton("Enable");
		Button disable = new BasicButton("Disable");
		Dimension enableSize = theme.getUIForComponent(enable).getDefaultSize(enable);
		Dimension disableSize = theme.getUIForComponent(disable).getDefaultSize(disable);
		int buttonWidth = Math.max(enableSize.width, disableSize.width);
		int buttonHeight = Math.max(enableSize.height, disableSize.height);
		for (Frame frame : frames) {
			if (frame instanceof ModuleFrame) {
				for (Component component : frame.getChildren()) {
					if (component instanceof Button) {
						component.setWidth(buttonWidth);
						component.setHeight(buttonHeight);
					}
				}
			}
		}
		recalculateSizes();
	}

	public Dimension recalculateSizes() {
		Frame[] frames = getFrames();
		int maxWidth = 0, maxHeight = 0;
		for (Frame frame : frames) {
			Dimension dim = frame.getTheme().getUIForComponent(frame).getDefaultSize(frame);
			frame.setWidth(dim.width);
			frame.setHeight(dim.height);
			maxWidth = Math.max(maxWidth, dim.width);
		}
		for (Frame frame : frames) {
			float maxTitleWidth = 0;

			maxTitleWidth = Math.max(maxTitleWidth,
					((SimpleTheme) frame.getTheme()).getFontRenderer().getStringWidth(frame.getTitle() + "++++"));

			for (Component child : frame.getChildren()) {
				if (!(child instanceof BasicButton)) {
					continue;
				}
				BasicButton button = (BasicButton) child;
				System.out.println(button.getText() + " : "
						+ ((SimpleTheme) button.getTheme()).getFontRenderer().getStringWidth(button.getText() + "+++"));
				maxTitleWidth = Math.max(maxTitleWidth,
						((SimpleTheme) button.getTheme()).getFontRenderer().getStringWidth(button.getText() + "+++"));
			}
			if (frame.getTitle().equalsIgnoreCase("settings")) {
				frame.setWidth(maxWidth);
			} else {
				frame.setWidth(Math.round(maxTitleWidth));
			}
			frame.layoutChildren();
		}
		return new Dimension(maxWidth, maxHeight);
	}

	public void displayKeyBindScreen(Module updateModule) {
		Minecraft.getMinecraft()
				.displayGuiScreen(new GuiJigsawKeyBind(updateModule, Minecraft.getMinecraft().currentScreen));
	}
	
	public void reloadSettingScreens() {
		for(Module module : Jigsaw.getModules()) {
			for (Frame frame : getFrames()) {
				if (frame instanceof ModuleFrame) {
					ModuleFrame modFrame = (ModuleFrame) frame;
					if (modFrame.module != null && modFrame.module.getName().equals(module.getName())
							&& frame.isVisible()) {
						frame.close();
						module.settingsDisplayed = false;
						return;
					}
				}

			}
		}
	}

	public void desplaySettingsScreen(final Module updateModule, Component sender) {
		for (Frame frame : getFrames()) {
			if (frame instanceof ModuleFrame) {
				ModuleFrame modFrame = (ModuleFrame) frame;
				if (modFrame.module != null && modFrame.module.getName().equals(updateModule.getName())
						&& frame.isVisible()) {
					frame.close();
					updateModule.settingsDisplayed = false;
					return;
				}
			}

		}
		ComboBox comboBox = null;
		if (updateModule.getModes().length > 1) {
			comboBox = new BasicComboBox(updateModule.getModes()) {
				@Override
				public void update() {
					if (isSelected()) {
						Component[] children = getParent().getChildren();
						setBackgroundColor(updateModule.isToggled() ? ClientSettings.buttonSelectedColor
								: ClientSettings.guiBackgroundColor);
						if (!children[children.length - 1].equals(this)) {

						}
					} else {
						setBackgroundColor(updateModule.isToggled() ? ClientSettings.buttonSelectedColor
								: ClientSettings.guiBackgroundColor);

					}

				}
			};
			comboBox.addComboBoxListener(new ComboBoxListener() {

				@Override
				public void onComboBoxSelectionChanged(ComboBox comboBox) {
					try {
						updateModule.setMode(comboBox.getSelectedElement());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			for (int i = 0; i < comboBox.getElements().length; i++) {
				if (updateModule.getCurrentMode().equals(comboBox.getElements()[i])) {
					comboBox.setSelectedIndex(i);
					break;
				}
			}
		}
		if (!(updateModule.getModSettings() != null || updateModule.getModes().length > 1)) {
			return;
		}
		ModuleFrame frame = new ModuleFrame(updateModule);
		frame.setTheme(theme);
		frame.setTitle(updateModule.getName());
		frame.setLayoutManager(new GridLayoutManager(1, 0));
		frame.setVisible(true);
		frame.setClosable(true);
		frame.setMinimized(false);
		frame.setMinimizable(false);
		frame.setPinnable(false);
		addFrame(frame);
		frame.setX(sender.getX() + sender.getParent().getX() + sender.getWidth() + 1);
		frame.setY(sender.getY() + sender.getParent().getY());
		BasicButton keyBindBtn = new BasicButton("Change Keybind");
		keyBindBtn.addButtonListener(new ButtonListener() {

			@Override
			public void onRightButtonPress(Button button) {

			}

			@Override
			public void onButtonPress(Button button) {
				displayKeyBindScreen(updateModule);
			}
		});
		frame.add(keyBindBtn, HorizontalGridConstraint.CENTER);
		if (updateModule.getModSettings() != null) {
			frame.add(new BasicLabel("Settings:"), HorizontalGridConstraint.CENTER);
			for (Component comp : updateModule.getModSettings()) {
				frame.add(comp, HorizontalGridConstraint.FILL);
			}
		}
		if (comboBox != null) {
			frame.add(new BasicLabel("Modes:"), HorizontalGridConstraint.CENTER);
			frame.add(comboBox, HorizontalGridConstraint.FILL);
			comboBox.setSelected(true);
		}

		int maxWidth = 0, maxHeight = 0;
		float maxTitleWidth = 0;

		maxTitleWidth = Math.max(maxTitleWidth,
				((SimpleTheme) frame.getTheme()).getFontRenderer().getStringWidth(frame.getTitle() + "++++"));

		for (Component child : frame.getChildren()) {
			if (!(child instanceof BasicButton)) {
				continue;
			}
			BasicButton button = (BasicButton) child;
			System.out.println(button.getText() + " : "
					+ ((SimpleTheme) button.getTheme()).getFontRenderer().getStringWidth(button.getText() + "+++"));
			maxTitleWidth = Math.max(maxTitleWidth,
					((SimpleTheme) button.getTheme()).getFontRenderer().getStringWidth(button.getText() + "+++"));
		}
		if (frame.getTitle().equalsIgnoreCase("settings")) {
			frame.setWidth(maxWidth);
		} else {
			frame.setWidth(Math.round(maxTitleWidth));
		}
		frame.resize();
		frame.layoutChildren();
		updateModule.settingsDisplayed = true;
	}
	public void reload() {
		Jigsaw.getFileMananger().saveGUI(Jigsaw.getGUIMananger().getFrames());
		Jigsaw.getGUIMananger().setup();
		Jigsaw.getFileMananger().loadGUI(Jigsaw.getGUIMananger().getFrames());
		Jigsaw.getNotificationManager().addNotification(new Notification(Level.INFO, "GuiManager reloaded!"));
	}
}
