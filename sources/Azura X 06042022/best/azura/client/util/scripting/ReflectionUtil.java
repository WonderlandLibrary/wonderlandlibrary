package best.azura.client.util.scripting;

import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.values.Boolean;
import fr.ducouscous.csl.running.variable.values.Number;
import fr.ducouscous.csl.running.variable.values.StringVar;
import fr.ducouscous.csl.running.variable.values.Table;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtil {
    public static String getDesc(Field field) {
        return field.getType().getName();
    }

    public static String getDesc(Method method) {
        StringBuilder out = new StringBuilder("(");

        for (Class<?> parameterType : method.getParameterTypes()) {
            out.append(parameterType.getName());
            out.append(',');
        }
        out.replace(out.length() - 1, out.length(), ")");
        out.append(method.getReturnType().getName());

        return out.toString();
    }

    public static Method findMethod(Method[] methods, Variable...variables) {
        for (Method method : methods) {
            if (variables.length == 0 && method.getParameterTypes().length == 0) return method;
            boolean gud = false;
            if (variables.length != method.getParameterCount()) continue;
            int i;
            for (i = 0; i < method.getParameterCount(); i++) {
                final Class<?> parameterType = method.getParameterTypes()[i];
                if (parameterType == java.lang.Boolean.class) {
                    if (variables[i].getValue() instanceof Boolean) continue;
                    else break;
                }
                if (parameterType.getName().equals("boolean")) {
                    if (variables[i].getValue() instanceof Boolean) continue;
                    else break;
                }
                if (parameterType.getSuperclass() == java.lang.Number.class) {
                    if (variables[i].getValue() instanceof Number) continue;
                    else break;
                }
                if (parameterType.getSuperclass() == null) {
                    if (variables[i].getValue() instanceof Number) continue;
                    else break;
                }
                if (parameterType == java.lang.Number.class) {
                    if (variables[i].getValue() instanceof Number) continue;
                    else break;
                }
                if (!(variables[i].getValue() instanceof Table)) break;
            }
            if (i == method.getParameterCount())
                return method;
        }
        return null;
    }
}
