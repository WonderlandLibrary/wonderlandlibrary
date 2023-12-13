package de.tired.base.module;


import de.tired.base.annotations.ModuleAnnotation;
import de.tired.Tired;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class    ModuleManager {

    private final List<Module> moduleList = new ArrayList<>();
    public static ArrayList<Module> sortedModList = new ArrayList<>();

    public ModuleManager() {
        final Reflections reflections = new Reflections("de.tired.base.module.implementation");
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(ModuleAnnotation.class);
        for (Class<?> aClass : classes) {
            try {
                final Module module = (Module) aClass.newInstance();
                    moduleList.add(module);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        sortedModList.addAll(moduleList);

    }

    public static <T extends Module> T getInstance(Class<T> tClass) {
        return Tired.INSTANCE.moduleManager.moduleBy(tClass);
    }

    public final ArrayList<Module> getModules(ModuleCategory category) {
        ArrayList<Module> modules = new ArrayList<>();
        for (Module m : this.moduleList) {
            if (m.getModuleCategory() == category)
                modules.add(m);
        }
        return modules;
    }

    public <T extends Module> T moduleBy(Class<T> tClass) {
        return (T) moduleList.stream().filter(mod -> mod.getClass().equals(tClass)).findFirst().orElse(null);
    }


    public Module findModuleByClass(Class clazz) {
        for (Module module : moduleList) {
            if (module.getClass().equals(clazz)) {
                return module;
            }
        }
        return null;
    }

    public Module moduleBy(String name) {
        for (Module module : moduleList) {
            if (module.getName().equalsIgnoreCase(name)) return module;
        }
        return null;
    }

    public List<Module> getModuleList() {
        return moduleList;
    }
}
