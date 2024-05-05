package sudo.ui.screens.clickgui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

//import me.surge.animation.ColourAnimation;
//import me.surge.animation.Easing;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import sudo.module.Mod;
import sudo.module.ModuleManager;
import sudo.module.client.ClickGuiMod;
import sudo.module.settings.*;
import sudo.ui.screens.clickgui.setting.*;
import sudo.utils.surge.animation.ColorAnimation;
import sudo.utils.surge.animation.Easing;
import sudo.utils.text.GlyphPageFontRenderer;
import sudo.utils.text.IFont;

public class ModuleButton {
	private static MinecraftClient mc = MinecraftClient.getInstance();
	private ColorAnimation hoverAnim = new ColorAnimation(new Color(0xff2A2A2A), new Color(0xff1c1c1c), 300F, false, Easing.LINEAR);
//	private ColorAnimation enabledAnim = new ColorAnimation(new Color(0xff545454), new Color(0xff9D73E6), 100F, false, Easing.LINEAR);
//	private ColorAnimation textEnabledAnim = new ColorAnimation(Color.WHITE, new Color(0xff9D73E6), 100F, false, Easing.LINEAR);
	GlyphPageFontRenderer textRend = IFont.CONSOLAS;
	
	float speed;
	public Mod module;
	public Frame parent;
	public int offset;
	public List<Component> components;
	public boolean extended;
	public float extraOffset;
	public float colorOffset;
	
	public ModuleButton(Mod module, Frame parent, int offset) {
		this.module = module;
		this.parent = parent;
		this.offset = offset;
		this.extended = false;
		this.components = new ArrayList<>();
		
		int setOffset = parent.height;
		for (Setting setting : module.getSetting()) {

			if (setting instanceof BooleanSetting) {
				components.add(new CheckBox(setting, this, setOffset));
			} else if (setting instanceof ModeSetting) {
				components.add(new ModeBox(setting, this, setOffset));
			} else if (setting instanceof NumberSetting) {
				components.add(new Slider(setting, this, setOffset));
			} else if (setting instanceof KeybindSetting) {
				components.add(new Keybind(setting, this, setOffset));
			} else if (setting instanceof ColorSetting) {
				components.add(new ColorBox(setting, this, setOffset));
			}
			setOffset += parent.height;
		}
	}
	
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		hoverAnim.setState(isHovered(mouseX,mouseY));
//		enabledAnim.setState(module.isEnabled());
//		textEnabledAnim.setState(module.isEnabled());
		DrawableHelper.fill(matrices, parent.x, parent.y + offset, parent.x + parent.width, parent.y+offset+parent.height, 0xff2A2A2A);
		DrawableHelper.fill(matrices, parent.x, parent.y + offset, parent.x + parent.width, parent.y+offset+parent.height, hoverAnim.getColor().getRGB());
		DrawableHelper.fill(matrices, parent.x + parent.width - 5, parent.y + offset+2, parent.x + parent.width-2, parent.y+offset+parent.height-2, module.isEnabled() ? ModuleManager.INSTANCE.getModule(ClickGuiMod.class).primaryColor.getColor().getRGB() : 0xff545454);
		
		textRend.drawString(matrices, module.getName(), parent.x + 2 + speed, parent.y+(parent.height/2)-(mc.textRenderer.fontHeight/2) + offset+1-3.5, module.isEnabled() ? ModuleManager.INSTANCE.getModule(ClickGuiMod.class).primaryColor.getColor().getRGB() : -1, 1);

		if (isHovered(mouseX, mouseY)) {
			speed+=0.3;
		} else {
			if (speed>0)speed-=0.3;
		}
		if (speed>2.0)speed=2;
		
		if (extended) {
			for (Component component : components) {
				parent.updateButton();
				component.render(matrices, mouseX, mouseY, delta);
				parent.updateButton();
			}
		}
		
		if (ModuleManager.INSTANCE.getModule(ClickGuiMod.class).description.isEnabled() && isHovered(mouseX, mouseY)) {
			DrawableHelper.fill(matrices, mc.getWindow().getScaledWidth()-textRend.getStringWidth(module.getDescription())-7, mc.getWindow().getScaledHeight()-textRend.getFontHeight()-2, mc.getWindow().getScaledWidth()-textRend.getStringWidth(module.getDescription())-5, mc.getWindow().getScaledHeight()-1, 
					ModuleManager.INSTANCE.getModule(ClickGuiMod.class).primaryColor.getColor().getRGB());
			DrawableHelper.fill(matrices, mc.getWindow().getScaledWidth()-textRend.getStringWidth(module.getDescription())-5, mc.getWindow().getScaledHeight()-textRend.getFontHeight()-2, mc.getWindow().getScaledWidth()-1, mc.getWindow().getScaledHeight()-1, 
					0xff1f1f1f);
			textRend.drawString(matrices, module.getDescription(), mc.getWindow().getScaledWidth()-textRend.getStringWidth(module.getDescription())-4, mc.getWindow().getScaledHeight()-textRend.getFontHeight()-2, -1, 1);
		}
	}
	
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if(isHovered(mouseX, mouseY)) {
			if (button == 0) {
				module.toggle();
			} else if (button == 1) {
				extended = !extended;
				parent.updateButton();
			}
		}
		if (extended) {
			for (Component component : components) {
				component.mouseClicked(mouseX, mouseY, button);
				parent.updateButton();
			}
		}
	}
	
	public void mouseReleased(double mouseX, double mouseY, int button) {
		for (Component component : components) {
			component.mouseReleased(mouseX, mouseY, button);
		}
	}
	
	public boolean isHovered(double mouseX, double mouseY) {
		return mouseX > parent.x && mouseX < parent.x + parent.width && mouseY > parent.y + offset && mouseY < parent.y + offset + parent.height;
	}

	public void keyPressed(int key) {
        for (Component component : components) {
            component.keyPressed(key);
        }
    }
}
