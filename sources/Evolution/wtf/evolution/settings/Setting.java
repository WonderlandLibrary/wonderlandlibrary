package wtf.evolution.settings;

import java.util.function.Supplier;

public class Setting extends Config {

    public String name;
    public Supplier<Boolean> hidden = () -> false;
}
