package xyz.northclient.features.values;

import lombok.Getter;
import lombok.Setter;
import xyz.northclient.NorthSingleton;
import xyz.northclient.features.AbstractModule;
import xyz.northclient.features.Value;
import xyz.northclient.features.VisibleSupplier;
import xyz.northclient.features.modules.Sprint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModeValue extends Value<Mode> {
    @Getter
    private int selected = 0;
    @Getter
    private List<Mode> options = new ArrayList<>();
    @Getter
    private AbstractModule owner;

    private String name;

    private VisibleSupplier visibleSupplier;

    public ModeValue(String name, AbstractModule parent) {
        this.name = name;
        this.owner = parent;
    }

    public ModeValue(String name, AbstractModule parent, VisibleSupplier visibleSupplier) {
        this.name = name;
        this.owner = parent;
        this.visibleSupplier = visibleSupplier;
    }

    @Override
    public Mode get() {
        return options.get(selected);
    }

    public ModeValue add(Mode<?>... modes) {
        for(Mode m : modes) {
            m.setParent(this);
        }
        this.options.addAll(Arrays.asList(modes));
        return this;
    }
    public ModeValue setDefault(String default1) {
        this.set(getByName(default1));
        return this;
    }

    public Mode getByName(String name) {
        return this.options.stream().filter((mode) -> mode.getName().equalsIgnoreCase(name)).findFirst().get();
    }

    public boolean is(String name) {
        return this.options.get(selected).getName().equalsIgnoreCase(name);
    }

    @Override
    public void set(Mode value) {
        if(owner.isEnabled()) {
            NorthSingleton.INSTANCE.getEventBus().register(value);
        }
        NorthSingleton.INSTANCE.getEventBus().unregister(options.indexOf(value));
        this.selected = options.indexOf(value);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public VisibleSupplier isVisible() {
        return this.visibleSupplier == null ? () -> true : this.visibleSupplier;
    }
}
