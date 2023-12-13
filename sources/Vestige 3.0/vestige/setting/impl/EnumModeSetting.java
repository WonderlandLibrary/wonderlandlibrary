package vestige.setting.impl;

import lombok.Getter;
import vestige.setting.AbstractSetting;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

@Getter
public class EnumModeSetting<T extends Enum> extends AbstractSetting {

    private int index;
    private List<T> list;

    public EnumModeSetting(String name, T t, T ... list) {
        super(name);
        this.list = Arrays.asList(list);
        this.setMode(t);
    }

    public EnumModeSetting(String name, Supplier<Boolean> visibility, T t, T ... list) {
        super(name, visibility);
        this.list = Arrays.asList(list);
        this.setMode(t);
    }

    public T getMode() {
        return list.get(index);
    }

    public void setMode(T mode) {
        this.index = list.indexOf(mode);
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
