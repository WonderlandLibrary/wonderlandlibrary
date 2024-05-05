package xyz.cucumber.base.file.files;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import xyz.cucumber.base.Client;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.settings.BooleanSettings;
import xyz.cucumber.base.module.settings.ColorSettings;
import xyz.cucumber.base.module.settings.ModeSettings;
import xyz.cucumber.base.module.settings.ModuleSettings;
import xyz.cucumber.base.module.settings.NumberSettings;
import xyz.cucumber.base.module.settings.StringSettings;
import xyz.cucumber.base.utils.FileUtils;

public class BindFile extends FileUtils {

	public BindFile() {
		super("Gothaj", "binds.json");
	}

	@Override
	public void save() {
		try {
			JsonObject json = new JsonObject();

			for (Mod m : Client.INSTANCE.getModuleManager().getModules()) {
				JsonObject jsonMod = new JsonObject();
				jsonMod.addProperty("key", m.getKey());
				json.add(m.getName(), jsonMod);
			}

			PrintWriter save = new PrintWriter(new FileWriter(this.getFile()));
			save.println(prettyGson.toJson(json));
			save.close();
		} catch (Exception e) {
		}
	}

	@Override
	public void load() {

		try {
			BufferedReader load = new BufferedReader(new FileReader(this.getFile()));
			JsonObject json = (JsonObject) jsonParser.parse(load);

			load.close();
			Iterator<Entry<String, JsonElement>> itr = json.entrySet().iterator();

			while (itr.hasNext()) {
				Entry<String, JsonElement> entry = itr.next();

				Mod mod = Client.INSTANCE.getModuleManager().getModule(entry.getKey());
				if (mod == null) {
					continue;
				}

				JsonObject module = (JsonObject) entry.getValue();

				mod.setKey(module.get("key").getAsInt());
			}

		} catch (Exception e) {
		}

	}
}
