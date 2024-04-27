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
    private final JSObject commandObject;
    private final HashMap events;

    public final void on(String string, JSObject jSObject) {
        ((Map)this.events).put(string, jSObject);
    }

    @Override
    public void execute(String[] stringArray) {
        try {
            JSObject jSObject = (JSObject)this.events.get("execute");
            if (jSObject != null) {
                jSObject.call((Object)this.commandObject, new Object[]{stringArray});
            }
        }
        catch (Throwable throwable) {
            ClientUtils.getLogger().error("[ScriptAPI] Exception in command '" + this.getCommand() + "'!", throwable);
        }
    }

    public ScriptCommand(JSObject jSObject) {
        Object object = jSObject.getMember("name");
        if (object == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
        Object object2 = ScriptUtils.convert((Object)jSObject.getMember("aliases"), String[].class);
        if (object2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<out kotlin.String>");
        }
        String[] stringArray = (String[])object2;
        super((String)object, Arrays.copyOf(stringArray, stringArray.length));
        this.commandObject = jSObject;
        this.events = new HashMap();
    }
}

