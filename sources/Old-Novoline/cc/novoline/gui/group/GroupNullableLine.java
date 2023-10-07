package cc.novoline.gui.group;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.Function;

/**
 * @author xDelsy
 */
public class GroupNullableLine<T> extends GroupSupplierLine<T> {

    protected GroupNullableLine(@NonNull Function<T, String> stringFunction) {
        super(stringFunction);
    }

    @NonNull
    static <T> GroupNullableLine<T> of(@NonNull Function<T, String> stringFunction) {
        return new GroupNullableLine<>(stringFunction);
    }

}
