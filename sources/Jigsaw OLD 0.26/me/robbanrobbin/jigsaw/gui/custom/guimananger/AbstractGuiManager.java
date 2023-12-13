package me.robbanrobbin.jigsaw.gui.custom.guimananger;

import java.util.concurrent.CopyOnWriteArrayList;

import me.robbanrobbin.jigsaw.gui.custom.component.ComponentFrame;

public abstract class AbstractGuiManager implements GuiManager {

	protected CopyOnWriteArrayList<ComponentFrame> frames = new CopyOnWriteArrayList<ComponentFrame>();

	@Override
	public abstract void init();

	@Override
	public void addFrame(ComponentFrame frame) {
		frames.add(frame);
	}

	@Override
	public void removeFrame(ComponentFrame frame) {
		frames.remove(frame);
	}

	@Override
	public ComponentFrame[] getFrames() {
		return this.frames.toArray(new ComponentFrame[frames.size()]);
	}

	@Override
	public void bringForward(ComponentFrame frame) {
	}

	@Override
	public void render() {
		for (ComponentFrame frame : frames) {
			frame.render();
		}
	}

	@Override
	public void update() {
		for (ComponentFrame frame : frames) {
			frame.update();
		}
	}

}
