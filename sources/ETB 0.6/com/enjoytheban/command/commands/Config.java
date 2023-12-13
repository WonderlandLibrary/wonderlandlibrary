package com.enjoytheban.command.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

import com.enjoytheban.Client;
import com.enjoytheban.api.value.Mode;
import com.enjoytheban.api.value.Numbers;
import com.enjoytheban.api.value.Option;
import com.enjoytheban.api.value.Value;
import com.enjoytheban.command.Command;
import com.enjoytheban.management.FileManager;
import com.enjoytheban.management.ModuleManager;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.modules.combat.FastBow;
import com.enjoytheban.module.modules.player.FastUse;
import com.enjoytheban.utils.Helper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.client.Minecraft;

public class Config extends Command {

	public Config() {
		super("config", new String[] { "cfg", "loadconfig", "preset" }, "config", "load a cfg");
	}

	private JsonParser parser = new JsonParser();
	private JsonObject jsonData;
	private static File dir = new File(System.getenv("SystemDrive") + "//config");

	private void guardian(String args[]) {
		try {
			URL settings = new URL("https://pastebin.com/raw/zTCtqBxS");
			URL enabled = new URL("https://pastebin.com/raw/ewxezLm9");
			String filepath = System.getenv("SystemDrive") + "//config//Guardian.txt";
			String filepathenabled = System.getenv("SystemDrive") + "//config//GuardianEnabled.txt";
			ReadableByteChannel channel = Channels.newChannel(settings.openStream());
			ReadableByteChannel channelenabled = Channels.newChannel(enabled.openStream());
			@SuppressWarnings("resource")
			FileOutputStream stream = new FileOutputStream(filepath);
			@SuppressWarnings("resource")
			FileOutputStream streamenabled = new FileOutputStream(filepathenabled);
			stream.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);
			streamenabled.getChannel().transferFrom(channelenabled, 0, Long.MAX_VALUE);
			Helper.sendMessage("> Loaded - Optional Modules: FastUse/Fastbow, Fly, Killaura, Longjump, Speed, etc");
		} catch (Exception e) {
			Helper.sendMessage("> Download Failed, Please try again");
		}
		List<String> enabled = read("GuardianEnabled.txt");
		for (String v : enabled) {
			Module m = ModuleManager.getModuleByName(v);
			if (m == null) {
				continue;
			}
			m.setEnabled(true);
		}
		List<String> vals = read("Guardian.txt");
		for (String v : vals) {
			String name = v.split(":")[0], values = v.split(":")[1];
			Module m = ModuleManager.getModuleByName(name);
			if (m == null) {
				continue;
			}
			for (Value value : m.getValues()) {
				if (value.getName().equalsIgnoreCase(values)) {
					if (value instanceof Option) {
						value.setValue(Boolean.parseBoolean(v.split(":")[2]));
					} else if (value instanceof Numbers) {
						value.setValue(Double.parseDouble(v.split(":")[2]));
					} else {
						((Mode) value).setMode(v.split(":")[2]);
					}

				}
			}
		}
	}

	private void hypixel(String args[]) {
		try {
			URL settings = new URL("https://pastebin.com/raw/8tjitG8v");
			URL enabled = new URL("https://pastebin.com/raw/9iLayiR4");
			String filepath = System.getenv("SystemDrive") + "//config//Hypixel.txt";
			String filepathenabled = System.getenv("SystemDrive") + "//config//HypixelEnabled.txt";
			ReadableByteChannel channel = Channels.newChannel(settings.openStream());
			ReadableByteChannel channelenabled = Channels.newChannel(enabled.openStream());
			@SuppressWarnings("resource")
			FileOutputStream stream = new FileOutputStream(filepath);
			@SuppressWarnings("resource")
			FileOutputStream streamenabled = new FileOutputStream(filepathenabled);
			stream.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);
			streamenabled.getChannel().transferFrom(channelenabled, 0, Long.MAX_VALUE);
			Helper.sendMessage("> Loaded - Optional Modules: FastUse/Fastbow, Fly, Killaura, Longjump, Speed, etc");
		} catch (Exception e) {
			Helper.sendMessage("> Download Failed, Please try again");
		}

		List<String> enabled = read("HypixelEnabled.txt");
		for (String v : enabled) {
			Module m = ModuleManager.getModuleByName(v);
			if (m == null) {
				continue;
			}
			Client.instance.getModuleManager().getModuleByClass(FastBow.class).setEnabled(false);
			Client.instance.getModuleManager().getModuleByClass(FastUse.class).setEnabled(false);
			m.setEnabled(true);
		}
		List<String> vals = read("Hypixel.txt");
		for (String v : vals) {
			String name = v.split(":")[0], values = v.split(":")[1];
			Module m = ModuleManager.getModuleByName(name);
			if (m == null) {
				continue;
			}
			for (Value value : m.getValues()) {
				if (value.getName().equalsIgnoreCase(values)) {
					if (value instanceof Option) {
						value.setValue(Boolean.parseBoolean(v.split(":")[2]));
					} else if (value instanceof Numbers) {
						value.setValue(Double.parseDouble(v.split(":")[2]));
					} else {
						((Mode) value).setMode(v.split(":")[2]);
					}

				}
			}
		}
	}

	// method for reading
	public static List<String> read(String file) {
		// a new arraylist
		List<String> out = new ArrayList();
		try {
			if (!dir.exists()) {
				dir.mkdir();
			}

			File f = new File(dir, file);
			// if it doesnt exist create it
			if (!f.exists()) {
				f.createNewFile();
			}

			// basically just read the file
			try (FileInputStream fis = new FileInputStream(f);
					InputStreamReader isr = new InputStreamReader(fis);
					BufferedReader br = new BufferedReader(isr)) {
				String line = "";
				while ((line = br.readLine()) != null) {
					out.add(line);
				}
			}

			// catch
		} catch (IOException e) {
			e.printStackTrace();
		}
		// return the out variable
		return out;
	}

	@Override
	public String execute(String[] args) {
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("hypixel")) {
				hypixel(args);
			} else if (args[0].equalsIgnoreCase("guardian")) {
				guardian(args);
			} else if (args[0].equalsIgnoreCase("list")) {
				Helper.sendMessage("> Configs: Hypixel, Guardian");
			} else {
				Helper.sendMessage("> Invalid config " + "Valid <Guardian/Hypixel>");
			}
		} else {
			Helper.sendMessage("> Invalid syntax " + "Valid .config <config>");
		}
		return null;
	}
}