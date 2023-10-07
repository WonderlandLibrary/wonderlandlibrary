package cc.novoline.modules.configurations.property;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * @author xDelsy
 */
public interface ImmutableProperty<Type> extends Property<Type> {

    @Override
	default void set(@Nullable Type value) {
        throw new UnsupportedOperationException("Tried to change immutable property");
    }

}
