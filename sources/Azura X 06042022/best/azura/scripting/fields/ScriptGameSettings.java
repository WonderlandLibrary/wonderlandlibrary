package best.azura.scripting.fields;


import fr.ducouscous.csl.running.variable.values.Table;
import fr.ducouscous.mcscript.mc.GameSettingTable;
import fr.ducouscous.mcscript.mc.fields.Field;

public class ScriptGameSettings extends Field<Table> {
    GameSettingTable table = new GameSettingTable();

    @Override
    protected Table get() {
        return table;
    }

    @Override
    protected void set(Table table) {

    }
}
