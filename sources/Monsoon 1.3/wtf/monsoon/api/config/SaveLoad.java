package wtf.monsoon.api.config;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import wtf.monsoon.Monsoon;
import wtf.monsoon.api.module.Module;
import wtf.monsoon.api.setting.Setting;
import wtf.monsoon.api.setting.impl.BooleanSetting;
import wtf.monsoon.api.setting.impl.ModeSetting;
import wtf.monsoon.api.setting.impl.NumberSetting;
import net.minecraft.client.Minecraft;


public class SaveLoad {

	public String configFileName;
	private File dir, configDir;
	private File dataFile;
	   
	public SaveLoad(String configFileName) {
		this.configFileName = configFileName;
		dir = new File(Minecraft.getMinecraft().mcDataDir, "Monsoon");
		configDir = new File(dir, "Configs");
		if(!dir.exists()) {
			dir.mkdir();
		}
		if(!configDir.exists()) {
			configDir.mkdir();
		}
		dataFile = new File(configDir, configFileName + ".txt");
		if(!dataFile.exists()) {
			try {
				dataFile.createNewFile();
			} catch (IOException e) {e.printStackTrace();}
		}
		
		this.load();
	}
	
	public void save() {
		ArrayList<String> toSave = new ArrayList<String>();
		
		for(Module mod : Monsoon.INSTANCE.manager.modules) {
			toSave.add("module:" + mod.getName() + ":" + mod.isEnabled() + ":" + mod.getKey());
		}
		
		for(Module mod : Monsoon.INSTANCE.manager.modules) {
			for(Setting setting : mod.settings) {
				
				if(setting instanceof BooleanSetting) {
					BooleanSetting bool = (BooleanSetting) setting;
					toSave.add("setting:" + mod.getName() + ":" + setting.name + ":" + bool.isEnabled());
				}
				
				if(setting instanceof NumberSetting) {
					NumberSetting numb = (NumberSetting) setting;
					toSave.add("setting:" + mod.getName() + ":" + setting.name + ":" + numb.getValue());
				}
				
				if(setting instanceof ModeSetting) {
					ModeSetting mode = (ModeSetting) setting;

					try {
						toSave.add("setting:" + mod.getName() + ":" + setting.name + ":" + mode.getMode());
					} catch(ArrayIndexOutOfBoundsException e) {
						Monsoon.sendMessage("Invalid mode");
						Monsoon.sendMessage("Could either be an old config, or someone wanted your game to crash.");
					}
				}
			}
		}
		toSave.add("COMMANDPREFIX:" + Monsoon.INSTANCE.commandManager.prefix);
		
		try {
			PrintWriter pw = new PrintWriter(this.dataFile);
			for(String str : toSave) {
				pw.println(str);
			}
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void load() {
		ArrayList<String> lines = new ArrayList<String>();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(this.dataFile));
			String line = reader.readLine();
			while(line != null) {
				lines.add(line);
				line = reader.readLine();
			}
			reader.close();
		} catch(Exception e) {
			e.printStackTrace();
			}
		
		for(String s : lines) {
			String[] args = s.split(":");
			if(s.toLowerCase().startsWith("module:")) {
				Module m = Monsoon.INSTANCE.manager.getModule(args[1]);
				if(m != null) {
					if(m.getName().equals("ClickGUI") && m.getName().equals("hudEditor"))
						m.setEnabledSilent(!Boolean.parseBoolean(args[2]));
					
					if(!m.getName().equals("ClickGUI") && !m.getName().equals("hudEditor"))
					m.setEnabledSilent(Boolean.parseBoolean(args[2]));
					m.setKey(Integer.parseInt(args[3]));
				}
			}else if(s.toLowerCase().startsWith("setting:")) {
				Module m =  Monsoon.INSTANCE.manager.getModule(args[1]);
				if(m != null) {
					for(Setting setting : m.settings) {
						if(setting.name.equalsIgnoreCase(args[2]) && setting != null) {
							if(setting instanceof BooleanSetting) {
								((BooleanSetting) setting).setEnabled(Boolean.parseBoolean(args[3]));
							}
							if(setting instanceof NumberSetting) {
								try {
									((NumberSetting)setting).setValue(Double.parseDouble(args[3]));
								} catch(ArrayIndexOutOfBoundsException e) {
									Monsoon.sendMessage("Invalid amount " + args[3]);
									Monsoon.sendMessage("Could either be an old config, or someone wanted your game to crash.");
								}
							}
							if(setting instanceof ModeSetting) {
								//System.out.println(args[3]);
								try {
									((ModeSetting) setting).setMode(args[3]);
								} catch(ArrayIndexOutOfBoundsException e) {
									Monsoon.sendMessage("Invalid mode " + args[3]);
									Monsoon.sendMessage("Could either be an old config, or someone wanted your game to crash.");
								}
							}
						}
					}
				}
			}else if(s.toLowerCase().startsWith("commandprefix:")) {
				Monsoon.INSTANCE.commandManager.setPrefix(args[1]);
			}
		}
	}
}