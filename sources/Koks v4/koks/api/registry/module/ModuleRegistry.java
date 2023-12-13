package koks.api.registry.module;

import koks.Koks;
import koks.api.registry.Registry;
import koks.api.manager.value.ValueManager;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * @author kroko
 * @created on 21.01.2021 : 09:54
 */
public class ModuleRegistry implements Registry {

    private static final ArrayList<Module> MODULES = new ArrayList<Module>();

    @Override
    public void initialize() {
        final HashMap<? extends Registry, ArrayList<Class<Module>>> executors = new HashMap<>();
        final Reflections reflections = new Reflections("koks");
        reflections.getTypesAnnotatedWith(Module.Info.class).forEach(aClass -> {
            try {
                addModule((Module) aClass.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
        ValueManager.getInstance().setDefaults();

        MODULES.sort(Comparator.comparing(Module::getName));
        MODULES.removeIf(module -> module.getCategory().equals(Module.Category.DEBUG) && !Koks.getKoks().isDeveloperMode());
    }

    public static <T extends Module> T getModule(Class<T> clazz) {
        return (T) MODULES.stream().filter(module -> module.getClass().equals(clazz)).findAny().orElse(null);
    }

    public static <T extends Module> T getModule(String name) {
        return (T) MODULES.stream().filter(module -> module.getName().equalsIgnoreCase(name)).findAny().orElse(null);
    }

    public static void addModule(Module module) {
        MODULES.add(module);
    }

    public static ArrayList<Module> getModules() {
        return MODULES;
    }
}
