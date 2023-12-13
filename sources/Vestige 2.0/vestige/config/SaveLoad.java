package vestige.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import vestige.Vestige;
import vestige.api.module.Category;
import vestige.api.module.Module;
import vestige.api.setting.Setting;
import vestige.api.setting.impl.BooleanSetting;
import vestige.api.setting.impl.ModeSetting;
import vestige.api.setting.impl.NumberSetting;


public class SaveLoad {

	public String configFileName;
	private File dir, configDir;
	private File dataFile;
	   
	public SaveLoad(String configFileName) {
		this.configFileName = configFileName;
		dir = new File(Minecraft.getMinecraft().mcDataDir, "Vestige 2.0");
		configDir = new File(dir, "configs");
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
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void save() {
		ArrayList<String> toSave = new ArrayList<String>();
		if(this.dataFile.exists()) {
			dataFile.delete();
			try {
				dataFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		for(Module mod : Vestige.getInstance().getModuleManager().getModules()) {
			toSave.add("module:" + mod.getName() + ":" + mod.isEnabled() + ":" + mod.getKeybind().getKey());
		}
		
		for(Module mod : Vestige.getInstance().getModuleManager().getModules()) {
			for(Setting setting : mod.getSettings()) {
				
				if(setting instanceof BooleanSetting) {
					BooleanSetting bool = (BooleanSetting) setting;
					toSave.add("setting:" + mod.getName() + ":" + setting.getName() + ":" + bool.isEnabled());
				}
				
				if(setting instanceof NumberSetting) {
					NumberSetting numb = (NumberSetting) setting;
					toSave.add("setting:" + mod.getName() + ":" + setting.getName() + ":" + numb.getCurrentValue());
				}
				
				if(setting instanceof ModeSetting) {
					ModeSetting mode = (ModeSetting) setting;

					try {
						toSave.add("setting:" + mod.getName() + ":" + setting.getName() + ":" + mode.getMode());
					} catch(ArrayIndexOutOfBoundsException e) {
						
					}
				}
			}
		}
		
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
	
	public void load(boolean renderAndKeybinds) {
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
				Module m = Vestige.getInstance().getModuleManager().getModuleByName(args[1]);
				if(m != null) {
					if(!m.getName().equals("ClickGUI")) {
						if(renderAndKeybinds || m.getCategory() != Category.RENDER) {
							m.setEnabledSilently(Boolean.parseBoolean(args[2]));
						}
					}
					if(renderAndKeybinds || configFileName.equals("default")) {
						m.getKeybind().setKey(Integer.parseInt(args[3]));
					}
				}
			}else if(s.toLowerCase().startsWith("setting:")) {
				Module m = Vestige.getInstance().getModuleManager().getModuleByName(args[1]);
				if(m != null) {
					for(Setting setting : m.getSettings()) {
						if(setting.getName().equalsIgnoreCase(args[2]) && setting != null) {
							if(setting.getParent().getCategory() != Category.RENDER || renderAndKeybinds) {
								if(setting instanceof BooleanSetting) {
									((BooleanSetting) setting).setEnabled(Boolean.parseBoolean(args[3]));
								}
								if(setting instanceof NumberSetting) {
									try {
										((NumberSetting)setting).setCurrentValue(Double.parseDouble(args[3]));
									} catch(ArrayIndexOutOfBoundsException e) {
									}
								}
								if(setting instanceof ModeSetting) {
									try {
										((ModeSetting) setting).setMode(args[3]);
									} catch(ArrayIndexOutOfBoundsException e) {
										System.out.println("Error while loading config");
										((ModeSetting) setting).setIndex(0);;
									}
								}
							}
						}
					}
				}
			}
		}
		
		if(lines.isEmpty()) {
			dataFile.delete();
		}
	}
}