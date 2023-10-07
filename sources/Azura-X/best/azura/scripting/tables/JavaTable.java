package best.azura.scripting.tables;

import best.azura.scripting.functions.TypeFunction;
import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.values.Table;

public class JavaTable extends Table {

    public JavaTable() {
        setValue("type", new Variable(new TypeFunction()));
    }

}