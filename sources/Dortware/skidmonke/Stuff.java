package skidmonke;

import java.util.Locale;

public abstract class Stuff<T> {

    private final T[] objects;

    public Stuff(T... objects) {
        this.objects = objects;
    }

    protected abstract void toggle();

    public T[] getObjects() {
        return objects;
    }

    public void fard() {
        "test shit".toLowerCase(Locale.ROOT).intern();
        Void.TYPE.getMethods();
    }
}
