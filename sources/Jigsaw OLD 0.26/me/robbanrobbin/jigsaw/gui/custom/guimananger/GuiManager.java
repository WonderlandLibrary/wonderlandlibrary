package me.robbanrobbin.jigsaw.gui.custom.guimananger;

import me.robbanrobbin.jigsaw.gui.custom.component.ComponentFrame;

public interface GuiManager {

	public void init();

	public void addFrame(ComponentFrame frame);

	public void removeFrame(ComponentFrame frame);

	public ComponentFrame[] getFrames();

	public void bringForward(ComponentFrame frame);

	public void render();

	public void update();

}
