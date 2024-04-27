package me.aquavit.liquidsense.ui.client.hud.element;

import me.aquavit.liquidsense.utils.mc.MinecraftInstance;
import me.aquavit.liquidsense.value.Value;
import net.minecraft.client.gui.ScaledResolution;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class Element extends MinecraftInstance {

	private final ElementInfo info;
	public float prevMouseX;
	public float prevMouseY;
	private double x;
	private double y;
	private float scale;
	private Border border;
	private boolean drag;
	private Side side;

	public Element(double x, double y, float scale, Side side) {
		this.x = x;
		this.y = y;
		this.side = side;
		ElementInfo elementInfo = this.getClass().getAnnotation(ElementInfo.class);
		if (elementInfo != null) {
			this.info = elementInfo;
			this.scale = 1.0F;
			this.setScale(scale);
		} else {
			throw new IllegalArgumentException("Passed element with missing element info");
		}
	}

	public Element() {
		this(2.0, 2.0, 1F, new Side(Side.Horizontal.LEFT, Side.Vertical.UP));
	}

	public final ElementInfo getInfo() {
		return this.info;
	}

	public final float getScale() {
		return this.info.disableScale() ? 1.0F : this.scale;
	}

	public final void setScale(float value) {
		if (!this.info.disableScale()) {
			this.scale = value;
		}
	}

	public final String getName() {
		return this.info.name();
	}

	public final double getRenderX() {
		switch (side.getHorizontal()) {
			case LEFT:
				return x;
			case RIGHT:
				return new ScaledResolution(mc).getScaledWidth() - x;
			case MIDDLE:
				return (float) new ScaledResolution(mc).getScaledWidth() / 2 - x;
		}
		return 0;
	}

	public final double getRenderY() {
		switch (side.getVertical()) {
			case UP:
				return y;
			case MIDDLE:
				return (float) (new ScaledResolution(mc).getScaledHeight() / 2) - y;
			case DOWN:
				return new ScaledResolution(mc).getScaledHeight() - y;
			default:return 0;
		}
	}

	public final void setRenderX(double value) {
		switch (side.getHorizontal()) {
			case MIDDLE:
			case RIGHT:
				x -= value;
				break;
			case LEFT:
				x += value;
				break;
		}
	}

	public final void setRenderY(double value) {
		switch (side.getVertical()) {
			case UP: {
				y += value;
				break;
			}
			case MIDDLE:
			case DOWN:
				y -= value;
				break;
		}
	}

	public final Border getBorder() {
		return this.border;
	}

	public final void setBorder(Border state) {
		this.border = state;
	}

	public final boolean getDrag() {
		return this.drag;
	}

	public final void setDrag(boolean state) {
		this.drag = state;
	}

	public final float getPrevMouseX() {
		return this.prevMouseX;
	}

	public final void setPrevMouseX(float x) {
		this.prevMouseX = x;
	}

	public final float getPrevMouseY() {
		return this.prevMouseY;
	}

	public final void setPrevMouseY(float y) {
		this.prevMouseY = y;
	}

	public List<Value<?>> getValues() {
		final List<Value<?>> values = new ArrayList<Value<?>>();
		for (final Field field : this.getClass().getDeclaredFields()) {
			try {
				field.setAccessible(true);
				final Object o = field.get(this);
				if (o instanceof Value && ((Value<?>) o).isDisplayable()) {
					values.add((Value<?>) o);
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return values;
	}

	public boolean createElement() {
		return true;
	}

	public void destroyElement() {
	}

	public abstract Border drawElement();

	public void updateElement() {
	}

	public void livingupdateElement() {
	}

	public boolean isInBorder(double x, double y) {
		Border border = this.border;
		if (border == null) return false;

		float minX = Math.min(border.x, border.x2);
		float minY = Math.min(border.y, border.y2);

		float maxX = Math.max(border.x, border.x2);
		float maxY = Math.max(border.y, border.y2);

		return minX <= x && minY <= y && maxX >= x && maxY >= y;
	}

	public void handleMouseClick(double x, double y, int mouseButton) {
	}

	public void handleKey(char c, int keyCode) {
	}

	public final double getX() {
		return this.x;
	}

	public final void setX(double x) {
		this.x = x;
	}

	public final double getY() {
		return this.y;
	}

	public final void setY(double y) {
		this.y = y;
	}

	public final Side getSide() {
		return this.side;
	}

	public final void setSide(Side side) {
		this.side = side;
	}
}
