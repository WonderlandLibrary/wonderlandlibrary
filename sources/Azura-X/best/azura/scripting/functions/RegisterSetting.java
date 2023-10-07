package best.azura.scripting.functions;

import best.azura.scripting.tables.ModuleTable;
import fr.ducouscous.csl.running.variable.values.Function;

public class RegisterSetting extends Function {
    private final ModuleTable module;

    public RegisterSetting(ModuleTable module) {
        this.module = module;
    }

    public ModuleTable getModule() {
        return module;
    }
}
