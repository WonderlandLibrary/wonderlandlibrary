package kevin.depends.reflectives;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodWrapper<T> {
    private final Class<T> parent;
    private final Method method;
    private Object inst;
    private boolean access;

    public MethodWrapper(Class<T> parent, Method method, Object inst) {
        this.parent = parent;
        this.method = method;
        this.inst = inst;
    }

    public MethodWrapper(Class<T> parent, Method method) {
        this(parent, method, null);
    }

    public MethodWrapper(ClassWrapper<T> parent, Method method) {
        this(parent, method, null);
    }

    public MethodWrapper(ClassWrapper<T> parent, Method method, Object inst) {
        this(parent.getClazz(), method, inst);
    }

    public Object invoke(Object... objects) {
        try {
            return method.invoke(inst, objects);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public MethodWrapper<T> handle(Object... objects) {
        try {
            method.invoke(inst, objects);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public MethodWrapper<T> setAccessible(boolean flag) {
        method.setAccessible(flag);
        return this;
    }

    public ClassWrapper<T> back() {
        return new ClassWrapper<>(parent);
    }

    public boolean mayException() {
        return method.getExceptionTypes().length != 0;
    }

    public Method getMethod() {
        return method;
    }

    public Object getInst() {
        return inst;
    }

    public MethodWrapper<T> setInst(Object inst) {
        this.inst = inst;
        return this;
    }

    public Class<T> getParent() {
        return parent;
    }
}
