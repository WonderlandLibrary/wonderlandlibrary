package org.neverhook.client.settings.config;

import com.google.gson.JsonObject;

public interface ConfigUpdater {
  JsonObject save();
  
  void load(JsonObject paramJsonObject);
}


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\settings\config\ConfigUpdater.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */