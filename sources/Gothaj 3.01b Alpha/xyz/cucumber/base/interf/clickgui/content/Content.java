package xyz.cucumber.base.interf.clickgui.content;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.google.gson.JsonObject;

import xyz.cucumber.base.interf.clickgui.content.ex.ConfigButton;
import xyz.cucumber.base.interf.clickgui.content.ex.ModuleButton;
import xyz.cucumber.base.interf.clickgui.navbar.Navbar;
import xyz.cucumber.base.interf.clickgui.navbar.ex.CategoryButton;
import xyz.cucumber.base.interf.clickgui.navbar.ex.ClientButtons;
import xyz.cucumber.base.interf.clickgui.navbar.ex.NavbarButtons;
import xyz.cucumber.base.utils.FileUtils;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.Fonts;
import xyz.cucumber.base.utils.render.StencilUtils;

public class Content {
	
	private PositionUtils position;
	
	private Navbar navbar;
	ArrayList<ConfigButton> cfgButtons = new ArrayList();
	
	private List<String> alphabet = new ArrayList();
	
	private double scrollY,temp;
	
	public double openAnimation;
	NavbarButtons last = null;
	public Content(PositionUtils position, Navbar navbar) {
		this.position = position;
		this.navbar = navbar;
		openAnimation = 0;
	}

	public void draw(int mouseX, int mouseY) {
		RenderUtils.drawRoundedRect(position.getX(), position.getY(), position.getX2(), position.getY2(), 0x70161616, 2D);
		openAnimation = (openAnimation * 9 + this.position.getWidth()/2)/10;
		
		GL11.glPushMatrix();
		StencilUtils.initStencil();
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        StencilUtils.bindWriteStencilBuffer();
        RenderUtils.drawRect(position.getX(), position.getY(), position.getX2(), position.getY2(), 0x90202020);
		
		StencilUtils.bindReadStencilBuffer(1);
		
		if(navbar.active instanceof CategoryButton) {
			CategoryButton category = (CategoryButton) navbar.active;
			
			double s = 0;
			for(ModuleButton module : category.getModules()) {
				module.getPosition().setX(position.getX()+2.5);
				module.getPosition().setY(position.getY()+2.5+s-scrollY);
				module.draw(mouseX, mouseY);
				s += module.getPosition().getHeight()+2.5;
			}
			
		}
		
		last = navbar.active;
		
        StencilUtils.uninitStencilBuffer();
        GL11.glPopMatrix();
		
		double save = position.getHeight();
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
	
	private String getFileExtension(File file) {
	    String name = file.getName();
	    int lastIndexOf = name.lastIndexOf(".");
	    if (lastIndexOf == -1) {
	        return "";
	    }
	    return name.substring(lastIndexOf);
	}
	public double getBigger() {
		if(navbar.active instanceof CategoryButton) {
			CategoryButton category = (CategoryButton) navbar.active;
			double s = 0;
			for(ModuleButton module : category.getModules()) {
				s += module.getPosition().getHeight()+2.5;
			}
			return s;
		}else if(navbar.active instanceof ClientButtons) {
			ClientButtons clientB = (ClientButtons) navbar.active;
			ArrayList<ConfigButton> cfgButtons = new ArrayList();
			double h = 0;
			double v = 0;
			
			for(ConfigButton cb : cfgButtons) {
				h++;
				if(h == 3) {
					h = 0;
					v+=cb.getPosition().getHeight()+2.5;
				}
			}
			return v;
		}
		return 0;
		
	}
	
	public void clicked(int mouseX, int mouseY, int button) {
		if(position.isInside(mouseX, mouseY)) {
			
			if(navbar.active instanceof CategoryButton) {
				CategoryButton category = (CategoryButton) navbar.active;
				for(ModuleButton module : category.getModules()) {
					module.clicked(mouseX, mouseY, button);;
					
				}
			}
			if(navbar.active instanceof ClientButtons) {
				ClientButtons clientB = (ClientButtons) navbar.active;
				
				double h = 0;
				double v = 0;
				
				for(ConfigButton cb : cfgButtons) {
					cb.onClick(mouseX, mouseY, button);
				}
			
			}
		}
	}
	public void released(int mouseX, int mouseY, int button) {
		if(navbar.active instanceof CategoryButton) {
			CategoryButton category = (CategoryButton) navbar.active;
			for(ModuleButton module : category.getModules()) {
				module.released(mouseX, mouseY, button);
				
			}
		}
	}
	public void key(char typedChar, int keyCode) {
		if(navbar.active instanceof CategoryButton) {
			CategoryButton category = (CategoryButton) navbar.active;
			for(ModuleButton module : category.getModules()) {
				module.key(typedChar,keyCode);
				
			}
		}
	}

	public PositionUtils getPosition() {
		return position;
	}

	public void setPosition(PositionUtils position) {
		this.position = position;
	}

	public Navbar getNavbar() {
		return navbar;
	}

	public void setNavbar(Navbar navbar) {
		this.navbar = navbar;
	}
	
}
