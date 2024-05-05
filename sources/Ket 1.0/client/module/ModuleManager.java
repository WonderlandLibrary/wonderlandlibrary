package client.module;

import client.Client;
import client.event.EventLink;
import client.event.Listener;
import client.event.Priority;
import client.event.impl.input.KeyboardInputEvent;
import org.atteo.classindex.ClassIndex;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class ModuleManager extends ArrayList<Module> {

    public void init() {
        ClassIndex.getSubclasses(Module.class, Module.class.getClassLoader()).forEach(clazz -> {
            if (!Modifier.isAbstract(clazz.getModifiers()) && (!clazz.isAnnotationPresent(DevelopmentFeature.class) || Client.DEVELOPMENT_SWITCH)) {
                try {
                    add(clazz.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Client.INSTANCE.getEventBus().register(this);
    }

    public List<Module> getAll() {
        return new ArrayList<>(this);
    }

    public <T extends Module> T get(final String name) {
        return (T) stream().filter(module -> module.getInfo().name().replace(" ", "").equalsIgnoreCase(name)).findAny().orElse(null);
    }

    public <T extends Module> T get(final Class<T> clazz) {
        return (T) stream().filter(module -> module.getClass() == clazz).findAny().orElse(null);
    }

    public List<Module> get(final Category category) {
        return stream().filter(module -> module.getInfo().category() == category).collect(Collectors.toList());
    }

    @EventLink(value = Priority.HIGHEST)
    public final Listener<KeyboardInputEvent> onKey = event -> stream().filter(module -> module.getKeyCode() == event.getKeyCode()).forEach(Module::toggle);

    @Override
    public boolean add(final Module module) {
        return super.add(module);
    }

    @Override
    public void add(final int i, final Module module) {
        super.add(i, module);
    }

    @Override
    public Module remove(final int i) {
        return super.remove(i);
    }

    @Override
    public boolean remove(final Object o) {
        return super.remove(o);
    }
}
