package cc.novoline.modules.binds;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * @author xDelsy
 */
public final class MouseKeybind implements ModuleKeybind {

    /* fields */
    private int key;
    private long lastTime;

    /* constructors */
    private MouseKeybind(int key) {
        this.key = key;
    }

    @NonNull
    public static MouseKeybind of(int key) {
        return new MouseKeybind(key);
    }

    /* methods */
    public boolean click() {
        if (System.currentTimeMillis() > this.lastTime + 200) {
            this.lastTime = System.currentTimeMillis();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getKey() {
        return this.key;
    }

    //region Lombok
    public void setKey(int key) {
        this.key = key;
    }

    public long getLastTime() {
        return this.lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MouseKeybind that = (MouseKeybind) o;
        return this.key == that.key;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.key);
    }

    @Override
    public String toString() {
        return "MouseKeybind{" + "key=" + this.key + ", lastTime=" + this.lastTime + '}';
    }
    //endregion

}
