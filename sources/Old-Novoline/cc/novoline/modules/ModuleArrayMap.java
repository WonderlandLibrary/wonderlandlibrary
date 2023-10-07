package cc.novoline.modules;

import cc.novoline.modules.configurations.holder.ModuleHolder;
import cc.novoline.modules.exceptions.UnregisteredModuleException;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author xDelsy
 */
public final class ModuleArrayMap extends Object2ObjectArrayMap<@NotNull String, @NotNull ModuleHolder<?>> {

    /* fields */
    public ModuleArrayMap() {
    }

    /* constructors */
    public ModuleArrayMap(Object2ObjectMap<@NotNull String, @NotNull ModuleHolder<?>> m) {
        super(m);
    }

    public ModuleArrayMap(int capacity) {
        super(capacity);
    }

    /* methods */
    @SuppressWarnings("unchecked")
    public <Module extends AbstractModule> @NotNull Module getByClass(@NotNull Class<? extends Module> moduleClass) {
        return (Module) entrySet().stream()
                .filter(entry -> entry.getValue().getModule().getClass() == moduleClass)
                .findFirst()
                .map(Map.Entry::getValue)
                .map(ModuleHolder::getModule)
                .orElseThrow(() -> new UnregisteredModuleException("Module " + moduleClass.getCanonicalName() + " is not registered!"));
    }

    public @NotNull List<AbstractModule> getByCategory(EnumModuleType enumModuleType) {
        return values().stream().filter(holder -> holder.getModule().getType().equals(enumModuleType)).map(ModuleHolder::getModule)
                .collect(Collectors.toCollection(ObjectArrayList::new));
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public <Module extends AbstractModule> ModuleHolder<Module> getHolderByClass(@NotNull Class<? extends Module> moduleClass) {
        //noinspection CastToConcreteClass
        return (ModuleHolder<Module>) entrySet().stream()
                .filter(entry -> entry.getValue().getModule().getClass() == moduleClass)
                .findFirst()
                .map(Map.Entry::getValue)
                .orElseThrow(() -> new UnregisteredModuleException("Module " + moduleClass.getCanonicalName() + " is not registered!"));
    }

    @SuppressWarnings("unchecked")
    public <Module extends AbstractModule> @Nullable Module getByName(@Nullable String name, boolean ignoreCase) {
        if(StringUtils.isBlank(name)) return null;
        Stream<ModuleHolder<?>> stream = values().stream();

        if(ignoreCase) {
            stream = stream.filter(holder -> holder.getModule().getName().equalsIgnoreCase(name));
        } else {
            stream = stream.filter(holder -> holder.getModule().getName().equals(name));
        }

        return (Module) stream.findAny().map(ModuleHolder::getModule).orElse(null);
    }

    public <Module extends AbstractModule> @Nullable Module getByNameIgnoreCase(@Nullable String name) {
        return getByName(name, true);
    }
}
