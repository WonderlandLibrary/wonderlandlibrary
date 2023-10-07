package best.azura.scripting.tables;

import best.azura.client.api.module.Module;
import best.azura.client.impl.Client;
import best.azura.scripting.AzuraScript;
import best.azura.scripting.ScriptModule;
import best.azura.scripting.functions.RegisterEvent;
import fr.ducouscous.csl.running.Script;
import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.values.Number;
import fr.ducouscous.csl.running.variable.values.StringVar;
import fr.ducouscous.csl.running.variable.values.Table;

public class ModuleTable extends Table {
    ScriptModule module;
    public ModuleTable(Script script, String name, String desc, int key) {
        setValue("name", new Variable(new StringVar(name)));
        setValue("description", new Variable(new StringVar(desc)));
        setValue("keyCode", new Variable(new Number(key)));
        if (!(script instanceof AzuraScript)) {
            Client.INSTANCE.getLogger().error("Script not instance of Azura Script! Aborting!");
            return;
        }
        this.module = new ScriptModule((AzuraScript) script, name, desc, key);
        final RegisterEvent registerEvent = new RegisterEvent(this.module, this);
        registerEvent.script = script;
        setValue("registerEvent", new Variable(registerEvent));
        Client.INSTANCE.getModuleManager().getRegistered().add(module);
    }
}
