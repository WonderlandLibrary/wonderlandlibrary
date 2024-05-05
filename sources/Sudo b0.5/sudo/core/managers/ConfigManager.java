package sudo.core.managers;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import net.minecraft.client.MinecraftClient;
import sudo.Client;
import sudo.module.Mod;
import sudo.module.ModuleManager;
import sudo.module.settings.BooleanSetting;
import sudo.module.settings.ColorSetting;
import sudo.module.settings.KeybindSetting;
import sudo.module.settings.ModeSetting;
import sudo.module.settings.NumberSetting;
import sudo.module.settings.Setting;

public class ConfigManager {
	@SuppressWarnings("resource")
    public static void saveConfig() {
		File directory = new File(MinecraftClient.getInstance().runDirectory + "\\config\\sudo\\");
		String path = MinecraftClient.getInstance().runDirectory + "\\config\\sudo\\";
		
		if (directory.mkdir()) {
			Client.logger.info("Sudo config folder successfully created");
		}
		
        for (Mod module : ModuleManager.INSTANCE.getModules()) {
            try {
                FileWriter writer = new FileWriter(path + module.getName() + ".json");
                writer.write(""
                		+ "{"
                		+ "\n    \"name\": \"" + module.getName() + "\","
                		+ "\n    \"enabled\": " + module.isEnabled() + ",");

				for (Setting setting : module.getSetting()) {
	                if (setting instanceof BooleanSetting) {
	                	writer.write("\n    \"" + ((BooleanSetting) setting).getName() + "\": " + ((BooleanSetting) setting).isEnabled() + ",");
	                }
	                if (setting instanceof ModeSetting) {
	                	writer.write("\n    \"" + ((ModeSetting) setting).getName() + "\": \"" + ((ModeSetting) setting).getMode() + "\",");
	                }
	                if (setting instanceof NumberSetting) {
	                	writer.write("\n    \"" + ((NumberSetting) setting).getName() + "\": " + ((NumberSetting) setting).getValue() + ",");
	                }
	                if (setting instanceof ColorSetting) {
	                	ColorSetting colorSet = (ColorSetting)setting;
	                	writer.write("\n    \"" + colorSet.getName() + "\": \"" + colorSet.getHex() + "\",");
	                }
	                if (setting instanceof KeybindSetting) {
	                	writer.write("\n    \"keybind\": " + ((KeybindSetting) setting).getKey() + ",");
	                }
        		}
				writer.write("\n    \"description\": \"" + module.getDescription() + "\"");
                writer.write("\n}");
                writer.close();
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    

	@SuppressWarnings({ "resource", "unused" })
    public static void loadConfig() {
		for (Mod module : ModuleManager.INSTANCE.getModules()) {
			module.setEnabled(false);
		}
		File directory = new File(MinecraftClient.getInstance().runDirectory + "\\config\\sudo\\");
		String path = MinecraftClient.getInstance().runDirectory + "\\config\\sudo\\";
		
		if (!directory.exists()) {
			if (directory.mkdir()) {
				return;
			}
		}
		
		for (Mod module : ModuleManager.INSTANCE.getModules()) {
            try {            
            	String filePath = directory + File.separator + module.getName() + ".json";
                FileReader reader = new FileReader(filePath);
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
                module.setEnabled(jsonObject.get("enabled").getAsBoolean());
                module.setKey(jsonObject.get("keybind").getAsInt());
                
                for (Setting setting : module.getSetting()) {
                	if (setting instanceof KeybindSetting) {
                		((KeybindSetting) setting).setKey(jsonObject.get("keybind").getAsInt());
                	} else if (setting instanceof BooleanSetting) {
                        ((BooleanSetting) setting).setEnabled(jsonObject.get(((BooleanSetting) setting).getName()).getAsBoolean());
                    } else if (setting instanceof ModeSetting) {
                        ((ModeSetting) setting).setMode(jsonObject.get(((ModeSetting) setting).getName()).getAsString());
                        ((ModeSetting) setting).cycle();
                        ((ModeSetting) setting).cycleBack();
                    } else if (setting instanceof NumberSetting) {
                        ((NumberSetting) setting).setValue(jsonObject.get(((NumberSetting) setting).getName()).getAsDouble());
                    } else if (setting instanceof ColorSetting) {
                    	int[] color = ((ColorSetting)setting).hexToRgbInt(jsonObject.get(((ColorSetting) setting).getName()).getAsString());
                    	((ColorSetting) setting).setRGB(color[0], color[1], color[2], color[3]);
                    }
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
}