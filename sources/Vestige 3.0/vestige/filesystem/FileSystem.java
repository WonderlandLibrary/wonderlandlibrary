package vestige.filesystem;

import net.minecraft.client.Minecraft;
import vestige.Vestige;
import vestige.module.Module;
import vestige.setting.AbstractSetting;
import vestige.setting.impl.BooleanSetting;
import vestige.setting.impl.DoubleSetting;
import vestige.setting.impl.IntegerSetting;
import vestige.setting.impl.ModeSetting;

import java.io.*;
import java.util.ArrayList;

public class FileSystem {

    private File vestigeDir;
    private File vestigeConfigDir;

    public FileSystem() {
        File mcDir = Minecraft.getMinecraft().mcDataDir;

        vestigeDir = new File(mcDir, "Vestige v3");

        if(!vestigeDir.exists()) {
            vestigeDir.mkdir();
        }

        vestigeConfigDir = new File(vestigeDir, "configs");

        if(!vestigeConfigDir.exists()) {
            vestigeConfigDir.mkdir();
        }
    }

    public void saveConfig(String configName) {
        configName = configName.toLowerCase();

        try {
            File configFile = new File(vestigeConfigDir, configName + ".txt");

            if(!configFile.exists()) {
                configFile.createNewFile();
            }

            PrintWriter writer = new PrintWriter(configFile);

            ArrayList<String> toWrite = new ArrayList<>();

            for(Module m : Vestige.instance.getModuleManager().modules) {
                toWrite.add("State:" + m.getName() + ":" + m.isEnabled());

                if(!m.getSettings().isEmpty()) {
                    for(AbstractSetting s : m.getSettings()) {
                        if(s instanceof BooleanSetting) {
                            BooleanSetting boolSetting = (BooleanSetting) s;

                            toWrite.add("Setting:" + m.getName() + ":" + boolSetting.getName() + ":" + boolSetting.isEnabled());
                        } else if(s instanceof ModeSetting) {
                            ModeSetting modeSetting = (ModeSetting) s;

                            toWrite.add("Setting:" + m.getName() + ":" + modeSetting.getName() + ":" + modeSetting.getMode());
                        } else if(s instanceof DoubleSetting) {
                            DoubleSetting doubleSetting = (DoubleSetting) s;

                            toWrite.add("Setting:" + m.getName() + ":" + doubleSetting.getName() + ":" + doubleSetting.getValue());
                        } else if(s instanceof IntegerSetting) {
                            IntegerSetting intSetting = (IntegerSetting) s;

                            toWrite.add("Setting:" + m.getName() + ":" + intSetting.getName() + ":" + intSetting.getValue());
                        }
                    }
                }
            }

            for(String s : toWrite) {
                writer.println(s);
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean loadConfig(String configName, boolean defaultConfig) {
        try {
            File configFile = new File(vestigeConfigDir, configName + ".txt");

            if(configFile.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(configFile));

                ArrayList<String> lines = new ArrayList<>();

                String line;

                while((line = reader.readLine()) != null) {
                    lines.add(line);
                }

                reader.close();

                for(String s : lines) {
                    String infos[] = s.split(":");

                    if(infos.length >= 3) {
                        String type = infos[0];
                        String moduleName = infos[1];

                        Module m = Vestige.instance.getModuleManager().getModuleByName(moduleName);

                        if(m != null) {
                            switch (type) {
                                case "State":
                                    if(defaultConfig) {
                                        m.setEnabledSilently(Boolean.parseBoolean(infos[2]));
                                    } else {
                                        m.setEnabled(Boolean.parseBoolean(infos[2]));
                                    }
                                    break;
                                case "Setting":
                                    AbstractSetting setting = m.getSettingByName(infos[2]);

                                    if(setting != null) {
                                        if(setting instanceof BooleanSetting) {
                                            BooleanSetting boolSetting = (BooleanSetting) setting;

                                            boolSetting.setEnabled(Boolean.parseBoolean(infos[3]));
                                        } else if(setting instanceof ModeSetting) {
                                            ModeSetting modeSetting = (ModeSetting) setting;

                                            modeSetting.setMode(infos[3]);
                                        } else if(setting instanceof DoubleSetting) {
                                            DoubleSetting doubleSetting = (DoubleSetting) setting;

                                            doubleSetting.setValue(Double.parseDouble(infos[3]));
                                        } else if(setting instanceof IntegerSetting) {
                                            IntegerSetting intSetting = (IntegerSetting) setting;

                                            intSetting.setValue(Integer.parseInt(infos[3]));
                                        }
                                    }
                                    break;
                            }
                        }
                    }
                }

                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void saveKeybinds() {
        try {
            File keybindsFile = new File(vestigeDir, "keybinds.txt");

            if(!keybindsFile.exists()) {
                keybindsFile.createNewFile();
            }

            PrintWriter writer = new PrintWriter(keybindsFile);

            ArrayList<String> toWrite = new ArrayList<>();

            for(Module m : Vestige.instance.getModuleManager().modules) {
                toWrite.add(m.getName() + ":" + m.getKey());
            }

            for(String s : toWrite) {
                writer.println(s);
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadKeybinds() {
        try {
            File keybindsFile = new File(vestigeDir, "keybinds.txt");

            if(keybindsFile.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(keybindsFile));

                ArrayList<String> lines = new ArrayList<>();

                String line;

                while((line = reader.readLine()) != null) {
                    lines.add(line);
                }

                reader.close();

                for(String s : lines) {
                    String infos[] = s.split(":");

                    if(infos.length == 2) {
                        String moduleName = infos[0];
                        int key = Integer.parseInt(infos[1]);

                        Module m = Vestige.instance.getModuleManager().getModuleByName(moduleName);

                        if(m != null) {
                            m.setKey(key);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveDefaultConfig() {
        saveConfig("default");
    }

    public void loadDefaultConfig() {
        loadConfig("default", true);
    }

}