package intent.AquaDev.aqua.config;

import de.Hero.settings.Setting;
import de.Hero.settings.Type;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.config.Config;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;
import intent.AquaDev.aqua.utils.FileUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class Config {
    private static final String FILE_ENDING = ".txt";
    private final String fileName;
    private boolean github = false;
    private boolean defaultSettings = false;

    public Config(String fileName) {
        this(fileName, false);
    }

    public Config(String fileName, boolean github) {
        this(fileName, github, false);
    }

    public Config(String fileName, boolean github, boolean defaultSettings) {
        this.fileName = fileName;
        this.github = github;
        this.defaultSettings = defaultSettings;
    }

    public boolean load() {
        File confDir = new File(FileUtil.DIRECTORY, this.defaultSettings ? "" : "configs/");
        if (!confDir.exists()) {
            confDir.mkdirs();
            return false;
        }
        File confFile = new File(confDir, this.fileName.endsWith(FILE_ENDING) ? this.fileName : this.fileName + FILE_ENDING);
        String configName = this.fileName;
        Config.sendChatMessageWithPrefix("Loaded Config : " + this.fileName);
        ArrayList<String> lines = FileUtil.readFile((File)confFile);
        if (this.github) {
            try {
                URLConnection urlConnection = new URL("https://raw.githubusercontent.com/LCAMODZ/FantaX-Configs/main/" + (this.fileName.endsWith(FILE_ENDING) ? this.fileName : this.fileName + FILE_ENDING)).openConnection();
                urlConnection.setConnectTimeout(10000);
                urlConnection.connect();
                try (BufferedReader bufferedReader = new BufferedReader((Reader)new InputStreamReader(urlConnection.getInputStream()));){
                    String text;
                    while ((text = bufferedReader.readLine()) != null) {
                        lines.add(text);
                    }
                }
            }
            catch (Exception exception) {}
        } else if (!confFile.exists()) {
            return false;
        }
        for (String string : lines) {
            String[] args = string.split(" ");
            if (!args[0].equalsIgnoreCase("MODULE")) continue;
            if (args[1].equalsIgnoreCase("TOGGLE")) {
                String modName = args[2];
                boolean val = Boolean.parseBoolean((String)args[3]);
                try {
                    Aqua.moduleManager.getModuleByName(modName).setState(val);
                }
                catch (Exception exception) {}
                continue;
            }
            if (!args[1].equalsIgnoreCase("SET")) continue;
            String settingName = args[2];
            try {
                Type settingType = Type.valueOf(args[3]);
                String value = args[4];
                switch (settingType) {
                    case BOOLEAN: {
                        Aqua.setmgr.getSetting(settingName).setState(Boolean.parseBoolean((String)value));
                    }
                    case NUMBER: {
                        Aqua.setmgr.getSetting(settingName).setCurrentNumber(Double.parseDouble((String)value));
                    }
                    case STRING: {
                        Aqua.setmgr.getSetting(settingName).setCurrentMode(value);
                    }
                    case COLOR: {
                        Aqua.setmgr.getSetting((String)settingName).color = Integer.parseInt((String)value);
                    }
                }
            }
            catch (Exception ignored) {}
        }
        return lines != null && !lines.isEmpty();
    }

    public boolean saveCurrent() {
        File confDir = new File(FileUtil.DIRECTORY, this.defaultSettings ? "" : "configs/");
        if (!confDir.exists()) {
            confDir.mkdirs();
        }
        File confFile = new File(confDir, this.fileName.endsWith(FILE_ENDING) ? this.fileName : this.fileName + FILE_ENDING);
        String configName = this.fileName;
        Config.sendChatMessageWithPrefix("Saved Config : " + this.fileName);
        if (!confFile.exists()) {
            try {
                confFile.createNewFile();
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        ArrayList lines = new ArrayList();
        for (Module module : Aqua.moduleManager.getModules()) {
            if (module.getCategory() == Category.Visual && !this.defaultSettings) continue;
            String TOGGLE = "MODULE TOGGLE " + module.getName() + " " + module.isToggled();
            lines.add((Object)TOGGLE);
            for (Setting setting : Aqua.setmgr.getSettings()) {
                if (setting.getModule() != module) continue;
                String SET = "MODULE SET " + setting.getName() + " " + setting.getType() + " " + setting.getValue();
                lines.add((Object)SET);
            }
        }
        FileUtil.writeFile((File)confFile, (List)lines);
        return true;
    }

    public String getConfigName() {
        return this.fileName.replaceAll(FILE_ENDING, "");
    }

    public static void sendChatMessageWithPrefix(String message) {
    }
}
