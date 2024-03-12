package xyz.cucumber.base.interf.DropdownClickGui.ext.Settings;

import org.lwjgl.input.Keyboard;

import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.Fonts;

public class DropdownClickGuiKeybind extends DropdownClickGuiSettings {

	private Mod module;
	
	private boolean binding;

	public DropdownClickGuiKeybind(Mod module, PositionUtils position) {
		this.module = module;
		this.position = position;
	}

	@Override
	public void draw(int mouseX, int mouseY) {
		String s = Keyboard.getKeyName(module.getKey());
		if(binding) s = "...";
		Fonts.getFont("rb-m-13").drawString("Key: "+s+"",
				this.position.getX()+this.position.getWidth()/2-Fonts.getFont("rb-m-13").getWidth("Key: "+s+"")/2,
				this.position.getY()+2,
				-1);
	}

	@Override
	public void onClick(int mouseX, int mouseY, int button) {
		binding = !binding;
	}

	@Override
	public void onKey(char chr, int key) {
		if(binding) {
			binding =false;
			if(key == Keyboard.KEY_ESCAPE) {
				module.setKey(0);
				return;
			}
			module.setKey(key);
		}
	}
	
	
	
}
