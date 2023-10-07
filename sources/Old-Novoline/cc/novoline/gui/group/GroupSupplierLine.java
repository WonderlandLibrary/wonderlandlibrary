package cc.novoline.gui.group;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.Function;

/**
 * @author xDelsy
 */
public class GroupSupplierLine<T> implements GroupLine<T> {

    /* fields */
    @NonNull
    private final Function<T, String> stringFunction;

    /* constructors */
    protected GroupSupplierLine(@NonNull Function<T, String> stringFunction) {
        this.stringFunction = stringFunction;
    }

    @NonNull
    static <T> GroupSupplierLine<T> of(@NonNull Function<T, String> stringFunction) {
        return new GroupSupplierLine<>(stringFunction);
    }

    /* methods */
    @Override
    public String getText(T t) {
        return this.stringFunction.apply(t);
    }

}
