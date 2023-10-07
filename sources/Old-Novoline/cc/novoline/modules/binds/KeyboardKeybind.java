package cc.novoline.modules.binds;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * @author xDelsy
 */
public final class KeyboardKeybind implements ModuleKeybind {

    /* fields */
    private int key;

    /* constructors */
    private KeyboardKeybind(int key) {
        this.key = key;
    }

    @NonNull
    public static KeyboardKeybind of(int key) {
        return new KeyboardKeybind(key);
    }

    /* methods */
    @Override
    public int getKey() {
        return this.key;
    }

    //region Lombok
    public void setKey(int key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final KeyboardKeybind that = (KeyboardKeybind) o;
        return this.key == that.key;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.key);
    }

    @Override
    public String toString() {
        return "KeyboardKeybind{" + "key=" + this.key + '}';
    }
    //endregion

}
