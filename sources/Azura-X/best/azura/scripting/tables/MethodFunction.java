package best.azura.scripting.tables;

import best.azura.client.impl.Client;
import best.azura.client.util.scripting.ReflectionUtil;
import best.azura.client.util.scripting.VariableUtil;
import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.values.Function;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodFunction extends Function {
    private final Method[] methods;
    private final Object object;
    public MethodFunction(Method[] methods, Object object) {
        this.methods = methods;
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public Method[] getMethods() {
        return methods;
    }

    @Override
    public Variable run(Variable... variables) {
        try {
            Method method = ReflectionUtil.findMethod(methods, variables);
            if (method == null) {
                Client.INSTANCE.getLogger().error("What da fuc are you trying to do with the method ????? Aborting!");
                return new Variable(null);
            }
            Object[] arguments = VariableUtil.variablesToObjects(method, variables);
            Object obj = method.invoke(object, arguments);
            return new Variable(VariableUtil.objectToVariableValue(obj));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return new Variable(null);
    }
}
