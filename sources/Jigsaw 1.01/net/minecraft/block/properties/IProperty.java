package net.minecraft.block.properties;

import java.util.Collection;

import com.google.common.base.Optional;

public interface IProperty<T extends Comparable<T>>
{
    String getName();

    Collection<T> getAllowedValues();

    Class<T> getValueClass();

    Optional<T> parseValue(String value);

    /**
     * Get the name for the given value.
     */
    String getName(T value);
}
