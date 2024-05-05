package tech.atani.client.feature.module.storage;

import de.florianmichael.rclasses.storage.Storage;
import org.reflections.Reflections;
import tech.atani.client.listener.event.minecraft.input.KeyInputEvent;
import tech.atani.client.listener.handling.EventHandling;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class ModuleStorage extends Storage<Module> {

    private static ModuleStorage instance;

    @Override
    public void init() {
        EventHandling.getInstance().registerListener(this);
        final Reflections reflections = new Reflections("tech.atani");
        reflections.getTypesAnnotatedWith(ModuleData.class).forEach(aClass -> {
            try {
                this.add((Module) aClass.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }

    @Listen
    public void onKey(KeyInputEvent keyInputEvent) {
        this.getList()
                .stream()
                .filter(module -> module.getKey() == keyInputEvent.getKey())
                .forEach(Module::toggle);
    }

    @Override
    public <V extends Module> V getByClass(final Class<V> clazz) {
        final Module feature = this.getList().stream().filter(m -> m.getClass().equals(clazz)).findFirst().orElse(null);
        if (feature == null) return null;
        return clazz.cast(feature);
    }

    public final ArrayList<Module> getModules(Category category) {
        ArrayList<Module> modules = new ArrayList<>();
        for (Module m : this.getList()) {
            if (m.getCategory() == category)
                modules.add(m);
        }
        return modules;
    }

    public <T extends Module> T getModule(String name) {
        return (T) this.getList().stream().filter(module -> module.getName().equalsIgnoreCase(name)).findAny().orElse(null);
    }

    public static ModuleStorage getInstance() {
        return instance;
    }

    public static void setInstance(ModuleStorage instance) {
        ModuleStorage.instance = instance;
    }
}
