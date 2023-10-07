package cc.novoline.gui.screen.setting;

import cc.novoline.modules.AbstractModule;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public final class Manager {

    private static final List<Setting> settingList = new CopyOnWriteArrayList<>();

    public static void put(Setting setting) {
        settingList.add(setting);
    }

    public static List<Setting> getSettingsByMod(AbstractModule module) {
        return settingList.stream().filter(setting -> setting.getParentModule().equals(module))
                .collect(Collectors.toCollection(CopyOnWriteArrayList::new));
    }

    public static List<Setting> getSettingList() {
        return settingList;
    }

}
