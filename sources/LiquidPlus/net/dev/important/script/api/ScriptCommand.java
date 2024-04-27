/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.script.api;

import java.util.HashMap;
import java.util.Map;
import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.ScriptUtils;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.modules.command.Command;
import net.dev.important.utils.ClientUtils;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001b\u0010\t\u001a\u00020\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00070\fH\u0016\u00a2\u0006\u0002\u0010\rJ\u0016\u0010\u000e\u001a\u00020\n2\u0006\u0010\u000f\u001a\u00020\u00072\u0006\u0010\u0010\u001a\u00020\u0003R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R*\u0010\u0005\u001a\u001e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00030\u0006j\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u0003`\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2={"Lnet/dev/important/script/api/ScriptCommand;", "Lnet/dev/important/modules/command/Command;", "commandObject", "Ljdk/nashorn/api/scripting/JSObject;", "(Ljdk/nashorn/api/scripting/JSObject;)V", "events", "Ljava/util/HashMap;", "", "Lkotlin/collections/HashMap;", "execute", "", "args", "", "([Ljava/lang/String;)V", "on", "eventName", "handler", "LiquidBounce"})
public final class ScriptCommand
extends Command {
    @NotNull
    private final JSObject commandObject;
    @NotNull
    private final HashMap<String, JSObject> events;

    public ScriptCommand(@NotNull JSObject commandObject) {
        Intrinsics.checkNotNullParameter(commandObject, "commandObject");
        Object object = commandObject.getMember("name");
        if (object == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.String");
        }
        Object object2 = ScriptUtils.convert(commandObject.getMember("aliases"), String[].class);
        if (object2 == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<kotlin.String>");
        }
        super((String)object, (String[])object2);
        this.commandObject = commandObject;
        this.events = new HashMap();
    }

    public final void on(@NotNull String eventName, @NotNull JSObject handler) {
        Intrinsics.checkNotNullParameter(eventName, "eventName");
        Intrinsics.checkNotNullParameter(handler, "handler");
        ((Map)this.events).put(eventName, handler);
    }

    @Override
    public void execute(@NotNull String[] args2) {
        Intrinsics.checkNotNullParameter(args2, "args");
        try {
            JSObject jSObject = this.events.get("execute");
            if (jSObject != null) {
                Object[] objectArray = new Object[]{args2};
                jSObject.call(this.commandObject, objectArray);
            }
        }
        catch (Throwable throwable) {
            ClientUtils.getLogger().error("[ScriptAPI] Exception in command '" + this.getCommand() + "'!", throwable);
        }
    }
}

