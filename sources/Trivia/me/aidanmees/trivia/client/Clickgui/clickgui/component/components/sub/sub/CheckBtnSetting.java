package me.aidanmees.trivia.client.Clickgui.clickgui.component.components.sub.sub;

import java.lang.reflect.Field;

import me.aidanmees.trivia.client.settings.ClientSettings;
import me.aidanmees.trivia.gui.custom.clickgui.CheckBtnTask;
import me.aidanmees.trivia.gui.custom.clickgui.Component;
import me.aidanmees.trivia.gui.custom.clickgui.ModSetting;
import me.aidanmees.trivia.gui.custom.clickgui.SettingCheckBtn;

public abstract class CheckBtnSetting extends ModSetting {

	private String name;
	public Boolean value;
	private Field valueField;
	
	public CheckBtnSetting(String name, String fieldName) {
		this.name = name;
		Field[] fields = ClientSettings.class.getFields();
		for(Field field : fields) {
			try {
				if(fieldName.equals(field.getName())) {
					this.valueField = field;
					
					this.value = valueField.getBoolean(ClientSettings.class);
					return;
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		System.out.println("NO VALUE FOR: " + name);
	}
	
	
	public boolean getValue() {
		try {
			return valueField.getBoolean(ClientSettings.class);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Field getValueField() {
		return valueField;
	}
	
}
