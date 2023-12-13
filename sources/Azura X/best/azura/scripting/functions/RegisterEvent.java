package best.azura.scripting.functions;

import best.azura.client.impl.Client;
import best.azura.scripting.AzuraScript;
import best.azura.scripting.ScriptModule;
import best.azura.scripting.tables.ModuleTable;
import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.values.Function;

import java.util.ArrayList;

public class RegisterEvent extends Function {
    private final ScriptModule scriptModule;
    private final ModuleTable module;

    public RegisterEvent(final ScriptModule scriptModule, final ModuleTable module) {
        this.scriptModule = scriptModule;
        this.module = module;
    }

    @Override
    public Variable run(Variable... variables) {
        if (script instanceof AzuraScript) {
            scriptModule.getEvents().putIfAbsent(variables[0].toString(), new ArrayList<>());
            scriptModule.getEvents().get(variables[0].toString()).add((Function) variables[1].getValue());
        } else {
            System.out.println(script);
            new Exception().printStackTrace();
            Client.INSTANCE.getLogger().error("Script not instance of Azura Script! Aborting!");
        }
        return super.run(variables);
    }

    public ModuleTable getModule() {
        return module;
    }
}
