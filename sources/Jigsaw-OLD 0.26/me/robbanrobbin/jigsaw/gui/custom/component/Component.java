package me.robbanrobbin.jigsaw.gui.custom.component;

import java.awt.Point;

public interface Component {

	public void render();

	public void update();

	public void setX(int x);

	public void setY(int y);

	public void setWidth(int width);

	public void setHeight(int height);

	public int getWidth();

	public int getHeight();

	public Point getPosition();

	public void setParent(Container parent);

	public Container getParent();

	public void onMousePress(int x, int y, int button);

	public void onMouseRelease(int x, int y, int button);

}
