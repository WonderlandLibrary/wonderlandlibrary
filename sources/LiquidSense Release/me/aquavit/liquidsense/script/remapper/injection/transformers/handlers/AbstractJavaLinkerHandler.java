package me.aquavit.liquidsense.script.remapper.injection.transformers.handlers;

import me.aquavit.liquidsense.script.remapper.Remapper;

import org.objectweb.asm.Type;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

public class AbstractJavaLinkerHandler {

    public String addMember(Class<?> clazz, String name, AccessibleObject accessibleObject) {
        if (clazz == null || name == null || accessibleObject == null) return null;
        if (!(accessibleObject instanceof Method)) return name;

        Class<?> currentClass = clazz;
        while(!currentClass.getName().equals("java.lang.Object")) {
            String remapped = Remapper.remapMethod(currentClass, name, Type.getMethodDescriptor((Method) accessibleObject));

            if(!remapped.equals(name)) return remapped;

            if(currentClass.getSuperclass() == null) break;

            currentClass = currentClass.getSuperclass();
        }

        return name;
    }

    public static String addMember(Class<?> clazz, String name) {
        if (clazz == null || name == null) return null;
        Class<?> currentClass = clazz;
        while(!currentClass.getName().equals("java.lang.Object")) {
            String remapped = Remapper.remapField(currentClass, name);

            if(!remapped.equals(name))
                return remapped;

            if(currentClass.getSuperclass() == null) break;

            currentClass = currentClass.getSuperclass();
        }

        return name;
    }

    public static String setPropertyGetter(Class<?> clazz, String name) {
        if (clazz == null || name == null) return null;
        Class<?> currentClass = clazz;
        while(!currentClass.getName().equals("java.lang.Object")) {
            String remapped = Remapper.remapField(currentClass, name);

            if(!remapped.equals(name))
                return remapped;

            if(currentClass.getSuperclass() == null) break;

            currentClass = currentClass.getSuperclass();
        }

        return name;
    }

}
