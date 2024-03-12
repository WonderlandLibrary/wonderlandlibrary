package xyz.cucumber.base.interf.clickgui.navbar.ex;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.ResourceLocation;
import xyz.cucumber.base.Client;
import xyz.cucumber.base.interf.clickgui.content.ex.ModuleButton;
import xyz.cucumber.base.interf.clickgui.navbar.Navbar;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.Fonts;

public class CategoryButton extends NavbarButtons{
	
	private Category category;
	
	private List<ModuleButton> modules = new ArrayList();
	
	private Navbar navbar;
	
	public CategoryButton(Category category, Navbar navbar) {
		this.category = category;
		this.navcategory = NavCategory.Modules;
		this.position = new PositionUtils(0,0,90,15,1);
		this.navbar = navbar;
		for(Mod m : Client.INSTANCE.getModuleManager().getModulesByCategory(category)) {
			modules.add(new ModuleButton(m, new PositionUtils(0,0,242.5-5, 15, 1)));
		}
	}

	@Override
	public void draw(int mouseX, int mouseY) {
		int color = 0xffaaaaaa;
		if(navbar.active == this) {
			color = 0xff000000;
		}
		
		RenderUtils.drawImage(position.getX()+8, position.getY()+2, position.getHeight()-4, position.getHeight()-4, new ResourceLocation("client/images/"+category.name().toLowerCase()+".png"), color);
		Fonts.getFont("rb-r").drawString(category.name().substring(0, 1).toUpperCase() + category.name().substring(1).toLowerCase(), position.getX()+30,position.getY()+position.getHeight()/2-Fonts.getFont("rb-r").getHeight(category.name().substring(0, 1).toUpperCase() + category.name().substring(1).toLowerCase())/2, color);
	}

	@Override
	public void clicked(int mouseX, int mouseY, int button) {
		if(position.isInside(mouseX, mouseY) && button == 0) {
			navbar.active = this;
		}
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<ModuleButton> getModules() {
		return modules;
	}

	public void setModules(List<ModuleButton> modules) {
		this.modules = modules;
	}

	public Navbar getNavbar() {
		return navbar;
	}

	public void setNavbar(Navbar navbar) {
		this.navbar = navbar;
	}
	
	
}
