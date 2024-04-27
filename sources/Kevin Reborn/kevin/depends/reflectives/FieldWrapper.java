package kevin.depends.reflectives;

import java.lang.reflect.Field;

public class FieldWrapper<T> {
    private final Class<T> parent;
    private final Field field;
    private Object inst;
    private boolean access = false;

    public FieldWrapper(Class<T> parent, Field field, Object inst) {
        this.parent = parent;
        this.field = field;
        this.inst = inst;
    }



    public FieldWrapper(Class<T> parent, Field field) {
        this(parent, field, null);
    }
    public FieldWrapper(ClassWrapper<T> parent, Field field, Object inst) {
        this(parent.getClazz(), field, inst);
    }

    public FieldWrapper(ClassWrapper<T> parent, Field field) {
        this(parent, field, null);
    }

    public Object getInst() {
        return inst;
    }

    public FieldWrapper<T> setInst(Object inst) {
        this.inst = inst;
        return this;
    }

    public FieldWrapper<T> autoAccess() {
        access = true;
        return this;
    }

    public ClassWrapper<T> back() {
        return new ClassWrapper<>(getParent());
    }

    public Class<T> getParent() {
        return parent;
    }

    public Object getE() throws IllegalAccessException {
        boolean b = false;
        if (access) {
            b = field.isAccessible();
            field.setAccessible(true);
        }

        Object o = field.get(inst);

        if (access) {
            field.setAccessible(b);
        }

        return o;
    }

    public Object get() {
        try {
            return getE();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Field getField() {
        return field;
    }
}
