package cc.novoline.gui.screen.dropdown.config;

import cc.novoline.Novoline;
import cc.novoline.commands.impl.ConfigCommand;
import cc.novoline.modules.visual.HUD;
import cc.novoline.utils.OpenGLUtil;
import cc.novoline.utils.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.MathHelper;

import java.awt.*;

import static cc.novoline.utils.fonts.impl.Fonts.SF.SF_18.SF_18;

public class Config {

	public int y;

	public final static Color WHITE = new Color(255, 255, 255, 255);
	public final static Color BACKGROUND_130 = new Color(0, 0, 0, 130);
	public final static Color BACKGROUND_120 = new Color(0, 0, 0, 120);

	private final String name;
	private final ConfigTab parent;
	private float fraction = 0;
	private Timer lastClick = new Timer();

	public Config(String name, ConfigTab parent) {
		this.name = name;
		this.parent = parent;
	}

	public void drawScreen(int mouseX, int mouseY) {
		y = (int) (parent.getPosY() + 15);

		boolean isSelected = parent.getSelectedConfig() == this;

		HUD hud = Novoline.getInstance().getModuleManager().getModule(HUD.class);

		for (Config config : parent.getConfigs()) {
			if (config == this) {
				break;
			} else {
				y += config.getYPerConfig();
			}
		}

		if(isSelected && fraction < 1){
			fraction+=0.0025 * (2000 / Minecraft.getInstance().getDebugFPS());
		}
		if(!isSelected && fraction > 0){
			fraction-=0.0025 * (2000 / Minecraft.getInstance().getDebugFPS());
		}
		fraction = MathHelper.clamp_float(fraction,0,1);

		Gui.drawRect(parent.getPosX(), y,parent.getPosX() + 100, y + getYPerConfig(), new Color(40, 40, 40, 255).getRGB());
		SF_18.drawCenteredString(name, parent.getPosX() + 101 / 2, y + 4, OpenGLUtil.interpolateColor(WHITE,new Color(hud.getColor().getRed(), hud.getColor().getGreen(), hud.getColor().getBlue(), 250),fraction), true);
	}

	public void keyTyped(char typedChar, int keyCode) {

	}

	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if(isHovered(mouseX, mouseY) && mouseButton == 0) {
			parent.setSelectedConfig(this);

			if(!lastClick.check(200)) {
				ConfigCommand.loadConfig(Novoline.getInstance().getModuleManager().getConfigManager(), getName());
			}

			lastClick.reset();
		}
	}

	public boolean isHovered(int mouseX, int mouseY) {
		y = (int) (parent.getPosY() + 15);

		for (Config tabModule : parent.getConfigs()) {
			if (tabModule == this) {
				break;
			} else {
				y += tabModule.getYPerConfig();
			}
		}

		return mouseX >= parent.getPosX() && mouseY >= y && mouseX <= parent.getPosX() + 101 && mouseY <= y + getYPerConfig();
	}

	public int getYPerConfig() {
		return 14;
	}

	public String getName() {
		return name;
	}

	public ConfigTab getParent() {
		return parent;
	}
	
}
