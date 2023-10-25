package me.rhys.base.module.data;

import com.google.gson.annotations.SerializedName;

public class ModuleData {
  @SerializedName("name")
  private final String name;
  
  @SerializedName("description")
  private final String description;
  
  @SerializedName("category")
  private final Category category;
  
  @SerializedName("keyCode")
  private int keyCode;
  
  @SerializedName("mode")
  private int currentMode;
  
  @SerializedName("enabled")
  private boolean enabled;
  
  public ModuleData(String name, String description, Category category, int keyCode, int currentMode, boolean enabled) {
    this.name = name;
    this.description = description;
    this.category = category;
    this.keyCode = keyCode;
    this.currentMode = currentMode;
    this.enabled = enabled;
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getDescription() {
    return this.description;
  }
  
  public Category getCategory() {
    return this.category;
  }
  
  public void setKeyCode(int keyCode) {
    this.keyCode = keyCode;
  }
  
  public int getKeyCode() {
    return this.keyCode;
  }
  
  public void setCurrentMode(int currentMode) {
    this.currentMode = currentMode;
  }
  
  public int getCurrentMode() {
    return this.currentMode;
  }
  
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
  
  public boolean isEnabled() {
    return this.enabled;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\module\data\ModuleData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */