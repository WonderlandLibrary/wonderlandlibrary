package Reality.Realii.guis.clickgui.component.components;
import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import Reality.Realii.Client;
import Reality.Realii.event.value.Mode;
import Reality.Realii.event.value.Numbers;
import Reality.Realii.event.value.Option;
import Reality.Realii.event.value.Value;
import Reality.Realii.guis.clickgui.ClickGuisex;
import Reality.Realii.guis.clickgui.component.Component;
import Reality.Realii.guis.clickgui.component.Frame;
import Reality.Realii.guis.clickgui.component.components.sub.Checkbox;
import Reality.Realii.guis.clickgui.component.components.sub.Keybind;
import Reality.Realii.guis.clickgui.component.components.sub.ModeButton;
import Reality.Realii.guis.clickgui.component.components.sub.Slider;

import Reality.Realii.mods.Module;
import Reality.Realii.mods.modules.ClientSettings;
import Reality.Realii.utils.cheats.RenderUtills.Pair;
import Reality.Realii.utils.cheats.RenderUtills.Render2;

public class Button extends Component {

	public Module mod;
	public Frame parent;
	public int offset;
	private boolean isHovered;
	private ArrayList<Component> subcomponents;
	public boolean open;
	private int height;
	
	public Button(Module mod, Frame parent, int offset) {
		this.mod = mod;
		this.parent = parent;
		this.offset = offset;
		this.subcomponents = new ArrayList<Component>();
		this.open = false;
		height = 12;
		int opY = offset + 12;
		
		if(mod.getValues() != null) {
			for(Value s : mod.getValues()){
				   if (s instanceof Option) {
					   this.subcomponents.add(new Checkbox(s, this, opY));
					
					opY += 12;
				}
				   if (s instanceof Numbers) {
					this.subcomponents.add(new Slider(s, this, opY));
					opY += 12;
				}
				   if (s instanceof Mode) {
					   this.subcomponents.add(new ModeButton(s, this, mod, opY));
					opY += 12;
				}
			}
		}
		this.subcomponents.add(new Keybind(this, opY));
		//this.subcomponents.add(new VisibleButton(this, mod, opY));
	}
	
	@Override
	public void setOff(int newOff) {
		offset = newOff;
		int opY = offset + 12;
		for(Component comp : this.subcomponents) {
			comp.setOff(opY);
			opY += 12;
		}
	}
	
	@Override
	public void renderComponent() {
		int i = 0;
		Color startColor = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue(),200);
		Color endColor = new Color(ClientSettings.r2.getValue().intValue(), ClientSettings.g2.getValue().intValue(), ClientSettings.b2.getValue().intValue(),200);
		Pair<Color, Color> colors = Pair.of(startColor, endColor);
		Color c = Render2.interpolateColorsBackAndForth(7, 3 + (i * 20), colors.getFirst(), colors.getSecond(), false);
		Gui.drawRect(parent.getX(), this.parent.getY() + this.offset, parent.getX() + parent.getWidth(), this.parent.getY() + 12 + this.offset, this.isHovered ? (this.mod.isEnabled() ? c.darker().getRGB() : 0xFF222222) : (this.mod.isEnabled() ?   c.getRGB() : new Color(15,15,15).getRGB()));
		GL11.glPushMatrix();
		GL11.glScalef(0.5f,0.5f, 0.5f);
		Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(this.mod.getName(), (parent.getX() + 2) * 2, (parent.getY() + offset + 2) * 2 + 4, this.mod.isEnabled() ? 0x999999 : -1);
		if(this.subcomponents.size() > 2)
		Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(this.open ? "-" : "+", (parent.getX() + parent.getWidth() - 10) * 2, (parent.getY() + offset + 2) * 2 + 4, -1);
		GL11.glPopMatrix();
		if(this.open) {
			if(!this.subcomponents.isEmpty()) {
				for(Component comp : this.subcomponents) {
					comp.renderComponent();
				}

				//Gui.drawRect(parent.getX() + 2, parent.getY() + this.offset + 12, parent.getX() + 3, parent.getY() + this.offset + ((this.subcomponents.size() + 1) * 12), ClickGuisex.color);
			}
		}
	}
	
	@Override
	public int getHeight() {
		if(this.open) {
			return (12 * (this.subcomponents.size() + 1));
		}
		return 12;
	}
	
	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.isHovered = isMouseOnButton(mouseX, mouseY);
		if(!this.subcomponents.isEmpty()) {
			for(Component comp : this.subcomponents) {
				comp.updateComponent(mouseX, mouseY);
			}
		}
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(isMouseOnButton(mouseX, mouseY) && button == 0) {
			if(!mod.isEnabled()) {
			this.mod.setEnabled(true);
			} else {
				mod.setEnabled(false);
			}
		} 
		if(isMouseOnButton(mouseX, mouseY) && button == 1) {
			this.open = !this.open;
			this.parent.refresh();
			
		}
		for(Component comp : this.subcomponents) {
			comp.mouseClicked(mouseX, mouseY, button);
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		for(Component comp : this.subcomponents) {
			comp.mouseReleased(mouseX, mouseY, mouseButton);
		}
	}
	
	@Override
	public void keyTyped(char typedChar, int key) {
		for(Component comp : this.subcomponents) {
			comp.keyTyped(typedChar, key);
		}
	}
	
	public boolean isMouseOnButton(int x, int y) {
		if(x > parent.getX() && x < parent.getX() + parent.getWidth() && y > this.parent.getY() + this.offset && y < this.parent.getY() + 12 + this.offset) {
			return true;
		}
		return false;
	}
}
