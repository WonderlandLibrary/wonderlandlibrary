package wtf.expensive.modules.settings.imp;

import wtf.expensive.modules.settings.Setting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MultiBoxSetting extends Setting {
    public List<BooleanOption> options;
    public int index;
    public float animation;

    public MultiBoxSetting(String name, BooleanOption... options) {
        super(name);
        this.options = List.of(options);
    }

    public boolean get(String name) {
        return Objects.requireNonNull(this.options.stream().filter((option) -> option.getName().equalsIgnoreCase(name)).findFirst().orElse(null)).get();
    }

    public void set(String name, boolean value) {
        Objects.requireNonNull(this.options.stream().filter((option) -> option.getName().equalsIgnoreCase(name)).findFirst().orElse(null)).set(value);
    }

    public List<BooleanOption> getToggled() {
        return this.options.stream().filter(BooleanOption::get).collect(Collectors.toList());
    }

    public String get() {
        List<String> includedOptions = new ArrayList<>();
        for (BooleanOption option : options) {
            if (option.get()) {
                includedOptions.add(option.getName());
            }
        }
        return String.join(", ", includedOptions);
    }

    public void set(int index, boolean value) {
        this.options.get(index).set(value);
    }

    public boolean get(int index) {
        return this.options.get(index).get();
    }

    public MultiBoxSetting setVisible(Supplier<Boolean> bool) {
        this.visible = bool;
        return this;
    }

    @Override
    public SettingType getType() {
        return SettingType.MULTI_BOX_SETTING;
    }
}
