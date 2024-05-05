/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  jdk.nashorn.api.scripting.JSObject
 *  jdk.nashorn.api.scripting.ScriptUtils
 *  kotlin.TypeCastException
 */
package net.ccbluex.liquidbounce.script.api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.ScriptUtils;
import kotlin.TypeCastException;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.utils.ClientUtils;

public final class ScriptCommand
extends Command {
    private final HashMap<String, JSObject> events;
    private final JSObject commandObject;

    public final void on(String eventName, JSObject handler) {
        ((Map)this.events).put(eventName, handler);
    }

    @Override
    public void execute(String[] args) {
        try {
            JSObject jSObject = this.events.get("execute");
            if (jSObject != null) {
                jSObject.call((Object)this.commandObject, new Object[]{args});
            }
        }
        catch (Throwable throwable) {
            ClientUtils.getLogger().error("[ScriptAPI] Exception in command '" + this.getCommand() + "'!", throwable);
        }
    }

    public ScriptCommand(JSObject commandObject) {
        Object object = commandObject.getMember("name");
        if (object == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
        Object object2 = ScriptUtils.convert((Object)commandObject.getMember("aliases"), String[].class);
        if (object2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<out kotlin.String>");
        }
        String[] stringArray = (String[])object2;
        super((String)object, Arrays.copyOf(stringArray, stringArray.length));
        this.commandObject = commandObject;
        this.events = new HashMap();
    }
}

