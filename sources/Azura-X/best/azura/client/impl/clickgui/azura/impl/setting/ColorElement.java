package best.azura.client.impl.clickgui.azura.impl.setting;

import best.azura.client.impl.clickgui.azura.SettingElement;
import best.azura.client.impl.ui.font.Fonts;
import best.azura.client.util.color.HSBColor;
import best.azura.client.util.render.RenderUtil;
import best.azura.client.impl.value.ColorValue;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class ColorElement extends SettingElement {

	private final ColorValue value;
	private boolean extended;
	private double clickAnimation;
	private long clickStart;
	private boolean draggingBrightSat = false, draggingHue = false;

	public ColorElement(int x, int y, int width, int height, ColorValue value) {
		super(x, y, width, height, value);
		this.value = value;
	}

	@Override
	public void render(int mouseX, int mouseY) {
		super.render(mouseX, mouseY);
		this.hovered = RenderUtil.INSTANCE.isHovered(x, y, width, height, mouseX, mouseY);

		if (!extended) {
			float anim = Math.min(1, (System.currentTimeMillis() - clickStart) / 500f);
			clickAnimation = -1 * Math.pow(anim - 1, 6) + 1;
			clickAnimation = 1 - clickAnimation;
		} else {
			float anim = Math.min(1, (System.currentTimeMillis() - clickStart) / 500f);
			clickAnimation = -1 * Math.pow(anim - 1, 6) + 1;
		}

		if (this.visibilityAnimation == 0) {
			this.height = 0;
			return;
		}

		double textAni = Math.min(animation, visibilityAnimation);
		this.height = (int) ((30 + 100 * this.clickAnimation) * visibilityAnimation);
		Fonts.INSTANCE.arial15.drawString(value.getName(), x + 30, y + 15 - Fonts.INSTANCE.arial15.FONT_HEIGHT / 2.0, new Color(255, 255, 255, (int) (255 * textAni)).getRGB());
		RenderUtil.INSTANCE.drawRect(x + width - 30, y + 10, x + width - 40, y + 20, RenderUtil.INSTANCE.modifiedAlpha(value.getObject().getColor(), (int) (255 * textAni)));

		double generalAni = Math.min(animation, Math.min(visibilityAnimation, clickAnimation));
		value.collapsed = clickAnimation > 0.2;
		if (clickAnimation != 0) {

			int bigX = x + 30, bigY = y + 30, diffX = x + width - 70 - bigX;
			if (draggingBrightSat) {
				HSBColor color = value.getObject();
				int mouseDiffX = bigX + diffX - mouseX;
				float sat = mouseDiffX / (float) diffX;
				int mouseDiffY = bigY + 90 - mouseY;
				float bright = mouseDiffY / 90f;
				sat = Math.min(1f, Math.max(0f, sat));
				bright = Math.min(1f, Math.max(0f, bright));
				value.setObject(new HSBColor(color.getHue(), (1 - sat), bright));
			} else if (draggingHue) {
				HSBColor color = value.getObject();
				int mouseDiffY = bigY + 90 - mouseY;
				float bright = mouseDiffY / 90f;
				bright = Math.min(1f, Math.max(0f, bright));
				value.setObject(new HSBColor((1 - bright), color.getSaturation(), color.getBrightness()));
			}

			glPushMatrix();
			glEnable(GL_BLEND);
			glEnable(GL_LINE_SMOOTH);
			glDisable(GL_TEXTURE_2D);
			glLineWidth(1.0f);
			glShadeModel(GL_SMOOTH);
			glBegin(GL_QUADS);
			glColor4f(1f, 1f, 1f, (float) (1f * generalAni));
			glVertex2d(bigX, bigY);
			glColor4f(0f, 0f, 0f, (float) (1f * generalAni));
			glVertex2d(bigX, bigY + 90);
			glColor4f(0f, 0f, 0f, (float) (1f * generalAni));
			glVertex2d(x + width - 70, bigY + 90);
			RenderUtil.INSTANCE.color(RenderUtil.INSTANCE.modifiedAlpha(Color.getHSBColor(value.getObject().getHue(), 1f, 1f), (int) (255 * generalAni)));
			glVertex2d(x + width - 70, bigY);
			glEnd();
			glPopMatrix();
			RenderUtil.INSTANCE.drawGradientRect(x + width - 50, bigY, x + width - 30, bigY + 90 / 4.0,
					RenderUtil.INSTANCE.modifiedAlpha(Color.getHSBColor(0.25f, 1f, 1f), (int) (255 * generalAni)).getRGB(),
					RenderUtil.INSTANCE.modifiedAlpha(Color.getHSBColor(0f, 1f, 1f), (int) (255 * generalAni)).getRGB());
			RenderUtil.INSTANCE.drawGradientRect(x + width - 50, bigY + 90 / 4.0, x + width - 30, bigY + (90 / 4.0 * 2),
					RenderUtil.INSTANCE.modifiedAlpha(Color.getHSBColor(0.5f, 1f, 1f), (int) (255 * generalAni)).getRGB(),
					RenderUtil.INSTANCE.modifiedAlpha(Color.getHSBColor(0.25f, 1f, 1f), (int) (255 * generalAni)).getRGB());
			RenderUtil.INSTANCE.drawGradientRect(x + width - 50, bigY + (90 / 4.0 * 2), x + width - 30, bigY + (90 / 4.0 * 3),
					RenderUtil.INSTANCE.modifiedAlpha(Color.getHSBColor(0.75f, 1f, 1f), (int) (255 * generalAni)).getRGB(),
					RenderUtil.INSTANCE.modifiedAlpha(Color.getHSBColor(0.5f, 1f, 1f), (int) (255 * generalAni)).getRGB());
			RenderUtil.INSTANCE.drawGradientRect(x + width - 50, bigY + (90 / 4.0 * 3), x + width - 30, bigY + (90 / 4.0 * 4),
					RenderUtil.INSTANCE.modifiedAlpha(Color.getHSBColor(1f, 1f, 1f), (int) (255 * generalAni)).getRGB(),
					RenderUtil.INSTANCE.modifiedAlpha(Color.getHSBColor(0.75f, 1f, 1f), (int) (255 * generalAni)).getRGB());
			RenderUtil.INSTANCE.drawLine(x + width - 30 - 20 * generalAni, bigY + 90 * value.getObject().getHue(), x + width - 30, bigY + 90 * value.getObject().getHue(), 2.0f, new Color(255, 255, 255, (int) (255 * generalAni)).getRGB());
			RenderUtil.INSTANCE.drawCircle(bigX + diffX * value.getObject().getSaturation(), bigY + 90 * (1 - value.getObject().getBrightness()), 3.0 * generalAni, new Color(255, 255, 255));
            /*
            glPushMatrix();
            glEnable(GL_BLEND);
            glEnable(GL_LINE_SMOOTH);
            glDisable(GL_TEXTURE_2D);
            glLineWidth(20.0f);
            glShadeModel(GL_SMOOTH);
            glBegin(GL_LINE_LOOP);
            RenderUtil.INSTANCE.color(RenderUtil.INSTANCE.modifiedAlpha(Color.getHSBColor(0f, 1f, 1f), (int) (255*generalAni)));
            glVertex2d(x+width-40, bigY);
            RenderUtil.INSTANCE.color(RenderUtil.INSTANCE.modifiedAlpha(Color.getHSBColor(0.33f, 1f, 1f), (int) (255*generalAni)));
            glVertex2d(x+width-40, bigY+30);
            RenderUtil.INSTANCE.color(RenderUtil.INSTANCE.modifiedAlpha(Color.getHSBColor(0.66f, 1f, 1f), (int) (255*generalAni)));
            glVertex2d(x+width-40, bigY+60);
            RenderUtil.INSTANCE.color(RenderUtil.INSTANCE.modifiedAlpha(Color.getHSBColor(1f, 1f, 1f), (int) (255*generalAni)));
            glVertex2d(x+width-40, bigY+90);
            glEnd();
            glDisable(GL_BLEND);
            glDisable(GL_LINE_SMOOTH);
            glEnable(GL_TEXTURE_2D);
            glPopMatrix();

             */
		}

	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {

		if (RenderUtil.INSTANCE.isHovered(x, y, width, 30, mouseX, mouseY) && animation >= 0.95 && visibilityAnimation >= 0.95) {
			this.extended = !this.extended;
			this.clickStart = System.currentTimeMillis();
		} else if (this.hovered && animation >= 0.95 && visibilityAnimation >= 0.95) {
			int bigX = x + 30, bigY = y + 30;
			int diffX = x + width - 70 - bigX;
			if (RenderUtil.INSTANCE.isHovered(bigX, bigY, diffX, 90, mouseX, mouseY)) {
				draggingBrightSat = true;
			}
			if (RenderUtil.INSTANCE.isHovered(x + width - 50, bigY, 20, 90, mouseX, mouseY)) {
				draggingHue = true;
			}
		}

	}

	@Override
	public void mouseReleased(int mouseX, int mouseY) {
		super.mouseReleased(mouseX, mouseY);
		draggingBrightSat = false;
		draggingHue = false;
	}

	public ColorValue getValue() {
		return value;
	}
}
