package xyz.cucumber.base.file.files;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.Session;
import xyz.cucumber.base.Client;
import xyz.cucumber.base.interf.altmanager.AltManager;
import xyz.cucumber.base.interf.altmanager.ut.AltManagerSession;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.utils.FileUtils;
import xyz.cucumber.base.utils.position.PositionUtils;

public class AccountsFile extends FileUtils {
	
	public AccountsFile() {
		super("Gothaj", "accounts.json");
	}

	public void save(AltManager altManager) {
		try {
			JsonObject json = new JsonObject();

			for (AltManagerSession m : altManager.sessions) {
			    JsonObject jsonMod = new JsonObject();
				jsonMod.addProperty("id", m.getSession().getPlayerID());
				jsonMod.addProperty("token", m.getSession().getToken());
				json.add(m.getSession().getUsername(), jsonMod);
			}

			PrintWriter save = new PrintWriter(new FileWriter(this.getFile()));
			save.println(prettyGson.toJson(json));
			save.close();
		} catch (Exception e) {
		}
	}

	public void load(AltManager altManager) {
		try {
			BufferedReader load = new BufferedReader(new FileReader(getFile()));
			JsonObject json = (JsonObject) jsonParser.parse(load);

			load.close();
			Iterator<Entry<String, JsonElement>> itr = json.entrySet().iterator();

			while (itr.hasNext()) {
				Entry<String, JsonElement> entry = itr.next();
				JsonObject module = (JsonObject) entry.getValue();
				
				altManager.sessions.add(new AltManagerSession(altManager, new Session(entry.getKey(),  module.get("id").getAsString(),module.get("token").getAsString(), "mojang")));
			}
		} catch (Exception e) {
		}
	}
}
