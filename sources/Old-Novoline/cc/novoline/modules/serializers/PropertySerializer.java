package cc.novoline.modules.serializers;

import cc.novoline.modules.configurations.property.Property;
import cc.novoline.modules.configurations.property.object.ListProperty;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.List;

import static cc.novoline.modules.configurations.property.object.PropertyFactory.*;

/**
 * @author xDelsy
 */
public final class PropertySerializer implements TypeSerializer<Property<?>> {

    @Override
    public void serialize(@NonNull TypeToken<?> type, @Nullable Property<?> obj, @NonNull ConfigurationNode node) {
        if (obj == null || obj.get() == null) {
            node.setValue(null);
            return;
        }

        node.setValue(obj.get());
    }

    @Override
    @Nullable
    public Property<?> deserialize(@NonNull TypeToken<?> type,
                                   @NonNull ConfigurationNode node) throws ObjectMappingException {
        switch (node.getValueType()) {
            case LIST:
                return createList(
                        (Collection<ListProperty<?>>) node.getList(new TypeToken<List<ListProperty<?>>>() {
                        }));

            case SCALAR:
                final Object value = node.getValue();

                if (value instanceof CharSequence) {
                    return createString(value.toString());
                } else if (value instanceof Integer) {
                    return createInt((int) value);
                } else if (value instanceof Double) {
                    return createDouble((double) value);
                } else if (value instanceof Boolean) {
                    return createBoolean((boolean) value);
                } else if (value instanceof Float) {
                    return createFloat((float) value);
                } else if (value instanceof Long) {
                    return createLong((long) value);
                }

                break;

            case MAP:
                throw new ObjectMappingException("Unable to deserialize map property");
        }

        return null;
    }

}
