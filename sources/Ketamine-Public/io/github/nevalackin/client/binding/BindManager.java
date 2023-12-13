package io.github.nevalackin.client.binding;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface BindManager {

    default void register(Bindable bindable, Bind bind) {
        this.getBinds().put(bindable, bind);
    }

    Map<Bindable, Bind> getBinds();

    void save();

    void load();

    default List<String> getActiveBinds() {
        return this.getBinds().keySet().stream()
            .filter(Bindable::isActive)
            .map(Bindable::getName)
            .collect(Collectors.toList());
    }
}
