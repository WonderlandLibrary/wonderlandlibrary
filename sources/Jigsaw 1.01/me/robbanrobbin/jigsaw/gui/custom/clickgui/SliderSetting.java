package me.robbanrobbin.jigsaw.gui.custom.clickgui;

import java.lang.reflect.Field;

import me.robbanrobbin.jigsaw.client.settings.ClientSettings;

public class SliderSetting extends ModSetting {
	
	private String name;
	private double increment = 0.0;
	private Number value;
	private double minValue;
	private double maxValue;
	private ValueFormat valueFormat;
	private Field valueField;
	
	public SliderSetting(String name, String fieldName, double minValue, double maxValue, ValueFormat valueFormat) {
		this(name, fieldName, minValue, maxValue, 0.0, valueFormat);
	}
	
	public SliderSetting(String name, String fieldName, double minValue, double maxValue, double increment, ValueFormat valueFormat) {
		this.name = name;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.increment = increment;
		this.valueFormat = valueFormat;
		Field[] fields = ClientSettings.class.getFields();
		for(Field field : fields) {
			try {
				if(fieldName.equals(field.getName())) {
					this.valueField = field;
					this.value = (Number) valueField.get(ClientSettings.class);
					return;
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Component createComponent() {
		SliderTask task = new SliderTask() {
			@Override
			public void task(Slider slider) {
				if(value instanceof Integer) {
					try {
						valueField.setInt(value, slider.getValue().intValue());
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				if(value instanceof Double) {
					try {
						valueField.setDouble(value, slider.getValue().doubleValue());
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				if(value instanceof Float) {
					try {
						valueField.setFloat(value, slider.getValue().floatValue());
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		};
		return new Slider(name, value, minValue, maxValue, increment, valueFormat, task, this);
	}

	public Number getValue() {
		try {
			return (Number) valueField.get(ClientSettings.class);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
