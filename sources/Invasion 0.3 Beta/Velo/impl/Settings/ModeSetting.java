package Velo.impl.Settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import Velo.api.setting.Setting;

public class ModeSetting extends Setting {
	
	public int index;
	
	public List<String> modes;
	
	@Expose
    @SerializedName("setting")
	public String selected;
	
	public boolean isExpanded;
	
	public ModeSetting(String name, String defaultMode, String... modes) {
		this.name = name;
		this.modes = Arrays.asList(modes);
		index = this.modes.indexOf(defaultMode);
		this.selected = this.modes.get(index);
	}
	   public List<String> getValueOrNull(String constantName) {
		   List<String> var2 = this.modes;
		      int var3 = var2.toArray().length;

		      for(int var4 = 0; var4 < var3; ++var4) {
		    	  List<String> value = (List<String>) var2.toArray()[var4];
		         if (((Enum) value).name().equals(constantName)) {
		            return value;
		         }
		      }

		      return null;
		   }
	   
	   
	   
	   public String getSelected() {
		   return selected;
	   }
	public String getMode() {
		return modes.get(index);
	}
	public String getModes() {
		return modes.get(0);
	}
	
	public Object[] getModes1() {
		return  modes.toArray();
	}
	public void setMode(String mode) {
		index = this.modes.indexOf(mode);
	}
	
	public boolean equalsIgnorecase(String mode) {
		return index == modes.indexOf(mode);
	}
	
	public void cycle() {
		if(index < modes.size() - 1) {
			index++;
		} else {
			index = 0;
		}
	}
	public void setMode(Object object) {
		index = this.modes.indexOf(object);
		
	}
	
	
}
