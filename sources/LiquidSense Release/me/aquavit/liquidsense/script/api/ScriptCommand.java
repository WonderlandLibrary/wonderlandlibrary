package me.aquavit.liquidsense.script.api;

import java.util.HashMap;

import jdk.nashorn.api.scripting.ScriptUtils;
import me.aquavit.liquidsense.command.Command;
import me.aquavit.liquidsense.utils.client.ClientUtils;
import jdk.nashorn.api.scripting.JSObject;

@SuppressWarnings({"unchecked", "unused"})
public class ScriptCommand extends Command {

    private final JSObject commandObject;
    private final HashMap<String, JSObject> events;

    public ScriptCommand(JSObject commandObject) {
        super(commandObject.getMember("name").toString(),
                ScriptUtils.convert(commandObject.getMember("aliases"), String[].class).toString());
        this.commandObject = commandObject;
        this.events = new HashMap<>();
    }

    public void on(String eventName, JSObject handler) {
        events.put(eventName, handler);
    }

    @Override
    public void execute(String[] args) {
        try {
            JSObject executeHandler = events.get("execute");
            if (executeHandler != null) {
                executeHandler.call(commandObject, (Object) args);
            }
        } catch (Throwable throwable) {
            ClientUtils.getLogger().error("[ScriptAPI] Exception in command '" + this.getCommand() + "'!", throwable);
        }
    }
}