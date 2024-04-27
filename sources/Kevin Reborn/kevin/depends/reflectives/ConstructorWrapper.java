package kevin.depends.reflectives;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ConstructorWrapper<T> {
    private final Class<T> parent;
    private final Constructor<T> constructor;
    private Object inst;

    public ConstructorWrapper(Class<T> parent, Constructor<T> constructor, Object inst) {
        this.parent = parent;
        this.constructor = constructor;
        this.inst = inst;
    }

    public ConstructorWrapper(Class<T> parent, Constructor<T> constructor) {
        this(parent, constructor, null);
    }

    public ConstructorWrapper(ClassWrapper<T> parent, Constructor<T> constructor, Object inst) {
        this(parent.getClazz(), constructor, inst);
    }

    public ConstructorWrapper(ClassWrapper<T> parent, Constructor<T> constructor) {
        this(parent, constructor, null);
    }

    public T newInstance(Object... args) {
        try {
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public T newInstance() {
        try {
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getInst() {
        return inst;
    }

    public void setInst(Object inst) {
        this.inst = inst;
    }

    public Constructor<T> getConstructor() {
        return constructor;
    }

    public Class<T> getParent() {
        return parent;
    }
}
