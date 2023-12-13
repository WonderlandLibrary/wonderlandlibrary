package me.robbanrobbin.jigsaw.gui.custom.component;

import java.util.ArrayList;

public class AbstractContainer extends AbstractComponent implements Container {

	protected ArrayList<Component> children = new ArrayList<Component>();

	@Override
	public Component[] getChildren() {
		return (Component[]) this.children.toArray();
	}

	@Override
	public void add(Component component) {
		this.children.add(component);
	}

	@Override
	public void positionChildren() {
		int i = 0;
		int height = 0;
		for (Component child : children) {
			child.setX(this.rectangle.x + 1);
			child.setWidth(this.rectangle.width - 1);
			child.setY(i * child.getHeight());
			height += child.getHeight();
			i++;
		}
		this.setHeight(height + 1);
	}

}
