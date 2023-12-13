package best.azura.scripting;

import best.azura.client.api.module.Module;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.scripting.event.EventTable;
import best.azura.scripting.event.NamedEvent;
import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.values.Function;

import java.util.ArrayList;
import java.util.HashMap;

public class ScriptModule extends Module {

    private final AzuraScript script;
    private final HashMap<String, ArrayList<Function>> events = new HashMap<>();
    
    public ScriptModule(AzuraScript script, String name, String description, int keyBind) {
        super(name, description, keyBind);
        this.script = script;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (events.containsKey("enable"))
            for (Function function : events.get("enable"))
                function.run();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (events.containsKey("disable"))
            for (Function function : events.get("disable"))
                function.run();
    }

    @EventHandler
    public void onEvent(final Event event) {
        if (!(event instanceof NamedEvent)) return;
        final NamedEvent named = (NamedEvent) event;
        if (events.containsKey(named.name())) {
            for (Function function : events.get(named.name())) {
                final EventTable eventTable = new EventTable(event);
                function.run(new Variable(eventTable));
                eventTable.post();
            }
        }
    }

    public HashMap<String, ArrayList<Function>> getEvents() {
        return events;
    }
}