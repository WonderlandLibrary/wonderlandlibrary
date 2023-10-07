package best.azura.client.impl.clickgui.azura;

import best.azura.client.impl.clickgui.azura.impl.setting.ModeElement;
import best.azura.client.impl.module.impl.render.ClickGUI;
import best.azura.client.impl.ui.font.Fonts;
import best.azura.client.util.render.RenderUtil;

import java.awt.*;

public class ModeSelector {

	public int x, y, width, height;
	public final ModeElement parent;
	public double animation;
	public long start;
	public boolean hovered = false;

	public ModeSelector(int x, int y, int width, ModeElement element) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = 0;
		this.parent = element;
		this.animation = 0;
		this.start = System.currentTimeMillis();
	}

	public void draw(int mouseX, int mouseY) {

		this.hovered = RenderUtil.INSTANCE.isHovered(x, y, width, height, mouseX, mouseY);
		float anim = Math.min(1, (System.currentTimeMillis() - start) / 500f);
		animation = -1 * Math.pow(anim - 1, 6) + 1;

		RenderUtil.INSTANCE.drawRoundedRect(x, y, width * animation, height, 10, new Color(0, 0, 0, (int) (50 * animation) + 100));
		int count = 0;
		for (String s : parent.getValue().getObjects()) {
			Color color = parent.getValue().getObject().equals(s) ? RenderUtil.INSTANCE.modifiedAlpha(ClickGUI.color.getObject().getColor(), (int) (255 * animation)) : new Color(30, 30, 30, (int) (255 * animation));
			int elementY = (int) (y + (45 + count * 30) * animation);
			RenderUtil.INSTANCE.drawRoundedRect(x + 30, elementY - 5, 10, 10, 10, color);
			Fonts.INSTANCE.arial15.drawString(s, x + 48, elementY - Fonts.INSTANCE.arial15.FONT_HEIGHT / 2.0,
					new Color(255, 255, 255, (int) (255 * animation)).getRGB());
			count++;
		}
		this.height = (int) ((30 + count * 30 * animation));

	}

	public void mouseClicked() {
		if (!this.hovered) {
			animation = 0;
		}
	}

}
