package best.azura.client.api.ui.customhud;

import best.azura.client.api.ui.customhud.corner.Direction;
import best.azura.client.api.ui.customhud.corner.Side;
import best.azura.client.api.value.Value;
import best.azura.client.impl.config.Config;
import best.azura.client.impl.value.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"unused", "unchecked"})
public abstract class Element {
	private double x, y, width, height, scale;
	private boolean enabled;
	private String name;
	protected final Minecraft mc;
	protected final ScaledResolution scaledResolution;
	private final Element instance;
	private boolean canSizeBeModified, canScaleBeModified;
	
	/**
	 * Constructor for the Elements
	 *
	 * @param name Name for the corresponding element
	 * @param x    Default x position for the element
	 * @param y    Default y height for the element
	 */
	public Element(final String name, final double x, final double y, final double width, final double height) {
		mc = Minecraft.getMinecraft();
		scaledResolution = new ScaledResolution(mc);
		setScale(1.0);
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
		this.name = name;
		this.enabled = true;
		this.instance = this;
		
	}
	
	/**
	 * @return Builds the json for the settings of the HUD
	 */
	
	public void fitInScreen(int scaledWidth, int scaledHeight) {
		double x = this.getX();
		double y = this.getY();
		double width = this.getWidth();
		double height = this.getHeight();
		
		if (x < 0.0f) {
			this.setX(0.0f);
		} else if (x + width > scaledWidth) {
			this.setX(scaledWidth - width);
		}
		
		if (y < 0.0f) {
			this.setY(0.0f);
		} else if (y + height > scaledHeight) {
			this.setY(scaledHeight - height);
		}
	}
	
	public JsonObject buildJson() {
		JsonObject object = new JsonObject();
		object.addProperty("name", name);
		object.addProperty("x", x);
		object.addProperty("y", y);
		object.addProperty("width", width);
		object.addProperty("height", height);
		object.addProperty("scale", scale);
		object.addProperty("enabled", enabled);
		final List<Value<?>> values = getValues();
		if (values != null && !values.isEmpty()) {
			JsonArray valuesArray = new JsonArray();
			for (Value<?> v : values) {
				if (v instanceof CategoryValue) continue;
				JsonObject valueObject = new JsonObject();
				valueObject.addProperty("name", v.getName());
				if (v instanceof ColorValue)
					valueObject.addProperty("value", ((ColorValue) v).getObject().getRGB());
				else if (v instanceof NumberValue)
					valueObject.addProperty("value", ((NumberValue<Number>) v).getObject().doubleValue());
				else if (v instanceof ModeValue)
					valueObject.addProperty("value", ((ModeValue) v).getObject());
				else if (v instanceof BooleanValue)
					valueObject.addProperty("value", ((BooleanValue) v).getObject());
				else if (v instanceof StringValue)
					valueObject.addProperty("value", ((StringValue) v).getObject());
				else if (v instanceof ComboValue) {
					JsonObject comboObject = new JsonObject();
					for (ComboSelection s : ((ComboValue) v).getObject())
						comboObject.addProperty(s.getName(), s.getObject());
					valueObject.add("value", comboObject);
				}
				valuesArray.add(valueObject);
			}
			object.add("values", valuesArray);
		}
		return object;
	}
	
	public void loadFromJson(JsonObject object) {
		if (object.has("x")) this.x = object.get("x").getAsDouble();
		if (object.has("y")) this.y = object.get("y").getAsDouble();
		if (object.has("width")) this.width = object.get("width").getAsDouble();
		if (object.has("height")) this.height = object.get("height").getAsDouble();
		if (object.has("scale")) this.scale = object.get("scale").getAsDouble();
		if (object.has("enabled")) this.enabled = object.get("enabled").getAsBoolean();
		final List<Value<?>> values = getValues();
		if (object.has("values") && values != null && !values.isEmpty()) {
			JsonArray valuesArray = object.get("values").getAsJsonArray();
			try {
				for (int i1 = 0; i1 < valuesArray.size(); i1++) {
					try {
						if (!valuesArray.get(i1).isJsonObject()) continue;
						JsonObject valueObject = valuesArray.get(i1).getAsJsonObject();
						if (valueObject.has("name") && valueObject.has("value")) {
							Value<?> value = values.stream().filter(v -> v.getName()
									.equalsIgnoreCase(valueObject.get("name").getAsString())).findFirst().orElse(null);
							Config.value(valueObject, value);
						}
					} catch (Exception ignored) {
					}
				}
			} catch (Exception ignored) {
			}
		}
	}
	
	
	/**
	 * Render method
	 */
	public abstract void render();
	
