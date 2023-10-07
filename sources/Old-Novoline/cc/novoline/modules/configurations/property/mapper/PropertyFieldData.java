package cc.novoline.modules.configurations.property.mapper;

import cc.novoline.modules.configurations.property.Property;
import cc.novoline.modules.configurations.property.object.*;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ValueType;
import ninja.leaping.configurate.objectmapping.ObjectMapper;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

/**
 * @author xDelsy
 */
public final class PropertyFieldData extends ObjectMapper.SampleFieldData {

    public PropertyFieldData(Field field, String comment) {
        super(field, comment);
        // System.out.println("created new field data");
    }

    @Override
    protected void deserializeAndSet(Object container, ConfigurationNode node,
                                     TypeSerializer<?> serial) throws ObjectMappingException {
        try {
            if (node.getValueType() == ValueType.LIST) {
                final Property<?> propertyFromContainer = getPropertyFromContainer(this.field, container);
                final List<?> value = (List<?>) node.getValue();
                final ObjectArrayList<?> newValue = value != null ?
                        new ObjectArrayList<>(value) :
                        new ObjectArrayList<>();

                updatePropertyValue(propertyFromContainer, newValue);
            } else {
                final Object newVal = node.isVirtual() ? null : serial.deserialize(this.fieldType, node);

                if (newVal == null) {
                    final Object existingVal = this.field.get(container);
                    if (existingVal != null) serializeTo(container, node);
                } else {
                    set(container, this.field, newVal);
                }
            }
        } catch (IllegalAccessException e) {
            throw new ObjectMappingException("Unable to deserialize field " + this.field.getName(), e);
        }
    }

    private Property<?> getPropertyFromContainer(@NonNull Field field,
                                                 @NonNull Object container) throws IllegalAccessException {
        return (Property<?>) field.get(container);
    }

    private void set(@NonNull Object container, @NonNull Field field,
                     Object newVal) throws ObjectMappingException, IllegalAccessException {
        // System.out.println("set \"" + newVal + "\" to \"" + field.getName() + "\"");

        final Property<?> property = getPropertyFromContainer(field, container);
        final Object newValue = ((Property<?>) newVal).get();

        try {
            updatePropertyValue(property, newValue);
        } catch (ClassCastException e) {
            try {
                castAndUpdatePropertyValue(property, Objects.requireNonNull(newValue));
            } catch (Throwable t) {
                throw new ObjectMappingException("Cannot update value", t);
            }
        } catch (Throwable e) {
            throw new ObjectMappingException("Cannot update value", e);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void updatePropertyValue(@NonNull Property property, @Nullable Object newValue) {
        property.set(newValue);
    }

    @SuppressWarnings("rawtypes")
    private void castAndUpdatePropertyValue(@NonNull Property property, @NonNull Object newValue) throws Throwable {
        if (property instanceof IntProperty) {
            updatePropertyValue(property, ((Number) newValue).intValue());
        } else if (property instanceof DoubleProperty) {
            updatePropertyValue(property, ((Number) newValue).doubleValue());
        } else if (property instanceof FloatProperty) {
            updatePropertyValue(property, ((Number) newValue).floatValue());
        } else if (property instanceof LongProperty) {
            updatePropertyValue(property, ((Number) newValue).longValue());
        } else if (property instanceof StringProperty) {
            updatePropertyValue(property, newValue.toString());
        } else if (property instanceof ListProperty) {
            updatePropertyValue(property, new ObjectArrayList<>(new Object[]{newValue}));
        } else if (property instanceof BooleanProperty) {
            if (newValue instanceof Number) {
                updatePropertyValue(property, ((Number) newValue).shortValue() > 0);
                return;
            }

            throw new ObjectMappingException();
        }
    }

}
