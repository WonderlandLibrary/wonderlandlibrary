package markgg.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import markgg.RazeClient;
import markgg.modules.Module;
import markgg.modules.Module.Category;
import markgg.settings.BooleanSetting;
import markgg.settings.ModeSetting;
import markgg.settings.NumberSetting;
import markgg.settings.Setting;

public class ConfigSystem {

	private File configFile;
	private List<Module> modules;
	public static String error = "no error";

	public ConfigSystem(File configFile) {
		this.configFile = configFile;
		this.modules = new ArrayList<Module>();
	}

	public static void saveConfig(String filename) {
		if (filename == null || filename.isEmpty()) {
			filename = "default.json";
		} else if (!filename.endsWith(".json")) {
			filename += ".json";
		}
		try {
			JsonObject configData = new JsonObject();

			configData.addProperty("Version", RazeClient.INSTANCE.getVersion());

			for (Category category : Category.values()) {
				JsonArray moduleArray = new JsonArray();
				List<Module> modulesInCategory = RazeClient.getModuleManager().getModulesByCategory(category);
				for (Module module : modulesInCategory) {
					JsonObject moduleObject = new JsonObject();
					moduleObject.addProperty("Name", module.getName());
					moduleObject.addProperty("Enabled", module.isEnabled());
					for (Setting setting : module.getSettings()) {
						if(setting instanceof ModeSetting) {
							moduleObject.addProperty(setting.name, ((ModeSetting)setting).getMode());
						}
						if(setting instanceof NumberSetting) {
							moduleObject.addProperty(setting.name, ((NumberSetting)setting).getValue());
						}
						if(setting instanceof BooleanSetting) {
							moduleObject.addProperty(setting.name, ((BooleanSetting)setting).getValue());
						}
					}
					moduleArray.add(moduleObject);
				}
				configData.add(category.name(), moduleArray);
			}

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			FileWriter writer = new FileWriter("Raze/configs/" + filename);
			gson.toJson(configData, writer);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadConfig(String filename) {
		if (filename == null || filename.isEmpty()) {
			filename = "default.json";
		} else if (!filename.endsWith(".json")) {
			filename += ".json";
		}

		try {
			Gson gson = new Gson();
			BufferedReader reader = new BufferedReader(new FileReader("Raze/configs/" + filename));
			JsonObject configData = gson.fromJson(reader, JsonObject.class);
			reader.close();

			error = "no error";

			if (configData.has("Version") && configData.get("Version").getAsString().equals(RazeClient.INSTANCE.getVersion())) {
				for (Module module : RazeClient.getModuleManager().getModules().values()) {
					String categoryName = module.getCategory().name();
					JsonArray moduleArray = configData.getAsJsonArray(categoryName);
					for (JsonElement element : moduleArray) {
						JsonObject moduleObject = element.getAsJsonObject();
						if (module.name.equals(moduleObject.get("Name").getAsString())) {
							boolean enabled = moduleObject.get("Enabled").getAsBoolean();
							module.setToggled(enabled);
							for (Setting setting : module.settings) {
								if(setting instanceof BooleanSetting) {
									try {
										boolean bool = moduleObject.get(setting.name).getAsBoolean();
										BooleanSetting bool2 = (BooleanSetting) setting;
										bool2.setEnabled(bool);
									}catch (Exception e) {
										System.err.println(String.valueOf(e.getMessage()));
									}
								}
								if(setting instanceof ModeSetting) {
									try {
										String mode = moduleObject.get(setting.name).getAsString();
										ModeSetting mode2 = (ModeSetting) setting;
										if (!mode2.is(mode)) {
											int numModes = mode2.modes.size();
											int currentIndex = mode2.index;
											int targetIndex = mode2.modes.indexOf(mode);
											int numCycles = (targetIndex - currentIndex + numModes) % numModes;
											for (int i = 0; i < numCycles; i++) {
												mode2.cycle();
											}
										}
									}catch (Exception e) {
										System.err.println(String.valueOf(e.getMessage()));
									}
								}
								if(setting instanceof NumberSetting) {
									try {
										Double number = moduleObject.get(setting.name).getAsDouble();
										NumberSetting number1 = (NumberSetting) setting;
										number1.setValue(number);
									} catch (Exception e) {
										System.err.println(String.valueOf(e.getMessage()));
									}
								}
							}
						}
					}
				}
			} else {
				String version = configData.get("Version").getAsString();
				if (!version.equals(RazeClient.INSTANCE.getVersion())) {
					error = "Incompatible config version";
				}else {
					error = "no error";
				}
				RazeClient.print("[RAZE] - Error loading config: incompatible version.");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
