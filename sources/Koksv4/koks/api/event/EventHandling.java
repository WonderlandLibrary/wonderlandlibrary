package koks.api.event;

import koks.api.registry.module.Module;
import koks.api.registry.module.ModuleRegistry;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class EventHandling {
    public static final ArrayList<Module> priorityModules = new ArrayList<>();

    private static final HashMap<Event.Priority, ArrayList<Module>> priorityList = new HashMap<>();
    private static final Reflections reflection = new Reflections("koks.module", Scanners.MethodsAnnotated);

    public static void init() {
        for (Event.Priority priority : Event.Priority.values()) {
            priorityList.put(priority, new ArrayList<>());
        }
        reflection.getMethodsAnnotatedWith(Event.Info.class).forEach(method -> {
            final Event.Info info = method.getAnnotation(Event.Info.class);
            final Class<?> moduleClass = method.getDeclaringClass();
            if (moduleClass.isAnnotationPresent(Module.Info.class)) {
                final Module.Info moduleInfo = moduleClass.getAnnotation(Module.Info.class);
                final Module module = ModuleRegistry.getModule(moduleInfo.name());
                if (module != null) {
                    priorityList.get(info.priority()).add(module);
                }
            }
        });

        priorityList.keySet().stream().sorted(Comparator.comparing(Enum::ordinal)).forEach(key -> {
            priorityModules.addAll(priorityList.get(key));
        });
    }
}
