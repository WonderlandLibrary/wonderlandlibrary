package Reality.Realii.guis.clickgui.component;

import java.awt.*;
import java.util.ArrayList;

import Reality.Realii.mods.modules.ClientSettings;
import Reality.Realii.mods.modules.render.ClickGui;
import Reality.Realii.utils.cheats.RenderUtills.Pair;
import Reality.Realii.utils.cheats.RenderUtills.Render2;
import Reality.Realii.utils.render.RenderUtil;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import Reality.Realii.Client;
//Your Imports
import Reality.Realii.guis.clickgui.ClickGuisex;
import Reality.Realii.guis.clickgui.component.components.Button;
import Reality.Realii.managers.ModuleManager;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.mods.Module;
public class Frame {

	public ArrayList<Component> components;
	public ModuleType category;
	private boolean open;
	private int width;
	private int y;
	private int x;
	private int barHeight;
	private boolean isDragging;
	public int dragX;
	public int dragY;

	public Frame(ModuleType cat) {
		this.components = new ArrayList<Component>();
		this.category = cat;
		this.width = 88;
		this.x = 5;
		this.y = 5;
		this.barHeight = 13;
		this.dragX = 0;
		this.open = true;
		//or false
		this.isDragging = false;
		int tY = this.barHeight;


	//	  for (Module m : ModuleManager.modules) {
		  for(Module mod : Client.instance.getModuleManager().getModulesInType(category)) {
         //     if (m.getType() != category) {
            	  Button modButton = new Button(mod, this, tY);
      			this.components.add(modButton);
      			tY += 12;
             // }
		  }






		}


	public ArrayList<Component> getComponents() {
		return components;
	}

	public void setX(int newX) {
		this.x = newX;
	}

	public void setY(int newY) {
		this.y = newY;
	}

	public void setDrag(boolean drag) {
		this.isDragging = drag;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public void renderFrame(FontRenderer fontRenderer) {
		if (ClickGui.LexiMode.getValue().equals("Reality")) {
			RenderUtil.drawCustomImageAlpha(760, 235, 200, 300, new ResourceLocation("client/AnimeGirluwu/Reality.png"), new Color(255, 255, 255).getRGB(), 300);
		}

		else  if (ClickGui.LexiMode.getValue().equals("Astolfo")) {
			RenderUtil.drawCustomImageAlpha(760, 235, 200, 300, new ResourceLocation("client/AnimeGirluwu/asstolfo.png"), new Color(255, 255, 255).getRGB(), 300);
		}

		else if (ClickGui.LexiMode.getValue().equals("Astolfo2")) {
			RenderUtil.drawCustomImageAlpha(760, 235, 200, 300, new ResourceLocation("client/AnimeGirluwu/astoflo4.png"), new Color(255, 255, 255).getRGB(), 300);
		}

		else if (ClickGui.LexiMode.getValue().equals("Astolfo3")) {
			RenderUtil.drawCustomImageAlpha(760, 235, 200, 300, new ResourceLocation("client/AnimeGirluwu/astolfo4.png"), new Color(255, 255, 255).getRGB(), 300);
		}
		else  if (ClickGui.LexiMode.getValue().equals("Astolfo4")) {
			RenderUtil.drawCustomImageAlpha(690, 300, 300, 200, new ResourceLocation("client/AnimeGirluwu/astolfo5.png"), new Color(255, 255, 255).getRGB(), 300);
		}
		else   if (ClickGui.LexiMode.getValue().equals("Ihassesdich")) {
			RenderUtil.drawCustomImageAlpha(760, 235, 200, 300, new ResourceLocation("client/AnimeGirluwu/catgirl.png"), new Color(255, 255, 255).getRGB(), 300);
		}
		else if (ClickGui.LexiMode.getValue().equals("NotHot")) {
			RenderUtil.drawCustomImageAlpha(760, 235, 200, 300, new ResourceLocation("client/AnimeGirluwu/felix.png"), new Color(255, 255, 255).getRGB(), 300);
		}
		else if (ClickGui.LexiMode.getValue().equals("Idk")) {
			RenderUtil.drawCustomImageAlpha(760, 235, 200, 300, new ResourceLocation("client/AnimeGirluwu/idk.png"), new Color(255, 255, 255).getRGB(), 300);
		}
		else  if (ClickGui.LexiMode.getValue().equals("Hideri")) {
			RenderUtil.drawCustomImageAlpha(760, 235, 200, 300, new ResourceLocation("client/AnimeGirluwu/hideri.png"), new Color(255, 255, 255).getRGB(), 300);
		}

		int i = 0;
		Color startColor = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue());
		Color endColor = new Color(ClientSettings.r2.getValue().intValue(), ClientSettings.g2.getValue().intValue(), ClientSettings.b2.getValue().intValue());
		Pair<Color, Color> colors = Pair.of(startColor, endColor);
		Color c = Render2.interpolateColorsBackAndForth(7, 3 + (i * 20), colors.getFirst(), colors.getSecond(), false);
		RenderUtil.drawBorderedRect(this.x, this.y, this.x + this.width, this.y + this.barHeight,2, c.getRGB(), new Color(15,15,15).getRGB());
		GL11.glPushMatrix();
		GL11.glScalef(0.5f,0.5f, 0.5f);
		fontRenderer.drawStringWithShadow(this.category.name(), (this.x + 2) * 2 + 5, (this.y + 2.5f) * 2 + 5, 0xFFFFFFFF);
		fontRenderer.drawStringWithShadow(this.open ? "-" : "+", (this.x + this.width - 10) * 2 + 5, (this.y + 2.5f) * 2 + 5, -1);
		GL11.glPopMatrix();
		if(this.open) {
			if(!this.components.isEmpty()) {

				//Gui.drawRect(this.x, this.y + this.barHeight, this.x + 1, this.y + this.barHeight + (12 * components.size()), new Color(0, 200, 20, 150).getRGB());
				Gui.drawRect(this.x, this.y + this.barHeight + (12 * components.size()), this.x + this.width, this.y + this.barHeight + (12 * components.size()) + 1, c.getRGB());
				//Gui.drawRect(x, this.y + this.barHeight + (12 * components.size()), this.x + this.width, this.y + this.barHeight + (12 * components.size()) + 1, new Color(255, 200, 20, 150).getRGB());
				//Gui.drawRect(this.x + this.width, this.y + this.barHeight, this.x + this.width - 1, this.y + this.barHeight + (12 * components.size()), new Color(0, 200, 20, 150).getRGB());
				for(Component component : components) {
					component.renderComponent();
				}
			}
		}
	}

	public void refresh() {
		int off = this.barHeight;
		for(Component comp : components) {
			comp.setOff(off);
			off += comp.getHeight();
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public void updatePosition(int mouseX, int mouseY) {
		if(this.isDragging) {
			this.setX(mouseX - dragX);
			this.setY(mouseY - dragY);
		}
	}

	public boolean isWithinHeader(int x, int y) {
		if(x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.barHeight) {
			return true;
		}
		return false;
	}

}
