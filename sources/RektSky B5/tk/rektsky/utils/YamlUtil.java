/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;
import tk.rektsky.RektLogger;
import tk.rektsky.module.Module;
import tk.rektsky.module.ModulesManager;
import tk.rektsky.module.settings.Setting;
import tk.rektsky.utils.InvalidConfigurationError;
import tk.rektsky.utils.file.FileManager;

public class YamlUtil {
    public static ArrayList<ConfiguredModule> getModuleSetting() {
        ArrayList<ConfiguredModule> output = new ArrayList<ConfiguredModule>();
        Map modules = (Map)new Yaml().load(FileManager.getFile("settings.yml"));
        for (String m2 : modules.keySet()) {
            try {
                output.add(new ConfiguredModule(m2, (Map)modules.get(m2)));
            }
            catch (InvalidConfigurationError invalidConfigurationError) {
                RektLogger.error("Could not get Configuration for ", m2, "!  Skipping...");
            }
        }
        return output;
    }

    public static class ConfiguredModule {
        private int keybind;
        private Module module;
        private Map<Setting, Object> settings = new HashMap<Setting, Object>();
        private boolean enabled;

        public ConfiguredModule(String moduleName, Map<String, Object> module) throws InvalidConfigurationError {
            if (!(module.get("key") instanceof Integer && module.get("enabled") instanceof Boolean && module.get("settings") instanceof Map)) {
                throw new InvalidConfigurationError();
            }
            this.module = ModulesManager.getModuleByName(moduleName);
            if (this.module == null) {
                throw new InvalidConfigurationError();
            }
            this.keybind = (Integer)module.get("key");
            this.enabled = (Boolean)module.get("enabled");
            LinkedHashMap setting = (LinkedHashMap)module.get("settings");
            block0: for (String key : setting.keySet()) {
                for (Setting s2 : this.module.settings) {
                    if (!key.equals(s2.name)) continue;
                    s2.setValue(setting.get(key));
                    continue block0;
                }
            }
        }

        public int getKeybind() {
            return this.keybind;
        }

        public Module getModule() {
            return this.module;
        }

        public Map<Setting, Object> getSettings() {
            return this.settings;
        }

        public boolean isEnabled() {
            return this.enabled;
        }
    }
}

