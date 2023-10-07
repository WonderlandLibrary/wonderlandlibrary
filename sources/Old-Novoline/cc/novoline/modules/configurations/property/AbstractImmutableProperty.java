package cc.novoline.modules.configurations.property;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * @author xDelsy
 */
public abstract class AbstractImmutableProperty<T> extends AbstractProperty<T> implements ImmutableProperty<T> {

    @Override
    public final void set(@Nullable T value) {
        super.set(value);
    }

}
