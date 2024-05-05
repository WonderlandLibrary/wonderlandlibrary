package best.azura.client.impl.clickgui.azura.impl.setting;

import best.azura.client.impl.clickgui.azura.SettingElement;
import best.azura.client.impl.ui.font.Fonts;
import best.azura.client.util.render.RenderUtil;
import best.azura.client.impl.value.StringValue;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;

import java.awt.*;

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
		this.height = this.visibilityAnimation == 0 ? 0 : (int) (30 * this.visibilityAnimation);
		if (this.visibilityAnimation == 0) return;

		double clickAnimation = 0;
		if (!this.extended) {
			float anim = Math.min(1, (System.currentTimeMillis() - start) / 200f);
			clickAnimation = -1 * Math.pow(anim - 1, 6) + 1;
			clickAnimation = 1 - clickAnimation;
		} else {
			float anim = Math.min(1, (System.currentTimeMillis() - start) / 200f);
			clickAnimation = -1 * Math.pow(anim - 1, 6) + 1;
		}

		this.hovered = RenderUtil.INSTANCE.isHovered(x, y, width, height, mouseX, mouseY);

		double opacity = visibilityAnimation == 1 ? animation : visibilityAnimation;
		if (clickAnimation != 0) {
			RenderUtil.INSTANCE.drawLine(x + 30, y + height - 3, x + 30 + ((x + width - 30) - (x + 30)) * clickAnimation, y + height - 3, 2.0f, RenderUtil.INSTANCE.modifiedAlpha(new Color(255, 255, 255), (int) (255 * opacity)).getRGB());
		}

		if (!extended) {
			Fonts.INSTANCE.arial15.drawString(value.getName() + ": " + (value.getObject().equals("") ? "Nothing" : value.getObject()), x + 30, y + 15 - Fonts.INSTANCE.arial15.FONT_HEIGHT / 2.0,
					new Color(255, 255, 255, (int) (255 * ((1 - clickAnimation) == 1 ? visibilityAnimation == 1 ? animation : visibilityAnimation : (1 - clickAnimation)))).getRGB());
		}

		String text = "Write here..";
		if (!value.getObject().isEmpty()) {
			text = value.getObject();
		}
		Fonts.INSTANCE.arial15.drawString(text, x + 30, y + 15 - Fonts.INSTANCE.arial15.FONT_HEIGHT / 2.0,
				new Color(255, 255, 255, (int) (255 * (clickAnimation == 1 ? visibilityAnimation == 1 ? animation : visibilityAnimation : clickAnimation))).getRGB());

		if (clickAnimation != 0 && !text.equals("Write here..")) {
			int width = Fonts.INSTANCE.arial15.getStringWidth(text);
			RenderUtil.INSTANCE.drawLine(x + 30 + width + 3, y + 15 - Fonts.INSTANCE.arial15.FONT_HEIGHT / 2.0, x + 30 + width + 3, y + 15 + Fonts.INSTANCE.arial15.FONT_HEIGHT / 2.0 - 2, 1.0f,
					new Color(255, 255, 255, (int) (255 * (clickAnimation == 1 ? visibilityAnimation == 1 ? animation : visibilityAnimation : clickAnimation))).getRGB());
		}

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
