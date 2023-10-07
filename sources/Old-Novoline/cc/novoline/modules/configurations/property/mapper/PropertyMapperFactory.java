package cc.novoline.modules.configurations.property.mapper;

import cc.novoline.utils.java.Checks;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import ninja.leaping.configurate.objectmapping.ObjectMapperFactory;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.ExecutionException;

/**
 * @author xDelsy
 */
public final class PropertyMapperFactory implements ObjectMapperFactory {

    private static final PropertyMapperFactory INSTANCE = new PropertyMapperFactory();

    @NonNull
    public static ObjectMapperFactory getInstance() {
        return INSTANCE;
    }

    private final LoadingCache<Class<?>, PropertyMapper<?>> mapperCache = CacheBuilder.newBuilder().weakKeys()
            .maximumSize(500).build(new CacheLoader<Class<?>, PropertyMapper<?>>() {

                @Override
                public PropertyMapper<?> load(Class<?> key) throws Exception {
                    return new PropertyMapper<>(key);
                }
            });

    @Override
    @SuppressWarnings("unchecked")
    @NonNull
    public <T> PropertyMapper<T> getMapper(@NonNull Class<T> type) throws ObjectMappingException {
        Checks.notNull(type, "type");

        try {
            return (PropertyMapper<T>) this.mapperCache.get(type);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof ObjectMappingException) {
                throw (ObjectMappingException) e.getCause();
            } else {
                throw new ObjectMappingException(e);
            }
        }
    }

    @Override
    @NonNull
    public String toString() {
        return "PropertyMapperFactory{}";
    }

}
