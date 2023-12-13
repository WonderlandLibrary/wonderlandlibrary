package me.robbanrobbin.jigsaw.gui.custom.clickgui;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.layout.Layout;

public class ModuleWindow extends ComponentWindow {
	
	public ModuleWindow(Layout layout) {
		super(layout);
	}
	
	protected Category category;
	
	public Category getCategory() {
		return category;
	}
	
	public void setCategory(Category category) {
		this.category = category;
	}

}
