package xyz.cucumber.base.interf.DropdownClickGui.ext;

import xyz.cucumber.base.utils.position.PositionUtils;

public interface DropdownButton {
	
	public void draw(int mouseX, int mouseY);
	
	public void onClick(int mouseX, int mouseY, int button);
	
	public void onRelease(int mouseX, int mouseY, int button);
	
	public void onKey(char chr, int key);
	
}
