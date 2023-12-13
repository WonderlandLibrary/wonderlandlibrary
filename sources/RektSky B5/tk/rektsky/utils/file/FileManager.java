/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;
import tk.rektsky.RektLogger;
import tk.rektsky.module.Module;
import tk.rektsky.module.ModulesManager;
import tk.rektsky.module.settings.Setting;
import tk.rektsky.utils.YamlUtil;

public class FileManager {
    public static final File FILES_DIR = new File("rektsky/");
    public static final File MODS_DIR = new File(FILES_DIR, "mods/");
    public static final String FILE_PATH = "rektsky/";
    public static final String CONFIG_PATH = "rektsky/configs/";
    public static final String SETTINGS_FILE = "rektsky/settings";
    public static final File ALTS_PATH = new File("rektsky/alts.yml");
    public static Map<String, Object> config;
    public static Map<String, String> altInfo;

    public static void init() {
        FILES_DIR.mkdirs();
        MODS_DIR.mkdirs();
        new File(CONFIG_PATH).mkdirs();
        try {
            if (!ALTS_PATH.exists()) {
                ALTS_PATH.createNewFile();
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        FileManager.generateConfig();
    }

    public static InputStream getFile(String pathWithoutSlash) {
        File file = new File(FILE_PATH + pathWithoutSlash);
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        }
        catch (FileNotFoundException e2) {
            try {
                inputStream = new FileInputStream(file);
            }
            catch (Exception ioException) {
                ioException.printStackTrace();
            }
        }
        return inputStream;
    }

    public static Object parseYAML(String pathWithoutSlash) {
        Yaml yaml = new Yaml();
        InputStream file = FileManager.getFile(pathWithoutSlash);
        return yaml.load(file);
    }

    public static void saveSettings() {
        FileManager.replaceAndSaveSettings();
        RektLogger.log("Saving Settings...");
    }

    private static void generateConfig() {
        FileManager.generateConfigYaml();
    }

    public static void getConfig() {
    }

    public static void replaceAndSaveSettings() {
        File file = new File("rektsky/settings.yml");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            Yaml yaml = new Yaml();
            HashMap data = new HashMap();
            for (Module m2 : ModulesManager.getModules()) {
                HashMap<String, Serializable> module = new HashMap<String, Serializable>();
                HashMap<String, Object> settings = new HashMap<String, Object>();
                module.put("enabled", Boolean.valueOf(m2.isToggled()));
                for (Setting s2 : m2.settings) {
                    settings.put(s2.name, s2.getValue());
                }
                module.put("settings", settings);
                module.put("key", Integer.valueOf(m2.keyCode));
                data.put(m2.name, module);
            }
            ((OutputStream)out).write(yaml.dump(data).getBytes(StandardCharsets.UTF_8));
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private static void generateConfigYaml() {
        File file = new File("rektsky/settings.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
                FileManager.replaceAndSaveSettings();
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
        } else {
            for (YamlUtil.ConfiguredModule module : YamlUtil.getModuleSetting()) {
                ModulesManager.loadModuleSetting(module);
            }
        }
    }
}

