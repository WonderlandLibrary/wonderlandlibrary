package cc.novoline.modules.configurations.holder;

import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.ModuleManager.ModuleCreator;
import cc.novoline.modules.configurations.property.Property;
import cc.novoline.modules.configurations.property.object.BooleanProperty;
import cc.novoline.utils.java.Checks;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.BiFunction;

import static cc.novoline.modules.configurations.property.mapper.PropertyMapper.mapPropertyPath;

/**
 * @author xDelsy
 */
public final class CreatingModuleHolder<Module extends AbstractModule> extends ModuleHolder<Module> {

    /* fields */
    @NonNull
    private final ModuleManager moduleManager;
    @NonNull
    private final ModuleCreator<Module> creator;

    @NonNull
    private final Map<@NonNull String, @NonNull PropertyFieldData> fields;

    /* constructors */
    private CreatingModuleHolder(@NonNull ModuleManager moduleManager, @NonNull String name,
                                 @NonNull ModuleCreator<Module> creator) {
        super(name, creator.create(moduleManager));
        this.moduleManager = moduleManager;
        this.creator = creator;
        this.fields = collectFields();
    }

    @NonNull
    public static <Module extends AbstractModule> CreatingModuleHolder<Module> of(@NonNull ModuleManager moduleManager,
                                                                                  @NonNull String name,
                                                                                  @NonNull ModuleCreator<Module> creator) {
        Checks.notNull(moduleManager, "module manager");
        Checks.notBlank(name, "name");
        Checks.notNull(creator, "module creator");

        return new CreatingModuleHolder<>(moduleManager, name, creator);
    }

    /* methods */
    @NonNull
    private Map<@NonNull String, @NonNull PropertyFieldData> collectFields() {
        final Map<@NonNull String, @NonNull PropertyFieldData> fields = new Object2ObjectArrayMap<>();
        final Class<? extends AbstractModule> moduleClass = this.module.getClass();

        for (final Field field : AbstractModule.class.getDeclaredFields()) {
            collectField(fields, field, (s, field1) -> {
                if (s.equalsIgnoreCase("enabled")) {
                    return new EnabledPropertyFieldData(s, field1);
                } else {
                    return new PropertyFieldData(s, field1);
                }
            });
        }

        for (final Field field : moduleClass.getDeclaredFields()) {
            collectField(fields, field, PropertyFieldData::new);
        }

        return fields;
    }

    private void collectField(Map<@NonNull String, @NonNull PropertyFieldData> fields, @NonNull Field field,
                              @NonNull BiFunction<String, Field, PropertyFieldData> constructor) {
        final cc.novoline.modules.configurations.annotation.Property annotation = field
                .getAnnotation(cc.novoline.modules.configurations.annotation.Property.class);

        if (annotation != null) {
            field.setAccessible(true);

            final String path = mapPropertyPath(field, annotation);
            final PropertyFieldData data = constructor.apply(path, field);

            fields.put(path, data);
        }
    }

    @NonNull
    public Module createNew() {
        return this.creator.create(this.moduleManager);
    }

    @NonNull
    public Map<@NonNull String, @NonNull PropertyFieldData> getFields() {
        return this.fields;
    }

    public static class PropertyFieldData {

        @NonNull
        protected final String name;
        @NonNull
        protected final Field field;

        /* constructors */
        private PropertyFieldData(@NonNull String name, @NonNull Field field) {
            this.name = name;
            this.field = field;
        }

        /* methods */
        @SuppressWarnings({"unchecked", "rawtypes"})
        public void copyValue(@NonNull AbstractModule src, @NonNull AbstractModule dest,
                              boolean disable) throws IllegalAccessException {
            Checks.check(dest.getClass().isAssignableFrom(src.getClass()),
                    "cannot copy value of property from different modules");

            final Property srcProperty = (Property) this.field.get(src);
            final Property destProperty = (Property) this.field.get(dest);

            destProperty.set(srcProperty.get());
        }

    }

    public static class EnabledPropertyFieldData extends PropertyFieldData {

        private EnabledPropertyFieldData(@NonNull String name, @NonNull Field field) {
            super(name, field);
        }

        @Override
        public void copyValue(@NonNull AbstractModule src, @NonNull AbstractModule dest,
                              boolean disable) throws IllegalAccessException {
            Checks.check(dest.getClass().isAssignableFrom(src.getClass()),
                    "cannot copy value of property from different modules");

            final BooleanProperty srcProperty = (BooleanProperty) this.field.get(src);
            final BooleanProperty destProperty = (BooleanProperty) this.field.get(dest);

            dest.setEnabled(srcProperty.get());
        }

    }

}
