package vestige.setting;

import lombok.Getter;

import java.util.function.Supplier;

@Getter
public abstract class AbstractSetting {

    private final String name;
    private Supplier<Boolean> visibility = () -> true;

    public AbstractSetting(String name) {
        this.name = name;
    }

    public AbstractSetting(String name, Supplier<Boolean> visibility) {
        this.name = name;
        this.visibility = visibility;
    }

}
