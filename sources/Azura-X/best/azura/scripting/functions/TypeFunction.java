package best.azura.scripting.functions;

import best.azura.scripting.tables.ClassTable;
import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.VariableValue;
import fr.ducouscous.csl.running.variable.values.Function;
import fr.ducouscous.csl.running.variable.values.Number;
import fr.ducouscous.csl.running.variable.values.StringVar;
import fr.ducouscous.csl.running.variable.values.Table;
import fr.ducouscous.mcscript.utils.FunctionLambda;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class TypeFunction extends Function {
    @Override
    public Variable run(Variable... variables) {
        try {
            final Class<?> clazz = Class.forName(variables[0].getValue().value().toString());
            return new Variable(new ClassTable(clazz));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}