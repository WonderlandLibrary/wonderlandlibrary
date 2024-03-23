package xyz.northclient.features.values;

import lombok.Getter;
import lombok.Setter;
import xyz.northclient.features.AbstractModule;
import xyz.northclient.features.VisibleSupplier;

public class Mode<T> {
    @Getter
    private String name;

    @Getter @Setter
    private ModeValue parent;


    @Getter
    private AbstractModule moduleParent;

    public Mode(String name, AbstractModule parent) {
        this.name = name;
        this.moduleParent = parent;
    }

}
