package graphite.impl.gui.click.framework;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.ScaledResolution;

public class Component {
	private final List<Component> children = new ArrayList<>();
	private final Component parent;
	private double x;
	private double y;
	private double width;
	private double height;

	public Component(final Component parent, final double x, final double y, final double width, final double height) {
		this.parent = parent;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public Component getParent() {
		return this.parent;
	}

	public void addChild(Component child) {
		this.children.add(child);
	}

	public void onDraw(final ScaledResolution scaledResolution, int mouseX, int mouseY) {
		for (Component child : this.children) {
			child.onDraw(scaledResolution, mouseX, mouseY);
		}
	}

	public void onMouseClick(int mouseX, int mouseY, int button) {
		for (Component child : this.children) {
			child.onMouseClick(mouseX, mouseY, button);
		}
	}

	public void onMouseRelease(int button) {
		for (Component child : this.children) {
			child.onMouseRelease(button);
		}
	}

	public void onKeyPress(int keyCode) {
		for (Component child : this.children) {
			child.onKeyPress(keyCode);
		}
	}

	public double getX() {
		Component familyMember = this.parent;
		double familyTreeX = this.x;

		while (familyMember != null) {
			familyTreeX += familyMember.x;
			familyMember = familyMember.parent;
		}

		return familyTreeX;
	}

	public void setX(double x) {
		this.x = x;
	}

	public boolean isHovered(int mouseX, int mouseY) {
		if (this instanceof Expandable) {
			final Expandable expandableComponent = (Expandable) this;
			if (expandableComponent.isHoveredExpanded(mouseX, mouseY))
				return true;
		}

		final double x = this.getX();
		final double y = this.getY();

		return mouseX > x && mouseY > y && mouseX < x + getWidth() && mouseY < y + getHeight();
	}

	public double getY() {
		Component familyMember = this.parent;
		double familyTreeY = this.y;

		while (familyMember != null) {
			familyTreeY += familyMember.y;
			familyMember = familyMember.parent;
		}

		return familyTreeY;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public boolean hasChildren() {
		return !this.children.isEmpty();
	}

	public List<Component> getChildren() {
		return children;
	}
}