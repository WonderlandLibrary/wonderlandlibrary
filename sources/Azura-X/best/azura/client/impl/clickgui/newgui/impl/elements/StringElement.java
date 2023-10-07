package best.azura.client.impl.clickgui.newgui.impl.elements;

import best.azura.client.impl.value.StringValue;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;

public class StringElement extends SettingElement {

	private final StringValue value;
	private long start;
	private boolean extended = false;

	public StringElement(int x, int y, int width, int height, StringValue value) {
		super(x, y, width, height, value);
		this.value = value;
		this.start = System.currentTimeMillis();
	}

	@Override
	public void render(int mouseX, int mouseY) {
		super.render(mouseX, mouseY);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if (this.hovered && this.animation >= 0.99) {
			this.extended = !this.extended;
			this.start = System.currentTimeMillis();
		} else if (this.extended) {
			this.extended = false;
			this.start = System.currentTimeMillis();
		}
	}

	@Override
	public void keyTyped(char typed, int keyCode) {
		if (!extended) return;

		if (keyCode == Keyboard.KEY_BACK) {
			if (!value.getObject().equals("")) {
				value.setObject(value.getObject().substring(0, value.getObject().length() - 1));
			}
		} else if (ChatAllowedCharacters.isAllowedCharacter(typed)) {
			value.setObject(value.getObject() + typed);
		}
	}

	public StringValue getValue() {
		return value;
	}

}
