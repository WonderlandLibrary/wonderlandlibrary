package fun.expensive.client.ui;

import net.minecraft.client.Minecraft;
import fun.expensive.ui.element.Element;
import fun.expensive.ui.element.imp.panel.ElementFlow;
import fun.expensive.ui.element.imp.panel.ElementHeader;

import java.util.ArrayList;
import java.util.List;

public class Panel implements IEventListener {
	
	private Minecraft mc = Minecraft.getMinecraft();
	private List<Element> elements = new ArrayList<Element>();
	
	public boolean dragging;
	public double x, y, x2, y2;
	public double width, height;

	private UI ui;
	
	public Panel(UI ui, double x, double y, double width) {
		this.ui = ui;
		this.x = x;
		this.y = y;
		this.width = width;
		elements.add(new ElementHeader(this));
		elements.add(new ElementFlow(this));
	}

	public void updateScreen(){
		for(Element e : elements){
			if(e instanceof  ElementHeader){
				ElementHeader header = (ElementHeader)e;
				header.updateScreen();
			}
		}
	}

	private void border(int width, int height){
		if(this.y <= -12) this.y = -12;
		if(this.x <= 2) this.x = 2;
		if(this.x >= width - this.getWidth() - 3) this.x = width - this.getWidth() - 3;
		if(this.y >= height - this.getHeight() - 3) this.y = height - this.getHeight() - 3;
	}

	@Override
	public void render(int width, int height, int x, int y, float ticks) {
		if(dragging) {
			this.x = x + x2;
			this.y = y + y2;
		}
		border(width, height);
		int offset = 0;
		for(Element e : elements) {
			e.setX(getX());
			e.setY(getY() + offset);
			offset +=e.getHeight();
		}
		setHeight(offset);
		elements.forEach(e -> e.render(width, height, x, y, ticks));
	}

	@Override
	public void handleMouseInput() {
		elements.forEach(e -> e.handleMouseInput());
	}

	@Override
	public void mouseClicked(int x, int y, int button) {
		elements.forEach(e -> e.mouseClicked(x, y, button))	;
	}
	
	@Override
	public void mouseRealesed(int x, int y, int button) {
		elements.forEach(e -> e.mouseRealesed(x, y, button));
	}

	@Override
	public void keypressed(char c, int key) {
		elements.forEach(e -> e.keypressed(c, key));
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
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

	public UI getUi() {
		return ui;
	}

	public List<Element> getElements() {
		return elements;
	}
	
}
