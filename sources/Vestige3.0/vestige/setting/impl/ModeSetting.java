package vestige.setting.impl;

import lombok.Getter;
import vestige.setting.AbstractSetting;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

@Getter
public class ModeSetting extends AbstractSetting {

    private int index;
    private List<String> list;

    public ModeSetting(String name, String t, String ... list) {
        super(name);
        this.list = Arrays.asList(list);
        this.setMode(t);
    }

    public ModeSetting(String name, Supplier<Boolean> visibility, String t, String ... list) {
        super(name, visibility);
        this.list = Arrays.asList(list);
        this.setMode(t);
    }

    public String getMode() {
        if(index >= list.size() || index < 0) {
            index = 0;
        }

        return list.get(index);
    }

    public void setMode(String mode) {
        this.index = list.indexOf(mode);
    }

    public boolean is(String mode) {
        if(index >= list.size() || index < 0) {
            index = 0;
        }

        return list.get(index).equals(mode);
    }

    public void increment() {
        if(index < list.size() - 1) {
            index++;
        } else {
            index = 0;
        }
    }

    public void decrement() {
        if(index > 0) {
            index--;
        } else {
            index = list.size() - 1;
        }
    }

}
