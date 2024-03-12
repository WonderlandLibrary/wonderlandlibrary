package xyz.cucumber.base.interf.clickgui.navbar.ex;

import net.minecraft.util.ResourceLocation;
import xyz.cucumber.base.interf.clickgui.navbar.Navbar;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.Fonts;

public class ClientButtons extends NavbarButtons{
	private String name;
	
	private Navbar navbar;
	
	public ClientButtons(String name, Navbar navbar) {
		this.name = name;
		this.navcategory = NavCategory.Client;
		this.position = new PositionUtils(0,0,90,15,1);
		this.navbar = navbar;
	}

	@Override
	public void draw(int mouseX, int mouseY) {
		int color = 0xffaaaaaa;
		if(navbar.active == this) {
			color = 0xff000000;
		}
		RenderUtils.drawImage(position.getX()+8, position.getY()+2, position.getHeight()-4, position.getHeight()-4, new ResourceLocation("client/images/"+name.toLowerCase()+".png"), color);
		Fonts.getFont("rb-r").drawString(name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase(), position.getX()+30,position.getY()+position.getHeight()/2-Fonts.getFont("rb-r").getHeight(name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase())/2, color);
	}

	@Override
	public void clicked(int mouseX, int mouseY, int button) {
		if(position.isInside(mouseX, mouseY) && button == 0) {
			navbar.active = this;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Navbar getNavbar() {
		return navbar;
	}

	public void setNavbar(Navbar navbar) {
		this.navbar = navbar;
	}
	
	
}


