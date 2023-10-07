package cc.novoline.modules.configurations.property.mapper;

import cc.novoline.modules.configurations.annotation.Property;
import ninja.leaping.configurate.objectmapping.ObjectMapper;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author xDelsy
 */
public final class PropertyMapper<T> extends ObjectMapper<T> {

    protected PropertyMapper(Class<T> clazz) throws ObjectMappingException {
        super(clazz);
    }

    @Override
    protected void collectFields(Map<String, FieldData> cachedFields,
                                 @NonNull Class<? super T> clazz) throws ObjectMappingException {
        boolean b = false;

        for (final Field field : clazz.getDeclaredFields()) {
            if (cc.novoline.modules.configurations.property.Property.class.isAssignableFrom(field.getType()) && field
                    .isAnnotationPresent(Property.class)) {
                b = true;

                field.setAccessible(true);
                final FieldData data = new PropertyFieldData(field, null);

                final Property property = field.getAnnotation(Property.class);
                final String path = mapPropertyPath(field, property);

                if (!cachedFields.containsKey(path)) {
                    cachedFields.put(path, data);
                }
            }
        }

        if (!b) super.collectFields(cachedFields, clazz);
    }

    @NonNull
    public static String mapPropertyPath(@NonNull Field field, @NonNull Property annotation) {
        return annotation.value();
    }

}
