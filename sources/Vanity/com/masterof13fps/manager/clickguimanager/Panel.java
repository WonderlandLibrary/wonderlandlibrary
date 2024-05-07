package com.masterof13fps.manager.clickguimanager;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.masterof13fps.Client;
import com.masterof13fps.features.modules.Category;
import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.impl.gui.ClickGUI;
import com.masterof13fps.manager.clickguimanager.components.Frame;
import com.masterof13fps.manager.clickguimanager.components.GuiButton;
import com.masterof13fps.manager.clickguimanager.components.GuiFrame;
import com.masterof13fps.manager.clickguimanager.listeners.ClickListener;
import com.masterof13fps.manager.clickguimanager.listeners.ComponentsListener;
import com.masterof13fps.manager.clickguimanager.util.FramePosition;
import com.masterof13fps.manager.fontmanager.UnicodeFontRenderer;
import com.masterof13fps.utils.FileUtils;

/**
 * @author sendQueue <Vinii>
 *
 *         Further info at Vinii.de or github@vinii.de, file created at
 *         11.11.2020. Use is only authorized if given credit!
 * 
 *         Some renderstuff requires https://github.com/sendQueue/LWJGLUtil
 * 
 */

/**
 * Recoded by CrazyMemeCoke, 20-12-15
 * -> added GUI saving
 * -> optimized code
 */
public class Panel extends ClickGui {
	public static UnicodeFontRenderer fR = Client.main().fontMgr().font("Arial", 18, Font.PLAIN);

	public static String theme;

	public static int FRAME_WIDTH = 100;

	private File guiFile;

	public static int color = new Color(193, 105, 170, 220).getRGB();
	public static int fontColor = Color.white.getRGB();
	public static int grey40_240 = new Color(40, 40, 40, 140).getRGB();
	public static int black195 = new Color(0, 0, 0, 195).getRGB();
	public static int black100 = new Color(0, 0, 0, 100).getRGB();

	/**
	 * Initializes Panel
	 * 
	 * @param theme
	 * @param fontSize
	 */
	public Panel(String theme, int fontSize) {
		Panel.theme = theme;
		fR = Client.main().fontMgr().font("BigNoodleTitling", fontSize, Font.PLAIN);
	}

	@Override
	public void initGui() {
		int x = 25, y = 25;

		try {
			guiFile = new File(Client.main().getClientDir() + "/gui.txt");
			if (guiFile.createNewFile()) {
				notify.debug("File created: " + guiFile.getName());
			} else {
				notify.debug("File \"gui.txt\" already exists.");
			}
		} catch (IOException e) {
			notify.debug("An error occurred.");
			e.printStackTrace();
		}

		List<String> lines = FileUtils.loadFile(guiFile);
		HashMap<Category, String> guiLocations = new HashMap<>();

		assert lines != null;
		lines.forEach(line -> {
			String[] data = line.split("_");

			guiLocations.put(Category.valueOf(data[0]),
					data[1] + ":" + data[2] + ":" + (data.length == 4 ? data[3] : "false"));
		});

		// loading values
		for (Category c : Category.values()) {
			String title = Character.toUpperCase(c.name().toLowerCase().charAt(0))
					+ c.name().toLowerCase().substring(1);
			boolean expanded = false;
			GuiFrame frame;
			/**
			 * arg 0 - x
			 * arg 1 - y
			 * arg 2 - expanded?
			 * title - name
			 */
			// load frame positions
			if (guiLocations.containsKey(c)) {
				// if frame is existing in config file
				String positions = guiLocations.get(c);
				String[] options = positions.split(":");
				if (options.length == 3) {
					expanded = Boolean.parseBoolean(options[2]);
				}

				x = (int) Double.parseDouble(options[0]);
				y = (int) Double.parseDouble(options[1]);

				frame = new GuiFrame(title, x, y, expanded);
			} else {
				// if frame is not existing in config file
				frame = new GuiFrame(title, x, 50, true);
			}
			for (Module m : getModuleManager().modules) {
				if (c == m.category()) {
					GuiButton button = new GuiButton(m.name());
					button.addClickListener(new ClickListener(button));
					button.addExtendListener(new ComponentsListener(button));
					frame.addButton(button);
				}
			}
			addFrame(frame);
			x += 110;
		}

		Client.main().setMgr().loadSettings();

		super.initGui();
	}

	public void onGuiClosed() {
		if (mc.entityRenderer.theShaderGroup != null) {
			mc.entityRenderer.theShaderGroup.deleteShaderGroup();
			mc.entityRenderer.theShaderGroup = null;
		}

		List<String> lines = new ArrayList<>();

		for(Frame frame : getFrames()){
			GuiFrame guiFrame = (GuiFrame) frame;

			lines.add(guiFrame.getTitle().toUpperCase() + "_" + guiFrame.getPosX() + "_" + guiFrame.getPosY() + "_" + guiFrame.isExpaned());
		}

		FileUtils.saveFile(guiFile, lines);

		Client.main().modMgr().getModule(ClickGUI.class).onDisable();

		Client.main().setMgr().saveSettings();
	}
}
