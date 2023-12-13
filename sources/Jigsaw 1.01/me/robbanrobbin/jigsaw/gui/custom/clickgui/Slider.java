package me.robbanrobbin.jigsaw.gui.custom.clickgui;

import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

import java.awt.Point;

import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.gui.custom.clickgui.utils.GuiUtils;

public class Slider extends Component {

	private String title;
	private double increment = 0.0;
	private Number value;
	private double minValue;
	private double maxValue;
	private ValueFormat format;
	private SliderTask task;
	private SliderSetting sliderSetting;
	private double preValue;
	private boolean idk = false;

	public Slider(String title, Number value, double minValue, double maxValue, double increment, ValueFormat format,
			SliderTask task, SliderSetting sliderSetting) {
		this.title = title;
		this.value = value;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.increment = increment;
		this.format = format;
		this.task = task;
		this.sliderSetting = sliderSetting;
	}

	@Override
	public void update() {
		this.setValue(sliderSetting.getValue());
//		if(idk) {
//			task.task(this);
//		}
	}

	@Override
	public void draw() {
//		double value = (this.preValue
//				+ (this.value.doubleValue() - this.preValue) * Minecraft.getMinecraft().timer.renderPartialTicks);
		double value = this.value.doubleValue();
		GuiUtils.translate(this, false);
		Point mouse = GuiUtils.calculateMouseLocation();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		glDisable(GL_CULL_FACE);
		
		GuiUtils.setColorsBasedOnSettingContainer(this);
		
		glBegin(GL11.GL_QUADS);
		{
			glVertex2d(0, 0);
			glVertex2d(0, getHeight());
			glVertex2d(getWidth(), getHeight());
			glVertex2d(getWidth(), 0);
		}
		glEnd();
		double sliderWidth = ((value - minValue) / (maxValue - minValue)) * getWidth();
		double sliderHeight = 3d;
		double compHeightInTwo = getHeight() / 2 + fontRenderer.FONT_HEIGHT / 2;
		
		GL11.glLineWidth(1f);
		
		GuiUtils.setColor(background, 0.8f);
		
		glBegin(GL11.GL_QUADS);
		{
			glVertex2d(0, compHeightInTwo - sliderHeight / 2);
			glVertex2d(0, compHeightInTwo + sliderHeight / 2);
			glVertex2d(getWidth(), compHeightInTwo + sliderHeight / 2);
			glVertex2d(getWidth(), compHeightInTwo - sliderHeight / 2);
		}
		glEnd();
		
		GuiUtils.setColor(foreground, 1f);
		
		glBegin(GL11.GL_QUADS);
		{
			glVertex2d(0, compHeightInTwo - sliderHeight / 2);
			glVertex2d(0, compHeightInTwo + sliderHeight / 2);
			glVertex2d(sliderWidth, compHeightInTwo + sliderHeight / 2);
			glVertex2d(sliderWidth, compHeightInTwo - sliderHeight / 2);
		}
		glEnd();
		
		GuiUtils.renderShadowHorizontal(0.2, 6, compHeightInTwo + sliderHeight / 2, sliderWidth, getWidth(), false, false);
		GuiUtils.renderShadowHorizontal(0.2, 8, compHeightInTwo + sliderHeight / 2, 0, sliderWidth, false, false);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		fontRenderer.drawStringWithShadow(title, 1, 0, 0xffffffff);
		String sValue = getValueString(value);
		fontRenderer.drawStringWithShadow(sValue, (int) (getWidth() - fontRenderer.getStringWidth(sValue) - 2), 0,
				0xffffffff);
		
		GL11.glDisable(GL11.GL_BLEND);
		glEnable(GL_CULL_FACE);
		GuiUtils.translate(this, true);
	}

	@Override
	public void onDragged(int x, int y, double mx, double my, int button) {
		super.onDragged(x, y, mx, my, button);
		if (button == 0) {
			this.setValue((((mx) / getWidth()) * (maxValue - minValue)) + minValue);
			task.task(this);
		}
	}

	@Override
	public void onClicked(double x, double y, int button) {
		super.onClicked(x, y, button);
		if (button == 0) {
			this.setValue((((x) / getWidth()) * (maxValue - minValue)) + minValue);
			task.task(this);
		}
	}

	public void setValue(Number value) {
		this.value = value;
//		idk = true;
	}

	private String getValueString(double value) {
		String sValue = "";
		if (format == ValueFormat.DECIMAL) {
			sValue = String.valueOf(Math.round(value * 10.0) / 10.0);
		}
		if (format == ValueFormat.PERCENT) {
			sValue = String.valueOf((Math.round((value / maxValue) * 1000d)) / 10d) + "%";
		}
		if (format == ValueFormat.INT) {
			sValue = String.valueOf(Math.round(value));
		}
		if (format == ValueFormat.MS) {
			sValue = String.valueOf(Math.round(value)) + "ms";
		}
		if (format == ValueFormat.DEGREES) {
			sValue = String.valueOf(Math.round(value)) + "°";
		}
		return sValue;
	}

	@Override
	public double getPreferedWidth() {
		return fontRenderer.getStringWidth(title + getValueString(this.value.doubleValue()) + "___");
	}

	@Override
	public double getPreferedHeight() {
		return fontRenderer.FONT_HEIGHT + 7;
	}

	public String getTitle() {
		return title;
	}

	public double getIncrement() {
		return increment;
	}

	public double getMinValue() {
		return minValue;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public Number getValue() {
		return value;
	}

}
