package best.azura.client.impl.clickgui.newgui.impl.panel;

public abstract class PanelElement {

	public int x, y, width, height;
	public boolean hovered = false;
	public double animation = 0;

	public PanelElement(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public abstract void render(int mouseX, int mouseY);

	public void mouseClicked(int mouseX, int mouseY, int button) {}

	public void mouseReleased(int mouseX, int mouseY) {}

	public void keyTyped(char typed, int keyCode) {}

}
