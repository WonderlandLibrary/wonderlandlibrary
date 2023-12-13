package best.azura.scripting.tables;

import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.values.Table;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClassTable extends Table {
    private final Class<?> klass;
    private final HashMap<String, Method[]> staticMethods = new HashMap<>();
    private final ArrayList<Method> methods = new ArrayList<>();
    private final ArrayList<Field> fields = new ArrayList<>();

    public ClassTable(final Class<?> klass) {
        this.klass = klass;

        final HashMap<String, ArrayList<Method>> tempMethods = new HashMap<>();
        for (Method method : klass.getDeclaredMethods()) {
            if (method.getName().equals("<init>") || method.getName().equals("<cinit>") || (method.getModifiers() & Modifier.STATIC) == Modifier.STATIC) {
                tempMethods.putIfAbsent(method.getName(), new ArrayList<>());
                tempMethods.get(method.getName()).add(method);
            } else {
                methods.add(method);
            }
        }
        for (String key : tempMethods.keySet()) {
            Method[] methods = tempMethods.get(key).toArray(new Method[0]);
            if (key.equals("<init>")) {
                this.staticMethods.put(klass.getSimpleName(), methods);
            } else if (key.equals("<cinit>")) {
                if (tempMethods.get("<cinit>").size() == 1) {
                    try {
                        tempMethods.get("<cinit>").get(0).invoke(null);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                this.staticMethods.put(key, methods);
                setValue(key, new Variable(new MethodFunction(methods, null)));
            }
        }
        for (Field field : klass.getDeclaredFields()) {
            if ((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) {
                setValue(field.getName(), new FieldVariable(field, null));
            } else {
                fields.add(field);
            }
        }
    }

    public ArrayList<Method> getMethods() {
        return methods;
    }

    public HashMap<String, Method[]> getStaticMethods() {
        return staticMethods;
    }

    public Class<?> getKlass() {
        return klass;
    }
}
