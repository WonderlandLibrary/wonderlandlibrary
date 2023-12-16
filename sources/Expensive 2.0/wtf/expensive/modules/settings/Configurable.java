package wtf.expensive.modules.settings;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class Configurable {
    public ArrayList<Setting> settingList = new ArrayList<>();
    public final void addSettings(Setting... options) {
        settingList.addAll(Arrays.asList(options));
    }


}
