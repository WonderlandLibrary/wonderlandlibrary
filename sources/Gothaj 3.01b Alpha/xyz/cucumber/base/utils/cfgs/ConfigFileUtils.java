package xyz.cucumber.base.utils.cfgs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import xyz.cucumber.base.Client;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.feat.other.NotificationsModule;
import xyz.cucumber.base.module.feat.other.NotificationsModule.Notification;
import xyz.cucumber.base.module.feat.other.NotificationsModule.Type;
import xyz.cucumber.base.module.settings.BooleanSettings;
import xyz.cucumber.base.module.settings.ColorSettings;
import xyz.cucumber.base.module.settings.ModeSettings;
import xyz.cucumber.base.module.settings.ModuleSettings;
import xyz.cucumber.base.module.settings.NumberSettings;
import xyz.cucumber.base.module.settings.StringSettings;
import xyz.cucumber.base.utils.FileUtils;
import xyz.cucumber.base.utils.position.PositionUtils;

public class ConfigFileUtils {
	public static File directory = new File("Gothaj/configs");
	public static File file = new File(directory, "default.json");

	public static String getConfigDate(JsonObject obj) {
		try {
			
			return obj.get("last-update").getAsString();
		} catch (Exception e) {
			
		}
		return "";
	}
	public static String getConfigDate(File f) {
		try {
			BufferedReader load = new BufferedReader(new FileReader(f));
			JsonObject json = (JsonObject) FileUtils.jsonParser.parse(load);
			
			return json.get("last-update").getAsString();
		} catch (Exception e) {
			
		}
		return "";
	}
	
	public static void save(File f, boolean b) {

		if (!directory.exists()) {
			directory.mkdirs();
		}
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			JsonObject json = new JsonObject();
			LocalDateTime myDateObj = LocalDateTime.now();
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

			String formattedDate = myDateObj.format(myFormatObj);

			file = f;

			json.addProperty("last-update", formattedDate);
			for (Mod m : Client.INSTANCE.getModuleManager().getModules()) {
				JsonObject jsonMod = new JsonObject();
				jsonMod.addProperty("enabled", m.isEnabled());
				for (ModuleSettings settings : m.getSettings()) {
					if (settings instanceof ModeSettings) {
						jsonMod.addProperty(settings.getName(), ((ModeSettings) settings).getMode());
					}
					if (settings instanceof BooleanSettings) {
						jsonMod.addProperty(settings.getName(), ((BooleanSettings) settings).isEnabled());
					}
					if (settings instanceof StringSettings) {
						jsonMod.addProperty(settings.getName(), ((StringSettings) settings).getString());
					}
					if (settings instanceof NumberSettings) {
						jsonMod.addProperty(settings.getName(), ((NumberSettings) settings).getValue());
					}
					if (settings instanceof ColorSettings) {
						ColorSettings set = (ColorSettings) settings;
						JsonObject jsonColor = new JsonObject();
						jsonColor.addProperty("Main color", set.getMainColor());
						jsonColor.addProperty("Secondary color", set.getSecondaryColor());
						jsonColor.addProperty("Alpha", set.getAlpha());
						jsonColor.addProperty("Mode", set.getMode());

						jsonMod.add(settings.getName(), jsonColor);
					}

				}
				json.add(m.getName(), jsonMod);
			}

			PrintWriter save = new PrintWriter(new FileWriter(f));
			save.println(FileUtils.prettyGson.toJson(json));
			save.close();
			if(!b) return;
			NotificationsModule mod = (NotificationsModule) Client.INSTANCE.getModuleManager()
					.getModule(NotificationsModule.class);
			if (mod.isEnabled()) {
				mod.notifications.add(new Notification(f.getName().substring(0, f.getName().lastIndexOf(".")),
						"Config was successfully saved!", Type.ENABLED, new PositionUtils(0, 0, 0, 0, 1)));
			}

		} catch (Exception e) {
			if(!b) return;
			NotificationsModule mod = (NotificationsModule) Client.INSTANCE.getModuleManager()
					.getModule(NotificationsModule.class);
			if (mod.isEnabled()) {
				try {
					mod.notifications.add(new Notification(f.getName().substring(0, f.getName().lastIndexOf(".")),
							"Config failed to load!", Type.DISABLED, new PositionUtils(0, 0, 0, 0, 1)));
				} catch (Exception ex) {

				}
			}
		}
	}

	public static void load(File f, boolean b, boolean startup) {
		if (!directory.exists()) {
			directory.mkdirs();
		}
		
		if (!file.exists()) {
			save(f, false);
			return;
		}
		try {
			BufferedReader load = new BufferedReader(new FileReader(f));
			JsonObject json = (JsonObject) FileUtils.jsonParser.parse(load);
			file = f;
			load.close();
			Iterator<Entry<String, JsonElement>> itr = json.entrySet().iterator();

			while (itr.hasNext()) {
				Entry<String, JsonElement> entry = itr.next();

				Mod mod = Client.INSTANCE.getModuleManager().getModule(entry.getKey());
				if (mod == null) {
					continue;
				}

				JsonObject module = (JsonObject) entry.getValue();

				if (module.get("enabled").getAsBoolean() && !mod.isEnabled()) {
					mod.toggle();
				}
				if (!module.get("enabled").getAsBoolean() && mod.isEnabled()) {
					mod.toggle();
				}
				for (ModuleSettings settings : mod.getSettings()) {
					JsonElement s = module.get(settings.getName());
					if (s == null) {
						continue;
					}
					if (settings instanceof ModeSettings) {
						((ModeSettings) settings).setMode(s.getAsString());
					}
					if (settings instanceof BooleanSettings) {
						((BooleanSettings) settings).setEnabled(s.getAsBoolean());
					}
					if (settings instanceof StringSettings) {
						((StringSettings) settings).setString(s.getAsString());
					}
					if (settings instanceof NumberSettings) {
						((NumberSettings) settings).setValue(s.getAsDouble());
					}
					if (settings instanceof ColorSettings) {
						ColorSettings set = (ColorSettings) settings;

						JsonObject obj = (JsonObject) s;

						set.setMainColor(obj.get("Main color").getAsInt());
						set.setSecondaryColor(obj.get("Secondary color").getAsInt());
						set.setAlpha(obj.get("Alpha").getAsInt());
						set.setMode(obj.get("Mode").getAsString());
					}
				}

			}

			NotificationsModule mod = (NotificationsModule) Client.INSTANCE.getModuleManager()
					.getModule(NotificationsModule.class);
			if (mod.isEnabled() && b) {
				try {
					mod.notifications.add(new Notification(f.getName().substring(0, f.getName().lastIndexOf(".")),
							"Config was successfully loaded", Type.ENABLED, new PositionUtils(0, 0, 0, 0, 1)));
				} catch (Exception ex) {

				}
			}

		} catch (Exception e) {
			if(!b) return;
			NotificationsModule mod = (NotificationsModule) Client.INSTANCE.getModuleManager()
					.getModule(NotificationsModule.class);
			if (mod.isEnabled() && b) {
				try {
					mod.notifications.add(new Notification(f.getName().substring(0, f.getName().lastIndexOf(".")),
							"Config failed to load!", Type.DISABLED, new PositionUtils(0, 0, 0, 0, 1)));
				} catch (Exception ex) {

				}
			}
		}
	}
}
