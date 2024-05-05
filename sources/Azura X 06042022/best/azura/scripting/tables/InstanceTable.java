package best.azura.scripting.tables;

import fr.ducouscous.csl.running.variable.values.Table;

public class InstanceTable extends Table {
    private final Object object;
    public InstanceTable(final Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
