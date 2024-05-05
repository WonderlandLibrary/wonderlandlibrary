package xyz.cucumber.base.interf.DropdownClickGui.ext;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import xyz.cucumber.base.Client;
import xyz.cucumber.base.interf.DropdownClickGui.ext.Settings.DropdownClickGuiSettings;
import xyz.cucumber.base.interf.clickgui.content.ex.ConfigButton;
import xyz.cucumber.base.interf.clickgui.content.ex.ModuleButton;
import xyz.cucumber.base.interf.clickgui.navbar.ex.CategoryButton;
import xyz.cucumber.base.interf.clickgui.navbar.ex.ClientButtons;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.Fonts;

public class DropdownCategory implements DropdownButton{

	private Category category;
	public PositionUtils position = new PositionUtils(0,0,110,0,1);
	
	private double dragX,dragY;
	private boolean dragging;
	
	private double scrollY, temp;
	
	private ArrayList<DropdownButton> buttons = new ArrayList();
	
	private boolean open = true;
	
	private double maxHeight = 200;
	
	public DropdownCategory(Category category) {
		this.category = category;
		double h = 0;
		for(Mod m : Client.INSTANCE.getModuleManager().getModulesByCategory(category)) {
			DropdownModule button = new DropdownModule(m);
			button.getPosition().setX(this.position.getX()+5);
			button.getPosition().setY(this.position.getY()+20+h-scrollY);
			h+= button.getPosition().getHeight()+2;
			buttons.add(button);
		}
	}
	
	@Override
	public void draw(int mouseX, int mouseY) {
		if(this.position.isInside(mouseX, mouseY)) {
			double save = position.getHeight() - 20;
			if (save < getBigger()) {
				float g = Mouse.getEventDWheel();
				double maxScrollY = getBigger() - save;
				double size = Mouse.getDWheel() / 60;
				if (size != 0) {
					temp += size;
				}
				if (Math.round(temp) != 0) {
					temp = ((temp * (10 - 1)) / (10));
					double l = scrollY;
					scrollY = scrollY - temp;
					if (scrollY < 0) {
						scrollY = 0;
					} else if (scrollY > maxScrollY) {
						scrollY = maxScrollY;
					}
				} else {
					temp = 0;
				}
			} else {
				scrollY = 0;
			}
		}
		if(dragging) {
			this.position.setX(mouseX-dragX);
			this.position.setY(mouseY-dragY);
		}
		this.position.setHeight(getModHeight());
		RenderUtils.drawRoundedRectWithCorners(position.getX(), position.getY(), position.getX2(), position.getY2(), 0xee181818, 4, true,true,true,true);
		RenderUtils.drawImage(position.getX()+3, position.getY()+3, 15, 15, new ResourceLocation("client/images/"+ category.name().toLowerCase()+".png"), -1);
		Fonts.getFont("rb-b").drawString(category.name(), position.getX()+position.getWidth()/2-Fonts.getFont("rb-b").getWidth(category.name())/2, position.getY()+8, -1);
		double h = 0;
		if(open) {
			RenderUtils.enableScisor();
			RenderUtils.scissor(new ScaledResolution(Minecraft.getMinecraft()),position.getX()+5, position.getY()+20,100 , getModHeight()-20);
			for(DropdownButton b : buttons) {
				DropdownModule button = (DropdownModule) b;
				button.getPosition().setX(this.position.getX()+5);
				button.getPosition().setY(this.position.getY()+20+h-scrollY);
				h+= button.getPosition().getHeight()+2;
				button.draw(mouseX, mouseY);
			}
			
			RenderUtils.disableScisor();
		}

	}
	public double getBigger() {
		double h = 0;
		for(DropdownButton b : buttons) {
			DropdownModule button = (DropdownModule) b;
			
			h+= button.getPosition().getHeight()+2;
		}
		return h;
		
	}
	public double getModHeight() {
		if(open) {
			double h = 0;
			for(DropdownButton b : buttons) {
				DropdownModule button = (DropdownModule) b;
				
				h+= button.getPosition().getHeight()+2;
			}
			if(h >= 178) {
				return maxHeight;
			}
			return h+20+2;
		}
		
		return 20;
	}

	@Override
	public void onClick(int mouseX, int mouseY, int button) {
		if(this.position.isInside(mouseX, mouseY)) {
			if(mouseX > position.getX() && position.getX2() > mouseX && mouseY > position.getY()+15 && mouseY<position.getY2()) {

				for(DropdownButton b : buttons) {
					DropdownModule but = (DropdownModule) b;
					if(but.getPosition().isInside(mouseX, mouseY)) {
						but.onClick(mouseX, mouseY, button);
						return;
					}
				}
				
			}
			
			if(button == 0) {
				dragging = true;
				dragX = mouseX-this.position.getX();
				dragY = mouseY-this.position.getY();
			}
			if(button == 1) {
				open = !open;
			}
		}
	}

	@Override
	public void onRelease(int mouseX, int mouseY, int button) {
		dragging = false;
		for(DropdownButton b : buttons) {
			DropdownModule but = (DropdownModule) b;
			but.onRelease(mouseX, mouseY, button);
		}
	}

	@Override
	public void onKey(char chr, int key) {
		for(DropdownButton b : buttons) {
			DropdownModule but = (DropdownModule) b;
			but.onKey(chr, key);
		}
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public double getMaxHeight() {
		return maxHeight;
	}

	public void setMaxHeight(double maxHeight) {
		this.maxHeight = maxHeight;
	}
	
	public PositionUtils getPosition() {
		return this.position;
	}

}
