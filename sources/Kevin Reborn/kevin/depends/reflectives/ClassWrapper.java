package kevin.depends.reflectives;

/**
 * reflection tools
 * @param <T>
 */
public class ClassWrapper<T> {
    private final Class<T> clazz;

    public ClassWrapper(Class<T> clazz) {
        this.clazz = clazz;
    }

    public FieldWrapper<T> fieldE(String name) throws NoSuchFieldException {
        return new FieldWrapper<>(this, clazz.getField(name));
    }
    public FieldWrapper<T> fieldE(Object inst, String name) throws NoSuchFieldException {
        return new FieldWrapper<>(this, clazz.getField(name), inst);
    }

    public FieldWrapper<T> field(String name) {
        try {
            return fieldE(name);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
    public FieldWrapper<T> field(Object inst, String name) {
        try {
            return fieldE(inst, name);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public MethodWrapper<T> methodE(String name, Class<?>... paramTypes) throws NoSuchMethodException {
        return new MethodWrapper<>(this, clazz.getMethod(name, paramTypes));
    }

    public MethodWrapper<T> methodE(Object inst, String name, Class<?>... paramTypes) throws NoSuchMethodException {
        return new MethodWrapper<>(this, clazz.getMethod(name, paramTypes), inst);
    }

    public MethodWrapper<T> method(String name, Class<?>... paramTypes) {
        try {
            return methodE(name, paramTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public MethodWrapper<T> method(Object inst, String name, Class<?>... paramTypes) {
        try {
            return methodE(inst, name, paramTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public ConstructorWrapper<T> constructorE(Class<?>... paramTypes) throws NoSuchMethodException {
        return new ConstructorWrapper<>(this, clazz.getConstructor(paramTypes));
    }

    public ConstructorWrapper<T> constructorE(Object inst, Class<?>... paramTypes) throws NoSuchMethodException {
        return new ConstructorWrapper<>(this, clazz.getConstructor(paramTypes), inst);
    }

    public ConstructorWrapper<T> constructor(Class<?>... paramTypes) {
        try {
            return constructorE(paramTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public ConstructorWrapper<T> constructor(Object inst, Class<?>... paramTypes) {
        try {
            return constructorE(inst, paramTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public Class<T> getClazz() {
        return clazz;
    }
}
