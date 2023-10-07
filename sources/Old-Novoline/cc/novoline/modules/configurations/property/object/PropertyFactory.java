package cc.novoline.modules.configurations.property.object;

import cc.novoline.modules.binds.*;
import cc.novoline.utils.java.Checks;
import java.util.Collection;
import java.util.List;
import org.jetbrains.annotations.*;

public final class PropertyFactory {

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull KeyBindProperty keyBinding(@NotNull ModuleKeybind keyBinding) {
        Checks.notNull(keyBinding, "Key binding");
        return new KeyBindProperty(keyBinding);
    }

    public static @NotNull <Subtype> ListProperty<Subtype> createList(@NotNull List<Subtype> values) {
        return ListProperty.of(values);
    }

    public static @NotNull <Subtype> ListProperty<Subtype> createList(@NotNull Collection<Subtype> values) {
        return ListProperty.of(values);
    }

    @SafeVarargs
    public static @NotNull <Subtype> ListProperty<Subtype> createList(@NotNull Subtype... values) {
        return ListProperty.of(values);
    }

    public static @NotNull <Subtype> ListProperty<Subtype> createList(@Nullable Subtype value) {
        return ListProperty.of(value);
    }

    public static @NotNull <Subtype> ListProperty<Subtype> createList(@Nullable Subtype value1,
                                                                      @Nullable Subtype value2) {
        return ListProperty.of(value1, value2);
    }

    public static @NotNull <Subtype> ListProperty<Subtype> createList(@Nullable Subtype value1,
                                                                      @Nullable Subtype value2,
                                                                      @Nullable Subtype value3) {
        return ListProperty.of(value1, value2, value3);
    }

    public static @NotNull BooleanProperty createBoolean(@Nullable Boolean value) {
        return BooleanProperty.of(value);
    }

    public static @NotNull BooleanProperty booleanTrue() {
        return BooleanProperty.of(true);
    }

    public static @NotNull BooleanProperty booleanFalse() {
        return BooleanProperty.of(false);
    }

    public static @NotNull BooleanProperty immutableBooleanFalse() {
        return BooleanProperty.alwaysFalse();
    }

    public static @NotNull BooleanProperty immutableBooleanTrue() {
        return BooleanProperty.alwaysTrue();
    }

    public static @NotNull DoubleProperty createDouble(@Nullable Double value) {
        return DoubleProperty.of(value);
    }

    public static @NotNull DoubleProperty createDouble() {
        return DoubleProperty.create();
    }

    public static @NotNull FloatProperty createFloat(@Nullable Float value) {
        return FloatProperty.of(value);
    }

    public static @NotNull FloatProperty createFloat() {
        return FloatProperty.create();
    }

    public static @NotNull IntProperty createInt(@Nullable Integer value) {
        return IntProperty.of(value);
    }

    public static @NotNull IntProperty createInt() {
        return IntProperty.create();
    }

    public static @NotNull LongProperty createLong(@Nullable Long value) {
        return LongProperty.of(value);
    }

    public static @NotNull LongProperty createLong() {
        return LongProperty.create();
    }

    public static @NotNull StringProperty createString(@Nullable String value) {
        return StringProperty.of(value);
    }

    public static @NotNull StringProperty createString() {
        return StringProperty.create();
    }

    public static @NotNull ColorProperty createColor(@Nullable Integer value) {
        return ColorProperty.of(value);
    }
}
