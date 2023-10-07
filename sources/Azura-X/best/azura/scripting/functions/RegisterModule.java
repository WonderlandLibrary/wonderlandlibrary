package best.azura.scripting.functions;

import best.azura.scripting.tables.ModuleTable;
import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.values.Function;
import fr.ducouscous.csl.running.variable.values.Number;

public class RegisterModule extends Function {

    @Override
    public Variable run(final Variable... variables) {
        int keyCode = variables.length <= 2 ? 0 : ((int) ((Number) variables[2].getValue()).getValue());
        return new Variable(new ModuleTable(
                script,
                variables[0].toString(),
                variables[1].toString(),
                keyCode
        ));
    }
}
