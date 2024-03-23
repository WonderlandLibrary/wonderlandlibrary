package xyz.northclient.features;

import lombok.Getter;
import xyz.northclient.features.modules.*;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class Modules {
    @Getter
    private final ConcurrentLinkedQueue<AbstractModule> modules = new ConcurrentLinkedQueue<>();

    public Modules() {
        modules.add(new Sprint());
        modules.add(new ClickUI());
        modules.add(new Animations());
        //modules.add(new KillAura());
        modules.add(new Velocity());
        modules.add(new Speed());
        modules.add(new Stealer());
        modules.add(new Manager());
        modules.add(new NoHurtCam());
        modules.add(new Chams());
        modules.add(new ESP2D());
        modules.add(new Scaffold());
        modules.add(new KillAura());
        modules.add(new TickBase());
        modules.add(new Timer());
        modules.add(new Ambience());
        modules.add(new Brightness());
        modules.add(new Nametags());
        modules.add(new Flight());
    }

    public List<AbstractModule> ByCategory(Category c) {
        return modules.stream().filter((mod) -> mod.getCategory() == c).collect(Collectors.toList());
    }

    public void finish() {
        for(AbstractModule m : modules) {
            m.reflectValues();
        }
    }

    public boolean isEnabled(String name) {
        return modules.stream().filter((mod) -> mod.getName().equalsIgnoreCase(name)).findFirst().get().isEnabled();
    }

    public AbstractModule get(String name) {
        return modules.stream().filter((mod) -> mod.getName().equalsIgnoreCase(name)).findFirst().get();
    }

    public void toggle(String name) {
        modules.stream().filter((mod) -> mod.getName().equalsIgnoreCase(name)).findFirst().get().toggle();
    }
}
