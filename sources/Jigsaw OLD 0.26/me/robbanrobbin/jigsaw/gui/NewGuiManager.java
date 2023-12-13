package me.robbanrobbin.jigsaw.gui;

import me.robbanrobbin.jigsaw.gui.custom.component.ComponentButton;
import me.robbanrobbin.jigsaw.gui.custom.component.ComponentFrame;
import me.robbanrobbin.jigsaw.gui.custom.guimananger.AbstractGuiManager;

public class NewGuiManager extends AbstractGuiManager {

	@Override
	public void init() {

		ComponentFrame frame = new ComponentFrame();
		frame.setX(50);
		frame.setY(60);
		frame.setWidth(50);
		ComponentButton button = new ComponentButton();
		button.setHeight(10);
		frame.add(button);
		addFrame(frame);

	}

}
