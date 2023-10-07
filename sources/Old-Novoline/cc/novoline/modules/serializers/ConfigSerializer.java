package cc.novoline.modules.serializers;

import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.configurations.holder.CreatingModuleHolder;
import cc.novoline.modules.configurations.property.mapper.PropertyMapper;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMapper;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * @author xDelsy
 */
public final class ConfigSerializer implements TypeSerializer<Object> {

    /* fields */
    @NonNull
    private final ModuleManager moduleManager;

    /* constructors */
    public ConfigSerializer(@NonNull ModuleManager moduleManager) {
        this.moduleManager = moduleManager;
    }

    /* methods */
    @Override
    @SuppressWarnings("unchecked")
    public void serialize(@NonNull TypeToken<?> type, @Nullable Object obj,
                          @NonNull ConfigurationNode node) throws ObjectMappingException {
        if (obj != null) {
            ((PropertyMapper<Object>) node.getOptions() // @off
                    .getObjectMapperFactory()
                    .getMapper(obj.getClass()))
                    .bind(obj)
                    .serialize(node); // @on
        } else {
            node.setValue(null);
        }
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Object deserialize(@NonNull TypeToken<?> type,
                              @NonNull ConfigurationNode node) throws ObjectMappingException {
        final PropertyMapper mapper = (PropertyMapper<?>) node.getOptions().getObjectMapperFactory()
                .getMapper(type.getRawType());
        final ObjectMapper<?>.BoundInstance instance;

        if (!AbstractModule.class.isAssignableFrom(mapper.getClazz())) {
            instance = mapper.bindToNew();
        } else {
            final CreatingModuleHolder<AbstractModule> holder;

            try {
                holder = (CreatingModuleHolder<AbstractModule>) this.moduleManager.getModuleManager()
                        .getHolderByClass((Class<AbstractModule>) mapper.getClazz());
            } catch (Throwable t) {
                throw new ObjectMappingException("No registered module is available for class " + mapper
                        .getClazz() + " but is required to construct new instances!", t);
            }

            instance = mapper.bind(holder.createNew());
        }

        return instance.populate(node);
    }

}