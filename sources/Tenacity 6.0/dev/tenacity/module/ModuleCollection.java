// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module;

import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import dev.tenacity.module.impl.render.NotificationsMod;
import dev.tenacity.module.impl.render.ArrayListMod;
import java.util.List;
import java.util.HashMap;

public class ModuleCollection
{
    public static boolean reloadModules;
    private HashMap<Object, Module> modules;
    private final List<Class<? extends Module>> hiddenModules;
    
    public ModuleCollection() {
        this.modules = new HashMap<Object, Module>();
        this.hiddenModules = new ArrayList<Class<? extends Module>>((Collection<? extends Class<? extends Module>>)Arrays.asList(ArrayListMod.class, NotificationsMod.class));
    }
    
    public List<Class<? extends Module>> getHiddenModules() {
        return this.hiddenModules;
    }
    
    public List<Module> getModules() {
        return new ArrayList<Module>(this.modules.values());
    }
    
    public HashMap<Object, Module> getModuleMap() {
        return this.modules;
    }
    
    public void setModules(final HashMap<Object, Module> modules) {
        this.modules = modules;
    }
    
    public List<Module> getModulesInCategory(final Category c) {
        return this.modules.values().stream().filter(m -> m.getCategory() == c).collect((Collector<? super Module, ?, List<Module>>)Collectors.toList());
    }
    
    public Module get(final Class<? extends Module> mod) {
        return this.modules.get(mod);
    }
    
    public <T extends Module> T getModule(final Class<T> mod) {
        return (T)this.modules.get(mod);
    }
    
    public List<Module> getModulesThatContainText(final String text) {
        return this.getModules().stream().filter(m -> m.getName().toLowerCase().contains(text.toLowerCase())).collect((Collector<? super Object, ?, List<Module>>)Collectors.toList());
    }
    
    public Module getModuleByName(final String name) {
        return this.modules.values().stream().filter(m -> m.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
    
    public List<Module> getModulesContains(final String text) {
        return this.modules.values().stream().filter(m -> m.getName().toLowerCase().contains(text.toLowerCase())).collect((Collector<? super Module, ?, List<Module>>)Collectors.toList());
    }
    
    public final List<Module> getToggledModules() {
        return this.modules.values().stream().filter(Module::isEnabled).collect((Collector<? super Module, ?, List<Module>>)Collectors.toList());
    }
    
    public final List<Module> getArraylistModules(final ArrayListMod arraylistMod, final List<Module> modules) {
        return modules.stream().filter(module -> module.isEnabled() && (!arraylistMod.importantModules.isEnabled() || !module.getCategory().equals(Category.RENDER))).collect((Collector<? super Object, ?, List<Module>>)Collectors.toList());
    }
}
