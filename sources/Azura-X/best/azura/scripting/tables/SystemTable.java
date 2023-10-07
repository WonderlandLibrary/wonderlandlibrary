package best.azura.scripting.tables;

import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.values.Number;
import fr.ducouscous.csl.running.variable.values.StringVar;
import fr.ducouscous.csl.running.variable.values.Table;
import fr.ducouscous.mcscript.utils.FunctionLambda;

public class SystemTable extends Table {

    public SystemTable() {
        setValue("currentTimeMillis", new Variable(new FunctionLambda((args) -> new Variable(new Number(System.currentTimeMillis())))));
        setValue("nanoTime", new Variable(new FunctionLambda((args) -> new Variable(new Number(System.currentTimeMillis())))));
        setValue("lineSeparator", new Variable(new FunctionLambda((args) -> new Variable(new StringVar(System.currentTimeMillis())))));
    }

}