package best.azura.client.api.ui.buttons;

public interface Button {

	void draw(int mouseX, int mouseY);

	void keyTyped(char typed, int keyCode);

	void mouseClicked();
}
