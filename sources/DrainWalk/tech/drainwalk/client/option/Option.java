package tech.drainwalk.client.option;

import lombok.Getter;
import lombok.Setter;

import java.util.function.BooleanSupplier;

public abstract class Option<T> {
    @Getter
    private final String settingName;
    @Getter
    protected String settingDescription;
    @Getter
    @Setter
    private BooleanSupplier visible;
    @Setter
    @Getter
    private T value;

    public Option(String settingName, T value) {
        this.settingName = settingName;
        this.settingDescription = "Setting haven't description";
        this.value = value;
        this.visible = () -> true;
    }

    public abstract Option<T> addVisibleCondition(BooleanSupplier visible);
    public abstract Option<T> addSettingDescription(String settingDescription);
}
