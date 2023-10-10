/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import org.yaml.snakeyaml.Yaml;
import tk.rektsky.module.Module;
import tk.rektsky.module.ModulesManager;
import tk.rektsky.module.impl.render.Notification;
import tk.rektsky.module.settings.Setting;
import tk.rektsky.utils.StreamUtils;
import tk.rektsky.utils.YamlUtil;
import tk.rektsky.utils.display.ColorUtil;
import tk.rektsky.utils.file.FileManager;

public class ConfigManager {
    public static void loadConfigFromSTR(String config) throws IOException {
        File file = new File("rektsky/settings.yml");
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(config.getBytes(StandardCharsets.UTF_8));
        fos.close();
        for (YamlUtil.ConfiguredModule module : YamlUtil.getModuleSetting()) {
            ModulesManager.loadModuleSetting(module);
        }
        FileManager.replaceAndSaveSettings();
    }

    public static void loadConfig(String name) {
        File conf_file = new File("rektsky/configs/", name + ".yml");
        if (!conf_file.exists()) {
            Notification.displayNotification(new Notification.PopupMessage("Config", "Config: " + name + " does not exist!", ColorUtil.NotificationColors.RED, 40));
        } else {
            try {
                FileInputStream fis = new FileInputStream(conf_file);
                String raw_data = new String(StreamUtils.readAllBytes(fis));
                ConfigManager.loadConfigFromSTR(raw_data);
                Notification.displayNotification(new Notification.PopupMessage("Config", "Config: " + name + " loaded successfully!", ColorUtil.NotificationColors.GREEN, 40));
            }
            catch (Exception ex) {
                Notification.displayNotification(new Notification.PopupMessage("Config", ex.getMessage(), ColorUtil.NotificationColors.RED, 40));
            }
        }
    }

    public static void saveConfig(String name) {
        name = name.replaceAll("\\W+", "");
        try {
            File conf_file = new File("rektsky/configs/", name + ".yml");
            FileOutputStream fos = new FileOutputStream(conf_file);
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
            fos.write(yaml.dump(data).getBytes(StandardCharsets.UTF_8));
            fos.close();
            Notification.displayNotification(new Notification.PopupMessage("Config", "Config: " + name + " saved successfully!", ColorUtil.NotificationColors.GREEN, 40));
        }
        catch (IOException ex) {
            Notification.displayNotification(new Notification.PopupMessage("Config", ex.getMessage(), ColorUtil.NotificationColors.RED, 40));
        }
    }
}

