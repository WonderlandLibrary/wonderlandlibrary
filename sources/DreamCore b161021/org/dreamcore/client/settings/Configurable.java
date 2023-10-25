package org.dreamcore.client.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Configurable {

    private final ArrayList<Setting> settingList = new ArrayList<>();

    public final void addSettings(Setting... options) {
        this.settingList.addAll(Arrays.asList(options));
    }

    public final List<Setting> getSettings() {
        return this.settingList;
    }
}