	public List<Value<?>> getValues() {
		return createValuesArray();
	}
	
	public final double getX() {
		return x * mc.displayWidth / scale;
	}
	
	public final double getY() {
		return y * mc.displayHeight / scale;
	}
	
	public final void setX(final double x) {
		this.x = x / mc.displayWidth * scale;
	}
	
	public final void setY(final double y) {
		this.y = y / mc.displayHeight * scale;
	}
	
	public final void setXNormal(final double x) {
		this.x = x / scaledResolution.getScaledWidth() * scale;
	}
	
	public final void setYNormal(final double y) {
		this.y = y / scaledResolution.getScaledHeight() * scale;
	}
	
	protected final ScaledResolution getScaledResolution() {
		return scaledResolution;
	}
	
	public double getWidth() {
		return width * scale;
	}
	
	public double getHeight() {
		return height * scale;
	}
	
	public void setWidth(double width) {
		this.width = width / scale;
	}
	
	public void setHeight(double height) {
		this.height = height / scale;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public final String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isCanSizeBeModified() {
		return canSizeBeModified;
	}

	public void onFastTick() {}
	
	public void setCanSizeBeModified(boolean canSizeBeModified) {
		this.canSizeBeModified = canSizeBeModified;
	}
	
	public boolean isCanScaleBeModified() {
		return canScaleBeModified;
	}
	
	public void setCanScaleBeModified(boolean canScaleBeModified) {
		this.canScaleBeModified = canScaleBeModified;
	}
	
	public List<Value<?>> createValuesArray(Value<?>... valuesIn) {
		final List<Value<?>> values = new ArrayList<>();
		if (canSizeBeModified) {
			values.add(new NumberValue<Double>("Width", "Width of the " + name + " element",
					(val) -> width = ((NumberValue<Double>) val).getObject(), width, 0D, 5000D));
			values.add(new NumberValue<Double>("Height", "Height of the " + name + " element",
					(val) -> height = ((NumberValue<Double>) val).getObject(), height, 0D, 5000D));
		}
		if (canScaleBeModified) values.add(new NumberValue<>("Scale", "Scale of the " + name + " element",
				(val) -> scale = ((NumberValue<Double>) val).getObject(), scale, 0.1D, 0.1D, 5.0D));
		if (valuesIn != null && valuesIn.length > 0) values.addAll(Arrays.asList(valuesIn));
		return values;
	}
	
	public void setScale(double scale) {
		this.scale = scale;
		if (this.scale <= 0) this.scale = 1;
	}
	
	public double getScale() {
		return scale;
	}
	
	public Direction getDirection() {
		return getY() > mc.displayHeight / 2. ? Direction.DOWN : Direction.UP;
	}
	
	public Side getSide() {
		return getX() <= mc.displayWidth / 2. ? Side.LEFT : Side.RIGHT;
	}
	
	public Element getInstance() {
		return instance;
	}
	
	public void onRemove() {
	}
	
	public void onAdd() {
	}
	
	public Element copy() {
		return null;
	}
	
	@Override
	public String toString() {
		return "Element{" +
				"x=" + x +
				", y=" + y +
				", width=" + width +
				", height=" + height +
				", enabled=" + enabled +
				", name='" + name + '\'' +
				", mc=" + mc +
				'}';
	}
}