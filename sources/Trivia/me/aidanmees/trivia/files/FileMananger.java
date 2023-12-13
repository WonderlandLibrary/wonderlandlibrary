package me.aidanmees.trivia.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.common.io.Files;
import com.google.gson.JsonObject;

import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.module.state.ModuleState;
import me.aidanmees.trivia.client.settings.ClientSettings;
import me.aidanmees.trivia.gui.custom.clickgui.ModuleWindow;
import me.aidanmees.trivia.module.Module;
import net.minecraft.client.Minecraft;

public class FileMananger {

	public JSONParser parser = new JSONParser();

	private Minecraft mc = Minecraft.getMinecraft();

	public File triviaDir = new File(mc.mcDataDir, "trivia");

	public File windowDataDir = new File(triviaDir, "windowData.json");

	public File modulesDir = new File(triviaDir, "keyBinds.json");

	public File friendsDir = new File(triviaDir, "friends.txt");

	public File settingsDir = new File(triviaDir, "settings.json");
	
	public File altsDir = new File(triviaDir, "alts.json");

	
	public File firstStartDir = new File(triviaDir, ".start");

	public boolean saveKeyBinds = true;

	public void copyFiles(File[] dir) throws IOException {
		for (File file : dir) {
			if (file.isDirectory()) {
				copyFiles(file.listFiles());
			} else {
				Files.copy(file, new File(file.getAbsolutePath().replaceAll("atlas", "trivia")));
			}
		}
	}

	public void load() throws IOException {
		if (!triviaDir.exists()) {
			triviaDir.mkdir();
		}
		
		if (!friendsDir.exists()) {
			friendsDir.createNewFile();
		}
		if (!modulesDir.exists()) {
			modulesDir.createNewFile();
		}
		if (!windowDataDir.exists()) {
			windowDataDir.createNewFile();
		}
		if (!settingsDir.exists()) {
			settingsDir.createNewFile();
		}
		if(!altsDir.exists()) {
			altsDir.createNewFile();
		}
		if(!altsDir.exists()) {
			altsDir.createNewFile();
		}
		if(!firstStartDir.exists()) {
			trivia.firstStart = true;
			firstStartDir.createNewFile();
			Files.write("subscribe to cheese donkeys", firstStartDir, Charset.forName("UTF-8"));
		}
	}

	public void deleteFile(File file) {
		if (file == modulesDir) {
			saveKeyBinds = false;
		}
		file.delete();
	}

	public void saveGUI(ArrayList<ModuleWindow> windows) {
		System.out.println("mc.mcDataDir = " + mc.mcDataDir.getAbsolutePath());
		System.out.println("Saving GUI...");

		JsonObject json = new JsonObject();

		for (ModuleWindow frame : windows) {
			JsonObject jsonFrame = new JsonObject();
			jsonFrame.addProperty("minimized", frame.isExtended());
			jsonFrame.addProperty("posX", frame.getX());
			jsonFrame.addProperty("posY", frame.getY());
			json.add(frame.getTitle(), jsonFrame);
		}
		try {

			FileWriter fw = new FileWriter(windowDataDir);
			fw.write(json.toString());
			fw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void loadGUI(ArrayList<ModuleWindow> windows) {

		Object obj = null;
		try {

			obj = parser.parse(new FileReader(windowDataDir));
			JSONObject json = (JSONObject) obj;
			for (Object s : json.keySet()) {
				System.out.println(s.toString());
				for (ModuleWindow frame : windows) {
					try {
						JSONObject data = (JSONObject) json.get(frame.getTitle());
						// System.out.println(data.toJSONString());
						frame.setExtended((Boolean) data.get("minimized"));
						double x = (Double) data.get("posX");
						double y = (Double) data.get("posY");
						frame.setX((int) x);
						frame.setY((int) y);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void saveModules() {
		if (saveKeyBinds == false) {
			return;
		}
		System.out.println("Saving modules...");
		JsonObject json = new JsonObject();
		json.addProperty("version", "2");
		for (Module module : trivia.getModules()) {
			JsonObject moduleJson = new JsonObject();
			module.createState().saveToJson(moduleJson);
			json.add(module.getName(), moduleJson);
		}
		try {
			FileWriter fw = new FileWriter(modulesDir);
			fw.write(json.toString());
			fw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadModules() {
		Object obj = null;
		try {
			obj = parser.parse(new FileReader(modulesDir));
			JSONObject json = (JSONObject) obj;
			if (json.get("version") == null) {
				saveModules();
			}
			for (Module module : trivia.getModules()) {
				try {
					if (module.dontToggleOnLoadModules()) {
						continue;
					}
					JSONObject moduleJson = (JSONObject) json.get(module.getName());
					if (moduleJson == null) {
						continue;
					}
					module.setState(ModuleState.createStateFromJson(module.getDefaultKeyboardKey(), moduleJson));
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}

			}
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void saveFriends() {
		friendsDir.delete();
		System.out.println("Saving friends...");
		try {
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(friendsDir)), true);
			for (String name : trivia.getFriendsMananger().getFriends()) {
				pw.println(name);
			}
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void loadFriends() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(friendsDir)));
			String line;
			while ((line = br.readLine()) != null) {
				trivia.getFriendsMananger().getFriends().add(line);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveSettings() {
		System.out.println("Saving settings...");
		ClientSettings.saveSettings();
	}

	public void loadSettings() {
		ClientSettings.loadSettings();
	}
	
	public void saveAlts() {
		trivia.getAltManager().saveAlts();
	}
	
	public void loadAlts() {
		trivia.getAltManager().loadAlts();
	}
	
}
