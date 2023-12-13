package me.robbanrobbin.jigsaw.gui.custom.component;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

public abstract class AbstractComponent implements Component {

	private Container parent = null;

	protected Color first, second;

	protected ComponentLook look;

	protected Rectangle rectangle = new Rectangle(0, 0, 0, 0);

	protected boolean visible = true;

	@Override
	public void render() {
		if (look == null) {
			return;
		}
		look.render(this);
	}

	@Override
	public void update() {
		if (look == null) {
			return;
		}
		look.update(this);
	}

	@Override
	public void setX(int x) {
		this.rectangle.x = x;
	}

	@Override
	public void setY(int y) {
		this.rectangle.y = y;
	}

	@Override
	public void setWidth(int width) {
		this.rectangle.width = width;
	}

	@Override
	public void setHeight(int height) {
		this.rectangle.height = height;
	}

	@Override
	public Point getPosition() {
		return new Point(rectangle.x, rectangle.y);
	}

	@Override
	public void setParent(Container parent) {
		this.parent = parent;
	}

	@Override
	public void onMousePress(int x, int y, int button) {

	}

	@Override
	public void onMouseRelease(int x, int y, int button) {

	}

	@Override
	public Container getParent() {
		return this.parent;
	}

	@Override
	public int getHeight() {
		return this.rectangle.height;
	}

	@Override
	public int getWidth() {
		return this.rectangle.width;
	}
}
