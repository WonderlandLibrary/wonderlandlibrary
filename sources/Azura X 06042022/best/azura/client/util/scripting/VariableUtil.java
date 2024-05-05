package best.azura.client.util.scripting;

import best.azura.scripting.tables.InstanceTable;
import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.VariableValue;
import fr.ducouscous.csl.running.variable.values.Boolean;
import fr.ducouscous.csl.running.variable.values.Number;
import fr.ducouscous.csl.running.variable.values.StringVar;
import fr.ducouscous.csl.running.variable.values.Table;

import java.lang.reflect.Method;

public class VariableUtil {
    public static VariableValue objectToVariableValue(Object object) {
        if (object == null) return null;
        if (object instanceof java.lang.Number) {
            return new Number(((java.lang.Number) object).doubleValue());
        } else if (object instanceof String) {
            return new StringVar(object);
        } else if (object instanceof java.lang.Boolean) {
            return new Boolean(object.toString().equals("true"));
        }
        return null;
    }

    public static Object[] variablesToObjects(Method method, Variable...variables) {
        Object[] out = new Object[variables.length];

        for (int i = 0; i < variables.length; i++) {
            if (method.getParameterTypes()[i].isPrimitive()) {
                out[i] = variables[i].getValue().value();
                if (out[i] instanceof java.lang.Number) {
                    Object object;
                    switch (method.getParameterTypes()[i].getName()) {
                        case "double":
                        case "float":
                        case "int":
                        case "byte":
                        case "short":
                        case "long":
                            object = out[i];
                            break;
                        default:
                            object = null;
                            break;
                    }
                    out[i] = object;
                }
                continue;
            }
            if (variables[i].getValue() instanceof Number || variables[i].getValue() instanceof StringVar || variables[i].getValue() instanceof Boolean) {
                out[i] = method.getParameterTypes()[i].cast(variables[i].getValue().value());
                continue;
            }
            if (variables[i].getValue() instanceof InstanceTable) {
                out[i] = ((InstanceTable) variables[i].getValue()).getObject();
            }
        }

        return out;
    }
}
