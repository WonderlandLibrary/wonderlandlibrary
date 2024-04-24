package Method.Client.module.Profiles;

import Method.Client.managers.FileManager;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.ModuleManager;
import Method.Client.utils.visual.ChatUtils;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

import static Method.Client.Main.setmgr;
import static Method.Client.module.ModuleManager.toggledModules;

public class Profiletem extends Module {
    public String name;

    public ArrayList<ModuleStored> StoredModules = new ArrayList<>();

    public Profiletem(String name) {
        super(name, Keyboard.KEY_NONE, Category.PROFILES, name);
        this.name = name;
    }

    @Override
    public void setsave() {
        // Remove all Stored
        this.StoredModules.clear();
        // Set stored to toggled
        saver(true);

        ChatUtils.message("Saved Enabled Mods in Profile: " + this.name);
        FileManager.saveData(FileManager.files[0]);
    }

    public void saver(boolean enabled) {
        ArrayList<Module> mods = new ArrayList<>();
        if (enabled)
            mods.addAll(toggledModules);
        else
            mods.addAll(ModuleManager.modules);

        for (Module toggledModule : mods)
            if (!toggledModule.getCategory().equals(Category.PROFILES) && !toggledModule.getCategory().equals(Category.ONSCREEN)) {
                ModuleStored settingStored = new ModuleStored();
                for (Setting setting : setmgr.getSettingsByMod(toggledModule)) {
                    settingStored.settings.add(new Setting(setting));
                }

                settingStored.name = toggledModule.getName();

                settingStored.keys = toggledModule.keys;
                settingStored.parent = this.name;
                if (enabled)
                    settingStored.toggled = true;
                else
                    settingStored.toggled = toggledModule.isToggled();
                settingStored.visible = toggledModule.visible;
                this.StoredModules.add(settingStored);
            }
    }

    @Override
    public void setAll() {
        // Remove all Stored
        this.StoredModules.clear();
        // Set stored to toggled
        saver(false);

        ChatUtils.message("Saved All Mods in Profile: " + this.name);
        FileManager.saveData(FileManager.files[0]);
    }

    @Override
    public void onEnable() {
        ArrayList<Module> remove = new ArrayList<>();
        toggledModules.forEach(module -> {
            if (!module.getCategory().equals(Category.PROFILES) && !module.getCategory().equals(Category.ONSCREEN)) {
                remove.add(module);
            }
        });
        remove.forEach(module -> {
            if (!module.getCategory().equals(Category.PROFILES) && !module.getCategory().equals(Category.ONSCREEN)) {
                module.setToggled(false);
            }
        });
        toggledModules.removeAll(remove);

        for (ModuleStored storedSetting : StoredModules) {
            for (Module module : ModuleManager.modules) {

                if (module.getName().equalsIgnoreCase(storedSetting.name)) {

                    module.setToggled(storedSetting.toggled);
                    module.visible = storedSetting.visible;
                    module.keys = storedSetting.keys;
                    for (Setting setting : setmgr.getSettingsByMod(module))
                        for (Setting setting1 : storedSetting.settings)
                            if (setting.getName().equals(setting1.getName())) {
                                setting.setall(setting1);
                            }
                }
            }
        }
        this.toggle();
    }


    public static class ModuleStored {
        public String name;
        public String parent;
        public Boolean visible;
        public Boolean toggled;
        public ArrayList<Integer> keys;
        public ArrayList<Setting> settings = new ArrayList<>();

        public void setKeys(String keys) {
            if (keys != null) {
                keys = keys.replaceAll("\\[", "");
                keys = keys.replaceAll("]", "");
                keys = keys.replaceAll(" ", "");
                String[] tryit = keys.split(",");
                ArrayList<Integer> key = new ArrayList<>();
                for (String s : tryit) {
                    key.add(Integer.valueOf(s));
                }
                this.keys = key;
            }
        }
    }


}


